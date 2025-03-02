package com.developers.recepcionEquipos.servicioImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.developers.recepcionEquipos.entidad.Orden;
import com.developers.recepcionEquipos.repositorio.OrdenRepositorio;
import com.developers.recepcionEquipos.servicio.OrdenServicio;

@Service
public class OrdenServicioImpl implements OrdenServicio {

    @Autowired
    private OrdenRepositorio ordenRepositorio;

    @Override
    public Orden save(Orden orden) {
        return ordenRepositorio.save(orden);
    }

    @Override
    public List<Orden> findAll() {
        return ordenRepositorio.findAll();
    }

    @Override
    public void Delete(Integer IdOrden) {
        ordenRepositorio.deleteById(IdOrden);
    }

    @Override
    public Optional<Orden> get(Integer IdOrden) {
        return ordenRepositorio.findById(IdOrden);
    }

    @Override
    public void update(Orden orden) {
        ordenRepositorio.save(orden);
    }

   

}
