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

public class SpinnerAdapterEval extends ArrayAdapter<Evaluacion> {

    private Context context;

    private List<Evaluacion> evaluacionLists;

    public SpinnerAdapterEval(Context context, int textViewResourceId, List<Evaluacion> evaluacionLists) {
        super(context, textViewResourceId, evaluacionLists);
        this.context = context;
        this.evaluacionLists = evaluacionLists;
    }

    public int getCount(){
        return evaluacionLists.size();
    }

    public Evaluacion getItem(int position){
        return evaluacionLists.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(context, android.R.layout.simple_spinner_item, null);
        textView.setText(evaluacionLists.get(position).getName());
        return textView;


    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        ((TextView) convertView).setText(evaluacionLists.get(position).getName());
        return convertView;

    }
}
