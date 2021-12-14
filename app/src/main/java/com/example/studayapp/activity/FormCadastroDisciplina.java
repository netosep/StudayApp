package com.example.studayapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.studayapp.R;

public class FormCadastroDisciplina extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_cadastro_disciplina);
        setSpinnerDia();
        setSpinnerDisciplina();
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

}