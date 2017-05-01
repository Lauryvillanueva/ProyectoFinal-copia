package com.uninorte.proyecto1;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class CustomAdapterRep extends RecyclerView.Adapter<CustomAdapterRep.ViewHolder>{
    private List<Reporte> reporteLists;
    private Context context;

    OnItemClickListener clickListener;


    public CustomAdapterRep(Context context,List<Reporte> reporteLists) {
        this.reporteLists = reporteLists;
        this.context = context;
    }



    public Context getContext() {
        return context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View materiaView = inflater.inflate(R.layout.rowreportes, parent, false);

        ViewHolder viewHolder = new ViewHolder(materiaView);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Reporte reporteList = reporteLists.get(position);
        ViewHolder mViewHolderRep = viewHolder;
        mViewHolderRep.tvNombre.setText(reporteList.getNombre());
        mViewHolderRep.etNota.setText(String.valueOf(reporteList.getNota()));
        mViewHolderRep.etNota.setFocusable(false);
        mViewHolderRep.etNota.setFocusableInTouchMode(false);
        //

    }

    @Override
    public int getItemCount() {
        return reporteLists.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //
        public TextView tvNombre;
        public EditText etNota;

        public ViewHolder(View itemView) {
            super(itemView);
            tvNombre=(TextView) itemView.findViewById(R.id.TextViewRowRep);
            etNota=(EditText) itemView.findViewById(R.id.EditTextRepoNota);
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