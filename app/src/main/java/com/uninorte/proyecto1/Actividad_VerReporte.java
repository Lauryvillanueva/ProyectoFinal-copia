package com.uninorte.proyecto1;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Actividad_VerReporte extends AppCompatActivity {

    private TextView tbTitle;
    private RecyclerView list;

    private List<Reporte> reporteLists;
    private CustomAdapterRep customAdapterRep;

    private String title;
    private Long reporteId;

    private List<NotaEstudElemento> notaEstudElementoList;
    private Evaluacion evaluacion;
    private Materia materia;
    private List<Estudiante> estudianteList;

    private Rubrica rubrica;
    private List<Categoria> categoriaList;

    CoordinatorLayout layoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verreporte);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        tbTitle=(TextView) findViewById(R.id.toolbar_title);
        title=getIntent().getStringExtra("Reporte");
        tbTitle.setText("Reporte "+title);

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);

        list = (RecyclerView)findViewById(R.id.ReciclerView);
        reporteLists= new ArrayList<>();
        reporteId=getIntent().getLongExtra("id",0);
        Log.d("VerReporte", "Reporte ID: " + reporteId);

        if(NotaEstudElemento.count(NotaEstudElemento.class)>0) {
            switch (title) {
                case "Evaluacion":
                    evaluacion = Evaluacion.findById(Evaluacion.class, reporteId);
                    Double nota;
                    Elemento elemento;
                    Categoria categoria;

                    materia = Materia.findById(Materia.class, evaluacion.getMateria());
                    estudianteList = materia.getEstudiantes();

                    if (!estudianteList.isEmpty()) {
                        Estudiante estudiante;
                        List<NotaEstudElemento> notaEstudElementoList;
                        NotaEstudElemento notaEstudElemento;
                        for (Estudiante estudianteItem: estudianteList) {
                            estudiante = estudianteItem;
                            notaEstudElementoList=estudiante.getNotas(evaluacion.getId());
                            if(!notaEstudElementoList.isEmpty()) {
                                for (NotaEstudElemento notaEstudElementoItem: notaEstudElementoList) {
                                    notaEstudElemento=notaEstudElementoItem;
                                    elemento=Elemento.findById(Elemento.class,notaEstudElemento.getElemento());
                                    categoria=Categoria.findById(Categoria.class,elemento.getCategoria());
                                   if(notaEstudElemento.getEvaluacion()==evaluacion.getId()){
                                       nota=notaEstudElemento.getNota()*(elemento.getPeso()/100.0)*(categoria.getPeso()/100.0);
                                        if(reporteLists.isEmpty()){
                                            reporteLists.add(new Reporte(estudiante.getName(),nota,estudiante.getId()));
                                        }else{
                                            if(containsId(reporteLists,estudiante.getId())==-1){
                                                Log.d("ReporteView", "Nota: "+estudiante.getName());
                                                reporteLists.add(new Reporte(estudiante.getName(),nota,estudiante.getId()));
                                            }else{
                                                reporteLists.get(containsId(reporteLists,estudiante.getId())).addNota(nota);
                                            }
                                        }
                                   }

                                }
                            }else{
                                reporteLists.add(new Reporte(estudiante.getName(),0.0,estudiante.getId()));
                            }
                        }
                    }

                    if(!reporteLists.isEmpty()) {
                        customAdapterRep = new CustomAdapterRep(this, reporteLists);
                        list.setAdapter(customAdapterRep);
                        list.setLayoutManager(new LinearLayoutManager(this));
                    }else{
                        Snackbar.make(layoutRoot, "No hay Estudiantes.", Snackbar.LENGTH_LONG).show();
                    }

                    break;
                case "Estudiante":
                    break;
            }

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   finish();
                }
            });
        }

    }

    public int containsId(List<Reporte> reporteLists,Long id){
        for(Reporte item: reporteLists){
            if(item.getId()==id){
                return reporteLists.indexOf(item);
            }
        }
        return -1;
    }
}
