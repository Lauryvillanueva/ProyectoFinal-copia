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

public class Actividad_Niveles extends AppCompatActivity {

    private RecyclerView list;
    private List<Nivel> nivelesList = new ArrayList<>();
    private CustomAdapterNivel customAdapterNivel;
    private Rubrica rubricaNivel;
    private String RubName;
    private TextView title;
    long initialCount;
    int modifyPos = -1;

    CoordinatorLayout layoutRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niveles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title=(TextView) findViewById(R.id.toolbar_title);
        title.setText("Niveles");

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);

        RubName=getIntent().getStringExtra("Rub_name");

        initialCount = Nivel.count(Nivel.class);
        Log.d("create", "onCreate initialcountNivel: "+initialCount);
        list = (RecyclerView)findViewById(R.id.ReciclerView);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");

        if(initialCount>=0){
            rubricaNivel= Rubrica.find(Rubrica.class,"name = ?",RubName).get(0);

            //estudiantesList = Estudiante.find(Estudiante.class,"materia=?",new String[]{materiaEstud.getId().toString()});
            nivelesList = rubricaNivel.getNiveles();

            customAdapterNivel = new CustomAdapterNivel(this,nivelesList);
            list.setAdapter(customAdapterNivel);
            list.setLayoutManager(new LinearLayoutManager(this));

            if (nivelesList.isEmpty())
                Snackbar.make(layoutRoot, "No hay Niveles.", Snackbar.LENGTH_LONG).show();
            else{
                customAdapterNivel.SetOnItemClickListener(new CustomAdapterNivel.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("Niveles", "click");
                    }
                });
            }

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Actividad_Niveles.this,Agregar.class);
                i.putExtra("title","Nivel");
                i.putExtra("Rub_name",RubName);
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
                final Nivel nivel = nivelesList.get(viewHolder.getAdapterPosition());
                nivelesList.remove(viewHolder.getAdapterPosition());
                customAdapterNivel.notifyItemRemoved(position);

                nivel.delete();
                initialCount -= 1;

                Snackbar.make(layoutRoot, "Nivel Eliminado", Snackbar.LENGTH_SHORT)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                nivel.save();
                                nivelesList.add(position, nivel);
                                customAdapterNivel.notifyItemInserted(position);
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

        final long newCount = Nivel.count(Nivel.class);

        if (newCount > initialCount) {

            Log.d("Main", "Adding new Nivel");


            Nivel nivel = Nivel.last(Nivel.class);

            Log.d("Niveles","nombre Rubrica: "+nivel.getRubrica().toString());
            nivelesList.add(nivel);
            customAdapterNivel.notifyItemInserted((int) newCount);

            initialCount = newCount;
        }

        if (modifyPos != -1) {
            nivelesList.set(modifyPos, rubricaNivel.getNiveles().get(modifyPos));
            customAdapterNivel.notifyItemChanged(modifyPos);
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
            Intent i = new Intent(Actividad_Niveles.this,Home.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick_Edit(View view){

        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Niveles.this,Agregar.class);
        i.putExtra("isEditing", true);
        i.putExtra("Rub_name",RubName);
        i.putExtra("NivelCat_name",nivelesList.get(position).getName());
        i.putExtra("title","Nivel");

        modifyPos = position;

        startActivity(i);


    }
    public void onClick_View(View view){
        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Niveles.this,Agregar.class);
        i.putExtra("isViewing", true);
        i.putExtra("Rub_name",RubName);
        i.putExtra("NivelCat_name",nivelesList.get(position).getName());
        i.putExtra("title","Nivel");
        startActivity(i);
    }
    public void onClick_Note(View view){

    }
    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }


}
