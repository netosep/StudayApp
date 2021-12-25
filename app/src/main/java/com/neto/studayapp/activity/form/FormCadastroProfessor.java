package com.neto.studayapp.activity.form;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
import com.neto.studayapp.model.Professor;
import com.neto.studayapp.util.Mask;
import com.neto.studayapp.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class FormCadastroProfessor extends AppCompatActivity {

    private EditText nomeSobrenome, email, whatsapp, dataNascimento;
    private EditText descricao, biografia, senha, confirmSenha;
    private Spinner sexo;
    // foto de perfil
    private Button buttonSubmit;
    private TextView alertNome, alertEmail, alertWhatsapp, alertDataNasc, alertSexo;
    private TextView alertDescricao, alertBiografia, alertFotoPerfil, alertSenha, alertConfSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_professor);
        setSpinner();
        iniciarComponentes();

        // mascaras para o EditText de telefone e data
        whatsapp.addTextChangedListener(Mask.insert("(##) #####-####", whatsapp));
        dataNascimento.addTextChangedListener(Mask.insert("##/##/####", dataNascimento));

        buttonSubmit.setOnClickListener(view -> {
            if (formularioIsValid()) {
                try {
                    cadastrarProfessor();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Verifique possíveis campos com erro!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinner() {
        String[] valores = getResources().getStringArray(R.array.sexo);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(adapter);
    }

    public void voltar(View view) {
        startActivity(new Intent(FormCadastroProfessor.this, EscolhaCadastro.class));
        finish();
    }

    private void iniciarComponentes() {
        nomeSobrenome = findViewById(R.id.editTextNomeSobrenome);
        email = findViewById(R.id.editTextEmail);
        whatsapp = findViewById(R.id.editTextWhatsapp);
        dataNascimento = findViewById(R.id.editTextDataNasc);
        sexo = findViewById(R.id.spinnerSexo);
        descricao = findViewById(R.id.editTextDescricao);
        biografia = findViewById(R.id.editTextBiografia);
        // foto de perfil
        senha = findViewById(R.id.editTextSenha);
        confirmSenha = findViewById(R.id.editTextConfSenha);
        buttonSubmit = findViewById(R.id.buttonSubmitCadastro);
        alertNome = findViewById(R.id.textViewNomeSobrenomeAlert);
        alertEmail = findViewById(R.id.textViewEmailAlert);
        alertWhatsapp = findViewById(R.id.textViewWhatsappAlert);
        alertDataNasc = findViewById(R.id.textViewDataNascAlert);
        alertSexo = findViewById(R.id.textViewSexoAlert);
        alertDescricao = findViewById(R.id.textViewDescricaoAlert);
        alertBiografia = findViewById(R.id.textViewBiografiaAlert);
        alertFotoPerfil = findViewById(R.id.textViewFotoAlert);
        alertSenha = findViewById(R.id.textViewSenhaAlert);
        alertConfSenha = findViewById(R.id.textViewConfSenhaAlert);
    }

    private boolean formularioIsValid() {

        boolean nomeValido = false, emailValido = false, whatsappValido = false, dataValido = false, descricaoValido = false;
        boolean senhaValido = false, confSenhaValido = false, sexoValido = false, biografiaValido = false, formValido = false;
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

        return nomeValido && emailValido && sexoValido && dataValido &&
                whatsappValido && descricaoValido && biografiaValido && formValido;
    }

    @SuppressLint("SimpleDateFormat")
    private void cadastrarProfessor() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Professor professor = new Professor();
        professor.setNomeCompleto(nomeSobrenome.getText().toString());
        professor.setWhatsapp(Mask.unmask(whatsapp.getText().toString()));
        professor.setDataNascimento(dataNascimento.getText().toString().isEmpty() ? null : sdf.parse(dataNascimento.getText().toString()));
        professor.setSexo(sexo.getSelectedItem().toString());
        professor.setDescricao(descricao.getText().toString());
        professor.setBiografia(biografia.getText().toString());
        //professor.setUrlFotoPerfil();
        professor.setNivelAcesso(2);

        String emailString = email.getText().toString();
        String senhaString = senha.getText().toString();

        Task<AuthResult> authResultTask = FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailString, senhaString);
        authResultTask.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseFirestore database = FirebaseFirestore.getInstance();
                FirebaseUser user = Objects.requireNonNull(authResultTask.getResult()).getUser();
                DocumentReference df = database.collection("professor").document(Objects.requireNonNull(user).getUid());
                df.set(professor);

                //
                // retornar sucesso
                //

                Toast.makeText(getApplicationContext(), "Sucesso!", Toast.LENGTH_SHORT).show();

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