package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repositories.EventRepository;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

  @Autowired
  EventRepository eventRepository;

  public List<EventDTO> findAll() {
    List<Event> result = this.eventRepository.findAll(Sort.by("name"));
    return result.stream().map(EventDTO::new).toList();
  }

  public EventDTO findById(Long id) {
    Optional<Event> result = this.eventRepository.findById(id);
    return new EventDTO(result.orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
  }

  @Transactional
  public EventDTO update(Long id, EventDTO dto) {
    try {
      Event entity = this.eventRepository.getReferenceById(id);
      if (entity == null) {
        throw new ResourceNotFoundException("Id not found " + id);
      }
      copyDtoToEntity(dto, entity);
      entity = eventRepository.save(entity);
      return new EventDTO(entity);
    }
    catch (EntityNotFoundException e) {
      throw new ResourceNotFoundException("Id not found " + id);
    }
  }

  private void copyDtoToEntity(EventDTO dto, Event entity) {
    entity.setName(dto.getName());
    entity.setUrl(dto.getUrl());
    entity.setDate(dto.getDate());
    entity.setCity(new City(dto.getCityId(), null));
  }


}
