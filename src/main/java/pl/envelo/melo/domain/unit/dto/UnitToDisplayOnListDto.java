package pl.envelo.melo.domain.unit.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UnitToDisplayOnListDto {
    private String name;
    private String description;
    private int unitId;
}
