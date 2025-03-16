package com.developers.recepcionEquipos.entidad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Date;
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
    @Column(name = "aviso_cliente") // Especifica el nombre de la columna en la base de datos
    private String avisoCliente;
    private String medioAviso;
    private String numero;
    private Date fechaCreacion;
    private String fechaCreacionFormateada;
    // Atributos de detalleOrden
    private String trabajoRealizado;
    private LocalDate fechaInicio;
    private LocalDate fechaFinalizacion;
    private String repuestoUtilizado;
    private Double precioManoObra;
    private String precioManoObraFormateado;
    private Double precioRepuesto;
    private String precioRepuestoFormateado;
    private Double iva;
    private String ivaFormateado;
    private Double total;
    private String totalFormateado;
    private Double subTotal;
    private String subTotalFormateado;
    @Column(name = "estado_orden") // Especifica el nombre de la columna en la base de datos
    private String estadoOrden;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Equipo equipo;

    @OneToMany(mappedBy = "orden")
    private List<DetalleOrden> detalle;

    public Orden() {
    }

    public Orden(Integer IdOrden, String fallaCliente, String fallaTecnico, String prioridad, String avisoCliente, String medioAviso, String numero, Date fechaCreacion, String fechaCreacionFormateada, String trabajoRealizado, LocalDate fechaInicio, LocalDate fechaFinalizacion, String repuestoUtilizado, Double precioManoObra, String precioManoObraFormateado, Double precioRepuesto, String precioRepuestoFormateado, Double iva, String ivaFormateado, Double total, String totalFormateado, Double subTotal, String subTotalFormateado, String estadoOrden, Cliente cliente, Equipo equipo, List<DetalleOrden> detalle) {
        this.IdOrden = IdOrden;
        this.fallaCliente = fallaCliente;
        this.fallaTecnico = fallaTecnico;
        this.prioridad = prioridad;
        this.avisoCliente = avisoCliente;
        this.medioAviso = medioAviso;
        this.numero = numero;
        this.fechaCreacion = fechaCreacion;
        this.fechaCreacionFormateada = fechaCreacionFormateada;
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
        this.subTotal = subTotal;
        this.subTotalFormateado = subTotalFormateado;
        this.estadoOrden = estadoOrden;
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

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getFechaCreacionFormateada() {
        return fechaCreacionFormateada;
    }

    public void setFechaCreacionFormateada(String fechaCreacionFormateada) {
        this.fechaCreacionFormateada = fechaCreacionFormateada;
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

    public Double getPrecioManoObra() {
        return precioManoObra;
    }

    public void setPrecioManoObra(Double precioManoObra) {
        this.precioManoObra = precioManoObra;
    }

    public String getPrecioManoObraFormateado() {
        return precioManoObraFormateado;
    }

    public void setPrecioManoObraFormateado(String precioManoObraFormateado) {
        this.precioManoObraFormateado = precioManoObraFormateado;
    }

    public Double getPrecioRepuesto() {
        return precioRepuesto;
    }

    public void setPrecioRepuesto(Double precioRepuesto) {
        this.precioRepuesto = precioRepuesto;
    }

    public String getPrecioRepuestoFormateado() {
        return precioRepuestoFormateado;
    }

    public void setPrecioRepuestoFormateado(String precioRepuestoFormateado) {
        this.precioRepuestoFormateado = precioRepuestoFormateado;
    }

    public Double getIva() {
        return iva;
    }

    public void setIva(Double iva) {
        this.iva = iva;
    }

    public String getIvaFormateado() {
        return ivaFormateado;
    }

    public void setIvaFormateado(String ivaFormateado) {
        this.ivaFormateado = ivaFormateado;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getTotalFormateado() {
        return totalFormateado;
    }

    public void setTotalFormateado(String totalFormateado) {
        this.totalFormateado = totalFormateado;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getSubTotalFormateado() {
        return subTotalFormateado;
    }

    public void setSubTotalFormateado(String subTotalFormateado) {
        this.subTotalFormateado = subTotalFormateado;
    }

    public String getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        this.estadoOrden = estadoOrden;
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
        return "Orden{" + "IdOrden=" + IdOrden + ", fallaCliente=" + fallaCliente + ", fallaTecnico=" + fallaTecnico + ", prioridad=" + prioridad + ", avisoCliente=" + avisoCliente + ", medioAviso=" + medioAviso + ", numero=" + numero + ", fechaCreacion=" + fechaCreacion + ", fechaCreacionFormateada=" + fechaCreacionFormateada + ", trabajoRealizado=" + trabajoRealizado + ", fechaInicio=" + fechaInicio + ", fechaFinalizacion=" + fechaFinalizacion + ", repuestoUtilizado=" + repuestoUtilizado + ", precioManoObra=" + precioManoObra + ", precioManoObraFormateado=" + precioManoObraFormateado + ", precioRepuesto=" + precioRepuesto + ", precioRepuestoFormateado=" + precioRepuestoFormateado + ", iva=" + iva + ", ivaFormateado=" + ivaFormateado + ", total=" + total + ", totalFormateado=" + totalFormateado + ", subTotal=" + subTotal + ", subTotalFormateado=" + subTotalFormateado + ", estadoOrden=" + estadoOrden + ", cliente=" + cliente + ", equipo=" + equipo + ", detalle=" + detalle + '}';
    }
}
