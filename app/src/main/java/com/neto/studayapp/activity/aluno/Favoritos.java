package com.neto.studayapp.activity.aluno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.neto.studayapp.R;
import com.google.android.material.navigation.NavigationView;
import com.neto.studayapp.activity.misc.Loading;
import com.neto.studayapp.adapter.FavoritosAdapter;
import com.neto.studayapp.model.Favorito;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Favoritos extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView nomeUsuario;
    View header;

    List<Favorito> favoritos;
    RecyclerView recyclerView;
    FavoritosAdapter favoritosAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
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
            startActivity(new Intent(Favoritos.this, Professores.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
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
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.professoresId:
                startActivity(new Intent(this, Professores.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
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

    public void iniciarComponentes() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toobarMenu);
        header = navigationView.getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);
        recyclerView = findViewById(R.id.recViewFavoritos);
        // add vazio
        iniciarMenu();
    }

    private void iniciarMenu() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(Favoritos.this);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void carregarRecyclerView() {

        String uuidAluno = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this) {
            @Override public boolean canScrollVertically() { return false; }
        });
        favoritos = new ArrayList<>();
        favoritosAdapter = new FavoritosAdapter(favoritos);
        recyclerView.setAdapter(favoritosAdapter);

        Query query = database.collection("favoritos").whereEqualTo("uuidAluno", uuidAluno);
        query.addSnapshotListener((value, error) -> {
           if (value != null) {
               for (DocumentChange dc : value.getDocumentChanges()) {
                   if (dc.getType() == DocumentChange.Type.ADDED) {
                       favoritos.add(dc.getDocument().toObject(Favorito.class));
                       //System.out.println(favoritos.get(0).getProfessor().toString());
                   }
                   if (dc.getType() == DocumentChange.Type.REMOVED) {
                       favoritos.remove(dc.getDocument().toObject(Favorito.class));
                   }
                   favoritosAdapter.notifyDataSetChanged();
               }
           }
           if (error != null) {
               // Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
               Log.e("FirebaseError", error.getMessage());
           }
        });

    }

    public void voltar(View view) {
        startActivity(new Intent(Favoritos.this, Professores.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void deslogar() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Favoritos.this, Loading.class);
        intent.putExtra("mensagem", "Saindo...");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }
}