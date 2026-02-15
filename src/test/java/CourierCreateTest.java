import client.CourierClient;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import model.Courier;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AllureJunit5.class)
public class CourierCreateTest {
    private CourierClient courierClient;
    private Courier courier;
    private Integer courierId;

    @BeforeEach
    public void setUp() {
        courierClient = new CourierClient();
        courier = TestDataGenerator.getRandomCourier();
    }

    @AfterEach
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Создание нового курьера")
    public void testCreateCourierSuccess() {
        Response response = courierClient.createCourier(courier);
        response.then().statusCode(201).assertThat().body("ok", is(true));

        // Получаем id курьера для удаления
        Response loginResponse = courierClient.loginCourier(TestDataGenerator.getCredentialsFromCourier(courier));
        courierId = loginResponse.jsonPath().getInt("id");
        assertNotNull(courierId);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void testCreateDuplicateCourier() {
        // Создаем первого курьера
        Response firstResponse = courierClient.createCourier(courier);
        firstResponse.then().statusCode(201);

        // Получаем id первого курьера
        Response loginResponse = courierClient.loginCourier(TestDataGenerator.getCredentialsFromCourier(courier));
        courierId = loginResponse.jsonPath().getInt("id");

        // Пытаемся создать второго курьера с теми же данными
        Response secondResponse = courierClient.createCourier(courier);
        secondResponse.then().statusCode(409).assertThat().body("message",is("Этот логин уже используется. Попробуйте другой."));
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void testCreateCourierWithoutLogin() {
        courier.setLogin(null);

        Response response = courierClient.createCourier(courier);
        response.then().statusCode(400).assertThat().body("message",is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void testCreateCourierWithoutPassword() {
        courier.setPassword(null);

        Response response = courierClient.createCourier(courier);
        response.then().statusCode(400).assertThat().body("message",is("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Можно создать курьера без имени")
    public void testCreateCourierWithoutFirstName() {
        courier.setFirstName(null);

        Response response = courierClient.createCourier(courier);
        response.then().statusCode(201).assertThat().body("ok", is(true));

        // Получаем id курьера для удаления
        Response loginResponse = courierClient.loginCourier(
                TestDataGenerator.getCredentialsFromCourier(courier)
        );
        courierId = loginResponse.jsonPath().getInt("id");
        assertNotNull(courierId);
    }
}