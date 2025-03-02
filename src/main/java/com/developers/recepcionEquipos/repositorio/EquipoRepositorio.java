package com.developers.recepcionEquipos.repositorio;

import com.developers.recepcionEquipos.entidad.Cliente;
import com.developers.recepcionEquipos.entidad.Equipo;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EquipoRepositorio extends JpaRepository<Equipo, Integer> {
    
    Optional<Equipo> findByNroSerie(String nroSerie);

    List<Equipo> findByCliente(Cliente cliente);
}
