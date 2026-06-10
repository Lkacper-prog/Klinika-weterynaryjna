package pl.klinika.Uzytkownik;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "uzytkownik")
public class Uzytkownik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "haslo", nullable = false)
    private String password;

    @Column(name = "rola", nullable = false)
    @Enumerated(EnumType.STRING)
    private RolaUzytkownika rola;
}