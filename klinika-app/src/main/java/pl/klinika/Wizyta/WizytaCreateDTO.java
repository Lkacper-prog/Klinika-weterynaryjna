package pl.klinika.Wizyta;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * DTO do umawiania nowej wizyty
 */
public record WizytaCreateDTO(
        @Future(message = "Data wizyty musi być w przyszłości")
        LocalDateTime data,

        @NotNull(message = "ID zwierzęcia nie może być puste")
        Integer zwierzeId,

        @NotNull(message = "ID weterynarza nie może być puste")
        Integer vetId
) {
}

