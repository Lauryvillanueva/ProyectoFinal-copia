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

public class SpinnerAdapterRub extends ArrayAdapter<Rubrica> {

    private Context context;

    private List<Rubrica> rubricaLists;

    public SpinnerAdapterRub(Context context, int textViewResourceId, List<Rubrica> rubricaLists) {
        super(context, textViewResourceId, rubricaLists);
        this.context = context;
        this.rubricaLists = rubricaLists;
    }

    public int getCount(){
        return rubricaLists.size();
    }

    public Rubrica getItem(int position){
        return rubricaLists.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);

        label.setText(rubricaLists.get(position).getName());


        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(rubricaLists.get(position).getName());

        return label;
    }
}
