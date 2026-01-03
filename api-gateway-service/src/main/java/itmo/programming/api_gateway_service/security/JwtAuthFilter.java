package itmo.programming.api_gateway_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import itmo.programming.api_gateway_service.utils.AuthServiceClient;
import java.util.List;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter implements WebFilter {

    private static final String ACCESS_TYPE = "access";
    private static final String REFRESH_TYPE = "refresh";

    private final List<String> publicPaths;
    private final AuthServiceClient authServiceClient;

    public JwtAuthFilter(String[] publicPaths, AuthServiceClient authServiceClient) {
        this.publicPaths = List.of(publicPaths);
        this.authServiceClient = authServiceClient;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        if (publicPaths.contains(path)) {
            return chain.filter(exchange);
        }

        HttpCookie accessTokenCookie = exchange.getRequest().getCookies().getFirst("accessToken");
        if (accessTokenCookie == null || accessTokenCookie.getValue().isBlank()) {
            return abortWithUnauthorized(exchange);
        }

        String token = accessTokenCookie.getValue();

        try {
            Claims claims = JwtUtils.parseToken(token);
            String tokenType = (String) claims.get("type");

            if (!tokenType.equals(ACCESS_TYPE)) {
                return abortWithUnauthorized(exchange);
            }

            String userId = claims.getSubject();

            return authServiceClient.isBlacklisted(token, tokenType)
                    .flatMap(isBlacklisted -> {
                        if (isBlacklisted) {
                            return abortWithUnauthorized(exchange);
                        }

                        ServerHttpRequest mutatedRequest = exchange.getRequest()
                                .mutate()
                                .header("X-User-Id", userId)
                                .header("X-Internal-Request", "true")
                                .build();

                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(mutatedExchange);

                    });

        } catch (JwtException | IllegalArgumentException e) {
            return abortWithUnauthorized(exchange);
        }

    }

    Mono<Void> abortWithUnauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }

}
