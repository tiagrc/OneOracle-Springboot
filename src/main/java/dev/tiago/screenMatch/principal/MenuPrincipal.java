package dev.tiago.screenMatch.principal;

import dev.tiago.screenMatch.model.DadosEpisodio;
import dev.tiago.screenMatch.model.DadosSerie;
import dev.tiago.screenMatch.model.DadosTemporada;
import dev.tiago.screenMatch.model.Episodio;
import dev.tiago.screenMatch.services.ConsumoApi;
import dev.tiago.screenMatch.services.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MenuPrincipal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=94b4869b";

    public void exibeMenu(){
        System.out.println("Digite o nome da série: ");
        var nomeSerie = sc.nextLine();
        var jason = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(jason, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

        for (int i=1; i<=dados.totalTemporadas(); i++){
            jason = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            DadosTemporada dadosTemporada = conversor.obterDados(jason, DadosTemporada.class);
            temporadas.add(dadosTemporada);
        }
        temporadas.forEach(System.out::println);

//        for (int i=0; i< dados.totalTemporadas(); i++){
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//            for (int j = 0; j< episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());


        System.out.println("\nTop 5 episodios!");
        dadosEpisodios.stream().filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed()).limit(5).forEach(System.out::println);


        Stream<Episodio> episodios = temporadas.stream().flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.nmrTemporada(), d)));

        episodios.forEach(System.out::println);

    }
}
