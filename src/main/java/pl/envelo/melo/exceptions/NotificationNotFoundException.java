package pl.envelo.melo.exceptions;

public class NotificationNotFoundException extends RuntimeException{
    public NotificationNotFoundException() {
        super("Notification with desired id not found");
    }
}
