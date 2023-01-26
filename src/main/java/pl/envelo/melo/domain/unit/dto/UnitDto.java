package pl.envelo.melo.domain.unit.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
public class UnitDto {
    @NotNull
    @Range(min = 2, max = 255, message = "wrong unit name size (must be between 2 and 255)")
    private String name;
    @Max(value = 4000, message = "too long description (max 4000 characters)")
    private String description;
}
