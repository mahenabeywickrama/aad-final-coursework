package lk.ijse.gdse.project.backend.service.impl;

import lk.ijse.gdse.project.backend.entity.*;
import lk.ijse.gdse.project.backend.repository.OrdersRepository;
import lk.ijse.gdse.project.backend.repository.PaymentRepository;
import lk.ijse.gdse.project.backend.service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;

    @Value("${payhere.app.id}")
    private String appId;

    @Value("${payhere.app.secret}")
    private String appSecret;

    @Value("${payhere.return.url}")
    private String returnUrl;

    @Value("${payhere.cancel.url}")
    private String cancelUrl;

    @Value("${payhere.notify.url}")
    private String notifyUrl;

    public PaymentServiceImpl(OrdersRepository ordersRepository,
                              PaymentRepository paymentRepository) {
        this.ordersRepository = ordersRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Map<String, String> createPaymentSession(Long orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        Map<String, String> fields = new HashMap<>();
        fields.put("sandbox", "true");
        fields.put("merchant_id", appId);
        fields.put("return_url", returnUrl);
        fields.put("cancel_url", cancelUrl);
        fields.put("notify_url", notifyUrl);
        fields.put("order_id", String.valueOf(order.getId()));
        fields.put("items", "CoreOne Order #" + order.getId());
        fields.put("amount", order.getTotalAmount().toPlainString());
        fields.put("currency", "LKR");

        // Optional: fill customer info
        fields.put("first_name", "John");
        fields.put("last_name", "Doe");
        fields.put("email", "john.doe@gmail.com");
        fields.put("phone", "1234567890");
        fields.put("address", "123 Main Street");
        fields.put("city", "Colombo");
        fields.put("country", "Sri Lanka");

        return fields; // just return this to frontend
    }

    @Override
    public Payment handleNotification(Map<String, String> data) {
        // PayHere will post lots of fields. Typical relevant fields:
        // merchant_id, order_id, status_code, payhere_amount, payhere_currency, md5sig (signature), transaction_id etc.
        System.out.println("PayHere IPN: " + data);

        String orderIdStr = data.get("order_id");
        if (orderIdStr == null) {
            throw new RuntimeException("Missing order_id in IPN");
        }
        Long orderId = Long.valueOf(orderIdStr);

        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));

        String status = data.get("status");
        String gatewayTxId = data.getOrDefault("payment_id", data.get("transaction_id")); // vary by gateway
        String amountStr = data.get("payhere_amount") != null ? data.get("payhere_amount") : data.get("amount");

        // TODO: verify signature using your merchant secret. VERY IMPORTANT in production.
        // Example:
        // String md5sig = data.get("md5sig");
        // verify hashed signature using payhere merchant secret and fields.

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod("PAYHERE");
        payment.setAmount(new BigDecimal(amountStr != null ? amountStr : order.getTotalAmount().toPlainString()));
        payment.setTransactionId(gatewayTxId);

        if ("2".equals(status) || "SUCCESS".equalsIgnoreCase(status) || "success".equalsIgnoreCase(status)) {
            payment.setStatus(PaymentStatus.SUCCESS);
            payment.setPaidAt(LocalDateTime.now());
            order.setStatus(OrderStatus.PAID);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            order.setStatus(OrderStatus.FAILED);
        }

        paymentRepository.save(payment);
        ordersRepository.save(order);

        return payment;
    }

}
