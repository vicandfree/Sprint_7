package courier;

import data.*;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import utils.Specification;

import static io.restassured.RestAssured.*;

public class CourierClient {
    private static final String PATH = "api/v1/courier";
    private static final String LOGIN_PATH = "api/v1/courier/login";
    private static final String DELETE_PATH = "api/v1/courier/";

    @Step("Send post request to /api/v1/courier")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(courier)
                .when()
                .post(PATH)
                .then();
    }

    @Step("Send post request to /api/v1/courier/login")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(credentials)
                .when()
                .post(LOGIN_PATH)
                .then();
    }

    @Step("Send delete request to /api/v1/courier/:id")
    public ValidatableResponse delete(int id) {
        DeleteCourier deleteCourier = new DeleteCourier(String.valueOf(id));

        return given()
                .spec(Specification.requestSpecification())
                .and()
                .body(deleteCourier)
                .when()
                .delete(DELETE_PATH + id)
                .then();
    }
}