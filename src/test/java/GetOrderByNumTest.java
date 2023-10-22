import courier.*;
import data.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.*;
import org.apache.http.HttpStatus;
import org.junit.*;
import utils.RandomUtils;

import static courier.CourierGenerator.*;
import static data.CourierCredentials.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class GetOrderByNumTest {
    private CourierClient courierClient;
    private OrderClient orderClient;
    private int trackId;
    private int courierId;

    @Before
    public void setup() {
        Courier courier = CourierGenerator.randomCourier();
        Order order = OrderGenerator.randomOrder();

        courierClient = new CourierClient();
        courierClient.create(courier);

        ValidatableResponse loginResponse = courierClient.login(from(courier));
        courierId = loginResponse.extract().path("id");

        orderClient = new OrderClient();
        trackId = orderClient.create(order).extract().path("track");
    }

    @Test
    @DisplayName("Get order by id")
    @Description("Проверка получения заказа по его номеру")
    public void getOrderByNumTest() {
        ValidatableResponse getOrderResponse = orderClient.getTrack(String.valueOf(trackId));

        assertThat("Неверный код статуса при получении заказа по его номеру",
                getOrderResponse.extract().statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Неверное сообщение при получении заказа по его номеру",
                getOrderResponse.extract().path("order"), notNullValue());
    }

    @Test
    @DisplayName("Get order by invalid id")
    @Description("Проверка получения заказа по несуществующему номеру")
    public void getOrderByInvalidNumTest() {
        ValidatableResponse getOrderResponse = orderClient.getTrack(String.valueOf(RandomUtils.randomInt(500, 200000)));

        assertThat("Неверный код статуса при запросе с несуществующим номером заказа",
                getOrderResponse.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при запросе с несуществующим номером заказа",
                getOrderResponse.extract().path("message"), equalTo("Заказ не найден"));
    }

    @Test
    @DisplayName("Get order without id")
    @Description("Проверка получения заказа без номера")
    public void getOrderWithoutNumTest() {
        ValidatableResponse getOrderResponse = orderClient.getTrack("");

        assertThat("Неверный код статуса при запросе заказа без номера",
                getOrderResponse.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        assertThat("Неверное сообщение при запросе заказа без номера",
                getOrderResponse.extract().path("message"), equalTo("Недостаточно данных для поиска"));
    }

    @After
    public void dispose() {
        orderClient.delete(trackId);
        courierClient.delete(courierId);
    }
}