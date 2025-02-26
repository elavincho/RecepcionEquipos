package com.developers.recepcionEquipos.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

/* En esta clase vamos a ingresar a SOLAMENTE A LOS CLIENTES.*/

@Entity
@Table(name = "clientes")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdCliente;
    private String nombre;
    private String apellido;
    private String documento;
    private String email;
    private String provincia;
    private String localidad;
    private String direccion;
    private String altura;
    private String piso;
    private String depto;
    private String telefono;
    private String foto;

    @OneToMany(mappedBy = "cliente")
    private List<Equipo> equipos;

    @OneToMany(mappedBy = "cliente")
    private List<Orden> ordenes;

    public Cliente() {
    }

    public Cliente(Integer IdCliente, String nombre, String apellido, String documento, String email, String provincia, String localidad, String direccion, String altura, String piso, String depto, String telefono, String foto, List<Equipo> equipos, List<Orden> ordenes) {
        this.IdCliente = IdCliente;
        this.nombre = nombre;
        this.apellido = apellido;
        this.documento = documento;
        this.email = email;
        this.provincia = provincia;
        this.localidad = localidad;
        this.direccion = direccion;
        this.altura = altura;
        this.piso = piso;
        this.depto = depto;
        this.telefono = telefono;
        this.foto = foto;
        this.equipos = equipos;
        this.ordenes = ordenes;
    }

    public Integer getIdCliente() {
        return IdCliente;
    }

    public void setIdCliente(Integer IdCliente) {
        this.IdCliente = IdCliente;
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

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public List<Equipo> getEquipos() {
        return equipos;
    }

    public void setEquipos(List<Equipo> equipos) {
        this.equipos = equipos;
    }

    public List<Orden> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Orden> ordenes) {
        this.ordenes = ordenes;
    }

    @Override
    public String toString() {
        return "Cliente{" + "IdCliente=" + IdCliente + ", nombre=" + nombre + ", apellido=" + apellido + ", documento=" + documento + ", email=" + email + ", provincia=" + provincia + ", localidad=" + localidad + ", direccion=" + direccion + ", altura=" + altura + ", piso=" + piso + ", depto=" + depto + ", telefono=" + telefono + ", foto=" + foto + ", equipos=" + equipos + ", ordenes=" + ordenes + '}';
    }
}
