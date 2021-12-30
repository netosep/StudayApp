package com.neto.studayapp.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.model.Disciplina;
import com.neto.studayapp.model.Professor;

import java.text.DecimalFormat;
import java.util.List;

public class ProfessorAdapter extends RecyclerView.Adapter {

    List<Professor> professores;
    List<Disciplina> disciplinas;

    public ProfessorAdapter(List<Professor> professores, List<Disciplina> disciplinas) {
        this.professores = professores;
        this.disciplinas = disciplinas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_professor, parent, false);
        return new ViewHolderClass(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DecimalFormat df = new DecimalFormat("#00.00");
        ViewHolderClass vhClass = (ViewHolderClass) holder;
        Professor professor = professores.get(position);
        //Disciplina disciplina = disciplinas.get(position);
        StringBuilder nomesDisciplina = new StringBuilder();
        for (int i = 0; i < disciplinas.size(); i++) {
            if(professores.get(position).getUuidProfessor().equals(disciplinas.get(i).getUuidProfessor())) {
                nomesDisciplina.append(disciplinas.get(i).getNome()).append(", ");
            }
        }
        if(nomesDisciplina.length() > 2) {
            int size = nomesDisciplina.length();
            nomesDisciplina = new StringBuilder(nomesDisciplina.substring(0, size - 2) + ".");
        }
        vhClass.nomeProfessor.setText(professor.getNomeCompleto());
        vhClass.disciplina.setText(nomesDisciplina.toString());
        vhClass.descricao.setText(professor.getDescricao());
        vhClass.ratingBar.setRating(3.5F);
        vhClass.sobre.setText(professor.getBiografia());
        vhClass.valor.setText("R$ " + df.format(professor.getValorAula()));
        vhClass.professor = professor;
    }

    @Override
    public int getItemCount() {
        return professores.size();
    }

    public static class ViewHolderClass extends RecyclerView.ViewHolder {

        TextView nomeProfessor, disciplina, descricao, sobre, valor;
        RatingBar ratingBar;
        LinearLayout buttonWhatsapp;
        ImageButton buttonFavorito, buttonAvaliar;
        Professor professor;
        FirebaseFirestore database;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            iniciarComponentes(itemView);

            database = FirebaseFirestore.getInstance();

            buttonWhatsapp.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), professor.getNomeCompleto(), Toast.LENGTH_SHORT).show();
            });
            buttonFavorito.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "Favoritou " + professor.getNomeCompleto(), Toast.LENGTH_SHORT).show();
            });
            buttonAvaliar.setOnClickListener(view -> {
                Toast.makeText(view.getContext(), "Avaliou " + professor.getNomeCompleto(), Toast.LENGTH_SHORT).show();
            });

            // desenvolver metodos

        }

        private void iniciarComponentes(View itemView) {
            nomeProfessor = itemView.findViewById(R.id.textViewNomeProfessor);
            disciplina = itemView.findViewById(R.id.textViewDisciplina);
            ratingBar = itemView.findViewById(R.id.ratingProfessor);
            descricao = itemView.findViewById(R.id.textViewDescricao);
            sobre = itemView.findViewById(R.id.textViewSobre);
            valor = itemView.findViewById(R.id.textViewValor);
            buttonWhatsapp = itemView.findViewById(R.id.buttonWhatsapp);
            buttonAvaliar = itemView.findViewById(R.id.buttonAvaliar);
            buttonFavorito = itemView.findViewById(R.id.buttonFavorito);
        }
    }
}
