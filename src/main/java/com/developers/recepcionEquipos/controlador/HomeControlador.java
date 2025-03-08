package com.developers.recepcionEquipos.controlador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class HomeControlador {

    private final Logger logger = LoggerFactory.getLogger(HomeControlador.class);

    @GetMapping("")
    public String home(Model model, HttpSession session) {

        logger.info("Sesion del usuario: {}", session.getAttribute("idusuario"));

        logger.info("Todos los datos del usuario: {}", session.getAttribute("usersession"));

        // sesion
        model.addAttribute("sesion", session.getAttribute("idusuario"));

        // Con esto obtenemos todos los datos del usuario
        model.addAttribute("usuario", session.getAttribute("usersession"));

        return "usuario/home";
    }
}
