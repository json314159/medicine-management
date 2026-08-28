package com.medicine.management.domain.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "stock_transactions")
public class StockTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "medicine_id", nullable = false) private Medicine medicine;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 16) private TransactionType type;
    @Column(nullable = false) private Integer quantity;
    @Column(nullable = false) private Integer beforeQuantity;
    @Column(nullable = false) private Integer afterQuantity;
    @Column(length = 255) private String remark;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    @PrePersist void onCreate() { createdAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public Medicine getMedicine() { return medicine; } public void setMedicine(Medicine medicine) { this.medicine = medicine; }
    public TransactionType getType() { return type; } public void setType(TransactionType type) { this.type = type; }
    public Integer getQuantity() { return quantity; } public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public Integer getBeforeQuantity() { return beforeQuantity; } public void setBeforeQuantity(Integer beforeQuantity) { this.beforeQuantity = beforeQuantity; }
    public Integer getAfterQuantity() { return afterQuantity; } public void setAfterQuantity(Integer afterQuantity) { this.afterQuantity = afterQuantity; }
    public String getRemark() { return remark; } public void setRemark(String remark) { this.remark = remark; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
