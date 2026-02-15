import model.Courier;
import model.CourierCredentials;
import model.Order;

import java.util.List;
import java.util.Random;

public class TestDataGenerator {
    private static final Random random = new Random();

    public static Courier getRandomCourier() {
        String login = "courier_n" + random.nextInt(100);
        String password = "password_" + random.nextInt(100);
        String firstName = "Name_n" + random.nextInt(100);

        return Courier.builder()
                .login(login)
                .password(password)
                .firstName(firstName)
                .build();
    }

    public static CourierCredentials getCredentialsFromCourier(Courier courier) {
        return CourierCredentials.builder()
                .login(courier.getLogin())
                .password(courier.getPassword())
                .build();
    }

    public static Order getRandomOrder(List<String> colors) {
        return Order.builder()
                .firstName("Иван")
                .lastName("Иванов")
                .address("Москва, ул. Петровка, 38")
                .metroStation("Чеховская")
                .phone("+79161234567")
                .rentTime(3)
                .deliveryDate("2026-02-20")
                .comment("Комментарий к тестовому заказу")
                .color(colors)
                .build();
    }
}