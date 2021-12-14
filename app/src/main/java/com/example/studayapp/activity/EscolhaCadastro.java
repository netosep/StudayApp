package com.example.studayapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.studayapp.R;

public class EscolhaCadastro extends AppCompatActivity {

    private CardView cardEstudar;
    private CardView cardDarAulas;
    private ConstraintLayout jaPossuoConta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolha_cadastro);

        cardEstudar = findViewById(R.id.cardEstudar);
        cardEstudar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FormCadastroAluno.class);
                startActivity(intent);
            }
        });

        cardDarAulas = findViewById(R.id.cardDarAulas);
        cardDarAulas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), FormCadastroProfessor.class);
                startActivity(intent);
            }
        });

        jaPossuoConta = findViewById(R.id.jaPossuoContaId);
        jaPossuoConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FormLogin.class);
                startActivity(intent);
            }
        });

    }

    public void voltar(View view) {
        startActivity(new Intent(getApplicationContext(), FormLogin.class));
    }
}