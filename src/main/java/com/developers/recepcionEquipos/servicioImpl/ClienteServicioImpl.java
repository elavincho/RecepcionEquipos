
package com.developers.recepcionEquipos.servicioImpl;

import com.developers.recepcionEquipos.entidad.Cliente;
import com.developers.recepcionEquipos.repositorio.ClienteRepositorio;
import com.developers.recepcionEquipos.servicio.ClienteServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClienteServicioImpl implements ClienteServicio {
    
    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Override
    public Cliente save(Cliente cliente) {
        return clienteRepositorio.save(cliente);
    }

    @Override
    public Optional<Cliente> findByEmail(String email) {
        return clienteRepositorio.findByEmail(email);
    }

    @Override
    public Optional<Cliente> findByIdCliente(Integer IdCliente) {
        return clienteRepositorio.findById(IdCliente);
    }

    @Override
    public List<Cliente> findAll() {
        return clienteRepositorio.findAll();
    }

    @Override
    public void Delete(Integer IdCliente) {
        clienteRepositorio.deleteById(IdCliente);
    }

    @Override
    public Optional<Cliente> get(Integer IdCliente) {
        return clienteRepositorio.findById(IdCliente);
    }

    @Override
    public void update(Cliente cliente) {
        clienteRepositorio.save(cliente);
    }
    
}
