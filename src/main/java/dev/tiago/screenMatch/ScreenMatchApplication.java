package dev.tiago.screenMatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public abstract class ScreenMatchApplication{
	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}
}
