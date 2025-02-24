package com.developers.recepcionEquipos.controlador;

import com.developers.recepcionEquipos.entidad.EmailSender;
import com.developers.recepcionEquipos.entidad.Usuario;
import com.developers.recepcionEquipos.servicio.EmailSenderServicio;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import com.developers.recepcionEquipos.servicioImpl.TokenServicio;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private EmailSenderServicio emailSenderServicio;

    @Autowired
    private TokenServicio tokenServicio;

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

    // Link para mostrar el formulario Olvidaste tu Contraseña
    @GetMapping("/solicitarRestablecimiento")
    public String mostrarFormularioRestablecimiento() {
        return "usuario/recuperarContrasena";
    }

    // Metodo Post para enviar los datos del usuario al formulario
    @PostMapping("/solicitarRestablecimiento")
    public String solicitarRestablecimiento(@RequestParam String destinatario, RedirectAttributes redirectAttributes)
            throws MessagingException {
        // Buscar al usuario por su correo electrónico
        Optional<Usuario> optionalUsuario = usuarioServicio.findByEmail(destinatario);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            // Enviar el correo con el token
            emailSenderServicio.enviarCorreoRestablecimiento(destinatario, usuario.getIdUsuario());
            redirectAttributes.addFlashAttribute("exito", "Se ha enviado un correo con las instrucciones.");
        } else {
            redirectAttributes.addFlashAttribute("error", "No se encontró un usuario con ese correo.");
        }
        return "redirect:/usuario/iniciarSesion";
    }

    // Endpoint para mostrar el formulario de cambio de contraseña
    @GetMapping("/linkCambiarContrasena")
    public String linkCambiarContrasena(@RequestParam String token, Model model, HttpSession session) {

        logger.info("Token recibido: ", token);
        Integer IdUsuario = tokenServicio.validarToken(token);
        if (IdUsuario == null) {
            return "redirect:/usuario/iniciarSesion?error=Token inválido";
        }

        Optional<Usuario> optionalUsuario = usuarioServicio.findByIdUsuario(IdUsuario);
        if (optionalUsuario.isEmpty()) {
            logger.info("Usuario no encontrado para el ID: ", IdUsuario);
            return "redirect:/usuario/iniciarSesion?error=Usuario no encontrado";
        }

        Usuario usuario = optionalUsuario.get();
        model.addAttribute("usuario", usuario);

        // Pasar el token al formulario
        model.addAttribute("token", token);

        return "usuario/linkCambiarContrasena";
    }

    // Metodo por el cual el usuario ingresa su nueva contraseña
    @PostMapping("/linkUpdatePassword")
    public String linkUpdatePassword(String token,
            @RequestParam String password2,
            @RequestParam String password3,
            Model model, HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {
        // Verifica si el token llega correctamente
        logger.info("Token recibido: {}", token);

        Integer IdUsuario = tokenServicio.validarToken(token);
        if (IdUsuario == null) {
            redirectAttributes.addFlashAttribute("error", "Token inválido o expirado.");
            return "redirect:/usuario/iniciarSesion";
        }

        Optional<Usuario> optionalUsuario = usuarioServicio.findByIdUsuario(IdUsuario);
        if (optionalUsuario.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            return "redirect:/usuario/iniciarSesion";
        }

        Usuario usuario = optionalUsuario.get();

        if (password2.equals(password3)) {
            usuario.setContrasena(password3);
        } else {
            redirectAttributes.addFlashAttribute("error", "Las contraseñas no coinciden.");
            return "redirect:/usuario/iniciarSesion";
        }

        usuarioServicio.save(usuario);
        tokenServicio.eliminarToken(token);

        redirectAttributes.addFlashAttribute("exito", "¡Contraseña modificada correctamente!");
        return "redirect:/usuario/iniciarSesion";
    }

}
