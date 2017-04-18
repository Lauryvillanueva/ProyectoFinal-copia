package com.uninorte.proyecto1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class CustomAdapterRub extends RecyclerView.Adapter<CustomAdapterRub.ViewHolder>{
    private List<Rubrica> rubricaLists;
    private Context context;

    OnItemClickListener clickListener;


    public CustomAdapterRub(Context context,List<Rubrica> rubricaLists) {
        this.rubricaLists = rubricaLists;
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
        Rubrica rubricaList = rubricaLists.get(position);
        ViewHolder mViewHolderRub = viewHolder;
        mViewHolderRub.tvRubricaName.setText(rubricaList.getName());
        mViewHolderRub.buttonedit.setFocusable(false);
        mViewHolderRub.buttonedit.setFocusableInTouchMode(false);
        mViewHolderRub.buttonedit.setTag(position);
        mViewHolderRub.buttonview.setFocusable(false);
        mViewHolderRub.buttonview.setFocusableInTouchMode(false);
        mViewHolderRub.buttonview.setTag(position);
        mViewHolderRub.buttonnote.setVisibility(View.INVISIBLE);

    }

    @Override
    public int getItemCount() {
        return rubricaLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvRubricaName;
        public ImageButton buttonedit;
        public ImageButton buttonview;
        public ImageButton buttonnote;

        public ViewHolder(View itemView) {
            super(itemView);
            tvRubricaName = (TextView) itemView.findViewById(R.id.textViewName);
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