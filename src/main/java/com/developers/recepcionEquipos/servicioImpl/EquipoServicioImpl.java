package com.developers.recepcionEquipos.servicioImpl;

import com.developers.recepcionEquipos.entidad.Equipo;
import com.developers.recepcionEquipos.repositorio.EquipoRepositorio;
import com.developers.recepcionEquipos.servicio.EquipoServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipoServicioImpl implements EquipoServicio{
    
    @Autowired
    private EquipoRepositorio equipoRepositorio;

    @Override
    public Equipo save(Equipo equipo) {
        return equipoRepositorio.save(equipo);
    }

    @Override
    public Optional<Equipo> findByNroSerie(String nroSerie) {
        return equipoRepositorio.findByNroSerie(nroSerie);
    }

    @Override
    public List<Equipo> findAll() {
        return equipoRepositorio.findAll();
    }

    @Override
    public void Delete(Integer IdEquipo) {
        equipoRepositorio.deleteById(IdEquipo);
    }

    @Override
    public Optional<Equipo> get(Integer IdEquipo) {
        return equipoRepositorio.findById(IdEquipo);
    }

    @Override
    public void update(Equipo equipo) {
        equipoRepositorio.save(equipo);
    }

    @Override
    public Optional<Equipo> findByIdEquipo(Integer IdEquipo) {
        return equipoRepositorio.findById(IdEquipo);
    }
    
}
