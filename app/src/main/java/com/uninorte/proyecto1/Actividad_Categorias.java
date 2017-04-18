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

public class Actividad_Categorias extends AppCompatActivity {

    private RecyclerView list;
    private List<Categoria> categoriasList = new ArrayList<>();
    private CustomAdapterCat customAdapterCat;
    private Rubrica rubricaCat;
    private String RubName;
    private TextView title;
    long initialCount;
    int modifyPos = -1;

    CoordinatorLayout layoutRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title=(TextView) findViewById(R.id.toolbar_title);
        title.setText("Categorias");

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);

        RubName=getIntent().getStringExtra("Rub_name");

        initialCount = Categoria.count(Categoria.class);
        Log.d("create", "onCreate initialcountCat: "+initialCount);
        list = (RecyclerView)findViewById(R.id.ReciclerView);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");

        if(initialCount>=0){
            rubricaCat= Rubrica.find(Rubrica.class,"name = ?",RubName).get(0);

            //estudiantesList = Estudiante.find(Estudiante.class,"materia=?",new String[]{materiaEstud.getId().toString()});
            categoriasList = rubricaCat.getCategorias();

            customAdapterCat = new CustomAdapterCat(this,categoriasList);
            list.setAdapter(customAdapterCat);
            list.setLayoutManager(new LinearLayoutManager(this));

            if (categoriasList.isEmpty())
                Snackbar.make(layoutRoot, "No hay Categorias.", Snackbar.LENGTH_LONG).show();
            else{
                customAdapterCat.SetOnItemClickListener(new CustomAdapterCat.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("Categorias", "click");
                        Intent i= new Intent(Actividad_Categorias.this,Actividad_Elementos.class);
                        i.putExtra("Cat_name", categoriasList.get(position).getName());
                        startActivity(i);
                    }
                });
            }

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Actividad_Categorias.this,Agregar.class);
                i.putExtra("title","Categoria");
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
                final Categoria categoria = categoriasList.get(viewHolder.getAdapterPosition());
                categoriasList.remove(viewHolder.getAdapterPosition());
                customAdapterCat.notifyItemRemoved(position);

                categoria.delete();
                initialCount -= 1;

                Snackbar.make(layoutRoot, "Nivel Eliminado", Snackbar.LENGTH_SHORT)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                categoria.save();
                                categoriasList.add(position, categoria);
                                customAdapterCat.notifyItemInserted(position);
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

        final long newCount = Categoria.count(Categoria.class);

        if (newCount > initialCount) {

            Log.d("Main", "Adding new Categoria");


            Categoria categoria = Categoria.last(Categoria.class);

            //Log.d("Categoria","nombre Rubrica: "+categoria.getRubrica().toString());
            categoriasList.add(categoria);
            customAdapterCat.notifyItemInserted((int) newCount);

            initialCount = newCount;
        }

        if (modifyPos != -1) {
            categoriasList.set(modifyPos, rubricaCat.getCategorias().get(modifyPos));
            customAdapterCat.notifyItemChanged(modifyPos);
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
            Intent i = new Intent(Actividad_Categorias.this,Home.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick_Edit(View view){

        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Categorias.this,Agregar.class);
        i.putExtra("isEditing", true);
        i.putExtra("Rub_name",RubName);
        i.putExtra("NivelCat_name",categoriasList.get(position).getName());
        i.putExtra("CatEle_peso",categoriasList.get(position).getPeso());
        i.putExtra("title","Categoria");

        modifyPos = position;

        startActivity(i);


    }
    public void onClick_View(View view){
        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Categorias.this,Agregar.class);
        i.putExtra("isViewing", true);
        i.putExtra("Rub_name",RubName);
        i.putExtra("NivelCat_name",categoriasList.get(position).getName());
        i.putExtra("CatEle_peso",categoriasList.get(position).getPeso());
        i.putExtra("title","Categoria");
        startActivity(i);
    }
    public void onClick_Note(View view){

    }
    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }


}
