package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factory.Factory;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

  @InjectMocks
  private ProductService productService;

  @Mock
  private ProductRepository productRepository;

  @Mock
  CategoryRepository categoryRepository;

  Long existingId;
  Long notExistingId;
  Long dependentId;
  private Product product;
  private PageImpl<Product> page;
  private Category category;


  @BeforeEach
  public void setUp() {

    existingId = 1L;
    notExistingId = 1000L;
    dependentId = 2L;
    product = Factory.createProduct();
    page = new PageImpl<>(List.of(product));
    category = Factory.createCategory();

    when(productRepository.findAll((Pageable)any())).thenReturn(page);
    when(productRepository.save(any())).thenReturn(product);

    when(productRepository.findById(existingId)).thenReturn(Optional.of(product));
    when(productRepository.findById(notExistingId)).thenReturn(Optional.empty());

    when(productRepository.existsById(existingId)).thenReturn(true);
    when(productRepository.existsById(notExistingId)).thenReturn(false);
    when(productRepository.existsById(dependentId)).thenReturn(true);

    when(productRepository.getReferenceById(existingId)).thenReturn(product);
    when(categoryRepository.getReferenceById(dependentId)).thenReturn(category);

    System.out.println("Config OK..");
  }

  @Test
  public void findAllPagedShouldReturnPage() {
    Pageable pageable = PageRequest.of(0, 10);

    Page<ProductDTO> allPaged = productService.findAllPaged(pageable);
    Assertions.assertNotNull(allPaged);
    Mockito.verify(productRepository).findAll(pageable);

  }

  @Test
  public void findByIdShouldReturnWhenIdExists() {
    ProductDTO productDTO = productService.findById(existingId);

    Assertions.assertNotNull(productDTO);
    Mockito.verify(productRepository).findById(existingId);
  }

  @Test
  public void findByIdShouldReturnNotFoundWhenIdNotExists() {

    Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.findById(notExistingId));
    Mockito.verify(productRepository).findById(notExistingId);
  }

  @Test
  public void updateShouldReturnObjectWhenIdExists() {

    ProductDTO productDTO = productService.update(existingId, Factory.createProductDTO());
    Assertions.assertNotNull(productDTO);
  }

  @Test
  public void updateShouldReturnNotFoundWhenIdNotExists() {
    ProductDTO productDTO = Factory.createProductDTO();
    Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.update(notExistingId, productDTO));
  }

}
