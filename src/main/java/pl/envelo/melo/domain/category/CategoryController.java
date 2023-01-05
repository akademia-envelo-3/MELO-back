package pl.envelo.melo.domain.category;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

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
