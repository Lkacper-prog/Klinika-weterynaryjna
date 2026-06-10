package pl.klinika.Wizyta;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

@Entity
@Table(name = "konsultacja")
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Konsultacja extends ZabiegMedyczny {

    @Column(name = "wywiad")
    private String wywiad;

    @Override
    public void wykonajZabieg(Wizyta wizyta) {
        log.info("Przeprowadzenie konsultacji dla zwierzęcia: {}", wizyta.getZwierze().getImie());
        log.info("Wywiad: {}", this.wywiad);
        log.info("Koszt konsultacji: {} PLN", this.getCenaBazowa());
    }
}