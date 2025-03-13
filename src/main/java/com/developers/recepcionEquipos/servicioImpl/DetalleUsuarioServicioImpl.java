// package com.developers.recepcionEquipos.servicioImpl;

// import java.util.Optional;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.security.core.userdetails.User;
// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.stereotype.Service;
// import com.developers.recepcionEquipos.entidad.Usuario;
// import com.developers.recepcionEquipos.servicio.UsuarioServicio;

// import jakarta.servlet.http.HttpSession;

// @Service
// public class DetalleUsuarioServicioImpl implements UserDetailsService {

//     @Autowired
//     public UsuarioServicio usuarioServicio;

    
    
//     @Autowired
//     HttpSession session;

//     private Logger log = LoggerFactory.getLogger(DetalleUsuarioServicioImpl.class);

//     @Autowired
//     private BCryptPasswordEncoder bCrypt;

//     @Override
//     public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//         log.info("Esto es el username: ");
//         Optional<Usuario> optionalUsuario = usuarioServicio.findByEmail(email);
//         if(optionalUsuario.isPresent()){
//             log.info("Esto es el id el usuario", optionalUsuario.get().getIdUsuario());
//             session.setAttribute("IdUsuario", optionalUsuario.get().getIdUsuario());
//             Usuario usuario = optionalUsuario.get();
//             return User.builder().username(usuario.getEmail()).password(bCrypt.encode(usuario.getContrasena())).roles(usuario.getRol()).build();
//         } else {
//             throw new UsernameNotFoundException("Usuario no Encontrado");
//         }
//     }
// }
