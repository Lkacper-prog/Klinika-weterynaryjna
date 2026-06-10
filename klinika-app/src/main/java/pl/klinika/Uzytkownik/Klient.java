package pl.klinika.Uzytkownik;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private List<Zwierze> zwierzaki;
}