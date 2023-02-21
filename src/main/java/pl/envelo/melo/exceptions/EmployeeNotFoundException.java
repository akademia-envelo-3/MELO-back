package pl.envelo.melo.exceptions;

public class EmployeeNotFoundException extends AppUserNotFoundException {
    public EmployeeNotFoundException() {
        super("Employee not found");
    }
}
