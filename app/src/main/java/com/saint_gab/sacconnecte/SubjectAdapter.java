package com.saint_gab.sacconnecte;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SubjectAdapter extends ArrayAdapter<Subject> //Permet d'adapter un subject en vue afin de l'afficher dans la page des matières
{
    private Timetable mTimetable;
    Context mContext;
    Resources mRes;

    //Permet de stocker toutes les vues d'un élément
    private static class ViewHolder
    {
        TextView Name;
        TextView LessonCount;
    }

    public SubjectAdapter(Timetable timetable, Context context, Resources res) {
        super(context, R.layout.row_subject, timetable.getSubjects());
        this.mContext = context;
        mRes = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Get the data item for this position
        Subject subject = getItem(position);
        //Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        final View result;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_subject, parent, false);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.row_subject_name);
            viewHolder.LessonCount = (TextView) convertView.findViewById(R.id.row_subject_lesson_count);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.Name.setText(subject.getName());
        int lessonCount = subject.getLessonCount();
        viewHolder.LessonCount.setText(lessonCount + " " + mRes.getString(R.string.subjectAdapter_lessons));
        result.setBackgroundColor(Color.parseColor(subject.getColor()));

        return result;
    }
}
