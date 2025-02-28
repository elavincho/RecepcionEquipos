
package com.developers.recepcionEquipos.servicio;

import com.developers.recepcionEquipos.entidad.Cliente;
import java.util.List;
import java.util.Optional;


public interface ClienteServicio {
    
    Cliente save (Cliente cliente);
    
    Optional<Cliente> findByEmail(String email);
    
    Optional<Cliente> findByIdCliente(Integer IdCliente);
    
    List<Cliente> findAll();
    
    public void Delete(Integer IdCliente);
    
    public Optional<Cliente> get(Integer IdCliente);

    public void update(Cliente cliente);
    
}
