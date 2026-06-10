package pl.klinika.Uzytkownik;

/**
 * Enum definiujący możliwe role użytkowników w systemie
 */
public enum RolaUzytkownika {
    KLIENT("Klient"),
    WETERYNARZ("Weterynarz"),
    ADMIN("Administrator");

    private final String opis;

    RolaUzytkownika(String opis) {
        this.opis = opis;
    }

    public String getOpis() {
        return opis;
    }
}

