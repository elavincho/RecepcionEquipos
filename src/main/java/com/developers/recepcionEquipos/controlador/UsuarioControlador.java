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

@Controller
@RequestMapping("/")
public class UsuarioControlador {
    
     
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("")
    public String home() {
        return "usuario/home";
    }
    
    @GetMapping("usuario/registro")
    public String registro() {
        return "usuario/registro";
    }
    
    @PostMapping("usuario/save")
    public String save(Usuario usuario) {
        usuario.setRol("USER");
        
        // imagen
        //if (usuario.getIdUsuario() == null) { // cuando se crea un usuario
        //    String nombreFoto = upload.saveImage(file);
        //    usuario.setFoto(nombreFoto);
        //}
        
        usuarioServicio.save(usuario);
        
        return "redirect:/";
    }
    
    @GetMapping("usuario/iniciarSesion")
    public String iniciarSesion() {
        return "usuario/iniciarSesion";
    }
    
    @PostMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session, Model model) {

        

        //logger.info("Accesos : {}", usuario);

        Optional<Usuario> user = usuarioServicio.findByEmail(usuario.getEmail());
        // logger.info("Usuario de la bd: {}", user.get());

        // validacion momentanea
        if (user.isPresent()) {

            // Obtenemos el id del usuario para usarlo en cualquier lugar de la app
            session.setAttribute("idusuario", user.get().getIdUsuario());

            // Obtenemos todos los datos del usuario para usarlo en cualquier lugar de la
            // app
            session.setAttribute("usersession", user.get());

            if (user.get().getRol().equals("ADMIN")) {

                return "redirect:/administrador";
            } else if (user.get().getRol().equals("USER")) {

                return "redirect:/";
            } else if (user.get().getRol().equals("BLOQUEADO")) {
                return "redirect:/usuario/bloqueado";
            }
        } else {
            //logger.info("Usuario no exsite");

            // crear un html o una alerta de que el usuario no existe
        }

        return "redirect:/";
    }
}
