package com.developers.recepcionEquipos.servicio;

import java.util.List;
import java.util.Optional;

import com.developers.recepcionEquipos.entidad.Cliente;
import com.developers.recepcionEquipos.entidad.Orden;

public interface OrdenServicio {

    Orden save(Orden orden);

    List<Orden> findAll();

    public void Delete(Integer IdOrden);

    public Optional<Orden> get(Integer IdOrden);

    public void update(Orden orden);

    List<Orden>  findByCliente(Cliente cliente);

    String generarNumeroOrden();
}
