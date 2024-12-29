package com.devsuperior.examplemockspy.services;

import com.devsuperior.examplemockspy.dto.ProductDTO;
import com.devsuperior.examplemockspy.entities.Product;
import com.devsuperior.examplemockspy.repositories.ProductRepository;
import com.devsuperior.examplemockspy.services.exceptions.InvalidDataException;
import com.devsuperior.examplemockspy.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

  @InjectMocks
  ProductService service;

  @Mock
  private ProductRepository repository;

  private Long existingId, nonExistingId;
  private ProductDTO productDTO;
  private Product product;

  @BeforeEach
  void setUp() {
    //given
    product = new Product(1L, "Xbox", 4000.00);
    productDTO = new ProductDTO(product);

    existingId = 1L;
    nonExistingId = 2L;

    //when
    when(repository.save(any())).thenReturn(product);
    when(repository.getReferenceById(existingId)).thenReturn(product);
    when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);
  }

  @Test
  void insertShouldReturnProductDTOWhenValidDate() {
    ProductService serviceSpy = spy(service);
    doNothing().when(serviceSpy).validateData(productDTO);

    ProductDTO result = service.insert(productDTO);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getId(), 1L);
    Assertions.assertEquals(result.getName(), "Xbox");
    Assertions.assertEquals(result.getPrice(), 4000.00);
  }

  @Test
  void insertShouldReturnInvalidDataExceptionWhenProductNameIsBlank() {
    //given
    productDTO.setName("");

    ProductService serviceSpy = spy(service);
    doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

    Assertions.assertThrows(InvalidDataException.class,
            () -> service.insert(productDTO));
  }

  @Test
  void insertShouldReturnInvalidDataExceptionWhenProductPriceIsNegativeOrZero() {
    //given
    productDTO.setPrice(-5.0);

    ProductService serviceSpy = spy(service);
    doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

    Assertions.assertThrows(InvalidDataException.class,
            () -> service.insert(productDTO));
  }

  @Test
  void updateShouldReturnProductDTOWhenIdExistsAndValidDate() {
    ProductService serviceSpy = spy(service);
    doNothing().when(serviceSpy).validateData(productDTO);

    ProductDTO result = service.update(1L, productDTO);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(result.getId(), existingId);
  }

  @Test
  void updateShouldReturnProductDTOWhenIdExistsAndProductNameIsBlank() {
    //given
    productDTO.setName("");

    ProductService serviceSpy = spy(service);
    doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

    Assertions.assertThrows(InvalidDataException.class,
            () -> serviceSpy.update(existingId, productDTO));
  }

  @Test
  void updateShouldReturnProductDTOWhenIdExistsAndProductPriceIsNegativeOrZero() {
    //given
    productDTO.setPrice(-5.0);

    ProductService serviceSpy = spy(service);
    doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

    Assertions.assertThrows(InvalidDataException.class,
            () -> serviceSpy.update(existingId, productDTO));
  }

  @Test
  void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExistAndValidData() {
    ProductService serviceSpy = spy(service);
    doNothing().when(serviceSpy).validateData(productDTO);

    Assertions.assertThrows(ResourceNotFoundException.class,
            () -> serviceSpy.update(nonExistingId, productDTO));
  }

  @Test
  void updateShouldReturnProductDTOWhenIdDoesNotExistAndProductNameIsBlank() {
    //given
    productDTO.setName("");

    ProductService serviceSpy = spy(service);
    doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

    Assertions.assertThrows(InvalidDataException.class,
            () -> serviceSpy.update(nonExistingId, productDTO));
  }

  @Test
  void updateShouldReturnProductDTOWhenIdDoesNotExistAndProductPriceIsNegativeOrZero() {
    //given
    productDTO.setPrice(-5.0);

    ProductService serviceSpy = spy(service);
    doThrow(InvalidDataException.class).when(serviceSpy).validateData(productDTO);

    Assertions.assertThrows(InvalidDataException.class,
            () -> serviceSpy.update(nonExistingId, productDTO));
  }

}
