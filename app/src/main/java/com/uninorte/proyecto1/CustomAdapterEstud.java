package com.uninorte.proyecto1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by daniel on 16/04/17.
 */

public class CustomAdapterEstud extends  RecyclerView.Adapter<CustomAdapterEstud.ViewHolder>{
    private List<Estudiante> estudianteLists;
    private Context context;

    OnItemClickListener clickListener;

    public CustomAdapterEstud(Context context,List<Estudiante> estudianteLists) {
        this.estudianteLists = estudianteLists;
        this.context = context;
    }



    public Context getContext() {
        return context;
    }

    @Override
    public CustomAdapterEstud.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View estudianteView = inflater.inflate(R.layout.rowonlyname, parent, false);

        ViewHolder viewHolder = new ViewHolder(estudianteView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Estudiante estudianteList = estudianteLists.get(position);
        ViewHolder mViewHolderEstud = viewHolder;
        mViewHolderEstud.tvEstudName.setText(estudianteList.getName());
        mViewHolderEstud.buttonedit.setFocusable(false);
        mViewHolderEstud.buttonedit.setFocusableInTouchMode(false);
        mViewHolderEstud.buttonedit.setTag(position);
        mViewHolderEstud.buttonview.setFocusable(false);
        mViewHolderEstud.buttonview.setFocusableInTouchMode(false);
        mViewHolderEstud.buttonview.setTag(position);
        mViewHolderEstud.buttonnote.setVisibility(View.INVISIBLE);


    }

    @Override
    public int getItemCount() {
        return estudianteLists.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvEstudName;
        public ImageButton buttonedit;
        public ImageButton buttonview;
        public ImageButton buttonnote;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEstudName = (TextView) itemView.findViewById(R.id.textViewName);
            buttonedit =(ImageButton) itemView.findViewById(R.id.edit);
            buttonview =(ImageButton) itemView.findViewById(R.id.view);
            buttonnote =(ImageButton) itemView.findViewById(R.id.note);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }
    }



}
