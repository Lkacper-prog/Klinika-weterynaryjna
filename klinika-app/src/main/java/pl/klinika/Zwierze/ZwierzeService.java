package pl.klinika.Zwierze;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ZwierzeService {
    private final ZwierzeRepository zwierzeRepository;
}
