import courier.CourierClient;
import data.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import utils.RandomUtils;

import static data.CourierCredentials.*;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class CreateCourierParameterizedTest {
    private final String login;
    private final String password;
    private final String firstName;

    private CourierClient courierClient;
    private Courier courier;

    public CreateCourierParameterizedTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Object[][] getCourierCredentials() {
        return new Object[][]{
                {"", RandomUtils.randomString(8), RandomUtils.randomString(10)},
                {RandomUtils.randomString(5), "", RandomUtils.randomString(10)},
        };
    }

    @Before
    public void setup() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Create courier with wrong data")
    @Description("Создание курьера без необходимых данных в запросе")
    public void createCourierNoDataTest() {
        ValidatableResponse response = courierClient.create(courier = new Courier(login, password, firstName));

        assertEquals("Неверный код статуса при создании курьера без необходимых данных",
                HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());

        assertEquals("Некорректное сообщение об ошибке при создании необходимых данных",
                "Недостаточно данных для создания учетной записи", response.extract().path("message"));
    }

    @After
    public void dispose() {
        ValidatableResponse loginResponse = courierClient.login(from(courier));

        if (loginResponse.extract().statusCode() == HttpStatus.SC_OK) {
            courierClient.delete(loginResponse.extract().path("id"));
        }
    }
}