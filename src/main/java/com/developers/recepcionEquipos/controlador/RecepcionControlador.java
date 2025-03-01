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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.developers.recepcionEquipos.entidad.Cliente;
import com.developers.recepcionEquipos.entidad.Usuario;
import com.developers.recepcionEquipos.servicio.ClienteServicio;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import com.developers.recepcionEquipos.servicioImpl.UploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/recepcion")
public class RecepcionControlador {

    private final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private UploadFileService upload;

    @GetMapping("/homeRecepcion")
    public String homeRecepcion(Usuario usuario, Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        return "recepcion/homeRecepcion";
    }

    // Metodo editar con token
    @GetMapping("/editarRecepcion")
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

        return "recepcion/editarRecepcion";
    }

    // Metodo actualizar con token
    @PostMapping("/actualizarRecepcion")
    public String actualizarRecepcion(Model model, Usuario usuario, @RequestParam("img") MultipartFile file,
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
            return "recepcion/homeRecepcion";
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
        usuario.setRol("RECEPCIONISTA");

        usuarioServicio.save(usuario);

        // Eliminar el token de la sesión después de la actualización
        session.removeAttribute("editToken");

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Perfil editado correctamente!");

        return "redirect:/recepcion/homeRecepcion";
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

        return "recepcion/cambiarContrasena";
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
            return "redirect:/recepcion/homeRecepcion";
        }

        // Obtener el ID del usuario desde la sesión
        Integer idUsuario = (Integer) session.getAttribute("idusuario");

        // Obtener el usuario desde la base de datos
        Usuario u = usuarioServicio.findByIdUsuario(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificamos la contraseña actual del usuario
        if (!u.getContrasena().equals(actualContrasena)) {
            redirectAttributes.addFlashAttribute("error", "¡Contraseña actual incorrecta!");
            return "redirect:/recepcion/cambiarContrasena";
        }

        // Verificamos que las nuevas contraseñas coincidan
        if (!contrasena.equals(password2)) {
            redirectAttributes.addFlashAttribute("error", "¡Las contraseñas no coinciden!");
            return "redirect:/recepcion/cambiarContrasena";
        }

        // Actualizar la contraseña
        usuario.setContrasena(contrasena);

        // Seteamos estos datos para que no se pierdan
        usuario.setIdUsuario(u.getIdUsuario());
        usuario.setNombreUsuario(u.getNombreUsuario());
        usuario.setEmail(u.getEmail());
        usuario.setRol("RECEPCIONISTA");
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

        return "redirect:/recepcion/homeRecepcion";
    }

    @GetMapping("/altaCliente")
    public String altaCliente(Usuario usuario, Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        return "clientes/alta";
    }

    @PostMapping("/guardarCliente")
    public String guardarCliente(Cliente cliente, @RequestParam("img") MultipartFile file, @RequestParam String email,
            RedirectAttributes redirectAttributes)
            throws IOException {
        logger.info("Usuario Registro: {}", cliente);

        // Verificación de un cliente existente
        Optional<Cliente> clienteExistente = clienteServicio.findByEmail(email);
        logger.info("Cliente Exsistente: {}", clienteExistente);

        if (clienteExistente.isPresent()) {
            // Alerta para un cliente existente
            redirectAttributes.addFlashAttribute("error", "¡El cliente ya se encuentra registrado!");
        } else {

            // Imagen cuando se crea un usuario
            if (cliente.getIdCliente() == null) {
                String nombreFoto = upload.saveImage(file);
                cliente.setFoto(nombreFoto);
            }

            clienteServicio.save(cliente);

            // Alerta para un cambio correcto
            redirectAttributes.addFlashAttribute("exito", "¡Cliente agregado correctamente!");
        }

        return "redirect:/recepcion/homeRecepcion";
    }

    // Mostrar todos los clientes registrados
    @GetMapping("/clientes")
    public String usuarios(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Mandamos todos los datos de los clientes registrados
        model.addAttribute("clientes", clienteServicio.findAll());

        return "clientes/mostrar";
    }

    @PostMapping("/editarCliente")
    public String editarCliente(@RequestParam Integer id, HttpSession session,
            RedirectAttributes redirectAttributes) {

        // Verificar que el ID del cliente es válido
        Optional<Cliente> optionalCliente = clienteServicio.get(id);
        if (!optionalCliente.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
            return "redirect:/recepcion/clientes";
        }

        // Almacenar el ID del cliente en la sesión
        session.setAttribute("clienteId", id);

        // Redirigir a la página de edición
        return "redirect:/recepcion/editarCliente";
    }

    @GetMapping("/editarCliente")
    public String editarCliente(Model model, HttpSession session) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Obtener el ID del cliente desde la sesión
        Integer id = (Integer) session.getAttribute("clienteId");
        if (id == null) {
            return "redirect:/recepcion/clientes"; // Redirigir si no hay un cliente seleccionado
        }

        // Buscar el cliente por su ID
        Optional<Cliente> optionalCliente = clienteServicio.get(id);
        if (!optionalCliente.isPresent()) {
            return "redirect:/recepcion/clientes"; // Redirigir si el cliente no existe
        }
        Cliente cliente = optionalCliente.get();

        // Generar un token UUID
        String token = UUID.randomUUID().toString();
        session.setAttribute("editToken", token);

        // Pasar los datos del cliente y el token a la vista
        model.addAttribute("clientes", cliente);
        model.addAttribute("editToken", token);

        return "clientes/editar";
    }

    @PostMapping("/actualizarCliente")
    public String actualizarCliente(Model model, Cliente cliente, @RequestParam("img") MultipartFile file,
            HttpSession session, RedirectAttributes redirectAttributes, @RequestParam String editToken)
            throws IOException {

        // Verificar el token
        String sessionToken = (String) session.getAttribute("editToken");
        if (sessionToken == null || !sessionToken.equals(editToken)) {
            redirectAttributes.addFlashAttribute("error", "Token inválido o expirado.");
            return "redirect:/recepcion/clientes";
        }

        /* cuando editamos el cliente pero no cambiamos la imagen */
        Cliente c = new Cliente();
        c = clienteServicio.get(cliente.getIdCliente()).get();

        /* cuando editamos el cliente pero no cambiamos la imagen */
        if (file.isEmpty()) {
            cliente.setFoto(c.getFoto());
        } else {

            /* eliminar cuando no sea la imagen por defecto */
            if (!c.getFoto().equals("default.jpg")) {
                upload.deleteImage(c.getFoto());
            }
            String nombreImagen = upload.saveImage(file);
            cliente.setFoto(nombreImagen);
        }

        // Guardamos nuevamente estos datos para que no se borren
        cliente.setEmail(c.getEmail());

        // Actualizar el cliente
        clienteServicio.update(cliente);

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Cliente actualizado correctamente!");

        // Eliminamos el token
        session.removeAttribute("editToken");

        return "redirect:/recepcion/clientes";
    }

}
