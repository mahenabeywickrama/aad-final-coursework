package lk.ijse.gdse.project.backend.service;

import lk.ijse.gdse.project.backend.dto.CartItemDTO;
import lk.ijse.gdse.project.backend.entity.Orders;
import lk.ijse.gdse.project.backend.entity.User;

import java.util.List;

public interface OrderService {
    Orders createOrder(User user, List<CartItemDTO> cartItems);
    Orders findById(long id);
    List<Orders> getOrdersByUser(User user);
}
