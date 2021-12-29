package com.neto.studayapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.neto.studayapp.R;
import com.neto.studayapp.model.Disciplina;

import java.util.List;

public class DisciplinaAdapter extends RecyclerView.Adapter {

    List<Disciplina> disciplinas;

    public DisciplinaAdapter(List<Disciplina> disciplinas) {
        this.disciplinas = disciplinas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disciplina, parent, false);
        return new ViewHolderClass(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolderClass vhClass = (ViewHolderClass) holder;
        Disciplina disciplina = disciplinas.get(position);
        vhClass.disciplina.setText(disciplina.getNome());
    }

    @Override
    public int getItemCount() {
        return disciplinas.size();
    }

    public static class ViewHolderClass extends RecyclerView.ViewHolder {
        TextView disciplina;
        public ViewHolderClass(@NonNull View itemView) {
            super(itemView);
            disciplina = itemView.findViewById(R.id.nomeDisciplina);
        }
    }
}
