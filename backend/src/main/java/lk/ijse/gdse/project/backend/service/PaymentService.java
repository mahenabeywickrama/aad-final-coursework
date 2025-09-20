package lk.ijse.gdse.project.backend.service;

import lk.ijse.gdse.project.backend.entity.Payment;

import java.util.Map;

public interface PaymentService {
    Map<String, String> createPaymentSession(Long orderId);
    Payment handleNotification(Map<String, String> data);
}
