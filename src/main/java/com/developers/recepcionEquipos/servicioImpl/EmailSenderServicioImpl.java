package com.developers.recepcionEquipos.servicioImpl;

import com.developers.recepcionEquipos.entidad.EmailSender;
import com.developers.recepcionEquipos.entidad.Usuario;
import com.developers.recepcionEquipos.servicio.EmailSenderServicio;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailSenderServicioImpl implements EmailSenderServicio {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Autowired
    private UsuarioServicio usuarioServicio;

    public EmailSenderServicioImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public String sendMail(EmailSender emailSender) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
    
        helper.setTo(emailSender.getDestinatario());
        helper.setSubject(emailSender.getAsunto());
        helper.setText("<img src='cid:imagen'/>", true);
    
        Context context = new Context();
        context.setVariable("mensaje", emailSender.getMensaje());
    
        // Obtener el usuario
        Optional<Usuario> optionalUsuario = usuarioServicio.findByEmail(emailSender.getDestinatario());
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            context.setVariable("IdUsuario", usuario.getIdUsuario().toString());
            context.setVariable("nombre", usuario.getNombre());
            context.setVariable("email", emailSender.getDestinatario());
        } else {
            throw new MessagingException("Usuario no encontrado");
        }
    
        String contextHTML = templateEngine.process("usuario/email", context);
        helper.setText(contextHTML, true);
    
        // Agregar la imagen en línea
        ClassPathResource imageResource = new ClassPathResource("/static/imagenes/iniciarSesion.png");
        helper.addInline("imagen", imageResource);
    
        // Enviar el correo
        javaMailSender.send(message);
    
        return "Email enviado exitosamente";
    }


}
