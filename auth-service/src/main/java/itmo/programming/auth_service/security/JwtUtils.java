package itmo.programming.auth_service.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import itmo.programming.auth_service.models.User;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;

public class JwtUtils {

    private static final String ACCESS_TYPE = "access";
    private static final String REFRESH_TYPE = "refresh";

    private static final SecretKey KEY;
    private static final long ACCESS_EXPIRATION_TIME;
    private static final long REFRESH_EXPIRATION_TIME;

    static {
        Dotenv dotenv = Dotenv.configure().directory(System.getenv("DOTENV_DIR")).load();

        String secret = dotenv.get("JWT_SECRET");
        if (secret == null || secret.trim().isEmpty()) {
            throw new IllegalArgumentException("JWT_SECRET not found in environment variables");
        }
        KEY = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        String accessTime = dotenv.get("ACCESS_EXPIRATION_TIME");
        try {
            ACCESS_EXPIRATION_TIME = Long.parseLong(accessTime);
            if (ACCESS_EXPIRATION_TIME <= 0) {
                throw new IllegalArgumentException("ACCESS_EXPIRATION_TIME is invalid");
            }
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("ACCESS_EXPIRATION_TIME not found in environment variables");
        }

        String refreshTime = dotenv.get("REFRESH_EXPIRATION_TIME");
        try {
            REFRESH_EXPIRATION_TIME = Long.parseLong(refreshTime);
            if (REFRESH_EXPIRATION_TIME <= 0) {
                throw new IllegalArgumentException("REFRESH_EXPIRATION_TIME is invalid");
            }
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("REFRESH_EXPIRATION_TIME not found in environment variables");
        }
    }


    public static Claims parseToken(String jwtToken) throws JwtException, IllegalArgumentException {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public static String generateAccessToken(User user) {
        return generateToken(ACCESS_TYPE, ACCESS_EXPIRATION_TIME, user);
    }

    public static String generateRefreshToken(User user) {
        return generateToken(REFRESH_TYPE, REFRESH_EXPIRATION_TIME, user);
    }

    public static boolean isRefreshToken(String token) throws JwtException, IllegalArgumentException {
        Claims claims = parseToken(token);
        return REFRESH_TYPE.equals(claims.get("type"));
    }

    public static String getSubject(String token) throws JwtException, IllegalArgumentException {
        return parseToken(token).getSubject();
    }

    public static Date getExpirationTime(String token) {
        return parseToken(token).getExpiration();
    }

    public static long getAccessExpirationTime() {
        return ACCESS_EXPIRATION_TIME;
    }

    public static long getRefreshExpirationTime() {
        return REFRESH_EXPIRATION_TIME;
    }

    private static String generateToken(String type, long expiration, User user) {
        return Jwts.builder()
                .subject(user.getEmail())
                .claim("type", type)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(KEY)
                .compact();
    }

}
