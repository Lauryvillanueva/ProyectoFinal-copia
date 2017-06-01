
package com.uninorte.proyecto1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectreporte);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

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

        mDatabase= FirebaseDatabase.getInstance().getReference("noterubric").child("Materia");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                int cont=Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                if(cont>0) {
                    materiaList=Materia.listAll(Materia.class);

                    // initialize the array
                    final List<String> mate = new ArrayList<String>();
                     for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                        String namemat = areaSnapshot.child("name").getValue(String.class);
                        mate.add(namemat);
                    }
                    ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(Actividad_SelectReporte.this, android.R.layout.simple_spinner_item, mate);
                    areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spMateria.setAdapter(areasAdapter);


                    spMateria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
                            if(i!=-1) {
                                switch (title) {
                                    case "Evaluacion":

                                        // if (Evaluacion.count(Evaluacion.class) > 0)
                                        mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Evaluacion");
                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(DataSnapshot dataSnapshot) {
                                                  int cont=Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                                                  if(cont>0) {
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
                                                  }
                                               @Override
                                               public void onCancelled(DatabaseError databaseError) {
                                             }
                                              });

                                        break;
                                    case "Estudiante":
                                        mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Estudiante");
                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                                 @Override
                                                 public void onDataChange(DataSnapshot dataSnapshot) {
                                                     int cont=Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                                                     if(cont>0) {
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
                                                 }

                                                 @Override
                                                 public void onCancelled(DatabaseError databaseError) {

                                                  }
                                                  });


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
                //
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                                                     }

                                                     @Override
                                                     public void onCancelled(DatabaseError databaseError) {

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
            Intent i = new Intent(Actividad_SelectReporte.this,Home.class);
            startActivityForResult(i,1);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
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
