package com.saint_gab.sacconnecte;

import android.content.Context;
import android.util.Log;

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

import static android.content.Context.MODE_PRIVATE;

public class Timetable {

    ArrayList<Subject> mSubjects;
    ArrayList<Lesson>[] mDays;
    ArrayList<Equipment> mEquipments;
    Context mContext;

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

    public void addEquipment(Equipment equipment)
    {
        mEquipments.add(equipment);
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

    public String getEquipmentNameFromID(String id)
    {
        for (int i=0; i< mEquipments.size(); i++)
        {
            //if (mEquipments.get(i))
        }
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
            if (i != mSubjects.size() - 1) subjectsStr += ";";
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
                if (i != mDays[i].size() - 1)   lessonsStr += ";";
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
}