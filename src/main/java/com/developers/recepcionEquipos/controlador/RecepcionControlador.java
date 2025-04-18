package com.developers.recepcionEquipos.controlador;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
import com.developers.recepcionEquipos.servicio.OrdenServicio;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import com.developers.recepcionEquipos.servicioImpl.ContrasenaEncriptadaImpl;
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
    private OrdenServicio ordenServicio;

    @Autowired
    private UploadFileService upload;

    @GetMapping("/homeRecepcion")
    public String homeRecepcion(Usuario usuario, Model model, HttpSession session) {

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

        return "recepcion/homeRecepcion";
    }

    @GetMapping("/perfilRecepcion")
    public String perfilRecepcion(Usuario usuario, Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        return "recepcion/perfilRecepcion";
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
        if (!ContrasenaEncriptadaImpl.checkPassword(actualContrasena, u.getContrasena())) {
            redirectAttributes.addFlashAttribute("error", "¡Contraseña actual incorrecta!");
            return "redirect:/recepcion/cambiarContrasena";
        }

        // Verificamos que las nuevas contraseñas coincidan
        if (!contrasena.equals(password2)) {
            redirectAttributes.addFlashAttribute("error", "¡Las contraseñas no coinciden!");
            return "redirect:/recepcion/cambiarContrasena";
        }

        // Encriptar la nueva contraseña antes de guardarla
        String nuevaContrasenaEncriptada = ContrasenaEncriptadaImpl.encryptPassword(contrasena);

        // Actualizar la contraseña en el objeto usuario
        usuario.setContrasena(nuevaContrasenaEncriptada);

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

        return "redirect:/recepcion/clientes";
    }

    // Mostrar todos los clientes registrados
    @GetMapping("/clientes")
    public String clientes(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Obtenemos la lista de clientes
        List<Cliente> clientes = clienteServicio.findAll();

        // Ordenar la lista de mayor a menor por IdCliente
        clientes.sort((cliente1, cliente2) -> cliente2.getIdCliente().compareTo(cliente1.getIdCliente()));

        // Mandamos todos los datos de los clientes registrados
        model.addAttribute("clientes", clientes);

        return "clientes/mostrar";
    }

    @PostMapping("/editarCliente")
    public String editarCliente(@RequestParam Integer clienteId, HttpSession session,
            RedirectAttributes redirectAttributes) {

        System.out.println("ID recibido: " + clienteId);
        System.out.println("Sesión ID: " + session.getId()); // Verifica el ID de la sesión

        // Verificar que el ID del cliente es válido
        Optional<Cliente> optionalCliente = clienteServicio.get(clienteId);
        if (!optionalCliente.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
            return "redirect:/recepcion/clientes";
        }

        System.out.println("CLIENTE ID POST EDITAR CLIENTE: " + clienteId);
        // Almacenar el ID del cliente en la sesión
        session.setAttribute("clienteId", clienteId);

        // Redirigir a la página de edición
        return "redirect:/recepcion/editarCliente";
    }

    @GetMapping("/editarCliente")
    public String editarCliente(Model model, HttpSession session) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        System.out.println("CLIENTE session ID GET EDITAR CLIENTE: " + session.getAttribute("clienteId"));

        // Obtener el ID del cliente desde la sesión
        Integer id = (Integer) session.getAttribute("clienteId");
        if (id == null) {
            return "redirect:/recepcion/clientes"; // Redirigir si no hay un cliente seleccionado
        }

        System.out.println("ID en sesión: (GET)" + id);

        // Buscar el cliente por su ID
        Optional<Cliente> optionalCliente = clienteServicio.get(id);
        if (!optionalCliente.isPresent()) {
            return "redirect:/recepcion/clientes"; // Redirigir si el cliente no existe
        }
        Cliente cliente = optionalCliente.get();

        // Generar un token UUID
        String tokenCliente = UUID.randomUUID().toString();
        session.setAttribute("editToken", tokenCliente);

        // Pasar los datos del cliente y el token a la vista
        model.addAttribute("clientes", cliente);
        model.addAttribute("editToken", tokenCliente);

        System.out.println("token GET editarCliente: " + tokenCliente);

        // Obtener el ID del cliente desde la sesión
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        System.out.println("ID RECIBIDO GET EDITAR CLIENTE: " + clienteId);

        // Pasar los datos del cliente a la vista
        // model.addAttribute("clienteId", clienteId); // es necesario, se obtiene
        // directamente

        return "clientes/editar";
    }

    @PostMapping("/actualizarCliente")
    public String actualizarCliente(Model model, Cliente cliente, @RequestParam("img") MultipartFile file,
            HttpSession session, RedirectAttributes redirectAttributes, @RequestParam String editToken,
            @RequestParam Integer clienteId)
            throws IOException {

        System.out.println("TOKEN RECIBIDO EN actualizar cliente POST " + editToken);

        // Verificar el token
        String sessionToken = (String) session.getAttribute("editToken");
        if (sessionToken == null || !sessionToken.equals(editToken)) {
            redirectAttributes.addFlashAttribute("error", "Token inválido o expirado.");
            return "redirect:/recepcion/clientes";
        }

        Cliente c = new Cliente();

        c = clienteServicio.get(clienteId).get();

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
        cliente.setIdCliente(c.getIdCliente());

        // Actualizar el cliente
        clienteServicio.update(cliente);

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Cliente actualizado correctamente!");

        // Eliminamos el token
        session.removeAttribute("editToken");

        return "redirect:/recepcion/clientes";
    }

}
