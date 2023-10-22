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
import static order.OrderGenerator.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class AcceptOrderTest {
    private CourierClient courierClient;
    private OrderClient orderClient;

    private int courierId;
    private int trackId;
    private int orderId;

    @Before
    public void setup() {
        Courier courier = CourierGenerator.randomCourier();
        Order order = OrderGenerator.randomOrder();

        courierClient = new CourierClient();
        courierClient.create(courier);
        courierId = courierClient.login(from(courier)).extract().path("id");

        orderClient = new OrderClient();
        trackId = orderClient.create(order).extract().path("track");
        orderId = orderClient.getTrack(String.valueOf(trackId)).extract().path("order.id");
    }

    @Test
    @DisplayName("Accept order")
    @Description("Проверка принятия заказа")
    public void getListOrdersTest() {
        ValidatableResponse response = orderClient.acceptOrder(String.valueOf(orderId), String.valueOf(courierId));

        assertThat("Неверный код статуса при принятии заказа",
                response.extract().statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Неверное сообщение при принятии заказа",
                response.extract().path("ok"), equalTo(true));
    }

    @Test
    @DisplayName("Accept order without courierId")
    @Description("Проверка принятия заказа без id курьера")
    public void getListOrdersWithoutCourierIdTest() {
        ValidatableResponse response = orderClient.acceptOrder(String.valueOf(orderId), "");

        assertThat("Неверный код статуса при принятии заказа",
                response.extract().statusCode(), equalTo(HttpStatus.SC_BAD_REQUEST));

        assertThat("Неверное сообщение при принятии заказа",
                response.extract().path("message"), equalTo("Недостаточно данных для поиска"));
    }

    @Test
    @DisplayName("Accept order with invalid courierId")
    @Description("Проверка принятия заказа c несуществующим id курьера")
    public void getListOrdersWithInvalidCourierIdTest() {
        ValidatableResponse response = orderClient.acceptOrder(String.valueOf(orderId), String.valueOf(RandomUtils.randomInt(-2000, -1000)));

        assertThat("Неверный код статуса при принятии заказа",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при принятии заказа",
                response.extract().path("message"), equalTo("Курьера с таким id не существует"));
    }

    @Test
    @DisplayName("Accept order with invalid orderId")
    @Description("Проверка принятия заказа c несуществующим id заказа")
    public void getListOrdersWithInvalidOrderIdTest() {
        ValidatableResponse response = orderClient.acceptOrder(String.valueOf(-1), String.valueOf(courierId));

        assertThat("Неверный код статуса при принятии заказа",
                response.extract().statusCode(), equalTo(HttpStatus.SC_NOT_FOUND));

        assertThat("Неверное сообщение при принятии заказа",
                response.extract().path("message"), equalTo("Заказа с таким id не существует"));
    }

    @After
    public void dispose() {
        orderClient.delete(trackId);
        courierClient.delete(courierId);
    }
}