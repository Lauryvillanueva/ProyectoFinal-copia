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

public class CustomAdapterCat extends  RecyclerView.Adapter<CustomAdapterCat.ViewHolder>{
    private List<Categoria> categoriaLists;
    private Context context;
    private Boolean selector;

    OnItemClickListener clickListener;

    public CustomAdapterCat(Context context,List<Categoria> categoriaLists, Boolean selector) {
        this.categoriaLists = categoriaLists;
        this.context = context;
        this.selector=selector;
    }



    public Context getContext() {
        return context;
    }

    @Override
    public CustomAdapterCat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View categoriaView = inflater.inflate(R.layout.rowonlyname, parent, false);

        ViewHolder viewHolder = new ViewHolder(categoriaView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Categoria categoriaList = categoriaLists.get(position);
        ViewHolder mViewHolderCat = viewHolder;
        mViewHolderCat.tvCatName.setText(categoriaList.getName());
        if (selector) {
            mViewHolderCat.buttonedit.setFocusable(false);
            mViewHolderCat.buttonedit.setFocusableInTouchMode(false);
            mViewHolderCat.buttonedit.setTag(position);
            mViewHolderCat.buttonview.setFocusable(false);
            mViewHolderCat.buttonview.setFocusableInTouchMode(false);
            mViewHolderCat.buttonview.setTag(position);
            mViewHolderCat.buttonnote.setVisibility(View.INVISIBLE);
        }else{
            mViewHolderCat.buttonedit.setVisibility(View.INVISIBLE);
            mViewHolderCat.buttonview.setVisibility(View.INVISIBLE);
            mViewHolderCat.buttonnote.setVisibility(View.INVISIBLE);

        }


    }

    @Override
    public int getItemCount() {
        return categoriaLists.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvCatName;
        public ImageButton buttonedit;
        public ImageButton buttonview;
        public ImageButton buttonnote;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCatName = (TextView) itemView.findViewById(R.id.textViewName);
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
