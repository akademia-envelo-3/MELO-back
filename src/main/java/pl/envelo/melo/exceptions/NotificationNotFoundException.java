package pl.envelo.melo.exceptions;

public class NotificationNotFoundException extends ResourceNotFoundException{
    public NotificationNotFoundException() {
        super("Notification with desired id not found");
    }
}
