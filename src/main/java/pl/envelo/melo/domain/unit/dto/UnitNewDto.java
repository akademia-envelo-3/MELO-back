package pl.envelo.melo.domain.unit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UnitNewDto {
    @NotNull
    @Size(min = 2, max = 255, message = "wrong unit name size (must be between 2 and 255)")
    private String name;
    @Size(max = 4000, message = "too long description (max 4000 characters)")
    private String description;
}
