package pl.envelo.melo.domain.unit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.UnitConst;

@Getter
@Setter
@NoArgsConstructor
public class UnitNewDto {
    @Size(min = UnitConst.MIN_NAME_CHAR_AMOUNT, max = UnitConst.MAX_NAME_CHAR_AMOUNT, message = "wrong unit name size (must be between 2 and 255)")
    private String name;
    @Size(max = UnitConst.MAX_DESCRIPTION_CHAR_AMOUNT, message = "too long description (max 4000 characters)")
    private String description;
}
