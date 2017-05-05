package com.uninorte.proyecto1;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ThemedSpinnerAdapter;

import java.util.List;

/**
 * Created by daniel on 19/04/17.
 */


@TargetApi(Build.VERSION_CODES.M)
public class SpinnerAdapterCat extends ArrayAdapter<Categoria> {

    private Context context;



    private List<Categoria> categoriaLists;

    public SpinnerAdapterCat(Context context, int textViewResourceId,  List<Categoria> categoriaLists) {
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
        TextView textView = (TextView) View.inflate(context, android.R.layout.simple_spinner_item, null);
        textView.setText(categoriaLists.get(position).getName());
        return textView;


    }


    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }
        ((TextView) convertView).setText(categoriaLists.get(position).getName());
        return convertView;

    }


}
