package pixelgwint.model;

public enum TypRzeduEnum {
    PIECHOTA("Piechota"),         // Rząd walki wręcz
    STRZELECKIE("Strzeleckie"),   // Rząd jednostek dystansowych
    OBLEZENIE("Oblężnicze");    // Rząd machin oblężniczych
    // Karty z pozycją "Dowolne" będą mogły być zagrywane na którykolwiek z tych rzędów.
    // Nie potrzebujemy dla nich osobnego typu rzędu na planszy.

    private final String nazwaWyswietlana;

    TypRzeduEnum(String nazwaWyswietlana) {
        this.nazwaWyswietlana = nazwaWyswietlana;
    }

    public String getNazwaWyswietlana() {
        return nazwaWyswietlana;
    }

    // Metoda do konwersji stringa z pozycji karty na typ rzędu (jeśli potrzebne)
    public static TypRzeduEnum fromStringPozycjiKarty(String pozycjaKarty) {
        if (pozycjaKarty != null) {
            String p = pozycjaKarty.trim().toLowerCase();
            if (p.contains("piechota") || p.contains("melee") || p.contains("bliskie")) return PIECHOTA;
            if (p.contains("strzeleckie") || p.contains("ranged") || p.contains("dystansowe")) return STRZELECKIE;
            if (p.contains("oblężnicze") || p.contains("obleznicze") || p.contains("siege")) return OBLEZENIE;
        }
        return null; // Jeśli nie pasuje lub jest "Dowolne"
    }
}