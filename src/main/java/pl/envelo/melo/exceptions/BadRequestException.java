package pl.envelo.melo.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(){}

    public BadRequestException(String message){
        super(message);
    }
}
