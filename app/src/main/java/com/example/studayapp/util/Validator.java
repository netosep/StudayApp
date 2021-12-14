package com.example.studayapp.util;

import android.widget.EditText;
import android.widget.Spinner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Validator {

    private String msgNome;
    private String msgEmail;
    private String msgWhatsApp;
    private String msgData;
    private String msgSexo;
    private String msgDescricao;
    private String msgBiografia;
    private String msgSenha;
    private String msgConfSenha;

    public Validator() {}

    public boolean nomeIsValid(EditText nome) {
        String stringNome = nome.getText().toString();
        if(stringNome.isEmpty()) {
            this.setMsgNome("Campo obrigatório!");
            return false;
        }
        // separando por espaço
        else if(stringNome.split("\\s+").length < 2){
            this.setMsgNome("Insira nome e sobrenome!");
            return false;
        }
        else if(stringNome.length() < 5) {
            this.setMsgNome("Mínimo: 5 caracteres!");
            return false;
        }
        else if(stringNome.length() > 35) {
            this.setMsgNome("Máximo: 35 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean emailIsValid(EditText email) {
        String stringEmail = email.getText().toString();
        if(stringEmail.isEmpty()) {
            this.setMsgEmail("Campo obrigatório!");
            return false;
        }
        else if(!stringEmail.contains("@") || stringEmail.startsWith(" ") || stringEmail.contains(" ")){
            this.setMsgEmail("E-mail inválido!");
            return false;
        }
        else if(stringEmail.length() < 10) {
            this.setMsgEmail("Mínimo: 10 caracteres!");
            return false;
        }
        else if(stringEmail.length() > 50) {
            this.setMsgEmail("Máximo: 50 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean whastappIsValid(EditText whatsapp) {
        String stringWhatsApp = whatsapp.getText().toString();
        if(stringWhatsApp.isEmpty()) {
            this.setMsgWhatsApp("Campo obrigatório!");
            return false;
        }
        else if(stringWhatsApp.length() < 15) {
            this.setMsgWhatsApp("Telefone inválido!");
            return false;
        } else {
            return true;
        }
    }

    public boolean dataIsValid(EditText data) {
        String stringData = data.getText().toString();
        if(stringData.isEmpty()) {
            return true;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            sdf.setLenient(false); // não corrigir números de datas erradas
            try {
                sdf.parse(stringData);
                return true;
            } catch (ParseException err) {
                this.setMsgData("Data inválida!");
                return false;
            }
        }
    }

    public boolean sexoIsValid(Spinner sexo) {
        String stringSexo = sexo.getSelectedItem().toString().toUpperCase(Locale.ROOT);
        if(stringSexo.equals("SELECIONE")) {
            this.setMsgSexo("Campo obrigatório!");
            return false;
        } else {
            return true;
        }
    }

    public boolean descricaoIsValid(EditText descricao) {
        String stringDescricao = descricao.getText().toString();
        if(stringDescricao.isEmpty()) {
            return true;
        }
        else if(stringDescricao.length() < 10) {
            this.setMsgDescricao("Mínimo: 10 caracteres!");
            return false;
        }
        else if(stringDescricao.length() > 55) {
            this.setMsgDescricao("Máximo: 55 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean biografiaIsValid(EditText biografia) {
        String stringBiografia = biografia.getText().toString();
        if(stringBiografia.isEmpty()) {
            this.setMsgBiografia("Campo obrigatório!");
            return false;
        }
        else if(stringBiografia.length() < 15) {
            this.setMsgBiografia("Mínimo: 15 caracteres!");
            return false;
        }
        else if(stringBiografia.length() > 450) {
            this.setMsgBiografia("Máximo: 450 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean senhaIsValid(EditText senha) {
        String stringSenha = senha.getText().toString();
        if(stringSenha.isEmpty()) {
            this.setMsgSenha("Campo obrigatório!");
            return false;
        }
        else if(stringSenha.length() < 5) {
            this.setMsgSenha("Mínimo: 5 caracteres!");
            return false;
        }
        else if(stringSenha.length() > 35) {
            this.setMsgSenha("Máximo: 35 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean confSenhaIsValid(EditText confSenha) {
        String stringConfSenha = confSenha.getText().toString();
        if(stringConfSenha.isEmpty()) {
            this.setMsgConfSenha("Campo obrigatório!");
            return false;
        }
        else if(stringConfSenha.length() < 5) {
            this.setMsgConfSenha("Mínimo: 5 caracteres!");
            return false;
        }
        else if(stringConfSenha.length() > 35) {
            this.setMsgConfSenha("Máximo: 35 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean senhaEquals(EditText senha, EditText confSenha) {
        String stringSenha = senha.getText().toString();
        String stringConfSenha = confSenha.getText().toString();
        if(stringSenha.equals(stringConfSenha)) {
            return true;
        } else {
            this.setMsgConfSenha("Senhas diferentes!");
            return false;
        }
    }

    public String getMsgNome() {
        return msgNome;
    }

    public void setMsgNome(String msgNome) {
        this.msgNome = msgNome;
    }

    public String getMsgEmail() {
        return msgEmail;
    }

    public void setMsgEmail(String msgEmail) {
        this.msgEmail = msgEmail;
    }

    public String getMsgWhatsApp() {
        return msgWhatsApp;
    }

    public void setMsgWhatsApp(String msgWhatsApp) {
        this.msgWhatsApp = msgWhatsApp;
    }

    public String getMsgData() {
        return msgData;
    }

    public void setMsgData(String msgData) {
        this.msgData = msgData;
    }

    public String getMsgSexo() {
        return msgSexo;
    }

    public void setMsgSexo(String msgSexo) {
        this.msgSexo = msgSexo;
    }

    public String getMsgDescricao() {
        return msgDescricao;
    }

    public void setMsgDescricao(String msgDescricao) {
        this.msgDescricao = msgDescricao;
    }

    public String getMsgBiografia() {
        return msgBiografia;
    }

    public void setMsgBiografia(String msgBiografia) {
        this.msgBiografia = msgBiografia;
    }

    public String getMsgSenha() {
        return msgSenha;
    }

    public void setMsgSenha(String msgSenha) {
        this.msgSenha = msgSenha;
    }

    public String getMsgConfSenha() {
        return msgConfSenha;
    }

    public void setMsgConfSenha(String msgConfSenha) {
        this.msgConfSenha = msgConfSenha;
    }

}
