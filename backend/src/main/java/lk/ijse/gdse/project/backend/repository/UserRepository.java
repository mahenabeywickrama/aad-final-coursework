package lk.ijse.gdse.project.backend.repository;

import lk.ijse.gdse.project.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
