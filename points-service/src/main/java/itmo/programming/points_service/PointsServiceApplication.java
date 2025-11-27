package itmo.programming.points_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {})
public class PointsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PointsServiceApplication.class, args);
	}

}
