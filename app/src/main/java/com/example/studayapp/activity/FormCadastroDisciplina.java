package com.example.studayapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

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

import com.example.studayapp.R;
import com.example.studayapp.model.Disciplina;
import com.example.studayapp.model.Disponibilidade;
import com.example.studayapp.util.Mask;
import com.example.studayapp.util.Validator;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.timepicker.TimeFormat;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FormCadastroDisciplina extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextView nomeUsuario;
    View header;

    Spinner nome;
    EditText valor;
    Spinner diaSemana;
    EditText das;
    EditText ate;
    Button btnSubmit;

    TextView alertDisciplina;
    TextView alertValor;
    TextView alertDia;
    TextView alertDas;
    TextView alertAte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_disciplina);
        iniciarComponentes();
        setSpinnerDia();
        setSpinnerDisciplina();

        // mascaras para formatar a hora
        das.addTextChangedListener(Mask.insert("##:##", das));
        ate.addTextChangedListener(Mask.insert("##:##", ate));

        /* ------------------- Navigation Drawer Menu ------------------- */
        setSupportActionBar(toolbar);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.abrir, R.string.fechar);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        /* -------------------------------------------------------------- */

        nomeUsuario.setText("Olá Professor!");

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(formularioIsValid()) {
                    try {
                        cadastrarDisciplina();
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

    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.minhaContaId:
                Toast.makeText(getApplicationContext(), "Minha conta", Toast.LENGTH_SHORT).show();
                break;
            case R.id.disciplinasId:
                startActivity(new Intent(getApplicationContext(), Disciplinas.class));
                break;
            case R.id.avaliacoesId:
                Toast.makeText(getApplicationContext(), "Avaliações", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sairId:
                Toast.makeText(getApplicationContext(), "Sair", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    private void setSpinnerDia() {
        Spinner dia = (Spinner) findViewById(R.id.spinnerDiaSemana);
        String[] valores = getResources().getStringArray(R.array.dias);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dia.setAdapter(adapter);
    }

    private void setSpinnerDisciplina() {
        Spinner disciplina = (Spinner) findViewById(R.id.spinnerDisciplina);
        String[] valores = getResources().getStringArray(R.array.disciplinas);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, valores);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        disciplina.setAdapter(adapter);
    }

    public void voltar(View view) {
        startActivity(new Intent(getApplicationContext(), Disciplinas.class));
    }

    public void iniciarComponentes() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toobarMenu);

        header = navigationView.getHeaderView(0);
        nomeUsuario = header.findViewById(R.id.nomeUsuarioId);

        nome = findViewById(R.id.spinnerDisciplina);
        valor = findViewById(R.id.editTextValor);
        diaSemana = findViewById(R.id.spinnerDiaSemana);
        das = findViewById(R.id.editTextDas);
        ate = findViewById(R.id.editTextAte);
        btnSubmit = findViewById(R.id.buttonSubmitCadastro);

        alertDisciplina = findViewById(R.id.textViewDisciplinaAlert);
        alertValor = findViewById(R.id.textViewValorAlert);
        alertDia = findViewById(R.id.textViewDiaSemanaAlert);
        alertDas = findViewById(R.id.textViewDasAlert);
        alertAte = findViewById(R.id.textViewAteAlert);
    }

    public boolean formularioIsValid() {

        boolean disciplinaValido = false, valorValido = false, diaValido = false;
        boolean dasValido = false, ateValido = false;
        Validator validator = new Validator();

        if(validator.selectIsValid(nome)) {
            alertDisciplina.setVisibility(View.INVISIBLE);
            disciplinaValido = true;
        } else {
            alertDisciplina.setVisibility(View.VISIBLE);
            alertDisciplina.setText(validator.getMsgSelect());
        }

        if(validator.valorIsValid(valor)) {
            alertValor.setVisibility(View.INVISIBLE);
            valorValido = true;
        } else {
            alertValor.setVisibility(View.VISIBLE);
            alertValor.setText(validator.getMsgValor());
        }

        if(validator.selectIsValid(diaSemana)) {
            alertDia.setVisibility(View.INVISIBLE);
            diaValido = true;
        } else {
            alertDia.setVisibility(View.VISIBLE);
            alertDia.setText(validator.getMsgSelect());
        }

        if(validator.horaIsValid(das)){
            alertDas.setVisibility(View.INVISIBLE);
            dasValido = true;
        } else {
            alertDas.setVisibility(View.VISIBLE);
            alertDas.setText(validator.getMsgHora());
        }

        if(validator.horaIsValid(ate)){
            alertAte.setVisibility(View.INVISIBLE);
            ateValido = true;
        } else {
            alertAte.setVisibility(View.VISIBLE);
            alertAte.setText(validator.getMsgHora());
        }

        if(disciplinaValido && valorValido && diaValido && dasValido && ateValido) {
            return true;
        } else {
            return false;
        }

    }

    public void cadastrarDisciplina() throws ParseException {

        Disciplina disciplina = new Disciplina();
        List<Disponibilidade> disponibilidades = new ArrayList<>();
        Disponibilidade disponibilidade = new Disponibilidade();

        disponibilidade.setDiaDaSemana(diaSemana.getSelectedItem().toString());
        disponibilidade.setHorarioInicio(das.getText().toString());
        disponibilidade.setHorarioFinal(ate.getText().toString());
        disponibilidades.add(disponibilidade);
        disciplina.setNome(nome.getSelectedItem().toString());
        disciplina.setCusto(Double.parseDouble(valor.getText().toString().replaceAll("[,]", ".")));
        disciplina.setDisponibilidade(disponibilidades);

        System.out.println("=== objeto disciplina criado ===");
        System.out.println("Nome: "+disciplina.getNome());
        System.out.println("Valor: "+disciplina.getCusto());
        System.out.println("Dia: "+disciplina.getDisponibilidade().get(0).getDiaDaSemana());
        System.out.println("Das: "+disciplina.getDisponibilidade().get(0).getHorarioInicio());
        System.out.println("Até: "+disciplina.getDisponibilidade().get(0).getHorarioFinal());
        System.out.println("===============================");

    }

}