package edu.unl.cc.smilehub.view.security;

import edu.unl.cc.smilehub.domain.admin.Usuario;
import edu.unl.cc.smilehub.domain.admin.TipoRol;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;

@Named
@SessionScoped
public class UserSession implements Serializable {

    private static final long serialVersionUID = 1L;

    private Usuario usuario;

    public void login(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * MÉTODO ACTUALIZADO
     * Cierra la sesión y redirige al login
     */
    public String logout() {
        // Borra toda la información de la sesión por seguridad
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        this.usuario = null;
        // Redirige a la página de login
        return "/login.xhtml?faces-redirect=true";
    }

    public boolean isLogged() {
        return usuario != null;
    }

    public void checkLogin() throws IOException {
        if (this.usuario == null) {
            // Intento de recuperación de emergencia:
            // A veces el controlador guarda en el mapa pero no actualiza el bean directamente.
            ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
            Object userMap = ec.getSessionMap().get("usuario");

            if (userMap != null && userMap instanceof Usuario) {
                this.usuario = (Usuario) userMap;
            } else {
                // Si definitivamente es nulo, redirigir al login
                ec.redirect(ec.getRequestContextPath() + "/login.xhtml");
            }
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public TipoRol getRol() {
        return usuario != null ? usuario.getRol() : null;
    }

    // Ayuda para vistas
    public boolean isPaciente() {
        return usuario != null && usuario.getRol() == TipoRol.PACIENTE;
    }

    public boolean isDoctor() {
        return usuario != null && usuario.getRol() == TipoRol.DOCTOR;
    }

    public boolean isSecretaria() {
        return usuario != null && usuario.getRol() == TipoRol.SECRETARIA;
    }

    // Este método actúa como puente para que la vista encuentre "user"
    public Usuario getUser() {
        return this.usuario;
    }
}