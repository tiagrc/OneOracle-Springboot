package dev.tiago.screenMatch;

import dev.tiago.screenMatch.model.DadosEpisodio;
import dev.tiago.screenMatch.model.DadosSerie;
import dev.tiago.screenMatch.model.DadosTemporada;
import dev.tiago.screenMatch.services.ConsumoApi;
import dev.tiago.screenMatch.services.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

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
		System.out.println();

		var converteDados = new ConverteDados();
		DadosSerie dados = converteDados.obterDados(jason, DadosSerie.class);
		System.out.println(dados);
		System.out.println();

		jason = consumoApi.obterDados("https://www.omdbapi.com/?t=prison+break&season=1&episode=1&apikey=94b4869b");
		DadosEpisodio dadosEpisodio = converteDados.obterDados(jason, DadosEpisodio.class);
		System.out.println(dadosEpisodio);

		List<DadosTemporada> temporadas = new ArrayList<>();

		for (int i=1; i<=dados.totalTemporadas(); i++){
			jason = consumoApi.obterDados("https://www.omdbapi.com/?t=prison+break&season=" + i + "&apikey=94b4869b");
			DadosTemporada dadosTemporada = converteDados.obterDados(jason, DadosTemporada.class);
			temporadas.add(dadosTemporada);
		}
		temporadas.forEach(System.out::println);
	}
}
