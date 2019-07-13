package com.sigma.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A ContactoSede.
 */
@Entity
@Table(name = "contacto_sede")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ContactoSede implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "correo")
    private String correo;

    @Column(name = "posicion")
    private String posicion;

    @ManyToOne
    @JsonIgnoreProperties("contactoSedes")
    private Sede sede;

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

    public ContactoSede nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public ContactoSede telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public ContactoSede correo(String correo) {
        this.correo = correo;
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPosicion() {
        return posicion;
    }

    public ContactoSede posicion(String posicion) {
        this.posicion = posicion;
        return this;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public Sede getSede() {
        return sede;
    }

    public ContactoSede sede(Sede sede) {
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
        if (!(o instanceof ContactoSede)) {
            return false;
        }
        return id != null && id.equals(((ContactoSede) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ContactoSede{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", posicion='" + getPosicion() + "'" +
            "}";
    }
}
