package pl.envelo.melo.domain.request;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CategoryRequest {
    @Id
    @GeneratedValue
    private int id;
    private String categoryName;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private boolean resolved;
}
