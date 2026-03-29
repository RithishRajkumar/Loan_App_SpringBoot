package com.example.college.loan_application.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Loan product is required")
    private LoanProduct product; // HOME, AUTO, PERSONAL

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;

    @NotNull(message = "Tenor months is required")
    @Min(value = 3, message = "Tenor must be at least 3 months")
    @Max(value = 360, message = "Tenor must be at most 360 months")
    private Integer tenorMonths;

    @NotNull(message = "Annual rate is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Annual rate must be at least 0%")
    @DecimalMax(value = "30.0", inclusive = true, message = "Annual rate must be at most 30%")
    private BigDecimal annualRate;

    @Enumerated(EnumType.STRING)
    private LoanState state = LoanState.DRAFT;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getCustomerId() { return customerId; }
    public void setCustomerId(String customerId) { this.customerId = customerId; }
    
    public LoanProduct getProduct() { return product; }
    public void setProduct(LoanProduct product) { this.product = product; }
    
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    
    public Integer getTenorMonths() { return tenorMonths; }
    public void setTenorMonths(Integer tenorMonths) { this.tenorMonths = tenorMonths; }
    
    public BigDecimal getAnnualRate() { return annualRate; }
    public void setAnnualRate(BigDecimal annualRate) { this.annualRate = annualRate; }
    
    public LoanState getState() { return state; }
    public void setState(LoanState state) { this.state = state; }
    
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
