package com.uninorte.proyecto1;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Welcome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void onClick_Iniciar(View view) {
        showLoginDialog();
    }

    public void onClick_Registrarse(View view) {

    }

    private void showLoginDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);

        View prompt = li.inflate(R.layout.login_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(prompt);
        final EditText user = (EditText) prompt.findViewById(R.id.login_name);
        final EditText pass = (EditText) prompt.findViewById(R.id.login_password);

        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                     //**FIREBASES PARA LOGIN -----------------------------

                        Intent i = new Intent(Welcome.this,Home.class);
                        startActivityForResult(i,1);
                        //----------------------------------------------
                    }
                });

        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id)
            {
                dialog.cancel();

            }
        });

        alertDialogBuilder.show();

    }



}
