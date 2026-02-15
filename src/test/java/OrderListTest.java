import client.OrderClient;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(AllureJunit5.class)
public class OrderListTest {

    @Test
    @DisplayName("Получение списка заказов")
    public void testGetOrdersList() {
        OrderClient orderClient = new OrderClient();

        Response response = orderClient.getOrdersList();
        response.then().statusCode(200).log().all().assertThat().body("orders", notNullValue());

    }
}