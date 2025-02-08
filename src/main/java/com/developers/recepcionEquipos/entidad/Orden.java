package com.developers.recepcionEquipos.entidad;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

/* En esta clase vamos a ingresar los pedidos de reparación relacionandolos
con los equipos */

@Entity
@Table(name = "ordenes")
public class Orden {
    
    private Integer IdOrden;
    private String fallaCliente;
    private String fallaTecnico;
    private String prioridad;
    private String avisoCliente;
    private String medioAviso;
    
    @OneToMany
    private Usuario usuario;
    
    @OneToMany(mappedBy = "orden")
    private List<DetalleOrden> detalle;

    public Orden() {
    }

    public Orden(Integer IdOrden, String fallaCliente, String fallaTecnico, String prioridad, String avisoCliente, String medioAviso, Usuario usuario, List<DetalleOrden> detalle) {
        this.IdOrden = IdOrden;
        this.fallaCliente = fallaCliente;
        this.fallaTecnico = fallaTecnico;
        this.prioridad = prioridad;
        this.avisoCliente = avisoCliente;
        this.medioAviso = medioAviso;
        this.usuario = usuario;
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<DetalleOrden> getDetalle() {
        return detalle;
    }

    public void setDetalle(List<DetalleOrden> detalle) {
        this.detalle = detalle;
    }

    @Override
    public String toString() {
        return "Orden{" + "IdOrden=" + IdOrden + ", fallaCliente=" + fallaCliente + ", fallaTecnico=" + fallaTecnico + ", prioridad=" + prioridad + ", avisoCliente=" + avisoCliente + ", medioAviso=" + medioAviso + ", usuario=" + usuario + ", detalle=" + detalle + '}';
    }
}
