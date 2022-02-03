package com.neto.studayapp.activity.aluno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.activity.form.FormCadastroAluno;
import com.neto.studayapp.activity.misc.EscolhaCadastro;
import com.neto.studayapp.activity.misc.Loading;
import com.neto.studayapp.activity.professor.MinhaContaProfessor;
import com.neto.studayapp.model.Aluno;
import com.neto.studayapp.model.Professor;
import com.neto.studayapp.util.Mask;
import com.neto.studayapp.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class EditarPerfilAluno extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView nomeUsuario;
    private EditText nomeSobrenome, email, whatsapp, dataNascimento, senha;
    private TextView alertNome, alertEmail, alertWhatsapp, alertDataNasc, alertSenha;
    private Button buttonUpdate;
    private Aluno aluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_aluno);
        iniciarComponentes();

        // mascaras para o EditText de telefone e data
        whatsapp.addTextChangedListener(Mask.insert("(##) #####-####", whatsapp));
        dataNascimento.addTextChangedListener(Mask.insert("##/##/####", dataNascimento));

        if (getIntent().hasExtra("aluno")) {
            aluno = (Aluno) getIntent().getSerializableExtra("aluno");
            setarValores();
        }

        buttonUpdate.setOnClickListener(view -> {
            if (formularioIsValid()) {
                try {
                    FirebaseFirestore database = FirebaseFirestore.getInstance();
                    DocumentReference documentReference = database.collection("alunos").document(aluno.getUuidAluno());
                    atualizarAluno(documentReference);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Verifique possíveis campos com erro!", Toast.LENGTH_SHORT).show();
            }
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
                String nome = documentSnapshot.getString("nomeCompleto");
                String text = "Olá " + Objects.requireNonNull(nome).split("[ ]")[0] + "!";
                nomeUsuario.setText(text);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(EditarPerfilAluno.this, MinhaContaAluno.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.minhaContaId:
                startActivity(new Intent(EditarPerfilAluno.this, MinhaContaAluno.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
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

    private void iniciarComponentes() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toobarMenu);
        View header = navigationView.getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);
        nomeSobrenome = findViewById(R.id.editTextNomeSobrenome);
        email = findViewById(R.id.editTextEmail);
        whatsapp = findViewById(R.id.editTextWhatsapp);
        dataNascimento = findViewById(R.id.editTextDataNasc);
        senha = findViewById(R.id.editTextSenha);
        buttonUpdate = findViewById(R.id.buttonSubmitCadastro);
        alertNome = findViewById(R.id.textViewNomeSobrenomeAlert);
        alertEmail = findViewById(R.id.textViewEmailAlert);
        alertWhatsapp = findViewById(R.id.textViewWhatsappAlert);
        alertDataNasc = findViewById(R.id.textViewDataNascAlert);
        alertSenha = findViewById(R.id.textViewSenhaAlert);
        iniciarMenu();
    }

    private void iniciarMenu() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(EditarPerfilAluno.this);
    }

    private boolean formularioIsValid() {

        boolean nomeValido = false, emailValido = false, whatsappValido = false;
        boolean dataValido = false, senhaValido = false;
        Validator validator = new Validator();

        if (validator.nomeIsValid(nomeSobrenome)) {
            alertNome.setVisibility(View.INVISIBLE);
            nomeValido = true;
        } else {
            alertNome.setVisibility(View.VISIBLE);
            alertNome.setText(validator.getMsgNome());
        }

        if (validator.emailIsValid(email)) {
            alertEmail.setVisibility(View.INVISIBLE);
            emailValido = true;
        } else {
            alertEmail.setVisibility(View.VISIBLE);
            alertEmail.setText(validator.getMsgEmail());
        }

        if (validator.whastappIsValid(whatsapp, false)) {
            alertWhatsapp.setVisibility(View.INVISIBLE);
            whatsappValido = true;
        } else {
            alertWhatsapp.setVisibility(View.VISIBLE);
            alertWhatsapp.setText(validator.getMsgWhatsApp());
        }

        if (validator.dataIsValid(dataNascimento)) {
            alertDataNasc.setVisibility(View.INVISIBLE);
            dataValido = true;
        } else {
            alertDataNasc.setVisibility(View.VISIBLE);
            alertDataNasc.setText(validator.getMsgData());
        }

        if (validator.senhaIsValid(senha)) {
            alertSenha.setVisibility(View.INVISIBLE);
            senhaValido = true;
        } else {
            alertSenha.setVisibility(View.VISIBLE);
            alertSenha.setText(validator.getMsgSenha());
        }

        return nomeValido && emailValido && whatsappValido && dataValido && senhaValido;
    }

    @SuppressLint("SimpleDateFormat")
    private void setarValores() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        nomeSobrenome.setText(aluno.getNomeCompleto());
        email.setText(aluno.getEmail());
        whatsapp.setText(aluno.getWhatsapp());
        dataNascimento.setText(aluno.getDataNascimento() == null ? "" : sdf.format(aluno.getDataNascimento()));
    }

    @SuppressLint("SimpleDateFormat")
    private void atualizarAluno(DocumentReference documentRef) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Aluno attAluno = new Aluno();
        // criando um novo objeto Aluno para atualizar
        attAluno.setUuidAluno(aluno.getUuidAluno());
        attAluno.setNomeCompleto(nomeSobrenome.getText().toString());
        attAluno.setEmail(email.getText().toString());
        attAluno.setWhatsapp(whatsapp.getText().toString().isEmpty() ? null : Mask.unmask(whatsapp.getText().toString()));
        attAluno.setDataNascimento(dataNascimento.getText().toString().isEmpty() ? null : sdf.parse(dataNascimento.getText().toString()));

        // pegando o usuário atual e reautenticando para a alteração dos dados
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credentials = EmailAuthProvider.getCredential(
                Objects.requireNonNull(Objects.requireNonNull(user).getEmail()),
                senha.getText().toString()
        );

        buttonUpdate.setEnabled(false);
        user.reauthenticate(credentials).addOnCompleteListener(authTask -> {
            if (authTask.isSuccessful()) {
                alertSenha.setVisibility(View.INVISIBLE);
                // atualizando o userAuth
                user.updateEmail(attAluno.getEmail()).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        // atualizando a collection Aluno
                        documentRef.set(attAluno);
                        // voltando para a tela Minha conta
                        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MinhaContaAluno.class));
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                    } else {
                        buttonUpdate.setEnabled(true);
                        String msgErro;
                        try {
                            throw Objects.requireNonNull(updateTask.getException());
                        } catch (FirebaseAuthInvalidCredentialsException err) {
                            alertEmail.setVisibility(View.VISIBLE);
                            alertEmail.setText(R.string.erro_email_inexistente);
                            msgErro = "E-mail inexistente!";
                        } catch (FirebaseAuthUserCollisionException err) {
                            alertEmail.setVisibility(View.VISIBLE);
                            alertEmail.setText(R.string.erro_email_em_uso);
                            msgErro = "Este e-mail já está em uso!";
                        } catch (FirebaseNetworkException err) {
                            msgErro = "Conexão com a internet indisponivel!";
                        } catch (Exception err) {
                            err.printStackTrace();
                            msgErro = "Ocorreu um erro ao realizar o cadastro!";
                        }
                        Toast.makeText(getApplicationContext(), msgErro, Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                buttonUpdate.setEnabled(true);
                try {
                    throw Objects.requireNonNull(authTask.getException());
                } catch (FirebaseAuthInvalidCredentialsException | FirebaseAuthInvalidUserException err) {
                    Toast.makeText(this, "Senha incorreta!", Toast.LENGTH_SHORT).show();
                    alertSenha.setVisibility(View.VISIBLE);
                    alertSenha.setText(R.string.senha_incorreta);
                } catch (FirebaseNetworkException err) {
                    Toast.makeText(getApplicationContext(), "Sem conexão com a internet!", Toast.LENGTH_SHORT).show();
                } catch (Exception err) {
                    Toast.makeText(getApplicationContext(), err.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void voltar(View view) {
        startActivity(new Intent(EditarPerfilAluno.this, MinhaContaAluno.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void deslogar() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(EditarPerfilAluno.this, Loading.class);
        intent.putExtra("mensagem", "Saindo...");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}