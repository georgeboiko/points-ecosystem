package itmo.programming.api_gateway_service.utils;

import itmo.programming.api_gateway_service.dtos.TokenValidateRequestDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthServiceClient {

    @Value("${auth-service.check-endpoint}")
    private String CHECK_ENDPOINT;

    private final WebClient webClient;

    public AuthServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Boolean> isBlacklisted(String token, String tokenType) {
        return webClient.post()
                .uri(CHECK_ENDPOINT)
                .bodyValue(new TokenValidateRequestDTO(token, tokenType))
                .retrieve()
                .bodyToMono(Boolean.class)
                .onErrorReturn(true);
    }

}
