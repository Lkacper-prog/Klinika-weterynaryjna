package pl.klinika.Core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Globalna obsługa wyjątków dla aplikacji.
 * Przechwytuje wyjątki biznesowe i zwraca odpowiednie komunikaty HTTP.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Obsługuje wyjątek gdy zwierzę nie zostanie znalezione
     *
     * @param ex wyjątek ZwierzeNieZnalezioneException
     * @return ResponseEntity z komunikatem błędu i statusem 404 Not Found
     */
    @ExceptionHandler(ZwierzeNieZnalezioneException.class)
    public ResponseEntity<String> handleZwierzeNieZnalezioneException(ZwierzeNieZnalezioneException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Obsługuje wyjątek gdy weterynarz nie zostanie znaleziony
     *
     * @param ex wyjątek WeterynarzNieZnalezionyException
     * @return ResponseEntity z komunikatem błędu i statusem 404 Not Found
     */
    @ExceptionHandler(WeterynarzNieZnalezionyException.class)
    public ResponseEntity<String> handleWeterynarzNieZnalezionyException(WeterynarzNieZnalezionyException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ex.getMessage());
    }

    /**
     * Obsługuje wyjątek gdy termin wizyty jest niedostępny (kolizja z inną wizytą)
     *
     * @param ex wyjątek NiedostepnyTerminException
     * @return ResponseEntity z komunikatem błędu i statusem 409 Conflict
     */
    @ExceptionHandler(NiedostepnyTerminException.class)
    public ResponseEntity<String> handleNiedostepnyTerminException(NiedostepnyTerminException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }
}
