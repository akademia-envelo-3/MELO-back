package pl.envelo.melo.domain.request;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.envelo.melo.domain.category.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRequestRepository extends JpaRepository<CategoryRequest, Integer> {
    List<CategoryRequest> findAllByIsResolved(boolean resolved);

    CategoryRequest findByCategoryName(String categoryName);
}
