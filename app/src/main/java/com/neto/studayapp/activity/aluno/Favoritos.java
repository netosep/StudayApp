package com.neto.studayapp.activity.aluno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.google.android.material.navigation.NavigationView;
import com.neto.studayapp.activity.misc.Loading;

import java.util.Objects;

public class Favoritos extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    TextView nomeUsuario;
    View header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);
        iniciarComponentes();
    }

    @Override
    protected void onStart() {
        super.onStart();
        String uuidAluno = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("aluno").document(uuidAluno);
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
            voltar();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.minhaContaId:
                Toast.makeText(this, "Minha conta", Toast.LENGTH_SHORT).show();
                break;
            case R.id.favoritosId:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.professoresId:
                startActivity(new Intent(this, Professores.class));
                finish();
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

    public void voltar() {
        startActivity(new Intent(Favoritos.this, Professores.class));
        finish();
    }

    public void iniciarComponentes() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toobarMenu);
        header = navigationView.getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);
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

    private void deslogar() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Favoritos.this, Loading.class);
        intent.putExtra("mensagem", R.string.saindo);
        startActivity(intent);
        finish();
    }
}