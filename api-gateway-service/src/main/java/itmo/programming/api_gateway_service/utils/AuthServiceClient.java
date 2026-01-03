package itmo.programming.api_gateway_service.utils;

import itmo.programming.api_gateway_service.dtos.TokenValidateRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AuthServiceClient {

    private final WebClient webClient;

    public AuthServiceClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<Boolean> isBlacklisted(String token, String tokenType) {
        return webClient.post()
                .uri("/api/v1/auth/internal/token/validate")
                .bodyValue(new TokenValidateRequestDTO(token, tokenType))
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return Mono.just(false);
                    }
                    if (clientResponse.statusCode().equals(HttpStatus.UNAUTHORIZED)) {
                        return Mono.just(true);
                    }
                    return Mono.error(new IllegalStateException("Unexpected auth-service response"));
                })
                .onErrorReturn(true);
    }

}
