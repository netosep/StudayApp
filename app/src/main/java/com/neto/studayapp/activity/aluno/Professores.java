package com.neto.studayapp.activity.aluno;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.neto.studayapp.R;
import com.google.android.material.navigation.NavigationView;
import com.neto.studayapp.activity.misc.Loading;
import com.neto.studayapp.adapter.ProfessorAdapter;
import com.neto.studayapp.model.Disciplina;
import com.neto.studayapp.model.Professor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Professores extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView nomeUsuario;
    View header;
    RatingBar ratingBar;
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    ProfessorAdapter professorAdapter;
    List<Professor> professores = new ArrayList<>();
    List<Disciplina> disciplinas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professores);
        iniciarComponentes();
        carregarRecyclerView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String uuidAluno = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("alunos").document(uuidAluno);
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
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.minhaContaId:
                startActivity(new Intent(this, MinhaContaAluno.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.favoritosId:
                startActivity(new Intent(this, Favoritos.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.professoresId:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.avaliacoesId:
                Toast.makeText(this, "Avaliações", Toast.LENGTH_SHORT).show();
                break;
            case R.id.infoId:
                Toast.makeText(this, "Sobre", Toast.LENGTH_SHORT).show();
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
        recyclerView = findViewById(R.id.recViewProfessores);
        ratingBar = findViewById(R.id.ratingProfessor);
        // professores vazio
        iniciarMenu();
    }

    private void iniciarMenu() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(Professores.this);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void carregarRecyclerView() {

        FirebaseFirestore database = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        professorAdapter = new ProfessorAdapter(professores, disciplinas);
        recyclerView.setAdapter(professorAdapter);

        database.collection("professores").addSnapshotListener((ssProfessores, errorProfessor) -> {
            if (ssProfessores != null) {
                for (DocumentChange dcProfessor : ssProfessores.getDocumentChanges()) {
                    String uuidProfessor = Objects.requireNonNull(dcProfessor.getDocument().get("uuidProfessor")).toString();
                    Query query = database.collection("disciplinas").whereEqualTo("uuidProfessor", uuidProfessor);
                    query.addSnapshotListener((ssDisciplinas, errorDisciplina) -> {
                        if (ssDisciplinas != null) {
                            for (DocumentChange dcDisciplina : ssDisciplinas.getDocumentChanges()) {
                                // professores
                                if (dcProfessor.getType() == DocumentChange.Type.ADDED) {
                                    professores.add(dcProfessor.getDocument().toObject(Professor.class));
                                }
                                // if (dcProfessor.getType() == DocumentChange.Type.REMOVED) {
                                //     professores.remove(dcProfessor.getDocument().toObject(Professor.class));
                                // }
                                // disciplinas
                                if (dcDisciplina.getType() == DocumentChange.Type.ADDED) {
                                    disciplinas.add(dcDisciplina.getDocument().toObject(Disciplina.class));
                                }
                                // if (dcDisciplina.getType() == DocumentChange.Type.REMOVED) {
                                //    disciplinas.remove(dcDisciplina.getDocument().toObject(Disciplina.class));
                                // }
                                professorAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
            }
        });

    }

    private void deslogar() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Professores.this, Loading.class);
        intent.putExtra("mensagem", "Saindo...");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}