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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Actividad_AddDescriptions extends AppCompatActivity {

    private RecyclerView list;
    private List<Nivel> nivelesList = new ArrayList<>();
    private CustomAdapterEleNiv customAdapterEleNiv;
    long initialCount;
    int modifyPos = -1;
    private Long nivelId;
    private EditText description;
    private EditText nota;
    private TextView title;
    private Long Eleid;
    private Rubrica rubrica;
    private Boolean mode;

    CoordinatorLayout layoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_descriptions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);
        title=(TextView)findViewById(R.id.toolbar_title);
        mode=getIntent().getBooleanExtra("selector",true);

        if(mode){
            title.setText("Añadir Descripcion");
        }else{
            title.setText("Añadir Nota");
        }

        initialCount = Nivel.count(Nivel.class);
        Log.d("create", "onCreate initialcountCat: "+initialCount);
        list = (RecyclerView)findViewById(R.id.ReciclerView);
        Eleid= getIntent().getLongExtra("Ele_id",0);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");

        if(initialCount>=0){

                Elemento elemento = Elemento.findById(Elemento.class,Eleid);
                Categoria categoria = Categoria.findById(Categoria.class, elemento.getCategoria());
                rubrica = Rubrica.findById(Rubrica.class, categoria.getRubrica());


                nivelesList = rubrica.getNiveles();

                customAdapterEleNiv = new CustomAdapterEleNiv(this, nivelesList, true,elemento.getId());
                list.setAdapter(customAdapterEleNiv);
                list.setLayoutManager(new LinearLayoutManager(this));

                if (nivelesList.isEmpty())
                    Snackbar.make(layoutRoot, "No hay Niveles en la Rubrica.", Snackbar.LENGTH_LONG).show();

        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                boolean selector;
                Elemento elemento = Elemento.findById(Elemento.class,Eleid);
                List<ElemenNivel> elenivdescriptions = elemento.getDescriptions();
                Log.d("ElemenNivel", "selector: "+elenivdescriptions.size());

                for (int i = 0; i < customAdapterEleNiv.getItemCount(); i++){
                    nivelId=Long.valueOf(String.valueOf(i));
                    description=(EditText) list.findViewHolderForLayoutPosition(i).itemView.findViewById(R.id.editTextNivel);
                    nota=(EditText) list.findViewHolderForLayoutPosition(i).itemView.findViewById(R.id.editTextPeso);
                    if(elenivdescriptions.isEmpty()){
                        selector=false;
                    }else{
                        if(i<elenivdescriptions.size())
                        {
                            selector=true;
                        }else{
                            selector=false;
                        }

                    }
                    if(mode) {
                        EditorCreatorEleNiv(Eleid, nivelId, description.getText().toString(), selector);
                    }

                }
                finish();
            }
        });
    }

    public void EditorCreatorEleNiv(Long Elemento,Long Nivel, String description,boolean credit){
        if (!credit){
            Log.d("ElemenNivel", "saving");
            ElemenNivel elenivdescription = new ElemenNivel(Elemento,Nivel,description);
            elenivdescription.save();
        }else{

            List<ElemenNivel> elemenNiveles = ElemenNivel.find(ElemenNivel.class,"elemento = ? and nivel = ?",String.valueOf(Elemento),String.valueOf(Nivel));
            if(elemenNiveles.size()>0){
                ElemenNivel elemenNivel = elemenNiveles.get(0);
                if(!elemenNivel.getDescription().equals(description)) {
                    Log.d("ElemenNivel", "updating");
                    elemenNivel.setDescription(description);
                    elemenNivel.save();
                }
            }
        }
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


            Nivel nivel = Nivel.last(Nivel.class);

            //Log.d("Elemento","nombre Categoria: "+categoria.getCategoria().toString());
            nivelesList.add(nivel);
            customAdapterEleNiv.notifyItemInserted((int) newCount);

            initialCount = newCount;
        }

        if (modifyPos != -1) {
            nivelesList.set(modifyPos, rubrica.getNiveles().get(modifyPos));
            customAdapterEleNiv.notifyItemChanged(modifyPos);
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
            Intent i = new Intent(Actividad_AddDescriptions.this,Home.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }


}
