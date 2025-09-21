package lk.ijse.gdse.project.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderUserDTO {
    private Long orderId;
    private String status;
    private BigDecimal total;
    private LocalDateTime createdAt;

    public OrderUserDTO() {
    }

    public OrderUserDTO(Long orderId, String status, BigDecimal total, LocalDateTime createdAt) {
        this.orderId = orderId;
        this.status = status;
        this.total = total;
        this.createdAt = createdAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
