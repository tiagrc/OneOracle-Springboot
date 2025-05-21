package dev.tiago.screenMatch.model;

import org.springframework.cglib.core.Local;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Episodio {
    private Integer temporada;
    private String titulo;
    private Integer nmrEpisodio;
    private Double avaliacao;
    private LocalDate dataLancamento;

    public Episodio(Integer nmrTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada = nmrTemporada;
        this.titulo = dadosEpisodio.titulo();
        this.nmrEpisodio = dadosEpisodio.nmrEpisodio();

        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
            this.dataLancamento = LocalDate.parse(dadosEpisodio.dataLancamento());
        } catch (NumberFormatException | DateTimeParseException ex){
            this.avaliacao = 0.0;
        }
    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getNmrEpisodio() {
        return nmrEpisodio;
    }

    public void setNmrEpisodio(Integer nmrEpisodio) {
        this.nmrEpisodio = nmrEpisodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    @Override
    public String toString() {
        return "temporada=" + temporada +
                ", titulo='" + titulo + '\'' +
                ", nmrEpisodio=" + nmrEpisodio +
                ", avaliacao=" + avaliacao +
                ", dataLancamento=" + dataLancamento;
    }
}
