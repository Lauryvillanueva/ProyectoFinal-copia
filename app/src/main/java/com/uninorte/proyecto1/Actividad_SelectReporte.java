
package com.uninorte.proyecto1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Actividad_SelectReporte extends AppCompatActivity {

    private LinearLayout layoutEval,layoutEstud;
    private TextView tvTitle;
    private MaterialSpinner spMateria,spEstud,spEvalu;
    private Button btnVerReporte;

    private String title;

    private SpinnerAdapterMat spinnerAdapterMat;
    private SpinnerAdapterEstud spinnerAdapterEstud;
    private SpinnerAdapterEval spinnerAdapterEval;

    private List<Materia> materiaList;
    private List<Evaluacion> evaluacionList;
    private List<Estudiante> estudianteList;

    private Evaluacion evaluacion;
    private Estudiante estudiante;

    CoordinatorLayout layoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectreporte);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);

        title=getIntent().getStringExtra("Reporte");
        tvTitle=(TextView) findViewById(R.id.toolbar_title);
        tvTitle.setText("Reporte "+title);

        btnVerReporte=(Button) findViewById(R.id.buttonVerReporte);

        layoutEval=(LinearLayout) findViewById(R.id.layoutEvaluacion);
        layoutEstud = (LinearLayout) findViewById(R.id.layoutEstudiante);

        spMateria=(MaterialSpinner) findViewById(R.id.spinnerMateria);
        spEstud=(MaterialSpinner) findViewById(R.id.spinnerEstudiante);
        spEvalu=(MaterialSpinner) findViewById(R.id.spinnerEvaluacion);

        if(Materia.count(Materia.class)>0){
            materiaList=Materia.listAll(Materia.class);
            spinnerAdapterMat= new SpinnerAdapterMat(this, android.R.layout.simple_spinner_item, materiaList);
            spinnerAdapterMat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spMateria.setAdapter(spinnerAdapterMat);
            spMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i!=-1) {
                        switch (title) {
                            case "Evaluacion":

                                if (Evaluacion.count(Evaluacion.class) > 0) {
                                    layoutEval.setVisibility(View.VISIBLE);
                                    evaluacionList = materiaList.get(i).getEvaluaciones();
                                    spinnerAdapterEval = new SpinnerAdapterEval(Actividad_SelectReporte.this, android.R.layout.simple_spinner_item, evaluacionList);
                                    spinnerAdapterEval.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spEvalu.setAdapter(spinnerAdapterEval);
                                    spEvalu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                            if(pos!=-1){
                                                evaluacion=evaluacionList.get(pos);
                                                btnVerReporte.setVisibility(View.VISIBLE);
                                            }else{
                                                btnVerReporte.setVisibility(View.GONE);
                                            }

                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                    if (evaluacionList.isEmpty()) {
                                        Snackbar.make(layoutRoot, "No hay Evaluaciones.", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                                break;
                            case "Estudiante":
                                if (Estudiante.count(Estudiante.class) > 0) {
                                    layoutEstud.setVisibility(View.VISIBLE);
                                    estudianteList = materiaList.get(i).getEstudiantes();
                                    spinnerAdapterEstud = new SpinnerAdapterEstud(Actividad_SelectReporte.this, android.R.layout.simple_spinner_item, estudianteList);
                                    spinnerAdapterEstud.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                    spEstud.setAdapter(spinnerAdapterEstud);
                                    spEstud.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                                            if(pos!=-1){
                                                estudiante=estudianteList.get(pos);
                                                btnVerReporte.setVisibility(View.VISIBLE);
                                            }else{
                                                btnVerReporte.setVisibility(View.GONE);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                    if (estudianteList.isEmpty()) {
                                        Snackbar.make(layoutRoot, "No hay Estudiantes.", Snackbar.LENGTH_LONG).show();
                                    }
                                }
                                break;
                        }
                    }else{
                        btnVerReporte.setVisibility(View.GONE);
                        switch(title){
                            case "Evaluacion":
                                layoutEval.setVisibility(View.GONE);
                                break;
                            case "Estudiante":
                                layoutEstud.setVisibility(View.GONE);
                                break;
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            if (materiaList.isEmpty()){
                Snackbar.make(layoutRoot, "No hay Materias.", Snackbar.LENGTH_LONG).show();
            }

        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

    public void onClick_SelectReporte(View view) {
        Intent i= new Intent(Actividad_SelectReporte.this,Actividad_VerReporte.class);
        i.putExtra("Reporte",title);
        switch (title){
            case "Evaluacion":
                i.putExtra("id",evaluacion.getId());
                break;
            case "Estudiante":
                i.putExtra("id",estudiante.getId());
                break;
        }
        startActivity(i);

    }



}
