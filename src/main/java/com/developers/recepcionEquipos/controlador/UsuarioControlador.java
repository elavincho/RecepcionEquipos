package com.developers.recepcionEquipos.controlador;

import com.developers.recepcionEquipos.entidad.Usuario;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import com.developers.recepcionEquipos.servicioImpl.UploadFileService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.*;

@Controller
@RequestMapping("/usuario")
public class UsuarioControlador {

    private final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UploadFileService upload;

    @GetMapping("/registro")
    public String registro(Model model) {
        return "usuario/registro";
    }

    @PostMapping("/save")
    public String save(Usuario usuario, @RequestParam("img") MultipartFile file, @RequestParam String email,
            RedirectAttributes redirectAttributes)
            throws IOException {
        logger.info("Usuario Registro: {}", usuario);

        // Verificación de un usuario existente
        Optional<Usuario> usuarioExistente = usuarioServicio.findByEmail(email);
        logger.info("Usuario Exsistente: {}", usuarioExistente);
        if (usuarioExistente.isPresent()) {
            // Alerta para un usuario existente
            redirectAttributes.addFlashAttribute("error", "¡El Usuario ya se encuentra registrado!");
        } else {
            usuario.setRol("USER");

            // imagen
            if (usuario.getIdUsuario() == null) { // cuando se crea un usuario
                String nombreFoto = upload.saveImage(file);
                usuario.setFoto(nombreFoto);
            }

            usuarioServicio.save(usuario);

            // Alerta para un registro exitoso
            redirectAttributes.addFlashAttribute("exito", "¡Usuario registrado correctamente!");
        }

        return "redirect:/usuario/iniciarSesion";
    }

    @GetMapping("/iniciarSesion")
    public String iniciarSesion() {
        return "usuario/iniciarSesion";
    }

    @PostMapping("/acceder")
    public String acceder(Usuario usuario, HttpSession session, RedirectAttributes redirectAttributes) {

        logger.info("Accesos : {}", usuario);

        Optional<Usuario> user = usuarioServicio.findByEmail(usuario.getEmail());

        // Verificamos si el usuario existe en la base de datos
        if (!user.isPresent()) {
            logger.info("Usuario no existe");

            // Alerta para email no registrado
            redirectAttributes.addFlashAttribute("error", "¡El correo electrónico no está registrado!");
            return "redirect:/usuario/iniciarSesion";
        }

        logger.info("Usuario de la bd: {}", user.get());

        // Validación de la contraseña
        if (user.get().getContrasena().equals(usuario.getContrasena())) {

            // Obtenemos el id del usuario para usarlo en cualquier lugar de la app
            session.setAttribute("idusuario", user.get().getIdUsuario());

            // Obtenemos todos los datos del usuario para usarlo en cualquier lugar de la
            // app
            session.setAttribute("usersession", user.get());

            if (user.get().getRol().equals("ADMIN")) {
                return "redirect:/administrador/homeAdmin";

            } else if (user.get().getRol().equals("USER")) {
                return "redirect:/";

            } else if (user.get().getRol().equals("BLOQUEADO")) {
                return "redirect:/usuario/bloqueado";
            }
        } else {
            logger.info("Contraseña incorrecta");

            // Alerta para contraseña incorrecta
            redirectAttributes.addFlashAttribute("error", "¡Contraseña incorrecta!");
            return "redirect:/usuario/iniciarSesion";
        }

        return "redirect:/usuario/iniciarSesion";
    }

    // Metodo editar con token
    @GetMapping("/editar")
    public String editar(Model model, HttpSession session) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

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

        return "usuario/editar";
    }

    // Metodo actualizar con token
    @PostMapping("/actualizar")
    public String actualizar(Model model, Usuario usuario, @RequestParam("img") MultipartFile file,
            HttpSession session, RedirectAttributes redirectAttributes, @RequestParam("editToken") String editToken)
            throws IOException {

        // Validar el token de edición
        String sessionToken = (String) session.getAttribute("editToken");
        if (sessionToken == null || !sessionToken.equals(editToken)) {
            redirectAttributes.addFlashAttribute("error", "Token de edición inválido");
            return "redirect:/";
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
        usuario.setRol("USER");

        usuarioServicio.save(usuario);

        // Eliminar el token de la sesión después de la actualización
        session.removeAttribute("editToken");

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Perfil editado correctamente!");

        return "redirect:/";
    }

    @GetMapping("/cerrar")
    public String cerrarSesion(HttpSession session) {
        session.removeAttribute("idusuario");
        session.removeAttribute("usersession");
        return "redirect:/";
    }

    @GetMapping("/bloqueado")
    public String bloqueado() {
        return "usuario/bloqueado";
    }

    // Cambiar contraseña con token
    @GetMapping("/cambiarContrasena")
    public String cambiarContrasena(Model model, HttpSession session) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Obtener el ID del usuario desde la sesión
        Integer idUsuario = (Integer) session.getAttribute("idusuario");

        // Generar un UUID único
        String uuid = UUID.randomUUID().toString();

        // Almacenar el UUID en la sesión
        session.setAttribute("tokenCambioContrasena", uuid);

        // Obtener el usuario desde la base de datos
        Usuario usuario = usuarioServicio.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Agregar el usuario y el UUID al modelo
        model.addAttribute("usuario", usuario);
        model.addAttribute("tokenCambioContrasena", uuid);

        logger.info("Usuario a cambiar contraseña: {}", usuario);

        return "usuario/cambiarContrasena";
    }

    // Actualizar contraseña con token
    @PostMapping("/actualizarContrasena")
    public String actualizarContrasena(Model model, Usuario usuario,
            @RequestParam String actualContrasena,
            @RequestParam String contrasena, @RequestParam String password2,
            @RequestParam String tokenCambioContrasena, HttpSession session,
            RedirectAttributes redirectAttributes) throws IOException {

        // Validar el token de cambio de contraseña
        String sessionToken = (String) session.getAttribute("tokenCambioContrasena");
        if (sessionToken == null || !sessionToken.equals(tokenCambioContrasena)) {
            redirectAttributes.addFlashAttribute("error", "Token de cambio de contraseña inválido");
            return "redirect:/";
        }

        // Obtener el ID del usuario desde la sesión
        Integer idUsuario = (Integer) session.getAttribute("idusuario");

        // Obtener el usuario desde la base de datos
        Usuario u = usuarioServicio.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificamos la contraseña actual del usuario
        if (!u.getContrasena().equals(actualContrasena)) {
            redirectAttributes.addFlashAttribute("error", "¡Contraseña actual incorrecta!");
            return "redirect:/usuario/cambiarContrasena";
        }

        // Verificamos que las nuevas contraseñas coincidan
        if (!contrasena.equals(password2)) {
            redirectAttributes.addFlashAttribute("error", "¡Las contraseñas no coinciden!");
            return "redirect:/usuario/cambiarContrasena";
        }

        // Actualizar la contraseña
        usuario.setContrasena(contrasena);

        // Seteamos estos datos para que no se pierdan
        usuario.setIdUsuario(u.getIdUsuario());
        usuario.setNombreUsuario(u.getNombreUsuario());
        usuario.setEmail(u.getEmail());
        usuario.setRol("USER");
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

        // Guardar el usuario actualizado
        usuarioServicio.save(usuario);

        // Eliminar el token de la sesión después de la actualización
        session.removeAttribute("tokenCambioContrasena");

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Contraseña modificada correctamente!");

        return "redirect:/";
    }

}
