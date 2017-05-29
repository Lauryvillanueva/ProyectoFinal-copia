package com.uninorte.proyecto1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class Actividad_Materias extends AppCompatActivity {
  private FirebaseAuth auth;
    private RecyclerView list;
    private List<Materia> materiasList = new ArrayList<>();
    private CustomAdapterMat customAdapterMat;
    long initialCount;
    int modifyPos = -1;

    private DatabaseReference mDatabase;

    CoordinatorLayout layoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);

        auth=FirebaseAuth.getInstance();
        mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Materia");
        //initialCount= Materia.count(Materia.class);


        list = (RecyclerView)findViewById(R.id.ReciclerView);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");

        materiasList=new ArrayList<>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initialCount = dataSnapshot.getChildrenCount();
                Log.d("create", "onCreate initialcountMat: "+initialCount);
                if(initialCount>=0){

                    //materiasList = Materia.listAll(Materia.class);

                    for (DataSnapshot snap:dataSnapshot.getChildren()){
                        materiasList.add(snap.getValue(Materia.class));
                    }
                    customAdapterMat = new CustomAdapterMat(Actividad_Materias.this,materiasList);
                    list.setAdapter(customAdapterMat);
                    list.setLayoutManager(new LinearLayoutManager(Actividad_Materias.this));

                    if (materiasList.isEmpty())
                        Snackbar.make(layoutRoot, "No hay Materias.", Snackbar.LENGTH_LONG).show();
                    else{

                        customAdapterMat.SetOnItemClickListener(new CustomAdapterMat.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Log.d("Materia","click "+position);
                                Intent i= new Intent(Actividad_Materias.this,Actividad_Estudiantes.class);
                                i.putExtra("Mat_name", materiasList.get(position).getName());
                                startActivity(i);

                            }
                        });

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Actividad_Materias.this,Agregar.class);
                i.putExtra("title","Materia");
                startActivity(i);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(Actividad_Materias.this, R.string.up, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Actividad_Materias.this,Home.class);
                startActivity(i);
            }
        });


        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                //Remove swiped item from list and notify the RecyclerView

                final int position = viewHolder.getAdapterPosition();
                final Materia materia = materiasList.get(viewHolder.getAdapterPosition());
                materiasList.remove(viewHolder.getAdapterPosition());
                customAdapterMat.notifyItemRemoved(position);

                //materia.delete();
                mDatabase.child(materia.getKey()).removeValue();
                initialCount -= 1;

                Snackbar.make(layoutRoot, "Materia Eliminada", Snackbar.LENGTH_SHORT)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                               // materia.save();
                                mDatabase.child(materia.getKey()).setValue(materia);
                                materiasList.add(position, materia);
                                customAdapterMat.notifyItemInserted(position);
                                initialCount += 1;

                            }
                        })
                        .show();
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(list);




    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("modify", modifyPos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        modifyPos = savedInstanceState.getInt("modify");
    }

    @Override
    protected void onResume() {
        super.onResume();

        //final long newCount = Materia.count(Materia.class);

        //final long newCount = new Counts().countClass("Materia");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final long newCount = dataSnapshot.getChildrenCount();
                if (newCount > initialCount) {

                    Log.d("Main", "Adding new materia");


                    // Materia materia = Materia.last(Materia.class);

                    Query lastQuery= mDatabase.limitToLast(1);
                    lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Materia materia= dataSnapshot.getValue(Materia.class);
                            materiasList.add(materia);

                            customAdapterMat.notifyItemInserted((int) newCount);

                            initialCount = newCount;

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
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (modifyPos != -1) {
                    int cont=0;
                    for (DataSnapshot snap: dataSnapshot.getChildren()){
                        if (cont==modifyPos){
                            //materiasList.set(modifyPos, Materia.listAll(Materia.class).get(modifyPos));
                            materiasList.set(modifyPos, snap.getValue(Materia.class));
                            break;
                        }
                        cont++;
                    }
                    customAdapterMat.notifyDataSetChanged();
                    //customAdapterMat.notifyItemChanged(modifyPos);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            action("Inicio");
            Intent i = new Intent(Actividad_Materias.this,Home.class);
            startActivity(i);
        } if (id == R.id.exit) {
             logoutUser();
        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick_Edit(View view){

        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Materias.this,Agregar.class);
        i.putExtra("isEditing", true);
        i.putExtra("Mat_name", materiasList.get(position).getName());
        i.putExtra("title","Materia");

        modifyPos = position;
        startActivity(i);

    }
    public void onClick_View(View view){
        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Materias.this,Agregar.class);
        i.putExtra("isViewing", true);
        i.putExtra("Mat_name", materiasList.get(position).getName());
        i.putExtra("title","Materia");
        startActivity(i);
    }
    public void onClick_Note(View view){
        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Materias.this,Actividad_Evaluaciones.class);
        i.putExtra("Mat_id",materiasList.get(position).getId());
        startActivity(i);


    }
    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }

    public void logoutUser() {
        auth.signOut();
        if(auth.getCurrentUser() == null)
        {
            startActivity(new Intent(Actividad_Materias.this,Welcome.class));
            finish();
        }
    }

}
