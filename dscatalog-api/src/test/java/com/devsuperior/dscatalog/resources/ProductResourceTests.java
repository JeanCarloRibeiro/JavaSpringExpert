package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.factory.Factory;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductResource.class)
public class ProductResourceTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ProductService productService;

  Long existingId;
  Long notExistingId;
  Long dependentId;
  ProductDTO productDTO;
  private PageImpl<ProductDTO> page;
  Product product;

  @Autowired
  private ObjectMapper objectMapper;

  @BeforeEach
  public void setUp() {

    existingId = 1L;
    notExistingId = 1000L;
    dependentId = 2L;

    productDTO = Factory.createProductDTO();
    product = Factory.createProduct();
    page = new PageImpl<>(List.of(productDTO));

    when(productService.findAllPaged(any())).thenReturn(page);
    when(productService.findById(existingId)).thenReturn(productDTO);
    when(productService.findById(notExistingId)).thenThrow(ResourceNotFoundException.class);

    when(productService.insert(any())).thenReturn(productDTO);

    when(productService.update(eq(existingId), any())).thenReturn(productDTO);
    when(productService.update(eq(notExistingId), any())).thenThrow(ResourceNotFoundException.class);

    doNothing().when(productService).delete(existingId);
    doThrow(ResourceNotFoundException.class).when(productService).delete(notExistingId);
    doThrow(DatabaseException.class).when(productService).delete(dependentId);

    System.out.println("Config OK..");
  }

  @Test
  public void findAllShouldReturnPage() throws Exception {
    ResultActions result = mockMvc.perform(get("/products")
            .accept(APPLICATION_JSON));
    result.andExpect(status().isOk());
  }

  @Test
  public void findByIdShouldReturnProductWhenIdExists() throws Exception {
    ResultActions result = mockMvc.perform(get("/products/{1}", existingId)
            .accept(APPLICATION_JSON));
    result.andExpect(status().isOk());

    result.andExpect(jsonPath("$.id").exists());
    result.andExpect(jsonPath("$.name").exists());
    result.andExpect(jsonPath("$.description").exists());

  }

  @Test
  public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

    ResultActions result = mockMvc.perform(get("/products/{1}", notExistingId)
            .accept(APPLICATION_JSON));
    result.andExpect(status().isNotFound());

  }

  @Test
  public void insertShouldReturnCreatedWithProduct() throws Exception {

    objectMapper.registerModule(new JavaTimeModule());

    String json = objectMapper.writeValueAsString(productDTO);

    ResultActions result = mockMvc.perform(post("/products")
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(json));
    result.andExpect(status().isCreated());
    result.andExpect(jsonPath("$.id").exists());

  }

  @Test
  public void updateShouldReturnProductDTOWhenIdExists() throws Exception {

    String json = objectMapper.writeValueAsString(productDTO);

    ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(json)

    );
    result.andExpect(status().isOk());

    result.andExpect(jsonPath("$.id").exists());
    result.andExpect(jsonPath("$.name").exists());
    result.andExpect(jsonPath("$.description").exists());

  }

  @Test
  public void updateShouldReturnNotFoundWhenIdNotExist() throws Exception {

    String json = objectMapper.writeValueAsString(productDTO);

    ResultActions result = mockMvc.perform(put("/products/{id}", notExistingId)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(json)

    );
    result.andExpect(status().isNotFound());

    result.andExpect(jsonPath("$.id").doesNotExist());
    result.andExpect(jsonPath("$.name").doesNotExist());
    result.andExpect(jsonPath("$.description").doesNotExist());

  }

  @Test
  public void deleteShouldReturnNotContentWhenIdExists() throws Exception {

    ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
            .accept(APPLICATION_JSON)
    );
    result.andExpect(status().isNoContent());
  }

  @Test
  public void deleteShouldReturnProductDTOWhenIdNotExist() throws Exception {
    ResultActions result = mockMvc.perform(delete("/products/{id}", notExistingId)
            .accept(APPLICATION_JSON));
    result.andExpect(status().isNotFound());
  }


}
