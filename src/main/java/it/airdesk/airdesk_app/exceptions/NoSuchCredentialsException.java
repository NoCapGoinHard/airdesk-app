package it.airdesk.airdesk_app.exceptions;

public class NoSuchCredentialsException extends RuntimeException {
    public NoSuchCredentialsException(String message) {
        super(message);
    }
}
