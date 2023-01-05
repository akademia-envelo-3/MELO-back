package pl.envelo.melo.domain.poll;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Set;

public class PollAnswer {
    @Id
    @GeneratedValue
    private int id;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private Set<String> pollResult;
}
