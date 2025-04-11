package com.developers.recepcionEquipos.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
//import java.util.List;

/* En esta clase vamos a ingresar a TODOS LOS USUARIOS ya sean ADMINISTRATIVOS,
TECNICOS Y ADMINISTRADORES para luego asignarles un ROL.*/

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdUsuario;
    private String nombre;
    private String apellido;
    private String documento;
    private String nombreUsuario;
    private String email;
    private String provincia;
    private String localidad;
    private String direccion;
    private String altura;
    private String piso;
    private String depto;
    private String telefono;
    private String rol;
    private String contrasena;
    private String foto;

    // El mapeo se va a hacer desde cliente
    
    public Usuario() {
    }

    public Usuario(Integer IdUsuario, String nombre, String apellido, String documento, String nombreUsuario, String email, String provincia, String localidad, String direccion, String altura, String piso, String depto, String telefono, String rol, String contrasena, String foto) {
        this.IdUsuario = IdUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.provincia = provincia;
        this.localidad = localidad;
        this.direccion = direccion;
        this.altura = altura;
        this.piso = piso;
        this.depto = depto;
        this.telefono = telefono;
        this.rol = rol;
        this.contrasena = contrasena;
        this.foto = foto;
    }

    public Integer getIdUsuario() {
        return IdUsuario;
    }

    public void setIdUsuario(Integer IdUsuario) {
        this.IdUsuario = IdUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getPiso() {
        return piso;
    }

    public void setPiso(String piso) {
        this.piso = piso;
    }

    public String getDepto() {
        return depto;
    }

    public void setDepto(String depto) {
        this.depto = depto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    @Override
    public String toString() {
        return "Usuario{" + "IdUsuario=" + IdUsuario + ", nombre=" + nombre + ", apellido=" + apellido + ", documento=" + documento + ", nombreUsuario=" + nombreUsuario + ", email=" + email + ", provincia=" + provincia + ", localidad=" + localidad + ", direccion=" + direccion + ", altura=" + altura + ", piso=" + piso + ", depto=" + depto + ", telefono=" + telefono + ", rol=" + rol + ", contrasena=" + contrasena + ", foto=" + foto + '}';
    }
}
