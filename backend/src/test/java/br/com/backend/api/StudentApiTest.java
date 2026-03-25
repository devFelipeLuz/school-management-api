package br.com.backend.api;

import br.com.backend.builders.dto.StudentUpdateRequestBuilder;
import br.com.backend.config.BaseApiTest;
import br.com.backend.dto.request.StudentCreateRequest;
import br.com.backend.dto.request.StudentUpdateRequest;
import br.com.backend.helper.AuthHelper;
import br.com.backend.helper.StudentHelper;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class StudentApiTest extends BaseApiTest {

    @Autowired
    private AuthHelper helper;

    @Autowired
    private StudentHelper studentHelper;

    private String adminToken;

    @BeforeEach
    void setupTest() {
        adminToken = helper.getAdminAccessToken();
    }

    @Test
    void shouldAllowAdminToCreateStudent() {
        StudentCreateRequest request =
                new StudentCreateRequest(
                        "Ricardo Juarez",
                        "ricardo.juarez@school.com",
                        "student");

        given()
                .header("Authorization", "Bearer " + adminToken)
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/students")
        .then()
                .statusCode(201)
                .body("id", notNullValue());
    }

    @Test
    void shouldListStudents() {
        given()
                .header("Authorization", "Bearer " + adminToken)
        .when()
                .get("/students")
        .then()
                .statusCode(200);
    }

    @Test
    void shouldAllowAdminGetStudentById() {
        ExtractableResponse<Response> student =
                studentHelper.createStudentAndReturn(
                        "Ricardo Juarez",
                        "ricardo.juarez@school.com",
                        "student");

        UUID studentId = studentHelper.getStudentId(student);

        given()
                .header("Authorization", "Bearer " + adminToken)
        .when()
                .get("/students/{id}", studentId)
        .then()
                .statusCode(200)
                .body("name", equalTo("Ricardo Juarez"))
                .body("email", equalTo("ricardo.juarez@school.com"));
    }

    @Test
    void shouldAllowStudentToGetOwnData() {
        ExtractableResponse<Response> student =
                studentHelper.createStudentAndReturn(
                        "Ricardo Juarez",
                        "ricardo.juarez@school.com",
                        "student");

        UUID studentId = studentHelper.getStudentId(student);

        String studentToken = studentHelper.getStudentToken(
                "ricardo.juarez@school.com", "student");

        given()
                .header("Authorization", "Bearer " + studentToken)
        .when()
                .get("/students/{id}", studentId)
        .then()
                .statusCode(200)
                .body("name", equalTo("Ricardo Juarez"))
                .body("email", equalTo("ricardo.juarez@school.com"));
    }

    @Test
    void shouldReturnNotFoundWhenStudentAccessesAnotherStudent() {
        ExtractableResponse<Response> student =
                studentHelper.createStudentAndReturn(
                        "Ricardo Juarez",
                        "ricardo.juarez@school.com",
                        "student");

        String studentToken = studentHelper.getStudentToken(
                "ricardo.juarez@school.com", "student");

        ExtractableResponse<Response> newStudent =
                studentHelper.createStudentAndReturn(
                        "Guilherme Briggs",
                        "guilherme.briggs@school.com",
                        "student");

        UUID newStudentId = studentHelper.getStudentId(newStudent);

        given()
                .header("Authorization", "Bearer " + studentToken)
        .when()
                .get("/students/{id}", newStudentId)
        .then()
                .statusCode(403);
    }

    @Test
    void shouldNotAllowAccessWithoutToken() {
        given()
        .when()
                .get("/students")
        .then()
                .statusCode(403);
    }

    @Test
    void shouldReturnUnauthorizedForInvalidToken() {
        given()
                .header("Authorization", "Bearer invalid token")
        .when()
                .get("/students")
        .then()
                .statusCode(401);
    }

    @Test
    void shouldUpdateStudent() {
        ExtractableResponse<Response> student = studentHelper.createStudentAndReturn(
                "Ricardo Juarez",
                "ricardo.juarez@school.com",
                "student"
        );

        String studentToken = studentHelper.getStudentToken(
                "ricardo.juarez@school.com",
                "student");

        UUID studentId = studentHelper.getStudentId(student);

        StudentUpdateRequest request = StudentUpdateRequestBuilder.builder()
                .withName("Ricardo Juarez")
                .withEmail("ricardo.juarez.student@school.com")
                .withPassword("student")
                .build();


        given()
                .header("Authorization", "Bearer " + studentToken)
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .patch("/students/{id}", studentId)
        .then()
                .statusCode(200)
                .body("name", equalTo("Ricardo Juarez"))
                .body("email", equalTo("ricardo.juarez.student@school.com"));
    }

    @Test
    void shouldDeactivateStudent() {
        ExtractableResponse<Response> student = studentHelper.createStudentAndReturn(
                "Ricardo Juarez",
                "ricardo.juarez@school.com",
                "student"
        );

        UUID studentId = studentHelper.getStudentId(student);

        given()
                .header("Authorization", "Bearer " + helper.getAdminAccessToken())
        .when()
                .delete("/students/{id}/deactivate", studentId)
        .then()
                .statusCode(204);
    }
}
