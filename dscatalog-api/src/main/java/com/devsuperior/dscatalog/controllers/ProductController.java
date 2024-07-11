package com.devsuperior.dscatalog.controllers;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.dto.ProductMinDTO;
import com.devsuperior.dscatalog.services.ProductService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/products")
public class ProductController {

  @Autowired
  ProductService productService;

  @GetMapping("/{id}")
  public ResponseEntity<ProductDTO> getProduct(@PathVariable(value = "id") Long id) {
    ProductDTO productDTO = this.productService.findById(id);

    return ResponseEntity.ok(productDTO);
  }

  @GetMapping
  public ResponseEntity<Page<ProductMinDTO>> getProductsByName(
          @RequestParam(value = "name", required = false) String name, Pageable pageable) {

    Page<ProductMinDTO> product = this.productService.searchByNamePageable(name, pageable);
    return ResponseEntity.ok(product);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @PostMapping
  public ResponseEntity<ProductDTO> insert(@RequestBody @Valid ProductDTO request) {
    ProductDTO result = this.productService.insert(request);
    URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
            .buildAndExpand(result.getId()).toUri();

    return ResponseEntity.created(uri).body(result);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @PutMapping("/{id}")
  public ResponseEntity<ProductDTO> update(@PathVariable Long id, @RequestBody @Valid  ProductDTO request) {
    ProductDTO result = this.productService.update(id, request);

    if (result == null) {
      return ResponseEntity.notFound().build();
    }

    return ResponseEntity.ok().body(result);
  }

  @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    this.productService.delete(id);
    return ResponseEntity.noContent().build();
  }

}
