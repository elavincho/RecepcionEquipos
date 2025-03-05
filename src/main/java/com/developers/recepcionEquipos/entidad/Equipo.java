package com.developers.recepcionEquipos.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/*En esta clase vamos a ingresar SOLAMENTE los equipos recibidos 
y sus accesorios*/

@Entity
@Table(name = "equipos")
public class Equipo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdEquipo;
    private String tipoEquipo;
    private String marca;
    private String modelo;
    private String nroSerie;
    private String nroProducto;
    private String accesorio;
    private String color;
    private String origen;
    private String observaciones;
    private String imagenEquipo;
    private String clave;
    
    
    @ManyToOne
    //private Usuario usuario;
    private Cliente cliente;

    //@OneToMany
    //private Orden orden;//Creo que no tiene sentido - borrar

    public Equipo() {
    }

    public Equipo(Integer IdEquipo, String tipoEquipo, String marca, String modelo, String nroSerie, String nroProducto, String accesorio, String color, String origen, String observaciones, String imagenEquipo, String clave, Cliente cliente) {
        this.IdEquipo = IdEquipo;
        this.tipoEquipo = tipoEquipo;
        this.marca = marca;
        this.modelo = modelo;
        this.nroSerie = nroSerie;
        this.nroProducto = nroProducto;
        this.accesorio = accesorio;
        this.color = color;
        this.origen = origen;
        this.observaciones = observaciones;
        this.imagenEquipo = imagenEquipo;
        this.clave = clave;
        this.cliente = cliente;
    }

    public Integer getIdEquipo() {
        return IdEquipo;
    }

    public void setIdEquipo(Integer IdEquipo) {
        this.IdEquipo = IdEquipo;
    }

    public String getTipoEquipo() {
        return tipoEquipo;
    }

    public void setTipoEquipo(String tipoEquipo) {
        this.tipoEquipo = tipoEquipo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getNroSerie() {
        return nroSerie;
    }

    public void setNroSerie(String nroSerie) {
        this.nroSerie = nroSerie;
    }

    public String getNroProducto() {
        return nroProducto;
    }

    public void setNroProducto(String nroProducto) {
        this.nroProducto = nroProducto;
    }

    public String getAccesorio() {
        return accesorio;
    }

    public void setAccesorio(String accesorio) {
        this.accesorio = accesorio;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getImagenEquipo() {
        return imagenEquipo;
    }

    public void setImagenEquipo(String imagenEquipo) {
        this.imagenEquipo = imagenEquipo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Equipo{" + "IdEquipo=" + IdEquipo + ", tipoEquipo=" + tipoEquipo + ", marca=" + marca + ", modelo=" + modelo + ", nroSerie=" + nroSerie + ", nroProducto=" + nroProducto + ", accesorio=" + accesorio + ", color=" + color + ", origen=" + origen + ", observaciones=" + observaciones + ", imagenEquipo=" + imagenEquipo + ", clave=" + clave + ", cliente=" + cliente + '}';
    }
}
