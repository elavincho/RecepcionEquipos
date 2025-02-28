package com.developers.recepcionEquipos.controlador;

import java.io.IOException;
import java.util.Optional;

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

import com.developers.recepcionEquipos.entidad.Equipo;
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

    @GetMapping("/altaEquipo")
    public String altaEquipo(Model model, HttpSession session) {

    // sesion
    model.addAttribute("sesion", session.getAttribute("idusuario"));

    // Con esto obtenemos todos los datos del usuario
    model.addAttribute("usuario", session.getAttribute("usersession"));

    return "equipos/altaEquipo";
    }

     @PostMapping("/guardarEquipo")
    public String guardarEquipo(Equipo equipo, @RequestParam("img") MultipartFile file, @RequestParam String nroSerie,
            RedirectAttributes redirectAttributes)
            throws IOException {
        logger.info("Usuario Registro: {}", equipo);

         //Verificación de un cliente existente
         Optional<Equipo> equipoExistente = equipoServicio.findByNroSerie(nroSerie);
         logger.info("Equipo Exsistente: {}", equipoExistente);

         if (equipoExistente.isPresent()) {
             // Alerta para un equipo existente
             redirectAttributes.addFlashAttribute("error", "¡El equipo ya se encuentra registrado!");
         } else {

             // Imagen cuando se crea un usuario
             if (equipo.getIdEquipo() == null) {
                 String nombreFoto = upload.saveImage(file);
                 equipo.setImagenEquipo(nombreFoto);
             }

             equipoServicio.save(equipo);

             // Alerta para un guardado correcto
             redirectAttributes.addFlashAttribute("exito", "¡Equipo agregado correctamente!");
         }

        return "redirect:/recepcion/homeRecepcion";
    }

    // Mostrar todos los equipos
    @GetMapping("/equipos")
    public String usuarios(Model model, HttpSession session) {

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        // Mandamos todos los datos de los clientes registrados
        model.addAttribute("equipos", equipoServicio.findAll());

        return "equipos/mostrarEquipo";
    }
}
