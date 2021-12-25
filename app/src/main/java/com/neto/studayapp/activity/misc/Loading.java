package com.neto.studayapp.activity.misc;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.activity.form.FormLogin;
import com.neto.studayapp.activity.aluno.Professores;
import com.neto.studayapp.activity.professor.Disciplinas;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        TextView textoCarregamento = findViewById(R.id.textViewLoading);

        if(getIntent().hasExtra("mensagem")) {
            textoCarregamento.setText(getIntent().getExtras().getString("mensagem"));
        } else {
            textoCarregamento.setText(R.string.loading);
        }

        new Handler().postDelayed(() -> {
            FirebaseUser usuarioAtual = FirebaseAuth.getInstance().getCurrentUser();
            if(usuarioAtual != null) {
                verificaNivelConta(usuarioAtual.getUid());
            } else {
                startActivity(new Intent(Loading.this, FormLogin.class));
                finish();
            }
        }, 1500);
    }

    private void verificaNivelConta(String uuid) {
        new Handler().postDelayed(() -> {

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            DocumentReference dfAluno = database.collection("aluno").document(uuid);
            DocumentReference dfProfessor = database.collection("professor").document(uuid);

            dfAluno.get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.get("nivelAcesso") != null) {
                    startActivity(new Intent(Loading.this, Professores.class));
                    finish();
                }
            });

            dfProfessor.get().addOnSuccessListener(documentSnapshot -> {
                if(documentSnapshot.get("nivelAcesso") != null) {
                    startActivity(new Intent(Loading.this, Disciplinas.class));
                    finish();
                }
            });

        }, 1000);
    }

}