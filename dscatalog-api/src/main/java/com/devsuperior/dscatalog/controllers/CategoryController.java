package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<List<CategoryDTO>> getCategories() {
    List<CategoryDTO> result = this.categoryService.findAll();
    return ResponseEntity.ok(result);
  }

  @GetMapping(value = "/pageable")
  public ResponseEntity<Page<CategoryDTO>> getCategoriesPageable(Pageable pageable) {
    Page<CategoryDTO> result = this.categoryService.findAllPageable(pageable);
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @GetMapping(value = "/{id}")
  public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id) {
    CategoryDTO result = this.categoryService.findById(id);
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @PostMapping
  public ResponseEntity<CategoryDTO> insert(@RequestBody @Valid CategoryDTO dto) {
    CategoryDTO result = this.categoryService.insert(dto);
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @PutMapping(value = "/{id}")
  public ResponseEntity<CategoryDTO> update(Long id, @RequestBody CategoryDTO dto) {
    CategoryDTO result = this.categoryService.update(id, dto);
    return ResponseEntity.ok(result);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @DeleteMapping(value = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    this.categoryService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
