package com.neto.studayapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.model.Disciplina;

import java.util.List;
import java.util.Objects;

public class DisciplinaAdapter extends RecyclerView.Adapter<ViewHolderClass> {

    List<Disciplina> disciplinas;

    public DisciplinaAdapter(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    @NonNull
    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disciplina, parent, false);
        return new ViewHolderClass(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClass holder, int position) {
        Disciplina disciplina = disciplinas.get(position);
        holder.nomeDisciplina.setText(disciplina.getNome());
        holder.disciplina = disciplina;
    }

    @Override
    public int getItemCount() {
        return disciplinas.size();
    }

}

class ViewHolderClass extends RecyclerView.ViewHolder {

    Disciplina disciplina;
    TextView nomeDisciplina;
    DisciplinaAdapter adapter;
    ImageButton buttonVerDisciplina, buttonEditarDisciplina, buttonApagarDisciplina;
    FirebaseFirestore database;

    public ViewHolderClass(@NonNull View itemView) {
        super(itemView);
        iniciarComponentes(itemView);
        database = FirebaseFirestore.getInstance();

        buttonVerDisciplina.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Ver disciplina: " + disciplina.getNome(), Toast.LENGTH_SHORT).show();
        });

        buttonEditarDisciplina.setOnClickListener(view -> {
            Toast.makeText(view.getContext(), "Editar disciplina: " + disciplina.getNome(), Toast.LENGTH_SHORT).show();
        });

        buttonApagarDisciplina.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage("Deseja excluir a disciplina " + disciplina.getNome() + "?");
            builder.setPositiveButton("Sim", (dialogInterface, i) -> {
                DocumentReference df = database.collection("disciplinas").document(disciplina.getUuid());
                df.delete().addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        Toast.makeText(view.getContext(), "Disciplina excluida com sucesso!", Toast.LENGTH_SHORT).show();
                        // removendo item da lista
                        adapter.disciplinas.remove(getAbsoluteAdapterPosition());
                        adapter.notifyItemRemoved(getAbsoluteAdapterPosition());
                    } else {
                        try {
                            throw Objects.requireNonNull(task.getException());
                        } catch (Exception err) {
                            //Log.d("teste", err.getMessage());
                            Toast.makeText(view.getContext(), err.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }).setNegativeButton("Não", null);
            AlertDialog alert = builder.create();
            alert.show();
        });

    }

    public ViewHolderClass linkAdapter(DisciplinaAdapter adapter) {
        this.adapter = adapter;
        return this;
    }

    private void iniciarComponentes(View itemView) {
        nomeDisciplina = itemView.findViewById(R.id.nomeDisciplina);
        buttonVerDisciplina = itemView.findViewById(R.id.buttonVerDisciplina);
        buttonEditarDisciplina = itemView.findViewById(R.id.buttonEditarDisciplina);
        buttonApagarDisciplina = itemView.findViewById(R.id.buttonDeleteDsiciplina);
    }

}
