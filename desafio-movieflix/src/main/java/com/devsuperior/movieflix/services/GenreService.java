package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.GenreDTO;
import com.devsuperior.movieflix.entities.Genre;
import com.devsuperior.movieflix.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenreService {

  @Autowired
  GenreRepository repository;

  public List<GenreDTO> findAllGenres() {
    List<Genre> genres = repository.findAll();
    return genres.stream().map(GenreDTO::new).collect(Collectors.toList());
  }

}
