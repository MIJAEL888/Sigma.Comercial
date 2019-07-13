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
 * A Distrito.
 */
@Entity
@Table(name = "distrito")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Distrito implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "ubigeo")
    private String ubigeo;

    @Column(name = "descripcion")
    private String descripcion;

    @OneToMany(mappedBy = "distrito")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sede> sedes = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("distritos")
    private Provincia provincia;

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

    public Distrito nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public Distrito ubigeo(String ubigeo) {
        this.ubigeo = ubigeo;
        return this;
    }

    public void setUbigeo(String ubigeo) {
        this.ubigeo = ubigeo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Distrito descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Sede> getSedes() {
        return sedes;
    }

    public Distrito sedes(Set<Sede> sedes) {
        this.sedes = sedes;
        return this;
    }

    public Distrito addSede(Sede sede) {
        this.sedes.add(sede);
        sede.setDistrito(this);
        return this;
    }

    public Distrito removeSede(Sede sede) {
        this.sedes.remove(sede);
        sede.setDistrito(null);
        return this;
    }

    public void setSedes(Set<Sede> sedes) {
        this.sedes = sedes;
    }

    public Provincia getProvincia() {
        return provincia;
    }

    public Distrito provincia(Provincia provincia) {
        this.provincia = provincia;
        return this;
    }

    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Distrito)) {
            return false;
        }
        return id != null && id.equals(((Distrito) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Distrito{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", ubigeo='" + getUbigeo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
}
