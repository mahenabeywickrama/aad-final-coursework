package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.APIResponse;
import lk.ijse.gdse.project.backend.dto.OrderRequestDTO;
import lk.ijse.gdse.project.backend.dto.OrderResponseDTO;
import lk.ijse.gdse.project.backend.entity.Orders;
import lk.ijse.gdse.project.backend.entity.User;
import lk.ijse.gdse.project.backend.service.OrderService;
import lk.ijse.gdse.project.backend.service.PaymentService;
import lk.ijse.gdse.project.backend.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;
    private final PaymentService paymentService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    public OrderController(OrderService orderService, PaymentService paymentService, UserService userService, ModelMapper modelMapper) {
        this.orderService = orderService;
        this.paymentService = paymentService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO request) {
        User user = userService.getEntityByUsername(request.getUsername());
        if (user == null) {
            return ResponseEntity.status(401).body(null);
        }

        Orders saved = orderService.createOrder(user, request.getItems());
        var paymentFields = paymentService.createPaymentSession(saved.getId());

        OrderResponseDTO resp = new OrderResponseDTO();
        resp.setOrderId(saved.getId());
        resp.setPaymentFormFields(paymentFields);

        return ResponseEntity.ok(resp);
    }
}

