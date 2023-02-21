package pl.envelo.melo.exceptions;

public class EventNotFoundException extends AppUserNotFoundException{
    public EventNotFoundException() {
        super("Event not found");
    }
}
