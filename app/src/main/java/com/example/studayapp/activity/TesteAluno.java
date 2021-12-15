package com.example.studayapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.studayapp.R;
import com.example.studayapp.model.Aluno;

public class TesteAluno extends AppCompatActivity {

    private Aluno aluno;

    private TextView nome;
    private TextView email;
    private TextView whatsapp;
    private TextView data;
    private TextView senha;
    private TextView tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teste_aluno);

        nome = findViewById(R.id.testeNomeId);
        email = findViewById(R.id.testeEmailId);
        whatsapp = findViewById(R.id.testeEmailId);
        data = findViewById(R.id.testeDataId);
        senha = findViewById(R.id.testeSenhaId);
        tipo = findViewById(R.id.testeTipoId);

        Intent intent = getIntent();
        if(intent.hasExtra("aluno")) {
            aluno = (Aluno) intent.getSerializableExtra("aluno");

            System.out.println("===  Teste ===");
            System.out.println("Nome: "+aluno.getNomeCompleto());
            System.out.println("Email: "+aluno.getEmail());
            System.out.println("Whatsapp: "+aluno.getWhatsapp());
            System.out.println("Senha: "+aluno.getSenha());
            System.out.println("Tipo: "+aluno.getTipo());


            nome.setText(aluno.getNomeCompleto());
            email.setText(aluno.getEmail());
            whatsapp.setText(aluno.getWhatsapp());
            //data.setText((CharSequence) aluno.getDataNascimento());
            senha.setText(aluno.getSenha());
            tipo.setText(String.valueOf(aluno.getTipo()));
            //Toast.makeText(getApplicationContext(), String.valueOf(aluno.getTipo()), Toast.LENGTH_SHORT).show();

        }
    }
}