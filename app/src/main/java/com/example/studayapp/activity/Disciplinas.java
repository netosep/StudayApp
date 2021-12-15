package com.example.studayapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.utils.widget.ImageFilterButton;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studayapp.R;
import com.google.android.material.navigation.NavigationView;

public class Disciplinas extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextView nomeUsuario;
    View header;

    ImageFilterButton addDisciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplinas);
        iniciarComponentes();

        /* ------------------- Navigation Drawer Menu ------------------- */
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        /* -------------------------------------------------------------- */

        nomeUsuario.setText("Olá Professor!");


    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.minhaContaId:
                Toast.makeText(getApplicationContext(), "Minha conta", Toast.LENGTH_SHORT).show();
                break;
            case R.id.disciplinasId:
                startActivity(new Intent(getApplicationContext(), Disciplinas.class));
                break;
            case R.id.avaliacoesId:
                Toast.makeText(getApplicationContext(), "Avaliações", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sairId:
                Toast.makeText(getApplicationContext(), "Sair", Toast.LENGTH_SHORT).show();
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

        addDisciplina = findViewById(R.id.addDisciplinaId);
    }

    public void cadastrarDisciplina(View view) {
        startActivity(new Intent(getApplicationContext(), FormCadastroDisciplina.class));
    }

}