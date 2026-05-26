package pl.klinika.Wizyta;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.klinika.Core.NiedostepnyTerminException;
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
     * Sprawdza czy dany weterynarz nie ma już zaplanowanej wizyty w podanym dniu
     * @param data data i godzina proponowanej wizyty
     * @param vetId ID weterynarza
     * @return true jeśli termin jest dostępny, false jeśli jest zajęty
     */
    public boolean sprawdzKonfliktTerminow(LocalDateTime data, Integer vetId) {
        List<Wizyta> wizytaWTymDniu = wizytaRepository.findByWeterynarz_IdAndData(vetId, data);
        return wizytaWTymDniu.isEmpty();
    }

    /**
     * Umawia nową wizytę jeśli termin jest dostępny
     * @param data data i godzina wizyty
     * @param zwierzeId ID zwierzęcia
     * @param vetId ID weterynarza
     * @return utworzona wizyta
     * @throws NiedostepnyTerminException jeśli termin jest zajęty
     * @throws ZwierzeNieZnalezioneException jeśli zwierzę lub weterynarz nie istnieje
     */
    public Wizyta umowWizyte(LocalDateTime data, Integer zwierzeId, Integer vetId) {

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
            throw new ZwierzeNieZnalezioneException(
                "Weterynarz o ID " + vetId + " nie został znaleziony"
            );
        }


        Wizyta nowaWizyta = new Wizyta();
        nowaWizyta.setDataczas(data);
        nowaWizyta.setZwierze(zwierze.get());
        nowaWizyta.setWeterynarz(weterynarz.get());
        nowaWizyta.setStatus("ZAPLANOWANA");

        return wizytaRepository.save(nowaWizyta);
    }

    /**
     * Realizuje wizytę poprzez wykonanie wszystkich zabiegów i zmianę statusu na ZAKOŃCZONA
     * @param wizytaId ID wizyty do realizacji
     * @return zrealizowana wizyta
     * @throws ZwierzeNieZnalezioneException jeśli wizyta nie istnieje
     */
    public Wizyta zrealizujWizyte(Integer wizytaId) {
        Optional<Wizyta> wizyta = wizytaRepository.findById(wizytaId);

        if (wizyta.isEmpty()) {
            throw new ZwierzeNieZnalezioneException(
                "Wizyta o ID " + wizytaId + " nie została znaleziona"
            );
        }

        Wizyta existingWizyta = wizyta.get();


        if (existingWizyta.getZabiegi() != null && !existingWizyta.getZabiegi().isEmpty()) {
            for (ZabiegMedyczny zabieg : existingWizyta.getZabiegi()) {
                zabieg.wykonajZabieg(existingWizyta);
            }
        }


        existingWizyta.setStatus("ZAKOŃCZONA");

        return wizytaRepository.save(existingWizyta);
    }
}
