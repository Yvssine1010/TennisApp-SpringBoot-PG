package com.dyma.tennis.rest;

import com.dyma.tennis.HealthCheck;
import com.dyma.tennis.service.HealthcheckService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
// donner un nom à l'API grace a tag
@Tag(name = "HealthCheck API")
@RestController
public class HealthCheckController {

    @Autowired
    private HealthcheckService healthCheckService;

    @Operation(summary = "Returns application status", description = "Returns the application status")//décrit ce que fait ton endpoint
    //les differentes reponses http possible(200, 400, 401, 404, 500, etc.)
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Healthcheck status with some details",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = HealthCheck.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = HealthCheck.class))
                    }
            )
    })

    @GetMapping("/healthcheck")
    public HealthCheck healthcheck() {
        return healthCheckService.healthcheck();
    }
}