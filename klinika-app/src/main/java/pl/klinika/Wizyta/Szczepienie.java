package pl.klinika.Wizyta;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

@Entity
@Table(name = "szczepienie")
@Data
@EqualsAndHashCode(callSuper = true)
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

        System.out.println("Przeprowadzenie szczepienia dla zwierzęcia: " + wizyta.getZwierze().getImie());
        System.out.println("Preparat: " + this.preparat);
        System.out.println("Data szczepienia: " + dataWizyta);
        System.out.println("Data ważności do: " + this.dataWaznosci);
        System.out.println("Ważność: " + this.waznoscWMiesiacach + " miesięcy");
        System.out.println("Koszt szczepienia: " + this.getCenaBazowa() + " PLN");
    }
}