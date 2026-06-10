package pl.klinika.Wizyta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface WizytaRepository extends JpaRepository<Wizyta, Integer> {
    List<Wizyta> findByZwierze_Id(Integer zwierzeId);

    /**
     * Wyszukuje wizyty danego weterynarza, które kolidują z proponowanym czasem.
     * Sprawdza czy nowa wizyta (30 minut) nie pokrywa się z istniejącymi wizytami.
     * Nowa wizyta będzie trwać od :dataczas do :dataczasKoniec (30 minut)
     *
     * @param vetId          ID weterynarza
     * @param dataczas       czas rozpoczęcia nowej wizyty
     * @param dataczasKoniec czas zakończenia nowej wizyty (dataczas + 30 minut)
     * @return lista wizyt, które kolidują z proponowanym czasem
     */
    @Query(value = "SELECT w FROM wizyta w WHERE w.weterynarz_id = :vetId " +
            "AND w.dataczas < :dataczasKoniec " +
            "AND DATE_ADD(w.dataczas, INTERVAL 30 MINUTE) > :dataczas",
            nativeQuery = true)
    List<Wizyta> findByWeterynarz_IdAndData(
            @Param("vetId") Integer vetId,
            @Param("dataczas") LocalDateTime dataczas,
            @Param("dataczasKoniec") LocalDateTime dataczasKoniec
    );
}




