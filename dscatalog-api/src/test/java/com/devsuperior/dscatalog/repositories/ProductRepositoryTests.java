package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factory.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

  @Autowired
  ProductRepository productRepository;

  Long existingId = null;
  Long notExistingId = null;

  @BeforeEach
  public void config() {
    System.out.println("config() -> OK");
    existingId = 1L;
    notExistingId = 1000L;
  }

  @Test
  public void shouldPersistWithAutoIncrement() {

    Product product = Factory.createProduct();
    product.setId(null);
    Product productSaved = productRepository.save(product);

    Assertions.assertNotNull(productSaved.getId());
    Assertions.assertEquals(26, productSaved.getId());
  }

  @Test
  public void deleteShouldDeleteObjectWhenIdExists() {

    productRepository.deleteById(existingId);
    Optional<Product> productOptional = productRepository.findById(existingId);
    Assertions.assertFalse(productOptional.isPresent());
  }

  @Test
  public void shouldReturnObjectWhenIdExists() {
    Optional<Product> productOptional = productRepository.findById(existingId);
    Assertions.assertTrue(productOptional.isPresent());
  }

  @Test
  public void shouldReturnObjectNullWhenIdNotExists() {
    Optional<Product> productOptional = productRepository.findById(notExistingId);
    Assertions.assertTrue(productOptional.isEmpty());
  }

}
