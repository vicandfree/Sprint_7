import courier.*;
import data.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.*;
import utils.RandomUtils;

import static courier.CourierGenerator.*;
import static data.CourierCredentials.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class LoginCourierTest {
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setup() {
        courier = CourierGenerator.randomCourier();

        courierClient = new CourierClient();
        courierClient.create(courier);
    }

    @Test
    @DisplayName("Login courier")
    @Description("Проверка авторизации курьером")
    public void loginCourierTest() {
        ValidatableResponse response = courierClient.login(from(courier));

        assertThat("Неверный код статуса при авторизации курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.extract().path("id"), instanceOf(Integer.class));
    }

    @Test
    @DisplayName("Login courier with wrong Login")
    @Description("Проверка авторизации курьером с неправильным логином")
    public void loginCourierWithWrongLoginTest() {
        Courier courierWithWrongLogin = new Courier(RandomUtils.randomString(5), courier.getPassword(), courier.getFirstName());
        ValidatableResponse response = courierClient.login(from(courierWithWrongLogin));

        assertThat("Неверный код статуса при авторизации курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.extract().path("message"), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Login courier with wrong password")
    @Description("Проверка авторизации курьером с неправильным паролем")
    public void loginCourierWithWrongPasswordTest() {
        Courier courierWithWrongPassword = new Courier(courier.getPassword(), RandomUtils.randomString(8), courier.getFirstName());
        ValidatableResponse response = courierClient.login(from(courierWithWrongPassword));

        assertThat("Неверный код статуса при авторизации курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.extract().path("message"), equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Login courier without password")
    @Description("Проверка авторизации курьером без паролем")
    public void loginCourierWithoutPasswordTest() {
        Courier courierWithoutPassword = new Courier(courier.getLogin(), "", courier.getFirstName());
        ValidatableResponse response = courierClient.login(from(courierWithoutPassword));

        assertThat("Неверный код статуса при авторизации курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.extract().path("message"), equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Login courier without login")
    @Description("Проверка авторизации курьером без логина")
    public void loginCourierWithoutLoginTest() {
        Courier courierWithoutLogin = new Courier("", courier.getPassword(), courier.getFirstName());
        ValidatableResponse response = courierClient.login(from(courierWithoutLogin));

        assertThat("Неверный код статуса при авторизации курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.extract().path("message"), equalTo("Недостаточно данных для входа"));
    }

    @After
    public void dispose() {
        ValidatableResponse loginResponse = courierClient.login(from(courier));
        courierClient.delete(loginResponse.extract().path("id"));
    }
}