package com.devsuperior.bds01.services;

import com.devsuperior.bds01.dto.EmployeeDTO;
import com.devsuperior.bds01.entities.Department;
import com.devsuperior.bds01.entities.Employee;
import com.devsuperior.bds01.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmployeeService {

  @Autowired
  EmployeeRepository repository;

  @Transactional(readOnly = true)
  public Page<EmployeeDTO> findAllPaged(Pageable pageable) {
    Page<Employee> list = repository.findAll(pageable);
    return list.map(EmployeeDTO::new);
  }

  @Transactional
  public EmployeeDTO insert(EmployeeDTO dto) {
    Employee entity = new Employee();
    dtoToEntity(dto, entity);
    entity = repository.save(entity);
    return new EmployeeDTO(entity);
  }

  private static void dtoToEntity(EmployeeDTO dto, Employee entity) {
    entity.setName(dto.getName());
    entity.setEmail(dto.getEmail());
    entity.setDepartment(new Department(dto.getDepartmentId(), null));
  }


}
