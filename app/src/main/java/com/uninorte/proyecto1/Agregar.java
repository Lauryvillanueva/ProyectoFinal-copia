package com.uninorte.proyecto1;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;

public class Agregar extends AppCompatActivity {

    private EditText nombre,Epeso;
    private TextView title,estado,pesocat,tvSelectRub;
    private MaterialSpinner stateEstud,selectRub;
    boolean editing, viewing;
    private String name;
    private int estadoEstud;
    private Button eNivel,eCateg,eDesc,evaluar;

    private Materia materiaestud;
    private Rubrica rubricaSel,RubricaSelEva;
    private Categoria categoriaEle;

    private SpinnerAdapterRub spinnerAdapterRub;

    CoordinatorLayout layoutRoot;


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
        stateEstud.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valores));

        if(Rubrica.count(Rubrica.class)>0) {
            spinnerAdapterRub = new SpinnerAdapterRub(this, android.R.layout.simple_spinner_item, Rubrica.listAll(Rubrica.class));
            selectRub.setAdapter(spinnerAdapterRub);

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
            String[] valor = {"Empty"};
            selectRub.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, valor));
        }

        title=(TextView) findViewById(R.id.title);
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
                materiaestud = Materia.find(Materia.class, "name = ?", getIntent().getStringExtra("Mat_name")).get(0);
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
                        rubricaSel = Rubrica.find(Rubrica.class, "name = ?", getIntent().getStringExtra("Rub_name")).get(0);
                        MainInputData("NivelCat_name");
                    }else{
                        if (title.getText().toString().equals("Agregar Categoria")){
                            pesocat.setVisibility(View.VISIBLE);
                            Epeso.setVisibility(View.VISIBLE);
                            rubricaSel = Rubrica.find(Rubrica.class, "name = ?", getIntent().getStringExtra("Rub_name")).get(0);
                            MainInputData("NivelCat_name");
                        }else{
                            if (title.getText().toString().equals("Agregar Elemento")){
                                if(editing || viewing)
                                    eDesc.setVisibility(View.VISIBLE);
                                pesocat.setVisibility(View.VISIBLE);
                                Epeso.setVisibility(View.VISIBLE);
                                categoriaEle=Categoria.find(Categoria.class,"name =  ?", getIntent().getStringExtra("Cat_name")).get(0);
                                MainInputData("Ele_name");

                            }else{
                                if (title.getText().toString().equals("Agregar Evaluacion")){
                                    selectRub.setVisibility(View.VISIBLE);
                                    tvSelectRub.setVisibility(View.VISIBLE);
                                    materiaestud = Materia.findById(Materia.class,getIntent().getLongExtra("Mat_id",0));
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
                Rubrica state = Rubrica.findById(Rubrica.class, getIntent().getLongExtra("Rub_id",0));
                int cont=0;
                evaluar.setVisibility(View.VISIBLE);
                for(Rubrica item:Rubrica.listAll(Rubrica.class)){
                    cont++;
                    if (item.getId().equals(state.getId())){
                        selectRub.setSelection(cont);
                        break;
                    }

                }
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
                String newName = nombre.getText().toString();
                if (title.getText().toString().equals("Agregar Materia")){
                    EditorCreatorMat(newName);
                    finish();
                }else{
                    if (title.getText().toString().equals("Agregar Estudiante")) {
                        int newState = stateEstud.getSelectedItemPosition();
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
                                        if(Rubrica.count(Rubrica.class)==0 || RubricaSelEva==null){
                                            Toast.makeText(this,"Crear o Seleccionar Rubrica",Toast.LENGTH_SHORT).show();
                                        }else{
                                            Long newRubricaPos = RubricaSelEva.getId();
                                            EditorCreatorEva(newName, newRubricaPos);
                                            finish();
                                        }
                                    }

                                }

                            }
                        }
                    }
                }

            }
        }


    }



    public void EditorCreatorMat(String Name){
        if (!editing) {
            Log.d("Materia", "saving");
            Materia materia = new Materia("" + Name);
            materia.save();
        } else {
            Log.d("Materia", "updating");

            List<Materia> materias = Materia.find(Materia.class, "name = ?", name);
            if (materias.size() > 0) {

                Materia materia = materias.get(0);
                Log.d("got materia", "materia: " + materia.getName());
                materia.setName("" + Name);
                materia.save();
            }
        }
    }

    public void EditorCreatorRub(String Name){
        if (!editing) {
            Log.d("Rubrica", "saving");
            Rubrica rubrica = new Rubrica("" + Name);
            rubrica.save();
        } else {
            Log.d("Rubrica", "updating");

            List<Rubrica> rubricas = Rubrica.find(Rubrica.class, "name = ?", name);
            if (rubricas.size() > 0) {

                Rubrica rubrica = rubricas.get(0);
                Log.d("got rubrica", "Rubrica: " + rubrica.getName());
                rubrica.setName("" + Name);
                rubrica.save();
            }
        }
    }

    public void EditorCreatorNivel(String Name){
        if (!editing) {
            Log.d("Nivel", "saving");
            Nivel nivel = new Nivel("" + Name,rubricaSel);
            nivel.save();

        } else {
            Log.d("Nivel", "updating");

            List<Nivel> niveles = Nivel.find(Nivel.class, "name = ? and rubrica =?", name,rubricaSel.getId().toString());
            if (niveles.size() > 0) {

                Nivel nivel = niveles.get(0);
                Log.d("got estudiante", "estudiante: " + nivel.getName());
                nivel.setName("" + Name);
                nivel.save();
            }
        }
    }

    public void EditorCreatorCat(String Name,int peso){
        if (!editing) {
            Log.d("Categoria", "saving");
            Categoria categoria = new Categoria("" + Name,peso,rubricaSel);
            categoria.save();
        } else {
            Log.d("Categoria", "updating");

            List<Categoria> categorias = Categoria.find(Categoria.class, "name = ? and rubrica =?", name,rubricaSel.getId().toString());
            if (categorias.size() > 0) {

                Categoria categoria = categorias.get(0);
                Log.d("got categoria", "categoria: " + categoria.getName());
                categoria.setName("" + Name);
                categoria.setPeso(peso);
                categoria.save();
            }
        }
    }

    public void EditorCreatorEle(String Name,int peso){
        if (!editing) {
            Log.d("Elemento", "saving");
            Elemento elemento = new Elemento("" + Name,peso,categoriaEle);
            elemento.save();
        } else {
            Log.d("Elemento", "updating");

            List<Elemento> elementos = Elemento.find(Elemento.class, "name = ? and categoria =?", name,categoriaEle.getId().toString());
            if (elementos.size() > 0) {

                Elemento elemento = elementos.get(0);
                Log.d("got elemento", "estudiante: " + elemento.getName());
                elemento.setName("" + Name);
                elemento.setPeso(peso);
                elemento.save();
            }
        }
    }

    public void EditorCreatorEva(String Name,Long Rubricaid){
        if (!editing) {
            Log.d("Evaluacion", "saving");
            Evaluacion evaluacion = new Evaluacion(Name,materiaestud, Rubricaid);
            evaluacion.save();
        } else {
            Log.d("Evaluacion", "updating");

            List<Evaluacion> evaluaciones = Evaluacion.find(Evaluacion.class, "name = ? and materia =?", name,materiaestud.getId().toString());
            if (evaluaciones.size() > 0) {

                Evaluacion evaluacion = evaluaciones.get(0);
                Log.d("got evaluacion", "evaluacion: " + evaluacion.getName());
                evaluacion.setName("" + Name);
                evaluacion.setRubrica(Rubricaid);
                evaluacion.save();
            }
        }
    }

    public void EditorCreatorEstud(String Name,int State){
        if (!editing) {
            Log.d("Estudiante", "saving");
            Estudiante estudiante = new Estudiante(Name,State,materiaestud);
            estudiante.save();
        } else {
            Log.d("Estudiante", "updating");

            List<Estudiante> estudiantes = Estudiante.find(Estudiante.class, "name = ? and materia =?", name,materiaestud.getId().toString());
            if (estudiantes.size() > 0) {

                Estudiante estudiante = estudiantes.get(0);
                Log.d("got estudiante", "estudiante: " + estudiante.getName());
                estudiante.setName("" + Name);
                estudiante.setState(State);
                estudiante.save();
            }
        }
    }

    public void onClick_EditNiveles(View view) {
            if(!editing && !viewing){
                Snackbar.make(layoutRoot, "Guardar Rubrica Primero.", Snackbar.LENGTH_LONG).show();
            }else{
                List<Rubrica> rubricas = Rubrica.find(Rubrica.class, "name = ?", name);
                if (rubricas.size() > 0) {
                    Rubrica rubrica = rubricas.get(0);

                    Intent i = new Intent(Agregar.this,Actividad_Niveles.class);
                    i.putExtra("Rub_name",rubrica.getName());
                    startActivity(i);
                }
            }
    }

    public void onClick_EditCategorias(View view) {
        if(!editing && !viewing){
            Snackbar.make(layoutRoot, "Guardar Rubrica Primero.", Snackbar.LENGTH_LONG).show();
        }else{
            List<Rubrica> rubricas = Rubrica.find(Rubrica.class, "name = ?", name);
            if (rubricas.size() > 0) {
                Rubrica rubrica = rubricas.get(0);
                Intent i = new Intent(Agregar.this,Actividad_Categorias.class);
                i.putExtra("Rub_name",rubrica.getName());
                i.putExtra("OpcionCreEva",true);
                startActivity(i);
            }
        }
    }

    public void onClick_addDescriptions(View view) {
        if(!editing && !viewing){
            Snackbar.make(layoutRoot, "Guardar Elemento Primero.", Snackbar.LENGTH_LONG).show();
        }else {
            List<Elemento> elementos = Elemento.find(Elemento.class, "name = ? and categoria =?", name,categoriaEle.getId().toString());
            if (elementos.size() > 0) {
                Elemento elemento = elementos.get(0);

                Intent i = new Intent(Agregar.this, Actividad_AddDescriptions.class);
                i.putExtra("Ele_id", elemento.getId());
                startActivity(i);
            }
        }
    }

    public void onClick_Evaluar(View view) {
        if(!editing && !viewing){
            Snackbar.make(layoutRoot, "Guardar Evaluacion Primero.", Snackbar.LENGTH_LONG).show();
        }else {
            Evaluacion evalua= Evaluacion.find(Evaluacion.class,"name = ?",getIntent().getStringExtra("Eva_name")).get(0);
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
                Toast.makeText(this,"Crear o Seleccionar Rubrica",Toast.LENGTH_SHORT).show();
            }
        }


    }

    public DialogInterface.OnClickListener dialogSave = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int choice) {
            switch(choice){
                case DialogInterface.BUTTON_POSITIVE:
                    String newName = nombre.getText().toString();
                    if(Rubrica.count(Rubrica.class)!=0 || RubricaSelEva!=null){
                        Long newRubricaPos = RubricaSelEva.getId();
                        EditorCreatorEva(newName, newRubricaPos);
                        Intent i = new Intent(Agregar.this, Actividad_Evaluar.class);
                        i.putExtra("Rub_id", RubricaSelEva.getId());
                        i.putExtra("Mat_id", materiaestud.getId());
                        i.putExtra("Eva_id", Evaluacion.find(Evaluacion.class, "name = ?", getIntent().getStringExtra("Eva_name")).get(0).getId());
                        startActivity(i);
                    }else{
                        Toast.makeText(Agregar.this, "No se Guardo el Registro", Toast.LENGTH_SHORT).show();
                    }

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
