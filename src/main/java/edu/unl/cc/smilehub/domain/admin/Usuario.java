package edu.unl.cc.smilehub.domain.admin;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String identificacion;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_identificacion")
    private TipoIdentificacion tipoIdentificacion;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private TipoRol rol;

    // El constructor protegido está perfecto para JPA
    protected Usuario() {
    }

    public Usuario(String identificacion,
                   TipoIdentificacion tipoIdentificacion,
                   String password,
                   TipoRol rol) {
        this.identificacion = identificacion;
        this.tipoIdentificacion = tipoIdentificacion;
        this.password = password;
        this.rol = rol;
    }

    // --- GETTERS Y SETTERS (Indispensables para que JPA funcione) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public TipoIdentificacion getTipoIdentificacion() {
        return tipoIdentificacion;
    }

    public void setTipoIdentificacion(TipoIdentificacion tipoIdentificacion) {
        this.tipoIdentificacion = tipoIdentificacion;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public TipoRol getRol() {
        return rol;
    }

    public void setRol(TipoRol rol) {
        this.rol = rol;
    }
    // Truco para que la vista no falle al pedir "name"
    public String getName() {
        return this.identificacion;
    }
}