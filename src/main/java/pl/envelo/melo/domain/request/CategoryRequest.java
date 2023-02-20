package pl.envelo.melo.domain.request;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.envelo.melo.authorization.employee.Employee;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "category_requests")
public class CategoryRequest {
    @Id
    @GeneratedValue
    private int id;

    @NotBlank
    @Column(nullable = false, length = 255)
    private String categoryName;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private boolean isResolved;

}
