import client.OrderClient;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import model.Order;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.notNullValue;

@ExtendWith(AllureJunit5.class)
public class OrderCreateTest {

    private static Stream<Arguments> provideColorsForOrder() {
        return Stream.of(
                Arguments.of(List.of("BLACK")),
                Arguments.of(List.of("GREY")),
                Arguments.of(List.of("BLACK", "GREY")),
                Arguments.of(List.of()),
                Arguments.of((Object) null)
        );
    }

    @ParameterizedTest
    @MethodSource("provideColorsForOrder")
    @DisplayName("Создание заказа с различными цветами")
    public void testCreateOrderWithDifferentColors(List<String> colors) {
        OrderClient orderClient = new OrderClient();
        Order order = TestDataGenerator.getRandomOrder(colors);

        Response response = orderClient.createOrder(order);
        response.then().statusCode(201).log().all().assertThat().body("track", notNullValue());

    }
}