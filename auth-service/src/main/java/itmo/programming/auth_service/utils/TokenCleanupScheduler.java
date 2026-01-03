package itmo.programming.auth_service.utils;

import itmo.programming.auth_service.services.AuthService;
import org.springframework.scheduling.annotation.Scheduled;

public class TokenCleanupScheduler {

    private final AuthService authService;

    public TokenCleanupScheduler(AuthService authService) {
        this.authService = authService;
    }

    @Scheduled(fixedDelay = 300_000)
    public void cleanup() {
        authService.cleanupExpiredTokens();
    }

}
