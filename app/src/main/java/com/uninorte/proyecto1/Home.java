package com.uninorte.proyecto1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class Home extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth=FirebaseAuth.getInstance();

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

    public void onClick_salir(View view) {
        logoutUser();
    }

    public void logoutUser() {
        auth.signOut();
        if(auth.getCurrentUser() == null)
        {
            startActivity(new Intent(Home.this,Welcome.class));
            finish();
        }
    }
}
