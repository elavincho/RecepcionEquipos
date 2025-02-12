package com.developers.recepcionEquipos.controlador;

import com.developers.recepcionEquipos.entidad.Usuario;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.slf4j.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {
    
    private final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/registro")
    public String registro(Model model) {
        return "usuario/registro";
    }
    
    @PostMapping("/save")
    public String save(Usuario usuario) {
        logger.info("Usuario Registro: {}", usuario);
        
        usuario.setRol("USER");
        
        // imagen
        //if (usuario.getIdUsuario() == null) { // cuando se crea un usuario
        //    String nombreFoto = upload.saveImage(file);
        //    usuario.setFoto(nombreFoto);
        //}
        
        usuarioServicio.save(usuario);
        
        return "redirect:/usuario/iniciarSesion";
    }
    
    @GetMapping("/iniciarSesion")
    public String iniciarSesion() {
        return "usuario/iniciarSesion";
    }


    
    @PostMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session, Model model) {

        logger.info("Accesos : {}", usuario);

        Optional<Usuario> user = usuarioServicio.findByEmail(usuario.getEmail());
        logger.info("Usuario de la bd: {}", user.get());

        // validacion momentanea
        if (user.isPresent()) {

            // Obtenemos el id del usuario para usarlo en cualquier lugar de la app
            session.setAttribute("idusuario", user.get().getIdUsuario());

            // Obtenemos todos los datos del usuario para usarlo en cualquier lugar de la
            // app
            session.setAttribute("usersession", user.get());

            if (user.get().getRol().equals("ADMIN")) {
                return "redirect:/administrador/homeAdministrador";

            } else if (user.get().getRol().equals("USER")) {
                return "redirect:/";

            } else if (user.get().getRol().equals("BLOQUEADO")) {
                return "redirect:/usuario/bloqueado";
            }
        } else {
            logger.info("Usuario no exsite");

            // crear un html o una alerta de que el usuario no existe
        }

        return "redirect:/";
    }

    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session) {
        session.removeAttribute("idusuario");
        return "redirect:/";
    }

    @GetMapping("/bloqueado")
    public String bloqueado() {
        return "usuario/bloqueado";
    }
}
