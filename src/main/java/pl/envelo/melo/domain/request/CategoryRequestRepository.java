package pl.envelo.melo.domain.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRequestRepository extends JpaRepository<CategoryRequest, Integer> {
    List<CategoryRequest> findAllByIsResolved(boolean resolved);

    CategoryRequest findByCategoryName(String categoryName);
}
