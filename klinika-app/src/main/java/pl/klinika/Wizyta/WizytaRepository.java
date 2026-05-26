package pl.klinika.Wizyta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.List;

public interface WizytaRepository extends JpaRepository<Wizyta, Integer> {
    List<Wizyta> findByZwierze_Id(Integer zwierzeId);

    /**
     * Wyszukuje wizyty danego weterynarz w konkretnym przedziale czasu
     * Sprawdza czy termin koliduje z istniejącą wizytą
     */
    @Query("SELECT w FROM Wizyta w WHERE w.weterynarz.id = :vetId " +
           "AND FUNCTION('DATE', w.dataczas) = FUNCTION('DATE', :dataczas)")
    List<Wizyta> findByWeterynarz_IdAndData(
        @Param("vetId") Integer vetId,
        @Param("dataczas") LocalDateTime dataczas
    );
}




