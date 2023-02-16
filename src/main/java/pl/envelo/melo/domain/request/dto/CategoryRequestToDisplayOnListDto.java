package pl.envelo.melo.domain.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;

@NoArgsConstructor
@Getter
@Setter
public class CategoryRequestToDisplayOnListDto {
    private String categoryName;
    private EmployeeNameDto employee;
    private boolean isResolved;
}
