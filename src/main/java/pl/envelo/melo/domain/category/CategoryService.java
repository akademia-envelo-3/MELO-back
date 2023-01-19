package pl.envelo.melo.domain.category;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.mappers.CategoryMapper;

import java.util.List;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public ResponseEntity<Boolean> changeStatusCategory(int id) {

        return null;
    }

    public ResponseEntity<CategoryDto> insertNewCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        category.setHidden(false);
        return new ResponseEntity(categoryRepository.save(category), HttpStatus.CREATED);
    }

    public ResponseEntity<?> getCategory(int id) {
        if(categoryRepository.existsById(id)) {
            Category category = categoryRepository.findById(id).get();
            return ResponseEntity.ok(categoryMapper.toDto(category));
        }
        else return ResponseEntity.status(404).body("Category with given ID does not exist in database");
    }

    public ResponseEntity<List<CategoryDto>> listAllCategory() {
        List<Category> listOfCategories = categoryRepository.findAll();
        return ResponseEntity.ok(listOfCategories.stream().map(categoryMapper::toDto).toList());
    }
}
