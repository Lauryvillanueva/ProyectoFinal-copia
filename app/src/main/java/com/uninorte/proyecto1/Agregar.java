package com.uninorte.proyecto1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Agregar extends AppCompatActivity {

    private EditText nombre,Epeso;
    private TextView title,estado,pesocat,tvSelectRub;
    private MaterialSpinner selectRub,stateEstud;
    boolean editing, viewing;
    private String name, titulo;

    private Button eNivel,eCateg,eDesc,evaluar;

    private Materia materiaestud;
    private Rubrica rubricaSel,RubricaSelEva;
    private Categoria categoriaEle;

    private SpinnerAdapterRub spinnerAdapterRub;

    CoordinatorLayout layoutRoot;
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabasetitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        layoutRoot=(CoordinatorLayout) findViewById(R.id.root);
        eDesc=(Button) findViewById(R.id.addDescriptions);

        pesocat=(TextView) findViewById(R.id.textViewPesoCat);
        tvSelectRub = (TextView) findViewById(R.id.textViewselectRub);
        Epeso=(EditText) findViewById(R.id.editTextPeso);
        //Epeso.setHint("Peso "+getIntent().getStringExtra("title"));
        Epeso.setFilters(new InputFilter[]{new InputFilterMinMax("1", "100")});

        estado=(TextView) findViewById(R.id.textViewState);
        stateEstud = (MaterialSpinner) findViewById(R.id.StateEstud);
        selectRub=(MaterialSpinner) findViewById(R.id.selectRubrica);

        eNivel=(Button) findViewById(R.id.editNiv);
        eCateg=(Button) findViewById(R.id.editCat);
        evaluar=(Button) findViewById(R.id.evaluar);

        String[] valores = {"Activo","Inactivo"};
        ArrayAdapter<String> adapter =new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateEstud.setAdapter(adapter);


        titulo=getIntent().getStringExtra("title");
            spinnerAdapterRub = new SpinnerAdapterRub(this, android.R.layout.simple_spinner_item, Rubrica.listAll(Rubrica.class));
            spinnerAdapterRub.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            selectRub.setAdapter(spinnerAdapterRub);
        //if(Rubrica.count(Rubrica.class)>0) {
        mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Rubrica");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int cont=Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                if(cont>0) {
                    selectRub.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (i!=-1){
                                RubricaSelEva = spinnerAdapterRub.getItem(i);
                            }else{
                                RubricaSelEva=null;
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }else{
                    if(getIntent().getStringExtra("title").equals("Evaluacion"))
                        Snackbar.make(layoutRoot, "No hay Rubricas.", Snackbar.LENGTH_LONG).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        title=(TextView) findViewById(R.id.title);

        mDatabasetitle= FirebaseDatabase.getInstance().getReference("noterubric").child(titulo);
        title.setText("Agregar "+getIntent().getStringExtra("title"));
        nombre=(EditText) findViewById(R.id.editTextName);
        nombre.setHint("Nombre "+getIntent().getStringExtra("title"));
        nombre.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);

        editing=getIntent().getBooleanExtra("isEditing", false);
        viewing=getIntent().getBooleanExtra("isViewing", false);

        if (title.getText().toString().equals("Agregar Materia")){          
            MainInputData("Mat_name");

        }else{
            if (title.getText().toString().equals("Agregar Estudiante")) {
                stateEstud.setVisibility(View.VISIBLE);
                estado.setVisibility(View.VISIBLE);
               // materiaestud = Materia.find(Materia.class, "name = ?", getIntent().getStringExtra("Mat_name")).get(0);
                mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Materia").child(getIntent().getStringExtra("Mat_name"));
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        materiaestud= dataSnapshot.getValue(Materia.class);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                MainInputData("Estud_name");
            }else{
                if (title.getText().toString().equals("Agregar Rubrica")) {
                    if(editing || viewing) {
                        eNivel.setVisibility(View.VISIBLE);
                        eCateg.setVisibility(View.VISIBLE);
                    }
                    MainInputData("Rub_name");
                }else{
                    if (title.getText().toString().equals("Agregar Nivel") ) {
                        //rubricaSel = Rubrica.find(Rubrica.class, "name = ?", getIntent().getStringExtra("Rub_name")).get(0);
                         mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Rubrica");
                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot snap: dataSnapshot.getChildren()) {
                                    Rubrica rubrica= snap.getValue(Rubrica.class);
                                    if(rubrica.getName().equals(getIntent().getStringExtra("Rub_name"))){
                                        rubricaSel=rubrica;
                                        break;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        MainInputData("NivelCat_name");
                    }else{
                        if (title.getText().toString().equals("Agregar Categoria")){
                            pesocat.setVisibility(View.VISIBLE);
                            Epeso.setVisibility(View.VISIBLE);
                           // rubricaSel = Rubrica.find(Rubrica.class, "name = ?", getIntent().getStringExtra("Rub_name")).get(0);
                            mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Rubrica");
                            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                                        Rubrica rubrica= snap.getValue(Rubrica.class);
                                        if(rubrica.getName().equals(getIntent().getStringExtra("Rub_name"))){
                                            rubricaSel=rubrica;
                                            break;
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                            MainInputData("NivelCat_name");
                        }else{
                            if (title.getText().toString().equals("Agregar Elemento")){
                                if(editing || viewing)
                                    eDesc.setVisibility(View.VISIBLE);
                                pesocat.setVisibility(View.VISIBLE);
                                Epeso.setVisibility(View.VISIBLE);
                                //categoriaEle=Categoria.find(Categoria.class,"name =  ?", getIntent().getStringExtra("Cat_name")).get(0);
                                mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Categoria");
                                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snap: dataSnapshot.getChildren()) {
                                            Categoria categoria = snap.getValue(Categoria.class);
                                            if(categoria.getName().equals(getIntent().getStringExtra("Cat_name"))){
                                                categoriaEle=categoria;
                                                break;
                                            }
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                MainInputData("Ele_name");

                            }else{
                                if (title.getText().toString().equals("Agregar Evaluacion")){
                                    selectRub.setVisibility(View.VISIBLE);
                                    tvSelectRub.setVisibility(View.VISIBLE);
                                    //materiaestud = Materia.findById(Materia.class,getIntent().getLongExtra("Mat_id",0));
                                    mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Materia").child(String.valueOf(getIntent().getLongExtra("Mat_id",0)));
                                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            materiaestud=dataSnapshot.getValue(Materia.class);

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                    MainInputData("Eva_name");
                                }
                            }
                        }
                    }
                }

            }
        }



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(Actividad_Materias.this, R.string.up, Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    public void MainInputData(String extra){

        if (editing) {
            name = getIntent().getStringExtra(extra);
            nombre.setText(name);
            if(getIntent().getStringExtra("title").equals("Estudiante")){
                int state = getIntent().getIntExtra("Estud_state",0);
                stateEstud.setSelection(state);
            }
            if(getIntent().getStringExtra("title").equals("Categoria") || getIntent().getStringExtra("title").equals("Elemento")){
                int pesocatele = getIntent().getIntExtra("CatEle_peso",0);
                Epeso.setText(String.valueOf(pesocatele));
            }
            if(getIntent().getStringExtra("title").equals("Evaluacion")){

                // Rubrica state = Rubrica.findById(Rubrica.class, getIntent().getLongExtra("Rub_id",0));
                mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Materia").child(String.valueOf(getIntent().getLongExtra("Rub_id",0)));
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Rubrica state;
                        state=dataSnapshot.getValue(Rubrica.class);

                        evaluar.setVisibility(View.VISIBLE);
                        DatabaseReference mDatabase2=FirebaseDatabase.getInstance().getReference("noterubric").child("Rubrica");
                        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int cont=0;
                                for(DataSnapshot snap:dataSnapshot.getChildren()){
                                    Rubrica item=snap.getValue(Rubrica.class);
                                    cont++;
                                    if (item.getKey().equals(state.getKey())){
                                        selectRub.setSelection(cont);
                                        break;
                                    }
                                }
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

            }
        }
        if (viewing){
            name = getIntent().getStringExtra(extra);
            nombre.setText(name);
            nombre.setFocusable(false);
            nombre.setFocusableInTouchMode(false);
            if(getIntent().getStringExtra("title").equals("Estudiante")){
                int state = getIntent().getIntExtra("Estud_state",0);
                stateEstud.setSelection(state);
                stateEstud.setEnabled(false);
                stateEstud.setClickable(false);
            }
            if(getIntent().getStringExtra("title").equals("Categoria") || getIntent().getStringExtra("title").equals("Elemento")){
                int pesocatele = getIntent().getIntExtra("CatEle_peso",0);
                Epeso.setText(String.valueOf(pesocatele));
                Epeso.setFocusable(false);
                Epeso.setFocusableInTouchMode(false);
            }
            if(getIntent().getStringExtra("title").equals("Evaluacion")){
                int state = (int) getIntent().getLongExtra("Rub_id",0);
                selectRub.setSelection(state);
                selectRub.setEnabled(false);
                selectRub.setClickable(false);
            }
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
            Intent i = new Intent(Agregar.this,Home.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

    private void action(String resid) {
        Toast.makeText(this, resid, Toast.LENGTH_SHORT).show();
    }



    public void onClick_Guardar(View view) {
        if(!viewing) {
            if (TextUtils.isEmpty(nombre.getText().toString())) {
                nombre.setError("No Puede Estar Vacio");
            } else {
                final String newName = nombre.getText().toString();
                if (title.getText().toString().equals("Agregar Materia")){
                    EditorCreatorMat(newName);
                    finish();
                }else{
                    if (title.getText().toString().equals("Agregar Estudiante")) {
                        int newState = stateEstud.getSelectedItemPosition();
                        Log.d("SelectEstate", "onClick_GuardarEstate: "+newState);
                        if(newState!=0) {
                            EditorCreatorEstud(newName, newState);
                            finish();
                        }else{
                            stateEstud.setError("Seleccionar Estado");
                        }
                    }else{
                        if (title.getText().toString().equals("Agregar Rubrica")) {
                            EditorCreatorRub(newName);
                            finish();
                        }else{
                            if (title.getText().toString().equals("Agregar Nivel")) {
                                EditorCreatorNivel(newName);
                                finish();
                            }else{
                                if (title.getText().toString().equals("Agregar Categoria")) {
                                    if(TextUtils.isEmpty(Epeso.getText().toString())) {
                                        Epeso.setError("No Puede Estar Vacio");
                                    }else {
                                        int newPeso = Integer.parseInt(Epeso.getText().toString());
                                        EditorCreatorCat(newName, newPeso);
                                        finish();
                                    }
                                }else{
                                    if (title.getText().toString().equals("Agregar Elemento")) {
                                        if(TextUtils.isEmpty(Epeso.getText().toString())) {
                                            Epeso.setError("No Puede Estar Vacio");
                                        }else {
                                            int newPeso = Integer.parseInt(Epeso.getText().toString());
                                            EditorCreatorEle(newName, newPeso);
                                            finish();
                                        }

                                    }else{
                                        //if(Rubrica.count(Rubrica.class)==0 || RubricaSelEva==null){
                                        mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Rubrica");
                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                int cont=Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                                                if(cont==0 || RubricaSelEva==null){
                                                    selectRub.setError("Crear o Seleccionar Rubrica");
                                                }else{
                                                    String newRubricaPos = RubricaSelEva.getKey();
                                                    EditorCreatorEva(newName, newRubricaPos);
                                                    finish();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }

                                }

                            }
                        }
                    }
                }

            }
        }


    }



    public void EditorCreatorMat(final String Name){
        if (!editing) {
            Log.d("Materia", "saving");
            Materia materia = new Materia("" + Name);
            //materia.save();
            String materiaid = mDatabasetitle.push().getKey();
            materia.setKey(materiaid);
            mDatabasetitle.child(materiaid).setValue(materia);
        } else {
            Log.d("Materia", "updating");

            //List<Materia> materias = Materia.find(Materia.class, "name = ?", name);
               mDatabasetitle.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                        Materia materia= snap.getValue(Materia.class);
                        if(materia.getName().equals(name)){
                            Materia newmateria= new Materia(Name);
                            newmateria.setKey(String.valueOf(materia.getKey()));
                            mDatabasetitle.child(materia.getKey()).setValue(newmateria);
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            /*if (materias.size() > 0) {

                Materia materia = materias.get(0);
                Log.d("got materia", "materia: " + materia.getName());
                materia.setName("" + Name);
                materia.save();
            }*/
        }
    }

    public void EditorCreatorRub(final String Name){
        if (!editing) {
            Log.d("Rubrica", "saving");
            Rubrica rubrica = new Rubrica("" + Name);
            //rubrica.save();
            String rubricaid = mDatabasetitle.push().getKey();
            rubrica.setKey(rubricaid);
            mDatabasetitle.child(rubricaid).setValue(rubrica);
        } else {
            Log.d("Rubrica", "updating");

           // List<Rubrica> rubricas = Rubrica.find(Rubrica.class, "name = ?", name);
            mDatabasetitle.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                        Rubrica rubrica= snap.getValue(Rubrica.class);
                        if(rubrica.getName().equals(name)){
                            Rubrica newrubrica= new Rubrica(Name);
                            newrubrica.setKey(String.valueOf(rubrica.getKey()));
                            mDatabasetitle.child(rubrica.getKey()).setValue(newrubrica);
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            /*if (rubricas.size() > 0) {

                Rubrica rubrica = rubricas.get(0);
                Log.d("got rubrica", "Rubrica: " + rubrica.getName());
                rubrica.setName("" + Name);
                rubrica.save();
            }*/
        }
    }

    public void EditorCreatorNivel(final String Name){
        if (!editing) {
            Log.d("Nivel", "saving");
            Nivel nivel = new Nivel("" + Name,rubricaSel);
            //nivel.save();
            String nivelid = mDatabasetitle.push().getKey();
            nivel.setKey(nivelid);
            mDatabasetitle.child(nivelid).setValue(nivel);

        } else {
            Log.d("Nivel", "updating");

            //List<Nivel> niveles = Nivel.find(Nivel.class, "name = ? and rubrica =?", name,rubricaSel.getId().toString());
            mDatabasetitle.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                        Nivel nivel= snap.getValue(Nivel.class);
                        if(nivel.getName().equals(name) && nivel.getRubrica().equals(rubricaSel.getKey())){
                            Nivel newnivel= new Nivel(Name,rubricaSel);
                            newnivel.setKey(String.valueOf(nivel.getKey()));
                            mDatabasetitle.child(nivel.getKey()).setValue(newnivel);
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
           /* if (niveles.size() > 0) {

                Nivel nivel = niveles.get(0);
                Log.d("got estudiante", "estudiante: " + nivel.getName());
                nivel.setName("" + Name);
                nivel.save();
            }*/
        }
    }

    public void EditorCreatorCat(final String Name, final int peso){
        if (!editing) {
            Log.d("Categoria", "saving");
            Categoria categoria = new Categoria("" + Name,peso,rubricaSel);
            //categoria.save();
            String categoriaid = mDatabasetitle.push().getKey();
            categoria.setKey(categoriaid);
            mDatabasetitle.child(categoriaid).setValue(categoria);
        } else {
            Log.d("Categoria", "updating");

            //List<Categoria> categorias = Categoria.find(Categoria.class, "name = ? and rubrica =?", name,rubricaSel.getId().toString());

            mDatabasetitle.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                        Categoria categoria= snap.getValue(Categoria.class);
                        if(categoria.getName().equals(name) && categoria.getRubrica().equals(rubricaSel.getKey())){
                            Categoria newcat= new Categoria(Name,peso,rubricaSel);
                            newcat.setKey(String.valueOf(categoria.getKey()));
                            mDatabasetitle.child(categoria.getKey()).setValue(newcat);
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            /* if (categorias.size() > 0) {

                Categoria categoria = categorias.get(0);
                Log.d("got categoria", "categoria: " + categoria.getName());
                categoria.setName("" + Name);
                categoria.setPeso(peso);
                categoria.save();
            }*/
        }
    }

    public void EditorCreatorEle(final String Name, final int peso){
        if (!editing) {
            Log.d("Elemento", "saving");
            Elemento elemento = new Elemento("" + Name,peso,categoriaEle);
            //elemento.save();
            String elementoid = mDatabasetitle.push().getKey();
            elemento.setKey(elementoid);
            mDatabasetitle.child(elementoid).setValue(elemento);
        } else {
            Log.d("Elemento", "updating");

            //List<Elemento> elementos = Elemento.find(Elemento.class, "name = ? and categoria =?", name,categoriaEle.getId().toString());
            mDatabasetitle.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                        Elemento elemento= snap.getValue(Elemento.class);
                        if(elemento.getName().equals(name) && elemento.getCategoria().equals(categoriaEle.getKey())){
                            Elemento newelem= new Elemento(Name,peso,categoriaEle);
                            newelem.setKey(String.valueOf(elemento.getKey()));
                            mDatabasetitle.child(elemento.getKey()).setValue(newelem);
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            /*if (elementos.size() > 0) {

                Elemento elemento = elementos.get(0);
                Log.d("got elemento", "estudiante: " + elemento.getName());
                elemento.setName("" + Name);
                elemento.setPeso(peso);
                elemento.save();
            }*/
        }
    }

    public void EditorCreatorEva(final String Name, final String Rubricaid){
        if (!editing) {
            Log.d("Evaluacion", "saving");
            Evaluacion evaluacion = new Evaluacion(Name,materiaestud, Rubricaid);
           // evaluacion.save();
            String evaluacionid = mDatabasetitle.push().getKey();
            evaluacion.setKey(evaluacionid);
            mDatabasetitle.child(evaluacionid).setValue(evaluacion);
        } else {
            Log.d("Evaluacion", "updating");

           // List<Evaluacion> evaluaciones = Evaluacion.find(Evaluacion.class, "name = ? and materia =?", name,materiaestud.getId().toString());

            mDatabasetitle.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                        Evaluacion evaluacion= snap.getValue(Evaluacion.class);
                        if(evaluacion.getName().equals(name) && evaluacion.getMateria().equals(materiaestud.getKey())){
                            Evaluacion neweval= new Evaluacion(Name,materiaestud,Rubricaid);
                            neweval.setKey(String.valueOf(evaluacion.getKey()));
                            mDatabasetitle.child(evaluacion.getKey()).setValue(neweval);
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

          /* if (evaluaciones.size() > 0) {

                Evaluacion evaluacion = evaluaciones.get(0);
                Log.d("got evaluacion", "evaluacion: " + evaluacion.getName());
                evaluacion.setName("" + Name);
                evaluacion.setRubrica(Rubricaid);
                evaluacion.save();
            }*/
        }
    }

    public void EditorCreatorEstud(final String Name, final int State){
        if (!editing) {
            Log.d("Estudiante", "saving");
            Estudiante estudiante = new Estudiante(Name,State,materiaestud);
            //estudiante.save();
            String estudid = mDatabasetitle.push().getKey();
            estudiante.setKey(estudid);
            mDatabasetitle.child(estudid).setValue(estudiante);
        } else {
            Log.d("Estudiante", "updating");

          //  List<Estudiante> estudiantes = Estudiante.find(Estudiante.class, "name = ? and materia =?", name,materiaestud.getId().toString());
            mDatabasetitle.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                        Estudiante estudiante= snap.getValue(Estudiante.class);
                        if(estudiante.getName().equals(name) && estudiante.getMateria().equals(materiaestud.getKey())){
                            Estudiante newest= new Estudiante(Name,State,materiaestud);
                            newest.setKey(String.valueOf(estudiante.getKey()));
                            mDatabasetitle.child(estudiante.getKey()).setValue(newest);
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            /*if (estudiantes.size() > 0) {

                Estudiante estudiante = estudiantes.get(0);
                Log.d("got estudiante", "estudiante: " + estudiante.getName());
                estudiante.setName("" + Name);
                estudiante.setState(State);
                estudiante.save();
            }*/
        }
    }

    public void onClick_EditNiveles(View view) {
            if(!editing && !viewing){
                Snackbar.make(layoutRoot, "Guardar Rubrica Primero.", Snackbar.LENGTH_LONG).show();
            }else{
              //  List<Rubrica> rubricas = Rubrica.find(Rubrica.class, "name = ?", name);
                mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Rubrica");
                mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snap: dataSnapshot.getChildren()) {
                            Rubrica rubrica= snap.getValue(Rubrica.class);
                            if(rubrica.getName().equals(name)){
                                Intent i = new Intent(Agregar.this,Actividad_Niveles.class);
                                i.putExtra("Rub_name",rubrica.getName());
                                startActivity(i);
                                break;
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
             /*   if (rubricas.size() > 0) {
                    Rubrica rubrica = rubricas.get(0);

                    Intent i = new Intent(Agregar.this,Actividad_Niveles.class);
                    i.putExtra("Rub_name",rubrica.getName());
                    startActivity(i);
                }*/
            }
    }

    public void onClick_EditCategorias(View view) {
        if(!editing && !viewing){
            Snackbar.make(layoutRoot, "Guardar Rubrica Primero.", Snackbar.LENGTH_LONG).show();
        }else{
            //List<Rubrica> rubricas = Rubrica.find(Rubrica.class, "name = ?", name);
            mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Rubrica");
           mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                        Rubrica rubrica= snap.getValue(Rubrica.class);
                        if(rubrica.getName().equals(name)){
                        Intent i = new Intent(Agregar.this,Actividad_Categorias.class);
                i.putExtra("Rub_name",rubrica.getName());
                i.putExtra("OpcionCreEva",true);
                startActivity(i);
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        /*   if (rubricas.size() > 0) {
                Rubrica rubrica = rubricas.get(0);
                Intent i = new Intent(Agregar.this,Actividad_Categorias.class);
                i.putExtra("Rub_name",rubrica.getName());
                i.putExtra("OpcionCreEva",true);
                startActivity(i);
            }*/
        }
    }

    public void onClick_addDescriptions(View view) {
        if(!editing && !viewing){
            Snackbar.make(layoutRoot, "Guardar Elemento Primero.", Snackbar.LENGTH_LONG).show();
        }else {
           // List<Elemento> elementos = Elemento.find(Elemento.class, "name = ? and categoria =?", name,categoriaEle.getId().toString());
            mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Elemento");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap: dataSnapshot.getChildren()) {
                        Elemento elemento= snap.getValue(Elemento.class);
                        if(elemento.getName().equals(name)&& elemento.getCategoria().equals(categoriaEle.getKey().toString())){
                            Intent i = new Intent(Agregar.this, Actividad_AddDescriptions.class);
                            i.putExtra("Ele_id", elemento.getKey());
                            startActivity(i);
                            break;
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            /*if (elementos.size() > 0) {
                Elemento elemento = elementos.get(0);

                Intent i = new Intent(Agregar.this, Actividad_AddDescriptions.class);
                i.putExtra("Ele_id", elemento.getId());
                startActivity(i);
            }*/
        }
    }


    public void onClick_Evaluar(View view) {
        if(!editing && !viewing){
            Snackbar.make(layoutRoot, "Guardar Evaluacion Primero.", Snackbar.LENGTH_LONG).show();
        }else {
           //Evaluacion evalua= Evaluacion.find(Evaluacion.class,"name = ?",getIntent().getStringExtra("Eva_name")).get(0);
            mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Evaluacion");
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap:dataSnapshot.getChildren()){
                        if(snap.getValue(Evaluacion.class).getName().equals(getIntent().getStringExtra("Eva_name"))){
                            Evaluacion evalua=snap.getValue(Evaluacion.class);
                            if(RubricaSelEva!=null) {
                                if (evalua.getRubrica().equals(RubricaSelEva.getId())) {
                                    Intent i = new Intent(Agregar.this, Actividad_Evaluar.class);
                                    i.putExtra("Rub_id", RubricaSelEva.getId());
                                    i.putExtra("Mat_id", materiaestud.getId());
                                    i.putExtra("Eva_id", Evaluacion.find(Evaluacion.class, "name = ?", getIntent().getStringExtra("Eva_name")).get(0).getId());
                                    startActivity(i);
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Agregar.this);
                                    builder.setTitle("Guardar")
                                            .setMessage("¿Desea guardar Rubrica antes de Evaluar?")
                                            .setPositiveButton("Si", dialogSave)
                                            .setNegativeButton("No", dialogSave).show();

                                }
                            }else{
                                Toast.makeText(Agregar.this,"Crear o Seleccionar Rubrica",Toast.LENGTH_SHORT).show();
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

    public DialogInterface.OnClickListener dialogSave = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int choice) {
            switch(choice){
                case DialogInterface.BUTTON_POSITIVE:
                    final String newName = nombre.getText().toString();
                    //if(Rubrica.count(Rubrica.class)!=0 || RubricaSelEva!=null){
                    mDatabase=FirebaseDatabase.getInstance().getReference("noterubric").child("Rubrica");
                    mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            int cont=Integer.parseInt(String.valueOf(dataSnapshot.getChildrenCount()));
                            if(cont!=0 || RubricaSelEva!=null){
                                String newRubricaPos = RubricaSelEva.getKey();
                                EditorCreatorEva(newName, newRubricaPos);
                                Intent i = new Intent(Agregar.this, Actividad_Evaluar.class);
                                i.putExtra("Rub_id", RubricaSelEva.getId());
                                i.putExtra("Mat_id", materiaestud.getId());
                                i.putExtra("Eva_id", Evaluacion.find(Evaluacion.class, "name = ?", getIntent().getStringExtra("Eva_name")).get(0).getId());
                                startActivity(i);
                            }else{
                                Toast.makeText(Agregar.this, "No se Guardo el Registro", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    Intent i = new Intent(Agregar.this, Actividad_Evaluar.class);
                    i.putExtra("Rub_id", RubricaSelEva.getId());
                    i.putExtra("Mat_id", materiaestud.getId());
                    i.putExtra("Eva_id", Evaluacion.find(Evaluacion.class, "name = ?", getIntent().getStringExtra("Eva_name")).get(0).getId());
                    startActivity(i);
                    break;
            }

        }
    };
}
