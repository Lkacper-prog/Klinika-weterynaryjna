package pl.klinika.Wizyta;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "operacja")
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Operacja extends ZabiegMedyczny {

    @Column(name = "czy_wymaga_szpitala")
    private Boolean czyWymagaSzpitala;

    @Override
    public void wykonajZabieg(Wizyta wizyta) {
        log.info("Przeprowadzenie operacji dla zwierzęcia: {}", wizyta.getZwierze().getImie());

        if (Boolean.TRUE.equals(this.czyWymagaSzpitala)) {
            log.warn("UWAGA: Operacja wymaga hospitalizacji zwierzęcia!");
            log.info("Zalecana obserwacja po operacji: minimum 24h");
        } else {
            log.info("Operacja nie wymaga hospitalizacji - pacjent może wrócić do domu");
        }

        log.info("Koszt operacji: {} PLN", this.getCenaBazowa());
    }
}