package lk.ijse.gdse.project.backend.repository;

import lk.ijse.gdse.project.backend.entity.OrdersPC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersPCRepository extends JpaRepository<OrdersPC, Long> {
}
