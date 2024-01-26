package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.dto.CategoryDTO;
import com.devsuperior.dscatalog.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/categories")
public class CategoryController {

  @Autowired
  private CategoryService categoryService;

  @GetMapping
  public ResponseEntity<Page<CategoryDTO>> getCategories(Pageable pageable) {

    Page<CategoryDTO> result = this.categoryService.findAll(pageable);
    return ResponseEntity.ok(result);
  }

}
