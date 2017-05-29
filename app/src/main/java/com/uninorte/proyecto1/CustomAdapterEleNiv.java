
package com.uninorte.proyecto1;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daniel on 16/04/17.
 */

public class CustomAdapterEleNiv extends  RecyclerView.Adapter<CustomAdapterEleNiv.ViewHolder>{
    private List<Nivel> nivelLists;
    private Context context;
    private boolean selector;
    private String elementoid;

    OnItemClickListener clickListener;

     public CustomAdapterEleNiv(Context context, List<Nivel> nivelLists, boolean selector, String elementoid) {
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
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Nivel nivelList = nivelLists.get(position);
        final ViewHolder mViewHolderEleNiv = viewHolder;
        mViewHolderEleNiv.tvNivName.setText(nivelList.getName());
        Long count;
        //count= ElemenNivel.count(ElemenNivel.class);
        DatabaseReference mDatabaseReference= FirebaseDatabase.getInstance().getReference("noterubric").child("ElemenNivel");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()>0){
                    DatabaseReference mDatabaseRef=FirebaseDatabase.getInstance().getReference("noterubric").child("Elemento");
                    mDatabaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot snap:dataSnapshot.getChildren()){
                                if(snap.getValue(Elemento.class).getKey().equals(elementoid)){
                                    final Elemento elemento = snap.getValue(Elemento.class);
                                    DatabaseReference referencia= FirebaseDatabase.getInstance().getReference("noterubric").child("ElemenNivel");
                                    referencia.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            List<ElemenNivel> elenivdescriptions= new ArrayList<>();
                                            for (DataSnapshot snap:dataSnapshot.getChildren()){
                                                if(snap.getValue(ElemenNivel.class).getElemento().equals(elemento.getKey())){
                                                    elenivdescriptions.add(snap.getValue(ElemenNivel.class));
                                                }
                                            }

                                            if (!elenivdescriptions.isEmpty()) {
                                                if(position<elenivdescriptions.size()) {
                                                    ElemenNivel elenivdescription = elenivdescriptions.get(position);
                                                    mViewHolderEleNiv.Edesc.setText(elenivdescription.getDescription());
                                                }
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*if (count>0) {
            Elemento elemento = Elemento.findById(Elemento.class,elementoid);
            List<ElemenNivel> elenivdescriptions= elemento.getDescriptions();
            Log.d("onBindViewHolder: ", "Count: "+elenivdescriptions.size());
            if (!elenivdescriptions.isEmpty()) {
                if(position<elenivdescriptions.size()) {
                    ElemenNivel elenivdescription = elenivdescriptions.get(position);
                    mViewHolderEleNiv.Edesc.setText(elenivdescription.getDescription());
                }
            }
        //}*/
        if(selector) {
            mViewHolderEleNiv.Edesc.setVisibility(View.VISIBLE);
            mViewHolderEleNiv.Edesc.setTag(position);
        }else{
            mViewHolderEleNiv.Edesc.setVisibility(View.VISIBLE);
            mViewHolderEleNiv.Edesc.setFocusable(false);
            mViewHolderEleNiv.Edesc.setFocusableInTouchMode(false);
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
