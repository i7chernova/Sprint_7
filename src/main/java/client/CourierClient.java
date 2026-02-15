package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient extends BaseClient {
    private static final String COURIER_PATH = "/courier";
    private static final String LOGIN_PATH = "/courier/login";

    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .spec(getRequestSpec())
                .body(courier)
                .when()
                .post(COURIER_PATH);
    }

    @Step("Логин курьера")
    public Response loginCourier(CourierCredentials credentials) {
        return given()
                .spec(getRequestSpec())
                .body(credentials)
                .when()
                .post(LOGIN_PATH);
    }

    @Step("Удаление курьера")
    public Response deleteCourier(int courierId) {
        return given()
                .spec(getRequestSpec())
                .when()
                .delete(COURIER_PATH + "/" + courierId);
    }
}