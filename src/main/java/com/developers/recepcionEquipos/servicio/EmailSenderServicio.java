package com.developers.recepcionEquipos.servicio;

import com.developers.recepcionEquipos.entidad.EmailSender;
import jakarta.mail.MessagingException;


public interface EmailSenderServicio {
    
    public String sendMail(EmailSender emailSender) throws MessagingException;

    public String enviarCorreoRestablecimiento(String destinatario, Integer IdUsuario) throws MessagingException;
}
