package pl.envelo.melo.domain.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@AllArgsConstructor
@Data
public class CategoryDto {
    @NotBlank
    @Size(min = CategoryConst.MIN_NAME_LENGTH, max = CategoryConst.MAX_NAME_LENGTH, message = CategoryConst.INVALID_NAME)
    private String name;
}
