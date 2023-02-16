package pl.envelo.melo.exceptions;

public class AppUserNotFound extends RuntimeException{
    public AppUserNotFound() {
        super("User not found");
    }

    public AppUserNotFound(String message) {
        super(message);
    }
}
