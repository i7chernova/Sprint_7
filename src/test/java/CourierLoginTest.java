import client.CourierClient;
import io.qameta.allure.junit5.AllureJunit5;
import io.restassured.response.Response;
import model.Courier;
import model.CourierCredentials;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AllureJunit5.class)
public class CourierLoginTest {
    private static CourierClient courierClient;
    private static Courier courier;
    private Integer courierId;

    @BeforeAll
    public static void setUp() {
        courierClient = new CourierClient();
        courier = TestDataGenerator.getRandomCourier();

        // Создаем курьера перед тестами логина
        Response createResponse = courierClient.createCourier(courier);
        createResponse.then().statusCode(201);
    }

    @AfterEach
    public void tearDown() {
        if (courierId != null) {
            courierClient.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешная авторизация с валидными данными")
    public void testLoginCourierSuccess() {
        CourierCredentials credentials = TestDataGenerator.getCredentialsFromCourier(courier);

        Response response = courierClient.loginCourier(credentials);
        assertEquals(200, response.statusCode());

        courierId = response.jsonPath().getInt("id");
        assertNotNull(courierId);
        assertTrue(courierId > 0);
    }

    @Test
    @DisplayName("Нельзя авторизоваться с неправильным логином")
    public void testLoginWithWrongLogin() {
        CourierCredentials credentials = new CourierCredentials();
        credentials.setLogin("wrong_login" + Math.random()*100);
        credentials.setPassword(courier.getPassword());

        Response response = courierClient.loginCourier(credentials);
        response.then().statusCode(404).assertThat().body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с неправильным паролем")
    public void testLoginWithWrongPassword() {
        CourierCredentials credentials = new CourierCredentials();
        credentials.setLogin(courier.getLogin());
        credentials.setPassword("wrong_password");

        Response response = courierClient.loginCourier(credentials);
        response.then().statusCode(404).assertThat().body("message", is("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться без логина")
    public void testLoginWithoutLogin() {
        CourierCredentials credentials = new CourierCredentials();
        credentials.setPassword(courier.getPassword());

        Response response = courierClient.loginCourier(credentials);
        response.then().statusCode(400).assertThat().body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться без пароля")
    public void testLoginWithoutPassword() {
        CourierCredentials credentials = new CourierCredentials();
        credentials.setLogin(courier.getLogin());

        Response response = courierClient.loginCourier(credentials);
        response.then().statusCode(400).assertThat().body("message", is("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Нельзя авторизоваться с несуществующим пользователем")
    public void testLoginWithNonExistentUser() {
        Courier nonExistentCourier = TestDataGenerator.getRandomCourier();
        CourierCredentials credentials = TestDataGenerator.getCredentialsFromCourier(nonExistentCourier);

        Response response = courierClient.loginCourier(credentials);
        response.then().statusCode(404).assertThat().body("message", is("Учетная запись не найдена"));
    }
}