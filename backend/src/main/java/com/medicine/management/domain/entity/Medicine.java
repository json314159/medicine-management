package com.medicine.management.domain.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "medicines", uniqueConstraints = @UniqueConstraint(name = "uk_medicine_code", columnNames = "code"))
public class Medicine {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 64) private String code;
    @Column(nullable = false, length = 128) private String name;
    @Column(length = 128) private String genericName;
    @Column(length = 128) private String specification;
    @Column(length = 128) private String manufacturer;
    @Column(length = 64) private String category;
    @Column(nullable = false, length = 32) private String unit;
    @Column(precision = 12, scale = 2) private BigDecimal purchasePrice = BigDecimal.ZERO;
    @Column(precision = 12, scale = 2) private BigDecimal salePrice = BigDecimal.ZERO;
    @Column(nullable = false) private Integer stockQuantity = 0;
    @Column(nullable = false) private Integer stockThreshold = 0;
    private LocalDate expiryDate;
    @Column(length = 64) private String batchNo;
    @Column(nullable = false) private Boolean enabled = true;
    @Column(nullable = false, updatable = false) private LocalDateTime createdAt;
    @Column(nullable = false) private LocalDateTime updatedAt;

    @PrePersist void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate void onUpdate() { updatedAt = LocalDateTime.now(); }
    public Long getId() { return id; }
    public String getCode() { return code; } public void setCode(String code) { this.code = code; }
    public String getName() { return name; } public void setName(String name) { this.name = name; }
    public String getGenericName() { return genericName; } public void setGenericName(String genericName) { this.genericName = genericName; }
    public String getSpecification() { return specification; } public void setSpecification(String specification) { this.specification = specification; }
    public String getManufacturer() { return manufacturer; } public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }
    public String getCategory() { return category; } public void setCategory(String category) { this.category = category; }
    public String getUnit() { return unit; } public void setUnit(String unit) { this.unit = unit; }
    public BigDecimal getPurchasePrice() { return purchasePrice; } public void setPurchasePrice(BigDecimal purchasePrice) { this.purchasePrice = purchasePrice; }
    public BigDecimal getSalePrice() { return salePrice; } public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public Integer getStockQuantity() { return stockQuantity; } public void setStockQuantity(Integer stockQuantity) { this.stockQuantity = stockQuantity; }
    public Integer getStockThreshold() { return stockThreshold; } public void setStockThreshold(Integer stockThreshold) { this.stockThreshold = stockThreshold; }
    public LocalDate getExpiryDate() { return expiryDate; } public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public String getBatchNo() { return batchNo; } public void setBatchNo(String batchNo) { this.batchNo = batchNo; }
    public Boolean getEnabled() { return enabled; } public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
