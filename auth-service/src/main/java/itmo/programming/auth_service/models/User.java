package itmo.programming.auth_service.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    private Long id;
    private String email;
    private String passwordHash;

    public User(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }
}
