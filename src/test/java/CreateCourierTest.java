import courier.*;
import data.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.*;

import static courier.CourierGenerator.*;
import static data.CourierCredentials.*;
import static org.junit.Assert.*;

public class CreateCourierTest {
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setup() {
        courierClient = new CourierClient();
        courier = CourierGenerator.randomCourier();
    }

    @Test
    @DisplayName("Create courier")
    @Description("Проверка возможности создания курьера")
    public void courierCreateTest() {
        ValidatableResponse response = courierClient.create(courier);

        assertEquals("Неверный код статуса при создании курьера",
                HttpStatus.SC_CREATED, response.extract().statusCode());

        assertEquals("Неверное сообщение при успешном создании курьера",
                true, response.extract().path("ok"));
    }

    @Test
    @DisplayName("Create two same couriers")
    @Description("Проверка возможности создания двух одинаковых курьеров")
    public void twoSameCourierCreateTest() {
        ValidatableResponse firstResponse = courierClient.create(courier);

        assertEquals("Неверный код статуса при создании курьера",
                HttpStatus.SC_CREATED, firstResponse.extract().statusCode());

        ValidatableResponse secondResponse = courierClient.create(courier);

        assertEquals("Неверный код статуса при создании курьера c уже существующим логином",
                HttpStatus.SC_CONFLICT, secondResponse.extract().statusCode());

        assertEquals("Некорректное сообщение об ошибке при создании курьера с уже использованным логином ",
                "Этот логин уже используется. Попробуйте другой.", secondResponse.extract().path("message"));
    }

    @After
    public void dispose() {
        ValidatableResponse loginResponse = courierClient.login(from(courier));
        courierClient.delete(loginResponse.extract().path("id"));
    }
}