package com.saint_gab.sacconnecte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class StringAdapterForBackpackFragment extends ArrayAdapter<String> {

    private Context mContext;

    private int[] mColors;

    //Permet de stocker toutes les vues d'un élément
    private static class ViewHolder
    {
        TextView Name;
    }

    public StringAdapterForBackpackFragment(Context context, String[] strings, int[] colors) {
        super(context, R.layout.row_string_for_backpack_fragment, strings);
        mColors = colors;
        this.mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //Get the data item for this position
        String string = getItem(position);
        //Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;

        final View result;

        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_string_for_backpack_fragment, parent, false);
            viewHolder.Name = (TextView) convertView.findViewById(R.id.row_equipment_name);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.Name.setText(string);
        viewHolder.Name.setBackgroundColor(mColors[position]);
        //result.setBackgroundColor(Color.parseColor(subject.getColor()));

        return result;
    }
}
