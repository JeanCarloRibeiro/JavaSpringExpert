package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.DepartmentDTO;
import com.devsuperior.demo.entities.Department;
import com.devsuperior.demo.repositories.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

	@Autowired
	private DepartmentRepository repository;
	
	public List<DepartmentDTO> findAll() {
		List<Department> list = repository.findAll(Sort.by("name"));
		return list.stream().map(DepartmentDTO::new).toList();
	}

	public Page<DepartmentDTO> findAllPaged(Pageable pageable) {
		Page<Department> list = repository.findAll(pageable);
		return list.map(DepartmentDTO::new);
	}

	public DepartmentDTO findById(Long id) {
		Department department = repository.getReferenceById(id);
      return new DepartmentDTO(department);
    }

}
