package com.devsuperior.demo.resources;

import com.devsuperior.demo.dto.EventDTO;
import com.devsuperior.demo.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping(value = "/events")
public class EventController {

  @Autowired
  EventService eventService;

  @GetMapping
  public ResponseEntity<Page<EventDTO>> findAll(Pageable page) {
    Page<EventDTO> result = this.eventService.findAll(page);
    return ResponseEntity.ok().body(result);
  }

  @GetMapping("/{id}")
  public ResponseEntity<EventDTO> findById(@PathVariable(name = "id") Long id) {
    EventDTO result = eventService.findById(id);
    return ResponseEntity.ok().body(result);
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  public ResponseEntity<EventDTO> save(@Validated @RequestBody EventDTO dto) {
    dto = eventService.insert(dto);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(dto.getId()).toUri();
    return ResponseEntity.created(uri).body(dto);
  }

  @PutMapping(value = "/{id}")
  public ResponseEntity<EventDTO> update(@PathVariable Long id, @RequestBody EventDTO dto) {
    dto = eventService.update(id, dto);
    return ResponseEntity.ok().body(dto);
  }

}
