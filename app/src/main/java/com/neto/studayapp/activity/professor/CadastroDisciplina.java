package com.neto.studayapp.activity.professor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.activity.misc.Loading;
import com.neto.studayapp.model.Disciplina;
import com.neto.studayapp.model.Disponibilidade;
import com.neto.studayapp.util.Mask;
import com.neto.studayapp.util.Validator;
import com.google.android.material.navigation.NavigationView;

import java.lang.ref.Reference;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CadastroDisciplina extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    View header;
    Spinner nome, diaSemana;
    EditText valor, das, ate;
    Button btnSubmit;
    TextView nomeUsuario, alertDisciplina, alertValor, alertDia, alertDas, alertAte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_disciplina);
        iniciarComponentes();

        // mascaras para formatar a hora
        das.addTextChangedListener(Mask.insert("##:##", das));
        ate.addTextChangedListener(Mask.insert("##:##", ate));

        btnSubmit.setOnClickListener(view -> {
            if (formularioIsValid()) {
                cadastrarDisciplina();
            } else {
                Toast.makeText(getApplicationContext(), "Verifique possíveis campos com erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String uuidProfessor = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("professores").document(uuidProfessor);
        documentReference.addSnapshotListener((documentSnapshot, error) -> {
            if (documentSnapshot != null) {
                String nome = documentSnapshot.getString("nomeCompleto");
                String text = "Olá " + Objects.requireNonNull(nome).split("[ ]")[0] + "!";
                nomeUsuario.setText(text);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(this, Disciplinas.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
    }

    @SuppressLint("NonConstantResourceId")
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.minhaContaId:
                startActivity(new Intent(this, MinhaContaProfessor.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.disciplinasId:
                startActivity(new Intent(this, Disciplinas.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.avaliacoesId:
                Toast.makeText(this, "Avaliações", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sairId:
                deslogar();
                break;
            default:
                break;
        }
        return true;
    }

    private void iniciarComponentes() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toobarMenu);
        header = navigationView.getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);
        nome = findViewById(R.id.spinnerDisciplina);
        valor = findViewById(R.id.editTextValor);
        diaSemana = findViewById(R.id.spinnerDiaSemana);
        das = findViewById(R.id.editTextDas);
        ate = findViewById(R.id.editTextAte);
        btnSubmit = findViewById(R.id.buttonSubmitCadastro);
        alertDisciplina = findViewById(R.id.textViewDisciplinaAlert);
        alertValor = findViewById(R.id.textViewValorAlert);
        alertDia = findViewById(R.id.textViewDiaSemanaAlert);
        alertDas = findViewById(R.id.textViewDasAlert);
        alertAte = findViewById(R.id.textViewAteAlert);
        iniciarMenu();
        setSpinnerDia();
        setSpinnerDisciplina();
    }

    private void iniciarMenu() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setSpinnerDia() {
        String[] valores = getResources().getStringArray(R.array.dias);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diaSemana.setAdapter(adapter);
    }

    private void setSpinnerDisciplina() {
        String[] valores = getResources().getStringArray(R.array.disciplinas);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nome.setAdapter(adapter);
    }

    private boolean formularioIsValid() {

        boolean disciplinaValido = false, valorValido = false, diaValido = false;
        boolean dasValido = false, ateValido = false;
        Validator validator = new Validator();

        if (validator.selectIsValid(nome)) {
            alertDisciplina.setVisibility(View.INVISIBLE);
            disciplinaValido = true;
        } else {
            alertDisciplina.setVisibility(View.VISIBLE);
            alertDisciplina.setText(validator.getMsgSelect());
        }

        if (validator.valorIsValid(valor)) {
            alertValor.setVisibility(View.INVISIBLE);
            valorValido = true;
        } else {
            alertValor.setVisibility(View.VISIBLE);
            alertValor.setText(validator.getMsgValor());
        }

        if (validator.selectIsValid(diaSemana)) {
            alertDia.setVisibility(View.INVISIBLE);
            diaValido = true;
        } else {
            alertDia.setVisibility(View.VISIBLE);
            alertDia.setText(validator.getMsgSelect());
        }

        if (validator.horaIsValid(das)) {
            alertDas.setVisibility(View.INVISIBLE);
            dasValido = true;
        } else {
            alertDas.setVisibility(View.VISIBLE);
            alertDas.setText(validator.getMsgHora());
        }

        if (validator.horaIsValid(ate)) {
            alertAte.setVisibility(View.INVISIBLE);
            ateValido = true;
        } else {
            alertAte.setVisibility(View.VISIBLE);
            alertAte.setText(validator.getMsgHora());
        }

        return disciplinaValido && valorValido && diaValido && dasValido && ateValido;

    }

    private void cadastrarDisciplina() {

        Disciplina disciplina = new Disciplina();
        List<Disponibilidade> disponibilidades = new ArrayList<>();
        Disponibilidade disponibilidade = new Disponibilidade();
        String uuidProfessor = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        disponibilidade.setDiaDaSemana(diaSemana.getSelectedItem().toString());
        disponibilidade.setHorarioInicio(das.getText().toString());
        disponibilidade.setHorarioFinal(ate.getText().toString());
        disponibilidades.add(disponibilidade);
        disciplina.setUuidProfessor(uuidProfessor);
        disciplina.setNome(nome.getSelectedItem().toString());
        disciplina.setCusto(Double.parseDouble(valor.getText().toString().replaceAll("[,]", ".")));
        disciplina.setDisponibilidade(disponibilidades);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference df = database.collection("disciplinas").document(disciplina.getUuid());
        df.set(disciplina).addOnCompleteListener(task -> {
            if (task.isComplete()) {
                Toast.makeText(getApplicationContext(), "Sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (Exception err) {
                    //Log.d("teste", err.getMessage());
                    Toast.makeText(getApplicationContext(), err.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void voltar(View view) {
        startActivity(new Intent(this, Disciplinas.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void deslogar() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Loading.class);
        intent.putExtra("mensagem", "Saindo...");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}


//        System.out.println("=== objeto disciplina criado ===");
//        System.out.println("Nome: " + disciplina.getNome());
//        System.out.println("Valor: " + disciplina.getCusto());
//        System.out.println("Dia: " + disciplina.getDisponibilidade().get(0).getDiaDaSemana());
//        System.out.println("Das: " + disciplina.getDisponibilidade().get(0).getHorarioInicio());
//        System.out.println("Até: " + disciplina.getDisponibilidade().get(0).getHorarioFinal());
//        System.out.println("===============================");