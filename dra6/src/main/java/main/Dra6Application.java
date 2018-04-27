package main;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import storage.StorageProperties;
import storage.StorageService;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
@ComponentScan("storage.*")

public class Dra6Application {

	public static void main(String[] args) {
		SpringApplication.run(Dra6Application.class, args);
	}
	
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
            storageService.deleteAll();
            storageService.init();
		};
	}
}
