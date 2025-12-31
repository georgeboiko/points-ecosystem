package itmo.programming.auth_service.utils;

import itmo.programming.auth_service.security.JwtUtils;
import org.springframework.http.ResponseCookie;

public class CookieMaker {
    public static ResponseCookie createAccessTokenCookie(String token) {
        return ResponseCookie.from("accessToken", token)
                .path("/")
                .maxAge((int) (JwtUtils.getAccessExpirationTime() / 1000))
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    public static ResponseCookie createRefreshTokenCookie(String token) {
        return ResponseCookie.from("refreshToken", token)
                .path("/")
                .maxAge((int) (JwtUtils.getRefreshExpirationTime() / 1000))
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    public static ResponseCookie createExpiredAccessTokenCookie() {
        return ResponseCookie.from("accessToken", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    public static ResponseCookie createExpiredRefreshTokenCookie() {
        return ResponseCookie.from("refreshToken", "")
                .path("/")
                .maxAge(0)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }
}
