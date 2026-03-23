package br.com.backend.api;

import br.com.backend.builders.AuthRequestBuilder;
import br.com.backend.config.DataInitializer;
import br.com.backend.dto.request.AuthRequest;
import br.com.backend.dto.request.UserCreateRequest;
import br.com.backend.entity.enums.Role;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@Import(DataInitializer.class)
public class AuthApiTest {

    @LocalServerPort
    private int port;

    private AuthRequest request;

    @BeforeEach
    void setup() {
        RestAssured.reset();
        RestAssured.baseURI = "http://127.0.0.1";
        RestAssured.port = port;

        RestAssured.config = RestAssured.config()
                .encoderConfig(io.restassured.config.EncoderConfig.encoderConfig()
                        .appendDefaultContentCharsetToContentTypeIfUndefined(false));

        request = AuthRequestBuilder.builder()
                .withEmail("admin@admin.com")
                .withPassword("admin")
                .build();
    }

    private String getAdminToken() {
        return given()
                    .contentType(ContentType.JSON)
                    .body(request)
                .when()
                    .post("/auth/login")
                .then()
                    .statusCode(200)
                    .extract()
                    .path("accessToken");
    }

    private String getStudentToken() {
        AuthRequest request =
                new AuthRequest("student@school.com", "student");

        return given()
                        .contentType(ContentType.JSON)
                        .body(request)
                    .when()
                        .post("/auth/login")
                    .then()
                        .extract()
                        .path("accessToken");
    }

    @Test
    void shouldLoginSuccessfully() {
        given()
            .contentType(ContentType.JSON)
            .body(request)
        .when()
            .post("/auth/login")
        .then()
            .statusCode(200)
            .body("accessToken", notNullValue())
            .body("refreshToken", notNullValue());
    }

    @Test
    void shouldAccessProtectedEndpoint() {
        String token = getAdminToken();

        given()
                .header("Authorization", "Bearer " + token)
        .when()
                .get("/students")
        .then()
                .statusCode(200);
    }

    @Test
    void shouldNotAccessStudentWithoutToken() {
        given()
                .log().all()
        .when()
                .get("/students")
        .then()
                .log().all()
                .statusCode(403);
    }

    @Test
    void shouldNotAccessWithInvalidToken() {
        given()
                .header("Authorization", "Bearer token-invalido")
        .when()
                .get("/students")
        .then()
                .statusCode(401);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        String token = getAdminToken();

        UserCreateRequest request =
                new UserCreateRequest("teste@teste.com", "123456", Role.STUDENT);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .header("Authorization", "Bearer " + token)
        .when()
                .post("/admin/users")
        .then()
                .statusCode(201);
    }

    @Test
    void shouldReturnForbiddenWhenUserHasNoPermission() {
        String token = getStudentToken();

        UserCreateRequest request =
                new UserCreateRequest("marquinhos@school.com", "marquinhos", Role.STUDENT);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .header("Authorization", "Bearer " + token)
        .when()
                .post("/admin/users")
        .then()
                .log().all()
                .statusCode(403);
    }
}
