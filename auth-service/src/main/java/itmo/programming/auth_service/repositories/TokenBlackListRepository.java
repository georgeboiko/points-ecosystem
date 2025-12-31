package itmo.programming.auth_service.repositories;

import itmo.programming.auth_service.entities.BlacklistedTokenEntity;
import java.time.Instant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface TokenBlackListRepository extends JpaRepository<BlacklistedTokenEntity, Long> {
    boolean existsByTokenHashAndTokenTypeAndExpiresAtAfter(
            String tokenHash,
            String tokenType,
            Instant now
    );

    @Modifying
    @Transactional
    void deleteByExpiresAtBefore(Instant now);
}
