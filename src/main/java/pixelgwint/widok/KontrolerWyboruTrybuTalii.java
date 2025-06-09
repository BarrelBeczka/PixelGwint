package pixelgwint.widok;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow; // Import dla DropShadow
import javafx.scene.layout.VBox;       // Import dla VBox
import javafx.scene.paint.Color;      // Import dla Color
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.model.Uzytkownik;

import java.net.URL; // Import dla URL

public class KontrolerWyboruTrybuTalii {

    @FXML private VBox rootWyboruTrybuPane; // Pole dla głównego kontenera
    @FXML private Button przyciskWlasneTalie;
    @FXML private Button przyciskPredefiniowaneTalie;
    @FXML private Button przyciskPowrotZTrybu;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private Uzytkownik aktualnyUzytkownik;

    // Efekt złotej poświaty dla przycisków
    private final DropShadow goldGlowEffect = new DropShadow(20, Color.rgb(255, 215, 0, 0.7));

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    public void inicjalizujDane(Uzytkownik uzytkownik) {
        this.aktualnyUzytkownik = uzytkownik;
    }

    @FXML
    public void initialize() {
        ustawTloProgramistycznie();

        // Dodanie efektu hover do przycisków
        Button[] przyciski = {przyciskWlasneTalie, przyciskPredefiniowaneTalie, przyciskPowrotZTrybu};
        for (Button przycisk : przyciski) {
            if (przycisk != null) {
                // Style bazowe (kolor tła, tekst, ramka) są już w FXML
                przycisk.setOnMouseEntered(event -> przycisk.setEffect(goldGlowEffect));
                przycisk.setOnMouseExited(event -> przycisk.setEffect(null));
            }
        }
    }

    private void ustawTloProgramistycznie() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png";
        if (rootWyboruTrybuPane == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerWyboruTrybuTalii): rootWyboruTrybuPane nie zostało wstrzyknięte!");
            return;
        }
        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);
        if (imageUrl == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerWyboruTrybuTalii): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
            // FXML ma już styl -fx-background-color: #3c3c3c; jako fallback
        } else {
            String cssExternalUrl = imageUrl.toExternalForm();
            String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: no-repeat;";
            rootWyboruTrybuPane.setStyle(newStyle); // Nadpisuje styl z FXML
            System.out.println("INFO (KontrolerWyboruTrybuTalii): Tło obrazkowe ustawione programistycznie.");
        }
    }

    @FXML
    private void handleWlasneTalieAction() {
        System.out.println("Wybrano: Użyj Własnej Talii");
        if (pixelGwintAplikacja != null && aktualnyUzytkownik != null) {
            pixelGwintAplikacja.pokazEkranWyboruZapisanejTalii(aktualnyUzytkownik, 1);
        }
    }

    @FXML
    private void handlePredefiniowaneTalieAction() {
        System.out.println("Wybrano: Użyj Talii Predefiniowanej");
        if (pixelGwintAplikacja != null && aktualnyUzytkownik != null) {
            pixelGwintAplikacja.pokazEkranWyboruFrakcji(aktualnyUzytkownik, 1, true);
        }
    }

    @FXML
    private void handlePowrotDoMenuAction() {
        if (pixelGwintAplikacja != null && aktualnyUzytkownik != null) {
            pixelGwintAplikacja.pokazMenuGlowne(aktualnyUzytkownik);
        }
    }
}