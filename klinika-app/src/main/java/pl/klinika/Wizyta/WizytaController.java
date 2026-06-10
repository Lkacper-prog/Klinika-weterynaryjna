package pl.klinika.Wizyta;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/wizyty")
@RequiredArgsConstructor
public class WizytaController {

    private final WizytaService wizytaService;
    private final WizytaRepository wizytaRepository;


    @PostMapping("/umow")
    public ResponseEntity<Wizyta> umowWizyte(
            @RequestBody @Validated WizytaCreateDTO dto) {
        Wizyta nowaWizyta = wizytaService.umowWizyte(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nowaWizyta);
    }

    @PutMapping("/{wizytaId}/zrealizuj")
    public ResponseEntity<Wizyta> zrealizujWizyte(@PathVariable Integer wizytaId,@RequestBody ZabiegCreateDTO dto) {
        Wizyta zrealizowana = wizytaService.zrealizujWizyte(wizytaId,dto);
        return ResponseEntity.ok(zrealizowana);
    }
    @GetMapping
    public ResponseEntity<List<Wizyta>> pobierzWszystkie() {
        return ResponseEntity.ok(wizytaRepository.findAll());
    }
}