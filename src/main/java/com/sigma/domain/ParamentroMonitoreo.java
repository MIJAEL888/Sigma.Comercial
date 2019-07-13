package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ParamentroMonitoreo.
 */
@Entity
@Table(name = "paramentro_monitoreo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ParamentroMonitoreo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "costo")
    private Float costo;

    @ManyToOne
    @JsonIgnoreProperties("paramentroMonitoreos")
    private ComponenteMonitoreo componenteMonitoreo;

    @OneToMany(mappedBy = "paramentroMonitoreo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<MonitoreoServicio> monitoreoServicios = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public ParamentroMonitoreo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ParamentroMonitoreo descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Float getCosto() {
        return costo;
    }

    public ParamentroMonitoreo costo(Float costo) {
        this.costo = costo;
        return this;
    }

    public void setCosto(Float costo) {
        this.costo = costo;
    }

    public ComponenteMonitoreo getComponenteMonitoreo() {
        return componenteMonitoreo;
    }

    public ParamentroMonitoreo componenteMonitoreo(ComponenteMonitoreo componenteMonitoreo) {
        this.componenteMonitoreo = componenteMonitoreo;
        return this;
    }

    public void setComponenteMonitoreo(ComponenteMonitoreo componenteMonitoreo) {
        this.componenteMonitoreo = componenteMonitoreo;
    }

    public Set<MonitoreoServicio> getMonitoreoServicios() {
        return monitoreoServicios;
    }

    public ParamentroMonitoreo monitoreoServicios(Set<MonitoreoServicio> monitoreoServicios) {
        this.monitoreoServicios = monitoreoServicios;
        return this;
    }

    public ParamentroMonitoreo addMonitoreoServicio(MonitoreoServicio monitoreoServicio) {
        this.monitoreoServicios.add(monitoreoServicio);
        monitoreoServicio.setParamentroMonitoreo(this);
        return this;
    }

    public ParamentroMonitoreo removeMonitoreoServicio(MonitoreoServicio monitoreoServicio) {
        this.monitoreoServicios.remove(monitoreoServicio);
        monitoreoServicio.setParamentroMonitoreo(null);
        return this;
    }

    public void setMonitoreoServicios(Set<MonitoreoServicio> monitoreoServicios) {
        this.monitoreoServicios = monitoreoServicios;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParamentroMonitoreo)) {
            return false;
        }
        return id != null && id.equals(((ParamentroMonitoreo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ParamentroMonitoreo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", costo=" + getCosto() +
            "}";
    }
}
