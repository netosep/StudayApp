package com.neto.studayapp.activity.professor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.activity.misc.Loading;
import com.neto.studayapp.model.Disciplina;
import com.neto.studayapp.model.Disponibilidade;
import com.neto.studayapp.util.Mask;
import com.neto.studayapp.util.Validator;

import java.util.Objects;

public class EditarDisciplina extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Spinner nomeDisciplina, diaDe, diaAte;
    private EditText horaDas, horaAte;
    private ImageFilterView imgPreviewMenu;
    private Button btnSubmit;
    private TextView nomeUsuario, alertDisciplina, alertDiaDe, alertDiaAte, alertHoraDas, alertHoraAte;
    private Disciplina disciplina = new Disciplina();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_disciplina);
        iniciarComponentes();

        if (getIntent().hasExtra("disciplina")) {
            disciplina = (Disciplina) getIntent().getSerializableExtra("disciplina");
            setarValores();
        }

        // mascaras para formatar a hora
        horaDas.addTextChangedListener(Mask.insert("##:##", horaDas));
        horaAte.addTextChangedListener(Mask.insert("##:##", horaAte));

        btnSubmit.setOnClickListener(view -> {
            if (formularioIsValid()) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                DocumentReference documentReference = database.collection("disciplinas").document(disciplina.getUuid());
                atualizarDisciplina(documentReference);
                btnSubmit.setEnabled(false);
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
                String urlImg = documentSnapshot.getString("urlFotoPerfil");
                if (nome != null) {
                    String text = "Olá " + nome.split("[ ]")[0] + "!";
                    nomeUsuario.setText(text);
                }
                if (urlImg != null) {
                    Glide.with(getApplicationContext()).load(urlImg).into(imgPreviewMenu);
                }
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
//            case R.id.avaliacoesId:
//                Toast.makeText(this, "Avaliações", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.infoId:
                Toast.makeText(this, "App Studay | Versão: 1.0", Toast.LENGTH_SHORT).show();
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
        imgPreviewMenu = header.findViewById(R.id.imgPreviewMenu);
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

    private void setarValores() {
        int indiceDisciplina = 0, indiceDiaDe = 0, indiceDiaAte = 0;
        String[] dias = getResources().getStringArray(R.array.dias);
        String[] disciplinas = getResources().getStringArray(R.array.disciplinas);

        for (int i = 0; i < dias.length; i++) {
            if (dias[i].equals(disciplina.getDisponibilidade().getDiaDe())) indiceDiaDe = i;
            if (dias[i].equals(disciplina.getDisponibilidade().getDiaAte())) indiceDiaAte = i;
        }
        for (int i = 0; i < disciplinas.length; i++) {
            if (disciplinas[i].equals(disciplina.getNome())) indiceDisciplina = i;
        }

        nomeDisciplina.setSelection(indiceDisciplina);
        nomeDisciplina.setEnabled(false);
        diaDe.setSelection(indiceDiaDe);
        diaAte.setSelection(indiceDiaAte);
        horaDas.setText(disciplina.getDisponibilidade().getHoraDas());
        horaAte.setText(disciplina.getDisponibilidade().getHoraAte());
    }

    private void atualizarDisciplina(DocumentReference documentRef) {
        String uuidProfessor = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        Disciplina attDisciplina = new Disciplina();
        Disponibilidade attDisponibilidade = new Disponibilidade();

        attDisciplina.setUuid(disciplina.getUuid());
        attDisciplina.setNome(nomeDisciplina.getSelectedItem().toString());
        attDisciplina.setUuidProfessor(uuidProfessor);
        attDisponibilidade.setHoraDas(horaDas.getText().toString());
        attDisponibilidade.setHoraAte(horaAte.getText().toString());
        attDisponibilidade.setDiaDe(diaDe.getSelectedItem().toString());
        attDisponibilidade.setDiaAte(diaAte.getSelectedItem().toString());
        attDisciplina.setDisponibilidade(attDisponibilidade);

        documentRef.set(attDisciplina);
        // voltando para a tela de disciplinas
        Toast.makeText(this, "Disciplina atualizada com sucesso!", Toast.LENGTH_SHORT).show();
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