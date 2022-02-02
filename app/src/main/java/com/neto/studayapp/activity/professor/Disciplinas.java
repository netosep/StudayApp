package com.neto.studayapp.activity.professor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.neto.studayapp.R;
import com.google.android.material.navigation.NavigationView;
import com.neto.studayapp.activity.misc.Loading;
import com.neto.studayapp.adapter.DisciplinaAdapter;
import com.neto.studayapp.model.Disciplina;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Disciplinas extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private SearchView searchView;
    private TextView nomeUsuario, disciplinasVazio;
    private ImageFilterView imgPreviewMenu;
    private List<Disciplina> disciplinas;
    private RecyclerView recyclerView;
    private DisciplinaAdapter disciplinaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplinas);
        iniciarComponentes();
        carregarRecyclerView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                List<Disciplina> disciplinaFilterList = new ArrayList<>();
                for (Disciplina disciplina : disciplinas) {
                    if(disciplina.getNome().toLowerCase().contains(newText.toLowerCase())) {
                        disciplinaFilterList.add(disciplina);
                    }
                }
                disciplinaAdapter.filtrarDalista(disciplinaFilterList);
                return true;
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
            super.onBackPressed();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.minhaContaId:
                startActivity(new Intent(this, MinhaContaProfessor.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.disciplinasId:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
//            case R.id.avaliacoesId:
//                Toast.makeText(this, "Avaliações", Toast.LENGTH_SHORT).show();
//                break;
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
        View header = navigationView.getHeaderView(0);
        imgPreviewMenu = header.findViewById(R.id.imgPreviewMenu);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);
        searchView = findViewById(R.id.searchViewDisciplina);
        recyclerView = findViewById(R.id.recViewDisciplinas);
        disciplinasVazio = findViewById(R.id.textViewDisciplinasVazio);
        iniciarMenu();
    }

    private void iniciarMenu() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void carregarRecyclerView() {

        String uuidProfessor = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this){
            @Override public boolean canScrollVertically() { return false; }
        });
        disciplinas = new ArrayList<>();
        disciplinaAdapter = new DisciplinaAdapter(disciplinas, this);
        recyclerView.setAdapter(disciplinaAdapter);

        Query query = database.collection("disciplinas").whereEqualTo("uuidProfessor", uuidProfessor);
        query.addSnapshotListener((value, error) -> {
            if (value != null) {
                if (value.isEmpty()) {
                    recyclerView.setVisibility(View.INVISIBLE);
                    disciplinasVazio.setVisibility(View.VISIBLE);
                } else {
                    disciplinasVazio.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                }
                for (DocumentChange dc : value.getDocumentChanges()) {
                    if (dc.getType() == DocumentChange.Type.ADDED) {
                        disciplinas.add(dc.getDocument().toObject(Disciplina.class));
                    }
                    if (dc.getType() == DocumentChange.Type.REMOVED) {
                        disciplinas.remove(dc.getDocument().toObject(Disciplina.class));
                    }
                    disciplinaAdapter.notifyDataSetChanged();
                }
            }
            if (error != null) {
                System.out.println(error.getMessage());
            }
        });
    }

    public void cadastrarDisciplina(View view) {
        startActivity(new Intent(getApplicationContext(), CadastroDisciplina.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        finish();
    }

    private void deslogar() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Disciplinas.this, Loading.class);
        intent.putExtra("mensagem", "Saindo...");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}