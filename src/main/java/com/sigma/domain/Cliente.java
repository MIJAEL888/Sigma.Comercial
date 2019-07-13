package com.sigma.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "razon_social", nullable = false)
    private String razonSocial;

    @NotNull
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @NotNull
    @Column(name = "ruc", nullable = false)
    private String ruc;

    @Column(name = "telefono")
    private String telefono;

    @Column(name = "correo")
    private String correo;

    @Column(name = "nombre_contacto")
    private String nombreContacto;

    @Lob
    @Column(name = "actividad")
    private String actividad;

    @Lob
    @Column(name = "comentario")
    private String comentario;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "codigo_zona")
    private String codigoZona;

    @OneToMany(mappedBy = "cliente")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Sede> sedes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public Cliente razonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
        return this;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getDireccion() {
        return direccion;
    }

    public Cliente direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getRuc() {
        return ruc;
    }

    public Cliente ruc(String ruc) {
        this.ruc = ruc;
        return this;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getTelefono() {
        return telefono;
    }

    public Cliente telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public Cliente correo(String correo) {
        this.correo = correo;
        return this;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreContacto() {
        return nombreContacto;
    }

    public Cliente nombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
        return this;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    public String getActividad() {
        return actividad;
    }

    public Cliente actividad(String actividad) {
        this.actividad = actividad;
        return this;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getComentario() {
        return comentario;
    }

    public Cliente comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public Cliente fechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        return this;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public String getCodigoZona() {
        return codigoZona;
    }

    public Cliente codigoZona(String codigoZona) {
        this.codigoZona = codigoZona;
        return this;
    }

    public void setCodigoZona(String codigoZona) {
        this.codigoZona = codigoZona;
    }

    public Set<Sede> getSedes() {
        return sedes;
    }

    public Cliente sedes(Set<Sede> sedes) {
        this.sedes = sedes;
        return this;
    }

    public Cliente addSede(Sede sede) {
        this.sedes.add(sede);
        sede.setCliente(this);
        return this;
    }

    public Cliente removeSede(Sede sede) {
        this.sedes.remove(sede);
        sede.setCliente(null);
        return this;
    }

    public void setSedes(Set<Sede> sedes) {
        this.sedes = sedes;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cliente)) {
            return false;
        }
        return id != null && id.equals(((Cliente) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + getId() +
            ", razonSocial='" + getRazonSocial() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", ruc='" + getRuc() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", correo='" + getCorreo() + "'" +
            ", nombreContacto='" + getNombreContacto() + "'" +
            ", actividad='" + getActividad() + "'" +
            ", comentario='" + getComentario() + "'" +
            ", fechaCreacion='" + getFechaCreacion() + "'" +
            ", codigoZona='" + getCodigoZona() + "'" +
            "}";
    }
}
