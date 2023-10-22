import data.Order;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import order.*;
import org.apache.http.HttpStatus;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static order.OrderGenerator.*;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    private final List<String> colors;
    private OrderClient orderClient;
    private Order order;
    private int trackId;

    public CreateOrderTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters
    public static Object[][] getColors() {
        return new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("GREY", "BLACK")},
                {List.of()},
        };
    }

    @Before
    public void setup() {
        order = OrderGenerator.randomOrder();
        order.setColor(colors);

        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Create order with color")
    @Description("Проверка возможности создания заказа c выбором цвета")
    public void createOrderTest() {
        ValidatableResponse response = orderClient.create(order);
        trackId = response.extract().path("track");

        assertThat("Неверный код статуса при создании заказа",
                response.extract().statusCode(), equalTo(HttpStatus.SC_CREATED));

        assertThat("Неверное сообщение при создании заказа",
                response.extract().path("track"), instanceOf(Integer.class));
    }

    @After
    public void dispose() {
        orderClient.delete(trackId);
    }
}