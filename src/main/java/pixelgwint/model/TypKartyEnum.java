package pixelgwint.model;

public enum TypKartyEnum {
    JEDNOSTKA("Jednostka"),
    BOHATER("Bohater"),
    SPECJALNA("Specjalna"),  // To obejmuje również karty pogodowe zgodnie z Twoim opisem
    DOWODCA("Dowódca");      // Zamiast "LIDER", zgodnie z Twoim CSV

    private final String nazwaWyswietlana;

    TypKartyEnum(String nazwaWyswietlana) {
        this.nazwaWyswietlana = nazwaWyswietlana;
    }

    public String getNazwaWyswietlana() {
        return nazwaWyswietlana;
    }

    public static TypKartyEnum fromString(String text) {
        if (text != null) {
            String trimmedText = text.trim();
            for (TypKartyEnum typ : TypKartyEnum.values()) {
                // 1. Porównanie z nazwą wyświetlaną
                if (trimmedText.equalsIgnoreCase(typ.nazwaWyswietlana)) {
                    return typ;
                }
                // 2. Porównanie z nazwą stałej enum
                if (trimmedText.equalsIgnoreCase(typ.name())) {
                    return typ;
                }
            }
        }
        System.err.println("Nie udało się rozpoznać typu karty: '" + text + "'. Ustawiono JEDNOSTKA jako wartość domyślną.");
        return JEDNOSTKA; // Domyślna wartość, jeśli typ nie zostanie rozpoznany
    }
}