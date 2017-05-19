package com.uninorte.proyecto1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Welcome extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG=Welcome.class.getName();
    private ProgressDialog progressDialog;
    private Snackbar snackbar;
    private ConstraintLayout welcomeLayout;

    private DatabaseReference mDatabase;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeLayout=(ConstraintLayout) findViewById(R.id.activity_welcome);

        mAuth=FirebaseAuth.getInstance();

        mAuthListener= new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user=firebaseAuth.getCurrentUser();
                if(user!=null){
                    Log.d(TAG, "onAuthStateChanged: signed_in "+ user);
                }else{
                    Log.d(TAG, "onAuthStateChanged: signed_out");
                }

            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void onClick_Iniciar(View view) {
        showDialog(true);
    }

    public void onClick_Registrarse(View view) {
        showDialog(false);
    }

    private void showDialog(final boolean type)
    {
        LayoutInflater li = LayoutInflater.from(this);

        View prompt = li.inflate(R.layout.loginregister_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(prompt);
        final TextInputLayout usernameWrapper = (TextInputLayout) prompt.findViewById(R.id.input_username);
        final TextView forgotpswd=(TextView) prompt.findViewById(R.id.login_btn_forgot_password);
        final EditText username = (EditText) prompt.findViewById(R.id.login_username);
        final EditText email = (EditText) prompt.findViewById(R.id.login_email);
        final EditText password = (EditText) prompt.findViewById(R.id.login_password);
        final TextView title=(TextView) prompt.findViewById(R.id.toolbar_title);
        final MaterialSpinner spRole=(MaterialSpinner) prompt.findViewById(R.id.spinnerRole);
        final String[] role = new String[1];
        String[] valores = {"Estudiante","Profesor"};
        final ArrayAdapter<String> adapter =new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapter);
        spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=-1){
                    role[0]=adapter.getItem(i);
                }else{
                    role[0] =null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        if(type){
            email.setVisibility(View.GONE);
            spRole.setVisibility(View.GONE);
            usernameWrapper.setHint("Username o Email");
            title.setText("Ingresar");
        }else{
            forgotpswd.setVisibility(View.GONE);
            title.setText("Registro");
        }


        alertDialogBuilder.setCancelable(false)
                .setPositiveButton("Ok", null) //Set to null. We override the onclick
                .setNegativeButton("Cancelar", null);

        AlertDialog alertDialog=alertDialogBuilder.create();


        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (type){
                            if(Patterns.EMAIL_ADDRESS.matcher(username.getText().toString()).matches()){

                                performLogin(username.getText().toString(),password.getText().toString());
                                dialog.dismiss();
                            }else{
                                mDatabase=FirebaseDatabase.getInstance().getReference().child("users");
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        boolean valid=false;
                                        for (DataSnapshot snap: dataSnapshot.getChildren()) {
                                            User usuario= snap.getValue(User.class);
                                            if(usuario.getUsername().equals(username.getText().toString())){
                                                valid=true;
                                                performLogin(usuario.getEmail(),password.getText().toString());

                                                break;
                                            }
                                        }
                                        if(!valid){
                                            Toast.makeText(Welcome.this,"Username no valido",Toast.LENGTH_SHORT);
                                        }else{
                                            dialog.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });

                            }
                        }else{
                            if (!TextUtils.isEmpty(username.getText().toString()) && !TextUtils.isEmpty(email.getText().toString())
                                    && !TextUtils.isEmpty(password.getText().toString()) && !role[0].equals(null)){
                                progressDialog=new ProgressDialog(Welcome.this);
                                progressDialog.setIndeterminate(true);
                                progressDialog.setMessage("Creando usuario...");
                                progressDialog.show();

                                mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                                        .addOnCompleteListener(Welcome.this, new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if(!task.isSuccessful())
                                                {
                                                    snackbar = Snackbar.make(welcomeLayout,"Error: "+task.getException(), Snackbar.LENGTH_SHORT);
                                                    snackbar.show();
                                                    progressDialog.dismiss();

                                                }
                                                else{
                                                    mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                                    String userId = task.getResult().getUser().getUid();
                                                    User user= new  User(email.getText().toString(),username.getText().toString(),role[0]);
                                                    mDatabase.child(mAuth.getCurrentUser().getUid()).setValue(user);

                                                    Toast.makeText(Welcome.this,"Registro exitoso! ",Toast.LENGTH_SHORT).show();

                                                    progressDialog.dismiss();
                                                    dialog.dismiss();

                                                }
                                            }
                                        });

                            }else{
                                Toast.makeText(Welcome.this,"Hay Campos vacios",Toast.LENGTH_SHORT);
                            }
                        }

                    }
                });
            }
        });
        alertDialog.show();

    }

    private void performLogin(final String email, final String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(Welcome.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        if(!task.isSuccessful())
                        {
                            if(password.length() < 6)
                            {
                                Toast.makeText(Welcome.this,"La contraseña debe tener mas de 6 caracteres",Toast.LENGTH_SHORT).show();


                            }else  if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                                Toast.makeText(Welcome.this,"Email no valido",Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(Welcome.this,"Email/Username o contraseña incorrecta! Verifiquelo e intente de nuevo",Toast.LENGTH_SHORT).show();

                            }
                        }
                        else{
                            Intent i = new Intent(Welcome.this,Home.class);
                            startActivity(i);
                        }

                    }
                });
    }



}
