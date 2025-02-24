package com.developers.recepcionEquipos.controlador;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    // Metodo editarAdmin con token
    @GetMapping("/editarAdmin")
    public String editar(Model model, HttpSession session) {
        // Obtener el ID del usuario desde la sesión
        Integer idUsuario = (Integer) session.getAttribute("idusuario");

        // Generar un UUID único
        String uuid = UUID.randomUUID().toString();

        // Almacenar el UUID en la sesión
        session.setAttribute("editToken", uuid);

        // Obtener el usuario desde la base de datos
        Usuario usuario = usuarioServicio.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Agregar el usuario y el UUID al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("editToken", uuid);

        logger.info("Usuario a Editar: {}", usuario);

        return "administrador/editarAdmin";
    }

    // Metodo actualizar con token
    @PostMapping("/actualizarAdmin")
    public String actualizar(Model model, Usuario usuario, @RequestParam("img") MultipartFile file,
            HttpSession session, RedirectAttributes redirectAttributes, @RequestParam("editToken") String editToken)
            throws IOException {

        // Validar el token de edición
        String sessionToken = (String) session.getAttribute("editToken");
        if (sessionToken == null || !sessionToken.equals(editToken)) {
            redirectAttributes.addFlashAttribute("error", "Token de edición inválido");
            return "redirect:/administrador/homeAdmin";
        }

        // Obtener el ID del usuario desde la sesión
        Integer idUsuario = (Integer) session.getAttribute("idusuario");

        // Obtener el usuario desde la base de datos
        Usuario u = usuarioServicio.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

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

        // Eliminar el token de la sesión después de la actualización
        session.removeAttribute("editToken");

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Perfil editado correctamente!");

        return "redirect:/administrador/homeAdmin";
    }

    @GetMapping("/cambiarContrasena/{id}")
    public String cambiarContrasena(@PathVariable Integer id, Model model, HttpSession session) {

        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario = new Usuario();

        Optional<Usuario> optionalUsuario = usuarioServicio.findByIdUsuario(id);
        usuario = optionalUsuario.get();

        model.addAttribute("usuario", usuario);

        logger.info("Usuario a Editar: {}", usuario);

        return "administrador/cambiarContrasena";
    }

    @PostMapping("/actualizarContrasena")
    public String actualizarContrasena(Model model, Usuario usuario, @RequestParam String actualContrasena,
            @RequestParam String contrasena, @RequestParam String password2, HttpSession session,
            RedirectAttributes redirectAttributes)
            throws IOException {

        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario u = new Usuario();
        u = usuarioServicio.findByIdUsuario(Integer.parseInt(session.getAttribute("idusuario").toString())).get();

        // Verificamos el password del usuario y la cambiamos
        if (u.getContrasena().equals(actualContrasena)) {
            if (contrasena.equals(password2)) {
                usuario.setContrasena(contrasena);
            }

        } else {
            // Alerta para contraseña incorrecta
            redirectAttributes.addFlashAttribute("error", "¡Contraseña incorrecta!");
            redirectAttributes.addAttribute("id", u.getIdUsuario());
            return "redirect:/administrador/cambiarContrasena/{id}";
        }

        // Seteamos estos datos para que no se pierdan
        usuario.setIdUsuario(u.getIdUsuario());
        usuario.setNombreUsuario(u.getNombreUsuario());
        usuario.setEmail(u.getEmail());
        usuario.setRol("ADMIN");
        usuario.setFoto(u.getFoto());
        usuario.setNombre(u.getNombre());
        usuario.setApellido(u.getApellido());
        usuario.setDocumento(u.getDocumento());
        usuario.setTelefono(u.getTelefono());
        usuario.setDireccion(u.getDireccion());
        usuario.setAltura(u.getAltura());
        usuario.setPiso(u.getPiso());
        usuario.setDepto(u.getDepto());
        usuario.setLocalidad(u.getLocalidad());
        usuario.setProvincia(u.getProvincia());

        usuarioServicio.save(usuario);

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Contraseña modificada correctamente!");

        return "redirect:/administrador/homeAdmin";
    }
}
