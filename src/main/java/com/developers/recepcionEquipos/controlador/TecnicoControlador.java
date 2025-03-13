package com.developers.recepcionEquipos.controlador;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.developers.recepcionEquipos.entidad.Usuario;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import com.developers.recepcionEquipos.servicioImpl.ContrasenaEncriptadaImpl;
import com.developers.recepcionEquipos.servicioImpl.UploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tecnico")
public class TecnicoControlador {

    private final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private UsuarioServicio usuarioServicio;

    // @Autowired
    // private ClienteServicio clienteServicio;

    @Autowired
    private UploadFileService upload;

    @GetMapping("/homeTecnico")
    public String homeTecnico(Model model, Usuario usuario, HttpSession session) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        return "tecnico/homeTecnico";
    }

    // Metodo editar con token
    @GetMapping("/editarTecnico")
    public String editarTecnico(Model model, HttpSession session) {
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

        return "tecnico/editarTecnico";
    }

    // Metodo actualizar con token
    @PostMapping("/actualizarTecnico")
    public String actualizarTecnico(Model model, Usuario usuario, @RequestParam("img") MultipartFile file,
            HttpSession session, RedirectAttributes redirectAttributes, @RequestParam String editToken)
            throws IOException {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Validar el token de edición
        String sessionToken = (String) session.getAttribute("editToken");
        if (sessionToken == null || !sessionToken.equals(editToken)) {
            redirectAttributes.addFlashAttribute("error", "Token de edición inválido");
            return "tecnico/homeTecnico";
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
        usuario.setRol("TECNICO");

        usuarioServicio.save(usuario);

        // Eliminar el token de la sesión después de la actualización
        session.removeAttribute("editToken");

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Perfil editado correctamente!");

        return "redirect:/tecnico/homeTecnico";
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

        return "tecnico/cambiarContrasena";
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
            return "redirect:/tecnico/homeTecnico";
        }

        // Obtener el ID del usuario desde la sesión
        Integer idUsuario = (Integer) session.getAttribute("idusuario");

        // Obtener el usuario desde la base de datos
        Usuario u = usuarioServicio.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificamos la contraseña actual del usuario
        if (!ContrasenaEncriptadaImpl.checkPassword(actualContrasena, u.getContrasena())) {
            redirectAttributes.addFlashAttribute("error", "¡Contraseña actual incorrecta!");
            return "redirect:/tecnico/cambiarContrasena";
        }

        // Verificamos que las nuevas contraseñas coincidan
        if (!contrasena.equals(password2)) {
            redirectAttributes.addFlashAttribute("error", "¡Las contraseñas no coinciden!");
            return "redirect:/tecnico/cambiarContrasena";
        }

        // Encriptar la nueva contraseña antes de guardarla
        String nuevaContrasenaEncriptada = ContrasenaEncriptadaImpl.encryptPassword(contrasena);

        // Actualizar la contraseña en el objeto usuario
        usuario.setContrasena(nuevaContrasenaEncriptada);

        // Seteamos estos datos para que no se pierdan
        usuario.setIdUsuario(u.getIdUsuario());
        usuario.setNombreUsuario(u.getNombreUsuario());
        usuario.setEmail(u.getEmail());
        usuario.setRol("TECNICO");
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

        return "redirect:/tecnico/homeTecnico";
    }

}
