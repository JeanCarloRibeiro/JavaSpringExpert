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

public class ScoreControllerRA {

	private String clientToken, adminToken;
	private String invalidToken;
	private Long existingMovieId;
	private Long nonExistingMovieId;
	private Map<String, Object> postScoreInstance;

	@BeforeEach
	void setUp() throws JSONException {
		baseURI = "http://localhost:8080";

		String clientUsername = "alex@gmail.com";
		String adminUsername = "maria@gmail.com";
		String clientPassword = "123456";
		String adminPassword = "123456";

		clientToken = TokenUtil.obtainAccessToken(clientUsername, clientPassword);
		adminToken = TokenUtil.obtainAccessToken(adminUsername, adminPassword);

		existingMovieId = 1L;
		nonExistingMovieId = 200L;

		postScoreInstance = new HashMap<>();
		postScoreInstance.put("movieId", 1L);
		postScoreInstance.put("score", 4.5F);
	}
	
	@Test
	public void saveScoreShouldReturnNotFoundWhenMovieIdDoesNotExist() throws Exception {
		postScoreInstance.put("movieId", nonExistingMovieId);
		JSONObject jsonObject = new JSONObject(postScoreInstance);

		given()
						.header("Content-Type", "application/json")
						.header("Authorization", "Bearer " + adminToken)
						.contentType(ContentType.JSON)
						.accept(ContentType.JSON)
						.body(jsonObject)
						.when()
							.put("/scores")
						.then()
							.statusCode(404);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenMissingMovieId() throws Exception {
		postScoreInstance.put("movieId", null);
		JSONObject jsonObject = new JSONObject(postScoreInstance);

		given()
						.header("Content-Type", "application/json")
						.header("Authorization", "Bearer " + adminToken)
						.contentType(ContentType.JSON)
						.accept(ContentType.JSON)
						.body(jsonObject)
						.when()
						.put("/scores")
						.then()
						.statusCode(422);
	}
	
	@Test
	public void saveScoreShouldReturnUnprocessableEntityWhenScoreIsLessThanZero() throws Exception {
		postScoreInstance.put("score", -1);
		JSONObject jsonObject = new JSONObject(postScoreInstance);

		given()
						.header("Content-Type", "application/json")
						.header("Authorization", "Bearer " + adminToken)
						.contentType(ContentType.JSON)
						.accept(ContentType.JSON)
						.body(jsonObject)
						.when()
						.put("/scores")
						.then()
						.statusCode(422);
	}
}
