package com.uninorte.proyecto1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Actividad_AddDescriptions extends AppCompatActivity {

    private RecyclerView list;
    private List<Nivel> nivelesList = new ArrayList<>();
    private CustomAdapterEleNiv customAdapterEleNiv;
    long initialCount;
    int modifyPos = -1;
    private String nivelId;
    private EditText description;
    private EditText nota;
    private TextView title;
    private String Eleid;
    private Rubrica rubrica;
    private Boolean mode;
    private DatabaseReference mDatabaseReference;

    CoordinatorLayout layoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_descriptions);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);
        title=(TextView)findViewById(R.id.toolbar_title);
        mode=getIntent().getBooleanExtra("selector",true);

        if(mode){
            title.setText("Añadir Descripcion");
        }else{
            title.setText("Añadir Nota");
        }

        //initialCount = Nivel.count(Nivel.class);
        Log.d("create", "onCreate initialcountCat: "+initialCount);
        list = (RecyclerView)findViewById(R.id.ReciclerView);
        Eleid= getIntent().getStringExtra("Ele_id");

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");

        mDatabaseReference= FirebaseDatabase.getInstance().getReference("noterubric").child("Nivel");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initialCount=dataSnapshot.getChildrenCount();
                if(initialCount>=0){
                    DatabaseReference referenceEle=FirebaseDatabase.getInstance().getReference("noterubric").child("Elemento").child(Eleid);
                    referenceEle.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            final Elemento elemento=dataSnapshot.getValue(Elemento.class);
                            DatabaseReference referenceCat=FirebaseDatabase.getInstance().getReference("noterubric").child("Categoria").child(elemento.getCategoria());
                            referenceCat.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Categoria categoria= dataSnapshot.getValue(Categoria.class);
                                    DatabaseReference referenceRub=FirebaseDatabase.getInstance().getReference("noterubric").child("Rubrica").child(categoria.getKey());
                                    nivelesList = rubrica.getNiveles();

                                    customAdapterEleNiv = new CustomAdapterEleNiv(Actividad_AddDescriptions.this, nivelesList, true,elemento.getKey());
                                    list.setAdapter(customAdapterEleNiv);
                                    list.setLayoutManager(new LinearLayoutManager(Actividad_AddDescriptions.this));

                                    if (nivelesList.isEmpty())
                                        Snackbar.make(layoutRoot, "No hay Niveles en la Rubrica.", Snackbar.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    //Elemento elemento = Elemento.findById(Elemento.class,Eleid);
                    //Categoria categoria = Categoria.findById(Categoria.class, elemento.getCategoria());
                    //rubrica = Rubrica.findById(Rubrica.class, categoria.getRubrica());




                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                mDatabaseReference=FirebaseDatabase.getInstance().getReference("noterubric").child("Elemento").child(Eleid);
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final Elemento elemento=dataSnapshot.getValue(Elemento.class);
                        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("noterubric").child("ElemenNivel");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                boolean selector;
                                List<ElemenNivel> elenivdescriptions= new ArrayList<>();
                                for(DataSnapshot snap:dataSnapshot.getChildren()){
                                    if(snap.getValue(ElemenNivel.class).getElemento().equals(elemento.getKey())){
                                        elenivdescriptions.add(snap.getValue(ElemenNivel.class));
                                    }
                                }
                                for (int i = 0; i < customAdapterEleNiv.getItemCount(); i++){
                                    nivelId=elenivdescriptions.get(i).getKey();
                                    description=(EditText) list.findViewHolderForLayoutPosition(i).itemView.findViewById(R.id.editTextNivel);
                                    nota=(EditText) list.findViewHolderForLayoutPosition(i).itemView.findViewById(R.id.editTextPeso);
                                    if(elenivdescriptions.isEmpty()){
                                        selector=false;
                                    }else{
                                        if(i<elenivdescriptions.size())
                                        {
                                            selector=true;
                                        }else{
                                            selector=false;
                                        }

                                    }
                                    if(mode) {
                                        EditorCreatorEleNiv(Eleid, nivelId, description.getText().toString(), selector);
                                    }

                                }
                                finish();


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                /*boolean selector;
                //Elemento elemento = Elemento.findById(Elemento.class,Eleid);
                List<ElemenNivel> elenivdescriptions = elemento.getDescriptions();
                Log.d("ElemenNivel", "selector: "+elenivdescriptions.size());*/


            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void EditorCreatorEleNiv(final String Elemento, final String Nivel, final String description, boolean credit){
        mDatabaseReference=FirebaseDatabase.getInstance().getReference("noterubric").child("ElemenNivel");
        if (!credit){
            Log.d("ElemenNivel", "saving");
           ElemenNivel elenivdescription = new ElemenNivel(Elemento,Nivel,description);
            String ElemenNivelid=mDatabaseReference.push().getKey();
            elenivdescription.setKey(ElemenNivelid);
            mDatabaseReference.child(ElemenNivelid).setValue(elenivdescription);
            //elenivdescription.save();
        }else{

           // List<ElemenNivel> elemenNiveles = ElemenNivel.find(ElemenNivel.class,"elemento = ? and nivel = ?",String.valueOf(Elemento),String.valueOf(Nivel));
            mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap:dataSnapshot.getChildren()){
                        ElemenNivel elemenNivel =snap.getValue(ElemenNivel.class);
                        if(elemenNivel.getElemento().equals(Elemento) && elemenNivel.getNivel().equals(Nivel)){
                            if(!elemenNivel.getDescription().equals(description)) {
                                Log.d("ElemenNivel", "updating");
                                //elemenNivel.setDescription(description);
                                ElemenNivel newelemennivel= new ElemenNivel(Elemento,Nivel,description);
                                newelemennivel.setKey(String.valueOf(elemenNivel.getKey()));
                                mDatabaseReference.child(elemenNivel.getKey()).setValue(newelemennivel);
                                break;
                                //elemenNivel.save();
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("modify", modifyPos);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        modifyPos = savedInstanceState.getInt("modify");
    }

    @Override
    protected void onResume() {
        super.onResume();

        final long newCount = Elemento.count(Elemento.class);

        if (newCount > initialCount) {

            Log.d("Main", "Adding new Elemento");


            Nivel nivel = Nivel.last(Nivel.class);

            //Log.d("Elemento","nombre Categoria: "+categoria.getCategoria().toString());
            nivelesList.add(nivel);
            customAdapterEleNiv.notifyItemInserted((int) newCount);

            initialCount = newCount;
        }

        if (modifyPos != -1) {
            nivelesList.set(modifyPos, rubrica.getNiveles().get(modifyPos));
            customAdapterEleNiv.notifyItemChanged(modifyPos);
        }

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
            Intent i = new Intent(Actividad_AddDescriptions.this,Home.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }


}
