package pl.envelo.melo.domain.category;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    public ResponseEntity<CategoryDto> addNewCategory(CategoryDto categoryDto) {
        return null;
    }

    public ResponseEntity<CategoryDto> getCategory(int id) {
        return null;
    }

    public ResponseEntity<CategoryDto> hideCategory(int id) {
        return null;
    }

    public ResponseEntity<CategoryDto> activateCategory(int id) {
        return null;
    }

    public ResponseEntity<List<CategoryDto>> showAllCategory() {
        return null;
    }
}
