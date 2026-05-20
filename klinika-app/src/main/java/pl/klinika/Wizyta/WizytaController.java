package pl.klinika.Wizyta;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WizytaController {
    private final WizytaService wizytaService;
}
