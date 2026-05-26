package pl.klinika.Zwierze;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.klinika.Core.ZwierzeNieZnalezioneException;
import pl.klinika.Uzytkownik.Klient;
import pl.klinika.Uzytkownik.KlientRepository;
import pl.klinika.Wizyta.Wizyta;
import pl.klinika.Wizyta.WizytaRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ZwierzeService {
    private final ZwierzeRepository zwierzeRepository;
    private final KlientRepository klientRepository;
    private final WizytaRepository wizytaRepository;

    /**
     * Rejestruje nowe zwierzę w systemie i powiązuje je z istniejącym klientem
     * @param zwierze nowe zwierzę do rejestracji
     * @param wlascicielId ID klienta (właściciela zwierzęcia)
     * @return zapisane zwierzę
     * @throws pl.klinika.Core.ZwierzeNieZnalezioneException jeśli klient nie istnieje
     */
    public Zwierze zarejestrujZwierze(Zwierze zwierze, Integer wlascicielId) {
        Optional<Klient> klient = klientRepository.findById(wlascicielId);

        if (klient.isEmpty()) {
            throw new ZwierzeNieZnalezioneException(
                "Klient o ID " + wlascicielId + " nie został znaleziony w systemie"
            );
        }

        zwierze.setWlasciciel(klient.get());
        return zwierzeRepository.save(zwierze);
    }

    /**
     * Pobiera historię leczenia (wszystkie wizyty) dla danego zwierzęcia
     * @param zwierzeId ID zwierzęcia
     * @return lista wizyt zwierzęcia
     * @throws ZwierzeNieZnalezioneException jeśli zwierzę nie istnieje
     */
    public List<Wizyta> pobierzHistorieLeczenia(Integer zwierzeId) {
        Optional<Zwierze> zwierze = zwierzeRepository.findById(zwierzeId);

        if (zwierze.isEmpty()) {
            throw new ZwierzeNieZnalezioneException(
                "Zwierzę o ID " + zwierzeId + " nie zostało znalezione w systemie"
            );
        }

        return wizytaRepository.findByZwierze_Id(zwierzeId);
    }
}
