package io.skipsave.savings_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = {"io.skipsave.savings_tracker"})
@SpringBootApplication
public class SavingsTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SavingsTrackerApplication.class, args);
	}

}
