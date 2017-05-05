package com.uninorte.proyecto1;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by daniel on 19/04/17.
 */

public class SpinnerAdapterEstud extends ArrayAdapter<Estudiante> {

    private Context context;

    private List<Estudiante> estudianteLists;

    public SpinnerAdapterEstud(Context context, int textViewResourceId, List<Estudiante> estudianteLists) {
        super(context, textViewResourceId, estudianteLists);
        this.context = context;
        this.estudianteLists = estudianteLists;
    }

    public int getCount(){
        return estudianteLists.size();
    }

    public Estudiante getItem(int position){
        return estudianteLists.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(context, android.R.layout.simple_spinner_item, null);
        textView.setText(estudianteLists.get(position).getName());
        return textView;


    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        ((TextView) convertView).setText(estudianteLists.get(position).getName());
        return convertView;

    }
}
