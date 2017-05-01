
package com.uninorte.proyecto1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Actividad_SelectReporte extends AppCompatActivity {

    private LinearLayout layoutEval,layoutEstud;
    private TextView tbTitle;
    private Spinner spMateria,spEstud,spEvalu;

    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectreporte);

        title=getIntent().getStringExtra("Reporte");
        tbTitle=(TextView) findViewById(R.id.toolbar_title);
        tbTitle.setText("Reporte "+title);

        layoutEval=(LinearLayout) findViewById(R.id.layoutEvaluacion);
        layoutEstud = (LinearLayout) findViewById(R.id.layoutEstudiante);

        if (title.equals("Evaluacion")){
            layoutEval.setVisibility(View.VISIBLE);
        }else{
            layoutEstud.setVisibility(View.VISIBLE);
        }



    }
}
