package avito.internassignment.exceptions;

public class IdenticalTransferIdException extends RuntimeException{

    public IdenticalTransferIdException(String message) {
        super(message);
    }
}
