package pl.klinika.Wizyta;

/**
 * Enum definiujący możliwe statusy wizyty
 */
public enum StatusWizyty {
    ZAPLANOWANA("Zaplanowana"),
    ZAKONCZONA("Zakończona"),
    ODWOLANA("Odwołana");

    private final String opis;

    StatusWizyty(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}

