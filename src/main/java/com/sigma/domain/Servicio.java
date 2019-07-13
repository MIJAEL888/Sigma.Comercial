package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.sigma.domain.enumeration.EstadoServicio;

/**
 * A Servicio.
 */
@Entity
@Table(name = "servicio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Servicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo")
    private String codigo;

    @Column(name = "fecha_entrega")
    private LocalDate fechaEntrega;

    @Column(name = "nombre_solicitante")
    private String nombreSolicitante;

    @Column(name = "numero_solicitante")
    private String numeroSolicitante;

    @Lob
    @Column(name = "observacion")
    private String observacion;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado")
    private EstadoServicio estado;

    @Column(name = "codigo_cliente")
    private String codigoCliente;

    @Column(name = "codigo_sede")
    private String codigoSede;

    @OneToMany(mappedBy = "servicio")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MonitoreoServicio> monitoreoServicios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("servicios")
    private TipoServicios tipoServicios;

    @ManyToOne
    @JsonIgnoreProperties("servicios")
    private TipoSolicitud tipoSolicitud;

    @ManyToOne
    @JsonIgnoreProperties("servicios")
    private TipoInduccion tipoInduccion;

    @ManyToOne
    @JsonIgnoreProperties("servicios")
    private RequisitosSeguridad requisitosSeguridad;

    @ManyToOne
    @JsonIgnoreProperties("servicios")
    private Sede sede;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public Servicio codigo(String codigo) {
        this.codigo = codigo;
        return this;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public Servicio fechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
        return this;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getNombreSolicitante() {
        return nombreSolicitante;
    }

    public Servicio nombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
        return this;
    }

    public void setNombreSolicitante(String nombreSolicitante) {
        this.nombreSolicitante = nombreSolicitante;
    }

    public String getNumeroSolicitante() {
        return numeroSolicitante;
    }

    public Servicio numeroSolicitante(String numeroSolicitante) {
        this.numeroSolicitante = numeroSolicitante;
        return this;
    }

    public void setNumeroSolicitante(String numeroSolicitante) {
        this.numeroSolicitante = numeroSolicitante;
    }

    public String getObservacion() {
        return observacion;
    }

    public Servicio observacion(String observacion) {
        this.observacion = observacion;
        return this;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Servicio descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public EstadoServicio getEstado() {
        return estado;
    }

    public Servicio estado(EstadoServicio estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(EstadoServicio estado) {
        this.estado = estado;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public Servicio codigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
        return this;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getCodigoSede() {
        return codigoSede;
    }

    public Servicio codigoSede(String codigoSede) {
        this.codigoSede = codigoSede;
        return this;
    }

    public void setCodigoSede(String codigoSede) {
        this.codigoSede = codigoSede;
    }

    public Set<MonitoreoServicio> getMonitoreoServicios() {
        return monitoreoServicios;
    }

    public Servicio monitoreoServicios(Set<MonitoreoServicio> monitoreoServicios) {
        this.monitoreoServicios = monitoreoServicios;
        return this;
    }

    public Servicio addMonitoreoServicio(MonitoreoServicio monitoreoServicio) {
        this.monitoreoServicios.add(monitoreoServicio);
        monitoreoServicio.setServicio(this);
        return this;
    }

    public Servicio removeMonitoreoServicio(MonitoreoServicio monitoreoServicio) {
        this.monitoreoServicios.remove(monitoreoServicio);
        monitoreoServicio.setServicio(null);
        return this;
    }

    public void setMonitoreoServicios(Set<MonitoreoServicio> monitoreoServicios) {
        this.monitoreoServicios = monitoreoServicios;
    }

    public TipoServicios getTipoServicios() {
        return tipoServicios;
    }

    public Servicio tipoServicios(TipoServicios tipoServicios) {
        this.tipoServicios = tipoServicios;
        return this;
    }

    public void setTipoServicios(TipoServicios tipoServicios) {
        this.tipoServicios = tipoServicios;
    }

    public TipoSolicitud getTipoSolicitud() {
        return tipoSolicitud;
    }

    public Servicio tipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
        return this;
    }

    public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
        this.tipoSolicitud = tipoSolicitud;
    }

    public TipoInduccion getTipoInduccion() {
        return tipoInduccion;
    }

    public Servicio tipoInduccion(TipoInduccion tipoInduccion) {
        this.tipoInduccion = tipoInduccion;
        return this;
    }

    public void setTipoInduccion(TipoInduccion tipoInduccion) {
        this.tipoInduccion = tipoInduccion;
    }

    public RequisitosSeguridad getRequisitosSeguridad() {
        return requisitosSeguridad;
    }

    public Servicio requisitosSeguridad(RequisitosSeguridad requisitosSeguridad) {
        this.requisitosSeguridad = requisitosSeguridad;
        return this;
    }

    public void setRequisitosSeguridad(RequisitosSeguridad requisitosSeguridad) {
        this.requisitosSeguridad = requisitosSeguridad;
    }

    public Sede getSede() {
        return sede;
    }

    public Servicio sede(Sede sede) {
        this.sede = sede;
        return this;
    }

    public void setSede(Sede sede) {
        this.sede = sede;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Servicio)) {
            return false;
        }
        return id != null && id.equals(((Servicio) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Servicio{" +
            "id=" + getId() +
            ", codigo='" + getCodigo() + "'" +
            ", fechaEntrega='" + getFechaEntrega() + "'" +
            ", nombreSolicitante='" + getNombreSolicitante() + "'" +
            ", numeroSolicitante='" + getNumeroSolicitante() + "'" +
            ", observacion='" + getObservacion() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", estado='" + getEstado() + "'" +
            ", codigoCliente='" + getCodigoCliente() + "'" +
            ", codigoSede='" + getCodigoSede() + "'" +
            "}";
    }
}
