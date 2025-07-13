package com.developers.recepcionEquipos.controlador;

import java.io.IOException;
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
import com.developers.recepcionEquipos.servicio.ClienteServicio;
import com.developers.recepcionEquipos.servicio.OrdenServicio;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import com.developers.recepcionEquipos.servicioImpl.ContrasenaEncriptadaImpl;
import com.developers.recepcionEquipos.servicioImpl.UploadFileService;

import jakarta.servlet.http.HttpSession;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/administrador")
public class AdministradorControlador {

    private final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private UploadFileService upload;

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private OrdenServicio ordenServicio;

    @GetMapping("/homeAdmin")
    public String homeAdmin(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Pasamos la cantidad de clientes a homeRecepcion
        long cantidadClientes = clienteServicio.obtenerCantidadClientes();
        model.addAttribute("cantidadClientes", cantidadClientes);

        // Obtenemos el conteo de ordenes y los pasamos a homeRecepcion
        Map<String, Long> ordenConteo = ordenServicio.contarOrdenesPorEstado();
        model.addAttribute("ordenConteo", ordenConteo);

        // Obtenemos el conteo de avisos a los clientes y los pasamos a homeRecepcion
        Map<String, Long> ordenAviso = ordenServicio.contarOrdenesPorAvisoCliente();
        model.addAttribute("ordenAviso", ordenAviso);

        return "administrador/homeAdmin";
    }

    @GetMapping("/registroAdmin")
    public String registroAdmin(Model model) {
        return "administrador/registroAdmin";
    }

    @PostMapping("/saveAdmin")
    public String saveAdmin(Usuario usuario, @RequestParam("img") MultipartFile file, @RequestParam String email,
            @RequestParam String autorizacion, @RequestParam String contrasena,
            RedirectAttributes redirectAttributes)
            throws IOException {
        logger.info("Usuario Registro: {}", usuario);

        // Verificación de Autorización
        if (autorizacion.equals("admin")) {

            // Verificación de un usuario existente
            Optional<Usuario> usuarioExistente = usuarioServicio.findByEmail(email);
            logger.info("Usuario Exsistente: {}", usuarioExistente);
            if (usuarioExistente.isPresent()) {
                // Alerta para un usuario existente
                redirectAttributes.addFlashAttribute("error", "¡El Usuario ya se encuentra registrado!");
            } else {
                // Encripta la contraseña antes de almacenarla
                String hashedPassword = ContrasenaEncriptadaImpl.encryptPassword(contrasena);
                // Guardamos la contraseña encriptada
                usuario.setContrasena(hashedPassword);

                usuario.setRol("ADMIN");

                // Imagen cuando se crea un usuario
                if (usuario.getIdUsuario() == null) {
                    String nombreFoto = upload.saveImage(file);
                    usuario.setFoto(nombreFoto);
                }

                usuarioServicio.save(usuario);

                // Alerta para un cambio correcto
                redirectAttributes.addFlashAttribute("exito", "¡Administrador agregado correctamente!");
            }

        } else {
            // Alerta Autorización Denegada
            redirectAttributes.addFlashAttribute("error", "¡Autorización Denegada!");
            return "redirect:/usuario/iniciarSesion";
        }

        return "redirect:/usuario/iniciarSesion";
    }

    // Metodo editarAdmin con token
    @GetMapping("/editarAdmin")
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

        return "administrador/editarAdmin";
    }

    // Metodo actualizar con token
    @PostMapping("/actualizarAdmin")
    public String actualizar(Model model, Usuario usuario, @RequestParam("img") MultipartFile file,
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

        return "administrador/cambiarContrasena";
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
            return "redirect:/administrador/cambiarContrasena";
        }

        // Obtener el ID del usuario desde la sesión
        Integer idUsuario = (Integer) session.getAttribute("idusuario");

        // Obtener el usuario desde la base de datos
        Usuario u = usuarioServicio.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificamos la contraseña actual del usuario
        if (!ContrasenaEncriptadaImpl.checkPassword(actualContrasena, u.getContrasena())) {
            redirectAttributes.addFlashAttribute("error", "¡Contraseña actual incorrecta!");
            return "redirect:/administrador/cambiarContrasena";
        }

        // Verificamos que las nuevas contraseñas coincidan
        if (!contrasena.equals(password2)) {
            redirectAttributes.addFlashAttribute("error", "¡Las contraseñas no coinciden!");
            return "redirect:/administrador/cambiarContrasena";
        }

        // Encriptar la nueva contraseña antes de guardarla
        String nuevaContrasenaEncriptada = ContrasenaEncriptadaImpl.encryptPassword(contrasena);

        // Actualizar la contraseña en el objeto usuario
        usuario.setContrasena(nuevaContrasenaEncriptada);

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

        // Guardar el usuario actualizado
        usuarioServicio.save(usuario);

        // Eliminar el token de la sesión después de la actualización
        session.removeAttribute("tokenCambioContrasena");

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Contraseña modificada correctamente!");

        return "redirect:/administrador/homeAdmin";
    }

    // Mostrar todos los usuarios registrados
    @GetMapping("/usuarios")
    public String usuarios(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Mandamos todos los datos de los usuarios registrados
        model.addAttribute("usuarios", usuarioServicio.findAll());

        return "administrador/usuarios";
    }

    @GetMapping("/editarRol/{id}")
    public String editarRol(@PathVariable Integer id, Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario = new Usuario();

        Optional<Usuario> optionalUsuario = usuarioServicio.findByIdUsuario(id);
        usuario = optionalUsuario.get();

        model.addAttribute("usuario", usuario);

        logger.info("Usuario a Editar: {}", usuario);

        return "administrador/cambiarRol";
    }

    @PostMapping("/actualizarRol")
    public String actualizarRol(Model model, Usuario usuario, @RequestParam("img") MultipartFile file,
            @RequestParam Integer IdUsuario, @RequestParam String rol, HttpSession session) throws IOException {

        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario u = new Usuario();
        u = usuarioServicio.findByIdUsuario(IdUsuario).get();

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
        usuario.setRol(rol);

        usuarioServicio.save(usuario);

        return "redirect:/administrador/usuarios";
    }

    // Mostrar todos los usuarios registrados
    @GetMapping("/tecnicos")
    public String tecnicos(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Mandamos todos los datos de los usuarios registrados
        model.addAttribute("usuarios", usuarioServicio.findAll());

        return "administrador/tecnicos";
    }

}
