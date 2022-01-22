package com.neto.studayapp.activity.form;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.activity.misc.EscolhaCadastro;
import com.neto.studayapp.model.Aluno;
import com.neto.studayapp.util.Mask;
import com.neto.studayapp.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class FormCadastroAluno extends AppCompatActivity {

    private EditText nomeSobrenome, email, whatsapp, dataNascimento, senha, confirmSenha;
    private TextView alertNome, alertEmail, alertWhatsapp, alertDataNasc, alertSenha, alertConfSenha;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_aluno);
        iniciarComponentes();

        // mascaras para o EditText de telefone e data
        whatsapp.addTextChangedListener(Mask.insert("(##) #####-####", whatsapp));
        dataNascimento.addTextChangedListener(Mask.insert("##/##/####", dataNascimento));



        buttonSubmit.setOnClickListener(view -> {
            if (formularioIsValid()) {
                try {
                    cadastrarAluno(view);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Verifique possíveis campos com erro!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FormCadastroAluno.this, EscolhaCadastro.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void voltar(View view) {
        startActivity(new Intent(FormCadastroAluno.this, EscolhaCadastro.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private void iniciarComponentes() {
        nomeSobrenome = findViewById(R.id.editTextNomeSobrenome);
        email = findViewById(R.id.editTextEmail);
        whatsapp = findViewById(R.id.editTextWhatsapp);
        dataNascimento = findViewById(R.id.editTextDataNasc);
        senha = findViewById(R.id.editTextSenha);
        confirmSenha = findViewById(R.id.editTextConfSenha);
        buttonSubmit = findViewById(R.id.buttonSubmitCadastro);
        alertNome = findViewById(R.id.textViewNomeSobrenomeAlert);
        alertEmail = findViewById(R.id.textViewEmailAlert);
        alertWhatsapp = findViewById(R.id.textViewWhatsappAlert);
        alertDataNasc = findViewById(R.id.textViewDataNascAlert);
        alertSenha = findViewById(R.id.textViewSenhaAlert);
        alertConfSenha = findViewById(R.id.textViewConfSenhaAlert);
    }

    private boolean formularioIsValid() {

        boolean nomeValido = false, emailValido = false, whatsappValido = false;
        boolean dataValido = false, senhaValido = false, confSenhaValido = false, formValido = false;
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

        if (validator.confSenhaIsValid(confirmSenha)) {
            alertConfSenha.setVisibility(View.INVISIBLE);
            confSenhaValido = true;
        } else {
            alertConfSenha.setVisibility(View.VISIBLE);
            alertConfSenha.setText(validator.getMsgConfSenha());
        }

        if (senhaValido && confSenhaValido) {
            if (validator.senhaEquals(senha, confirmSenha)) {
                alertConfSenha.setVisibility(View.INVISIBLE);
                formValido = true;
            } else {
                alertConfSenha.setVisibility(View.VISIBLE);
                alertConfSenha.setText(validator.getMsgConfSenha());
            }
        }

        return nomeValido && emailValido && whatsappValido && dataValido && formValido;
    }

    @SuppressLint("SimpleDateFormat")
    public void cadastrarAluno(View view) throws ParseException {
        // criando um objeto aluno para ser adc ao banco
        Aluno aluno = new Aluno();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        aluno.setNomeCompleto(nomeSobrenome.getText().toString());
        aluno.setWhatsapp(whatsapp.getText().toString().isEmpty() ? null : Mask.unmask(whatsapp.getText().toString()));
        aluno.setDataNascimento(dataNascimento.getText().toString().isEmpty() ? null : sdf.parse(dataNascimento.getText().toString()));
        aluno.setNivelAcesso(1);

        String emailString = email.getText().toString();
        String senhaString = senha.getText().toString();

        // criando um novo usuário pelo firebase auth
        Task<AuthResult> authResultTask = FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailString, senhaString);
        authResultTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // apos criar o usuário sair
                FirebaseAuth.getInstance().signOut();
                // criando uma collection para um aluno no banco
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseUser user = Objects.requireNonNull(authResultTask.getResult()).getUser();
                DocumentReference df = database.collection("alunos").document(Objects.requireNonNull(user).getUid());
                // definindo uma coleção "Aluno" no banco
                df.set(aluno);

                Toast.makeText(getApplicationContext(), "Sucesso!", Toast.LENGTH_SHORT).show();

                //
                // retornar sucesso
                //

            } else {
                String erro;
                try {
                    throw Objects.requireNonNull(task.getException());
                } catch (FirebaseAuthInvalidCredentialsException err) {
                    erro = "O e-mail informado não existe!";
                    alertEmail.setVisibility(View.VISIBLE);
                    alertEmail.setText(R.string.erro_email_inexistente);
                } catch (FirebaseAuthUserCollisionException err) {
                    erro = "O e-mail informado já está em uso!";
                    alertEmail.setVisibility(View.VISIBLE);
                    alertEmail.setText(R.string.erro_email_em_uso);
                } catch (FirebaseNetworkException err) {
                    erro = "Conexão com a internet indisponivel!";
                } catch (Exception err) {
                    err.printStackTrace();
                    erro = "Ocorreu um erro ao realizar o cadastro!";
                }
                Toast.makeText(getApplicationContext(), erro, Toast.LENGTH_SHORT).show();
            }
        });

    }

}