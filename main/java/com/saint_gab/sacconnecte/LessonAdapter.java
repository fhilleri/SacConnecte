package com.saint_gab.sacconnecte;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class LessonAdapter extends ArrayAdapter<Lesson>  {

    private ArrayList<Lesson> dataSet;
    Context mContext;

    //Permet de stocker toutes les vues d'une lesson
    private static class ViewHolder
    {
        TextView Name;
        TextView StartTime;
        TextView EndTime;
    }

    public LessonAdapter(ArrayList<Lesson> data, Context context) {
        super(context, R.layout.row_lesson, data);
        this.dataSet = data;
        this.mContext = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Get the data item for this position
        Lesson lesson = getItem(position);
        //Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        final View result;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_lesson, parent, false);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.lesson_name);
            viewHolder.StartTime = (TextView) convertView.findViewById(R.id.lesson_start_time);
            viewHolder.EndTime = (TextView) convertView.findViewById(R.id.lesson_end_time);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.Name.setText(lesson.getName());
        viewHolder.StartTime.setText(lesson.getStartTime());
        viewHolder.EndTime.setText(lesson.getEndTime());
        result.setBackgroundColor(Color.parseColor(lesson.getColor()));

        return result;
    }

}
