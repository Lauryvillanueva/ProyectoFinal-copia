package com.uninorte.proyecto1;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class VistaRubricas extends AppCompatActivity {
    private RecyclerView list;
    private List<Categoria> categoriaList;
    private List<Nivel> nivelList;
    private Rubrica rubrica;
    private TextView title;
    private long initialcount,nivel;
    private CustomAdapterViewCat customAdapterViewCat;
    private MaterialSpinner spNivel;
    private SpinnerAdapterNiv spinnerAdapterNiv;
    CoordinatorLayout layoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_rubricas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nivel=-1;
        list= (RecyclerView) findViewById(R.id.ReciclerView);
        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);
        title=(TextView)findViewById(R.id.toolbar_title);

        rubrica=Rubrica.findById(Rubrica.class,getIntent().getLongExtra("RubricaId",0));

        spNivel=(MaterialSpinner) findViewById(R.id.spinnerNivel);
        if(Nivel.count(Nivel.class)>=0) {
            nivelList=rubrica.getNiveles();


            if(nivelList.isEmpty()){
                Snackbar.make(layoutRoot, "No hay Niveles.", Snackbar.LENGTH_LONG).show();
            }else{
                spinnerAdapterNiv = new SpinnerAdapterNiv(this, android.R.layout.simple_spinner_item, nivelList);
                spinnerAdapterNiv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spNivel.setAdapter(spinnerAdapterNiv);
                spNivel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(i!=-1){
                            initialcount=Categoria.count(Elemento.class);

                            if(initialcount>=0){
                                categoriaList=rubrica.getCategorias();


                                if(!categoriaList.isEmpty()){
                                    customAdapterViewCat=new CustomAdapterViewCat(VistaRubricas.this,categoriaList,nivelList.get(i).getId());
                                    list.setVisibility(View.VISIBLE);
                                    list.setAdapter(customAdapterViewCat);
                                    list.setLayoutManager(new LinearLayoutManager(VistaRubricas.this));


                                    customAdapterViewCat.SetOnItemClickListener(new CustomAdapterViewCat.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            Log.d("Categorias", "click");
                                        }
                                    });

                                }else{
                                    Snackbar.make(layoutRoot, "No hay Categorias.", Snackbar.LENGTH_LONG).show();
                                }

                            }

                        }else{
                            list.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                     //   .setAction("Action", null).show();
            }
        });
    }

}
