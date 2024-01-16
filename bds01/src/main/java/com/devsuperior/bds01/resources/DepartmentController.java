package com.devsuperior.bds01.resources;

import com.devsuperior.bds01.dto.DepartmentDTO;
import com.devsuperior.bds01.services.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/departments")
public class DepartmentController {

  @Autowired
  DepartmentService service;

  @GetMapping
  public ResponseEntity<List<DepartmentDTO>> findAll() {
    List<DepartmentDTO> result = service.findAll();
    return ResponseEntity.ok().body(result);
  }

  @GetMapping("/paged")
  public ResponseEntity<Page<DepartmentDTO>> findAllPaged(Pageable pageable) {
    Page<DepartmentDTO> result = service.findAllPaged(pageable);
    return ResponseEntity.ok().body(result);
  }

}
