package pixelgwint.model;

public class Uzytkownik {
    private String nazwaUzytkownika;
    private String skrotHasla; // Tutaj będziemy przechowywać "zaszyfrowane" hasło
    private String nazwaPlikuIkonyProfilu; // Nazwa pliku graficznego ikony, np. "ikona1.png"

    public Uzytkownik(String nazwaUzytkownika, String skrotHasla, String nazwaPlikuIkonyProfilu) {
        this.nazwaUzytkownika = nazwaUzytkownika;
        this.skrotHasla = skrotHasla;
        this.nazwaPlikuIkonyProfilu = nazwaPlikuIkonyProfilu;
    }

    // Gettery (metody do pobierania wartości pól)
    // Możesz je wygenerować w IntelliJ: Alt+Insert -> Getter -> Zaznacz wszystkie pola -> OK

    public String getNazwaUzytkownika() {
        return nazwaUzytkownika;
    }

    public String getSkrotHasla() {
        return skrotHasla;
    }

    public String getNazwaPlikuIkonyProfilu() {
        return nazwaPlikuIkonyProfilu;
    }

    // Możesz też dodać Settery, jeśli będziesz potrzebował modyfikować te dane po utworzeniu obiektu,
    // ale na razie gettery wystarczą.

    // Opcjonalnie metoda toString() do łatwego debugowania
    @Override
    public String toString() {
        return "Uzytkownik{" +
                "nazwaUzytkownika='" + nazwaUzytkownika + '\'' +
                ", nazwaPlikuIkonyProfilu='" + nazwaPlikuIkonyProfilu + '\'' +
                '}';
        // Celowo nie wyświetlamy skrótu hasła w toString()
    }
}