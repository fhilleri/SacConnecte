package com.saint_gab.sacconnecte;

import java.util.ArrayList;

public class Timetable {

    ArrayList<Lesson>[] mDays;
    ArrayList<Subject> mSubjects;

    public Timetable()
    {
        mDays = new ArrayList[5];
        for(int i=0; i<mDays.length; i++)
        {
            mDays[i] = new ArrayList<>();
        }
        mSubjects = new ArrayList<>();

        mSubjects.add(new Subject("Maths", "#00B81C"));//0
        mSubjects.add(new Subject("Anglais", "#CF3838"));//1
        mSubjects.add(new Subject("Physique", "#b3b3b3"));//2
        mSubjects.add(new Subject("SIN", "#8ce6ff"));//3
        mSubjects.add(new Subject("Etude", "#ffffff"));//4
        mSubjects.add(new Subject("Devoir", "#8b63e0"));//5
        mSubjects.add(new Subject("EPS", "#578ff7"));//6
        mSubjects.add(new Subject("Espagnol", "#fc9d28"));//7
        mSubjects.add(new Subject("ETT LV1", "#ccff33"));//8

        mDays[0].add(new Lesson(mSubjects.get(3), "9h25", "12h35"));
        mDays[0].add(new Lesson(mSubjects.get(4), "13h45", "15h35"));
        mDays[0].add(new Lesson(mSubjects.get(7), "15h50", "16h45"));
        mDays[0].add(new Lesson(mSubjects.get(8), "16h45", "17h40"));

        mDays[1].add(new Lesson(mSubjects.get(6), "8h30", "10h20"));
        mDays[2].add(new Lesson(mSubjects.get(3), "8h30", "10h40"));
        mDays[3].add(new Lesson(mSubjects.get(2), "8h30", "10h20"));
        mDays[4].add(new Lesson(mSubjects.get(5), "8h30", "10h20"));

    }

    public void addSubject(Subject newSubject)
    {
        mSubjects.add(newSubject);
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
    }

    public void deleteLesson(int dayIndex ,int lessonIndex)
    {
        Lesson lesson = mDays[dayIndex].get(lessonIndex);
        lesson.getSubject().removeLesson(lesson);
        mDays[dayIndex].remove(lessonIndex);
    }

    public ArrayList<Lesson> getLessons(int position)
    {
        return mDays[position];
    }

    public ArrayList<Subject> getSubjects()
    {
        return mSubjects;
    }
}