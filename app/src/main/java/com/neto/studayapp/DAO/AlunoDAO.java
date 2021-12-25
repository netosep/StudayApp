package com.neto.studayapp.DAO;

import android.annotation.SuppressLint;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.neto.studayapp.model.Aluno;

public class AlunoDAO {

    public AlunoDAO() {
    }

    @SuppressLint("SimpleDateFormat")
    public Aluno getAluno(String uuid) {
        final Aluno aluno = new Aluno();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("aluno").document(uuid);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    aluno.setNomeCompleto(value.getString("nomeCompleto"));
                    aluno.setWhatsapp(value.getString("whatsapp"));
                    aluno.setDataNascimento(value.getDate("dataNascimento"));
                }
            }
        });
        System.out.println(aluno.getNomeCompleto());
        return aluno;
    }

}
