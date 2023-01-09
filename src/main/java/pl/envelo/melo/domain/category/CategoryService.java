package pl.envelo.melo.domain.category;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;


    public ResponseEntity<CategoryDto> changeStatusCategory(int id) {
        return null;
    }

    public ResponseEntity<CategoryDto> insertNewCategory(CategoryDto categoryDto) {
        return null;
    }

    public ResponseEntity<CategoryDto> getCategory(int id) {
        return null;
    }

    public ResponseEntity<List<CategoryDto>> listAllCategory() {
        return null;
    }
}
