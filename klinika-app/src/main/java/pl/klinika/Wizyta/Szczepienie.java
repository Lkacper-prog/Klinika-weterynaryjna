package pl.klinika.Wizyta;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "szczepienie")
@Data
@EqualsAndHashCode(callSuper = true)
public class Szczepienie extends ZabiegMedyczny {

    @Column(name = "preparat")
    private String preparat;

    @Column(name = "waznosc_w_miesiacach")
    private int waznoscWMiesiacach;

    @Override
    public void wykonajZabieg(Wizyta wizyta) {
    }
}