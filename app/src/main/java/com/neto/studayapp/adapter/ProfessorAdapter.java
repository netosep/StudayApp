package com.neto.studayapp.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.neto.studayapp.R;
import com.neto.studayapp.model.Disciplina;
import com.neto.studayapp.model.Favorito;
import com.neto.studayapp.model.Professor;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
        StringBuilder nomesDisciplina = new StringBuilder();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        String uuidAluno = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        for (int i = 0; i < disciplinas.size(); i++) {
            if (professores.get(position).getUuidProfessor().equals(disciplinas.get(i).getUuidProfessor())) {
                nomesDisciplina.append(disciplinas.get(i).getNome()).append(", ");
            }
        }
        if (nomesDisciplina.length() > 2) {
            int size = nomesDisciplina.length();
            nomesDisciplina = new StringBuilder(nomesDisciplina.substring(0, size - 2) + ".");
        }
        if (nomesDisciplina.toString().isEmpty()) {
            nomesDisciplina.append("Sem disciplinas...");
        }

        database.collection("favoritos")
                .whereEqualTo("professor.uuidProfessor", professor.getUuidProfessor())
                .whereEqualTo("uuidAluno", uuidAluno)
                .addSnapshotListener((value, error) -> {
                    if (value != null) {
                        if (value.isEmpty()) {
                            vhClass.buttonFavorito.setImageDrawable(
                                    ContextCompat.getDrawable(vhClass.buttonFavorito.getContext(), R.drawable.ic_favorite_border)
                            );
                        } else {
                            vhClass.buttonFavorito.setImageDrawable(
                                    ContextCompat.getDrawable(vhClass.buttonFavorito.getContext(), R.drawable.ic_favorite)
                            );
                        }
                    }
                });

        vhClass.nomeProfessor.setText(professor.getNomeCompleto());
//        vhClass.ratingBar.setRating(3.5F);
//        vhClass.ratingText.setText("(3,5)");
        vhClass.disciplina.setText(nomesDisciplina.toString());
        vhClass.descricao.setText(professor.getDescricao().isEmpty() ? "Sem descrição..." : professor.getDescricao());
        vhClass.sobre.setText(professor.getBiografia());
        vhClass.valor.setText("R$ " + df.format(professor.getValorAula()).replaceAll("[.]", ","));
        vhClass.professor = professor;
        Glide.with(vhClass.context).load(professor.getUrlFotoPerfil()).into(vhClass.imageProfessor);
    }

    @Override
    public int getItemCount() {
        return professores.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filtrarDalista(List<Professor> professorFilterList) {
        professores = professorFilterList;
        notifyDataSetChanged();
    }

    public static class ViewHolderClass extends RecyclerView.ViewHolder {

        TextView nomeProfessor, disciplina, descricao, sobre, valor, ratingText;
        RatingBar ratingBar;
        LinearLayout buttonWhatsapp;
        ImageButton buttonFavorito, buttonAvaliar;
        Professor professor;
        ImageFilterView imageProfessor;
        Context context;

        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            iniciarComponentes(itemView);

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            context = itemView.getContext();

            buttonWhatsapp.setOnClickListener(view -> {
                String mensagem = "Olá " + professor.getNomeCompleto() + "!, te encontrei pelo app Studay e tenho interesse nas suas aulas!";
                openWhatsApp(view.getContext(), professor.getWhatsapp(), mensagem);
            });

            buttonFavorito.setOnClickListener(view -> {
                Context context = view.getContext();
                String uuidFavorito = UUID.randomUUID().toString();
                String uuidAluno = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                Query query = database.collection("favoritos")
                        .whereEqualTo("uuidProfessor", professor.getUuidProfessor())
                        .whereEqualTo("uuidAluno", uuidAluno);
                query.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        if (task.getResult().isEmpty()) {
                            Favorito favorito = new Favorito(uuidFavorito, uuidAluno, professor.getUuidProfessor(), professor);
                            database.collection("favoritos").document(uuidFavorito).set(favorito);
                            Toast.makeText(context, "Adicionado as favoritos!", Toast.LENGTH_SHORT).show();
                            buttonFavorito.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite));
                        } else {
                            String documentId = task.getResult().getDocuments().get(0).getId();
                            database.collection("favoritos").document(documentId).delete();
                            Toast.makeText(context, "Removido dos favoritos!", Toast.LENGTH_SHORT).show();
                            buttonFavorito.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_border));
                        }
                    }
                }).addOnFailureListener(err -> {
                    Toast.makeText(context, err.getMessage(), Toast.LENGTH_SHORT).show();
                });

            });

//            buttonAvaliar.setOnClickListener(view -> {
//                Toast.makeText(view.getContext(), "Avaliou " + professor.getNomeCompleto(), Toast.LENGTH_SHORT).show();
//            });

        }

        private void iniciarComponentes(View itemView) {
            nomeProfessor = itemView.findViewById(R.id.textViewNomeProfessor);
            disciplina = itemView.findViewById(R.id.textViewDisciplina);
//            ratingBar = itemView.findViewById(R.id.ratingProfessor);
//            ratingText = itemView.findViewById(R.id.textViewRatingProfessor);
            descricao = itemView.findViewById(R.id.textViewDescricao);
            sobre = itemView.findViewById(R.id.textViewSobre);
            valor = itemView.findViewById(R.id.textViewValor);
            buttonWhatsapp = itemView.findViewById(R.id.buttonWhatsapp);
//            buttonAvaliar = itemView.findViewById(R.id.buttonAvaliar);
            buttonFavorito = itemView.findViewById(R.id.buttonFavorito);
            imageProfessor = itemView.findViewById(R.id.imgViewProfessor);
        }

        private void openWhatsApp(Context context, String numero, String mensagem) {
            try {
                PackageManager packageManager = context.getPackageManager();
                packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                String url = "https://api.whatsapp.com/send?phone=+55" + numero + "&text=" + URLEncoder.encode(mensagem, "UTF-8");
                intent.setData(Uri.parse(url));
                context.startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(context, "Necessário ter o aplicativo WhatsApp!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }

    }
}
