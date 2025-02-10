package com.devsuperior.dsmovie.controllers;

import com.devsuperior.dsmovie.tests.TokenUtil;
import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class MovieControllerRA {

	private String clientToken, adminToken;
	private String invalidToken;
	private Long existingMovieId;
	private Long nonExistingMovieId;
	private Map<String, Object> postMovieInstance;

	@BeforeEach
	void setUp() throws JSONException {
		baseURI = "http://localhost:8080";

		String clientUsername = "alex@gmail.com";
		String adminUsername = "maria@gmail.com";
		String clientPassword = "123456";
		String adminPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);
		invalidToken = adminToken.concat("xpto");

		existingMovieId = 1L;
		nonExistingMovieId = 200L;

		postMovieInstance = new HashMap<>();
		postMovieInstance.put("title", "The Witcher");
		postMovieInstance.put("score", 4.5F);
		postMovieInstance.put("count", 2);
		postMovieInstance.put("image", "https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg");
	}
	
	@Test
	public void findAllShouldReturnOkWhenMovieNoArgumentsGiven() {
		given()
						.header("Content-Type", "application/json")
						.when()
							.get("/movies")
						.then()
							.statusCode(200)
						.body("content[0].id", is(1))
						.body("content[0].title", equalTo("The Witcher"))
						.body("content[0].score", equalTo(4.5F))
						.body("content[0].count", equalTo(2))
						.body("content[0].image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));;
	}
	
	@Test
	public void findAllShouldReturnPagedMoviesWhenMovieTitleParamIsNotEmpty() {
		given()
						.queryParam("title", "Witcher")
						.when()
							.get("/movies")
						.then()
							.statusCode(200)
							.body("content[0].id", is(1))
							.body("content[0].title", equalTo("The Witcher"))
							.body("content[0].score", equalTo(4.5F))
							.body("content[0].count", equalTo(2))
							.body("content[0].image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnMovieWhenIdExists() {
		given()
						.get("/movies/{id}", existingMovieId)
						.then()
						.statusCode(200)
						.body("id", is(1))
						.body("title", equalTo("The Witcher"))
						.body("score", equalTo(4.5F))
						.body("count", equalTo(2))
						.body("image", equalTo("https://www.themoviedb.org/t/p/w533_and_h300_bestv2/jBJWaqoSCiARWtfV0GlqHrcdidd.jpg"));
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() {
		given()
						.get("/movies/{id}", nonExistingMovieId)
						.then()
						.statusCode(404);
	}
	
	@Test
	public void insertShouldReturnUnprocessableEntityWhenAdminLoggedAndBlankTitle() throws JSONException {
		postMovieInstance.put("title", null);
		JSONObject jsonObject = new JSONObject(postMovieInstance);

		given()
						.header("Content-Type", "application/json")
						.header("Authorization", "Bearer " + adminToken)
						.contentType(ContentType.JSON)
						.accept(ContentType.JSON)
						.body(jsonObject)
						.when()
							.post("/movies")
						.then()
							.statusCode(422)
							.body("errors[0].fieldName", equalTo("title"))
							.body("errors[0].message", equalTo("Required field"));
	}
	
	@Test
	public void insertShouldReturnForbiddenWhenClientLogged() throws Exception {
		JSONObject jsonObject = new JSONObject(postMovieInstance);

		given()
						.header("Content-Type", "application/json")
						.header("Authorization", "Bearer " + clientToken)
						.contentType(ContentType.JSON)
						.accept(ContentType.JSON)
						.body(jsonObject)
						.when()
						.post("/movies")
						.then()
						.statusCode(403);
	}
	
	@Test
	public void insertShouldReturnUnauthorizedWhenInvalidToken() throws Exception {
		JSONObject jsonObject = new JSONObject(postMovieInstance);

		given()
						.header("Content-Type", "application/json")
						.header("Authorization", "Bearer " + invalidToken)
						.contentType(ContentType.JSON)
						.accept(ContentType.JSON)
						.body(jsonObject)
						.when()
						.post("/movies")
						.then()
						.statusCode(401);
	}
}
