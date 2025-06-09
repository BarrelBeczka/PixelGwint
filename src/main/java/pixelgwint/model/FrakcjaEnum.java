package pixelgwint.model;

public enum FrakcjaEnum {
    KROLESTWA_POLNOCY("Królestwa Północy"),
    NILFGAARD("Nilfgaard"), // Dostosowane do Twojej nazwy w CSV
    POTWORY("Potwory"),
    SCOIATAEL("Scoia'tael"),
    // SKELLIGE("Skellige"), // Można odkomentować i dodać, jeśli pojawią się karty tej frakcji
    NEUTRALNA("Neutralna"); // Dla kart, które nie należą do żadnej głównej frakcji

    private final String nazwaWyswietlana;

    FrakcjaEnum(String nazwaWyswietlana) {
        this.nazwaWyswietlana = nazwaWyswietlana;
    }

    public String getNazwaWyswietlana() {
        return nazwaWyswietlana;
    }

    public static FrakcjaEnum fromString(String text) {
        if (text != null) {
            String trimmedText = text.trim(); // Usuwamy wiodące/końcowe spacje
            for (FrakcjaEnum frakcja : FrakcjaEnum.values()) {
                // 1. Porównanie z nazwą wyświetlaną (najbardziej prawdopodobne dla wartości z CSV)
                if (trimmedText.equalsIgnoreCase(frakcja.nazwaWyswietlana)) {
                    return frakcja;
                }
                // 2. Porównanie z nazwą stałej enum (np. KROLESTWA_POLNOCY)
                if (trimmedText.equalsIgnoreCase(frakcja.name())) {
                    return frakcja;
                }
                // 3. Porównanie z nazwą stałej enum bez podkreślników (np. KROLESTWAPOLNOCY)
                if (trimmedText.equalsIgnoreCase(frakcja.name().replace("_", ""))) {
                    return frakcja;
                }
                // 4. Porównanie z nazwą stałej enum ze spacjami zamiast podkreślników (np. KROLESTWA POLNOCY)
                if (trimmedText.equalsIgnoreCase(frakcja.name().replace("_", " "))) {
                    return frakcja;
                }
            }
        }
        // Jeśli żadne z powyższych nie pasuje
        System.err.println("Nie udało się rozpoznać frakcji: '" + text + "'. Ustawiono NEUTRALNA jako wartość domyślną.");
        return NEUTRALNA;
    }
}