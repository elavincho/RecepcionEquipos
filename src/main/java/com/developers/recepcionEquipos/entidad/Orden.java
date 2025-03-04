package com.developers.recepcionEquipos.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.List;

/* En esta clase vamos a ingresar los pedidos de reparación relacionandolos
con los equipos */
@Entity
@Table(name = "ordenes")
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer IdOrden;
    private String fallaCliente;
    private String fallaTecnico;
    private String prioridad;
    private String avisoCliente;
    private String medioAviso;
    // Atributos de detalleOrden
    private String trabajoRealizado;
    private LocalDate fechaInicio;
    //private String fechaInicioFormateado;
    private LocalDate fechaFinalizacion;
    //private String fechaFinalizacionFormateado;
    private String repuestoUtilizado;
    private double precioManoObra;
    private String precioManoObraFormateado;
    private double precioRepuesto;
    private String precioRepuestoFormateado;
    private double iva;
    private String ivaFormateado;
    private double total;
    private String totalFormateado;

    @ManyToOne
    //private Usuario usuario;
    private Cliente cliente;

    @ManyToOne
    private Equipo equipo;

    @OneToMany(mappedBy = "orden")
    private List<DetalleOrden> detalle;

    public Orden() {
    }

    public Orden(Integer IdOrden, String fallaCliente, String fallaTecnico, String prioridad, String avisoCliente, String medioAviso, String trabajoRealizado, LocalDate fechaInicio, LocalDate fechaFinalizacion, String repuestoUtilizado, double precioManoObra, String precioManoObraFormateado, double precioRepuesto, String precioRepuestoFormateado, double iva, String ivaFormateado, double total, String totalFormateado, Cliente cliente, Equipo equipo, List<DetalleOrden> detalle) {
        this.IdOrden = IdOrden;
        this.fallaCliente = fallaCliente;
        this.fallaTecnico = fallaTecnico;
        this.prioridad = prioridad;
        this.avisoCliente = avisoCliente;
        this.medioAviso = medioAviso;
        this.trabajoRealizado = trabajoRealizado;
        this.fechaInicio = fechaInicio;
        this.fechaFinalizacion = fechaFinalizacion;
        this.repuestoUtilizado = repuestoUtilizado;
        this.precioManoObra = precioManoObra;
        this.precioManoObraFormateado = precioManoObraFormateado;
        this.precioRepuesto = precioRepuesto;
        this.precioRepuestoFormateado = precioRepuestoFormateado;
        this.iva = iva;
        this.ivaFormateado = ivaFormateado;
        this.total = total;
        this.totalFormateado = totalFormateado;
        this.cliente = cliente;
        this.equipo = equipo;
        this.detalle = detalle;
    }

    public Integer getIdOrden() {
        return IdOrden;
    }

    public void setIdOrden(Integer IdOrden) {
        this.IdOrden = IdOrden;
    }

    public String getFallaCliente() {
        return fallaCliente;
    }

    public void setFallaCliente(String fallaCliente) {
        this.fallaCliente = fallaCliente;
    }

    public String getFallaTecnico() {
        return fallaTecnico;
    }

    public void setFallaTecnico(String fallaTecnico) {
        this.fallaTecnico = fallaTecnico;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public String getAvisoCliente() {
        return avisoCliente;
    }

    public void setAvisoCliente(String avisoCliente) {
        this.avisoCliente = avisoCliente;
    }

    public String getMedioAviso() {
        return medioAviso;
    }

    public void setMedioAviso(String medioAviso) {
        this.medioAviso = medioAviso;
    }

    public String getTrabajoRealizado() {
        return trabajoRealizado;
    }

    public void setTrabajoRealizado(String trabajoRealizado) {
        this.trabajoRealizado = trabajoRealizado;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFinalizacion() {
        return fechaFinalizacion;
    }

    public void setFechaFinalizacion(LocalDate fechaFinalizacion) {
        this.fechaFinalizacion = fechaFinalizacion;
    }

    public String getRepuestoUtilizado() {
        return repuestoUtilizado;
    }

    public void setRepuestoUtilizado(String repuestoUtilizado) {
        this.repuestoUtilizado = repuestoUtilizado;
    }

    public double getPrecioManoObra() {
        return precioManoObra;
    }

    public void setPrecioManoObra(double precioManoObra) {
        this.precioManoObra = precioManoObra;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public List<DetalleOrden> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleOrden> detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "Orden{" + "IdOrden=" + IdOrden + ", fallaCliente=" + fallaCliente + ", fallaTecnico=" + fallaTecnico + ", prioridad=" + prioridad + ", avisoCliente=" + avisoCliente + ", medioAviso=" + medioAviso + ", trabajoRealizado=" + trabajoRealizado + ", fechaInicio=" + fechaInicio + ", fechaFinalizacion=" + fechaFinalizacion + ", repuestoUtilizado=" + repuestoUtilizado + ", precioManoObra=" + precioManoObra + ", precioManoObraFormateado=" + precioManoObraFormateado + ", precioRepuesto=" + precioRepuesto + ", precioRepuestoFormateado=" + precioRepuestoFormateado + ", iva=" + iva + ", ivaFormateado=" + ivaFormateado + ", total=" + total + ", totalFormateado=" + totalFormateado + ", cliente=" + cliente + ", equipo=" + equipo + ", detalle=" + detalle + '}';
    }
}
