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
import org.springframework.mail.SimpleMailMessage;
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

    @Autowired
    private TokenServicio tokenServicio;

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



    public void enviarCorreoRestablecimiento(String email, Integer IdUsuario) {
        // Generar un token único
        String token = tokenServicio.generarToken(IdUsuario);

        // Crear el enlace de restablecimiento
        String resetUrl = "http://localhost:8080/linkCambiarContrasena?token=" + token;
        String mensaje = "Haz clic en el siguiente enlace para restablecer tu contraseña: " + resetUrl;

        // Crear y enviar el correo
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("Restablecimiento de Contraseña");
        mailMessage.setText(mensaje);
        javaMailSender.send(mailMessage);

        System.out.println("Correo enviado a " + email + ": " + mensaje);
    }


}
