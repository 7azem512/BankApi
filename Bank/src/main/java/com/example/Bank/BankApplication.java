package com.example.Bank;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
			title = "Bank API",
			version = "1.0",
			description = "Bank API",
			contact = @Contact(
					name = "hazem saed",
					email = "hazemsaed512@gmail.com"
				),
			license = @License(
					name = "hazem saed",
					url = "https://github.com/7azem512"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Bank API",
				url = "https://github.com/7azem512"
			)
)
public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}

}
