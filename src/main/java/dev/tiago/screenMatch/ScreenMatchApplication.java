package dev.tiago.screenMatch;

import dev.tiago.screenMatch.model.DadoSerie;
import dev.tiago.screenMatch.services.ConsumoApi;
import dev.tiago.screenMatch.services.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public abstract class ScreenMatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenMatchApplication.class, args);
	}

	@Override
	public void run (String... args) throws Exception{
		var consumoApi = new ConsumoApi();
		var jason = consumoApi.obterDados("https://www.omdbapi.com/?t=prison+break&apikey=94b4869b");
		System.out.println();
		System.out.println(jason);

		var converteDados = new ConverteDados();
		DadoSerie dados = converteDados.obterDados(jason, DadoSerie.class);
		System.out.println(dados);
	}

}
