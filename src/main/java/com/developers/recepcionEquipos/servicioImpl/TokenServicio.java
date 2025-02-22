package com.developers.recepcionEquipos.servicioImpl;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TokenServicio {

    // Mapa para almacenar temporalmente los tokens y sus IDs de usuario asociados
    private final Map<String, Integer> tokenMap = new HashMap<>();

    // Generar un token único y asociarlo al ID del usuario
    public String generarToken(Integer IdUsuario) {
        String token = UUID.randomUUID().toString();
        tokenMap.put(token, IdUsuario);
        return token;
    }

    // Validar el token y obtener el ID del usuario
    public Integer validarToken(String token) {
        return tokenMap.getOrDefault(token, null);
    }

    // Eliminar el token después de su uso (opcional)
    public void eliminarToken(String token) {
        tokenMap.remove(token);
    }
}
