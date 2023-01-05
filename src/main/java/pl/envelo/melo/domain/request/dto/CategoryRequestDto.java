package pl.envelo.melo.domain.request.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CategoryRequestDto {

    @NotNull
    @Range(min = 2, max = 255, message = "wrong category name size")
    private String categoryName;
    @NotNull
    private int employeeId;
    private boolean isResolved;


}
