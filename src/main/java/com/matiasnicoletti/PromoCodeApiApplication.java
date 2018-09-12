package com.matiasnicoletti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class PromoCodeApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PromoCodeApiApplication.class, args);
	}
}
