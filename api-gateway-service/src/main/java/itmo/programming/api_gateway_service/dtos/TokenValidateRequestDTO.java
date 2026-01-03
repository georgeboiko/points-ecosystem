package itmo.programming.api_gateway_service.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class TokenValidateRequestDTO {
    private String token;
    private String tokenType;
}
