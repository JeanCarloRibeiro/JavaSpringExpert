package com.devsuperior.movieflix.services;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.entities.Review;
import com.devsuperior.movieflix.entities.User;
import com.devsuperior.movieflix.repositories.MovieRepository;
import com.devsuperior.movieflix.repositories.ReviewRepository;
import com.devsuperior.movieflix.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewService {

  @Autowired
  ReviewRepository repository;

  @Autowired
  MovieRepository movieRepository;

  @Autowired
  AuthService authService;

  @Transactional(readOnly = true)
  public List<ReviewDTO> findAll() {
    List<Review> result = repository.findAll();
    return result.stream().map(ReviewDTO::new).collect(Collectors.toList());
  }

  @Transactional(readOnly = true)
  public ReviewDTO findById(Long id) {
    Optional<Review> review = repository.findById(id);
    if (review.isEmpty()) {
      throw new ResourceNotFoundException("Review não encontrado");
    }
    return new ReviewDTO(review.get());
  }

  @Transactional(readOnly = true)
  public List<ReviewDTO> findAllByMovie(Long movieId) {
    List<ReviewDTO> result = repository.findAllByMovieId(movieId);
    return result;
  }

  public ReviewDTO createNewReview(ReviewDTO body) {
    Long movieId = body.getMovieId();
    Optional<Movie> result = movieRepository.findById(movieId);
    if (result.isEmpty()) {
      throw new ResourceNotFoundException("Movie não encontrado");
    }
    Movie movie = result.get();

    Review review = new Review(body);
    review.setMovie(movie);

    User user = authService.authenticated();
    review.setUser(user);

    Review newReview = repository.save(review);
    return new ReviewDTO(newReview);
  }
}
