package dev.tiago.screenMatch.principal;

import dev.tiago.screenMatch.model.DadosSerie;
import dev.tiago.screenMatch.model.DadosTemporada;
import dev.tiago.screenMatch.services.ConsumoApi;
import dev.tiago.screenMatch.services.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

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
    }
}
