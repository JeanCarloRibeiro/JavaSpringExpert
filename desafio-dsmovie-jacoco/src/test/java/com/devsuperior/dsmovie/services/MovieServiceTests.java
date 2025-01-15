package com.devsuperior.dsmovie.services;

import com.devsuperior.dsmovie.dto.MovieDTO;
import com.devsuperior.dsmovie.entities.MovieEntity;
import com.devsuperior.dsmovie.repositories.MovieRepository;
import com.devsuperior.dsmovie.services.exceptions.DatabaseException;
import com.devsuperior.dsmovie.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dsmovie.tests.MovieFactory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(SpringExtension.class)
public class MovieServiceTests {
	
	@InjectMocks
	private MovieService service;

	@Mock
	private MovieRepository repository;

	private MovieEntity movie;
	private MovieDTO movieDTO;
	Page<MovieEntity> moviePage;

	private Long existingMovieId = 1L;
	private Long nonExistingMovieId = 2L;
	private Long dependentMovieId = 3L;

	@BeforeEach
	void setUp() {
		movie = MovieFactory.createMovieEntity();
		movieDTO = new MovieDTO(movie);

		moviePage = new PageImpl<>(List.of(movie));

		Mockito.when(repository.searchByTitle(anyString(), any(Pageable.class))).thenReturn(moviePage);

		Mockito.when(repository.findById(existingMovieId)).thenReturn(Optional.of(movie));
		Mockito.when(repository.findById(nonExistingMovieId)).thenReturn(Optional.empty());

		Mockito.when(repository.save(any())).thenReturn(movie);
		Mockito.when(repository.getReferenceById(existingMovieId)).thenReturn(movie);
		Mockito.when(repository.getReferenceById(nonExistingMovieId)).thenThrow(EntityNotFoundException.class);

		Mockito.when(repository.existsById(existingMovieId)).thenReturn(true);
		Mockito.doNothing().when(repository).deleteById(existingMovieId);
		Mockito.when(repository.existsById(dependentMovieId)).thenReturn(true);
		Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentMovieId);
	}

	@Test
	public void findAllShouldReturnPagedMovieDTO() {
		Pageable pageable = PageRequest.of(0, 12);
		Page<MovieDTO> result = service.findAll("teste", pageable);

		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldReturnMovieDTOWhenIdExists() {
		MovieDTO result = service.findById(existingMovieId);
		Assertions.assertNotNull(result);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class,
						()-> service.findById(nonExistingMovieId));
	}
	
	@Test
	public void insertShouldReturnMovieDTO() {
		MovieDTO result = service.insert(movieDTO);
		Assertions.assertNotNull(result);
		Assertions.assertEquals(result.getId(), 1L);
	}
	
	@Test
	public void updateShouldReturnMovieDTOWhenIdExists() {
		MovieDTO result = service.update(existingMovieId, movieDTO);
		Assertions.assertEquals(result.getId(), 1L);
	}

	@Test
	public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, ()->
						service.update(nonExistingMovieId, movieDTO));
	}

	@Test
	public void deleteShouldDoNothingWhenIdExists() {
		Assertions.assertDoesNotThrow( () ->
						service.delete(existingMovieId));
	}

	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		Assertions.assertThrows(ResourceNotFoundException.class, () ->
						service.delete(nonExistingMovieId));
	}

	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		Assertions.assertThrows(DatabaseException.class, () ->
						service.delete(dependentMovieId));
	}
}
