package pl.klinika.Zwierze;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

/**
 * DTO do rejestracji nowego zwierzęcia
 */
public record ZwierzeCreateDTO(
        @NotBlank(message = "Imię zwierzęcia nie może być puste")
        String imie,

        @NotBlank(message = "Gatunek zwierzęcia nie może być pusty")
        String gatunek,

        @PastOrPresent(message = "Data urodzenia nie może być w przyszłości")
        LocalDate dataurodzenia
) {
}

