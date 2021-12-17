package com.example.studayapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studayapp.R;
import com.example.studayapp.model.Professor;
import com.example.studayapp.util.Mask;
import com.example.studayapp.util.Validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FormCadastroProfessor extends AppCompatActivity {

    private EditText nomeSobrenome;
    private EditText email;
    private EditText whatsapp;
    private EditText dataNascimento;
    private Spinner sexo;
    private EditText descricao;
    private EditText biografia;
    // foto de perfil
    private EditText senha;
    private EditText confirmSenha;
    private Button buttonSubmit;

    private TextView alertNome;
    private TextView alertEmail;
    private TextView alertWhatsapp;
    private TextView alertDataNasc;
    private TextView alertSexo;
    private TextView alertDescricao;
    private TextView alertBiografia;
    private TextView alertFotoPerfil;
    private TextView alertSenha;
    private TextView alertConfSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_professor);
        setSpinner();
        iniciarComponentes();

        // mascaras para o EditText de telefone e data
        whatsapp.addTextChangedListener(Mask.insert("(##) #####-####", whatsapp));
        dataNascimento.addTextChangedListener(Mask.insert("##/##/####", dataNascimento));

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(formularioIsValid()) {
                    try {
                        cadastrarProfessor();
                        Toast.makeText(getApplicationContext(), "Tudo OK!", Toast.LENGTH_SHORT).show();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Verifique possíveis campos com erro!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setSpinner() {
        Spinner sexos = (Spinner) findViewById(R.id.spinnerSexo);
        String[] valores = getResources().getStringArray(R.array.sexo);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexos.setAdapter(adapter);
    }

    public void voltar(View view) {
        startActivity(new Intent(getApplicationContext(), EscolhaCadastro.class));
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

        if(validator.whastappIsValid(whatsapp, true)) {
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

        if(validator.selectIsValid(sexo)) {
            alertSexo.setVisibility(View.INVISIBLE);
            sexoValido = true;
        } else {
            alertSexo.setVisibility(View.VISIBLE);
            alertSexo.setText(validator.getMsgSelect());
        }

        if(validator.descricaoIsValid(descricao)) {
            alertDescricao.setVisibility(View.INVISIBLE);
            descricaoValido = true;
        } else {
            alertDescricao.setVisibility(View.VISIBLE);
            alertDescricao.setText(validator.getMsgDescricao());
        }

        if(validator.biografiaIsValid(biografia)) {
            alertBiografia.setVisibility(View.INVISIBLE);
            biografiaValido = true;
        } else {
            alertBiografia.setVisibility(View.VISIBLE);
            alertBiografia.setText(validator.getMsgBiografia());
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

        if(nomeValido && emailValido && sexoValido && dataValido &&
           whatsappValido && descricaoValido && biografiaValido && formValido) {
            return true;
        } else {
            return false;
        }
    }

    private void cadastrarProfessor() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Professor professor = new Professor();
        professor.setNomeCompleto(nomeSobrenome.getText().toString());
        professor.setEmail(email.getText().toString());
        professor.setWhatsapp(Mask.unmask(whatsapp.getText().toString()));
        professor.setDataNascimento(dataNascimento.getText().toString().isEmpty() ? null : sdf.parse(dataNascimento.getText().toString()));
        professor.setSexo(sexo.getSelectedItem().toString());
        professor.setDescricao(descricao.getText().toString());
        professor.setBiografia(biografia.getText().toString());
        //professor.setUrlFotoPerfil();
        professor.setSenha(senha.getText().toString());
        professor.setTipo(2);

        System.out.println("=== objeto professor criado ===");
        System.out.println("Nome: "+professor.getNomeCompleto());
        System.out.println("Email: "+professor.getEmail());
        System.out.println("Whatsapp: "+professor.getWhatsapp());
        System.out.println("Data: "+professor.getDataNascimento());
        System.out.println("Sexo: "+professor.getSexo());
        System.out.println("Descrição: "+professor.getDescricao());
        System.out.println("Biografia: "+professor.getBiografia());
        System.out.println("Senha: "+professor.getSenha());
        System.out.println("Tipo: "+professor.getTipo());
        System.out.println("===============================");
    }


}