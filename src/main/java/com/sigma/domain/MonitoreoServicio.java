package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A MonitoreoServicio.
 */
@Entity
@Table(name = "monitoreo_servicio")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class MonitoreoServicio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "costo_total")
    private Float costoTotal;

    @ManyToOne
    @JsonIgnoreProperties("monitoreoServicios")
    private Servicio servicio;

    @ManyToOne
    @JsonIgnoreProperties("monitoreoServicios")
    private ParamentroMonitoreo paramentroMonitoreo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public MonitoreoServicio cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getCostoTotal() {
        return costoTotal;
    }

    public MonitoreoServicio costoTotal(Float costoTotal) {
        this.costoTotal = costoTotal;
        return this;
    }

    public void setCostoTotal(Float costoTotal) {
        this.costoTotal = costoTotal;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public MonitoreoServicio servicio(Servicio servicio) {
        this.servicio = servicio;
        return this;
    }

    public void setServicio(Servicio servicio) {
        this.servicio = servicio;
    }

    public ParamentroMonitoreo getParamentroMonitoreo() {
        return paramentroMonitoreo;
    }

    public MonitoreoServicio paramentroMonitoreo(ParamentroMonitoreo paramentroMonitoreo) {
        this.paramentroMonitoreo = paramentroMonitoreo;
        return this;
    }

    public void setParamentroMonitoreo(ParamentroMonitoreo paramentroMonitoreo) {
        this.paramentroMonitoreo = paramentroMonitoreo;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MonitoreoServicio)) {
            return false;
        }
        return id != null && id.equals(((MonitoreoServicio) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MonitoreoServicio{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", costoTotal=" + getCostoTotal() +
            "}";
    }
}
