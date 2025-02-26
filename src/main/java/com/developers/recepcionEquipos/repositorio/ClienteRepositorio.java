package com.developers.recepcionEquipos.repositorio;

import com.developers.recepcionEquipos.entidad.Cliente;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer>{
    
    Optional<Cliente> findByEmail(String email);
}
