package itmo.programming.api_gateway_service.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import javax.crypto.SecretKey;

public class JwtUtils {

    private static final SecretKey KEY;

    static {
        Dotenv dotenv = Dotenv.configure().directory(System.getenv("DOTENV_DIR")).load();

        String secret = dotenv.get("JWT_SECRET");
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT_SECRET not found in environment variables");
        }
        KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    public static Claims parseToken(String jwtToken) throws JwtException, IllegalArgumentException {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

}
