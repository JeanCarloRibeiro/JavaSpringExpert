package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factory.Factory;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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

    Page<ProductDTO> allPaged = productService.findAllPaged(pageable);
    Assertions.assertNotNull(allPaged);
    Assertions.assertEquals(0, allPaged.getNumber());
    Assertions.assertEquals(countTotalProducts, allPaged.getTotalElements());
  }

  @Test
  public void findAllPagedShouldReturnEmptyWhenPageDoesNotExist() {
    Pageable pageable = PageRequest.of(50, 10);

    Page<ProductDTO> allPaged = productService.findAllPaged(pageable);
    Assertions.assertTrue(allPaged.isEmpty());
  }

  @Test
  public void findAllPagedShouldReturnSortedPageWhenSortByName() {
    Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

    Page<ProductDTO> allPaged = productService.findAllPaged(pageable);
    Assertions.assertFalse(allPaged.isEmpty());
    Assertions.assertEquals("Macbook Pro", allPaged.getContent().get(0).getName());

  }
 /*
  @Test
  public void findByIdShouldReturnWhenIdExists() {
    ProductDTO productDTO = productService.findById(existingId);

    Assertions.assertNotNull(productDTO);
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
  }*/

}
