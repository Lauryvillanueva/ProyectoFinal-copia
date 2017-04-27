package com.uninorte.proyecto1;

import android.content.Context;
import android.graphics.Color;
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

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);

        label.setText(estudianteLists.get(position).getName());


        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(estudianteLists.get(position).getName());

        return label;
    }
}
