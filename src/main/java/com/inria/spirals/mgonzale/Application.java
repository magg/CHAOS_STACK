package com.inria.spirals.mgonzale;

import java.security.SecureRandom;
import java.util.Random;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class Application extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

    @Bean
    @Primary
    ObjectMapper objectMapper() {
        return new ObjectMapper().disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Bean
    Random random() {
        return new SecureRandom();
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

