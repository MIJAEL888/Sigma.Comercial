package com.sigma.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A ComponenteMonitoreo.
 */
@Entity
@Table(name = "componente_monitoreo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ComponenteMonitoreo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "componenteMonitoreo")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ParamentroMonitoreo> paramentroMonitoreos = new HashSet<>();

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

    public ComponenteMonitoreo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public ComponenteMonitoreo descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<ParamentroMonitoreo> getParamentroMonitoreos() {
        return paramentroMonitoreos;
    }

    public ComponenteMonitoreo paramentroMonitoreos(Set<ParamentroMonitoreo> paramentroMonitoreos) {
        this.paramentroMonitoreos = paramentroMonitoreos;
        return this;
    }

    public ComponenteMonitoreo addParamentroMonitoreo(ParamentroMonitoreo paramentroMonitoreo) {
        this.paramentroMonitoreos.add(paramentroMonitoreo);
        paramentroMonitoreo.setComponenteMonitoreo(this);
        return this;
    }

    public ComponenteMonitoreo removeParamentroMonitoreo(ParamentroMonitoreo paramentroMonitoreo) {
        this.paramentroMonitoreos.remove(paramentroMonitoreo);
        paramentroMonitoreo.setComponenteMonitoreo(null);
        return this;
    }

    public void setParamentroMonitoreos(Set<ParamentroMonitoreo> paramentroMonitoreos) {
        this.paramentroMonitoreos = paramentroMonitoreos;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ComponenteMonitoreo)) {
            return false;
        }
        return id != null && id.equals(((ComponenteMonitoreo) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ComponenteMonitoreo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
