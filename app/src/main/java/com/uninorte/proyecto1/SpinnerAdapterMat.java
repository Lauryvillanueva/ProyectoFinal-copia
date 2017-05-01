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

public class SpinnerAdapterMat extends ArrayAdapter<Materia> {

    private Context context;

    private List<Materia> materiaLists;

    public SpinnerAdapterMat(Context context, int textViewResourceId, List<Materia> materiaLists) {
        super(context, textViewResourceId, materiaLists);
        this.context = context;
        this.materiaLists = materiaLists;
    }

    public int getCount(){
        return materiaLists.size();
    }

    public Materia getItem(int position){
        return materiaLists.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);

        label.setText(materiaLists.get(position).getName());


        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(materiaLists.get(position).getName());

        return label;
    }
}
