package order;

import data.Order;
import utils.RandomUtils;

import java.util.List;

public class OrderGenerator {

    public static Order randomOrder() {
        return Order.builder()
                .firstName(RandomUtils.randomString(10))
                .lastName(RandomUtils.randomString(10))
                .address(RandomUtils.randomString(10))
                .metroStation(RandomUtils.randomString(10))
                .phone(RandomUtils.randomPhoneNumber())
                .rentTime(RandomUtils.randomInt(10, 100))
                .deliveryDate(RandomUtils.randomDate(2023, 2024))
                .comment(RandomUtils.randomString(10))
                .color(List.of(RandomUtils.randomString(10)))
                .build();
    }
}