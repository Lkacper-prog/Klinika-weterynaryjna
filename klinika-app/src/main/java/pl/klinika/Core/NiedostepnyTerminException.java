package pl.klinika.Core;

public class NiedostepnyTerminException extends RuntimeException {
    public NiedostepnyTerminException(String message) {
        super(message);
    }

    public NiedostepnyTerminException(String message, Throwable cause) {
        super(message, cause);
    }
}

