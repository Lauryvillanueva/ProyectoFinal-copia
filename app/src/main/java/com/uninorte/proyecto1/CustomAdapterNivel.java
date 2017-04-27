package com.uninorte.proyecto1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class CustomAdapterNivel extends RecyclerView.Adapter<CustomAdapterNivel.ViewHolder>{
    private List<Nivel> nivelLists;
    private Context context;
    private boolean selector;

    OnItemClickListener clickListener;


    public CustomAdapterNivel(Context context,List<Nivel> nivelLists, boolean selector) {
        this.nivelLists = nivelLists;
        this.context = context;
        this.selector=selector;
    }



    public Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View materiaView = inflater.inflate(R.layout.rowonlyname, parent, false);

        ViewHolder viewHolder = new ViewHolder(materiaView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Nivel nivelList = nivelLists.get(position);
        ViewHolder mViewHolderNivel = viewHolder;
        mViewHolderNivel.tvNivelName.setText(nivelList.getName());
        if (selector){
            mViewHolderNivel.buttonedit.setFocusable(false);
            mViewHolderNivel.buttonedit.setFocusableInTouchMode(false);
            mViewHolderNivel.buttonedit.setTag(position);
            mViewHolderNivel.buttonview.setFocusable(false);
            mViewHolderNivel.buttonview.setFocusableInTouchMode(false);
            mViewHolderNivel.buttonview.setTag(position);
        }else{
            mViewHolderNivel.buttonedit.setVisibility(View.GONE);
            mViewHolderNivel.buttonview.setVisibility(View.GONE);
        }

        mViewHolderNivel.buttonnote.setVisibility(View.GONE);

    }

    @Override
    public int getItemCount() {
        return nivelLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvNivelName;
        public ImageButton buttonedit;
        public ImageButton buttonview;
        public ImageButton buttonnote;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNivelName = (TextView) itemView.findViewById(R.id.textViewName);
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

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

}