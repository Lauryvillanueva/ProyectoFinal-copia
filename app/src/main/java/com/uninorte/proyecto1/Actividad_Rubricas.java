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

public class Actividad_Rubricas extends AppCompatActivity {

    private RecyclerView list;
    private List<Rubrica> rubricasList = new ArrayList<>();
    private CustomAdapterRub customAdapterRub;
    long initialCount;
    int modifyPos = -1;

    CoordinatorLayout layoutRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rubricas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);

        initialCount= Rubrica.count(Rubrica.class);
        Log.d("create", "onCreate initialcountMat: "+initialCount);
        list = (RecyclerView)findViewById(R.id.ReciclerView);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");

        if(initialCount>=0){

            rubricasList = Rubrica.listAll(Rubrica.class);
            customAdapterRub = new CustomAdapterRub(this,rubricasList);
            list.setAdapter(customAdapterRub);
            list.setLayoutManager(new LinearLayoutManager(this));

            if (rubricasList.isEmpty())
                Snackbar.make(layoutRoot, "No hay Rubricas.", Snackbar.LENGTH_LONG).show();
            else{

                customAdapterRub.SetOnItemClickListener(new CustomAdapterRub.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("Rubrica","click "+position);
                        /*Intent i= new Intent(Actividad_Rubricas.this,Actividad_Estudiantes.class);
                        i.putExtra("Rub_name", rubricasList.get(position).getName());
                        startActivity(i);*/

                    }
                });

            }

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Actividad_Rubricas.this,Agregar.class);
                i.putExtra("title","Rubrica");
                startActivity(i);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Actividad_Materias.this, R.string.up, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(Actividad_Rubricas.this,Home.class);
                startActivityForResult(i,1);
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
                final Rubrica rubrica = rubricasList.get(viewHolder.getAdapterPosition());
                rubricasList.remove(viewHolder.getAdapterPosition());
                customAdapterRub.notifyItemRemoved(position);

                rubrica.delete();
                initialCount -= 1;

                Snackbar.make(layoutRoot, "Rubrica Eliminada", Snackbar.LENGTH_SHORT)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                rubrica.save();
                                rubricasList.add(position, rubrica);
                                customAdapterRub.notifyItemInserted(position);
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

        final long newCount = Rubrica.count(Rubrica.class);

        if (newCount > initialCount) {

            Log.d("Main", "Adding new rubrica");


            Rubrica rubrica = Rubrica.last(Rubrica.class);

            rubricasList.add(rubrica);
            customAdapterRub.notifyItemInserted((int) newCount);

            initialCount = newCount;
        }

        if (modifyPos != -1) {
            rubricasList.set(modifyPos, Rubrica.listAll(Rubrica.class).get(modifyPos));
            customAdapterRub.notifyItemChanged(modifyPos);
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
            Intent i = new Intent(Actividad_Rubricas.this,Home.class);
            startActivityForResult(i,1);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClick_Edit(View view){

        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Rubricas.this,Agregar.class);
        i.putExtra("isEditing", true);
        i.putExtra("Rub_name", rubricasList.get(position).getName());
        i.putExtra("title","Rubrica");

        modifyPos = position;

        startActivity(i);


    }
    public void onClick_View(View view){
        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Rubricas.this,VistaRubricas.class);
        i.putExtra("RubricaId", rubricasList.get(position).getId());
        startActivity(i);
    }
    public void onClick_Note(View view){

    }
    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }


}
