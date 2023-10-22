package data;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class Courier {
    private String login;
    private String password;
    private String firstName;
}