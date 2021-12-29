package com.neto.studayapp.util;

import android.annotation.SuppressLint;
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
    private String msgHora;
    private String msgValor;
    private String msgSelect;
    private String msgDescricao;
    private String msgBiografia;
    private String msgSenha;
    private String msgConfSenha;

    public Validator() {
    }

    public boolean nomeIsValid(EditText nome) {
        String stringNome = nome.getText().toString();
        if (stringNome.isEmpty()) {
            this.setMsgNome("Campo obrigatório!");
            return false;
        } else if (stringNome.startsWith(" ") || stringNome.endsWith(" ")) {
            this.setMsgNome("Nome inválido");
            return false;
        } else if (stringNome.split("[ ]").length < 2) {
            this.setMsgNome("Insira nome e sobrenome!");
            return false;
        } else if (stringNome.length() < 5) {
            this.setMsgNome("Mínimo: 5 caracteres!");
            return false;
        } else if (stringNome.length() > 35) {
            this.setMsgNome("Máximo: 35 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean emailIsValid(EditText email) {
        String stringEmail = email.getText().toString();
        if (stringEmail.isEmpty()) {
            this.setMsgEmail("Campo obrigatório!");
            return false;
        } else if (!stringEmail.contains("@") || stringEmail.startsWith(" ") ||
                stringEmail.endsWith(" ") || stringEmail.contains(" ")) {
            this.setMsgEmail("E-mail inválido!");
            return false;
        } else if (stringEmail.length() < 10) {
            this.setMsgEmail("Mínimo: 10 caracteres!");
            return false;
        } else if (stringEmail.length() > 50) {
            this.setMsgEmail("Máximo: 50 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean whastappIsValid(EditText whatsapp, boolean requerido) {
        String stringWhatsApp = whatsapp.getText().toString();
        if (stringWhatsApp.isEmpty() && requerido) {
            this.setMsgWhatsApp("Campo obrigatório!");
            return false;
        } else if (!stringWhatsApp.isEmpty() && stringWhatsApp.length() < 15) {
            this.setMsgWhatsApp("Telefone inválido!");
            return false;
        } else {
            return true;
        }
    }

    public boolean valorIsValid(EditText valor) {
        String stringValor = valor.getText().toString();
        if (stringValor.isEmpty()) {
            this.setMsgValor("Campo obrigatório!");
            return false;
        } else {
            try {
                stringValor = stringValor.replaceAll("[,]", ".");
                if (Double.parseDouble(stringValor) > 0) {
                    return true;
                } else {
                    this.setMsgValor("Valor inválido!");
                    return false;
                }
            } catch (Exception e) {
                this.setMsgValor("Valor inválido!");
                return false;
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    public boolean dataIsValid(EditText data) {
        String stringData = data.getText().toString();
        if (stringData.isEmpty()) {
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

    @SuppressLint("SimpleDateFormat")
    public boolean horaIsValid(EditText hora) {
        String stringHora = hora.getText().toString();
        if (stringHora.isEmpty()) {
            this.setMsgHora("Campo obrigatório!");
            return false;
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            sdf.setLenient(false);
            try {
                sdf.parse(stringHora);
                return true;
            } catch (ParseException e) {
                this.setMsgHora("Horário inválido!");
                return false;
            }
        }
    }

    public boolean selectIsValid(Spinner select) {
        String stringSelect = select.getSelectedItem().toString().toUpperCase(Locale.ROOT);
        if (stringSelect.equals("SELECIONE")) {
            this.setMsgSelect("Campo obrigatório!");
            return false;
        } else {
            return true;
        }
    }

    public boolean descricaoIsValid(EditText descricao) {
        String stringDescricao = descricao.getText().toString();
        if (stringDescricao.isEmpty()) {
            return true;
        } else if (stringDescricao.length() < 10) {
            this.setMsgDescricao("Mínimo: 10 caracteres!");
            return false;
        } else if (stringDescricao.length() > 55) {
            this.setMsgDescricao("Máximo: 55 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean biografiaIsValid(EditText biografia) {
        String stringBiografia = biografia.getText().toString();
        if (stringBiografia.isEmpty()) {
            this.setMsgBiografia("Campo obrigatório!");
            return false;
        } else if (stringBiografia.length() < 15) {
            this.setMsgBiografia("Mínimo: 15 caracteres!");
            return false;
        } else if (stringBiografia.length() > 450) {
            this.setMsgBiografia("Máximo: 450 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean senhaIsValid(EditText senha) {
        String stringSenha = senha.getText().toString();
        if (stringSenha.isEmpty()) {
            this.setMsgSenha("Campo obrigatório!");
            return false;
        } else if (stringSenha.length() < 6) {
            this.setMsgSenha("Mínimo: 6 caracteres!");
            return false;
        } else if (stringSenha.length() > 35) {
            this.setMsgSenha("Máximo: 35 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean confSenhaIsValid(EditText confSenha) {
        String stringConfSenha = confSenha.getText().toString();
        if (stringConfSenha.isEmpty()) {
            this.setMsgConfSenha("Campo obrigatório!");
            return false;
        } else if (stringConfSenha.length() < 6) {
            this.setMsgConfSenha("Mínimo: 6 caracteres!");
            return false;
        } else if (stringConfSenha.length() > 35) {
            this.setMsgConfSenha("Máximo: 35 caracteres!");
            return false;
        } else {
            return true;
        }
    }

    public boolean senhaEquals(EditText senha, EditText confSenha) {
        String stringSenha = senha.getText().toString();
        String stringConfSenha = confSenha.getText().toString();
        if (stringSenha.equals(stringConfSenha)) {
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

    public String getMsgHora() {
        return msgHora;
    }

    public void setMsgHora(String msgHora) {
        this.msgHora = msgHora;
    }

    public String getMsgSelect() {
        return msgSelect;
    }

    public String getMsgValor() {
        return msgValor;
    }

    public void setMsgValor(String msgValor) {
        this.msgValor = msgValor;
    }

    public void setMsgSelect(String msgSexo) {
        this.msgSelect = msgSexo;
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
