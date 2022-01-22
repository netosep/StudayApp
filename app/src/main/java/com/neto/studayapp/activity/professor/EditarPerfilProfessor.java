package com.neto.studayapp.activity.professor;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.neto.studayapp.R;
import com.neto.studayapp.model.Professor;
import com.neto.studayapp.util.Mask;
import com.neto.studayapp.util.Validator;

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

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class EditarPerfilProfessor extends AppCompatActivity {

    private EditText nomeSobrenome, email, whatsapp, dataNascimento;
    private EditText descricao, biografia, valor;
    private Spinner sexo;
    private Button buttonUpdate;
    private TextView alertNome, alertEmail, alertWhatsapp, alertDataNasc;
    private TextView alertSexo, alertValor, alertDescricao, alertBiografia;
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
            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MinhaContaProfessor.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    private boolean formularioIsValid() {
        boolean nomeValido = false, emailValido = false, whatsappValido = false, dataValido = false;
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

        return nomeValido && emailValido && sexoValido && dataValido &&
                valorValido && whatsappValido && descricaoValido && biografiaValido;
    }

    private void iniciarComponentes() {
        // editText e spinner
        nomeSobrenome = findViewById(R.id.editTextNomeSobrenome);
        email = findViewById(R.id.editTextEmail);
        whatsapp = findViewById(R.id.editTextWhatsapp);
        dataNascimento = findViewById(R.id.editTextDataNasc);
        sexo = findViewById(R.id.spinnerSexo);
        valor = findViewById(R.id.editTextValor);
        descricao = findViewById(R.id.editTextDescricao);
        biografia = findViewById(R.id.editTextBiografia);
        // textView
        alertNome = findViewById(R.id.textViewNomeSobrenomeAlert);
        alertEmail = findViewById(R.id.textViewEmailAlert);
        alertWhatsapp = findViewById(R.id.textViewWhatsappAlert);
        alertDataNasc = findViewById(R.id.textViewDataNascAlert);
        alertSexo = findViewById(R.id.textViewSexoAlert);
        alertValor = findViewById(R.id.textViewValorAlert);
        alertDescricao = findViewById(R.id.textViewDescricaoAlert);
        alertBiografia = findViewById(R.id.textViewBiografiaAlert);
        // button
        buttonUpdate = findViewById(R.id.buttonUpdateCadastro);
        setSpinner();
    }

    private void setSpinner() {
        String[] valores = getResources().getStringArray(R.array.sexo);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexo.setAdapter(adapter);
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
        attProfessor.setUuidProfessor(professor.getUuidProfessor());
        attProfessor.setNomeCompleto(nomeSobrenome.getText().toString());
        attProfessor.setEmail(email.getText().toString());
        attProfessor.setWhatsapp(Mask.unmask(whatsapp.getText().toString()));
        attProfessor.setDataNascimento(dataNascimento.getText().toString().isEmpty() ? null : sdf.parse(dataNascimento.getText().toString()));
        attProfessor.setSexo(sexo.getSelectedItem().toString());
        attProfessor.setValorAula(Double.parseDouble(valor.getText().toString().replaceAll("[,]", ".")));
        attProfessor.setDescricao(descricao.getText().toString());
        attProfessor.setBiografia(biografia.getText().toString());
        documentRef.set(attProfessor);

        Toast.makeText(this, "Perfil atualizado com sucesso!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, MinhaContaProfessor.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

    public void voltar(View view) {
        startActivity(new Intent(this, MinhaContaProfessor.class));
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();
    }

}