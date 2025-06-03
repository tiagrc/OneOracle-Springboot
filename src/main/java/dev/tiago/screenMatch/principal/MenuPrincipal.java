package dev.tiago.screenMatch.principal;

import dev.tiago.screenMatch.model.*;
import dev.tiago.screenMatch.repository.SerieRepository;
import dev.tiago.screenMatch.service.ConsumoApi;
import dev.tiago.screenMatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class MenuPrincipal {
    private Scanner sc = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=94b4869b";
    private List<DadosSerie> dadosSeries = new ArrayList<>();
    private SerieRepository repository;
    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBusca;

    public MenuPrincipal(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {

        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar séries buscadas
                    4 - Buscar série por titulo
                    5 - Buscar séries por ator/atriz
                    6 - Top 5 séries
                    7 - Buscar séries por categoria
                    8 - Filtrar séries
                    9 - Buscar episódio por trecho
                    10 - Top 5 episódios por série
                    
                    0 - Sair
                    """;
            System.out.println(menu);
            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    buscarNaWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    break;
                case 6:
                    BuscarTop5Series();
                    break;
                case 7:
                    BuscarSeriesPorCategoria();
                    break;
                case 8:
                    FiltrarSeriesPorTemporadaEAvaliacao();
                    break;
                case 9:
                    BuscarEposidioPorTrecho();
                    break;
                case 10:
                    topEpisodiosPorSerie();
                    break;
                case 0:
                    System.out.println("Saíndo da aplicação!");
                    break;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private void buscarNaWeb(){
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie(){
        System.out.println("Digite o nome da série para busca: ");
        var nomeSerie = sc.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();
        System.out.println("Escolha uma série pelo nome.");
        var nomeSerie = sc.nextLine();

        Optional<Serie> serie = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios =  temporadas.stream().flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.nmrTemporada(), e)))
                    .collect(Collectors.toList());
            serieEncontrada.setEpisodios(episodios);
            repository.save(serieEncontrada);
        } else {
            System.out.println("Esta série ainda não esta em nosso banco de dados!");
        }
    }
    private void listarSeriesBuscadas(){
        series = repository.findAll();

        series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
    }

    private void buscarSeriePorTitulo() {
        System.out.println("Escolha uma série pelo nome: ");
        var buscarTitulo = sc.nextLine();

        serieBusca = repository.findByTituloContainingIgnoreCase(buscarTitulo);

        if (serieBusca.isPresent()){
            System.out.println("Dados da série: " + serieBusca.get());
        }else {
            System.out.println("Série não encontrada.");
        }
    }

    private void buscarSeriesPorAtor() {
        System.out.println("Digite o nome do ator/atriz: ");
        var nomeAtor = sc.nextLine();
        List<Serie> serieEncontrada = repository.findByAtoresContainingIgnoreCase(nomeAtor);
        System.out.println("Séries produzidas pelo nome buscado: " + nomeAtor);
        serieEncontrada.forEach(s -> System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao()));
    }

    private void BuscarTop5Series(){
        List<Serie> serieTop = repository.findTop5ByOrderByAvaliacaoDesc();
        serieTop.forEach(s -> System.out.println(s.getTitulo() + " - avaliação: " + + s.getAvaliacao()));
    }

    private void BuscarSeriesPorCategoria(){
        System.out.println("Qual o gênero que você deseja buscar: ");
        var nomeGenero = sc.nextLine();
        Categoria categoria = Categoria.fromPortugues(nomeGenero);
        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);

        System.out.println("Séries da da categoria " + nomeGenero);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void FiltrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Filtrar por temporadas:  ");
        var totalTemporadas = sc.nextInt();
        sc.nextLine();
        System.out.println("Avaliação a partir de: ");
        var avaliacao = sc.nextDouble();
        sc.nextLine();

        List<Serie> filtroSeries = repository.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        System.out.println("*** Séries filtradas ***");
        filtroSeries.forEach(s ->
                System.out.println(s.getTitulo() + " - avaliação: " + s.getAvaliacao() + " - contém: " + s.getTotalTemporadas() + " temporadas"));
    }

    private void BuscarEposidioPorTrecho(){
        System.out.println("Qual o trecho do episódio?");
        var trechoEpisodio = sc.nextLine();

        List<Episodio> episodiosEncontrados = repository.episodiosPorTrecho(trechoEpisodio);
        episodiosEncontrados.forEach(e ->
                System.out.printf("Série: %s - Temporada %s - Episódio %s - %s\n",
                        e.getSerie().getTitulo(),
                        e.getTemporada(), e.getNmrEpisodio(), e.getTitulo()));
    }

    private void topEpisodiosPorSerie(){
        buscarSeriePorTitulo();
        if (serieBusca.isPresent()){
            Serie serie = serieBusca.get();
            List<Episodio> topEpisodios = repository.topEpisodiosPorSerie(serie);
            topEpisodios.forEach(t ->
                    System.out.printf("Série: %s - Temporada: %s - Episódio: %s - %s Avaliação: %s\n",
                            t.getSerie().getTitulo(),
                            t.getTemporada(), t.getNmrEpisodio(), t.getTitulo(), t.getAvaliacao()));
        }
    }
}
