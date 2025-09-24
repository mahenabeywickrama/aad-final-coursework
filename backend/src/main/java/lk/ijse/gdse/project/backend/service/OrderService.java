package lk.ijse.gdse.project.backend.service;

import lk.ijse.gdse.project.backend.dto.CartItemDTO;
import lk.ijse.gdse.project.backend.entity.Orders;
import lk.ijse.gdse.project.backend.entity.PC;
import lk.ijse.gdse.project.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface OrderService {
    Orders createOrder(User user, List<CartItemDTO> cartItems);
    Orders findById(long id);
    List<Orders> getOrdersByUser(User user);
    List<Orders> getAllOrders();
    Page<Orders> getAllOrdersFromPage(Pageable pageable);
    Map<String, Object> getOrderStats();
}
