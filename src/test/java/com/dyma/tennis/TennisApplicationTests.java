package com.dyma.tennis;

import com.dyma.tennis.repository.HealthCheckRepository;
import com.dyma.tennis.rest.HealthCheckController;
import com.dyma.tennis.service.HealthcheckService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TennisApplicationTests {
	@Autowired
	private HealthCheckController healthCheckController;
	@Autowired
	private HealthCheckRepository healthCheckRepository;
	@Autowired
	private HealthcheckService healthcheckService;


	@Test
	void contextLoads() {
		Assertions.assertNotNull(healthCheckController);
		Assertions.assertNotNull(healthCheckRepository);
		Assertions.assertNotNull(healthcheckService);

	}

}
