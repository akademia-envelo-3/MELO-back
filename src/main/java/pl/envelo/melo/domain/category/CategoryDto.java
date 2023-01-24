package pl.envelo.melo.domain.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class CategoryDto {
    @NotBlank
    @Size(min = 1, max = 255, message = "Size of category name must be between 1 and 255.")
    private String name;
}
