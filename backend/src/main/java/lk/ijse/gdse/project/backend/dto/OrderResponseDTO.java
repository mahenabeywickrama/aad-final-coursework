package lk.ijse.gdse.project.backend.dto;

import java.util.Map;

public class OrderResponseDTO {
    private Long orderId;
    private Map<String, String> paymentFormFields;

    public OrderResponseDTO() {
    }

    public OrderResponseDTO(Long orderId, Map<String, String> paymentFormFields) {
        this.orderId = orderId;
        this.paymentFormFields = paymentFormFields;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Map<String, String> getPaymentFormFields() {
        return paymentFormFields;
    }

    public void setPaymentFormFields(Map<String, String> paymentFormFields) {
        this.paymentFormFields = paymentFormFields;
    }
}
