package itmo.programming.auth_service.utils;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@Component
public class CookieMaker {

    private long ACCESS_EXPIRATION_TIME;
    private long REFRESH_EXPIRATION_TIME;

    @Value("${jwt.access.expiration-time}")
    private String accessExpirationTimeStr;

    @Value("${jwt.refresh.expiration-time}")
    private String refreshExpirationTimeStr;

    @PostConstruct
    private void init() {
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

    public ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from("accessToken", token)
                .path("/")
                .maxAge((int) (ACCESS_EXPIRATION_TIME / 1000))
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from("refreshToken", token)
                .path("/")
                .maxAge((int) (REFRESH_EXPIRATION_TIME / 1000))
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie createExpiredAccessTokenCookie() {
        return ResponseCookie.from("accessToken", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    public ResponseCookie createExpiredRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }
}
