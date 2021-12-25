package com.neto.studayapp.activity.misc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.neto.studayapp.R;
import com.neto.studayapp.activity.form.FormCadastroAluno;
import com.neto.studayapp.activity.form.FormCadastroProfessor;
import com.neto.studayapp.activity.form.FormLogin;

public class EscolhaCadastro extends AppCompatActivity {

    private CardView cardEstudar, cardDarAulas;
    private ConstraintLayout jaPossuoConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_cadastro);
        iniciarComponentes();

        cardEstudar.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), FormCadastroAluno.class));
            finish();
        });

        cardDarAulas.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), FormCadastroProfessor.class));
            finish();
        });

        jaPossuoConta.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), FormLogin.class));
            finish();
        });

    }

    public void voltar(View view) {
        startActivity(new Intent(EscolhaCadastro.this, FormLogin.class));
        finish();
    }

    public void iniciarComponentes() {
        cardEstudar = findViewById(R.id.cardEstudar);
        cardDarAulas = findViewById(R.id.cardDarAulas);
        jaPossuoConta = findViewById(R.id.jaPossuoContaId);
    }
}