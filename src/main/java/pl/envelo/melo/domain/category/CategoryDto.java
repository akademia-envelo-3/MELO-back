package pl.envelo.melo.domain.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {
    @NotBlank
    @Size(min = CategoryConst.MIN_NAME_LENGTH, max = CategoryConst.MAX_NAME_LENGTH, message = CategoryConst.INVALID_NAME)
    private String name;
}
