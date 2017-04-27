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

import java.util.ArrayList;
import java.util.List;

public class Actividad_Evaluaciones extends AppCompatActivity {

    private RecyclerView list;
    private List<Evaluacion> evaluacionesList = new ArrayList<>();
    private CustomAdapterEva customAdapterEva;
    private Materia materiaEle;
    private Long Matid;
    private TextView title;
    long initialCount;
    int modifyPos = -1;

    CoordinatorLayout layoutRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluaciones);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title=(TextView) findViewById(R.id.toolbar_title);
        title.setText("Evaluaciones");

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);

        Matid=getIntent().getLongExtra("Mat_id",0);

        initialCount = Evaluacion.count(Evaluacion.class);
        Log.d("create", "onCreate initialcountCat: "+initialCount);
        list = (RecyclerView)findViewById(R.id.ReciclerView);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");

        if(initialCount>=0){
            materiaEle= Materia.findById(Materia.class,Matid);

            //estudiantesList = Estudiante.find(Estudiante.class,"materia=?",new String[]{materiaEstud.getId().toString()});
            evaluacionesList = materiaEle.getEvaluaciones();

            customAdapterEva = new CustomAdapterEva(this,evaluacionesList);
            list.setAdapter(customAdapterEva);
            list.setLayoutManager(new LinearLayoutManager(this));

            if (evaluacionesList.isEmpty())
                Snackbar.make(layoutRoot, "No hay Evaluaciones.", Snackbar.LENGTH_LONG).show();
            else{
                customAdapterEva.SetOnItemClickListener(new CustomAdapterEva.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("Evaluacions", "click");
                    }
                });
            }

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Actividad_Evaluaciones.this,Agregar.class);
                i.putExtra("title","Evaluacion");
                i.putExtra("Mat_id",Matid);
                startActivity(i);
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Actividad_Materias.this, R.string.up, Toast.LENGTH_SHORT).show();
                finish();
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
                final Evaluacion evaluacion = evaluacionesList.get(viewHolder.getAdapterPosition());
                evaluacionesList.remove(viewHolder.getAdapterPosition());
                customAdapterEva.notifyItemRemoved(position);

                evaluacion.delete();
                initialCount -= 1;

                Snackbar.make(layoutRoot, "Evaluacion Eliminada", Snackbar.LENGTH_SHORT)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                evaluacion.save();
                                evaluacionesList.add(position, evaluacion);
                                customAdapterEva.notifyItemInserted(position);
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

        final long newCount = Evaluacion.count(Evaluacion.class);

        if (newCount > initialCount) {

            Log.d("Main", "Adding new Evaluacion");


            Evaluacion elemento = Evaluacion.last(Evaluacion.class);

            //Log.d("Evaluacion","nombre Materia: "+materia.getMateria().toString());
            evaluacionesList.add(elemento);
            customAdapterEva.notifyItemInserted((int) newCount);

            initialCount = newCount;
        }

        if (modifyPos != -1) {
            evaluacionesList.set(modifyPos, materiaEle.getEvaluaciones().get(modifyPos));
            customAdapterEva.notifyItemChanged(modifyPos);
        }

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
            Intent i = new Intent(Actividad_Evaluaciones.this,Home.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick_Edit(View view){

        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Evaluaciones.this,Agregar.class);
        i.putExtra("isEditing", true);
        i.putExtra("Mat_id",Matid);
        i.putExtra("Eva_name",evaluacionesList.get(position).getName());
        i.putExtra("title","Evaluacion");

        modifyPos = position;

        startActivity(i);


    }
    public void onClick_View(View view){
        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Evaluaciones.this,Agregar.class);
        i.putExtra("isViewing", true);
        i.putExtra("Mat_id",Matid);
        i.putExtra("Eva_name",evaluacionesList.get(position).getName());
        i.putExtra("Rub_id",evaluacionesList.get(position).getRubrica());
        i.putExtra("title","Evaluacion");
        startActivity(i);
    }
    public void onClick_Note(View view){

    }
    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }


}
