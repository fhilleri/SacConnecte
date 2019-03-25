package com.saint_gab.sacconnecte;

import android.content.Context;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import javax.security.auth.login.LoginException;

import static android.content.Context.MODE_PRIVATE;

public class Timetable {

    ArrayList<Subject> mSubjects;
    ArrayList<Lesson>[] mDays;
    ArrayList<Equipment> mEquipments;
    Context mContext;

    final String icalFileName = "calendar.ical";
    final String icalFilePath = "";

    public Timetable(Context context)
    {
        mContext = context;

        mDays = new ArrayList[5];
        for(int i=0; i<mDays.length; i++)
        {
            mDays[i] = new ArrayList<>();
        }
        mSubjects = new ArrayList<>();
        mEquipments = new ArrayList<>();

        //initializeTimetable();

        loadTimetable();
        //getExpectedEquipments();
    }

    public void addSubject(Subject newSubject)
    {
        mSubjects.add(newSubject);
        saveTimetable();
    }

    public void addLesson(Lesson lesson, int dayIndex)
    {
        mDays[dayIndex].add(lesson);
        saveTimetable();
    }

    private void sortLessons()
    {
        for (int i=0; i<mDays.length; i++)
        {
            ArrayList<Lesson> newLessonArray = new ArrayList<>();
            while(mDays[i].size() > 0)
            {
                int lowestTime = 99999, lowestTimeIndex = 0;
                for (int j=0; j<mDays[i].size(); j++)
                {

                    if (mDays[i].get(j).getStartTime().getTotalMinute() < lowestTime)
                    {
                        lowestTimeIndex = j;
                        lowestTime = mDays[i].get(j).getStartTime().getTotalMinute();
                    }
                }
                newLessonArray.add(mDays[i].get(lowestTimeIndex));
                mDays[i].remove(lowestTimeIndex);
            }
            mDays[i] = newLessonArray;
        }
    }

    public boolean equipmentExist(String id)
    {
        boolean exist = false;
        for (int i=0; i<mEquipments.size(); i++)
        {
            if (mEquipments.get(i).getId() == id) exist = true;
        }
        Log.i("Timetable", "equipmentExist: " + exist);
        return exist;
    }

    public boolean subjectExist(String name)
    {
        boolean exist = false;
        for (int i = 0; i < mSubjects.size(); i++)
        {
            String subjectName = mSubjects.get(i).getName();
            if (name.equals(subjectName)) exist = true;
        }
        Log.i("Timetable", "Subject : " + name + (exist ? " exist" : " does not exist"));
        return exist;
    }

    public void addEquipment(Equipment equipment)
    {
        mEquipments.add(equipment);
        saveTimetable();
    }

    public void deleteEquipment(int index)
    {
        Equipment equipment = mEquipments.get(index);
        for(int i=0; i<mSubjects.size(); i++)
        {
            mSubjects.get(i).removeEquipment(equipment);
        }
        mEquipments.remove(equipment);
        saveTimetable();
    }

    public void deleteSubject(int index)
    {
        Subject subject = mSubjects.get(index);
        ArrayList<Lesson> lessonsToDelete = subject.getLessons();
        for(int i=0; i<lessonsToDelete.size(); i++)
        {
            for (final ArrayList<Lesson> lessons: mDays)
            {
                if (lessons.contains(lessonsToDelete.get(i))) lessons.remove(lessonsToDelete.get(i));
            }
        }
        lessonsToDelete.clear();
        mSubjects.remove(subject);
        saveTimetable();
    }

    public void deleteLesson(int dayIndex ,int lessonIndex)
    {
        Lesson lesson = mDays[dayIndex].get(lessonIndex);
        lesson.getSubject().removeLesson(lesson);
        mDays[dayIndex].remove(lessonIndex);
        saveTimetable();
    }

    public int getSubjectIndex(Subject subject)
    {
        if (mSubjects.contains(subject)) return mSubjects.indexOf(subject);
        else return 0;
    }

    public ArrayList<Lesson> getLessons(int position)
    {
        return mDays[position];
    }

    public ArrayList<Subject> getSubjects()
    {
        return mSubjects;
    }

    public ArrayList<Equipment> getEquipments()
    {
        return mEquipments;
    }

    public Equipment getEquipment(int index)
    {
        return mEquipments.get(index);
    }

    public Equipment getEquipmentFromId(String id)
    {
        for (int i=0; i< mEquipments.size(); i++)
        {
            String idToTest = mEquipments.get(i).getId();
            if (id.compareTo(idToTest) == 0) return mEquipments.get(i);
        }
        return null;
    }

    public String getEquipmentNameFromID(String id)//Retourne null si aucun materiel trouvé
    {
        for (int i=0; i< mEquipments.size(); i++)
        {
            String idToTest = mEquipments.get(i).getId();
            if (id.compareTo(idToTest) == 0) return mEquipments.get(i).getName();
        }
        return null;
    }

    public Equipment getEquipmentFromName(String name)
    {
        for (int i=0; i<mEquipments.size(); i++)
        {
            if (mEquipments.get(i).getName().equals(name)) return mEquipments.get(i);
        }
        return null;
    }

    public int getIndexOfEquipment(Equipment equipment)
    {
        return mEquipments.indexOf(equipment);
    }

    public String[] getSubjectNames()
    {
        String[] subjectNames = new String[mSubjects.size()];
        for (int i=0; i<mSubjects.size(); i++)
        {
            subjectNames[i] = mSubjects.get(i).getName();
        }
        return subjectNames;
    }

    public int getSubjectIndexFromName(String name)
    {
        for (int i=0; i<mSubjects.size(); i++)
        {
            if (mSubjects.get(i).getName().equals(name)) return i;
        }
        return -1;
    }

    public String[] getEquipmentNames()
    {
        String[] equipmentNames = new String[mEquipments.size()];
        for (int i=0; i<mEquipments.size(); i++)
        {
            equipmentNames[i] = mEquipments.get(i).getName();
        }
        return equipmentNames;
    }

    public Subject getSubject(int index) { return mSubjects.get(index); }

    //Création d'un fichier CSV pour sauvegarder l'emploi du temps
    public void saveTimetable() {
        sortLessons();

        String equipmentStr = "";//Première ligne du fichier csv qui stock tous les équipments
        String subjectsStr = "";//Seconde ligne du fichier csv qui stocke toutes les matières
        String lessonsStr = "";//Troisième ligne du fichier csv qui stocke tous les cours
        String total = "";

        equipmentStr = getEquipmentStr();
        subjectsStr = getSubjectsStr();
        lessonsStr = getLessonStr();


        total = equipmentStr + "\n" + subjectsStr + "\n" + lessonsStr;
        Log.i("Timetable", "saveTimetable: " + total);
        try {

            FileOutputStream out = mContext.openFileOutput("timetable.csv", mContext.MODE_PRIVATE);

            byte[] fileContent = total.getBytes();
            out.write(fileContent);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Stockage des matières : ";Nom:Couleur;equipment1,equipment2"
    private String getSubjectsStr()
    {
        String subjectsStr = "";
        for (int i = 0; i < mSubjects.size(); i++) {
            subjectsStr += mSubjects.get(i).getName();
            subjectsStr += ":";
            subjectsStr += mSubjects.get(i).getColor();
            ArrayList<Equipment> equipments = mSubjects.get(i).getEquipments();
            if (equipments != null) {
                subjectsStr += ":";
                for (int j = 0; j < equipments.size(); j++) {
                    subjectsStr += mEquipments.indexOf(equipments.get(j));
                    if (j < equipments.size() - 1) subjectsStr += ",";
                }

            }
            if (i < mSubjects.size() - 1) subjectsStr += ";";
        }
        return subjectsStr;
    }

    //Stockage des cours : ";IndexMatière:HeureDebut:HeureFin;"
    //Séparation des jours : "/"
    private String getLessonStr()
    {
        String lessonsStr = "";
        for (int i=0; i < mDays.length; i++)
        {
            for (int j=0; j < mDays[i].size(); j++)
            {
                lessonsStr += getSubjectIndex(mDays[i].get(j).getSubject());
                lessonsStr += ":";
                lessonsStr += mDays[i].get(j).getStartTime();
                lessonsStr += ":";
                lessonsStr += mDays[i].get(j).getEndTime();
                if (j != mDays[i].size() - 1)   lessonsStr += ";";
            }

            if (i != mDays.length -1) lessonsStr += "/";
        }
        return lessonsStr;
    }

    private String getEquipmentStr()
    {
        String equipmentStr = "";

        for (int i=0; i<mEquipments.size(); i++)
        {
            equipmentStr += mEquipments.get(i).getName();
            equipmentStr += ":";
            equipmentStr += mEquipments.get(i).getId();
            if (i < mEquipments.size() - 1) equipmentStr += ";";
        }

        return equipmentStr;
    }

    private void loadTimetable()
    {
        try {
            FileInputStream in = mContext.openFileInput("timetable.csv");


            byte[] fileContent = new byte[in.available()];
            in.read(fileContent);

            String fileContentStr = new String(fileContent);
            Log.i("Timetable", "loadTimetable: " + fileContentStr);

            in.close();

            //On efface l'ancien emploi du temps
            while (!mSubjects.isEmpty())
            {
                deleteSubject(0);
            }

            //lines[0] = subjects lines[1] = lessons
            String[] lines = fileContentStr.split("\n");

            loadEquipment(lines[0]);
            Log.i("Timetable", "loadTimetable: equipment loaded");
            loadSubjects(lines[1]);
            Log.i("Timetable", "loadTimetable: subjects loaded");
            loadDays(lines[2]);
            Log.i("Timetable", "loadTimetable: lessons loaded");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadEquipment(String str)
    {
        String[] equipmentsStr = str.split(";");
        for (int i=0; i<equipmentsStr.length; i++)
        {
            mEquipments.add(new Equipment(equipmentsStr[i]));
        }
    }

    private void loadSubjects(String str)
    {
        if (!str.isEmpty())
        {
            String[] subjectsStr = str.split(";");
            for (int i=0; i < subjectsStr.length; i++)
            {
                mSubjects.add(new Subject(subjectsStr[i], this));
            }
        }
    }

    private void loadDays(String str)
    {
        Log.i("Timetable", "loadDays: str = " + str);
        String[] days = str.split("/");
        //mDays = new ArrayList[days.length];

        for (int i=0; i < days.length; i++)
        {
            mDays[i] = new ArrayList<>();
            if (!days[i].isEmpty()) loadDay(days[i], i);
        }

    }

    private void loadDay(String str, int index)
    {
        String[] lessons = str.split(";");
        for(int i=0; i < lessons.length; i++)
        {
            mDays[index].add(new Lesson(lessons[i], this));
        }
    }

    private void initializeTimetable()
    {
        mEquipments.add(new Equipment("Cahier d'anglais", "01 23 45 67"));
        mEquipments.add(new Equipment("Cahier de maths", "01 23 45 67"));
        mEquipments.add(new Equipment("Livre de maths", "01 23 45 67"));

        ArrayList<Equipment> mathsEquipments = new ArrayList<>();
        mathsEquipments.add(mEquipments.get(1));
        mathsEquipments.add(mEquipments.get(2));
        mSubjects.add(new Subject("Maths", "#00B81C", mathsEquipments, this));//0
        mSubjects.add(new Subject("Anglais", "#CF3838", null, this));//1
        mSubjects.add(new Subject("Physique", "#b3b3b3", null, this));//2
        mSubjects.add(new Subject("SIN", "#8ce6ff", null, this));//3
        mSubjects.add(new Subject("Etude", "#ffffff", null, this));//4
        mSubjects.add(new Subject("Devoir", "#8b63e0", null, this));//5
        mSubjects.add(new Subject("EPS", "#578ff7", null, this));//6
        mSubjects.add(new Subject("Espagnol", "#fc9d28", null, this));//7
        mSubjects.add(new Subject("ETT LV1", "#ccff33", null, this));//8
        mSubjects.add(new Subject("ETT", "#ff0000", null, this));//9
        mSubjects.add(new Subject("Philo", "#ffdbff", null, this));//10

        mDays[0].add(new Lesson(mSubjects.get(3), "9h25", "12h35", this));
        mDays[0].add(new Lesson(mSubjects.get(4), "13h45", "15h35", this));
        mDays[0].add(new Lesson(mSubjects.get(7), "15h50", "16h45", this));
        mDays[0].add(new Lesson(mSubjects.get(8), "16h45", "17h40", this));

        mDays[1].add(new Lesson(mSubjects.get(6), "8h30", "10h20", this));
        mDays[1].add(new Lesson(mSubjects.get(9), "10h45", "12h35", this));
        mDays[1].add(new Lesson(mSubjects.get(1), "13h45", "14h40", this));
        mDays[1].add(new Lesson(mSubjects.get(0), "14h40", "15h35", this));
        mDays[1].add(new Lesson(mSubjects.get(4), "15h50", "17h40", this));


        mDays[2].add(new Lesson(mSubjects.get(3), "8h30", "10h40", this));
        mDays[2].add(new Lesson(mSubjects.get(1), "10h40", "12h35", this));
        mDays[2].add(new Lesson(mSubjects.get(9), "13h45", "14h40", this));
        mDays[2].add(new Lesson(mSubjects.get(10), "14h40", "16h45", this));
        mDays[2].add(new Lesson(mSubjects.get(0), "16h45", "17h40", this));


        mDays[3].add(new Lesson(mSubjects.get(2), "8h30", "10h20", this));
        mDays[3].add(new Lesson(mSubjects.get(9), "10h45", "12h35", this));
        mDays[3].add(new Lesson(mSubjects.get(0), "13h45", "14h40", this));
        mDays[3].add(new Lesson(mSubjects.get(7), "14h40", "15h35", this));
        mDays[3].add(new Lesson(mSubjects.get(4), "15h50", "16h45", this));
        mDays[3].add(new Lesson(mSubjects.get(2), "16h45", "17h40", this));


        mDays[4].add(new Lesson(mSubjects.get(5), "8h30", "10h20", this));
        mDays[4].add(new Lesson(mSubjects.get(0), "10h45", "11h40", this));
        mDays[4].add(new Lesson(mSubjects.get(2), "10h40", "12h35", this));
        mDays[4].add(new Lesson(mSubjects.get(3), "13h45", "16h25", this));

        saveTimetable();
    }

    public ArrayList<Equipment> getExpectedEquipments()
    {
        Calendar calendar = Calendar.getInstance();

        Time currentTime = new Time(calendar.get(Calendar.HOUR_OF_DAY ), calendar.get(Calendar.MINUTE));
        //Time currentTime = new Time(5, 30);
        Log.i("Timetable", "getExpectedEquipments: time = " + currentTime.toString());

        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //On passe du début de la semaine du dimanche au lundi
        //et on passe de 1-7 à 0-6
        dayOfWeek -= 2;
        if (dayOfWeek<0) dayOfWeek = 6;
        Log.i("Timetable", "getExpectedEquipments: day of week = " + dayOfWeek);

        ArrayList<Equipment> expectedEquipments = new ArrayList<>();
        if (dayOfWeek < 5)
        {
            for (int i=0; i<mDays[dayOfWeek].size(); i++)
            {
                if (!mDays[dayOfWeek].get(i).hasBegun(currentTime))
                {
                    ArrayList<Equipment> equipments = mDays[dayOfWeek].get(i).getSubject().getEquipments();
                    if (equipments != null)
                    {
                        for (int j = 0; j<equipments.size(); j++)
                        {
                            if (!expectedEquipments.contains(equipments.get(j))) expectedEquipments.add(equipments.get(j));
                        }
                    }
                }
            }

            Log.i("Timetable", "getExpectedEquipments: ExpectedEquipments :");
            for (int i=0; i<expectedEquipments.size(); i++)
            {
                Log.i("Timetable", "- " + expectedEquipments.get(i).getName());
            }
        }

        return expectedEquipments;
    }

    private int getDayOfWeek(Calendar calendar)
    {
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        //On passe du début de la semaine du dimanche au lundi
        //et on passe de 1-7 à 0-6
        dayOfWeek -= 2;
        if (dayOfWeek<0) dayOfWeek = 6;
        Log.i("Timetable", "getExpectedEquipments: day of week = " + dayOfWeek);

        return dayOfWeek;
    }

    public void importIcalFile()
    {
        File myExternalFile = new File(mContext.getExternalFilesDir(icalFilePath), icalFileName);
        ArrayList<String> lessons = new ArrayList<>();
        ArrayList<String> subjects = new ArrayList<>();

        try {
            FileInputStream fis = new FileInputStream(myExternalFile);
            DataInputStream in = new DataInputStream(fis);
            BufferedReader br =
                    new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                if (strLine.contains("SUMMARY") || strLine.contains("DTSTART") || strLine.contains("DTEND"))
                {
                    strLine = strLine.replace("DTSTART:", "");
                    strLine = strLine.replace("DTEND:", "");
                    if (strLine.contains("SUMMARY"))
                    {
                        strLine = strLine.replace("SUMMARY:", "");
                        if (!subjects.contains(strLine))
                        {
                            subjects.add(strLine);
                        }
                    }

                    Log.i("Timetable", "importIcalFile: readLine : " + strLine);
                    lessons.set(lessons.size() - 1, lessons.get(lessons.size() - 1) + (!lessons.get(lessons.size() - 1).isEmpty() ? "/" : "") + strLine);
                }

                if (strLine.contains("BEGIN:VEVENT"))
                {
                    lessons.add("");
                }
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i("Timetable", "importIcalFile: " + lessons);
        Log.i("Timetable", "importIcalFile: " + subjects);

        //On efface l'ancien emploi du temps
        while (!mSubjects.isEmpty())
        {
            deleteSubject(0);
        }

        //On créer les matières
        for (int i=0; i<subjects.size(); i++)
        {
            creatSubjectFromIcalFile(subjects.get(i));
        }

        for (int i=0; i<lessons.size(); i++)
        {
            createLessonFromIcalFile(lessons.get(i));
        }
    }

    private void creatSubjectFromIcalFile(String str)
    {
        Random rand = new Random();
        int color = Color.rgb(rand.nextInt(240) + 16, rand.nextInt(240) + 16, rand.nextInt(240) + 16);
        String colorStr = ("#" + Integer.toString(Color.red(color), 16) + Integer.toString(Color.green(color), 16) + Integer.toString(Color.blue(color), 16)).toUpperCase();
        Log.i("Timetable", "creatSubjectFromIcalFile: colorStr = " + colorStr);
        mSubjects.add(new Subject(str, colorStr, new ArrayList<Equipment>(), this));
    }

    private void createLessonFromIcalFile(String lessonStr)
    {
        //On supprime le Z car il ne sert à rien
        lessonStr.replace("Z", "");
        String[] strParts = lessonStr.split("/");

        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);

        Log.i("Timetable", "createLessonFromIcalFile: UTC : " + TimeZone.getTimeZone("UTC").getRawOffset());
        Log.i("Timetable", "createLessonFromIcalFile: Paris : " + TimeZone.getDefault().getRawOffset());
        int timeOffset = TimeZone.getDefault().getRawOffset() / 3600000;

        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        calendar.set(Integer.valueOf(strParts[0].substring(0, 4)), Integer.valueOf(strParts[0].substring(4, 6)) - 1, Integer.valueOf(strParts[0].substring(6, 8)), Integer.valueOf(strParts[0].substring(9, 11)) + timeOffset, Integer.valueOf(strParts[0].substring(11, 13)));
        calendar.setTimeZone(TimeZone.getTimeZone("Paris"));
        Log.i("Timetable", "createLessonFromIcalFile: YEAR = " + calendar.get(Calendar.YEAR) + "  expected = " + Integer.parseInt(strParts[0].substring(0, 4)) + "  string = " + strParts[0].substring(0, 4));
        Log.i("Timetable", "createLessonFromIcalFile: MONTH = " + calendar.get(Calendar.MONTH) + "  expected = " + Integer.parseInt(strParts[0].substring(4, 6)) + "  string = " + strParts[0].substring(4, 6));
        Log.i("Timetable", "createLessonFromIcalFile: DAYOFMONTH = " + calendar.get(Calendar.DAY_OF_MONTH) + "  expected = " + Integer.parseInt(strParts[0].substring(6, 8)) + "  string = " + strParts[0].substring(6, 8));
        Log.i("Timetable", "createLessonFromIcalFile: HOUR = " + calendar.get(Calendar.HOUR_OF_DAY) + "  expected = " + Integer.parseInt(strParts[0].substring(9, 11)) + "  string = " + strParts[0].substring(9, 11));
        Log.i("Timetable", "createLessonFromIcalFile: MINUTE = " + calendar.get(Calendar.MINUTE) + "  expected = " + Integer.parseInt(strParts[0].substring(11, 13)) + "  string = " + strParts[0].substring(11, 13));
        int dayOfWeek = getDayOfWeek(calendar);
        Log.i("Timetable", "createLessonFromIcalFile: str = " + strParts[0] + "   day of week = " + dayOfWeek);
        Log.i("Timetable", " ");

        String startTime = calendar.get(Calendar.HOUR_OF_DAY ) + "h" + calendar.get(Calendar.MINUTE);
        Log.i("Timetable", "createLessonFromIcalFile: startTime = " + startTime);

        calendar.set(Integer.valueOf(strParts[1].substring(0, 4)), Integer.valueOf(strParts[1].substring(4, 6)) - 1, Integer.valueOf(strParts[1].substring(6, 8)), Integer.valueOf(strParts[1].substring(9, 11)) + timeOffset, Integer.valueOf(strParts[1].substring(11, 13)));
        String endTime = calendar.get(Calendar.HOUR_OF_DAY ) + "h" + calendar.get(Calendar.MINUTE);
        Log.i("Timetable", "createLessonFromIcalFile: endTime = " + endTime);

        Log.i("Timetable", "createLessonFromIcalFile: strParts[2] = " + strParts[2]);
        int subjectIndex = getSubjectIndexFromName(strParts[2]);

        if (subjectIndex != -1) mDays[dayOfWeek].add(new Lesson(mSubjects.get(subjectIndex), startTime, endTime, this));

        sortLessons();
    }

    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    private static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }

}