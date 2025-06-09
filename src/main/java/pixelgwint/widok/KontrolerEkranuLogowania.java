package pixelgwint.widok;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox; // Upewnij się, że ten import jest
import javafx.scene.paint.Color;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.logika.ZarzadzanieUzytkownikami;
import pixelgwint.model.Uzytkownik;

import java.net.URL; // Potrzebny dla getResource()

public class KontrolerEkranuLogowania {

    @FXML
    private VBox rootLoginPane; // Dodane pole dla głównego kontenera VBox

    @FXML private TextField poleNazwaUzytkownika;
    @FXML private PasswordField poleHaslo;
    @FXML private Button przyciskZaloguj;
    @FXML private Button przyciskRejestruj;
    @FXML private Label etykietaStatusu;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private ZarzadzanieUzytkownikami zarzadzanieUzytkownikami;

    public KontrolerEkranuLogowania() {
        zarzadzanieUzytkownikami = new ZarzadzanieUzytkownikami();
    }

    @FXML
    public void initialize() {
        if (etykietaStatusu != null) { // Dodatkowe sprawdzenie
            etykietaStatusu.setText(""); // Wyczyść etykietę statusu na starcie
        }

        // Programistyczne ustawienie obrazka tła
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png";

        if (rootLoginPane == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerLogowania): rootLoginPane nie zostało wstrzyknięte! Sprawdź fx:id w FXML.");
            return;
        }

        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);

        if (imageUrl == null) {
            System.err.println("--------------------------------------------------------------------------");
            System.err.println("BŁĄD KRYTYCZNY (KontrolerLogowania): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
            System.err.println("Upewnij się, że plik istnieje w: src/main/resources" + backgroundImageClasspathPath);
            System.err.println("Używane będzie awaryjne tło z FXML (#2c2c2c).");
            System.err.println("--------------------------------------------------------------------------");
            // Awaryjny styl z FXML zostanie użyty
        } else {
            System.out.println("Diagnostyka (KontrolerLogowania): Obrazek tła '" + backgroundImageClasspathPath + "' znaleziony. URL: " + imageUrl.toExternalForm());
            String cssExternalUrl = imageUrl.toExternalForm();
            String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                    "-fx-background-size: cover; " + // lub 'stretch' lub '100% 100%'
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: no-repeat;";
            rootLoginPane.setStyle(newStyle);
            System.out.println("INFO (KontrolerLogowania): Tło obrazkowe Ekranu Logowania powinno być ustawione programistycznie.");
        }
    }

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    private void handleZalogujButtonAction() {
        String nazwaUzytkownika = poleNazwaUzytkownika.getText();
        String haslo = poleHaslo.getText();

        if (nazwaUzytkownika.isEmpty() || haslo.isEmpty()) {
            pokazStatus("Nazwa użytkownika i hasło nie mogą być puste.", Color.RED);
            return;
        }

        Uzytkownik uzytkownik = zarzadzanieUzytkownikami.zalogujUzytkownika(nazwaUzytkownika, haslo);

        if (uzytkownik != null) {
            // Usunięto pokazStatus, ponieważ komunikat w tym miejscu może być niepotrzebny
            // pokazStatus("Zalogowano pomyślnie jako: " + uzytkownik.getNazwaUzytkownika(), Color.GREEN);
            if (pixelGwintAplikacja != null) {
                pixelGwintAplikacja.pokazMenuGlowne(uzytkownik);
            } else {
                System.err.println("Błąd krytyczny: Referencja do PixelGwintAplikacja jest null w KontrolerEkranuLogowania.");
            }
        } else {
            pokazStatus("Błędna nazwa użytkownika lub hasło.", Color.RED);
        }
    }

    @FXML
    private void handleRejestrujButtonAction() {
        if (pixelGwintAplikacja != null) {
            pixelGwintAplikacja.pokazEkranRejestracji();
        } else {
            System.err.println("Błąd krytyczny: Referencja do PixelGwintAplikacja jest null w KontrolerEkranuLogowania.");
            pokazStatus("Nie można otworzyć ekranu rejestracji.", Color.RED);
        }
    }

    private void pokazStatus(String wiadomosc, Color kolor) {
        if (etykietaStatusu != null) { // Sprawdzenie null
            etykietaStatusu.setText(wiadomosc);
            etykietaStatusu.setTextFill(kolor);
        }
    }
}