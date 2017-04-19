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

public class CustomAdapterEva extends  RecyclerView.Adapter<CustomAdapterEva.ViewHolder>{
    private List<Evaluacion> evaluacionLists;
    private Context context;

    OnItemClickListener clickListener;

    public CustomAdapterEva(Context context,List<Evaluacion> evaluacionLists) {
        this.evaluacionLists = evaluacionLists;
        this.context = context;
    }



    public Context getContext() {
        return context;
    }

    @Override
    public CustomAdapterEva.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View evaluacionView = inflater.inflate(R.layout.rowonlyname, parent, false);

        ViewHolder viewHolder = new ViewHolder(evaluacionView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Evaluacion evaluacionList = evaluacionLists.get(position);
        ViewHolder mViewHolderEva = viewHolder;
        mViewHolderEva.tvEleName.setText(evaluacionList.getName());
        mViewHolderEva.buttonedit.setVisibility(View.GONE);
        mViewHolderEva.buttonview.setVisibility(View.GONE);
        mViewHolderEva.buttonnote.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return evaluacionLists.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvEleName;
        public ImageButton buttonedit;
        public ImageButton buttonview;
        public ImageButton buttonnote;

        public ViewHolder(View itemView) {
            super(itemView);
            tvEleName = (TextView) itemView.findViewById(R.id.textViewName);
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
