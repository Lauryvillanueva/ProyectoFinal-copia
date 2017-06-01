package com.uninorte.proyecto1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class CustomAdapterMat extends RecyclerView.Adapter<CustomAdapterMat.ViewHolder>{
    private List<Materia> materiaLists;
    private Context context;
    long initialCount;

    OnItemClickListener clickListener;


    public CustomAdapterMat(Context context,List<Materia> materiaLists) {
        this.materiaLists = materiaLists;
        this.context = context;
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
        Materia materiaList = materiaLists.get(position);
        ViewHolder mViewHolderMat = viewHolder;
        mViewHolderMat.tvMateriaName.setText(materiaList.getName());
        mViewHolderMat.buttonedit.setFocusable(false);
        mViewHolderMat.buttonedit.setFocusableInTouchMode(false);
        mViewHolderMat.buttonedit.setTag(position);
        mViewHolderMat.buttonview.setFocusable(false);
        mViewHolderMat.buttonview.setFocusableInTouchMode(false);
        mViewHolderMat.buttonview.setTag(position);
        mViewHolderMat.buttonnote.setFocusable(false);
        mViewHolderMat.buttonnote.setFocusableInTouchMode(false);
        mViewHolderMat.buttonnote.setTag(position);

    }

    @Override
    public int getItemCount() {
        return materiaLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvMateriaName;
        public ImageButton buttonedit;
        public ImageButton buttonview;
        public ImageButton buttonnote;

        public ViewHolder(View itemView) {
            super(itemView);
            tvMateriaName = (TextView) itemView.findViewById(R.id.textViewName);
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