package com.neto.studayapp.activity.professor;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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
import com.neto.studayapp.activity.misc.Loading;
import com.neto.studayapp.model.Professor;
import com.neto.studayapp.util.Mask;
import com.neto.studayapp.util.Validator;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class EditarPerfilProfessor extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    private EditText nomeSobrenome, email, whatsapp, dataNascimento;
    private EditText valor, descricao, biografia, senha;
    private Button buttonUpdate;
    private Spinner sexo;
    private TextView nomeUsuario, alertNome, alertEmail, alertWhatsapp, alertDataNasc;
    private TextView alertSexo, alertValor, alertDescricao, alertBiografia, alertSenha;
    private Professor professor = new Professor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_professor);
        iniciarComponentes();

        // mascaras para o EditText de telefone e data
        whatsapp.addTextChangedListener(Mask.insert("(##) #####-####", whatsapp));
        dataNascimento.addTextChangedListener(Mask.insert("##/##/####", dataNascimento));

        if (getIntent().hasExtra("professor")) {
            professor = (Professor) getIntent().getSerializableExtra("professor");
            setarValores();
        }

        buttonUpdate.setOnClickListener(view -> {
            if (formularioIsValid()) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                DocumentReference documentReference = database.collection("professores").document(professor.getUuidProfessor());
                try {
                    atualizarProfessor(documentReference);
                } catch (Exception err) {
                    err.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Verifique possíveis campos com erro!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        String uuidProfessor = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        DocumentReference documentReference = database.collection("professores").document(uuidProfessor);
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
        startActivity(new Intent(this, MinhaContaProfessor.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.minhaContaId:
                startActivity(new Intent(this, MinhaContaProfessor.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
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

    private boolean formularioIsValid() {
        boolean nomeValido = false, emailValido = false, whatsappValido = false, dataValido = false, senhaValida = false;
        boolean sexoValido = false, valorValido = false, descricaoValido = false, biografiaValido = false;
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

        if (validator.whastappIsValid(whatsapp, true)) {
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

        if (validator.selectIsValid(sexo)) {
            alertSexo.setVisibility(View.INVISIBLE);
            sexoValido = true;
        } else {
            alertSexo.setVisibility(View.VISIBLE);
            alertSexo.setText(validator.getMsgSelect());
        }

        if (validator.valorIsValid(valor)) {
            alertValor.setVisibility(View.INVISIBLE);
            valorValido = true;
        } else {
            alertValor.setVisibility(View.VISIBLE);
            alertValor.setText(validator.getMsgValor());
        }

        if (validator.descricaoIsValid(descricao)) {
            alertDescricao.setVisibility(View.INVISIBLE);
            descricaoValido = true;
        } else {
            alertDescricao.setVisibility(View.VISIBLE);
            alertDescricao.setText(validator.getMsgDescricao());
        }

        if (validator.biografiaIsValid(biografia)) {
            alertBiografia.setVisibility(View.INVISIBLE);
            biografiaValido = true;
        } else {
            alertBiografia.setVisibility(View.VISIBLE);
            alertBiografia.setText(validator.getMsgBiografia());
        }

        if (validator.senhaIsValid(senha)) {
            alertSenha.setVisibility(View.INVISIBLE);
            senhaValida = true;
        } else {
            alertSenha.setVisibility(View.VISIBLE);
            alertSenha.setText(validator.getMsgSenha());
        }

        return nomeValido && emailValido && sexoValido && dataValido && senhaValida &&
                valorValido && whatsappValido && descricaoValido && biografiaValido;
    }

    private void iniciarComponentes() {
        // componentes para o menu
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toobarMenu);
        View header = navigationView.getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);
        // editText e spinner
        nomeSobrenome = findViewById(R.id.editTextNomeSobrenome);
        email = findViewById(R.id.editTextEmail);
        whatsapp = findViewById(R.id.editTextWhatsapp);
        dataNascimento = findViewById(R.id.editTextDataNasc);
        sexo = findViewById(R.id.spinnerSexo);
        valor = findViewById(R.id.editTextValor);
        descricao = findViewById(R.id.editTextDescricao);
        biografia = findViewById(R.id.editTextBiografia);
        senha = findViewById(R.id.editTextSenha);
        // textView
        alertNome = findViewById(R.id.textViewNomeSobrenomeAlert);
        alertEmail = findViewById(R.id.textViewEmailAlert);
        alertWhatsapp = findViewById(R.id.textViewWhatsappAlert);
        alertDataNasc = findViewById(R.id.textViewDataNascAlert);
        alertSexo = findViewById(R.id.textViewSexoAlert);
        alertValor = findViewById(R.id.textViewValorAlert);
        alertDescricao = findViewById(R.id.textViewDescricaoAlert);
        alertBiografia = findViewById(R.id.textViewBiografiaAlert);
        alertSenha = findViewById(R.id.textViewUpdSenhaAlert);
        // button
        buttonUpdate = findViewById(R.id.buttonUpdateCadastro);
        setSpinner();
        iniciarMenu();
    }

    private void setSpinner() {
        String[] valores = getResources().getStringArray(R.array.sexo);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(adapter);
    }

    private void iniciarMenu() {
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressLint("SimpleDateFormat")
    private void setarValores() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        DecimalFormat df = new DecimalFormat("#00.00");
        int indiceSexo = 0;
        switch (professor.getSexo()) {
            case "Masculino": indiceSexo = 1; break;
            case "Feminino": indiceSexo = 2; break;
            case "Outros": indiceSexo = 3; break;
        }
        nomeSobrenome.setText(professor.getNomeCompleto());
        email.setText(professor.getEmail());
        whatsapp.setText(professor.getWhatsapp());
        dataNascimento.setText(professor.getDataNascimento() == null ? "" : sdf.format(professor.getDataNascimento()));
        sexo.setSelection(indiceSexo);
        valor.setText(df.format(professor.getValorAula()));
        descricao.setText(professor.getDescricao());
        biografia.setText(professor.getBiografia());
    }

    @SuppressLint("SimpleDateFormat")
    private void atualizarProfessor(DocumentReference documentRef) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Professor attProfessor = new Professor();
        // criando um novo objeto Professor para atualizar
        attProfessor.setUuidProfessor(professor.getUuidProfessor());
        attProfessor.setNomeCompleto(nomeSobrenome.getText().toString());
        attProfessor.setEmail(email.getText().toString());
        attProfessor.setWhatsapp(Mask.unmask(whatsapp.getText().toString()));
        attProfessor.setDataNascimento(dataNascimento.getText().toString().isEmpty() ? null : sdf.parse(dataNascimento.getText().toString()));
        attProfessor.setSexo(sexo.getSelectedItem().toString());
        attProfessor.setValorAula(Double.parseDouble(valor.getText().toString().replaceAll("[,]", ".")));
        attProfessor.setDescricao(descricao.getText().toString());
        attProfessor.setBiografia(biografia.getText().toString());
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
                user.updateEmail(attProfessor.getEmail()).addOnCompleteListener(updateTask -> {
                    if (updateTask.isSuccessful()) {
                        // atualizando a collection Professor
                        documentRef.set(attProfessor);
                        // voltando para a tela Minha conta
                        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(this, MinhaContaProfessor.class));
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
        startActivity(new Intent(this, MinhaContaProfessor.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void deslogar() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, Loading.class);
        intent.putExtra("mensagem", "Saindo...");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}