package com.devsuperior.movieflix.repositories;

import com.devsuperior.movieflix.entities.Movie;
import com.devsuperior.movieflix.projections.MovieCardProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {


  @Query(nativeQuery = true, value =
          "SELECT * FROM ( " +
              "SELECT distinct m.id as id, m.title as title, m.sub_title as subTitle, " +
                  "m.movie_year as movieYear, m.img_url as imgUrl " +
              "FROM tb_movie m WHERE m.genre_id = NVL(:genreId, m.genre_id)) AS tb_result",
         countQuery =
           "SELECT COUNT(*) FROM ( " +
               "SELECT distinct m.id as id, m.title as title, m.sub_title as subTitle, " +
                    "m.movie_year as movieYear, m.img_url as imgUrl " +
               "FROM tb_movie m WHERE m.genre_id = NVL(:genreId, m.genre_id)) AS tb_result")
  Page<MovieCardProjection> findMoviesByGenre(Long genreId, Pageable pageable);

}
