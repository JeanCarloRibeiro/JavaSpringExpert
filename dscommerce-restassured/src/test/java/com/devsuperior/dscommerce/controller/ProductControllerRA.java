package com.devsuperior.dscommerce.controller;

import com.devsuperior.dscommerce.util.TokenUtil;
import io.restassured.http.ContentType;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

public class ProductControllerRA {

  private Long existingProductId;
  private Long nonExistingProductId;

  private String clientUsername, clientPassword, adminUsername, adminPassword;
  private String clientToken, adminToken;
  private String invalidToken;
  private Map<String, Object> postProductInstance;

  @BeforeEach
  void setUp() {
    baseURI = "http://localhost:8080";

    clientUsername = "maria@gmail.com";
    adminUsername = "alex@gmail.com";
    clientPassword = "123456";
    adminPassword = "123456";

    clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
    adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
    invalidToken = adminToken.concat("xpto");

    existingProductId = 4L;
    nonExistingProductId = 100L;

    postProductInstance = new HashMap<>();
    postProductInstance.put("name", "Meu produto");
    postProductInstance.put("description", "Meu produto descricao");
    postProductInstance.put("imgUrl", "https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg");
    postProductInstance.put("price", 50.0F);

    List<Map<String, Object>> categories = new ArrayList<>();

    Map<String, Object> category1 = new HashMap<>();
    category1.put("id", 2);

    Map<String, Object> category2 = new HashMap<>();
    category2.put("id", 3);

    categories.add(category1);
    categories.add(category2);

    postProductInstance.put("categories", categories);
  }

  @Test
  void findByIdShouldReturnProductWhenIdExists() {
    given().get("/products/{id}", existingProductId)
            .then().statusCode(200)
            .body("id", is(4))
            .body("name", equalTo("Smart TV"))
            .body("price", is(2190.0F))
            .body("categories.id", hasItems(3))
            .body("categories.name", hasItems("Computadores"));
  }

  @Test
  void insertProductShouldReturnProductWhenAdminLogged() {
    JSONObject jsonObject = new JSONObject(postProductInstance);
    given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + adminToken)
            .body(jsonObject)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
    .when()
            .post("/products")
            .then()
              .statusCode(201)
              .body("name", equalTo("Meu produto"))
              .body("price", is(50.0F))
              .body("imgUrl", equalTo("https://raw.githubusercontent.com/devsuperior/dscatalog-resources/master/backend/img/3-big.jpg"))
              .body("categories.id", hasItems(2,3));
  }

  @Test
  void insertProductShouldReturn422WhenAdminLoggedAndFieldNameIsInvalid() {
    postProductInstance.put("name", "ab");

    JSONObject jsonObject = new JSONObject(postProductInstance);

    given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + adminToken)
            .body(jsonObject)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .when()
            .post("/products")
            .then()
            .statusCode(422)
            .body("errors.message[0]", equalTo("Nome precisa ter de 3 a 80 caracteres"));
  }

  @Test
  void insertProductShouldReturn422WhenAdminLoggedAndFieldDescriptionIsInvalid() {
    postProductInstance.put("description", "ab");

    JSONObject jsonObject = new JSONObject(postProductInstance);

    given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + adminToken)
            .body(jsonObject)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .when()
            .post("/products")
            .then()
            .statusCode(422)
            .body("errors.message[0]", equalTo("Descrição precisa ter no mínimo 10 caracteres"));
  }

  @Test
  void insertProductShouldReturn422WhenAdminLoggedAndFieldPriceIsNegative() {
    postProductInstance.put("price", -500);

    JSONObject jsonObject = new JSONObject(postProductInstance);

    given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + adminToken)
            .body(jsonObject)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .when()
            .post("/products")
            .then()
            .statusCode(422)
            .body("errors.message[0]", equalTo("O preço deve ser positivo"));
  }

  @Test
  void insertProductShouldReturn422WhenAdminLoggedAndFieldPriceIsZero() {
    postProductInstance.put("price", -500);

    JSONObject jsonObject = new JSONObject(postProductInstance);

    given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + adminToken)
            .body(jsonObject)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .when()
            .post("/products")
            .then()
            .statusCode(422)
            .body("errors.message[0]", equalTo("O preço deve ser positivo"));
  }

  @Test
  void insertProductShouldReturn422WhenAdminLoggedAndFieldCategoryIsInvalid() {
    postProductInstance.put("categories", null);

    JSONObject jsonObject = new JSONObject(postProductInstance);

    given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + adminToken)
            .body(jsonObject)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .when()
            .post("/products")
            .then()
            .statusCode(422)
            .body("errors.message[0]", equalTo("Deve ter pelo menos uma categoria"));
  }

  @Test
  void insertProductShouldReturn403WhenClientLogged() {
    JSONObject jsonObject = new JSONObject(postProductInstance);
    given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + clientToken)
            .body(jsonObject)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .when()
            .post("/products")
            .then()
            .statusCode(403);
  }

  @Test
  void insertProductShouldReturn401WhenInvalidToken() {
    JSONObject jsonObject = new JSONObject(postProductInstance);
    given()
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer " + invalidToken)
            .body(jsonObject)
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .when()
              .post("/products")
            .then()
              .statusCode(401);
  }

}
