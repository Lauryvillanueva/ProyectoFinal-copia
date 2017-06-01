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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Actividad_Estudiantes extends AppCompatActivity {

    private RecyclerView list;
    private FirebaseAuth auth;
    private List<Estudiante> estudiantesList = new ArrayList<>();
    private CustomAdapterEstud customAdapterEstud;
    private Materia materiaEstud;
    private String MatName;
    private TextView title;
    long initialCount;
    int modifyPos = -1;

    private DatabaseReference mDatabase;

    CoordinatorLayout layoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estudiantes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        title=(TextView) findViewById(R.id.toolbar_title);

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);

        auth= FirebaseAuth.getInstance();
        mDatabase= FirebaseDatabase.getInstance().getReference("noterubric").child("Estudiante");

        MatName=getIntent().getStringExtra("Mat_name");

        //initialCount = Estudiante.count(Estudiante.class);


        Log.d("create", "onCreate initialcountEstud: "+initialCount);
        list = (RecyclerView)findViewById(R.id.ReciclerView);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");
        final DatabaseReference mDatabaseEst= FirebaseDatabase.getInstance().getReference("noterubric").child("Estudiante");
        final DatabaseReference mDatabaseMateria=FirebaseDatabase.getInstance().getReference("noterubric").child("Materia").child(MatName);

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initialCount = dataSnapshot.getChildrenCount();
                if(initialCount>=0){

                    mDatabaseMateria.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            materiaEstud= dataSnapshot.getValue(Materia.class);

                            //estudiantesList = materiaEstud.getEstudiantes();

                            mDatabaseEst.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshotestud) {
                                    for (DataSnapshot snapestud: dataSnapshotestud.getChildren()){
                                        Estudiante estud= snapestud.getValue(Estudiante.class);
                                        if(estud.getMateria().equals(MatName)){
                                            estudiantesList.add(estud);
                                        }
                                    }
                                    customAdapterEstud = new CustomAdapterEstud(Actividad_Estudiantes.this,estudiantesList);
                                    list.setAdapter(customAdapterEstud);
                                    list.setLayoutManager(new LinearLayoutManager(Actividad_Estudiantes.this));

                                    if (estudiantesList.isEmpty())
                                        Snackbar.make(layoutRoot, "No hay Estudiantes.", Snackbar.LENGTH_LONG).show();
                                    else{
                                        customAdapterEstud.SetOnItemClickListener(new CustomAdapterEstud.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {
                                                Log.d("Estudiantes", "click");
                                            }
                                        });
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //materiaEstud= Materia.find(Materia.class,"name = ?",MatName).get(0);


                    //estudiantesList = Estudiante.find(Estudiante.class,"materia=?",new String[]{materiaEstud.getId().toString()});


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
                Intent i = new Intent(Actividad_Estudiantes.this,Agregar.class);
                i.putExtra("title","Estudiante");
                i.putExtra("Mat_name",MatName);
                startActivity(i);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Actividad_Materias.this, R.string.up, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Actividad_Estudiantes.this,Actividad_Materias.class);
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
                final Estudiante estudiante = estudiantesList.get(viewHolder.getAdapterPosition());
                estudiantesList.remove(viewHolder.getAdapterPosition());
                customAdapterEstud.notifyItemRemoved(position);

                //estudiante.delete();
                mDatabase.child(estudiante.getKey()).removeValue();
                initialCount -= 1;

                Snackbar.make(layoutRoot, "Estudiante Eliminado", Snackbar.LENGTH_SHORT)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                //estudiante.save();
                                mDatabase.child(estudiante.getKey()).setValue(estudiante);
                                estudiantesList.add(position, estudiante);
                                customAdapterEstud.notifyItemInserted(position);
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

       // final long newCount = Estudiante.count(Estudiante.class);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final long newCount = dataSnapshot.getChildrenCount();
                if (newCount > initialCount) {

                Log.d("Main", "Adding new Estudiante");


                //Estudiante estudiante = Estudiante.last(Estudiante.class);
                    Query lastQuery= mDatabase.limitToLast(1);
                    lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Estudiante estudiante= dataSnapshot.getValue(Estudiante.class);
                            estudiantesList.add(estudiante);

                            customAdapterEstud.notifyItemInserted((int) newCount);

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
                            // estudiantesList.set(modifyPos, materiaEstud.getEstudiantes().get(modifyPos));
                            estudiantesList.set(modifyPos, snap.getValue(Estudiante.class));
                            break;
                        }
                        cont++;
                    }
                    customAdapterEstud.notifyItemChanged(modifyPos);
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
            Intent i = new Intent(Actividad_Estudiantes.this,Home.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick_Edit(View view){

        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Estudiantes.this,Agregar.class);
        i.putExtra("isEditing", true);
        i.putExtra("Mat_name",MatName);
        i.putExtra("Estud_name",estudiantesList.get(position).getName());
        i.putExtra("Estud_state",estudiantesList.get(position).getState());
        i.putExtra("title","Estudiante");

        modifyPos = position;

        startActivity(i);


    }
    public void onClick_View(View view){
        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Estudiantes.this,Agregar.class);
        i.putExtra("isViewing", true);
        i.putExtra("Mat_name",MatName);
        i.putExtra("Estud_name",estudiantesList.get(position).getName());
        i.putExtra("Estud_state",estudiantesList.get(position).getState());
        i.putExtra("title","Estudiante");
        startActivity(i);
    }
    public void onClick_Note(View view){

    }
    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }

}
