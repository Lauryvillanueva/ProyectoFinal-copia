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

public class SpinnerAdapterCat extends ArrayAdapter<Categoria> {

    private Context context;

    private List<Categoria> categoriaLists;

    public SpinnerAdapterCat(Context context, int textViewResourceId, List<Categoria> categoriaLists) {
        super(context, textViewResourceId, categoriaLists);
        this.context = context;
        this.categoriaLists = categoriaLists;
    }

    public int getCount(){
        return categoriaLists.size();
    }

    public Categoria getItem(int position){
        return categoriaLists.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);

        label.setText(categoriaLists.get(position).getName());


        return label;
    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(context);
        label.setTextColor(Color.BLACK);
        label.setText(categoriaLists.get(position).getName());

        return label;
    }
}
