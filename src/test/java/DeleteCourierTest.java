import courier.*;
import data.Courier;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.*;
import utils.RandomUtils;

import static data.CourierCredentials.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class DeleteCourierTest {
    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setup() {
        Courier courier = CourierGenerator.randomCourier();

        courierClient = new CourierClient();
        courierClient.create(courier);

        ValidatableResponse loginResponse = courierClient.login(from(courier));
        courierId = loginResponse.extract().path("id");
    }

    @Test
    @DisplayName("Delete courier with wrong id")
    @Description("Проверка удаления курьера с несуществующим id")
    public void deleteCourierWithWrongIdTest(){
        ValidatableResponse response = courierClient.delete(RandomUtils.randomInt(-200000, -100000));

        assertThat("Неверный код статуса при удалении курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.extract().path("message"),equalTo("Курьера с таким id нет."));
    }

    @Test
    @DisplayName("Delete courier")
    @Description("Проверка удаления курьера")
    public void deleteCourierTest(){
        ValidatableResponse response = courierClient.delete(courierId);

        assertThat("Неверный код статуса при удалении курьера",
                response.extract().statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Неверное сообщение при успешной авторизации курьера",
                response.extract().path("ok"),equalTo(true));
    }

    @After
    public void dispose() {
        courierClient.delete(courierId);
    }
}