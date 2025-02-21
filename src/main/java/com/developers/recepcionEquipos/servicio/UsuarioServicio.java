package com.developers.recepcionEquipos.servicio;

import com.developers.recepcionEquipos.entidad.Usuario;
import java.util.List;
import java.util.Optional;


public interface UsuarioServicio {
    
    Usuario save(Usuario usuario);
    
    Optional<Usuario> findByEmail(String email);
    
    Optional<Usuario> findByIdUsuario(Integer IdUsuario);
    
    List<Usuario> findAll();

    public void delete(Integer IdUsuario);

    public Optional<Usuario> get(Integer IdUsuario); //Optional nos da la opcion de poder validar si el objeto que llamamos de la base de datos existe o no.

}
