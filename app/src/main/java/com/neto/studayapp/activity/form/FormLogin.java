package com.neto.studayapp.activity.form;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.activity.aluno.Professores;
import com.neto.studayapp.activity.misc.EscolhaCadastro;
import com.neto.studayapp.activity.professor.Disciplinas;
import com.neto.studayapp.util.Validator;

import java.util.Objects;

public class FormLogin extends AppCompatActivity {

    private EditText emailEditText, senhaEditText;
    private TextView alertLogin, alertEmail, alertSenha, textTelaCadastro;
    private Button buttonSubmit;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_login);
        iniciarComponentes();

        buttonSubmit.setOnClickListener(view -> {
            if (formularioIsValid()) {
                fazerLogin();
                buttonSubmit.setEnabled(false);
            } else {
                buttonSubmit.setEnabled(true);
            }
        });

        textTelaCadastro.setOnClickListener(view -> {
            startActivity(new Intent(this, EscolhaCadastro.class));
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioAtual != null) verificaNivelConta(usuarioAtual.getUid());
    }

    private void iniciarComponentes() {
        emailEditText = findViewById(R.id.editTextEmail);
        senhaEditText = findViewById(R.id.editTextSenha);
        buttonSubmit = findViewById(R.id.buttonLoginSubmit);
        alertLogin = findViewById(R.id.textViewAlertLogin);
        alertEmail = findViewById(R.id.textViewEmailAlert);
        alertSenha = findViewById(R.id.textViewSenhaAlert);
        textTelaCadastro = findViewById(R.id.textViewCliqueAqui);
        progressBar = findViewById(R.id.progressBar);
    }

    private boolean formularioIsValid() {
        boolean emailValido = false, senhaValida = false;
        Validator validator = new Validator();

        if (validator.emailIsValid(emailEditText)) {
            alertEmail.setVisibility(View.INVISIBLE);
            emailValido = true;
        } else {
            alertEmail.setVisibility(View.VISIBLE);
            alertEmail.setText(validator.getMsgEmail());
        }

        if (validator.senhaIsValid(senhaEditText)) {
            alertSenha.setVisibility(View.INVISIBLE);
            senhaValida = true;
        } else {
            alertSenha.setVisibility(View.VISIBLE);
            alertSenha.setText(validator.getMsgSenha());
        }

        return emailValido && senhaValida;
    }

    private void fazerLogin() {

        String email = emailEditText.getText().toString();
        String senha = senhaEditText.getText().toString();
        progressBar.setVisibility(View.VISIBLE);
        alertLogin.setVisibility(View.INVISIBLE);

        Task<AuthResult> authResultTask = FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha);
        authResultTask.addOnCompleteListener(task -> {

            if (task.isSuccessful()) {
                String uuid = Objects.requireNonNull(Objects.requireNonNull(authResultTask.getResult()).getUser()).getUid();
                verificaNivelConta(uuid);
            } else {
                buttonSubmit.setEnabled(true);
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException err) {
                    progressBar.setVisibility(View.INVISIBLE);
                    alertLogin.setVisibility(View.VISIBLE);
                    alertLogin.setText(R.string.erro_email_senha);
                } catch (FirebaseNetworkException err) {
                    progressBar.setVisibility(View.INVISIBLE);
                    alertLogin.setVisibility(View.VISIBLE);
                    alertLogin.setText(R.string.erro_internet);
                    Toast.makeText(getApplicationContext(), err.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (Exception err) {
                    progressBar.setVisibility(View.INVISIBLE);
                    alertLogin.setVisibility(View.VISIBLE);
                    alertLogin.setText(R.string.erro_login);
                    err.printStackTrace();
                    Toast.makeText(getApplicationContext(), err.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verificaNivelConta(String uuid) {
        new Handler().postDelayed(() -> {
            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference dfAluno = database.collection("alunos").document(uuid);
            DocumentReference dfProfessor = database.collection("professores").document(uuid);

            dfAluno.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.get("nivelAcesso") != null) {
                    startActivity(new Intent(FormLogin.this, Professores.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            });

            dfProfessor.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.get("nivelAcesso") != null) {
                    startActivity(new Intent(FormLogin.this, Disciplinas.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                }
            });

        }, 1500);

    }
}