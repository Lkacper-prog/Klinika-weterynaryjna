package pl.klinika.Zwierze;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.klinika.Core.ZwierzeNieZnalezioneException;
import pl.klinika.Uzytkownik.Klient;
import pl.klinika.Uzytkownik.KlientRepository;
import pl.klinika.Wizyta.StatusWizyty;
import pl.klinika.Wizyta.Wizyta;
import pl.klinika.Wizyta.WizytaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ZwierzeService {
    private final ZwierzeRepository zwierzeRepository;
    private final KlientRepository klientRepository;
    private final WizytaRepository wizytaRepository;

    /**
     * Rejestruje nowe zwierzę w systemie i powiązuje je z istniejącym klientem
     *
     * @param dto          DTO zawierające dane nowego zwierzęcia
     * @param wlascicielId ID klienta (właściciela zwierzęcia)
     * @return zapisane zwierzę
     * @throws pl.klinika.Core.ZwierzeNieZnalezioneException jeśli klient nie istnieje
     */
    public Zwierze zarejestrujZwierze(ZwierzeCreateDTO dto, Integer wlascicielId) {
        Optional<Klient> klient = klientRepository.findById(wlascicielId);

        if (klient.isEmpty()) {
            throw new ZwierzeNieZnalezioneException(
                    "Klient o ID " + wlascicielId + " nie został znaleziony w systemie"
            );
        }

        // Mapowanie DTO na encję
        Zwierze zwierze = new Zwierze();
        zwierze.setImie(dto.imie());
        zwierze.setGatunek(dto.gatunek());
        zwierze.setDataurodzenia(dto.dataurodzenia());
        zwierze.setWlasciciel(klient.get());

        return zwierzeRepository.save(zwierze);
    }

    /**
     * Pobiera historię leczenia (wszystkie wizyty) dla danego zwierzęcia
     *
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

    /**
     * Generuje raport leczenia dla zwierzęcia zawierający wszystkie zakończone wizyty
     *
     * @param zwierzeId ID zwierzęcia
     * @return obiekt RaportLeczeniaDTO z informacjami o leczeniu
     * @throws ZwierzeNieZnalezioneException jeśli zwierzę nie istnieje
     */
    public RaportLeczeniaDTO generujRaportLeczenia(Integer zwierzeId) {
        Optional<Zwierze> zwierze = zwierzeRepository.findById(zwierzeId);

        if (zwierze.isEmpty()) {
            throw new ZwierzeNieZnalezioneException(
                    "Zwierzę o ID " + zwierzeId + " nie zostało znalezione w systemie"
            );
        }

        Zwierze znalezioneZwierze = zwierze.get();

        // Pobranie wszystkich wizyt dla zwierzęcia
        List<Wizyta> wszystkieWizyty = wizytaRepository.findByZwierze_Id(zwierzeId);

        // Filtrowanie tylko zakończonych wizyt
        List<Wizyta> zakonczone = wszystkieWizyty.stream()
                .filter(w -> StatusWizyty.ZAKONCZONA.equals(w.getStatus()))
                .collect(Collectors.toList());

        // Zbieranie wszystkich zabiegów z zakończonych wizyt
        List<RaportLeczeniaDTO.ZabiegSummaryDTO> zabiegi = zakonczone.stream()
                .flatMap(w -> w.getZabiegi() != null ? w.getZabiegi().stream() : java.util.stream.Stream.empty())
                .map(z -> new RaportLeczeniaDTO.ZabiegSummaryDTO(z.getNazwa(), z.getCenaBazowa()))
                .collect(Collectors.toList());

        // Obliczenie łącznego kosztu
        double lacznyKoszt = zakonczone.stream()
                .mapToDouble(Wizyta::podsumujCalkowityKoszt)
                .sum();

        return new RaportLeczeniaDTO(
                znalezioneZwierze.getImie(),
                zakonczone.size(),
                zabiegi,
                lacznyKoszt
        );
    }
}
