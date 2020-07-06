package com.viniciusmello.springrest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EntityScan(basePackages = {"com.viniciusmello.springrest.model"})
@ComponentScan(basePackages = {"com.viniciusmello.*"}) 
@EnableJpaRepositories(basePackages = {"com.viniciusmello.springrest.repository"})
@EnableTransactionManagement 
@EnableWebMvc
@RestController
@SpringBootApplication
@EnableAutoConfiguration
public class CursospringrestapiApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(CursospringrestapiApplication.class, args);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("*")
			.allowedMethods("*")
			.allowedOrigins("*");
		
		WebMvcConfigurer.super.addCorsMappings(registry);
	}
}
