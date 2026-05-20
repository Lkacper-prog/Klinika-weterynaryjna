package pl.klinika.Wizyta;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WizytaService {
    private final wizytaRepository wizytaRepository;
}
