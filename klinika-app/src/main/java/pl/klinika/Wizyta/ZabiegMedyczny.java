package pl.klinika.Wizyta;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "zabieg_medyczny")
public abstract class ZabiegMedyczny {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nazwa", nullable = false)
    private String nazwa;

    @Column(name = "cena_bazowa", nullable = false)
    private double cenaBazowa;

    @ManyToOne
    @JoinColumn(name = "wizyta_id")
    @JsonIgnore
    private Wizyta wizyta;

    public abstract void wykonajZabieg(Wizyta wizyta);
}