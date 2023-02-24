package pl.envelo.melo.authorization;

public enum AuthSucceded implements AuthStatus {
    USER_EXISTS,
    ADMIN_CREATED,
    EMPLOYEE_CREATED,
    ADMIN_AND_EMPLOYEE_CREATED
}
