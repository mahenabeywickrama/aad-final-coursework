package lk.ijse.gdse.project.backend.repository;

import lk.ijse.gdse.project.backend.entity.PC;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PCRepository extends JpaRepository<PC, Long> {
}
