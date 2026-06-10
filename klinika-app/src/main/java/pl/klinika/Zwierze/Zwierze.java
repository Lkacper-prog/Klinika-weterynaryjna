package pl.klinika.Zwierze;

import jakarta.persistence.*;
import lombok.Data;
import pl.klinika.Uzytkownik.Klient;

import java.time.LocalDate;

@Entity
@Data
public class Zwierze {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "imie", nullable = false)
    private String imie;

    @Column(name = "gatunek", nullable = false)
    private String gatunek;

    @Column(name = "dataurodzenia", nullable = false)
    private LocalDate dataurodzenia;

    @ManyToOne
    @JoinColumn(name = "wlasciciel_id")
    private Klient wlasciciel;
}