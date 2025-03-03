package com.developers.recepcionEquipos.controlador;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.developers.recepcionEquipos.entidad.Cliente;
import com.developers.recepcionEquipos.entidad.Equipo;
import com.developers.recepcionEquipos.entidad.Orden;
import com.developers.recepcionEquipos.servicio.ClienteServicio;
import com.developers.recepcionEquipos.servicio.EquipoServicio;
import com.developers.recepcionEquipos.servicio.OrdenServicio;

import jakarta.servlet.http.HttpSession;

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

        // Limpiar el token después de usarlo (opcional, para evitar reutilización)
        tokenMap.remove(token);

        // Obtenemos los equipos del cliente por su Id
        Cliente c = clienteServicio.findByIdCliente(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        List<Equipo> equipos = equipoServicio.findByCliente(c);
        model.addAttribute("equipos", equipos);

        model.addAttribute("equipoId", equipoId);
        model.addAttribute("clienteId", clienteId); // Asegúrate de pasar el clienteId
        model.addAttribute("token", token); // Asegúrate de pasar el token

        return "orden/altaOrden";
    }

    @PostMapping("/guardarOrden")
    public String guardarOrden(Orden orden, @RequestParam Integer clienteId, @RequestParam Integer equipoId,
            @RequestParam String token, RedirectAttributes redirectAttributes)
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

        ordenServicio.save(orden);

        // Alerta para un guardado correcto
        redirectAttributes.addFlashAttribute("exito", "¡Orden agregada correctamente!");

        return "redirect:/recepcion/homeRecepcion";
    }

    // Mostrar todos las Ordenes de Reparación
    @GetMapping("/ordenes")
    public String ordenes(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Mandamos todos los datos de las ordenes
        model.addAttribute("ordenes", ordenServicio.findAll());

        return "orden/mostrarOrden";
    }

}
