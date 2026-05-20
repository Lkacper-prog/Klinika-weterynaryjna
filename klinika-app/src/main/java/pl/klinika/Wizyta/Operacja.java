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
    }
}