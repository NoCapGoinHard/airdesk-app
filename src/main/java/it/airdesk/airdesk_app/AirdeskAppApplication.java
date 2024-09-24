package it.airdesk.airdesk_app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AirdeskAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(AirdeskAppApplication.class, args);
	}

}
