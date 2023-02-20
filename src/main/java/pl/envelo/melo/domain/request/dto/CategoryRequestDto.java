package pl.envelo.melo.domain.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@NoArgsConstructor
public class CategoryRequestDto {

    @NotNull
    @Range(min = 2, max = 255, message = "wrong category name size (must be between 2 and 255)")
    private String categoryName;
    @NotNull
    private int employeeId;
    private boolean isResolved;
}
