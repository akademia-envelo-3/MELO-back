package pl.envelo.melo.domain.unit.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.domain.unit.Unit;
import pl.envelo.melo.domain.unit.UnitConst;

import static pl.envelo.melo.domain.unit.UnitConst.INVALID_UNIT_DESCRIPTION_LENGTH;
import static pl.envelo.melo.domain.unit.UnitConst.INVALID_UNIT_NAME;

@Getter
@Setter
@NoArgsConstructor
public class UnitNewDto {
    @Size(min = UnitConst.MIN_NAME_CHAR_AMOUNT, max = UnitConst.MAX_NAME_CHAR_AMOUNT, message = INVALID_UNIT_NAME)
    private String name;
    @Size(max = UnitConst.MAX_DESCRIPTION_CHAR_AMOUNT, message = INVALID_UNIT_DESCRIPTION_LENGTH)
    private String description;
}
