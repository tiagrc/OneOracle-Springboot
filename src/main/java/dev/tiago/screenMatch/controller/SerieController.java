package dev.tiago.screenMatch.controller;

import dev.tiago.screenMatch.dto.SerieDTO;
import dev.tiago.screenMatch.model.Serie;
import dev.tiago.screenMatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class SerieController {

    @Autowired
    private SerieRepository repository;

    @GetMapping("/series")
    public List<SerieDTO> obterSeries(){
        return repository.findAll().stream().map(s -> new SerieDTO(
                s.getId(),
                s.getTitulo(),
                s.getTotalTemporadas(),
                s.getAvaliacao(),
                s.getGenero(),
                s.getAtores(),
                s.getPoster(),
                s.getSinopse())).collect(Collectors.toList());
    }


}
