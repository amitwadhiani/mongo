package co.arctern.api.provider.domain;

import co.arctern.api.provider.constant.PaymentState;
import co.arctern.api.provider.constant.SettleState;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Data
@NoArgsConstructor
@Entity
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @LastModifiedDate
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp lastModifiedAt;

    @ManyToOne
    @JsonBackReference("task-payment")
    private Task task;

    @Column(nullable = false, columnDefinition = "bigint(20) DEFAULT 1")
    @Version
    private Long version;

    @Column(nullable = false)
    private Double amount;

    @Enumerated(EnumType.STRING)
    private SettleState settleState;

    @Column(nullable = false, columnDefinition = "VARCHAR(255) DEFAULT 'PAID'")
    @Enumerated(EnumType.STRING)
    private PaymentState state;

    @Column
    private String mode;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1", nullable = false)
    private Boolean isPrepaid;

    @Column(columnDefinition = "tinyint(1) DEFAULT 1", nullable = false)
    private Boolean isSettled;

    private Long paidBy;

    private Long settledBy;

    @OneToMany(mappedBy = "payment")
    private Set<PaymentStateFlow> paymentStateFlows;

    @OneToMany(mappedBy = "payment")
    private Set<SettleStateFlow> settleStateFlows;

    public Payment(Long version) {
        this.version = version;
    }
}
