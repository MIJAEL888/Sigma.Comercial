package com.sigma.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Departamento.
 */
@Entity
@Table(name = "departamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Departamento implements Serializable {

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

    @OneToMany(mappedBy = "departamento")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Provincia> provincias = new HashSet<>();

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

    public Departamento nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public Departamento ubigeo(String ubigeo) {
        this.ubigeo = ubigeo;
        return this;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Departamento descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Provincia> getProvincias() {
        return provincias;
    }

    public Departamento provincias(Set<Provincia> provincias) {
        this.provincias = provincias;
        return this;
    }

    public Departamento addProvincia(Provincia provincia) {
        this.provincias.add(provincia);
        provincia.setDepartamento(this);
        return this;
    }

    public Departamento removeProvincia(Provincia provincia) {
        this.provincias.remove(provincia);
        provincia.setDepartamento(null);
        return this;
    }

    public void setProvincias(Set<Provincia> provincias) {
        this.provincias = provincias;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Departamento)) {
            return false;
        }
        return id != null && id.equals(((Departamento) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Departamento{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", ubigeo='" + getUbigeo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
