package com.developers.recepcionEquipos.servicio;

import com.developers.recepcionEquipos.entidad.Equipo;
import java.util.List;
import java.util.Optional;


public interface EquipoServicio {
    
    Equipo save (Equipo equipo);
    
    Optional<Equipo> findByNroSerie(String nroSerie);
    
    List<Equipo> findAll();
    
    public void Delete(Integer IdEquipo);
    
    public Optional<Equipo> get(Integer IdEquipo);

    public void update(Equipo equipo);
    
}
