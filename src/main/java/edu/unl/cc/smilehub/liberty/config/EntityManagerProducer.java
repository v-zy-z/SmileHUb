package edu.unl.cc.smilehub.liberty.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@ApplicationScoped
public class EntityManagerProducer {

    @Produces
    @PersistenceContext(unitName = "Smile-Hub") // DEBE coincidir con el nombre en tu persistence.xml
    private EntityManager em;

}