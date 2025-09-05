package lk.ijse.gdse.project.backend.repository;

import lk.ijse.gdse.project.backend.entity.PC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PCRepository extends JpaRepository<PC, Long> {
    @Query("SELECT p FROM PC p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.series) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.cpu) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.gpu) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<PC> searchPCs(@Param("search") String search, Pageable pageable);
}
