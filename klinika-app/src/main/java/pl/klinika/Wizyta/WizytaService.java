package pl.klinika.Wizyta;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.klinika.Core.NiedostepnyTerminException;
import pl.klinika.Core.WeterynarzNieZnalezionyException;
import pl.klinika.Core.ZwierzeNieZnalezioneException;
import pl.klinika.Uzytkownik.Weterynarz;
import pl.klinika.Uzytkownik.UzytkownikRepository;
import pl.klinika.Zwierze.Zwierze;
import pl.klinika.Zwierze.ZwierzeRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WizytaService {
    private final WizytaRepository wizytaRepository;
    private final ZwierzeRepository zwierzeRepository;
    private final UzytkownikRepository uzytkownikRepository;

    /**
     * Sprawdza czy dany weterynarz nie ma już zaplanowanej wizyty w proponowanym czasie.
     * Bierze pod uwagę, że każda wizyta trwa 30 minut.
     *
     * @param data  data i godzina proponowanej wizyty
     * @param vetId ID weterynarza
     * @return true jeśli termin jest dostępny, false jeśli koliduje z inną wizytą
     */
    public boolean sprawdzKonfliktTerminow(LocalDateTime data, Integer vetId) {

        LocalDateTime koniecNowaWizyta = data.plusMinutes(30);
        List<Wizyta> konflikty = wizytaRepository.findByWeterynarz_IdAndData(vetId, data, koniecNowaWizyta);
        return konflikty.isEmpty();
    }

    /**
     * Umawia nową wizytę jeśli termin jest dostępny
     *
     * @param dto DTO zawierające dane nowej wizyty
     * @return utworzona wizyta
     * @throws NiedostepnyTerminException    jeśli termin jest zajęty
     * @throws ZwierzeNieZnalezioneException jeśli zwierzę lub weterynarz nie istnieje
     */
    public Wizyta umowWizyte(WizytaCreateDTO dto) {
        LocalDateTime data = dto.data();
        Integer zwierzeId = dto.zwierzeId();
        Integer vetId = dto.vetId();

        if (!sprawdzKonfliktTerminow(data, vetId)) {
            throw new NiedostepnyTerminException(
                    "Weterynarz o ID " + vetId + " ma już zaplanowaną wizytę w dniu " + data.toLocalDate()
            );
        }


        Optional<Zwierze> zwierze = zwierzeRepository.findById(zwierzeId);
        if (zwierze.isEmpty()) {
            throw new ZwierzeNieZnalezioneException(
                    "Zwierzę o ID " + zwierzeId + " nie zostało znalezione"
            );
        }


        Optional<Weterynarz> weterynarz = uzytkownikRepository.findById(vetId)
                .filter(u -> u instanceof Weterynarz)
                .map(u -> (Weterynarz) u);

        if (weterynarz.isEmpty()) {
            throw new WeterynarzNieZnalezionyException(
                    "Weterynarz o ID " + vetId + " nie został znaleziony"
            );
        }


        Wizyta nowaWizyta = new Wizyta();
        nowaWizyta.setDataczas(data);
        nowaWizyta.setZwierze(zwierze.get());
        nowaWizyta.setWeterynarz(weterynarz.get());
        nowaWizyta.setStatus(StatusWizyty.ZAPLANOWANA);

        return wizytaRepository.save(nowaWizyta);
    }

    /**
     * Realizuje wizytę poprzez wykonanie wszystkich zabiegów i zmianę statusu na ZAKOŃCZONA
     *
     * @param wizytaId ID wizyty do realizacji
     * @return zrealizowana wizyta
     * @throws ZwierzeNieZnalezioneException jeśli wizyta nie istnieje
     */
    public Wizyta zrealizujWizyte(Integer wizytaId, ZabiegCreateDTO dto) { // Zmienione parametry!
        Optional<Wizyta> wizyta = wizytaRepository.findById(wizytaId);

        if (wizyta.isEmpty()) {
            throw new ZwierzeNieZnalezioneException("Wizyta o ID " + wizytaId + " nie została znaleziona");
        }

        Wizyta existingWizyta = wizyta.get();

        if (StatusWizyty.ZAKONCZONA.equals(existingWizyta.getStatus())) {
            throw new IllegalStateException("Ta wizyta została już wcześniej zrealizowana.");
        }

        // --- NOWE: Tworzymy nowy zabieg (wykorzystujemy Twoją klasę Konsultacja) ---
        Konsultacja zabieg = new Konsultacja();
        zabieg.setNazwa(dto.getNazwa());
        zabieg.setCenaBazowa(dto.getKoszt());
        zabieg.setWywiad(dto.getOpis());
        zabieg.setWizyta(existingWizyta); // Przypisujemy zabieg do wizyty

        // Upewniamy się, że lista nie jest nullem i dodajemy zabieg
        if (existingWizyta.getZabiegi() == null) {
            existingWizyta.setZabiegi(new java.util.ArrayList<>());
        }
        existingWizyta.getZabiegi().add(zabieg);

        // Wykonujemy zabiegi (Twoja logika z log.info)
        for (ZabiegMedyczny z : existingWizyta.getZabiegi()) {
            z.wykonajZabieg(existingWizyta);
        }

        existingWizyta.setStatus(StatusWizyty.ZAKONCZONA);

        // Dzięki 'cascade = CascadeType.ALL' w klasie Wizyta, zabieg zapisze się automatycznie do bazy!
        return wizytaRepository.save(existingWizyta);
    }
}
