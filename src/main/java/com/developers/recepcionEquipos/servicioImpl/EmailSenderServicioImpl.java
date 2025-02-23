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

    public String enviarCorreoRestablecimiento(String email, Integer IdUsuario) throws MessagingException {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true); // true indica que es multipart (para
                                                                             // adjuntos o imágenes)

        // Generar un token único
        String token = tokenServicio.generarToken(IdUsuario);

        // Crear el enlace de restablecimiento
        //String resetUrl = "http://localhost:8080/usuario/linkCambiarContrasena?token=" + token;

        Usuario usuario = usuarioServicio.findByIdUsuario(IdUsuario).get();
        Context context = new Context();
        // context.setVariable("mensaje", emailSender.getMensaje());
        context.setVariable("token", token);
        context.setVariable("email", email);
        context.setVariable("nombre", usuario.getNombre());

        // Procesar la plantilla HTML con los atributos
        String contextHTML = templateEngine.process("usuario/email", context);
        helper.setText(contextHTML, true);

        // Agregar la imagen en línea
        ClassPathResource imageResource = new ClassPathResource("/static/imagenes/iniciarSesion.png");
        helper.addInline("imagen", imageResource);

        // helper.setText("<img src='cid:imagen'/>", true);

        // Crear el contenido HTML y enviar el correo
        // String mensaje = "<html>"
        // + "<body>"
        // + "<center><img src='cid:login' alt='Login' style='width: 150px; height:
        // 150px;'/></center>" // Imagen incrustada
        // + "<center><h1>Restablecimiento de Contraseña</h1></center>"
        // + "<p>¡Hola! " + nombre + ". Solicitaste un enlace para cambiar tu
        // contraseña.</p>"
        // + "<p>Haz clic en el siguiente enlace para restablecer tu contraseña:</p>"
        // + "<a href='" + resetUrl + "'>Restablecer Contraseña</a>"
        // + "<br/><br/>"

        // + "</body>"
        // + "</html>";

        // Crear y enviar el correo
        helper.setTo(email);
        helper.setSubject("Restablecimiento de Contraseña");

        // Incrustar la imagen en el correo
        ClassPathResource resource = new ClassPathResource("static/imagenes/iniciarSesion.png"); // Ruta de la imagen
        helper.addInline("login", resource); // "login" es el identificador usado en el HTML (cid:logo)

        // helper.setText(mensaje, true); // true indica que el contenido es HTML
        // javaMailSender.send(mailMessage);

        javaMailSender.send(mimeMessage);

        return "usuario/iniciarSesion";
    }

}
