package edu.unl.cc.smilehub.view.security;

import edu.unl.cc.smilehub.domain.admin.TipoRol;
import edu.unl.cc.smilehub.domain.admin.Usuario;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class AuthenticationController implements Serializable {

    @PersistenceContext
    private EntityManager em;

    private String identificacion;
    private String password;
    private Usuario usuarioLogueado;

    public String login() {
        try {
            System.out.println("Iniciando login....");
            System.out.println("Identificacion: " + identificacion + ", password: " + password);
            List<Usuario> usuarios = em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.identificacion = :id AND u.password = :pass", Usuario.class)
                    .setParameter("id", identificacion)
                    .setParameter("pass", password)
                    .getResultList();
            System.out.println("Usuarios encontrados: " + usuarios.size());
            if (!usuarios.isEmpty() && usuarios.size() > 1) {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuarios muchos"));
                return null;
            }
            usuarioLogueado = usuarios.get(0);
            // Guardamos el objeto completo en la sesión de JSF
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuarioLogueado);

            System.out.println("Usuario logueado con rol: " + usuarioLogueado.getRol());
            // Redirección por Rol
            if (usuarioLogueado.getRol() == TipoRol.SECRETARIA) return "/dashboardsecretaria?faces-redirect=true";
            if (usuarioLogueado.getRol() == TipoRol.PACIENTE) return "/dashboardpaciente?faces-redirect=true";

            return "/dashboard?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Usuario o clave incorrectos"));
            return null;
        }
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "login?faces-redirect=true";
    }

    // Getters y Setters...
    public String getIdentificacion() { return identificacion; }
    public void setIdentificacion(String identificacion) { this.identificacion = identificacion; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Usuario getUsuarioLogueado() { return usuarioLogueado; }
}
