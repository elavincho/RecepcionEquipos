package com.developers.recepcionEquipos.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/administrador")
public class AdministradorControlador {
    

    @GetMapping("/homeAdministrador")
    public String homeAdministrador() {
        return "administrador/homeAdministrador";
    }
}
