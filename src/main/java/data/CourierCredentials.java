package data;

import lombok.Data;

@Data
public class CourierCredentials {
    private final String login;
    private final String password;

    public static CourierCredentials from(Courier courier) {
        return new CourierCredentials(courier.getLogin(), courier.getPassword());
    }
}