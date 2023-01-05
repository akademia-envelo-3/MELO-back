package pl.envelo.melo.domain.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRequestRepository extends JpaRepository<CategoryRequest, Integer> {
}
