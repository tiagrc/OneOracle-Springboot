package dev.tiago.screenMatch;

import dev.tiago.screenMatch.principal.MenuPrincipal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public abstract class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run (String... args) throws Exception {
		MenuPrincipal principal = new MenuPrincipal();
		principal.exibeMenu();
	}
}
