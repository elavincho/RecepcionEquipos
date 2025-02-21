package com.developers.recepcionEquipos.controlador;

import com.developers.recepcionEquipos.entidad.Usuario;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import com.developers.recepcionEquipos.servicioImpl.UploadFileService;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;
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
    public String save(Usuario usuario, @RequestParam("img") MultipartFile file, RedirectAttributes redirectAttributes) throws IOException {
        logger.info("Usuario Registro: {}", usuario);

        usuario.setRol("USER");

        // imagen
        if (usuario.getIdUsuario() == null) { // cuando se crea un usuario
            String nombreFoto = upload.saveImage(file);
            usuario.setFoto(nombreFoto);
        }

        usuarioServicio.save(usuario);

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Usuario registrado correctamente!");

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

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Integer id, Model model, HttpSession session) {

        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario = new Usuario();

        Optional<Usuario> optionalUsuario = usuarioServicio.findByIdUsuario(id);
        usuario = optionalUsuario.get();

        model.addAttribute("usuario", usuario);

        logger.info("Usuario a Editar: {}", usuario);

        return "usuario/editar";
    }

    @PostMapping("/actualizar")
    public String actualizar(Model model, Usuario usuario, @RequestParam("img") MultipartFile file,
            HttpSession session, RedirectAttributes redirectAttributes) throws IOException {

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
        usuario.setRol("USER");

        usuarioServicio.save(usuario);

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

    @GetMapping("/cambiarContrasena/{id}")
    public String cambiarContrasena(@PathVariable Integer id, Model model, HttpSession session) {

        model.addAttribute("sesion", session.getAttribute("idusuario"));

        Usuario usuario = new Usuario();

        Optional<Usuario> optionalUsuario = usuarioServicio.findByIdUsuario(id);
        usuario = optionalUsuario.get();

        model.addAttribute("usuario", usuario);

        logger.info("Usuario a Editar: {}", usuario);

        return "usuario/cambiarContrasena";
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
            return "redirect:/usuario/cambiarContrasena/{id}";
        }

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

        usuarioServicio.save(usuario);

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Contraseña modificada correctamente!");

        return "redirect:/";
    }

    @GetMapping("/linkCambiarContrasena/{id}")
    public String linkCambiarContrasena(@PathVariable Integer id, Model model, HttpSession session) {

        model.addAttribute("sesion", session.getAttribute("idusuario"));

        
        Usuario usuario = new Usuario();

        Optional<Usuario> optionalUsuario = usuarioServicio.findByIdUsuario(id);
        usuario = optionalUsuario.get();

        model.addAttribute("usuario", usuario);

        logger.info("Usuario a Editar: {}", usuario);

        return "usuario/linkCambiarContrasena";
    }

    // @PostMapping("/linkUpdatePassword")
    // public String linkUpdatePassword(Model model, Usuario usuario, @RequestParam String password2,
    //         @RequestParam String password3, HttpSession session)
    //         throws IOException {

    //     model.addAttribute("sesion", session.getAttribute("idusuario"));

      
    //     Usuario u = new Usuario();
    //     u = usuarioServicio.get(usuario.getIdUsuario()).get();
       
       
    //     Cambiamos el password del usuario

    //     if (password2.equals(password3)) {
    //         usuario.setPassword(password3);
    //     } else {
    //         return "usuario/login";
    //     }

    //     Seteamos estos datos para que no se pierdan
    //     usuario.setUsername(u.getUsername());
    //     usuario.setEmail(u.getEmail());
    //     usuario.setTipo(u.getTipo());
    //     usuario.setFoto(u.getFoto());
    //     usuario.setNombre(u.getNombre());
    //     usuario.setApellido(u.getApellido());
    //     usuario.setDocumento(u.getDocumento());
    //     usuario.setTelefono(u.getTelefono());
    //     usuario.setDireccion(u.getDireccion());
    //     usuario.setAltura(u.getAltura());
    //     usuario.setPiso(u.getPiso());
    //     usuario.setDepto(u.getDepto());
    //     usuario.setLocalidad(u.getLocalidad());
    //     usuario.setProvincia(u.getProvincia());

    //     usuarioService.save(usuario);

    //     return "redirect:/usuario/login";
    // }
}
