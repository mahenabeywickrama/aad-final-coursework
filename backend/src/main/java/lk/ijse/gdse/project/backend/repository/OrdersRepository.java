package lk.ijse.gdse.project.backend.repository;

import lk.ijse.gdse.project.backend.entity.Orders;
import lk.ijse.gdse.project.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE Orders SET status = 'PAID' WHERE id = ?", nativeQuery = true)
    void updatePaymentStatus(Long id);

    List<Orders> findByUser(User user);

    @Query("SELECT SUM(o.totalAmount) FROM Orders o")
    Double sumRevenue();

    @Query("SELECT COUNT(o) FROM Orders o")
    long countAllOrders();

    @Query("SELECT COUNT(o) FROM Orders o WHERE o.status = 'PENDING'")
    long countPendingOrders();

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Orders o WHERE o.status = 'PAID'")
    double totalRevenue();
}
