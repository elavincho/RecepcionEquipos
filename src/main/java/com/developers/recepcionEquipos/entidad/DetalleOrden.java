package com.developers.recepcionEquipos.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Date;

/* En esta clase la vamos a utilizar para cargar el trabajo realizado, 
la fecha de inicio y fin de la reparación y cargar los repuestos utilizados
y sus respectivos precios, además del iva y del total*/

@Entity
@Table(name = "detalles")
public class DetalleOrden {
    
    private Integer IdDetalle;
    private String trabajoRealizado;
    private Date fechaInicio;
    private String fechaInicioFormateado;
    private Date fechaFinalizacion;
    private String fechaFinalizacionFormateado;
    private String repuestoUtilizado;
    private double precioManoOBra;
    private String precioManoObraFormateado;
    private double precioRepuesto;
    private String precioRepuestoFormateado;
    private double iva;
    private String ivaFormateado;
    private double total;
    private String totalFormateado;
    
    @OneToMany
    private Orden orden;
    
    @OneToMany
    private Equipo equipo;

    public DetalleOrden() {
    }

    public DetalleOrden(Integer IdDetalle, String trabajoRealizado, Date fechaInicio, String fechaInicioFormateado, Date fechaFinalizacion, String fechaFinalizacionFormateado, String repuestoUtilizado, double precioManoOBra, String precioManoObraFormateado, double precioRepuesto, String precioRepuestoFormateado, double iva, String ivaFormateado, double total, String totalFormateado, Orden orden, Equipo equipo) {
        this.IdDetalle = IdDetalle;
        this.trabajoRealizado = trabajoRealizado;
        this.fechaInicio = fechaInicio;
        this.fechaInicioFormateado = fechaInicioFormateado;
        this.fechaFinalizacion = fechaFinalizacion;
        this.fechaFinalizacionFormateado = fechaFinalizacionFormateado;
        this.repuestoUtilizado = repuestoUtilizado;
        this.precioManoOBra = precioManoOBra;
        this.precioManoObraFormateado = precioManoObraFormateado;
        this.precioRepuesto = precioRepuesto;
        this.precioRepuestoFormateado = precioRepuestoFormateado;
        this.iva = iva;
        this.ivaFormateado = ivaFormateado;
        this.total = total;
        this.totalFormateado = totalFormateado;
        this.orden = orden;
        this.equipo = equipo;
    }

    public Integer getIdDetalle() {
        return IdDetalle;
    }

    public void setIdDetalle(Integer IdDetalle) {
        this.IdDetalle = IdDetalle;
    }

    public String getTrabajoRealizado() {
        return trabajoRealizado;
    }

    public void setTrabajoRealizado(String trabajoRealizado) {
        this.trabajoRealizado = trabajoRealizado;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaInicioFormateado() {
        return fechaInicioFormateado;
    }

    public void setFechaInicioFormateado(String fechaInicioFormateado) {
        this.fechaInicioFormateado = fechaInicioFormateado;
    }

    public Date getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(Date fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getFechaFinalizacionFormateado() {
        return fechaFinalizacionFormateado;
    }

    public void setFechaFinalizacionFormateado(String fechaFinalizacionFormateado) {
        this.fechaFinalizacionFormateado = fechaFinalizacionFormateado;
    }

    public String getRepuestoUtilizado() {
        return repuestoUtilizado;
    }

    public void setRepuestoUtilizado(String repuestoUtilizado) {
        this.repuestoUtilizado = repuestoUtilizado;
    }

    public double getPrecioManoOBra() {
        return precioManoOBra;
    }

    public void setPrecioManoOBra(double precioManoOBra) {
        this.precioManoOBra = precioManoOBra;
    }

    public String getPrecioManoObraFormateado() {
        return precioManoObraFormateado;
    }

    public void setPrecioManoObraFormateado(String precioManoObraFormateado) {
        this.precioManoObraFormateado = precioManoObraFormateado;
    }

    public double getPrecioRepuesto() {
        return precioRepuesto;
    }

    public void setPrecioRepuesto(double precioRepuesto) {
        this.precioRepuesto = precioRepuesto;
    }

    public String getPrecioRepuestoFormateado() {
        return precioRepuestoFormateado;
    }

    public void setPrecioRepuestoFormateado(String precioRepuestoFormateado) {
        this.precioRepuestoFormateado = precioRepuestoFormateado;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public String getIvaFormateado() {
        return ivaFormateado;
    }

    public void setIvaFormateado(String ivaFormateado) {
        this.ivaFormateado = ivaFormateado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getTotalFormateado() {
        return totalFormateado;
    }

    public void setTotalFormateado(String totalFormateado) {
        this.totalFormateado = totalFormateado;
    }

    public Orden getOrden() {
        return orden;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    @Override
    public String toString() {
        return "DetalleOrden{" + "IdDetalle=" + IdDetalle + ", trabajoRealizado=" + trabajoRealizado + ", fechaInicio=" + fechaInicio + ", fechaInicioFormateado=" + fechaInicioFormateado + ", fechaFinalizacion=" + fechaFinalizacion + ", fechaFinalizacionFormateado=" + fechaFinalizacionFormateado + ", repuestoUtilizado=" + repuestoUtilizado + ", precioManoOBra=" + precioManoOBra + ", precioManoObraFormateado=" + precioManoObraFormateado + ", precioRepuesto=" + precioRepuesto + ", precioRepuestoFormateado=" + precioRepuestoFormateado + ", iva=" + iva + ", ivaFormateado=" + ivaFormateado + ", total=" + total + ", totalFormateado=" + totalFormateado + ", orden=" + orden + ", equipo=" + equipo + '}';
    }
}
