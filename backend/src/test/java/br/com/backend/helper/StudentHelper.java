package br.com.backend.helper;

import br.com.backend.dto.request.StudentCreateRequest;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestComponent;

import java.util.UUID;

import static io.restassured.RestAssured.given;

@TestComponent
public class StudentHelper {

    @Autowired
    private AuthHelper helper;

    private String getAdminToken() {
        return helper.getAdminAccessToken();
    }

    public ExtractableResponse<Response> createStudentAndReturn(
            String name, String email, String password) {

        StudentCreateRequest request =
                new StudentCreateRequest(name, email, password);

        return given()
                .header("Authorization", "Bearer " + getAdminToken())
                .contentType(ContentType.JSON)
                .body(request)
        .when()
                .post("/students")
        .then()
                .statusCode(201)
                .extract();
    }

    public UUID getStudentId(ExtractableResponse<Response> response) {
        String id = response.path("id");

        return UUID.fromString(id);
    }

    public String getStudentToken(String email, String password) {
        return helper.getAccessToken(email, password);
    }
}
