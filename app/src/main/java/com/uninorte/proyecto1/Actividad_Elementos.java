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

public class Actividad_Elementos extends AppCompatActivity {

    private RecyclerView list;
    private List<Elemento> elementosList = new ArrayList<>();
    private CustomAdapterEle customAdapterEle;
    private Categoria categoriaEle;
    private String CatName;
    private TextView title;
    long initialCount;
    int modifyPos = -1;

    CoordinatorLayout layoutRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elementos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title=(TextView) findViewById(R.id.toolbar_title);
        title.setText("Elementos");

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);

        CatName=getIntent().getStringExtra("Cat_name");

        initialCount = Elemento.count(Elemento.class);
        Log.d("create", "onCreate initialcountCat: "+initialCount);
        list = (RecyclerView)findViewById(R.id.ReciclerView);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");

        if(initialCount>=0){
            categoriaEle= Categoria.find(Categoria.class,"name = ?",CatName).get(0);

            //estudiantesList = Estudiante.find(Estudiante.class,"materia=?",new String[]{materiaEstud.getId().toString()});
            elementosList = categoriaEle.getElementos();

            customAdapterEle = new CustomAdapterEle(this,elementosList);
            list.setAdapter(customAdapterEle);
            list.setLayoutManager(new LinearLayoutManager(this));

            if (elementosList.isEmpty())
                Snackbar.make(layoutRoot, "No hay Elementos.", Snackbar.LENGTH_LONG).show();
            else{
                customAdapterEle.SetOnItemClickListener(new CustomAdapterEle.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Log.d("Elementos", "click");
                    }
                });
            }

        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Actividad_Elementos.this,Agregar.class);
                i.putExtra("title","Elemento");
                i.putExtra("Cat_name",CatName);
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
                final Elemento categoria = elementosList.get(viewHolder.getAdapterPosition());
                elementosList.remove(viewHolder.getAdapterPosition());
                customAdapterEle.notifyItemRemoved(position);

                categoria.delete();
                initialCount -= 1;

                Snackbar.make(layoutRoot, "Nivel Eliminado", Snackbar.LENGTH_SHORT)
                        .setAction("DESHACER", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                categoria.save();
                                elementosList.add(position, categoria);
                                customAdapterEle.notifyItemInserted(position);
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

        final long newCount = Elemento.count(Elemento.class);

        if (newCount > initialCount) {

            Log.d("Main", "Adding new Elemento");


            Elemento elemento = Elemento.last(Elemento.class);

            //Log.d("Elemento","nombre Categoria: "+categoria.getCategoria().toString());
            elementosList.add(elemento);
            customAdapterEle.notifyItemInserted((int) newCount);

            initialCount = newCount;
        }

        if (modifyPos != -1) {
            elementosList.set(modifyPos, categoriaEle.getElementos().get(modifyPos));
            customAdapterEle.notifyItemChanged(modifyPos);
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
            Intent i = new Intent(Actividad_Elementos.this,Home.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }
    public void onClick_Edit(View view){

        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Elementos.this,Agregar.class);
        i.putExtra("isEditing", true);
        i.putExtra("Cat_name",CatName);
        i.putExtra("Ele_name",elementosList.get(position).getName());
        i.putExtra("CatEle_peso",elementosList.get(position).getPeso());
        i.putExtra("title","Elemento");

        modifyPos = position;

        startActivity(i);


    }
    public void onClick_View(View view){
        int position = (int) view.getTag();
        Intent i = new Intent(Actividad_Elementos.this,Agregar.class);
        i.putExtra("isViewing", true);
        i.putExtra("Cat_name",CatName);
        i.putExtra("Ele_name",elementosList.get(position).getName());
        i.putExtra("CatEle_peso",elementosList.get(position).getPeso());
        i.putExtra("title","Elemento");
        startActivity(i);
    }
    public void onClick_Note(View view){

    }
    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }


}
