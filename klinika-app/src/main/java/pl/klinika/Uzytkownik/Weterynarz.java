package pl.klinika.Uzytkownik;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "weterynarz")
@Data
@EqualsAndHashCode(callSuper = true)
public class Weterynarz extends Uzytkownik {

    @Column(name = "numer_pwz")
    private String numerPWZ;

    @Column(name = "specjalizacja")
    private String specjalizacja;
}