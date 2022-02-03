package com.neto.studayapp.activity.professor;

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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.neto.studayapp.R;
import com.neto.studayapp.activity.form.FormLogin;
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
    private ImageView btnEditImage;
    private ImageFilterView imagePreview, imgPreviewMenu;
    private AppCompatButton buttonEditPerfil, buttonEditSenha, buttonAtualizarSenha, buttonDeleteConta;
    private TextView nomeUsuario, nomeCompleto, email, whatsapp, dataNasc, valor, sexo, descricao, biografia;
    private TextView alertSenhaAtual, alertNovaSenha, alertConfNovaSenha;
    private EditText senhaAtual, novaSenha, confNovaSenha;
    private Uri caminhoImg;
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
            if (listVerPerfil.getVisibility() == View.VISIBLE) {
                listVerPerfil.setVisibility(View.GONE);
                listAlterarSenha.setVisibility(View.VISIBLE);
                buttonEditSenha.setText("Voltar");

                buttonAtualizarSenha.setOnClickListener(newView -> {
                    Toast.makeText(this, "Atualizar senha", Toast.LENGTH_SHORT).show();


                });

            } else {
                listAlterarSenha.setVisibility(View.GONE);
                listVerPerfil.setVisibility(View.VISIBLE);
                buttonEditSenha.setText("Editar senha");
            }
        });

        // add imagem
        ActivityResultLauncher<Intent> intentLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        Uri imgSelecionada = result.getData().getData();
                        caminhoImg = imgSelecionada;

                        if (caminhoImg != null) {
                            FirebaseFirestore database = FirebaseFirestore.getInstance();
                            String uuidProfessor = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                            DocumentReference documentRef = database.collection("professores").document(uuidProfessor);
                            System.out.println(documentRef.getId());
                            StorageReference storageRef = FirebaseStorage.getInstance().getReference("imagens/" + documentRef.getId());

                            storageRef.putFile(caminhoImg).continueWithTask(task -> {
                                if (!task.isSuccessful()) {
                                    throw Objects.requireNonNull(task.getException());
                                }
                                return storageRef.getDownloadUrl();
                            }).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    documentRef.update("urlFotoPerfil", Objects.requireNonNull(downloadUri).toString());
                                    Glide.with(MinhaContaProfessor.this).load(downloadUri).into(imgPreviewMenu);
                                }
                            }).addOnFailureListener(this, err -> {
                                Toast.makeText(this, "Ocorreu um erro!", Toast.LENGTH_SHORT).show();
                                Log.e("FirebaseDatabase", err.getMessage());
                            });
                        }

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

        buttonDeleteConta.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Você tem certeza disso?");
            builder.setMessage("Após excluir a sua conta você não terá mais " +
                    "acesso a plataforma com a mesma e todos os seus dados serão " +
                    "removidos do banco de dados. Ainda quer prosseguir com a ação?");
            builder.setPositiveButton("Sim", (dialogInterface, i) -> {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                if (user != null) {
                    String uuidProfessor = user.getUid();
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, FormLogin.class));
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    finish();

                    DocumentReference profRef = database.collection("professores").document(uuidProfessor);
//                    Query discplinaQuery = database.collection("disciplinas").whereEqualTo("uuidProfessor", uuidProfessor);
//                    discplinaQuery.addSnapshotListener((value, error) -> {
//                        if(value != null) {
//                            for (DocumentChange disciplina : value.getDocumentChanges()) {
//                                String uuidDiscplina = disciplina.getDocument().getReference().getId();
//                                database.collection("disciplinas").document(uuidDiscplina).delete();
//                            }
//                        }
//                    });
                    profRef.delete();
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
        String uuidProfessor = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("professores").document(uuidProfessor);
        documentReference.addSnapshotListener((documentSnapshot, error) -> {
            if (documentSnapshot != null) {
                try {
                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        setCardProfessor(documentSnapshot);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String nome = documentSnapshot.getString("nomeCompleto");
                String urlImg = documentSnapshot.getString("urlFotoPerfil");
                if (nome != null) {
                    String text = "Olá " + nome.split("[ ]")[0] + "!";
                    nomeUsuario.setText(text);
                }
                if (urlImg != null) {
                    Glide.with(getApplicationContext()).load(urlImg).into(imgPreviewMenu);
                }
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
        buttonDeleteConta = findViewById(R.id.buttonDeleteConta);
        listVerPerfil = findViewById(R.id.listPerfil);
        btnEditImage = findViewById(R.id.imgEditImage);
        imagePreview = findViewById(R.id.imageUser);
        listAlterarSenha = findViewById(R.id.listAlterarSenha);
        senhaAtual = findViewById(R.id.editTextSenhaAtual);
        novaSenha = findViewById(R.id.editTextNovaSenha);
        confNovaSenha = findViewById(R.id.editTextConfNovaSenha);
        alertSenhaAtual = findViewById(R.id.textViewSenhaAtualAlert);
        alertNovaSenha = findViewById(R.id.textViewNovaSenhaAlert);
        alertConfNovaSenha = findViewById(R.id.textViewConfSenhaAlert);
        buttonAtualizarSenha = findViewById(R.id.btnAtualizarSenha);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toobarMenu);
        View header = navigationView.getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);
        imgPreviewMenu = header.findViewById(R.id.imgPreviewMenu);
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
        professor.setUrlFotoPerfil(ds.getString("urlFotoPerfil"));
        // carregando dados na view
        nomeCompleto.setText(professor.getNomeCompleto());
        email.setText(professor.getEmail());
        whatsapp.setText(professor.getWhatsapp() == null ? "--" : Mask.maskCelular(professor.getWhatsapp()));
        dataNasc.setText(professor.getDataNascimento() == null ? "--" : sdf.format(professor.getDataNascimento()));
        valor.setText("R$ " + df.format(professor.getValorAula()).replaceAll("[.]", ","));
        sexo.setText(professor.getSexo());
        descricao.setText(professor.getDescricao().isEmpty() ? "--" : professor.getDescricao());
        biografia.setText(professor.getBiografia());
        Glide.with(getApplicationContext()).load(professor.getUrlFotoPerfil()).into(imagePreview);
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