package com.devsuperior.demo.services;

import com.devsuperior.demo.dto.DepartmentDTO;
import com.devsuperior.demo.services.exceptions.DataIntegrityViolationCustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.demo.dto.EmployeeDTO;
import com.devsuperior.demo.entities.Department;
import com.devsuperior.demo.entities.Employee;
import com.devsuperior.demo.repositories.EmployeeRepository;

import java.util.Optional;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository repository;

	@Autowired
	private DepartmentService departmentService;
	
	@Transactional(readOnly = true)
	public Page<EmployeeDTO> findAll(Pageable pageable) {
		Page<Employee> page = repository.findAll(pageable);
		return page.map(EmployeeDTO::new);
	}

	@Transactional
	public EmployeeDTO insert(EmployeeDTO dto) {

		Optional<Employee> byEmail = repository.findByEmail(dto.getEmail());
		if (byEmail.isPresent()) {
			throw new DataIntegrityViolationCustomException("Email já cadastrado!");
		}
		Employee entity = new Employee();
		copyDtoToEntity(dto, entity);

		entity = repository.save(entity);
		return new EmployeeDTO(entity);
	}

	private void copyDtoToEntity(EmployeeDTO dto, Employee entity) {
		entity.setName(dto.getName());
		entity.setEmail(dto.getEmail());

		DepartmentDTO departmentDTO = departmentService.findById(dto.getDepartmentId());
		Department newDepartment = new Department(departmentDTO);
		entity.setDepartment(newDepartment);
	}
}
