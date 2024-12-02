package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.entities.City;
import com.devsuperior.demo.entities.Event;
import com.devsuperior.demo.repositories.EventRepository;
import com.devsuperior.demo.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventService {

  @Autowired
  EventRepository eventRepository;

  public Page<EventDTO> findAll(Pageable page) {
    Page<Event> result = this.eventRepository.findAll(page);
    return result.map(EventDTO::new);
  }

  public EventDTO findById(Long id) {
    Optional<Event> result = this.eventRepository.findById(id);
    return new EventDTO(result.orElseThrow(() -> new ResourceNotFoundException("Entity not found")));
  }

  public EventDTO insert(EventDTO eventDTO) {
    Event eventEntity = new Event();
    copyDtoToEntity(eventDTO, eventEntity);
    Event saved = eventRepository.save(eventEntity);
    return new EventDTO(saved);
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
