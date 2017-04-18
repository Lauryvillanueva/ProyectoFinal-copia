
package com.uninorte.proyecto1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by daniel on 16/04/17.
 */

public class CustomAdapterEleNiv extends  RecyclerView.Adapter<CustomAdapterEleNiv.ViewHolder>{
    private List<Nivel> nivelLists;
    private Context context;
    private boolean selector;
    private Long elementoid;

    OnItemClickListener clickListener;

    public CustomAdapterEleNiv(Context context,List<Nivel> nivelLists,boolean selector,Long elementoid) {
        this.nivelLists = nivelLists;
        this.context = context;
        this.selector=selector;
        this.elementoid=elementoid;
    }



    public Context getContext() {
        return context;
    }

    @Override
    public CustomAdapterEleNiv.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View elenivView = inflater.inflate(R.layout.rowlevels, parent, false);

        ViewHolder viewHolder = new ViewHolder(elenivView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Nivel nivelList = nivelLists.get(position);
        ViewHolder mViewHolderEleNiv = viewHolder;
        mViewHolderEleNiv.tvNivName.setText(nivelList.getName());
        Long count;
        if(selector) {
            count= EleNivDescription.count(EleNivDescription.class);
            if (count>0) {
                Log.d("onBindViewHolder: ", "Count: "+count);
                Log.d("onBindViewHolder: ", "Elementoid: "+elementoid);
                Log.d("onBindViewHolder: ", "Nivelid: "+nivelList.getId());
                Elemento elemento = Elemento.findById(Elemento.class,elementoid);

                List<EleNivDescription> elenivdescriptions= elemento.getDescriptions();
                if (!elenivdescriptions.isEmpty()) {
                    EleNivDescription elenivdescription=elenivdescriptions.get(position);
                    mViewHolderEleNiv.Edesc.setText(elenivdescription.getDescription());
                }
            }
            mViewHolderEleNiv.Edesc.setVisibility(View.VISIBLE);
            mViewHolderEleNiv.Edesc.setTag(position);
        }else{
            mViewHolderEleNiv.Epeso.setVisibility(View.VISIBLE);
            mViewHolderEleNiv.Epeso.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return nivelLists.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvNivName;
        public EditText Epeso,Edesc;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNivName = (TextView) itemView.findViewById(R.id.textViewName);
            Epeso= (EditText) itemView.findViewById(R.id.editTextPeso);
            Edesc= (EditText) itemView.findViewById(R.id.editTextNivel);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }
    }



}
