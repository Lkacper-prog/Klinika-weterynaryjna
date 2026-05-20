package pl.klinika.Uzytkownik;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UzytkownikController {
    private final UzytkownikService uzytkownikService;
}
