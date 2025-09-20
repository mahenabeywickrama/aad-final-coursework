package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.entity.Payment;
import lk.ijse.gdse.project.backend.service.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("api/v1/payment")
public class PaymentController {

    private final PaymentService paymentService;
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/notify")
    public ResponseEntity<String> paymentNotify(@RequestParam Map<String, String> params) {
        try {
            Payment payment = paymentService.handleNotification(params);
            return ResponseEntity.ok("OK");
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.ok("FAIL");
        }
    }

    @GetMapping("/success")
    public ResponseEntity<String> paymentSuccess() {
        return ResponseEntity.ok("Payment succeeded. Thank you!");
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.ok("Payment canceled. You can try again.");
    }

}
