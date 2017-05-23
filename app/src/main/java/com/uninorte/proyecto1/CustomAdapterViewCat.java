package com.uninorte.proyecto1;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 16/04/17.
 */

public class CustomAdapterViewCat extends  RecyclerView.Adapter<CustomAdapterViewCat.ViewHolder>{
    private List<Categoria> categoriaLists;
    private Context context;
    private Long nivel;

    OnItemClickListener clickListener;

    public CustomAdapterViewCat(Context context,List<Categoria> categoriaLists, Long nivel) {
        this.categoriaLists = categoriaLists;
        this.context = context;
        this.nivel=nivel;
    }



    public Context getContext() {
        return context;
    }

    @Override
    public CustomAdapterViewCat.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View categoriaView = inflater.inflate(R.layout.rowlistcat, parent, false);

        ViewHolder viewHolder = new ViewHolder(categoriaView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Categoria categoriaList = categoriaLists.get(position);
        List<Elemento> elementoLists=categoriaList.getElementos();
        List<ElemenNivel> elemenNivelLists= new ArrayList<>();
        CustomAdapterViewEleNiv customAdapterViewEleNiv;
        ViewHolder mViewHolderCat = viewHolder;
        mViewHolderCat.tvCatName.setText(categoriaList.getName());
        if(!elementoLists.isEmpty()){
            for(Elemento elemento:elementoLists){
                List<ElemenNivel> elemenNivels=elemento.getDescriptions();
                if(!elemenNivels.isEmpty()){
                    for(ElemenNivel desc:elemenNivels){
                        if(desc.getNivel().equals(nivel)){
                            elemenNivelLists.add(desc);
                            break;
                        }
                    }
                }
            }
            customAdapterViewEleNiv=new CustomAdapterViewEleNiv(context,elemenNivelLists);
            mViewHolderCat.list.setAdapter(customAdapterViewEleNiv);
            mViewHolderCat.list.setLayoutManager(new LinearLayoutManager(context));

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
        public RecyclerView list;


        public ViewHolder(View itemView) {
            super(itemView);
            tvCatName = (TextView) itemView.findViewById(R.id.textViewName);
            list=(RecyclerView) itemView.findViewById(R.id.ReciclerView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(view, getAdapterPosition());
        }
    }



}
