package com.developers.recepcionEquipos.controlador;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.developers.recepcionEquipos.entidad.Usuario;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import com.developers.recepcionEquipos.servicioImpl.UploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/administrador")
public class AdministradorControlador {

    private final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UploadFileService upload;

    @GetMapping("/homeAdmin")
    public String homeAdmin(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        return "administrador/homeAdmin";
    }

    @GetMapping("/registroAdmin")
    public String registroAdmin(Model model) {
        return "administrador/registroAdmin";
    }

    @PostMapping("/saveAdmin")
    public String saveAdmin(Usuario usuario, @RequestParam("img") MultipartFile file) throws IOException {
        logger.info("Usuario Registro: {}", usuario);

        usuario.setRol("ADMIN");

        // imagen
        if (usuario.getIdUsuario() == null) { // cuando se crea un usuario
            String nombreFoto = upload.saveImage(file);
            usuario.setFoto(nombreFoto);
        }

        usuarioServicio.save(usuario);

        return "redirect:/usuario/iniciarSesion";
    }

    @GetMapping("/editarAdmin/{id}")
    public String editarAdmin(@PathVariable Integer id, Model model, HttpSession session) {

        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario = new Usuario();

        Optional<Usuario> optionalUsuario = usuarioServicio.findByIdUsuario(id);
        usuario = optionalUsuario.get();

        model.addAttribute("usuario", usuario);

        logger.info("Usuario a Editar: {}", usuario);

        return "administrador/editarAdmin";
    }

    @PostMapping("/actualizarAdmin")
    public String actualizarAdmin(Model model, Usuario usuario, @RequestParam("img") MultipartFile file,
            HttpSession session) throws IOException {

        model.addAttribute("sesion", session.getAttribute("idusuario"));

        
        Usuario u = new Usuario();
        u = usuarioServicio.findByIdUsuario(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

        /* cuando editamos el producto pero no cambiamos la imagen */
        if (file.isEmpty()) {
            usuario.setFoto(u.getFoto());
        } else {

            /* eliminar cuando no sea la imagen por defecto */
            if (!u.getFoto().equals("default.jpg")) {
                upload.deleteImage(u.getFoto());
            }
            String nombreFoto = upload.saveImage(file);
            usuario.setFoto(nombreFoto);
        }

        // Seteamos estos datos para que no se pierdan
        usuario.setNombreUsuario(u.getNombreUsuario());
        usuario.setEmail(u.getEmail());
        usuario.setContrasena(u.getContrasena());
        usuario.setRol("ADMIN");

        usuarioServicio.save(usuario);

        return "redirect:/administrador/homeAdmin";
    }
}
