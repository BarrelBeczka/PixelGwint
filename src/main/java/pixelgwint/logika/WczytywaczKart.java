package pixelgwint.logika;

import pixelgwint.model.FrakcjaEnum;
import pixelgwint.model.Karta;
import pixelgwint.model.TypKartyEnum;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WczytywaczKart {
    private static final String SCIEZKA_DO_PLIKU_KART = "/dane/pixelgwintdb.csv";

    public static List<Karta> wczytajWszystkieKarty() {
        List<Karta> listaKart = new ArrayList<>();
        InputStream inputStream = WczytywaczKart.class.getResourceAsStream(SCIEZKA_DO_PLIKU_KART);

        if (inputStream == null) {
            System.err.println("Nie można znaleźć pliku kart CSV: " + SCIEZKA_DO_PLIKU_KART);
            return listaKart;
        }

        try (BufferedReader czytnik = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String linia;
            czytnik.readLine(); // Pomiń nagłówek

            while ((linia = czytnik.readLine()) != null) {
                if (linia.trim().isEmpty() || linia.startsWith("#")) continue; // Pomiń puste linie i komentarze

                String[] daneKarty = linia.split(";", -1);

                if (daneKarty.length >= 11) {
                    try {
                        int id = Integer.parseInt(daneKarty[0].trim());
                        String nazwa = daneKarty[1].trim();
                        TypKartyEnum typ = TypKartyEnum.fromString(daneKarty[2].trim());

                        int punktySily = 0;
                        if (daneKarty[3].trim() != null && !daneKarty[3].trim().isEmpty()) {
                            try {
                                punktySily = Integer.parseInt(daneKarty[3].trim());
                            } catch (NumberFormatException e) {
                                System.err.println("Błędna wartość punkty_sily dla karty ID " + id + " ('" + nazwa + "'): '" + daneKarty[3].trim() + "'. Ustawiono na 0.");
                            }
                        }

                        String pozycja = daneKarty[4].trim();
                        String umiejetnosc = daneKarty[5].trim();
                        FrakcjaEnum frakcja = FrakcjaEnum.fromString(daneKarty[6].trim());
                        String grafika = daneKarty[7].trim();
                        String umiejetnosc2 = (daneKarty.length > 8 && daneKarty[8] != null) ? daneKarty[8].trim() : "";
                        String pozycja2 = (daneKarty.length > 9 && daneKarty[9] != null) ? daneKarty[9].trim() : "";

                        // Wczytanie nowej kolumny grupaBraterstwa (indeks 10)
                        String grupaBraterstwa = (daneKarty.length > 10 && daneKarty[10] != null) ? daneKarty[10].trim() : null;
                        if (grupaBraterstwa != null && grupaBraterstwa.isEmpty()) {
                            grupaBraterstwa = null;
                        }

                        Karta karta = new Karta(id, nazwa, typ, punktySily, pozycja, umiejetnosc,
                                frakcja, grafika, umiejetnosc2, pozycja2, grupaBraterstwa);
                        listaKart.add(karta);

                    } catch (NumberFormatException e) {
                        System.err.println("Błąd parsowania liczby w linii: " + linia + " | Błąd: " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Nieoczekiwany błąd podczas przetwarzania linii: " + linia + " | Błąd: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    System.err.println("Pominięto linię z powodu niewystarczającej liczby kolumn (" + daneKarty.length + "/11 wymagane): " + linia);
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas wczytywania pliku kart CSV: " + e.getMessage());
            e.printStackTrace();
        }
        return listaKart;
    }

    // Prosta metoda main do przetestowania wczytywania
    public static void main(String[] args) {
        List<Karta> wczytaneKarty = wczytajWszystkieKarty();
        if (wczytaneKarty.isEmpty()) {
            System.out.println("Nie wczytano żadnych kart. Sprawdź ścieżkę do pliku i jego zawartość.");
        } else {
            System.out.println("Wczytano " + wczytaneKarty.size() + " kart:");
            for (Karta karta : wczytaneKarty) {
                System.out.println(karta.toString());
            }
        }
    }
}