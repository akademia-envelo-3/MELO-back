package pl.envelo.melo.domain.category;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.envelo.melo.domain.event.Event;
import pl.envelo.melo.mappers.CategoryMapper;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;


    public ResponseEntity<?> changeStatusCategory(int id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setHidden(!category.isHidden());
            return ResponseEntity.ok(categoryRepository.save(category));
        } else return ResponseEntity.notFound().build();
    }

    public ResponseEntity<?> insertNewCategory(CategoryDto categoryDto) {
        Category category = categoryMapper.toEntity(categoryDto);
        category.setHidden(false);
        return ResponseEntity.status(201).body(categoryRepository.save(category));
//        return new ResponseEntity(categoryRepository.save(category), HttpStatus.CREATED);
    }

    public ResponseEntity<?> editCategoryName(int id, CategoryDto categoryDto) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            category.setName(categoryDto.getName());
            return ResponseEntity.ok(categoryRepository.save(category));
        }
        else return ResponseEntity.status(404).body("Category with given ID does not exist in database");
    }

    public ResponseEntity<?> getCategory(int id) {
        Optional<Category> categoryOptional = categoryRepository.findById(id);
        if(categoryOptional.isPresent()) {
            Category category = categoryOptional.get();
            return ResponseEntity.ok(categoryMapper.toDto(category));
        }
        else return ResponseEntity.status(404).body("Category with given ID does not exist in database");
    }

    public ResponseEntity<List<CategoryDto>> listAllCategory() {
        List<Category> listOfCategories = categoryRepository.findAll();
        return ResponseEntity.ok(listOfCategories.stream().map(categoryMapper::toDto).toList());
    }
}
