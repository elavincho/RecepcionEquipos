package com.developers.recepcionEquipos.controlador;

import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.developers.recepcionEquipos.entidad.Cliente;
import com.developers.recepcionEquipos.entidad.Equipo;
import com.developers.recepcionEquipos.entidad.Orden;
import com.developers.recepcionEquipos.servicio.ClienteServicio;
import com.developers.recepcionEquipos.servicio.EquipoServicio;
import com.developers.recepcionEquipos.servicio.OrdenServicio;

import jakarta.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/orden")
public class OrdenControlador {

    private final Logger logger = LoggerFactory.getLogger(UsuarioControlador.class);

    @Autowired
    private EquipoServicio equipoServicio;

    @Autowired
    private ClienteServicio clienteServicio;

    @Autowired
    private OrdenServicio ordenServicio;

    // Este método le dice a Spring cómo convertir los valores de tipo String a
    // LocalDate.
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalDate.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.trim().isEmpty()) {
                    // Si el valor es vacío, establecer como nulo
                    setValue(null);
                } else {
                    // Convertir el texto (en formato dd/MM/yyyy) a LocalDate
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate fecha = LocalDate.parse(text, formatter);
                    setValue(fecha);
                }
            }
        });
    }

    // Mapa temporal para almacenar tokens y clienteId (puedes usar una base de
    // datos o caché en producción)
    private Map<String, Integer> tokenMap = new ConcurrentHashMap<>();

    @PostMapping("/altaOrden")
    public String altaOrden(Model model, HttpSession session, @RequestParam Integer clienteId,
            @RequestParam Integer equipoId, RedirectAttributes redirectAttributes) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        System.out.println("EQUIPO ID EN POST ALTA ORDEN" + equipoId);
        // Generar un token
        String token = UUID.randomUUID().toString();

        // Almacenar el token y el clienteId en el mapa temporal
        tokenMap.put(token, clienteId);

        // Redirigir al método GET usando el token
        redirectAttributes.addAttribute("token", token);
        redirectAttributes.addAttribute("equipoId", equipoId);

        // Redirigir a la página de edición
        return "redirect:/orden/altaOrden";
    }

    @GetMapping("/altaOrden")
    public String altaOrden(Model model, HttpSession session, @RequestParam String token,
            @RequestParam Integer equipoId) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        System.out.println("EQUIPO ID EN GET ALTA ORDEN" + equipoId);

        // Verificar si el token existe en el mapa temporal
        Integer clienteId = tokenMap.get(token);
        if (clienteId == null) {
            throw new RuntimeException("Token inválido o expirado");
        }

        // Obtenemos los equipos del cliente por su Id
        Cliente c = clienteServicio.findByIdCliente(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<Equipo> equipos = equipoServicio.findByCliente(c);
        model.addAttribute("equipos", equipos);

        model.addAttribute("equipoId", equipoId);
        model.addAttribute("clienteId", clienteId);
        model.addAttribute("token", token);

        return "orden/altaOrden";
    }

    @PostMapping("/guardarOrden")
    public String guardarOrden(Orden orden, @RequestParam Integer clienteId, @RequestParam Integer equipoId,
            @RequestParam String token, RedirectAttributes redirectAttributes,
            @RequestParam(required = false) Double precioManoObra,
            @RequestParam(required = false) Double precioRepuesto)
            throws IOException {
        logger.info("Usuario Registro: {}", orden);

        System.out.println("ID del Cliente recibido en GUARDAR EQUIPO POST: " + clienteId);
        System.out.println("ID del  EQUIPO recibido en GUARDAR EQUIPO POST: " + equipoId);
        // Asignar el ID del cliente a la Orden
        Cliente c = clienteServicio.findByIdCliente(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        orden.setCliente(c);

        // Asignar el ID del equipo a la Orden
        Equipo e = equipoServicio.findByIdEquipo(equipoId)
                .orElseThrow(() -> new RuntimeException("Equipo no encontrado"));
        orden.setEquipo(e);

        Date fechaCreacion = new Date();
        // Le damos formato a la fecha
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyy HH:mm");
        String formattedDate = sdf.format(fechaCreacion);
        orden.setFechaCreacionFormateada(formattedDate);

        // Guardamos la fecha con formato original y generamos el nro de orden
        orden.setFechaCreacion(fechaCreacion);
        orden.setNumero(ordenServicio.generarNumeroOrden());

        System.out.println("precio repuesto recibido: " + precioRepuesto);
        System.out.println("precio mano de obra recibido: " + precioManoObra);
        // Si los valores son nulos, asigna un valor predeterminado
        if (precioManoObra == null) {
            precioManoObra = 0.0;
        }
        if (precioRepuesto == null) {
            precioRepuesto = 0.0;
        }

        // Calculamos el subTotal, iva, total y los guardamos
        orden.setSubTotal(precioManoObra + precioRepuesto);
        orden.setIva((precioManoObra + precioRepuesto) * 0.21);
        orden.setTotal((precioManoObra + precioRepuesto) + ((precioManoObra + precioRepuesto) * 0.21));
        orden.setEstadoOrden("PENDIENTE");
        
        ordenServicio.save(orden);

        // Limpiar el token después de usarlo (opcional, para evitar reutilización)
        tokenMap.remove(token);

        // Alerta para un guardado correcto
        redirectAttributes.addFlashAttribute("exito", "¡Orden agregada correctamente!");

        return "redirect:/orden/ordenes";
    }

    // Mostrar todos las Ordenes de Reparación
    @GetMapping("/ordenes")
    public String ordenes(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Obtenemos la lista de ordenes
        List<Orden> ordenes = ordenServicio.findAll();

        // Ordenar la lista de mayor a menor por IdOrden
        ordenes.sort((orden1, orden2) -> orden2.getIdOrden().compareTo(orden1.getIdOrden()));

        // Mandamos todos los datos de las ordenes
        model.addAttribute("ordenes", ordenes);

        return "orden/mostrarOrden";
    }

    @PostMapping("/editarOrden")
    public String editarOrden(@RequestParam Integer ordenId, HttpSession session,
            RedirectAttributes redirectAttributes) {

        System.out.println("Sesión ID: " + session.getId()); // Verifica el ID de la sesión

        // Verificar que el ID de la orden es válido
        Optional<Orden> optionalOrden = ordenServicio.get(ordenId);
        if (!optionalOrden.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Orden no encontrada.");
            return "redirect:/orden/ordenes";
        }

        System.out.println("ORDEN ID POST EDITAR ORDEN: " + ordenId);
        // Almacenar el ID de la orden en la sesión
        session.setAttribute("ordenId", ordenId);

        // Redirigir a la página de edición
        return "redirect:/orden/editarOrden";
    }

    @GetMapping("/editarOrden")
    public String editarOrden(Model model, HttpSession session) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        System.out.println("ORDEN session ID GET EDITAR ORDEN: " + session.getAttribute("ordenId"));

        // Obtener el ID de la orden desde la sesión
        Integer idOrden = (Integer) session.getAttribute("ordenId");
        if (idOrden == null) {
            return "redirect:/orden/ordenes";
        }

        System.out.println("ID en sesión: (GET)" + idOrden);

        // Buscar la orden por su ID
        Optional<Orden> optionalOrden = ordenServicio.get(idOrden);
        if (!optionalOrden.isPresent()) {
            return "redirect:/orden/ordenes";
        }
        Orden orden = optionalOrden.get();

        // Generar un token UUID
        String tokenOrden = UUID.randomUUID().toString();
        session.setAttribute("editTokenOrden", tokenOrden);

        // Pasar los datos del cliente y el token a la vista
        model.addAttribute("ordenes", orden);
        model.addAttribute("editTokenOrden", tokenOrden);

        System.out.println("token GET editarOrden: " + tokenOrden);

        // Obtener el ID del equipo desde la sesión
        Integer ordenId = (Integer) session.getAttribute("ordenId");
        System.out.println("ID ORDEN RECIBIDO GET EDITAR CLIENTE: " + ordenId);

        // Pasar los datos del cliente a la vista
        model.addAttribute("ordenId", ordenId);

        return "orden/editarOrden";
    }

    @PostMapping("/actualizarOrden")
    public String actualizarOrden(Model model, Orden orden, HttpSession session, RedirectAttributes redirectAttributes,
            @RequestParam String editTokenOrden, @RequestParam("fechaInicio") String fechaInicioStr,
            @RequestParam Integer ordenId, @RequestParam(required = false) Double precioManoObra,
            @RequestParam(required = false) Double precioRepuesto, BindingResult result)
            throws IOException {

        System.out.println("TOKEN RECIBIDO EN actualizar ORDEN POST " + editTokenOrden);

        // Verificar el token
        String sessionToken = (String) session.getAttribute("editTokenOrden");
        if (sessionToken == null || !sessionToken.equals(editTokenOrden)) {
            //redirectAttributes.addFlashAttribute("error", "Token inválido o expirado.");
            return "redirect:/orden/ordenes";
        }

        // Validar errores
        if (result.hasErrors()) {
            // Si hay errores, regresar al formulario con los mensajes de error
            return "redirect:/orden/ordenes";
        }

        Orden o = new Orden();
        o = ordenServicio.get(ordenId).get();

        // Calculamos el subTotal, iva, total y los guardamos
        // Calculamos el subTotal, iva, total y los guardamos
        orden.setSubTotal(precioManoObra + precioRepuesto);
        orden.setIva((precioManoObra + precioRepuesto) * 0.21);
        orden.setTotal((precioManoObra + precioRepuesto) + ((precioManoObra + precioRepuesto) * 0.21));

        // Guardamos nuevamente estos datos para que no se borren
        orden.setIdOrden(o.getIdOrden());
        orden.setCliente(o.getCliente());
        orden.setEquipo(o.getEquipo());
        orden.setFechaCreacion(o.getFechaCreacion());
        orden.setFechaCreacionFormateada(o.getFechaCreacionFormateada());
        orden.setNumero(o.getNumero());
        orden.setFallaCliente(o.getFallaCliente());
        orden.setMedioAviso(o.getMedioAviso());
        orden.setPrioridad(o.getPrioridad());

        // Actualizar la orden
        ordenServicio.update(orden);

        // Alerta para un cambio correcto
        redirectAttributes.addFlashAttribute("exito", "¡Orden actualizada correctamente!");

        // Eliminamos el token
        session.removeAttribute("editTokenOrden");

        return "redirect:/orden/ordenes";
    }

    // Mostramos todos las ordenes de un cliente
    @PostMapping("/ordenCliente")
    public String ordenCliente(Model model, HttpSession session, @RequestParam Integer clienteId,
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
        return "redirect:/orden/ordenCliente";
    }

    // Mostramos todos las ordenes de un cliente
    @GetMapping("/ordenCliente")
    public String ordenCliente(Model model, HttpSession session, @RequestParam String token,
            RedirectAttributes redirectAttributes) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Verificar si el token existe en el mapa temporal
        Integer clienteId = tokenMap.get(token);
        if (clienteId == null) {
            // throw new RuntimeException("Token inválido o expirado");
            //redirectAttributes.addFlashAttribute("error", "Token inválido o expirado.");
            return "redirect:/orden/ordenes";
        }

        // Limpiar el token después de usarlo (opcional, para evitar reutilización)
        tokenMap.remove(token);

        // Obtenemos los equipos del cliente por su Id
        Cliente c = clienteServicio.findByIdCliente(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        if (clienteId != null) {
            List<Orden> ordenes = ordenServicio.findByCliente(c);
            // Pasamos la lista de equipos del cliente a la vista
            model.addAttribute("ordenes", ordenes);
        }
        return "orden/ordenCliente";
    }

    @PostMapping("/detalleOrden")
    public String detalleOrden(@RequestParam Integer ordenId, HttpSession session,
            RedirectAttributes redirectAttributes) {

        System.out.println("Sesión ID: " + session.getId()); // Verifica el ID de la sesión

        // Verificar que el ID de la orden es válido
        Optional<Orden> optionalOrden = ordenServicio.get(ordenId);
        if (!optionalOrden.isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Orden no encontrada.");
            return "redirect:/orden/ordenes";
        }

        System.out.println("ORDEN ID POST EDITAR ORDEN: " + ordenId);
        // Almacenar el ID de la orden en la sesión
        session.setAttribute("ordenId", ordenId);

        // Redirigir a la página de edición
        return "redirect:/orden/detalleOrden";
    }

    @GetMapping("/detalleOrden")
    public String detalleOrden(Model model, HttpSession session) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        System.out.println("ORDEN session ID GET EDITAR ORDEN: " + session.getAttribute("ordenId"));

        // Obtener el ID de la orden desde la sesión
        Integer idOrden = (Integer) session.getAttribute("ordenId");
        if (idOrden == null) {
            return "redirect:/orden/ordenes";
        }

        System.out.println("ID en sesión: (GET)" + idOrden);

        // Buscar la orden por su ID
        Optional<Orden> optionalOrden = ordenServicio.get(idOrden);
        if (!optionalOrden.isPresent()) {
            return "redirect:/orden/ordenes";
        }
        Orden orden = optionalOrden.get();

        // Generar un token UUID
        String tokenOrden = UUID.randomUUID().toString();
        session.setAttribute("editTokenOrden", tokenOrden);

        // Pasar los datos del cliente y el token a la vista
        model.addAttribute("ordenes", orden);
        model.addAttribute("editTokenOrden", tokenOrden);

        System.out.println("token GET editarOrden: " + tokenOrden);

        // Obtener el ID del equipo desde la sesión
        Integer ordenId = (Integer) session.getAttribute("ordenId");
        System.out.println("ID ORDEN RECIBIDO GET EDITAR CLIENTE: " + ordenId);

        // Pasar los datos del cliente a la vista
        model.addAttribute("ordenId", ordenId);

        return "orden/detalleOrden";
    }

}
