package edu.unl.cc.smilehub.business;

import edu.unl.cc.smilehub.domain.admin.Usuario;
import edu.unl.cc.smilehub.faces.CrudGenericService;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.NoResultException;

@Stateless
public class UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Inject
    CrudGenericService crudGenericService;

    public Usuario save(Usuario usuario) {
        crudGenericService.create(usuario);
        return usuario;
    }

    public Usuario findByIdentificacion(String identificacion) {
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.identificacion = :id", Usuario.class)
                    .setParameter("id", identificacion)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
