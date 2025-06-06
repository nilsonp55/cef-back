package com.ath.adminefectivo;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AdminEfectivoApplication {

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT-5"));
		SpringApplication.run(AdminEfectivoApplication.class, args);
	}
}
