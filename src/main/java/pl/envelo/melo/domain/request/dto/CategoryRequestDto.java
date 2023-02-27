package pl.envelo.melo.domain.request.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import pl.envelo.melo.domain.category.CategoryConst;

@Getter
@Setter
@NoArgsConstructor
public class CategoryRequestDto {

    @NotNull
    @Range(min = 2, max = 255, message = CategoryConst.INVALID_NAME)
    private String categoryName;
    @NotNull
    private int employeeId;
    private boolean isResolved;
}
