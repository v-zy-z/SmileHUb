package edu.unl.cc.smilehub.business;

import edu.unl.cc.smilehub.domain.admin.Usuario;
import edu.unl.cc.smilehub.exception.CredentialInvalidException;
import edu.unl.cc.smilehub.exception.EntityNotFoundException;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

@Stateless
public class SecurityFacade {

    @Inject
    private UserRepository userRepository;

    public Usuario authenticate(String identificacion, String password)
            throws EntityNotFoundException, CredentialInvalidException {

        Usuario usuario = userRepository.findByIdentificacion(identificacion);

        if (usuario == null) {
            throw new EntityNotFoundException("Usuario no encontrado");
        }

        if (!usuario.getPassword().equals(password)) {
            throw new CredentialInvalidException("Credenciales incorrectas");
        }

        return usuario;
    }
}