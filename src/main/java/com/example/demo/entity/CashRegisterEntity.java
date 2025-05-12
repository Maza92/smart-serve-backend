package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;

import com.example.demo.enums.CashRegisterEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "cash_register")
public class CashRegisterEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cash_register_id_seq")
    @SequenceGenerator(name = "cash_register_id_seq", sequenceName = "cash_register_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(name = "open_date")
    private Instant openDate;

    @Column(name = "close_date")
    private Instant closeDate;

    @Column(name = "initial_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal initialAmount;

    @Column(name = "final_amount", precision = 10, scale = 2)
    private BigDecimal finalAmount;

    @Column(name = "expected_amount", precision = 10, scale = 2)
    private BigDecimal expectedAmount;

    @Column(name = "difference", precision = 10, scale = 2)
    private BigDecimal difference;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private CashRegisterEnum status;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "cashRegister", cascade = CascadeType.ALL)
    private Set<TransactionEntity> transactions;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "cashRegister", cascade = CascadeType.ALL)
    private Set<CashMovementEntity> cashMovements;

    @PrePersist
    public void prePersist() {
        super.prePersist();

        if (status == null) {
            status = CashRegisterEnum.CREATED;
        }
    }

    @PreUpdate
    public void preUpdate() {
        super.preUpdate();
    }
}
