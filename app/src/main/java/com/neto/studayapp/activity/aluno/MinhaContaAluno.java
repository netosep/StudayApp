package com.neto.studayapp.activity.aluno;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.activity.form.FormLogin;
import com.neto.studayapp.activity.misc.Loading;
import com.neto.studayapp.activity.professor.EditarPerfilProfessor;
import com.neto.studayapp.model.Aluno;
import com.neto.studayapp.util.Mask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class MinhaContaAluno extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView nomeUsuario, nomeCompleto, email, whatsapp, dataNasc;
    private ImageView btnEditImage;
    private ImageFilterView imagePreview;
    private AppCompatButton btnEditPerfil, btnEditSenha, btnDeleteConta;
    private final Aluno aluno = new Aluno();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minha_conta_aluno);
        iniciarComponentes();

        // add imagem
        ActivityResultLauncher<Intent> intentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imgSelecionada = result.getData().getData();
                        //caminhoImg = imgSelecionada;
                        try {
                            // setando imagem de preview
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgSelecionada);
                            imagePreview.setImageBitmap(bitmap);
                        } catch (Exception err) {
                            err.printStackTrace();
                        }
                    }
                }
        );

        btnEditImage.setOnClickListener(view -> {
            Intent pickImage = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intentLauncher.launch(pickImage);
        });

        btnEditPerfil.setOnClickListener(view -> {
            //Toast.makeText(this, "edit", Toast.LENGTH_SHORT).show();
            //System.out.println(aluno.toString());
            Intent intent = new Intent(this, EditarPerfilAluno.class);
            intent.putExtra("aluno", aluno);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            finish();
        });

        btnEditSenha.setOnClickListener(view -> {
            Toast.makeText(this, "senha", Toast.LENGTH_SHORT).show();
        });

        btnDeleteConta.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Você tem certeza disso?");
            builder.setMessage("Após excluir a sua conta você não terá mais " +
                    "acesso a plataforma com a mesma e todos os seus dados serão " +
                    "removidos do banco de dados. Ainda quer prosseguir com a ação?");
            builder.setPositiveButton("Sim", (dialogInterface, i) -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                if (user != null) {
                    String uuidAluno = user.getUid();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, FormLogin.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();

                    DocumentReference alunoRef = database.collection("professores").document(uuidAluno);
//                    Query discplinaQuery = database.collection("disciplinas").whereEqualTo("uuidProfessor", uuidProfessor);
//                    discplinaQuery.addSnapshotListener((value, error) -> {
//                        if(value != null) {
//                            for (DocumentChange disciplina : value.getDocumentChanges()) {
//                                String uuidDiscplina = disciplina.getDocument().getReference().getId();
//                                database.collection("disciplinas").document(uuidDiscplina).delete();
//                            }
//                        }
//                    });
                    alunoRef.delete();
                    user.delete();
                    Toast.makeText(getApplicationContext(), "Sua conta foi excluida com sucesso!", Toast.LENGTH_SHORT).show();

                }
            });
            builder.setNegativeButton("Não", null);
            AlertDialog alert = builder.create();
            alert.show();
        });

    }

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onStart() {
        super.onStart();
        String uuidAluno = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("alunos").document(uuidAluno);
        documentReference.addSnapshotListener((documentSnapshot, error) -> {
            if (documentSnapshot != null) {
                try {
                    setCardAluno(documentSnapshot);
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
            startActivity(new Intent(MinhaContaAluno.this, Professores.class));
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
            case R.id.favoritosId:
                startActivity(new Intent(this, Favoritos.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            case R.id.professoresId:
                startActivity(new Intent(this, Professores.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                break;
//            case R.id.avaliacoesId:
//                Toast.makeText(this, "Avaliações", Toast.LENGTH_SHORT).show();
//                break;
            case R.id.infoId:
                Toast.makeText(this, "App Studay | Versão: 1.0", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sairId:
                deslogar();
                break;
            default:
                break;
        }
        return true;
    }

    @SuppressLint("SimpleDateFormat")
    private void setCardAluno(DocumentSnapshot ds) throws ParseException {
        String emailString = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // populando o objeto aluno
        aluno.setUuidAluno(FirebaseAuth.getInstance().getCurrentUser().getUid());
        aluno.setNomeCompleto(ds.getString("nomeCompleto"));
        aluno.setEmail(emailString);
        aluno.setWhatsapp(ds.getString("whatsapp"));
        aluno.setDataNascimento(ds.getDate("dataNascimento"));
        aluno.setNivelAcesso(1);
        // carredando dados na view
        nomeCompleto.setText(aluno.getNomeCompleto());
        email.setText(emailString);
        whatsapp.setText(aluno.getWhatsapp() == null ? "--" : Mask.maskCelular(aluno.getWhatsapp()));
        dataNasc.setText(aluno.getDataNascimento() == null ? "--" : sdf.format(aluno.getDataNascimento()));
    }

    public void iniciarComponentes() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toobarMenu);
        btnEditImage = findViewById(R.id.imgEditImage);
        imagePreview = findViewById(R.id.imageUser);
        View header = navigationView.getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);
        nomeCompleto = findViewById(R.id.textViewNomeSobrenome);
        email = findViewById(R.id.textViewEmail);
        whatsapp = findViewById(R.id.textViewWhatsapp);
        dataNasc = findViewById(R.id.textViewDataNasc);
        btnEditPerfil = findViewById(R.id.buttonEditPerfil);
        btnEditSenha = findViewById(R.id.buttonEditSenha);
        btnDeleteConta = findViewById(R.id.buttonDeleteConta);
        iniciarMenu();
    }

    private void iniciarMenu() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(MinhaContaAluno.this);
    }

    public void voltar(View view) {
        startActivity(new Intent(MinhaContaAluno.this, Professores.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void deslogar() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MinhaContaAluno.this, Loading.class);
        intent.putExtra("mensagem", "Saindo...");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}