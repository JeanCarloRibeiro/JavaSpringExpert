package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.dto.ReviewDTO;
import com.devsuperior.movieflix.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

  @Query(nativeQuery = false, value =
          "SELECT new com.devsuperior.movieflix.dto.ReviewDTO(id, text, movie.id, user.id, user.name, user.email) " +
          "FROM Review WHERE movie.id = :movieId")
  List<ReviewDTO> findAllByMovieId(Long movieId);

}
