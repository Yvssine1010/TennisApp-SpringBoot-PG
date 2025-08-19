package com.dyma.tennis.rest;

import com.dyma.tennis.HealthCheck;
import com.dyma.tennis.service.HealthcheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @Autowired
    private HealthcheckService healthcheckService;

    @GetMapping("healthcheck")
    public HealthCheck healthcheck() {
        return  healthcheckService.healthcheck();
    }

}