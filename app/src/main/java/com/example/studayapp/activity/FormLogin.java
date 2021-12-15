package com.example.studayapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studayapp.R;
import com.example.studayapp.util.Validator;

public class FormLogin extends AppCompatActivity {

    private EditText emailEditText;
    private EditText senhaEditText;
    private Button buttonSubmit;

    private TextView alertLogin;
    private TextView alertEmail;
    private TextView alertSenha;

    private TextView textTelaCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        iniciarComponentes();

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(formularioIsValid()) {
                    String email = emailEditText.getText().toString();
                    String senha = senhaEditText.getText().toString();
                    if(email.equals("pr@email.com") && senha.equals("12345")) {
                        alertLogin.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(getApplicationContext(), Disciplinas.class);
                        startActivity(intent);
                    }
                    else if(email.equals("aluno@email.com") && senha.equals("12345")) {
                        alertLogin.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(getApplicationContext(), Professores.class);
                        startActivity(intent);
                    } else {
                        alertLogin.setVisibility(View.VISIBLE);
                        alertLogin.setText("E-mail ou senha incoretos!");
                    }
                }
            }
        });

        textTelaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormLogin.this, EscolhaCadastro.class);
                startActivity(intent);
            }
        });
    }

    private void iniciarComponentes() {
        emailEditText = findViewById(R.id.editTextEmail);
        senhaEditText = findViewById(R.id.editTextSenha);
        buttonSubmit = findViewById(R.id.buttonLoginSubmit);
        alertLogin = findViewById(R.id.textViewAlertLogin);
        alertEmail = findViewById(R.id.textViewEmailAlert);
        alertSenha = findViewById(R.id.textViewSenhaAlert);
        textTelaCadastro = findViewById(R.id.textViewCliqueAqui);
    }

    private boolean formularioIsValid() {

        boolean emailValido = false, senhaValida = false;
        Validator validator = new Validator();

        if(validator.emailIsValid(emailEditText)) {
            alertEmail.setVisibility(View.INVISIBLE);
            emailValido = true;
        } else {
            alertEmail.setVisibility(View.VISIBLE);
            alertEmail.setText(validator.getMsgEmail());
        }

        if(validator.senhaIsValid(senhaEditText)) {
            alertSenha.setVisibility(View.INVISIBLE);
            senhaValida = true;
        } else {
            alertSenha.setVisibility(View.VISIBLE);
            alertSenha.setText(validator.getMsgSenha());
        }

        if(emailValido && senhaValida) {
            return true;
        } else {
            return false;
        }
    }
}