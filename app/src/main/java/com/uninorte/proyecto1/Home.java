package com.uninorte.proyecto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void onClick_materias(View view) {
        Intent i = new Intent(Home.this,Actividad_Materias.class);
        startActivityForResult(i,1);
    }

    public void onClick_rubricas(View view) {
        Intent i = new Intent(Home.this,Actividad_Rubricas.class);
        startActivityForResult(i,1);
    }


    public void onClick_reportes(View view) {
        Intent i = new Intent(Home.this,Actividad_Reportes.class);
        startActivityForResult(i,1);

    }
}
