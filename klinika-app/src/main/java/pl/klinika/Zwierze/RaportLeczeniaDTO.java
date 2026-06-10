package pl.klinika.Zwierze;

import java.util.List;

/**
 * DTO zawierające raport leczenia zwierzęcia
 */
public record RaportLeczeniaDTO(
        String imieZwierzecia,
        int liczbaWizyt,
        List<ZabiegSummaryDTO> zabiegi,
        double lacznyKoszt
) {
    /**
     * Zagnieżdżony rekord zawierający podsumowanie zabiegu
     */
    public record ZabiegSummaryDTO(
            String nazwa,
            double cena
    ) {
    }
}

