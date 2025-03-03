package com.developers.recepcionEquipos.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.developers.recepcionEquipos.entidad.Cliente;
import com.developers.recepcionEquipos.entidad.Orden;

@Repository
public interface OrdenRepositorio extends JpaRepository<Orden, Integer> {

    List<Orden> findByCliente(Cliente cliente);
}
