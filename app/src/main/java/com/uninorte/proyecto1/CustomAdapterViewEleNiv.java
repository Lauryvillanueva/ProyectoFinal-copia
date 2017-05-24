package com.uninorte.proyecto1;

/**
 * Created by LauryV on 23/05/2017.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;
public class CustomAdapterViewEleNiv extends  RecyclerView.Adapter<CustomAdapterViewEleNiv.ViewHolder>{
    private List<ElemenNivel> elemenNivelLists;
    private Context context;



    OnItemClickListener clickListener;

    public CustomAdapterViewEleNiv(Context context,List<ElemenNivel> elemenNivelLists) {
        this.elemenNivelLists = elemenNivelLists;
        this.context = context;

    }

    public CustomAdapterViewEleNiv() {

    }

    public void setData(List<ElemenNivel> data) {
        if (elemenNivelLists != data) {
            elemenNivelLists = data;
            notifyDataSetChanged();
        }
    }


    public Context getContext() {
        return context;
    }

    @Override
    public CustomAdapterViewEleNiv.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View elenivView = inflater.inflate(R.layout.rowelenivdesc, parent, false);

        ViewHolder viewHolder = new ViewHolder(elenivView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        ElemenNivel elemenNivelList = elemenNivelLists.get(position);
        ViewHolder mViewHolderEleNiv = viewHolder;
        //Name
        Elemento elemento=Elemento.findById(Elemento.class,elemenNivelList.getElemento());
        mViewHolderEleNiv.tvNivName.setText(elemento.getName());
        mViewHolderEleNiv.tvDesc.setText(elemenNivelList.getDescription());

    }

    @Override
    public int getItemCount() {
        return elemenNivelLists.size();
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvNivName,tvDesc;
       
        public ViewHolder(View itemView) {
            super(itemView);
            tvNivName = (TextView) itemView.findViewById(R.id.TextViewElem);           
            tvDesc= (TextView) itemView.findViewById(R.id.TextViewNivelDesc);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }
    }



}
