package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.MovieCardDTO;
import com.devsuperior.movieflix.dto.MovieDetailsDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieCardProjection;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

  @Autowired
  MovieRepository repository;

//  @Transactional(readOnly = true)
//  public Page<MovieDetailsDTO> findAllMovies(Long genreId, Pageable pageable) {
////    Page<Movie> movies = repository.findAll(pageable);
////    return movies.map(MovieDetailsDTO::new);
//    Page<Movie> movies = repository.findMoviesByGenre(genreId, pageable);
//  }

  @Transactional(readOnly = true)
  public MovieDetailsDTO findById(Long id) {
    Optional<Movie> result = repository.findById(id);
    if (result.isEmpty()) {
      throw new ResourceNotFoundException("Filme não encontrado");
    }
    Movie movie = result.get();
    return new MovieDetailsDTO(movie);
  }

  @Transactional(readOnly = true)
  public Page<MovieCardDTO> findMoviesByGenre(Long genreId, Pageable pageable) {
    Page<MovieCardProjection> result = repository.findMoviesByGenre(genreId, pageable);
    if (result.isEmpty()) {
      throw new ResourceNotFoundException("Filmes não encontrados");
    }
    return result.map(MovieCardDTO::new);
  }

}
