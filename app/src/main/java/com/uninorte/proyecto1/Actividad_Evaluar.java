package com.uninorte.proyecto1;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class Actividad_Evaluar extends AppCompatActivity {

    private TextView tvMateria,tvRubrica;
    private EditText etNota;
    private Spinner spEstud,spCat,spEle;
    private RecyclerView list;

    private Rubrica rubrica;
    private Materia materia;
    private List<Estudiante> estudianteList;
    private List<Categoria> categoriaList;
    private List<Nivel> nivelList;
    private List<Elemento> elementoList;

    private SpinnerAdapterEstud spinnerAdapterEstud;
    private SpinnerAdapterCat spinnerAdapterCat;
    private SpinnerAdapterEle spinnerAdapterEle;

    private CustomAdapterNivel customAdapterNivel;

    CoordinatorLayout layoutRoot;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);
        list = (RecyclerView)findViewById(R.id.ReciclerView);

        materia= Materia.findById(Materia.class,getIntent().getLongExtra("Mat_id",0));
        rubrica=Rubrica.findById(Rubrica.class,getIntent().getLongExtra("Rub_id",0));

        tvMateria = (TextView) findViewById(R.id.textViewMateria);
        tvMateria.setText(materia.getName());

        tvRubrica = (TextView) findViewById(R.id.textViewRubrica);
        tvRubrica.setText(rubrica.getName());

        spEstud=(Spinner) findViewById(R.id.spinnerEstud);
        spCat=(Spinner) findViewById(R.id.spinnerCateg);
        spEle=(Spinner) findViewById(R.id.spinnerElemento);

        etNota=(EditText) findViewById(R.id.editTextNota);
        etNota.setFilters(new InputFilter[]{new InputFilterMinMax("0", "5")});

        if(Estudiante.count(Estudiante.class)>0 && Categoria.count(Categoria.class)>0 && Nivel.count(Nivel.class)>0){
            estudianteList= materia.getEstudiantes();
            categoriaList= rubrica.getCategorias();

            nivelList=rubrica.getNiveles();
            customAdapterNivel = new CustomAdapterNivel(this,nivelList);
            list.setAdapter(customAdapterNivel);
            list.setLayoutManager(new LinearLayoutManager(this));

            spinnerAdapterEstud= new SpinnerAdapterEstud(this, android.R.layout.simple_spinner_item, estudianteList);
            spEstud.setAdapter(spinnerAdapterEstud);

            spinnerAdapterCat= new SpinnerAdapterCat(this, android.R.layout.simple_spinner_item, categoriaList);
            spCat.setAdapter(spinnerAdapterCat);

            spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(Nivel.count(Nivel.class)>0){
                        elementoList=spinnerAdapterCat.getItem(i).getElementos();
                        spinnerAdapterEle=new SpinnerAdapterEle(Actividad_Evaluar.this, android.R.layout.simple_spinner_item, elementoList);
                        spEle.setAdapter(spinnerAdapterEle);

                        spEle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                list.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                list.setVisibility(View.GONE);
                            }
                        });

                        if(elementoList.isEmpty()){
                            Snackbar.make(layoutRoot, "No hay Elementos.", Snackbar.LENGTH_LONG).show();
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



            if (estudianteList.isEmpty()){
                Snackbar.make(layoutRoot, "No hay Estudiantes.", Snackbar.LENGTH_LONG).show();
            }

            if (categoriaList.isEmpty()){
                Snackbar.make(layoutRoot, "No hay Categorias.", Snackbar.LENGTH_LONG).show();
            }

            if (nivelList.isEmpty()){
                Snackbar.make(layoutRoot, "No hay Niveles.", Snackbar.LENGTH_LONG).show();
            }else{
                customAdapterNivel.SetOnItemClickListener(new CustomAdapterNivel.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });
            }

        }





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Actividad_Materias.this, R.string.up, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
