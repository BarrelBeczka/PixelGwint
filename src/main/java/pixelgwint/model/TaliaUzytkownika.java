package pixelgwint.model;

import java.util.List;
import java.util.stream.Collectors;

public class TaliaUzytkownika {
    private String nazwaProfiluUzytkownika; // Do kogo należy talia
    private String nazwaNadanaTalii;       // Nazwa talii nadana przez użytkownika
    private FrakcjaEnum frakcja;
    private int idKartyDowodcy;
    private List<Integer> idKartWTalii; // Lista ID kart (bez dowódcy)

    public TaliaUzytkownika(String nazwaProfiluUzytkownika, String nazwaNadanaTalii, FrakcjaEnum frakcja, int idKartyDowodcy, List<Integer> idKartWデッキ) {
        this.nazwaProfiluUzytkownika = nazwaProfiluUzytkownika;
        this.nazwaNadanaTalii = nazwaNadanaTalii;
        this.frakcja = frakcja;
        this.idKartyDowodcy = idKartyDowodcy;
        this.idKartWTalii = idKartWデッキ;
    }

    // Gettery
    public String getNazwaProfiluUzytkownika() { return nazwaProfiluUzytkownika; }
    public String getNazwaNadanaTalii() { return nazwaNadanaTalii; }
    public FrakcjaEnum getFrakcja() { return frakcja; }
    public int getIdKartyDowodcy() { return idKartyDowodcy; }
    public List<Integer> getIdKartWDecku() { return idKartWTalii; } // Zmieniona nazwa gettera

    // Do wyświetlania w ListView itp.
    @Override
    public String toString() {
        return nazwaNadanaTalii + " (" + frakcja.getNazwaWyswietlana() + ")";
    }

    // Metoda do konwersji listy kart na string ID (do zapisu w CSV)
    public String getIdKartWDeckuJakoString() {
        return idKartWTalii.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
}