package com.developers.recepcionEquipos.repositorio;

import com.developers.recepcionEquipos.entidad.Usuario;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByEmail(String email);
}
