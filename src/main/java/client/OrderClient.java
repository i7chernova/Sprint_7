package client;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderClient extends BaseClient {
    private static final String ORDER_PATH = "/orders";

    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return given()
                .spec(getRequestSpec())
                .body(order)
                .when()
                .post(ORDER_PATH);
    }

    @Step("Получение списка заказов")
    public Response getOrdersList() {
        return given()
                .spec(getRequestSpec())
                .when()
                .get(ORDER_PATH);
    }
}