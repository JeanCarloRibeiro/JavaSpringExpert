package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTests {

  @Autowired
  private ProductService productService;

  @Autowired
  private ProductRepository productRepository;

  Long existingId;
  Long notExistingId;
  Long dependentId;
  Long countTotalProducts;
  private Product product;
  private PageImpl<Product> page;
  private Category category;

  @BeforeEach
  public void setUp() {

    existingId = 1L;
    notExistingId = 1000L;
    countTotalProducts = 25L;

    System.out.println("Config OK..");
  }

  @Test
  public void deleteShouldDeleteWhenIdExists() {
    productService.delete(existingId);
    Assertions.assertEquals(countTotalProducts-1, productRepository.count());
  }

  @Test
  public void deleteShouldNotFoundWhenIdNotExist() {
    Assertions.assertThrows(ResourceNotFoundException.class, () -> productService.delete(notExistingId));
  }

  @Test
  public void findAllPagedShouldReturnPage() {
    Pageable pageable = PageRequest.of(0, 10);

    Page<ProductDTO> result = productService.findAllPaged(pageable);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(0, result.getNumber());
    Assertions.assertEquals(countTotalProducts, result.getTotalElements());
  }

  @Test
  public void findAllPagedShouldReturnEmptyWhenPageDoesNotExist() {
    Pageable pageable = PageRequest.of(50, 10);

    Page<ProductDTO> result = productService.findAllPaged(pageable);
    Assertions.assertTrue(result.isEmpty());
  }

  @Test
  public void findAllPagedShouldReturnSortedPageWhenSortByName() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

    Page<ProductDTO> allPaged = productService.findAllPaged(pageable);
    Assertions.assertFalse(allPaged.isEmpty());
    Assertions.assertEquals("Macbook Pro", allPaged.getContent().get(0).getName());
    Assertions.assertEquals("PC Gamer", allPaged.getContent().get(1).getName());
    Assertions.assertEquals("PC Gamer Alfa", allPaged.getContent().get(2).getName());

  }

}
