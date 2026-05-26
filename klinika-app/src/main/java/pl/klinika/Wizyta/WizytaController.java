package pl.klinika.Wizyta;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/wizyty")
@RequiredArgsConstructor
public class WizytaController {

    private final WizytaService wizytaService;


    @PostMapping("/umow")
    public ResponseEntity<Wizyta> umowWizyte(
            @RequestParam LocalDateTime data,
            @RequestParam Integer zwierzeId,
            @RequestParam Integer vetId) {
        Wizyta nowaWizyta = wizytaService.umowWizyte(data, zwierzeId, vetId);
        return ResponseEntity.status(HttpStatus.CREATED).body(nowaWizyta);
    }

    @PutMapping("/{wizytaId}/zrealizuj")
    public ResponseEntity<Wizyta> zrealizujWizyte(@PathVariable Integer wizytaId) {
        Wizyta zrealizowana = wizytaService.zrealizujWizyte(wizytaId);
        return ResponseEntity.ok(zrealizowana);
    }
}