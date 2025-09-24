package lk.ijse.gdse.project.backend.controller;

import lk.ijse.gdse.project.backend.dto.APIResponse;
import lk.ijse.gdse.project.backend.dto.DashboardOrderDTO;
import lk.ijse.gdse.project.backend.entity.Orders;
import lk.ijse.gdse.project.backend.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin/orderdashboard")
public class DashboardOrderController {
    OrderService orderService;

    public DashboardOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("getOrders")
    public ResponseEntity<APIResponse> getOrders() {
        List<Orders> allOrders = orderService.getAllOrders();

        List<DashboardOrderDTO> orderDTOS = new ArrayList<>();

        for (Orders order : allOrders) {
            DashboardOrderDTO dashboardOrderDTO = new DashboardOrderDTO();
            dashboardOrderDTO.setOrderID(order.getId());
            dashboardOrderDTO.setUserID(order.getUser().getId());
            dashboardOrderDTO.setOrderDate(order.getOrderDate());
            dashboardOrderDTO.setOrderAmount(order.getTotalAmount());
            dashboardOrderDTO.setOrderStatus(String.valueOf(order.getStatus()));
            orderDTOS.add(dashboardOrderDTO);
        }

        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Orders Received Successfully",
                        orderDTOS
                )
        );
    }

    @GetMapping("getFromPage")
    public ResponseEntity<APIResponse> getOrdersFromPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Orders> ordersPage;
        ordersPage = orderService.getAllOrdersFromPage(PageRequest.of(page, size));


        Map<String, Object> response = new HashMap<>();
        response.put("data", ordersPage.getContent());
        response.put("totalItems", ordersPage.getTotalElements());

        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Success",
                        response
                )
        );
    }

    @GetMapping("/stats")
    public ResponseEntity<APIResponse> getOrderStats() {
        Map<String, Object> stats = orderService.getOrderStats();

        return ResponseEntity.ok(
                new APIResponse(
                        200,
                        "Success",
                        stats
                )
        );
    }
}
