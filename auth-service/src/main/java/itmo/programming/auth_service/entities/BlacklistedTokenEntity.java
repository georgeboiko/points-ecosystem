package itmo.programming.auth_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "token_blacklist")
public class BlacklistedTokenEntity {
    @Id
    @Column(name = "token_hash")
    private String tokenHash;

    @Column(name = "token_type", nullable = false)
    private String tokenType;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "created_at")
    private Instant createdAt;

    public BlacklistedTokenEntity(String tokenHash, String tokenType, Instant expiresAt) {
        this.tokenHash = tokenHash;
        this.tokenType = tokenType;
        this.expiresAt = expiresAt;
        this.createdAt = Instant.now();
    }

}
