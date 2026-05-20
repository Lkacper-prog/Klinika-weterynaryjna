package pl.klinika.Wizyta;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "konsultacja")
@Data
@EqualsAndHashCode(callSuper = true)
public class Konsultacja extends ZabiegMedyczny {

    @Column(name = "wywiad")
    private String wywiad;

    @Override
    public void wykonajZabieg(Wizyta wizyta) {
    }
}