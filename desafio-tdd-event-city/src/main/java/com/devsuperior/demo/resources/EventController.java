package com.devsuperior.demo.resources;

import com.devsuperior.demo.dto.CityDTO;
import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.services.CityService;
import com.devsuperior.demo.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/events")
public class EventController {

  @Autowired
  EventService eventService;

  @GetMapping
  public ResponseEntity<List<EventDTO>> findAll() {
    List<EventDTO> result = this.eventService.findAll();
    return ResponseEntity.ok().body(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventDTO> findById(@PathVariable(name = "id") Long id) {
    EventDTO result = eventService.findById(id);
    return ResponseEntity.ok().body(result);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<EventDTO> update(@PathVariable Long id, @RequestBody EventDTO dto) {
    dto = eventService.update(id, dto);
    return ResponseEntity.ok().body(dto);
  }

}
