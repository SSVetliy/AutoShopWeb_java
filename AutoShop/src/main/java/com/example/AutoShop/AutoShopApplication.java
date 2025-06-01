package com.example.AutoShop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoShopApplication {
	public static void main(String[] args) {
		DB.init();
		SpringApplication.run(AutoShopApplication.class, args);
	}
}
