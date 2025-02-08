package com.developers.recepcionEquipos.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class UsuarioController {
    
    @GetMapping("")
    public String home() {
        return "usuario/home";
    }
    
    @GetMapping("usuario/registro")
    public String registro() {
        return "usuario/registro";
    }
    
    @GetMapping("usuario/iniciarSesion")
    public String iniciarSesion() {
        return "usuario/iniciarSesion";
    }
}
