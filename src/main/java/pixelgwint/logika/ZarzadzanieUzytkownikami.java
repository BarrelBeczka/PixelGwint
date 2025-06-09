package pixelgwint.logika;

import pixelgwint.model.Uzytkownik;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class ZarzadzanieUzytkownikami {

    private static final String SCIEZKA_DO_PLIKU_UZYTKOWNIKOW = "dane/uzytkownicy.csv"; // Względem folderu resources
    private static Path sciezkaPlikuUzytkownikowPath;

    // Inicjalizacja ścieżki do pliku użytkowników
    // To jest trochę bardziej skomplikowane, bo chcemy móc zapisywać do tego pliku,
    // a pliki wewnątrz JARa (po zbudowaniu) są tylko do odczytu.
    // Na etapie developmentu, plik będzie w resources.
    static {
        try {
            URL res = ZarzadzanieUzytkownikami.class.getClassLoader().getResource(SCIEZKA_DO_PLIKU_UZYTKOWNIKOW);
            if (res == null) {
                // Jeśli plik nie istnieje, próbujemy go stworzyć w folderze resources/dane
                // To zadziała tylko podczas developmentu, nie w spakowanym JARze.
                URL daneUrl = ZarzadzanieUzytkownikami.class.getClassLoader().getResource("dane/");
                if (daneUrl == null) {
                    // Jeśli nawet folder 'dane' nie istnieje, tworzymy go (tylko w dev)
                    URL resourcesUrl = ZarzadzanieUzytkownikami.class.getClassLoader().getResource("");
                    if (resourcesUrl != null) {
                        Path resourcesPath = Paths.get(resourcesUrl.toURI());
                        Path danePath = resourcesPath.resolve("dane");
                        Files.createDirectories(danePath); // Utwórz folder 'dane', jeśli nie istnieje
                        sciezkaPlikuUzytkownikowPath = danePath.resolve("uzytkownicy.csv");
                    } else {
                        throw new IOException("Nie można uzyskać dostępu do folderu resources.");
                    }
                } else {
                    sciezkaPlikuUzytkownikowPath = Paths.get(daneUrl.toURI()).resolve("uzytkownicy.csv");
                }

                if (!Files.exists(sciezkaPlikuUzytkownikowPath)) {
                    Files.createFile(sciezkaPlikuUzytkownikowPath);
                    System.out.println("Utworzono plik użytkowników: " + sciezkaPlikuUzytkownikowPath);
                }

            } else {
                sciezkaPlikuUzytkownikowPath = Paths.get(res.toURI());
            }
            System.out.println("Ścieżka do pliku użytkowników: " + sciezkaPlikuUzytkownikowPath.toAbsolutePath());


        } catch (URISyntaxException | IOException e) {
            System.err.println("Krytyczny błąd: Nie można zainicjalizować ścieżki do pliku użytkowników. " + e.getMessage());
            // W przypadku spakowanego JAR, zapis do resources nie jest możliwy.
            // Można by wtedy zapisywać plik w folderze użytkownika (System.getProperty("user.home")).
            // Na razie zostawiamy tak dla developmentu.
            e.printStackTrace();
        }
    }


    private String prostySzyfr(String haslo) {
        // BARDZO PROSTY przykład "szyfrowania" - NIE UŻYWAĆ W PRAWDZIWEJ APLIKACJI!
        return new StringBuilder(haslo).reverse().toString();
    }

    public boolean rejestrujUzytkownika(String nazwaUzytkownika, String haslo, String nazwaPlikuIkony) {
        if (sciezkaPlikuUzytkownikowPath == null) {
            System.err.println("Ścieżka do pliku użytkowników nie została zainicjalizowana.");
            return false;
        }

        // Sprawdź, czy użytkownik już istnieje
        if (pobierzUzytkownika(nazwaUzytkownika) != null) {
            System.out.println("Użytkownik o nazwie " + nazwaUzytkownika + " już istnieje.");
            return false; // Użytkownik już istnieje
        }

        String zaszyfrowaneHaslo = prostySzyfr(haslo);
        String liniaDoZapisu = String.join(";", nazwaUzytkownika, zaszyfrowaneHaslo, nazwaPlikuIkony);

        try (BufferedWriter writer = Files.newBufferedWriter(sciezkaPlikuUzytkownikowPath, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            writer.write(liniaDoZapisu);
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisu do pliku użytkowników: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public Uzytkownik zalogujUzytkownika(String nazwaUzytkownika, String haslo) {
        Uzytkownik uzytkownik = pobierzUzytkownika(nazwaUzytkownika);
        if (uzytkownik != null) {
            if (uzytkownik.getSkrotHasla().equals(prostySzyfr(haslo))) {
                return uzytkownik; // Hasło pasuje
            }
        }
        return null; // Użytkownik nie istnieje lub hasło nie pasuje
    }

    private Uzytkownik pobierzUzytkownika(String nazwaUzytkownika) {
        if (sciezkaPlikuUzytkownikowPath == null || !Files.exists(sciezkaPlikuUzytkownikowPath)) {
            System.err.println("Plik użytkowników nie istnieje lub ścieżka jest niepoprawna.");
            return null;
        }
        try (BufferedReader czytnik = Files.newBufferedReader(sciezkaPlikuUzytkownikowPath, StandardCharsets.UTF_8)) {
            String linia;
            while ((linia = czytnik.readLine()) != null) {
                String[] dane = linia.split(";", -1);
                if (dane.length >= 3 && dane[0].trim().equals(nazwaUzytkownika)) {
                    return new Uzytkownik(dane[0].trim(), dane[1].trim(), dane[2].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas odczytu pliku użytkowników: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Metoda do testowania (można ją później usunąć)
    public static void main(String[] args) {
        ZarzadzanieUzytkownikami manager = new ZarzadzanieUzytkownikami();

        // Test rejestracji
        System.out.println("Rejestracja JanKowalski: " + manager.rejestrujUzytkownika("JanKowalski", "tajne123", "ikona_jan.png"));
        System.out.println("Rejestracja AnnaNowak: " + manager.rejestrujUzytkownika("AnnaNowak", "qwerty", "ikona_anna.png"));
        System.out.println("Rejestracja JanKowalski (ponownie): " + manager.rejestrujUzytkownika("JanKowalski", "innehaslo", "ikona_jan2.png"));


        // Test logowania
        Uzytkownik jan = manager.zalogujUzytkownika("JanKowalski", "tajne123");
        if (jan != null) {
            System.out.println("Zalogowano: " + jan.getNazwaUzytkownika() + ", ikona: " + jan.getNazwaPlikuIkonyProfilu());
        } else {
            System.out.println("Logowanie JanKowalski nie powiodło się.");
        }

        Uzytkownik anna = manager.zalogujUzytkownika("AnnaNowak", "blednehaslo");
        if (anna != null) {
            System.out.println("Zalogowano: " + anna.getNazwaUzytkownika());
        } else {
            System.out.println("Logowanie AnnaNowak (błędne hasło) nie powiodło się.");
        }

        Uzytkownik nieistniejacy = manager.zalogujUzytkownika("Duch", "haslo");
        if (nieistniejacy != null) {
            System.out.println("Zalogowano: " + nieistniejacy.getNazwaUzytkownika());
        } else {
            System.out.println("Logowanie Duch (nieistniejący) nie powiodło się.");
        }
    }
}