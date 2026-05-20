package pl.klinika.Zwierze;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ZwierzeController {
    private final ZwierzeService zwierzeService;
}
