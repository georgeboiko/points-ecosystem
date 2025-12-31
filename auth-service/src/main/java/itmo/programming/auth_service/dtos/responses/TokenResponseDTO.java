package itmo.programming.auth_service.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenResponseDTO {
    private String email;
    private String accessToken;
    private String refreshToken;
}
