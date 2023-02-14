package pl.envelo.melo.authorization.admin;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByUserId(UUID id);
}
