package pixelgwint.logika;

import pixelgwint.model.FrakcjaEnum;
import pixelgwint.model.Karta; // Potrzebne do odtworzenia obiektów Karta
import pixelgwint.model.TaliaUzytkownika;
import pixelgwint.model.Uzytkownik;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MenedzerTalii {

    private static final String SCIEZKA_DO_PLIKU_TALII = "dane/talie_uzytkownikow.csv";
    private static Path sciezkaPlikuTaliiPath;
    private static final String CSV_SEPARATOR = ";";
    private static final String LIST_SEPARATOR = ",";

    static {
        try {
            URL res = MenedzerTalii.class.getClassLoader().getResource(SCIEZKA_DO_PLIKU_TALII);
            if (res == null) {
                URL daneUrl = MenedzerTalii.class.getClassLoader().getResource("dane/");
                Path basePath;
                if (daneUrl != null) {
                    basePath = Paths.get(daneUrl.toURI());
                } else {
                    URL resourcesUrl = MenedzerTalii.class.getClassLoader().getResource("");
                    if (resourcesUrl == null) throw new IOException("Nie można zlokalizować folderu resources.");
                    basePath = Paths.get(resourcesUrl.toURI()).resolve("dane");
                    Files.createDirectories(basePath);
                }
                sciezkaPlikuTaliiPath = basePath.resolve("talie_uzytkownikow.csv");
                if (!Files.exists(sciezkaPlikuTaliiPath)) {
                    Files.createFile(sciezkaPlikuTaliiPath);
                    // Dodaj nagłówek do nowego pliku CSV
                    try (BufferedWriter writer = Files.newBufferedWriter(sciezkaPlikuTaliiPath, StandardCharsets.UTF_8, StandardOpenOption.WRITE)) {
                        writer.write("NazwaProfilu;NazwaTalii;Frakcja;IDDowodcy;IDKartWtalii");
                        writer.newLine();
                    }
                    System.out.println("Utworzono plik talii: " + sciezkaPlikuTaliiPath + " z nagłówkiem.");
                }
            } else {
                sciezkaPlikuTaliiPath = Paths.get(res.toURI());
            }
            System.out.println("Ścieżka do pliku talii użytkowników: " + sciezkaPlikuTaliiPath.toAbsolutePath());
        } catch (URISyntaxException | IOException e) {
            System.err.println("Krytyczny błąd: Nie można zainicjalizować ścieżki do pliku talii użytkowników. " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean zapiszTalie(TaliaUzytkownika talia) {
        if (sciezkaPlikuTaliiPath == null) {
            System.err.println("Ścieżka do pliku talii nie została zainicjalizowana.");
            return false;
        }

        // Sprawdź, czy talia o tej nazwie dla tego użytkownika już istnieje. Jeśli tak, nadpisz lub zwróć błąd.
        // Dla uproszczenia na razie po prostu dopisujemy. Można dodać logikę aktualizacji.
        // Lepszym podejściem byłoby wczytanie wszystkich, usunięcie starej (jeśli istnieje) i dodanie nowej, potem zapis całości.
        // Na razie prosty append:

        String liniaDoZapisu = String.join(CSV_SEPARATOR,
                talia.getNazwaProfiluUzytkownika(),
                talia.getNazwaNadanaTalii(),
                talia.getFrakcja().name(), // Zapisujemy nazwę enum
                String.valueOf(talia.getIdKartyDowodcy()),
                talia.getIdKartWDeckuJakoString()
        );

        try (BufferedWriter writer = Files.newBufferedWriter(sciezkaPlikuTaliiPath, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            writer.write(liniaDoZapisu);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisu talii do pliku: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<TaliaUzytkownika> wczytajTalieUzytkownika(String nazwaProfiluUzytkownika) {
        List<TaliaUzytkownika> talie = new ArrayList<>();
        if (sciezkaPlikuTaliiPath == null || !Files.exists(sciezkaPlikuTaliiPath)) {
            System.err.println("Plik talii użytkowników nie istnieje lub ścieżka jest niepoprawna.");
            return talie;
        }

        try (BufferedReader czytnik = Files.newBufferedReader(sciezkaPlikuTaliiPath, StandardCharsets.UTF_8)) {
            String linia;
            boolean pominNaglowek = true;
            while ((linia = czytnik.readLine()) != null) {
                if (pominNaglowek) { // Pomiń linię nagłówka
                    pominNaglowek = false;
                    continue;
                }
                if (linia.trim().isEmpty()) continue; // Pomiń puste linie

                String[] dane = linia.split(CSV_SEPARATOR, -1); // -1 aby zachować puste pola
                if (dane.length >= 5 && dane[0].trim().equals(nazwaProfiluUzytkownika)) {
                    try {
                        String nazwaTalii = dane[1].trim();
                        FrakcjaEnum frakcja = FrakcjaEnum.fromString(dane[2].trim());
                        int idDowodcy = Integer.parseInt(dane[3].trim());
                        List<Integer> idKart = new ArrayList<>();
                        if (dane[4] != null && !dane[4].trim().isEmpty()) {
                            idKart = Arrays.stream(dane[4].trim().split(LIST_SEPARATOR))
                                    .map(String::trim)
                                    .map(Integer::parseInt)
                                    .collect(Collectors.toList());
                        }
                        talie.add(new TaliaUzytkownika(nazwaProfiluUzytkownika, nazwaTalii, frakcja, idDowodcy, idKart));
                    } catch (NumberFormatException e) {
                        System.err.println("Błąd parsowania ID karty w linii: " + linia + " | " + e.getMessage());
                    } catch (Exception e) {
                        System.err.println("Błąd przetwarzania linii talii: " + linia + " | " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas odczytu pliku talii użytkowników: " + e.getMessage());
            e.printStackTrace();
        }
        return talie;
    }
    // Metoda do usunięcia talii (opcjonalnie, na później)
    // public boolean usunTalie(String nazwaProfilu, String nazwaTalii) { ... }
}