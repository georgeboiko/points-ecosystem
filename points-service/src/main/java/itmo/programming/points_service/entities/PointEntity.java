package itmo.programming.points_service.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "points")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 19, scale = 10)
    private BigDecimal x;

    @Column(nullable = false, precision = 19, scale = 10)
    private BigDecimal y;

    @Column(nullable = false, precision = 19, scale = 10)
    private BigDecimal r;

    @Column(nullable = false)
    private boolean success;

    @Column(nullable = false)
    private Long ownerId;

    public PointEntity(BigDecimal x,
                       BigDecimal y,
                       BigDecimal r,
                       boolean success,
                       Long ownerId) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.success = success;
        this.ownerId = ownerId;
    }
}
