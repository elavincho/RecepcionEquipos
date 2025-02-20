package com.developers.recepcionEquipos.entidad;

public class EmailSender {

    private String destinatario;
    private String asunto;
    private String mensaje;

    public EmailSender() {
    }

    public EmailSender(String destinatario, String asunto, String mensaje) {
        this.destinatario = destinatario;
        this.asunto = asunto;
        this.mensaje = mensaje;
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

    @Override
    public String toString() {
        return "EmailSender{" + "destinatario=" + destinatario + ", asunto=" + asunto + ", mensaje=" + mensaje + '}';
    }
    
}
