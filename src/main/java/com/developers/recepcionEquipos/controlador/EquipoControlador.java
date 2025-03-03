package com.developers.recepcionEquipos.controlador;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

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
import com.developers.recepcionEquipos.entidad.Equipo;
import com.developers.recepcionEquipos.servicio.ClienteServicio;
import com.developers.recepcionEquipos.servicio.EquipoServicio;
import com.developers.recepcionEquipos.servicioImpl.UploadFileService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/equipo")
public class EquipoControlador {

    private final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private EquipoServicio equipoServicio;

    @Autowired
    private UploadFileService upload;

    @Autowired
    private ClienteServicio clienteServicio;

    // Mapa temporal para almacenar tokens y clienteId (puedes usar una base de datos o caché en producción)
    private Map<String, Integer> tokenMap = new ConcurrentHashMap<>();

    @GetMapping("/altaEquipo")
    public String altaEquipo(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Obtener el ID del cliente desde la sesión
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        System.out.println("ID RECIBIDO GET ALTA EQUIPO: " + clienteId);

        // Pasar los datos del cliente a la vista
        model.addAttribute("clienteId", clienteId);

        return "equipos/altaEquipo";
    }

    @PostMapping("/altaEquipo")
    public String altaEquipo(Model model, HttpSession session,
            @RequestParam Integer clienteId, RedirectAttributes redirectAttributes) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Verificar que el ID del cliente es válido
        Optional<Cliente> optionalCliente = clienteServicio.get(clienteId);
        if (!optionalCliente.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
            return "redirect:/recepcion/clientes";
        }

        // Almacenar el ID del cliente en la sesión
        session.setAttribute("clienteId", clienteId);

        // Redirigir a la página de edición
        return "redirect:/equipo/altaEquipo";
    }

    @PostMapping("/guardarEquipo")
    public String guardarEquipo(Equipo equipo, @RequestParam("img") MultipartFile file,
            @RequestParam Integer clienteId, RedirectAttributes redirectAttributes)
            throws IOException {
        logger.info("Usuario Registro: {}", equipo);

        System.out.println("ID del Cliente recibido en GUARDAR EQUIPO POST: " + clienteId);

        // Asignar el ID del cliente al equipo
        Cliente c = clienteServicio.findByIdCliente(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        equipo.setCliente(c);

        // Imagen cuando se crea un equipo
        if (equipo.getIdEquipo() == null) {
            String nombreFoto = upload.saveImage(file);
            equipo.setImagenEquipo(nombreFoto);
        }

        equipoServicio.save(equipo);
        // Alerta para un guardado correcto
        redirectAttributes.addFlashAttribute("exito", "¡Equipo agregado correctamente!");

        return "redirect:/recepcion/homeRecepcion";
    }

    // Mostrar todos los equipos
    @GetMapping("/equipos")
    public String equipos(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Mandamos todos los datos de los equipos registrados
        model.addAttribute("equipos", equipoServicio.findAll());

        return "equipos/mostrarEquipo";
    }

    @GetMapping("/editarEquipo")
    public String editarEquipo(Model model, HttpSession session) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Obtener el ID del equipo desde la sesión
        Integer id = (Integer) session.getAttribute("equipoId");
        if (id == null) {
            return "redirect:/equipos/mostrarEquipo"; // Redirigir si no hay un equipo seleccionado

        }
        System.out.println("ID en sesión: (GET)" + id);
        // Buscar el equipo por su ID
        Optional<Equipo> optionalEquipo = equipoServicio.get(id);
        if (!optionalEquipo.isPresent()) {
            return "redirect:/equipos/mostrarEquipo"; // Redirigir si el equipo no existe
        }
        Equipo equipo = optionalEquipo.get();

        // Generar un token UUID
        String tokenEquipo = UUID.randomUUID().toString();
        session.setAttribute("editToken", tokenEquipo);

        // Pasar los datos del equipo y el token a la vista

        model.addAttribute("equipos", equipo);
        model.addAttribute("editToken", tokenEquipo);

        System.out.println("token enviado: " + tokenEquipo); // Verifica que se almacena correctamente

        // Obtener el ID del cliente desde la sesión
        Integer clienteId = (Integer) session.getAttribute("clienteId");
        System.out.println("ID RECIBIDO GET ALTA EQUIPO: " + clienteId);

        // Pasar los datos del cliente a la vista
        model.addAttribute("clienteId", clienteId);

        return "equipos/editarEquipo";
    }

    @PostMapping("/editarEquipo")
    public String editarEquipo(Model model, @RequestParam Integer id, @RequestParam Integer clienteId,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        System.out.println("ID recibido: " + id);
        System.out.println("Sesión ID: " + session.getId()); // Verifica el ID de la sesión

        // Verificar que el ID del equipo es válido
        Optional<Equipo> optionalEquipo = equipoServicio.get(id);
        if (!optionalEquipo.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Equipo no encontrado.");
            return "redirect:/equipo/mostrarEquipo";
        }

        // Almacenar el ID del equipo en la sesión
        session.setAttribute("equipoId", id);
        System.out.println("ID almacenado en sesión: " + session.getAttribute("equipoId"));

        // Verificar que el ID del cliente es válido
        Optional<Cliente> optionalCliente = clienteServicio.get(clienteId);
        if (!optionalCliente.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Cliente no encontrado.");
            return "redirect:/recepcion/clientes";
        }

        // Almacenar el ID del cliente en la sesión
        session.setAttribute("clienteId", clienteId);

        // Redirigir a la página de edición
        return "redirect:/equipo/editarEquipo";

    }

    @PostMapping("/actualizarEquipo")
    public String actualizarEquipo(Model model, Equipo equipo, @RequestParam("img") MultipartFile file,
            HttpSession session, RedirectAttributes redirectAttributes, @RequestParam String editToken,
            @RequestParam Integer clienteId) throws IOException {

        System.out.println("Token recibido: " + editToken); // Verifica que se almacena correctamente
        // Verificar el token
        String sessionToken = (String) session.getAttribute("editToken");
        if (sessionToken == null || !sessionToken.equals(editToken)) {
            redirectAttributes.addFlashAttribute("error", "Token inválido o expirado.");
            return "redirect:/equipo/equipos";
        }

        Equipo e = new Equipo();
        e = equipoServicio.get(equipo.getIdEquipo()).get();

        /* cuando editamos el cliente pero no cambiamos la imagen */
        if (file.isEmpty()) {
            equipo.setImagenEquipo(e.getImagenEquipo());
        } else {

            /* eliminar cuando no sea la imagen por defecto */
            if (!e.getImagenEquipo().equals("default.jpg")) {
                upload.deleteImage(e.getImagenEquipo());
            }
            String nombreImagen = upload.saveImage(file);
            equipo.setImagenEquipo(nombreImagen);
        }

        // Asignar el ID del cliente al equipo
        Cliente c = clienteServicio.findByIdCliente(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        equipo.setCliente(c);

        // Guardamos nuevamente estos datos para que no se borren
        equipo.setIdEquipo(e.getIdEquipo());
        // Actualizar el equipo
        equipoServicio.save(equipo);

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Equipo actualizado correctamente!");

        // Eliminamos el token
        session.removeAttribute("editToken");

        return "redirect:/equipo/equipos";
    }

    // Mostramos todos los equipos de un cliente
    @PostMapping("/equiposCliente")
    public String equiposCliente(Model model, HttpSession session, @RequestParam Integer clienteId,
            RedirectAttributes redirectAttributes) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Generar un token
        String token = UUID.randomUUID().toString();

        // Almacenar el token y el clienteId en el mapa temporal
        tokenMap.put(token, clienteId);

        // Redirigir al método GET usando el token
        redirectAttributes.addAttribute("token", token);

        // Redirigir a la página de edición
        return "redirect:/equipo/equiposCliente";
    }

    // Mostramos todos los equipos de un cliente
    @GetMapping("/equiposCliente")
    public String equiposCliente(Model model, HttpSession session, @RequestParam String token) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Verificar si el token existe en el mapa temporal
        Integer clienteId = tokenMap.get(token);
        if (clienteId == null) {
            throw new RuntimeException("Token inválido o expirado");
        }

        // Limpiar el token después de usarlo (opcional, para evitar reutilización)
        tokenMap.remove(token);

        // Obtenemos los equipos del cliente por su Id
        Cliente c = clienteServicio.findByIdCliente(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (clienteId != null) {
            List<Equipo> equipos = equipoServicio.findByCliente(c);
            // Pasamos la lista de equipos del cliente a la vista
            model.addAttribute("equipos", equipos);
        }
        return "equipos/equiposCliente";
    }

}
