package dev.tiago.screenMatch.principal;

import dev.tiago.screenMatch.services.ConsumoApi;

import java.util.Scanner;

public class MenuPrincipal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=94b4869b";

    public void exibeMenu(){
        System.out.println("Digite o nome da série: ");
        var nomeSerie = sc.nextLine();
        var jason = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
    }
}
