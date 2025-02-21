package com.developers.recepcionEquipos.servicioImpl;

import com.developers.recepcionEquipos.entidad.Usuario;
import com.developers.recepcionEquipos.repositorio.UsuarioRepositorio;
import com.developers.recepcionEquipos.servicio.UsuarioServicio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServicioImpl implements UsuarioServicio{
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    
    @Override
    public Usuario save(Usuario usuario) {

        return usuarioRepositorio.save(usuario);
    }

    @Override
    public Optional<Usuario> findByEmail(String email) {
        
        return usuarioRepositorio.findByEmail(email);
    }

    @Override
    public Optional<Usuario> findByIdUsuario(Integer IdUsuario) {
        return usuarioRepositorio.findById(IdUsuario);
    }

    @Override
    public List<Usuario> findAll() {
        return usuarioRepositorio.findAll();
    }

    @Override
    public void delete(Integer IdUsuario) {
        usuarioRepositorio.deleteById(IdUsuario);
    }

    @Override
    public Optional<Usuario> get(Integer IdUsuario) {
        return usuarioRepositorio.findById(IdUsuario);
    }
    
}
