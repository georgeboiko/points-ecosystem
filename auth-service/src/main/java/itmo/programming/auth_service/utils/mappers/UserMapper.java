package itmo.programming.auth_service.utils.mappers;

import itmo.programming.auth_service.entities.UserEntity;
import itmo.programming.auth_service.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.getEmail(),
                user.getPasswordHash()
        );
    }

    public User toModel(UserEntity entity) {
        return new User(
                entity.getEmail(),
                entity.getPasswordHash()
        );
    }
}