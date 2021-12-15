package com.example.studayapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studayapp.R;
import com.example.studayapp.model.Aluno;
import com.example.studayapp.util.Mask;
import com.example.studayapp.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FormCadastroAluno extends AppCompatActivity {

    private ImageView imgVoltar;

    private EditText nomeSobrenome;
    private EditText email;
    private EditText whatsapp;
    private EditText dataNascimento;
    private EditText senha;
    private EditText confirmSenha;
    private Button buttonSubmit;

    private TextView alertNome;
    private TextView alertEmail;
    private TextView alertWhatsapp;
    private TextView alertDataNasc;
    private TextView alertSenha;
    private TextView alertConfSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_aluno);
        iniciarComponentes();

        imgVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FormCadastroAluno.this, FormLogin.class);
                startActivity(intent);
            }
        });

        // mascaras para o EditText de telefone e data
        whatsapp.addTextChangedListener(Mask.insert("(##) #####-####", whatsapp));
        dataNascimento.addTextChangedListener(Mask.insert("##/##/####", dataNascimento));

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(formularioIsValid()) {
                    try {
                        cadastrarAluno();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Verifique possíveis campos com erro!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void voltar(View view) {
        startActivity(new Intent(getApplicationContext(), EscolhaCadastro.class));
    }

    private void iniciarComponentes() {
        imgVoltar = findViewById(R.id.imgBack);

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

        if(validator.nomeIsValid(nomeSobrenome)) {
            alertNome.setVisibility(View.INVISIBLE);
            nomeValido = true;
        } else {
            alertNome.setVisibility(View.VISIBLE);
            alertNome.setText(validator.getMsgNome());
        }

        if(validator.emailIsValid(email)) {
            alertEmail.setVisibility(View.INVISIBLE);
            emailValido = true;
        } else {
            alertEmail.setVisibility(View.VISIBLE);
            alertEmail.setText(validator.getMsgEmail());
        }

        if(validator.whastappIsValid(whatsapp)) {
            alertWhatsapp.setVisibility(View.INVISIBLE);
            whatsappValido = true;
        } else {
            alertWhatsapp.setVisibility(View.VISIBLE);
            alertWhatsapp.setText(validator.getMsgWhatsApp());
        }

        if(validator.dataIsValid(dataNascimento)) {
            alertDataNasc.setVisibility(View.INVISIBLE);
            dataValido = true;
        } else {
            alertDataNasc.setVisibility(View.VISIBLE);
            alertDataNasc.setText(validator.getMsgData());
        }

        if(validator.senhaIsValid(senha)) {
            alertSenha.setVisibility(View.INVISIBLE);
            senhaValido = true;
        } else {
            alertSenha.setVisibility(View.VISIBLE);
            alertSenha.setText(validator.getMsgSenha());
        }

        if(validator.confSenhaIsValid(confirmSenha)) {
            alertConfSenha.setVisibility(View.INVISIBLE);
            confSenhaValido = true;
        } else {
            alertConfSenha.setVisibility(View.VISIBLE);
            alertConfSenha.setText(validator.getMsgConfSenha());
        }

        if(senhaValido && confSenhaValido) {
            if(validator.senhaEquals(senha, confirmSenha)) {
                alertConfSenha.setVisibility(View.INVISIBLE);
                formValido = true;
            } else {
                alertConfSenha.setVisibility(View.VISIBLE);
                alertConfSenha.setText(validator.getMsgConfSenha());
            }
        }

        if(nomeValido && emailValido && whatsappValido && dataValido && formValido) {
            return true;
        } else {
            return false;
        }
    }

    public void cadastrarAluno() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Aluno aluno = new Aluno();
        aluno.setNomeCompleto(nomeSobrenome.getText().toString());
        aluno.setEmail(email.getText().toString());
        aluno.setWhatsapp(Mask.unmask(whatsapp.getText().toString()));
        aluno.setDataNascimento(dataNascimento.getText().toString().isEmpty() ? null : sdf.parse(dataNascimento.getText().toString()));
        aluno.setSenha(senha.getText().toString());
        aluno.setTipo(1);

        System.out.println("=== objeto aluno criado ===");
        System.out.println("Nome: "+aluno.getNomeCompleto());
        System.out.println("Email: "+aluno.getEmail());
        System.out.println("Whatsapp: "+aluno.getWhatsapp());
        System.out.println("Data: "+aluno.getDataNascimento());
        System.out.println("Senha: "+aluno.getSenha());
        System.out.println("Tipo: "+aluno.getTipo());
        System.out.println("===========================");

        //Intent intent = new Intent(getApplicationContext(), TesteAluno.class);
        //intent.putExtra("aluno", aluno);
        //startActivity(intent);
    }

}