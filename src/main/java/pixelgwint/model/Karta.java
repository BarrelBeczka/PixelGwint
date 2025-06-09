package pixelgwint.model;

public class Karta {
    private int id;
    private String nazwa;
    private TypKartyEnum typ;
    private int punktySily;
    private String pozycja;
    private String umiejetnosc; // Główna umiejętność, np. "Braterstwo"
    private FrakcjaEnum frakcja;
    private String grafika;
    private String umiejetnosc2; // Możesz używać do dodatkowych opisów lub innych umiejętności
    private String pozycja2;
    private String grupaBraterstwa; // <<< NOWE POLE (zgodnie z nazwą Twojej kolumny)

    // Zaktualizowany konstruktor
    public Karta(int id, String nazwa, TypKartyEnum typ, int punktySily, String pozycja,
                 String umiejetnosc, FrakcjaEnum frakcja, String grafika,
                 String umiejetnosc2, String pozycja2, String grupaBraterstwa) { // <<< NOWY PARAMETR
        this.id = id;
        this.nazwa = nazwa;
        this.typ = typ;
        this.punktySily = punktySily;
        this.pozycja = pozycja;
        this.umiejetnosc = umiejetnosc;
        this.frakcja = frakcja;
        this.grafika = grafika;
        this.umiejetnosc2 = umiejetnosc2;
        this.pozycja2 = pozycja2;
        this.grupaBraterstwa = grupaBraterstwa; // <<< PRZYPISANIE NOWEGO POLA
    }

    // Istniejące gettery...
    public int getId() { return id; }
    public String getNazwa() { return nazwa; }
    public TypKartyEnum getTyp() { return typ; }
    public int getPunktySily() { return punktySily; }
    public String getPozycja() { return pozycja; }
    public String getUmiejetnosc() { return umiejetnosc; } // Zwraca główną umiejętność
    public FrakcjaEnum getFrakcja() { return frakcja; }
    public String getGrafika() { return grafika; }
    public String getUmiejetnosc2() { return umiejetnosc2; }
    public String getPozycja2() { return pozycja2; }

    // NOWY GETTER
    public String getGrupaBraterstwa() { // <<< NOWY GETTER
        return grupaBraterstwa;
    }

    @Override
    public String toString() {
        return "Karta{" +
                "id=" + id +
                ", nazwa='" + nazwa + '\'' +
                ", typ=" + (typ != null ? typ.getNazwaWyswietlana() : "null") +
                ", siła=" + punktySily +
                ", pozycja='" + pozycja + '\'' +
                ", umiejętność='" + umiejetnosc + '\'' +
                (grupaBraterstwa != null && !grupaBraterstwa.isEmpty() ? ", grupaBraterstwa='" + grupaBraterstwa + '\'' : "") +
                ", frakcja=" + (frakcja != null ? frakcja.getNazwaWyswietlana() : "null") +
                '}';
    }
}