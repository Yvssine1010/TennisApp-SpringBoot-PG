package com.dyma.tennis.repository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class HealthCheckRepository {
    //JPA = contrat/spécification.
    //Hibernate (ou autre) = implémentation réelle.
    //Repository (@Repository) = moyen pratique offert par Spring pour utiliser JPA sans écrire de SQL.
    @Autowired
    private EntityManager entityManager; //l’objet central de JPA qui permet d’exécuter des requêtes SQL ou JPQL
    public Long countApplicationConnection(){
     //Combien de connexions sont actuellement ouvertes dont l’application s’appelle "PostgreSQL JDBC Driver"
        String applicationConnectionsQuery = "SELECT COUNT(*) " +
                "FROM pg_stat_activity " +
                "WHERE application_name = 'PostgreSQL JDBC Driver'";
        return (Long) entityManager.createNativeQuery(applicationConnectionsQuery).getSingleResult();
        //createNativeQuery(...) → permet d’exécuter une requête SQL brute (native).
    }

}
