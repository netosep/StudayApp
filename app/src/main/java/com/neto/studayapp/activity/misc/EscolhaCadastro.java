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
            startActivity(new Intent(this, FormCadastroAluno.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        cardDarAulas.setOnClickListener(view -> {
            startActivity(new Intent(this, FormCadastroProfessor.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        jaPossuoConta.setOnClickListener(view -> {
            startActivity(new Intent(this, FormLogin.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EscolhaCadastro.this, FormLogin.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void voltar(View view) {
        startActivity(new Intent(EscolhaCadastro.this, FormLogin.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void iniciarComponentes() {
        cardEstudar = findViewById(R.id.cardEstudar);
        cardDarAulas = findViewById(R.id.cardDarAulas);
        jaPossuoConta = findViewById(R.id.jaPossuoContaId);
    }
}