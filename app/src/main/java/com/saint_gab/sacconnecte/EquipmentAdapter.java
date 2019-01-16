package com.saint_gab.sacconnecte;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EquipmentAdapter extends ArrayAdapter<Equipment> {

    private Context mContext;

    //Permet de stocker toutes les vues d'un élément
    private static class ViewHolder
    {
        TextView Name;
        TextView Id;
    }

    public EquipmentAdapter(Timetable timetable, Context context) {
        super(context, R.layout.row_equipment, timetable.getEquipments());
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Get the data item for this position
        Equipment equipment = getItem(position);
        //Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        final View result;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_equipment, parent, false);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.row_equipment_name);
            viewHolder.Id = (TextView) convertView.findViewById(R.id.row_equipment_id);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.Name.setText(equipment.getName());
        viewHolder.Id.setText("id : " + equipment.getId());
        //result.setBackgroundColor(Color.parseColor(subject.getColor()));

        return result;
    }
}
