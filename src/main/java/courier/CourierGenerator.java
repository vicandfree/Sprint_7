package courier;

import data.Courier;
import utils.RandomUtils;

public class CourierGenerator {

    public static Courier randomCourier() {
        return Courier.builder()
                .login(RandomUtils.randomString(5))
                .password(RandomUtils.randomString(8))
                .firstName(RandomUtils.randomString(10))
                .build();
    }
}