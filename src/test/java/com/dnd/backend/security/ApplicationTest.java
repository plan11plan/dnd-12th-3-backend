package com.dnd.backend.security;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.restassured.RestAssured;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ApplicationTest {

	@BeforeEach
	public void setup() {
		RestAssured.port = 8443;
	}

	@Test
	public void 기본path로_접근하면_index_html_호출된다() throws Exception {
		given()
			.when()
			.get("/")
			.then()
			.statusCode(200)
			.contentType("text/html")
			.body(containsString("권한 관리"));
	}
}
