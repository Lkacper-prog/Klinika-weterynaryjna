package pl.klinika.Core;

public class ZwierzeNieZnalezioneException extends RuntimeException {
    public ZwierzeNieZnalezioneException(String message) {
        super(message);
    }

    public ZwierzeNieZnalezioneException(String message, Throwable cause) {
        super(message, cause);
    }
}

