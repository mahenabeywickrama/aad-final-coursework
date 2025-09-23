package lk.ijse.gdse.project.backend.service.impl;

import lk.ijse.gdse.project.backend.dto.CartItemDTO;
import lk.ijse.gdse.project.backend.entity.*;
import lk.ijse.gdse.project.backend.repository.OrdersPCRepository;
import lk.ijse.gdse.project.backend.repository.OrdersRepository;
import lk.ijse.gdse.project.backend.repository.PCRepository;
import lk.ijse.gdse.project.backend.repository.PaymentRepository;
import lk.ijse.gdse.project.backend.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrdersRepository ordersRepository;
    private final OrdersPCRepository ordersPCRepository;
    private final PCRepository pcRepository;
    private final PaymentRepository paymentRepository;

    public OrderServiceImpl(OrdersRepository ordersRepository,
                            OrdersPCRepository ordersPCRepository,
                            PCRepository pcRepository,
                            PaymentRepository paymentRepository) {
        this.ordersRepository = ordersRepository;
        this.ordersPCRepository = ordersPCRepository;
        this.pcRepository = pcRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    public Orders createOrder(User user, List<CartItemDTO> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        BigDecimal total = BigDecimal.ZERO;

        List<OrdersPC> orderItems = new ArrayList<>();
        for (CartItemDTO item : cartItems) {
            PC pc = pcRepository.findById(item.getId())
                    .orElseThrow(() -> new RuntimeException("PC not found with ID: " + item.getId()));

            OrdersPC ordersPC = new OrdersPC();
            ordersPC.setOrder(order);
            ordersPC.setPc(pc);
            ordersPC.setQuantity(item.getQuantity());
            ordersPC.setPriceAtPurchase(item.getPrice());
            orderItems.add(ordersPC);

            BigDecimal line = BigDecimal.valueOf(item.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(line);

        }

        order.setTotalAmount(total);
        order.setOrdersItems(orderItems);

        Orders saved = ordersRepository.save(order);

        for (OrdersPC op : orderItems) {
            PC pc = op.getPc();
            Integer totalSales = pc.getTotalSales();
            totalSales = totalSales + op.getQuantity();
            pc.setTotalSales(totalSales);

            op.setOrder(saved);
            ordersPCRepository.save(op);
        }

        Payment payment = new Payment();
        payment.setOrder(saved);
        payment.setPaymentMethod("SIMULATED"); // "CASH", "TEST"
        payment.setAmount(total);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaidAt(LocalDateTime.now());
        paymentRepository.save(payment);

        ordersRepository.updatePaymentStatus(saved.getId());
        return saved;
    }

    @Override
    public Orders findById(long id) {
        return ordersRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Override
    public List<Orders> getOrdersByUser(User user) {
        return ordersRepository.findByUser(user);
    }
}