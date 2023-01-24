package pl.envelo.melo.exceptions;

public class LocationBadRequestException extends RuntimeException {

    public LocationBadRequestException(){}

    public LocationBadRequestException(String message){
        super(message);
    }
}
