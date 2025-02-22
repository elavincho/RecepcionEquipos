package com.developers.recepcionEquipos.controlador;

import com.developers.recepcionEquipos.entidad.EmailSender;
import com.developers.recepcionEquipos.servicio.EmailSenderServicio;

import jakarta.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/email")

public class EmailControlador {

    @Autowired
    private EmailSenderServicio emailSenderServicio;

    @GetMapping("/recuperarContrasena")
    public String recuperarContrasena(Model model) {

        model.addAttribute("emailSender", new EmailSender());

        return "usuario/recuperarContrasena";
    }

    @PostMapping("/send")
    private String sendEmail(@RequestParam String destinatario, EmailSender emailSender,
            RedirectAttributes redirectAttributes) throws MessagingException {

        // Asignar el destinatario al objeto EmailSender
        emailSender.setDestinatario(destinatario);
        // Enviar el correo
        emailSenderServicio.sendMail(emailSender);
        // Alerta Exito
        redirectAttributes.addAttribute("exito", "¡email enviado correctamente!");

        return "redirect:/usuario/iniciarSesion";
    }


    

}
