package com.dyma.tennis.service;

import com.dyma.tennis.ApplicationStatus;
import com.dyma.tennis.HealthCheck;
import com.dyma.tennis.repository.HealthCheckRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthcheckService {
    @Autowired
    private HealthCheckRepository healthCheckRepository;

    public HealthCheck healthcheck() {
        //Le service appelle le repository.
        //Le repository demande à PostgreSQL : “Combien de connexions actives a cette application ?”
        //Ce chiffre est stocké dans activeSessions pour pouvoir décider ensuite si l’utilisateur ou la requête est autorisé.
        Long activeSessions = healthCheckRepository.countApplicationConnection();
        if (activeSessions > 0) {
            return new HealthCheck(ApplicationStatus.OK,"Welcome,You are ALLOWED");
        }
        else
            return new HealthCheck(ApplicationStatus.KO, "SORRY YOU R NOT ALLOWED!");
    }
}

//Sans le repository, le service ne pourrait pas accéder aux données de la base.
//
//Le service s’occupe de la logique métier, le repository s’occupe de l’accès aux données (principe séparation des responsabilités).