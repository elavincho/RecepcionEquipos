package com.developers.recepcionEquipos.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.developers.recepcionEquipos.entidad.Usuario;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/tecnico")
public class TecnicoControlador {

    

 @GetMapping("/homeTecnico")
    public String homeTecnico(Model model, Usuario usuario, HttpSession session) {
        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        return "tecnico/homeTecnico";
    }

}
