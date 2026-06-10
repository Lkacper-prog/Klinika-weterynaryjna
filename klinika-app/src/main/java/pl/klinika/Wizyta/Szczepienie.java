package pl.klinika.Wizyta;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Entity
@Table(name = "szczepienie")
@Data
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Szczepienie extends ZabiegMedyczny {

    @Column(name = "preparat")
    private String preparat;

    @Column(name = "waznosc_w_miesiacach")
    private int waznoscWMiesiacach;

    @Column(name = "data_waznosci")
    private LocalDate dataWaznosci;

    @Override
    public void wykonajZabieg(Wizyta wizyta) {
        LocalDate dataWizyta = wizyta.getDataczas().toLocalDate();
        this.dataWaznosci = dataWizyta.plusMonths(this.waznoscWMiesiacach);

        log.info("Przeprowadzenie szczepienia dla zwierzęcia: {}", wizyta.getZwierze().getImie());
        log.info("Preparat: {}", this.preparat);
        log.info("Data szczepienia: {}", dataWizyta);
        log.info("Data ważności do: {}", this.dataWaznosci);
        log.info("Ważność: {} miesięcy", this.waznoscWMiesiacach);
        log.info("Koszt szczepienia: {} PLN", this.getCenaBazowa());
    }
}