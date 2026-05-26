package pl.klinika.Zwierze;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.klinika.Wizyta.Wizyta;
import java.util.List;

@RestController
@RequestMapping("/api/zwierzeta")
@RequiredArgsConstructor
public class ZwierzeController {
    private final ZwierzeService zwierzeService;
    private final ZwierzeRepository zwierzeRepository;

    /**
     * Rejestruje nowe zwierzę w systemie
     * @param zwierze dane zwierzęcia
     * @param wlascicielId ID właściciela (klienta)
     * @return zwierzę z przypisanym ID
     */
    @PostMapping
    public ResponseEntity<Zwierze> zarejestrujZwierze(
            @RequestBody Zwierze zwierze,
            @RequestParam Integer wlascicielId) {
        Zwierze registered = zwierzeService.zarejestrujZwierze(zwierze, wlascicielId);
        return ResponseEntity.status(HttpStatus.CREATED).body(registered);
    }

    /**
     * Pobiera historię leczenia (wszystkie wizyty) dla danego zwierzęcia
     * @param zwierzeId ID zwierzęcia
     * @return lista wizyt zwierzęcia
     */
    @GetMapping("/{zwierzeId}/historia")
    public ResponseEntity<List<Wizyta>> pobierzHistorieLeczenia(@PathVariable Integer zwierzeId) {
        List<Wizyta> historia = zwierzeService.pobierzHistorieLeczenia(zwierzeId);
        return ResponseEntity.ok(historia);
    }

    /**
     * Pobiera szczegóły konkretnego zwierzęcia
     * @param zwierzeId ID zwierzęcia
     * @return dane zwierzęcia
     */

    @GetMapping("/{zwierzeId}")
    public ResponseEntity<Zwierze> pobierzZwierze(@PathVariable Integer zwierzeId) {
        return zwierzeRepository.findById(zwierzeId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}


