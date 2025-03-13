package com.developers.recepcionEquipos.servicioImpl;

import org.mindrot.jbcrypt.BCrypt;

public class ContrasenaEncriptadaImpl {
// Método para encriptar una contraseña
    public static String encryptPassword(String plainPassword) {
        // Genera un hash de la contraseña
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Método para verificar si una contraseña coincide con su hash
    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}
