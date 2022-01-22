package com.neto.studayapp.activity.professor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.activity.misc.Loading;
import com.neto.studayapp.model.Professor;
import com.neto.studayapp.util.Mask;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class MinhaContaProfessor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private LinearLayout listVerPerfil, listAtualizarPerfil, listAlterarSenha;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private AppCompatButton buttonEditPerfil, buttonEditSenha;
    private TextView nomeUsuario, nomeCompleto, email, whatsapp, dataNasc, valor, sexo, descricao, biografia;
    private final Professor professor = new Professor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta_professor);
        iniciarComponentes();

        buttonEditPerfil.setOnClickListener(view -> {
            Intent intent = new Intent(this, EditarPerfilProfessor.class);
            intent.putExtra("professor", professor);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        buttonEditSenha.setOnClickListener(view -> {
            if(listVerPerfil.getVisibility() == View.VISIBLE) {
                listVerPerfil.setVisibility(View.GONE);
                listAlterarSenha.setVisibility(View.VISIBLE);
                buttonEditSenha.setText("Voltar");
            } else {
                listAlterarSenha.setVisibility(View.GONE);
                listVerPerfil.setVisibility(View.VISIBLE);
                buttonEditSenha.setText("Editar senha");
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onStart() {
        super.onStart();
        String uuidProfessor = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("professores").document(uuidProfessor);
        documentReference.addSnapshotListener((documentSnapshot, error) -> {
            if (documentSnapshot != null) {
                try {
                    setCardProfessor(documentSnapshot);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String nome = documentSnapshot.getString("nomeCompleto");
                String text = "Olá " + Objects.requireNonNull(nome).split("[ ]")[0] + "!";
                nomeUsuario.setText(text);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            startActivity(new Intent(MinhaContaProfessor.this, Disciplinas.class));
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.minhaContaId:
                drawerLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.disciplinasId:
                startActivity(new Intent(this, Disciplinas.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                break;
            case R.id.avaliacoesId:
                Toast.makeText(this, "Avaliações", Toast.LENGTH_SHORT).show();
                break;
            case R.id.infoId:
                Toast.makeText(this, "Sobre", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sairId:
                deslogar();
                break;
            default:
                break;
        }
        return true;
    }

    private void iniciarComponentes() {
        nomeCompleto = findViewById(R.id.textViewNomeSobrenome);
        email = findViewById(R.id.textViewEmail);
        whatsapp = findViewById(R.id.textViewWhatsapp);
        dataNasc = findViewById(R.id.textViewDataNasc);
        descricao = findViewById(R.id.textViewDescricao);
        biografia = findViewById(R.id.textViewBiografia);
        valor = findViewById(R.id.textViewValorAula);
        sexo = findViewById(R.id.textViewSexo);
        buttonEditPerfil = findViewById(R.id.buttonEditPerfil);
        buttonEditSenha = findViewById(R.id.buttonEditSenha);
        listVerPerfil = findViewById(R.id.listPerfil);
        //listAtualizarPerfil = findViewById(R.id.);
        listAlterarSenha = findViewById(R.id.listAlterarSenha);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toobarMenu);
        View header = navigationView.getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);
        iniciarMenu();
    }

    private void iniciarMenu() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint({"SimpleDateFormat", "SetTextI18n"})
    private void setCardProfessor(DocumentSnapshot ds) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#00.00");
        // populando o objeto professor
        professor.setUuidProfessor(ds.getString("uuidProfessor"));
        professor.setNomeCompleto(ds.getString("nomeCompleto"));
        professor.setEmail(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        professor.setWhatsapp(ds.getString("whatsapp"));
        professor.setDataNascimento(ds.getDate("dataNascimento"));
        professor.setValorAula(ds.getDouble("valorAula"));
        professor.setSexo(ds.getString("sexo"));
        professor.setDescricao(ds.getString("descricao"));
        professor.setBiografia(ds.getString("biografia"));
        // carregando dados na view
        nomeCompleto.setText(professor.getNomeCompleto());
        email.setText(professor.getEmail());
        whatsapp.setText(professor.getWhatsapp() == null ? "--" : Mask.maskCelular(professor.getWhatsapp()));
        dataNasc.setText(professor.getDataNascimento() == null ? "--" : sdf.format(professor.getDataNascimento()));
        valor.setText("R$ " + df.format(professor.getValorAula()).replaceAll("[.]", ","));
        sexo.setText(professor.getSexo());
        descricao.setText(professor.getDescricao().isEmpty() ? "--" : professor.getDescricao());
        biografia.setText(professor.getBiografia());
    }

    public void voltar(View view) {
        startActivity(new Intent(this, Disciplinas.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void deslogar() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MinhaContaProfessor.this, Loading.class);
        intent.putExtra("mensagem", "Saindo...");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}