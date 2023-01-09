package pl.envelo.melo.domain.category;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryDto {
    @NotBlank
    @Max(value = 255, message = "wrong size")
    private String name;
}
