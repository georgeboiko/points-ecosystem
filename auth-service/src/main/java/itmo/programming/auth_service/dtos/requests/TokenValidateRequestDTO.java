package itmo.programming.auth_service.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class TokenValidateRequestDTO {
    private String token;
    private String tokenType;
}
