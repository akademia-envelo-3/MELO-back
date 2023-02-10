package pl.envelo.melo.domain.unit;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UnitRepository extends JpaRepository<Unit, Integer> {
    Optional<Unit> findByName(String name);

    List<Unit> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String name, String description);
}
