package com.example.protec.health;

import java.time.Instant;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
    private final HealthDatabaseProvider healthDatabaseProvider;

    public HealthController(HealthDatabaseProvider healthDatabaseProvider)
    {
        this.healthDatabaseProvider = healthDatabaseProvider;
    }

    @GetMapping("/health")
    public HealthResponse response()
    {
        return new HealthResponse(
            "UP",
            "PROTEC JAVA API",
            "0.0.1",
            Instant.now(),
            "17.0.16",
            "4.1.0",
            "development",
            List.of(healthDatabaseProvider.check())
        );
    }
}
