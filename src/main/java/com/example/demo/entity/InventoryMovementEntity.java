package com.example.demo.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;

import com.example.demo.enums.MovementReasonEnum;
import com.example.demo.enums.MovementTypeEnum;
import com.example.demo.enums.ReferenceTypeEnum;

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
@Table(name = "inventory_movement")
public class InventoryMovementEntity extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_movement_id_seq")
    @SequenceGenerator(name = "inventory_movement_id_seq", sequenceName = "inventory_movement_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItemEntity inventoryItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false, length = 20)
    private MovementTypeEnum movementType;

    @Column(name = "quantity_before", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityBefore;

    @Column(name = "quantity_after", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityAfter;

    @Column(name = "quantity_changed", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityChanged;

    @Column(name = "unit_cost_at_time", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitCostAtTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "reason", nullable = false, length = 50)
    private MovementReasonEnum reason;

    @Column(name = "reference_id")
    private Integer referenceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "reference_type", length = 20)
    private ReferenceTypeEnum referenceType;

    @Column(name = "notes", length = 500)
    private String notes;

    @Column(name = "movement_date", nullable = false)
    private Instant movementDate;

    @PrePersist
    public void prePersist() {
        super.prePersist();
        if (movementDate == null) {
            movementDate = Instant.now();
        }
    }
}