import data.Order;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.*;
import org.apache.http.HttpStatus;
import org.junit.*;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

public class ListOrderTest {
    private OrderClient orderClient;
    private int trackId;

    @Before
    public void setup() {
        Order order = OrderGenerator.randomOrder();

        orderClient = new OrderClient();
        trackId = orderClient.create(order).extract().path("track");
    }

    @Test
    @DisplayName("Get orders list")
    @Description("Проверка получения списка заказов")
    public void getListOrdersTest() {
        ValidatableResponse response = orderClient.get();

        assertThat("Неверный код статуса при получении списка заказов",
                response.extract().statusCode(), equalTo(HttpStatus.SC_OK));

        assertThat("Список заказов пустой",
                response.extract().path("orders"), notNullValue());
    }

    @After
    public void dispose() {
        orderClient.delete(trackId);
    }
}