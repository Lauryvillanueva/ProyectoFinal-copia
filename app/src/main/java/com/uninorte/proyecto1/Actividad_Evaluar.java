package com.uninorte.proyecto1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Actividad_Evaluar extends AppCompatActivity {

    private TextView tvMateria,tvRubrica,tvElemento,tvNota;
    private EditText etNota;
    private MaterialSpinner spEstud,spCat,spEle;
    private RecyclerView list;


    private Rubrica rubrica;
    private Materia materia;
    private List<Estudiante> estudianteList;
    private List<Categoria> categoriaList;
    private List<Nivel> nivelList;
    private List<Elemento> elementoList;

    private Estudiante estudiante;
    private String evaluacionId;
    private Elemento elemento;

    private SpinnerAdapterEstud spinnerAdapterEstud;
    private SpinnerAdapterCat spinnerAdapterCat;
    private SpinnerAdapterEle spinnerAdapterEle;

    private CustomAdapterNivel customAdapterNivel;

    CoordinatorLayout layoutRoot;

    private boolean bEstudiante,bElemento;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        estudiante=null;
        elemento=null;

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);
        list = (RecyclerView)findViewById(R.id.ReciclerView);

        evaluacionId=getIntent().getStringExtra("Eva_id");

        materia= Materia.findById(Materia.class,getIntent().getLongExtra("Mat_id",0));
        rubrica=Rubrica.findById(Rubrica.class,getIntent().getLongExtra("Rub_id",0));

        tvMateria = (TextView) findViewById(R.id.textViewMateria);
        tvMateria.setText(materia.getName());

        tvRubrica = (TextView) findViewById(R.id.textViewRubrica);
        tvRubrica.setText(rubrica.getName());

        tvElemento=(TextView) findViewById(R.id.textViewElemento);
        tvElemento.setVisibility(View.INVISIBLE);

        spEstud=(MaterialSpinner) findViewById(R.id.spinnerEstud);
        spCat=(MaterialSpinner) findViewById(R.id.spinnerCateg);
        spEle=(MaterialSpinner) findViewById(R.id.spinnerElemento);
        spEle.setVisibility(View.INVISIBLE);


        tvNota=(TextView) findViewById(R.id.textViewNote);
        etNota=(EditText) findViewById(R.id.editTextNota);
        //etNota.setFilters(new InputFilter[]{new InputFilterMinMax("0", "5")});
        tvNota.setVisibility(View.INVISIBLE);
        etNota.setVisibility(View.INVISIBLE);

        bEstudiante=false;
        bElemento=false;

        if(Estudiante.count(Estudiante.class)>0 && Categoria.count(Categoria.class)>0 && Nivel.count(Nivel.class)>0){
            estudianteList= materia.getEstudiantes();
            categoriaList= rubrica.getCategorias();

            nivelList=rubrica.getNiveles();
            customAdapterNivel = new CustomAdapterNivel(this,nivelList,false);
            list.setAdapter(customAdapterNivel);
            list.setLayoutManager(new LinearLayoutManager(this));

            spinnerAdapterEstud= new SpinnerAdapterEstud(this, android.R.layout.simple_spinner_item, estudianteList);
            spinnerAdapterEstud.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spEstud.setAdapter(spinnerAdapterEstud);
            spEstud.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i!=-1){
                        bEstudiante=true;
                        estudiante=estudianteList.get(i);
                        if(bEstudiante && bElemento){
                            tvNota.setVisibility(View.VISIBLE);
                            etNota.setVisibility(View.VISIBLE);
                                /*NotaEstudElemento notaEstudElemento=estudiante.findRegister(evaluacionId,elemento.getId());
                                if(notaEstudElemento!=null){
                                    etNota.setText(String.valueOf(notaEstudElemento.getNota()));
                                }else{
                                    etNota.setText("");
                                }*/
                        }
                    }else{
                        tvNota.setVisibility(View.INVISIBLE);
                        etNota.setVisibility(View.INVISIBLE);
                        bEstudiante=false;
                        etNota.setText("");
                        estudiante=null;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    Log.d("SpinnerEstud", "onNothingSelected");
                }
            });

            spinnerAdapterCat= new SpinnerAdapterCat(this, android.R.layout.simple_spinner_item, categoriaList);
            spinnerAdapterCat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spCat.setAdapter(spinnerAdapterCat);

            spCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(Nivel.count(Nivel.class)>0){
                        if(i!=-1) {
                            elementoList = spinnerAdapterCat.getItem(i).getElementos();
                            spinnerAdapterEle = new SpinnerAdapterEle(Actividad_Evaluar.this, android.R.layout.simple_spinner_item, elementoList);
                            spinnerAdapterEle.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spEle.setAdapter(spinnerAdapterEle);
                            tvElemento.setVisibility(View.VISIBLE);
                            spEle.setVisibility(View.VISIBLE);


                            spEle.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long lon) {
                                    if(position!=-1){
                                        elemento=elementoList.get(position);
                                        bElemento=true;
                                        list.setVisibility(View.VISIBLE);
                                        if(bEstudiante && bElemento){
                                            tvNota.setVisibility(View.VISIBLE);
                                            etNota.setVisibility(View.VISIBLE);
                                               /* NotaEstudElemento notaEstudElemento=estudiante.findRegister(evaluacionId,elemento.getId());
                                                if(notaEstudElemento!=null){
                                                    Log.d("SpinnerEstud", "onItemSelected: "+ notaEstudElemento.getEstudiante());
                                                    etNota.setText(String.valueOf(notaEstudElemento.getNota()));
                                                }else{
                                                    Log.d("SpinnerEstud", "onItemSelected: no nota");
                                                    etNota.setText("");
                                                }*/
                                        }
                                    }else{
                                        bElemento=false;
                                        tvNota.setVisibility(View.INVISIBLE);
                                        etNota.setVisibility(View.INVISIBLE);
                                        list.setVisibility(View.GONE);
                                        etNota.setText("");
                                        elemento=null;
                                    }
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {
                                    list.setVisibility(View.GONE);
                                }
                            });

                            if (elementoList.isEmpty()) {
                                Snackbar.make(layoutRoot, "No hay Elementos.", Snackbar.LENGTH_LONG).show();
                                list.setVisibility(View.GONE);

                            }
                        }else{
                            list.setVisibility(View.GONE);
                            tvElemento.setVisibility(View.INVISIBLE);
                            spEle.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                    list.setVisibility(View.GONE);
                    spEle.setVisibility(View.INVISIBLE);
                    tvElemento.setVisibility(View.INVISIBLE);
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
                       String description="";

                       List <ElemenNivel> elemenNivels = ((Elemento) spEle.getSelectedItem()).getDescriptions();
                        if (!elemenNivels.isEmpty()) {
                            if(nivelList.get(position).getId()<=elemenNivels.size()) {
                                description=elemenNivels.get(nivelList.get(position).getId().intValue()-1).getDescription();
                            }else{
                                Toast.makeText(Actividad_Evaluar.this, "No hay descripcion Guardadas para este Nivel", Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(Actividad_Evaluar.this, "No hay descripciones Guardadas para este Elemento", Toast.LENGTH_SHORT).show();
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(Actividad_Evaluar.this);
                        LayoutInflater inflater = getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.activity_nivelview,null);
                       builder.setView(dialogView);
                        TextView title= (TextView) dialogView.findViewById(R.id.toolbar_title);
                        title.setText(nivelList.get(position).getName());
                        TextView Description= (TextView) dialogView.findViewById(R.id.TextViewDescription);
                        Description.setText(description);
                        builder.create();
                        builder.show().getWindow().setLayout(600,500);
                    }
                });
            }

        }





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNota();
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
            Intent i = new Intent(Actividad_Evaluar.this,Home.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }


    public void saveNota (){
        if(estudiante!=null){
            if(elemento!=null){
                if(!TextUtils.isEmpty(etNota.getText().toString())){
                    Double calificacion = Double.parseDouble(etNota.getText().toString());
                    /*List<NotaEstudElemento> notaEstudElementos= estudiante.getNotas(evaluacionId);
                    NotaEstudElemento nota;
                    Log.d("Notas", ""+notaEstudElementos.isEmpty());
                    if(!notaEstudElementos.isEmpty()){
                        NotaEstudElemento notaEstudElemento=estudiante.findRegister(evaluacionId,elemento.getId());
                        if(notaEstudElemento!=null){
                            Log.d("Notas", "updating");
                            notaEstudElemento.setNota(calificacion);
                            notaEstudElemento.save();
                            Toast.makeText(Actividad_Evaluar.this, "Nota Actualizada Exitosamente", Toast.LENGTH_SHORT).show();
                        }else{
                            Log.d("Notas", "saving");
                            nota= new NotaEstudElemento(estudiante.getKey(),evaluacionId,elemento.getKey(),calificacion);
                            nota.save();
                            Toast.makeText(Actividad_Evaluar.this, "Nota Guardada Exitosamente", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Log.d("Notas", "saving");
                        nota= new NotaEstudElemento(estudiante.getKey(),evaluacionId,elemento.getKey(),calificacion);
                        nota.save();
                        Toast.makeText(Actividad_Evaluar.this, "Nota Guardada Exitosamente", Toast.LENGTH_SHORT).show();
                    }*/
                }else{
                    etNota.setError("No puede estar vacio");
                }
            }else{
                Toast.makeText(Actividad_Evaluar.this, "Seleccionar Elemento", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Actividad_Evaluar.this, "Seleccionar Estudiante", Toast.LENGTH_SHORT).show();
        }
    }

    /*public void dialogSave(String texto){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int choice) {
                switch (choice) {
                    case DialogInterface.BUTTON_POSITIVE:
                            saveNota();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(Actividad_Evaluar.this);
        builder.setMessage("Desea guardar la nota antes de cambiar de "+texto)
                .setPositiveButton("Si", dialogClickListener)
                .setNegativeButton("No",dialogClickListener);

    }*/

}
