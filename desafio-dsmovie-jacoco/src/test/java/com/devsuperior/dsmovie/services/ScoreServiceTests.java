package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.dto.ScoreDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.entities.ScoreEntity;
import com.devsuperior.dsmovie.entities.UserEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.repositories.ScoreRepository;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import com.devsuperior.dsmovie.tests.ScoreFactory;
import com.devsuperior.dsmovie.tests.UserFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class ScoreServiceTests {
	
	@InjectMocks
	private ScoreService service;

	@Mock
	private ScoreRepository scoreRepository;

	@Mock
	private UserService userService;

	@Mock
	private MovieRepository movieRepository;

	private UserEntity user;
	private MovieEntity movie;
	private ScoreDTO scoreDTO;
	private Long existingMovieId;
	private Long nonExistingMovieId;

	@BeforeEach
	void setUp() {
		user = UserFactory.createUserEntity();
		movie = MovieFactory.createMovieEntity();

		existingMovieId = 1L;
		nonExistingMovieId = 2L;

		ScoreEntity scoreEntity = ScoreFactory.createScoreEntity();
		scoreDTO = ScoreFactory.createScoreDTO();

		Mockito.when(userService.authenticated()).thenReturn(user);

		Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));
		Mockito.when(movieRepository.findById(nonExistingMovieId)).thenReturn(Optional.empty());
		Mockito.when(scoreRepository.saveAndFlush(any())).thenReturn(scoreEntity);
		Mockito.when(movieRepository.save(any())).thenReturn(movie);
	}

	@Test
	public void saveScoreShouldReturnMovieDTO() {
		ScoreEntity scoreEntity = new ScoreEntity();
		scoreEntity.setMovie(movie);
		scoreEntity.setUser(user);
		scoreEntity.setValue(100000.0);
		movie.getScores().add(scoreEntity);

		Mockito.when(movieRepository.findById(existingMovieId)).thenReturn(Optional.of(movie));

		MovieDTO movieDTO = service.saveScore(scoreDTO);
		Assertions.assertNotNull(movieDTO);
	}
	
	@Test
	public void saveScoreShouldThrowResourceNotFoundExceptionWhenNonExistingMovieId() {
		ScoreDTO scoreDTO = new ScoreDTO(nonExistingMovieId, 2.0);
		Assertions.assertThrows(ResourceNotFoundException.class, ()-> service.saveScore(scoreDTO));
	}
}
