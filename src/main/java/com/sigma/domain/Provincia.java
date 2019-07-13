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
 * A Provincia.
 */
@Entity
@Table(name = "provincia")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Provincia implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "ubigeo", nullable = false)
    private String ubigeo;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "provincia")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Distrito> distritos = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("provincias")
    private Departamento departamento;

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

    public Provincia nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public Provincia ubigeo(String ubigeo) {
        this.ubigeo = ubigeo;
        return this;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Provincia descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Distrito> getDistritos() {
        return distritos;
    }

    public Provincia distritos(Set<Distrito> distritos) {
        this.distritos = distritos;
        return this;
    }

    public Provincia addDistrito(Distrito distrito) {
        this.distritos.add(distrito);
        distrito.setProvincia(this);
        return this;
    }

    public Provincia removeDistrito(Distrito distrito) {
        this.distritos.remove(distrito);
        distrito.setProvincia(null);
        return this;
    }

    public void setDistritos(Set<Distrito> distritos) {
        this.distritos = distritos;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public Provincia departamento(Departamento departamento) {
        this.departamento = departamento;
        return this;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Provincia)) {
            return false;
        }
        return id != null && id.equals(((Provincia) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Provincia{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", ubigeo='" + getUbigeo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
