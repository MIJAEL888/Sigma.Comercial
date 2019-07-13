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
 * A Sede.
 */
@Entity
@Table(name = "sede")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Sede implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "direccion", nullable = false)
    private String direccion;

    @Column(name = "referencia")
    private String referencia;

    @Column(name = "latitud")
    private String latitud;

    @Column(name = "longitud")
    private String longitud;

    @Lob
    @Column(name = "actividad")
    private String actividad;

    @Column(name = "telefono")
    private String telefono;

    @Lob
    @Column(name = "descripcion")
    private String descripcion;

    @Lob
    @Column(name = "comentario")
    private String comentario;

    @Column(name = "ruta_doc_estudio")
    private String rutaDocEstudio;

    @Column(name = "nombre_doc_estudio")
    private String nombreDocEstudio;

    @Lob
    @Column(name = "documento_estudio")
    private byte[] documentoEstudio;

    @Column(name = "documento_estudio_content_type")
    private String documentoEstudioContentType;

    @OneToMany(mappedBy = "sede")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<ContactoSede> contactoSedes = new HashSet<>();

    @OneToMany(mappedBy = "sede")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Servicio> servicios = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("sedes")
    private Cliente cliente;

    @ManyToOne
    @JsonIgnoreProperties("sedes")
    private Distrito distrito;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public Sede direccion(String direccion) {
        this.direccion = direccion;
        return this;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getReferencia() {
        return referencia;
    }

    public Sede referencia(String referencia) {
        this.referencia = referencia;
        return this;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getLatitud() {
        return latitud;
    }

    public Sede latitud(String latitud) {
        this.latitud = latitud;
        return this;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public Sede longitud(String longitud) {
        this.longitud = longitud;
        return this;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getActividad() {
        return actividad;
    }

    public Sede actividad(String actividad) {
        this.actividad = actividad;
        return this;
    }

    public void setActividad(String actividad) {
        this.actividad = actividad;
    }

    public String getTelefono() {
        return telefono;
    }

    public Sede telefono(String telefono) {
        this.telefono = telefono;
        return this;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Sede descripcion(String descripcion) {
        this.descripcion = descripcion;
        return this;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getComentario() {
        return comentario;
    }

    public Sede comentario(String comentario) {
        this.comentario = comentario;
        return this;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getRutaDocEstudio() {
        return rutaDocEstudio;
    }

    public Sede rutaDocEstudio(String rutaDocEstudio) {
        this.rutaDocEstudio = rutaDocEstudio;
        return this;
    }

    public void setRutaDocEstudio(String rutaDocEstudio) {
        this.rutaDocEstudio = rutaDocEstudio;
    }

    public String getNombreDocEstudio() {
        return nombreDocEstudio;
    }

    public Sede nombreDocEstudio(String nombreDocEstudio) {
        this.nombreDocEstudio = nombreDocEstudio;
        return this;
    }

    public void setNombreDocEstudio(String nombreDocEstudio) {
        this.nombreDocEstudio = nombreDocEstudio;
    }

    public byte[] getDocumentoEstudio() {
        return documentoEstudio;
    }

    public Sede documentoEstudio(byte[] documentoEstudio) {
        this.documentoEstudio = documentoEstudio;
        return this;
    }

    public void setDocumentoEstudio(byte[] documentoEstudio) {
        this.documentoEstudio = documentoEstudio;
    }

    public String getDocumentoEstudioContentType() {
        return documentoEstudioContentType;
    }

    public Sede documentoEstudioContentType(String documentoEstudioContentType) {
        this.documentoEstudioContentType = documentoEstudioContentType;
        return this;
    }

    public void setDocumentoEstudioContentType(String documentoEstudioContentType) {
        this.documentoEstudioContentType = documentoEstudioContentType;
    }

    public Set<ContactoSede> getContactoSedes() {
        return contactoSedes;
    }

    public Sede contactoSedes(Set<ContactoSede> contactoSedes) {
        this.contactoSedes = contactoSedes;
        return this;
    }

    public Sede addContactoSede(ContactoSede contactoSede) {
        this.contactoSedes.add(contactoSede);
        contactoSede.setSede(this);
        return this;
    }

    public Sede removeContactoSede(ContactoSede contactoSede) {
        this.contactoSedes.remove(contactoSede);
        contactoSede.setSede(null);
        return this;
    }

    public void setContactoSedes(Set<ContactoSede> contactoSedes) {
        this.contactoSedes = contactoSedes;
    }

    public Set<Servicio> getServicios() {
        return servicios;
    }

    public Sede servicios(Set<Servicio> servicios) {
        this.servicios = servicios;
        return this;
    }

    public Sede addServicio(Servicio servicio) {
        this.servicios.add(servicio);
        servicio.setSede(this);
        return this;
    }

    public Sede removeServicio(Servicio servicio) {
        this.servicios.remove(servicio);
        servicio.setSede(null);
        return this;
    }

    public void setServicios(Set<Servicio> servicios) {
        this.servicios = servicios;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Sede cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Distrito getDistrito() {
        return distrito;
    }

    public Sede distrito(Distrito distrito) {
        this.distrito = distrito;
        return this;
    }

    public void setDistrito(Distrito distrito) {
        this.distrito = distrito;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sede)) {
            return false;
        }
        return id != null && id.equals(((Sede) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Sede{" +
            "id=" + getId() +
            ", direccion='" + getDireccion() + "'" +
            ", referencia='" + getReferencia() + "'" +
            ", latitud='" + getLatitud() + "'" +
            ", longitud='" + getLongitud() + "'" +
            ", actividad='" + getActividad() + "'" +
            ", telefono='" + getTelefono() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            ", comentario='" + getComentario() + "'" +
            ", rutaDocEstudio='" + getRutaDocEstudio() + "'" +
            ", nombreDocEstudio='" + getNombreDocEstudio() + "'" +
            ", documentoEstudio='" + getDocumentoEstudio() + "'" +
            ", documentoEstudioContentType='" + getDocumentoEstudioContentType() + "'" +
            "}";
    }
}
