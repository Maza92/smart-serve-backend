package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.Instant;

import com.example.demo.enums.CashMovementTypeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cash_movement")
public class CashMovementEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_movement_id_seq")
    @SequenceGenerator(name = "cash_movement_id_seq", sequenceName = "cash_movement_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cash_register_id", nullable = false)
    private CashRegisterEntity cashRegister;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private CashMovementTypeEnum movementType;

    @Column(name = "reason", length = 255)
    private String reason;

    @Column(name = "movement_date", nullable = false)
    private Instant movementDate;

    @Column(name = "authorized_by", length = 100)
    private String authorizedBy;

    @Column(name = "active", nullable = false)
    private Boolean active;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        if (movementDate == null) {
            movementDate = Instant.now();
        }
        if (active == null) {
            active = true;
        }
    }
}
