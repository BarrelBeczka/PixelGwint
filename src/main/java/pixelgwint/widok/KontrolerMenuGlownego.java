package pixelgwint.widok;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow; // Import dla DropShadow
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent; // Import dla MouseEvent
import javafx.scene.layout.BorderPane; // ZMIANA: Import dla BorderPane
// import javafx.scene.layout.VBox; // Już niepotrzebny dla rootMenuPane
import javafx.scene.paint.Color; // Import dla Color
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.model.Uzytkownik;

import java.io.InputStream;
import java.net.URL;

public class KontrolerMenuGlownego {

    @FXML
    private BorderPane rootMenuPane; // ZMIANA: Typ na BorderPane

    @FXML private ImageView obrazekProfilu;
    @FXML private Label etykietaPowitalna;
    @FXML private Button przyciskGraj;
    @FXML private Button przyciskZarzadzajTaliami;
    @FXML private Button przyciskStatystyki;
    @FXML private Button przyciskWyloguj;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private Uzytkownik aktualnyUzytkownik;

    private final DropShadow goldGlowEffect = new DropShadow(20, Color.rgb(255, 215, 0, 0.7)); // Złota poświata (promień, kolor, intensywność)
    // Możesz dostosować promień (pierwszy parametr) i intensywność (ostatni parametr alpha w Color.rgb)

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    public void initialize() {
        ustawTloProgramistycznie(); // Ustawienie tła dla rootMenuPane

        // Dodanie efektu hover do przycisków
        Button[] przyciskiMenu = {przyciskGraj, przyciskZarzadzajTaliami, przyciskStatystyki, przyciskWyloguj};
        for (Button przycisk : przyciskiMenu) {
            if (przycisk != null) {
                przycisk.setOnMouseEntered(event -> przycisk.setEffect(goldGlowEffect));
                przycisk.setOnMouseExited(event -> przycisk.setEffect(null));
            }
        }
    }

    private void ustawTloProgramistycznie() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png";
        if (rootMenuPane == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerMenu): rootMenuPane nie zostało wstrzyknięte!");
            return;
        }
        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);
        if (imageUrl == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerMenu): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
        } else {
            String cssExternalUrl = imageUrl.toExternalForm();
            String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: no-repeat;";
            // Dodaj istniejący styl (np. kolor tła), jeśli FXML go nie ma, a chcesz fallback
            String existingStyle = rootMenuPane.getStyle() != null ? rootMenuPane.getStyle() : "";
            if (!existingStyle.contains("-fx-background-image:")) { // Zapobiegaj dublowaniu, jeśli FXML miałby już obrazek
                rootMenuPane.setStyle(existingStyle + (existingStyle.endsWith(";") || existingStyle.isEmpty() ? "" : ";") + newStyle);
            } else { // Jeśli FXML już próbował ustawić obrazek, nadpisz to
                rootMenuPane.setStyle(newStyle); // Upewnij się, że stary -fx-background-image jest nadpisywany
            }
            System.out.println("INFO (KontrolerMenu): Tło obrazkowe Menu Głównego powinno być ustawione programistycznie.");
        }
    }


    public void inicjalizujDane(Uzytkownik uzytkownik) {
        // ... (istniejący kod - bez zmian) ...
        this.aktualnyUzytkownik = uzytkownik;
        if (etykietaPowitalna != null && aktualnyUzytkownik != null && aktualnyUzytkownik.getNazwaUzytkownika() != null) {
            etykietaPowitalna.setText("Witaj, " + aktualnyUzytkownik.getNazwaUzytkownika() + "!");
        }
        String nazwaPlikuIkony = (aktualnyUzytkownik != null) ? aktualnyUzytkownik.getNazwaPlikuIkonyProfilu() : null;
        if (nazwaPlikuIkony != null && !nazwaPlikuIkony.isEmpty()) {
            try (InputStream stream = getClass().getResourceAsStream("/grafiki/ikony_profilu/" + nazwaPlikuIkony)) {
                if (stream != null && obrazekProfilu != null) {
                    obrazekProfilu.setImage(new Image(stream));
                } else {
                    if(obrazekProfilu != null) System.err.println("Nie można załadować ikony profilu: /grafiki/ikony_profilu/" + nazwaPlikuIkony);
                    ustawDomyslnaIkone();
                }
            } catch (Exception e) {
                System.err.println("Błąd podczas ładowania ikony profilu: " + e.getMessage());
                ustawDomyslnaIkone();
            }
        } else {
            ustawDomyslnaIkone();
        }
    }

    private void ustawDomyslnaIkone() {
        // ... (bez zmian) ...
        if (obrazekProfilu == null) return;
        try (InputStream stream = getClass().getResourceAsStream("/grafiki/ikony_profilu/default_icon.png")) {
            if (stream != null) {
                obrazekProfilu.setImage(new Image(stream));
            } else {
                System.err.println("Nie można załadować domyślnej ikony profilu.");
            }
        } catch (Exception e) {
            System.err.println("Błąd podczas ładowania domyślnej ikony: " + e.getMessage());
        }
    }

    @FXML
    private void handleGrajButtonAction() {
        System.out.println("Przycisk Graj (Hot Seat) wciśnięty przez: " + aktualnyUzytkownik.getNazwaUzytkownika());
        if (pixelGwintAplikacja != null && aktualnyUzytkownik != null) {
            pixelGwintAplikacja.pokazEkranWyboruTrybuTalii(aktualnyUzytkownik); // ZMIANA TUTAJ
        }
    }

    @FXML
    private void handleZarzadzajTaliamiButtonAction() {
        System.out.println("Przycisk Zarządzaj Taliami wciśnięty przez: " + aktualnyUzytkownik.getNazwaUzytkownika());
        if (pixelGwintAplikacja != null && aktualnyUzytkownik != null) {
            // Przekierowanie do nowego ekranu zarządzania taliami
            pixelGwintAplikacja.pokazEkranZarzadzaniaTaliami(aktualnyUzytkownik);
        }
    }

    @FXML
    private void handleStatystykiButtonAction() {
        System.out.println("Przycisk Statystyki wciśnięty");
        // Tutaj będzie logika przejścia do ekranu statystyk
    }

    @FXML
    private void handleWylogujButtonAction() {
        System.out.println("Przycisk Wyloguj wciśnięty");
        aktualnyUzytkownik = null; // Wyczyść dane zalogowanego użytkownika
        if (pixelGwintAplikacja != null) {
            pixelGwintAplikacja.pokazEkranLogowania(); // Powrót do ekranu logowania
        }
    }
}