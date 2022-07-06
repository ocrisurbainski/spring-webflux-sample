package br.com.urbainski.springwebfluxsample;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "${info.app.name}",
				description = "${info.app.description}",
				version = "${info.app.version}"))
public class SpringWebfluxSampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxSampleApplication.class, args);
	}

}
