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

public class CustomAdapterEle extends  RecyclerView.Adapter<CustomAdapterEle.ViewHolder>{
    private List<Elemento> elementoLists;
    private Context context;
    private Boolean selector;

    OnItemClickListener clickListener;

    public CustomAdapterEle(Context context,List<Elemento> elementoLists, Boolean selector) {
        this.elementoLists = elementoLists;
        this.context = context;
        this.selector=selector;
    }



    public Context getContext() {
        return context;
    }

    @Override
    public CustomAdapterEle.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View elementoView = inflater.inflate(R.layout.rowonlyname, parent, false);

        ViewHolder viewHolder = new ViewHolder(elementoView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Elemento elementoList = elementoLists.get(position);
        ViewHolder mViewHolderEle = viewHolder;
        mViewHolderEle.tvEleName.setText(elementoList.getName());
        if (selector) {
            mViewHolderEle.buttonedit.setFocusable(false);
            mViewHolderEle.buttonedit.setFocusableInTouchMode(false);
            mViewHolderEle.buttonedit.setTag(position);
            mViewHolderEle.buttonview.setFocusable(false);
            mViewHolderEle.buttonview.setFocusableInTouchMode(false);
            mViewHolderEle.buttonview.setTag(position);
            mViewHolderEle.buttonnote.setVisibility(View.INVISIBLE);
        }else{
            mViewHolderEle.buttonnote.setFocusable(false);
            mViewHolderEle.buttonnote.setFocusableInTouchMode(false);
            mViewHolderEle.buttonnote.setTag(position);
        }


    }

    @Override
    public int getItemCount() {
        return elementoLists.size();
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
