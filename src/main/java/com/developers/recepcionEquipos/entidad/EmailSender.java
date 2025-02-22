package com.developers.recepcionEquipos.entidad;

public class EmailSender {

    private String destinatario;
    private String asunto;
    private String mensaje;
    private String token;

    public EmailSender() {
    }

    public EmailSender(String destinatario, String asunto, String mensaje, String token) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.token = token;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "EmailSender{" + "destinatario=" + destinatario + ", asunto=" + asunto + ", mensaje=" + mensaje + ", token=" + token + '}';
    }

}
