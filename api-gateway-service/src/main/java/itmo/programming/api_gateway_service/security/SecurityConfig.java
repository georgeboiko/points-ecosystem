package itmo.programming.api_gateway_service.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${auth-service.public-paths}")
    private String[] PUBLIC_PATHS;

    @Value("${auth-service.url}")
    private String AUTH_SERVICE_URL;

    @Value("${cors.allowed-origins}")
    private List<String> ALLOWED_ORIGINS;

    @Value("${cors.allowed-methods}")
    private List<String> ALLOWED_METHODS;

    @Value("${cors.allowed-headers}")
    private List<String> ALLOWED_HEADERS;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JwtAuthFilter jwtAuthFilter
    ) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers(PUBLIC_PATHS).permitAll()
                        .anyExchange().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true);
        config.setAllowedOrigins(ALLOWED_ORIGINS);
        config.setAllowedMethods(ALLOWED_METHODS);
        config.setAllowedHeaders(ALLOWED_HEADERS);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public WebClient authWebClient() {
        return WebClient.builder()
                .baseUrl(AUTH_SERVICE_URL)
                .build();
    }
}