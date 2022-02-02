package com.neto.studayapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.model.Favorito;
import com.neto.studayapp.model.Professor;

import java.net.URLEncoder;
import java.util.List;


public class FavoritosAdapter extends RecyclerView.Adapter<ViewHolderClassFavoritos> {

    List<Favorito> favoritos;

    public FavoritosAdapter(List<Favorito> favoritos) {
        this.favoritos = favoritos;
    }

    @NonNull
    @Override
    public ViewHolderClassFavoritos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorito, parent, false);
        return new ViewHolderClassFavoritos(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderClassFavoritos holder, int position) {
        Favorito favorito = favoritos.get(position);
        System.out.println(favorito.getProfessor().toString());
        Glide.with(holder.context).load(favorito.getProfessor().getUrlFotoPerfil()).into(holder.imgProfessor);
        holder.nomeProfessor.setText(favorito.getProfessor().getNomeCompleto());
        holder.professor = favorito.getProfessor();
        holder.favorito = favorito;
    }

    @Override
    public int getItemCount() {
        return favoritos.size();
    }
}

class ViewHolderClassFavoritos extends RecyclerView.ViewHolder {

    ImageFilterView imgProfessor;
    TextView nomeProfessor;
    ImageButton btnFavorito, btnWhatsapp;
    Professor professor;
    Favorito favorito;
    FavoritosAdapter adapter;
    Context context;

    public ViewHolderClassFavoritos(@NonNull View itemView) {
        super(itemView);
        iniciarComponentes(itemView);

        FirebaseFirestore database = FirebaseFirestore.getInstance();

        btnFavorito.setOnClickListener(view -> {
            Context context = view.getContext();
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Remover o professor dos favoritos?");
            builder.setPositiveButton("Sim", (dialogInterface, i) -> {
                DocumentReference df = database.collection("favoritos").document(favorito.getUuidFavorito());
                df.delete().addOnCompleteListener(task -> {
                    if (task.isComplete()) {
                        Toast.makeText(context, "Removido dos favoritos!", Toast.LENGTH_SHORT).show();
                        adapter.favoritos.remove(getAbsoluteAdapterPosition());
                        adapter.notifyItemRemoved(getAbsoluteAdapterPosition());
                    }
                });
            });
            builder.setNegativeButton("Não", null);
            AlertDialog alert = builder.create();
            alert.show();
        });

        btnWhatsapp.setOnClickListener(view -> {
            String mensagem = "Olá " + professor.getNomeCompleto() + "!, te encontrei pelo app Studay e tenho interesse nas suas aulas!";
            openWhatsApp(view.getContext(), professor.getWhatsapp(), mensagem);
        });

    }

    public ViewHolderClassFavoritos linkAdapter(FavoritosAdapter favoritosAdapter) {
        this.adapter = favoritosAdapter;
        return this;
    }

    private void iniciarComponentes(View itemView) {
        imgProfessor = itemView.findViewById(R.id.imgProfessor);
        nomeProfessor = itemView.findViewById(R.id.textViewNomeProfessor);
        btnFavorito = itemView.findViewById(R.id.btnFavorito);
        btnWhatsapp = itemView.findViewById(R.id.btnWhatsapp);
        context = itemView.getContext();
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
