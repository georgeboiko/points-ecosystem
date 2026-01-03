package itmo.programming.api_gateway_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secret;

    private SecretKey KEY;

    @PostConstruct
    private void init() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT_SECRET not found in environment variables");
        }

        KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public Claims parseToken(String jwtToken) throws JwtException, IllegalArgumentException {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

}
