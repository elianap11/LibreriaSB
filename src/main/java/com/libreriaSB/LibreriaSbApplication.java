package com.libreriaSB;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
//@EnableJpaAuditing
//@EnableAsync
public class LibreriaSbApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibreriaSbApplication.class, args);
	}

}
