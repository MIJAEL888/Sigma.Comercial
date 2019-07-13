package com.sigma.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A RequisitosSeguridad.
 */
@Entity
@Table(name = "requisitos_seguridad")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RequisitosSeguridad implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "requisitosSeguridad")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Servicio> sevicios = new HashSet<>();

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

    public RequisitosSeguridad nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public RequisitosSeguridad descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Servicio> getSevicios() {
        return sevicios;
    }

    public RequisitosSeguridad sevicios(Set<Servicio> servicios) {
        this.sevicios = servicios;
        return this;
    }

    public RequisitosSeguridad addSevicio(Servicio servicio) {
        this.sevicios.add(servicio);
        servicio.setRequisitosSeguridad(this);
        return this;
    }

    public RequisitosSeguridad removeSevicio(Servicio servicio) {
        this.sevicios.remove(servicio);
        servicio.setRequisitosSeguridad(null);
        return this;
    }

    public void setSevicios(Set<Servicio> servicios) {
        this.sevicios = servicios;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RequisitosSeguridad)) {
            return false;
        }
        return id != null && id.equals(((RequisitosSeguridad) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "RequisitosSeguridad{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
