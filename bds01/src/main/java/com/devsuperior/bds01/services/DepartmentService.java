package com.devsuperior.bds01.services;

import com.devsuperior.bds01.dto.DepartmentDTO;
import com.devsuperior.bds01.entities.Department;
import com.devsuperior.bds01.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DepartmentService {

  @Autowired
  DepartmentRepository repository;

  public List<DepartmentDTO> findAll() {
    List<Department> result = this.repository.findAll(Sort.by("name"));
    return result.stream().map(DepartmentDTO::new).collect(Collectors.toList());
  }

  public Page<DepartmentDTO> findAllPaged(Pageable pageable) {
    Page<Department> result = this.repository.findAll(pageable);
    return result.map(DepartmentDTO::new);

  }
}
