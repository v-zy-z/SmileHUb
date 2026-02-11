package edu.unl.cc.smilehub.domain.admin;

public class Rol {

    private Long id;
    private TipoRol tipoRol;

    public Rol() {}

    public Rol(Long id, TipoRol tipoRol) {
        this.id = id;
        this.tipoRol = tipoRol;
    }

    public Long getId() {
        return id;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }
}
