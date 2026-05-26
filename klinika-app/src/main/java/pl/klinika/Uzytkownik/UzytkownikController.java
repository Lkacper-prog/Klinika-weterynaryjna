package pl.klinika.Uzytkownik;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/uzytkownicy")
@RequiredArgsConstructor
public class UzytkownikController {

    private final UzytkownikRepository uzytkownikRepository;

    @GetMapping
    public ResponseEntity<List<Uzytkownik>> pobierzWszystkich() {
        return ResponseEntity.ok(uzytkownikRepository.findAll());
    }

    @PostMapping("/klient")
    public ResponseEntity<Klient> dodajKlienta(@RequestBody Klient klient) {
        klient.setRola("KLIENT");
        return ResponseEntity.ok(uzytkownikRepository.save(klient));
    }

    @PostMapping("/weterynarz")
    public ResponseEntity<Weterynarz> dodajWeterynarza(@RequestBody Weterynarz weterynarz) {
        weterynarz.setRola("WETERYNARZ");
        return ResponseEntity.ok(uzytkownikRepository.save(weterynarz));
    }
}