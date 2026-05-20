package pl.klinika.Uzytkownik;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UzytkownikService {
    private final UzytkownikRepository uzytkownikRepository;
}
