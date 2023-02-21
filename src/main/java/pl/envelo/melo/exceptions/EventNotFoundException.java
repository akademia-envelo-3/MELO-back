package pl.envelo.melo.exceptions;

public class EventNotFoundException extends ResourceNotFoundException{
    public EventNotFoundException() {
        super("Event not found");
    }
}
