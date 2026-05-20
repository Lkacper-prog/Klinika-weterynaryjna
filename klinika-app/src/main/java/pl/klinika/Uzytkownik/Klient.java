package pl.klinika.Uzytkownik;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import pl.klinika.Zwierze.Zwierze;
import java.util.List;

@Entity
@Table(name = "klient")
@Data
@EqualsAndHashCode(callSuper = true)
public class Klient extends Uzytkownik {

    @Column(name = "nr_telefonu")
    private String nrTelefonu;

    @OneToMany(mappedBy = "wlasciciel", cascade = CascadeType.ALL)
    private List<Zwierze> zwierzaki;
}