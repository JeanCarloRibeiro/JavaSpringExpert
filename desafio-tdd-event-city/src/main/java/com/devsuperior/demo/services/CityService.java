package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.repositories.CityRepository;
import com.devsuperior.demo.services.exceptions.DatabaseException;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityService {

  @Autowired
  CityRepository cityRepository;

  public List<CityDTO> findAll() {
    List<City> result = this.cityRepository.findAll(Sort.by("name"));
    return result.stream().map(CityDTO::new).collect(Collectors.toList());
  }

  @Transactional
  public CityDTO insert(CityDTO dto) {
    City entity = new City();
    copyDtoToEntity(dto, entity);
    entity = cityRepository.save(entity);
    return new CityDTO(entity);
  }

  @Transactional(propagation = Propagation.SUPPORTS)
  public void delete(Long id) {
    if (!cityRepository.existsById(id)) {
      throw new ResourceNotFoundException("Id not found " + id);
    }
    try {
      cityRepository.deleteById(id);
    }
    catch (DataIntegrityViolationException e) {
      throw new DatabaseException("Integrity violation");
    }
  }

  private void copyDtoToEntity(CityDTO dto, City entity) {
    entity.setName(dto.getName());

  }



}
