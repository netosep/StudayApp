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
import java.util.UUID;

public class CadastroDisciplina extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Spinner nomeDisciplina, diaDe, diaAte;
    private EditText horaDas, horaAte;
    private Button btnSubmit;
    private TextView nomeUsuario, alertDisciplina, alertDiaDe, alertDiaAte, alertHoraDas, alertHoraAte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_disciplina);
        iniciarComponentes();

        // mascaras para formatar a hora
        horaDas.addTextChangedListener(Mask.insert("##:##", horaDas));
        horaAte.addTextChangedListener(Mask.insert("##:##", horaAte));

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
        View header = navigationView.getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);
        // editText e spinner
        nomeDisciplina = findViewById(R.id.spinnerDisciplina);
        diaDe = findViewById(R.id.spinnerDiaDe);
        diaAte = findViewById(R.id.spinnerDiaAte);
        horaDas = findViewById(R.id.editTextDas);
        horaAte = findViewById(R.id.editTextAte);
        // textView
        alertDisciplina = findViewById(R.id.textViewDisciplinaAlert);
        alertDiaDe = findViewById(R.id.textViewDiaDeAlert);
        alertDiaAte = findViewById(R.id.textViewDiaAteAlert);
        alertHoraDas = findViewById(R.id.textViewHoraDasAlert);
        alertHoraAte = findViewById(R.id.textViewHoraAteAlert);
        // button
        btnSubmit = findViewById(R.id.buttonSubmitCadastro);
        iniciarMenu();
        setSpinnersDia();
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

    private void setSpinnersDia() {
        String[] valores = getResources().getStringArray(R.array.dias);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diaDe.setAdapter(adapter);
        diaAte.setAdapter(adapter);
    }

    private void setSpinnerDisciplina() {
        String[] valores = getResources().getStringArray(R.array.disciplinas);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nomeDisciplina.setAdapter(adapter);
    }

    private boolean formularioIsValid() {
        boolean disciplinaValido = false, diaDeValido = false, diaAteValido = false;
        boolean horaDasValido = false, horaAteValido = false, formValido = false;
        Validator validator = new Validator();

        if (validator.selectIsValid(nomeDisciplina)) {
            alertDisciplina.setVisibility(View.INVISIBLE);
            disciplinaValido = true;
        } else {
            alertDisciplina.setVisibility(View.VISIBLE);
            alertDisciplina.setText(validator.getMsgSelect());
        }

        if (validator.selectIsValid(diaDe)) {
            alertDiaDe.setVisibility(View.INVISIBLE);
            diaDeValido = true;
        } else {
            alertDiaDe.setVisibility(View.VISIBLE);
            alertDiaDe.setText(validator.getMsgSelect());
        }

        if (validator.selectIsValid(diaAte)) {
            alertDiaAte.setVisibility(View.INVISIBLE);
            diaAteValido = true;
        } else {
            alertDiaAte.setVisibility(View.VISIBLE);
            alertDiaAte.setText(validator.getMsgSelect());
        }

        if (diaDeValido && diaAteValido) {
            if (validator.diaIsBiggestOrEquals(diaAte, diaDe)) {
                alertDiaAte.setVisibility(View.INVISIBLE);
                formValido = true;
            } else {
                alertDiaAte.setVisibility(View.VISIBLE);
                alertDiaAte.setText(validator.getMsgDia());
            }
        }

        if (validator.horaIsValid(horaDas)) {
            alertHoraDas.setVisibility(View.INVISIBLE);
            horaDasValido = true;
        } else {
            alertHoraDas.setVisibility(View.VISIBLE);
            alertHoraDas.setText(validator.getMsgHora());
        }

        if (validator.horaIsValid(horaAte)) {
            alertHoraAte.setVisibility(View.INVISIBLE);
            horaAteValido = true;
        } else {
            alertHoraAte.setVisibility(View.VISIBLE);
            alertHoraAte.setText(validator.getMsgHora());
        }

        return disciplinaValido && diaDeValido && diaAteValido
                && horaDasValido && horaAteValido && formValido;

    }

    private void cadastrarDisciplina() {
        String uuidProfessor = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Disciplina disciplina = new Disciplina();
        Disponibilidade disponibilidade = new Disponibilidade();

        disciplina.setUuid(UUID.randomUUID().toString());
        disciplina.setNome(nomeDisciplina.getSelectedItem().toString());
        disciplina.setUuidProfessor(uuidProfessor);
        disponibilidade.setHoraDas(horaDas.getText().toString());
        disponibilidade.setHoraAte(horaAte.getText().toString());
        disponibilidade.setDiaDe(diaDe.getSelectedItem().toString());
        disponibilidade.setDiaAte(diaAte.getSelectedItem().toString());
        disciplina.setDisponibilidade(disponibilidade);

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("disciplinas").document(disciplina.getUuid()).set(disciplina);

        Toast.makeText(this, "Disciplina cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, Disciplinas.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
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