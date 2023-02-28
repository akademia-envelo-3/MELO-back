package pl.envelo.melo.domain.request.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import pl.envelo.melo.authorization.employee.dto.EmployeeNameDto;

@NoArgsConstructor
@Getter
@Setter
public class CategoryRequestToDisplayOnListDto {
    @Range(min = 2, max = 255, message = "wrong category name size (must be between 2 and 255)")
    private String categoryName;
    private EmployeeNameDto employee;
    private boolean isResolved;
}
