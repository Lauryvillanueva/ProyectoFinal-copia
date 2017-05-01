package com.uninorte.proyecto1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verreporte);

        tbTitle=(TextView) findViewById(R.id.toolbar_title);
        title=getIntent().getStringExtra("Reporte");
        tbTitle.setText("Reporte "+title);

        list = (RecyclerView)findViewById(R.id.ReciclerView);
        reporteLists= new ArrayList<>();
        reporteId=getIntent().getLongExtra("id",0);
        Log.d("VerReporte", "Reporte ID: " + reporteId);

        if(NotaEstudElemento.count(NotaEstudElemento.class)>0) {
            switch (title) {
                case "Evaluacion":
                    evaluacion = Evaluacion.findById(Evaluacion.class, reporteId);
                    /*rubrica = Rubrica.findById(Rubrica.class, evaluacion.getRubrica());
                    categoriaList = rubrica.getCategorias();*/
                    Double nota;
                    Elemento elemento;
                    Categoria categoria;

                    materia = Materia.findById(Materia.class, evaluacion.getMateria());
                    estudianteList = materia.getEstudiantes();

                    if (!estudianteList.isEmpty()) {
                        Estudiante estudiante;
                        List<NotaEstudElemento> notaEstudElementoList;
                        NotaEstudElemento notaEstudElemento;
                        for (int i = 0; i < estudianteList.size(); i++) {
                            estudiante = estudianteList.get(i);
                            notaEstudElementoList=estudiante.getNotas(evaluacion.getId());
                            if(!notaEstudElementoList.isEmpty()) {
                                for (int j = 0; j < notaEstudElementoList.size(); j++) {
                                    notaEstudElemento=notaEstudElementoList.get(j);
                                    elemento=Elemento.findById(Elemento.class,notaEstudElemento.getElemento());
                                    categoria=Categoria.findById(Categoria.class,elemento.getCategoria());
                                   if(notaEstudElemento.getEvaluacion()==reporteId){
                                       nota=notaEstudElemento.getNota()*(elemento.getPeso()/100)*(categoria.getPeso()/100);
                                        if(reporteLists.isEmpty()){
                                            reporteLists.add(new Reporte(estudiante.getName(),nota,estudiante.getId()));
                                        }else{
                                            if(containsId(reporteLists,estudiante.getId())==-1){
                                                reporteLists.add(new Reporte(estudiante.getName(),nota,estudiante.getId()));
                                            }else{
                                                reporteLists.get(containsId(reporteLists,estudiante.getId())).addNota(nota);
                                            }
                                        }
                                   }

                                }
                            }
                        }
                        customAdapterRep=new CustomAdapterRep(this,reporteLists);
                        list.setAdapter(customAdapterRep);
                        list.setLayoutManager(new LinearLayoutManager(this));
                    }

                    break;
                case "Estudiante":
                    break;
            }
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
