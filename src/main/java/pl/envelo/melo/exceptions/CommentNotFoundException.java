package pl.envelo.melo.exceptions;

public class CommentNotFoundException extends ResourceNotFoundException {
    public CommentNotFoundException() {
        super("Comment with desired id not found");
    }
}
