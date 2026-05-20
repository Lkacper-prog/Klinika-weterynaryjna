package pl.klinika.Wizyta;

import jakarta.persistence.*;
import lombok.Data;
import pl.klinika.Uzytkownik.Weterynarz;
import pl.klinika.Zwierze.Zwierze;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Wizyta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "dataczas", nullable = false)
    private LocalDateTime dataczas;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(mappedBy = "wizyta", cascade = CascadeType.ALL)
    private List<ZabiegMedyczny> zabiegi;

    @ManyToOne
    @JoinColumn(name = "zwierze_id")
    private Zwierze zwierze;

    @ManyToOne
    @JoinColumn(name = "weterynarz_id")
    private Weterynarz weterynarz;
}