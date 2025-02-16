// package com.dnd.backend.security.config;
//
// import static org.hamcrest.Matchers.*;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.test.context.junit.jupiter.SpringExtension;
//
// @ExtendWith(SpringExtension.class)
// @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
// @TestPropertySource(
// 	properties = "spring.config.location=classpath:/google.yml")
// public class OAuthConfigTest {
//
// 	@BeforeEach
// 	public void setup() {
// 		RestAssured.baseURI = "https://localhost";
// 		RestAssured.port = 8443;
// 	}
//
// 	@Test
// 	public void google로그인_시도하면_OAuth인증창_등장한다() throws Exception {
// 		given()
// 			.when()
// 			.redirects().follow(false) // 리다이렉트 방지
// 			.get("/login")
// 			.then()
// 			.statusCode(302)
// 			.header("Location", containsString("https://accounts.google.com/o/oauth2/auth"));
// 	}
// }
