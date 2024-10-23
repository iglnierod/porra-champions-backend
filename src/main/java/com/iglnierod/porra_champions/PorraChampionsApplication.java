package com.iglnierod.porra_champions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class PorraChampionsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PorraChampionsApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						// Permitir todas las IPs
						.allowedOrigins("*")
						// Permitir solo los métodos GET, POST y PUT
						.allowedMethods("GET", "POST", "PUT")
						.allowedHeaders("*")
						.allowCredentials(false); // No se necesita permitir credenciales ya que se permiten todas las IPs
			}
		};
	}
}
