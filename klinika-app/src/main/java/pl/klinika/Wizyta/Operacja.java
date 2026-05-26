package pl.klinika.Wizyta;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "operacja")
@Data
@EqualsAndHashCode(callSuper = true)
public class Operacja extends ZabiegMedyczny {

    @Column(name = "czy_wymaga_szpitala")
    private Boolean czyWymagaSzpitala;

    @Override
    public void wykonajZabieg(Wizyta wizyta) {
        System.out.println("Przeprowadzenie operacji dla zwierzęcia: " + wizyta.getZwierze().getImie());
        
        if (Boolean.TRUE.equals(this.czyWymagaSzpitala)) {
            System.out.println("⚠️ UWAGA: Operacja wymaga hospitalizacji zwierzęcia!");
            System.out.println("Zalecana obserwacja po operacji: minimum 24h");
        } else {
            System.out.println("ℹ️ Operacja nie wymaga hospitalizacji - pacjent może wrócić do domu");
        }
        
        System.out.println("Koszt operacji: " + this.getCenaBazowa() + " PLN");
    }
}