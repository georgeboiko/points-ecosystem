package itmo.programming.auth_service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import itmo.programming.auth_service.models.User;
import jakarta.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    private static final String ACCESS_TYPE = "access";
    private static final String REFRESH_TYPE = "refresh";

    private SecretKey KEY;
    private long ACCESS_EXPIRATION_TIME;
    private long REFRESH_EXPIRATION_TIME;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access.expiration-time}")
    private String accessExpirationTimeStr;

    @Value("${jwt.refresh.expiration-time}")
    private String refreshExpirationTimeStr;

    @PostConstruct
    private void init() {
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT_SECRET not found in environment variables");
        }

        KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        try {
            ACCESS_EXPIRATION_TIME = Long.parseLong(accessExpirationTimeStr);
            if (ACCESS_EXPIRATION_TIME <= 0) {
                throw new IllegalArgumentException("ACCESS_EXPIRATION_TIME is invalid");
            }
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("ACCESS_EXPIRATION_TIME not found in environment variables");
        }
        try {
            REFRESH_EXPIRATION_TIME = Long.parseLong(refreshExpirationTimeStr);
            if (REFRESH_EXPIRATION_TIME <= 0) {
                throw new IllegalArgumentException("REFRESH_EXPIRATION_TIME is invalid");
            }
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("REFRESH_EXPIRATION_TIME not found in environment variables");
        }
    }

    public Claims parseToken(String jwtToken) throws JwtException, IllegalArgumentException {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public String generateAccessToken(User user) {
        return generateToken(ACCESS_TYPE, ACCESS_EXPIRATION_TIME, user);
    }

    public String generateRefreshToken(User user) {
        return generateToken(REFRESH_TYPE, REFRESH_EXPIRATION_TIME, user);
    }

    public boolean isRefreshToken(String token) throws JwtException, IllegalArgumentException {
        Claims claims = parseToken(token);
        return REFRESH_TYPE.equals(claims.get("type"));
    }

    public String getSubject(String token) throws JwtException, IllegalArgumentException {
        return parseToken(token).getSubject();
    }

    public Date getExpirationTime(String token) {
        return parseToken(token).getExpiration();
    }

    public long getAccessExpirationTime() {
        return ACCESS_EXPIRATION_TIME;
    }

    public long getRefreshExpirationTime() {
        return REFRESH_EXPIRATION_TIME;
    }

    private String generateToken(String type, long expiration, User user) {
        return Jwts.builder()
                .subject(user.getId().toString())
                .claim("email", user.getEmail())
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(KEY)
                .compact();
    }

}
