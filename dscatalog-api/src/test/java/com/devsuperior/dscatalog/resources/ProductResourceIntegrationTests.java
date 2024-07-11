package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.factory.Factory;
import com.devsuperior.dscatalog.factory.TokenUtil;
import com.devsuperior.dscatalog.services.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductResourceIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ProductService productService;

  Long existingId;
  Long notExistingId;
  Long dependentId;
  Long countTotalProducts;
  String userName, password, bearerToken;
  private PageImpl<ProductDTO> page;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  TokenUtil tokenUtil;

  @BeforeEach
  public void setUp() throws Exception {

    existingId = 1L;
    notExistingId = 1000L;
    dependentId = 2L;
    countTotalProducts = 26L;

    String userName = "maria@gmail.com";
    String password = "123456";
    bearerToken = tokenUtil.obtainAccessToken(mockMvc, userName, password);

    System.out.println("Config OK..");
  }

  @Test
  public void findAllPagedShouldReturnSortedPageWhenSortByName() throws Exception {
    ResultActions result = mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
            .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.totalElements").value(countTotalProducts));
    result.andExpect(jsonPath("$.content").exists());
    result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
    result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
    result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));

  }

  @Test
  public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
    ProductDTO productDTO = Factory.createProductDTO();

    Long expectedId = productDTO.getId();
    String expectedName = productDTO.getName();
    String expectedDescription = productDTO.getDescription();

    String json = objectMapper.writeValueAsString(productDTO);

    ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
            .header("Authorization", "Bearer " + bearerToken)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(json)

    );
    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").value(expectedId));
    result.andExpect(jsonPath("$.name").value(expectedName));
    result.andExpect(jsonPath("$.description").value(expectedDescription));

  }

  @Test
  public void updateShouldReturnProductDTOWhenIdNotExist() throws Exception {
    ProductDTO productDTO = Factory.createProductDTO();

    String json = objectMapper.writeValueAsString(productDTO);
    ResultActions result = mockMvc.perform(put("/products/{id}", notExistingId)
            .header("Authorization", "Bearer " + bearerToken)
            .accept(APPLICATION_JSON)
            .contentType(APPLICATION_JSON)
            .content(json));
    result.andExpect(status().isNotFound());

  }


}
