package pixelgwint.widok;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.logika.MenedzerMuzyki;
import pixelgwint.model.Uzytkownik;

import java.net.URL;

public class KontrolerUstawien {

    @FXML private VBox rootUstawieniaPane;
    @FXML private Slider suwakGlosnosci;
    @FXML private Button przyciskPowrot;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private Uzytkownik aktualnyUzytkownik;
    private final DropShadow goldGlowEffect = new DropShadow(20, Color.rgb(255, 215, 0, 0.7));

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    public void inicjalizuj(Uzytkownik uzytkownik) {
        this.aktualnyUzytkownik = uzytkownik;
    }

    @FXML
    public void initialize() {
        // Ustawienie tła programistycznie
        ustawTloProgramistycznie();

        // Dodanie efektu hover do przycisku
        przyciskPowrot.setOnMouseEntered(event -> przyciskPowrot.setEffect(goldGlowEffect));
        przyciskPowrot.setOnMouseExited(event -> przyciskPowrot.setEffect(null));

        // Konfiguracja suwaka głośności
        if (suwakGlosnosci != null) {
            suwakGlosnosci.setMin(0);
            suwakGlosnosci.setMax(100);

            // Listener do aktualizacji głośności w czasie rzeczywistym
            suwakGlosnosci.valueProperty().addListener((obs, oldVal, newVal) -> {
                if (pixelGwintAplikacja != null && pixelGwintAplikacja.getMenedzerMuzyki() != null) {
                    // Przelicz wartość z 0-100 na 0.0-1.0
                    pixelGwintAplikacja.getMenedzerMuzyki().ustawGlosnosc(newVal.doubleValue() / 100.0);
                }
            });
        }

        // Ustawienie początkowej wartości suwaka po załadowaniu
        // Używamy Platform.runLater, aby upewnić się, że referencja do aplikacji jest już ustawiona
        javafx.application.Platform.runLater(() -> {
            if (pixelGwintAplikacja != null && pixelGwintAplikacja.getMenedzerMuzyki() != null) {
                double aktualnaGlosnosc = pixelGwintAplikacja.getMenedzerMuzyki().getAktualnaGlosnosc();
                suwakGlosnosci.setValue(aktualnaGlosnosc * 100);
            }
        });
    }

    private void ustawTloProgramistycznie() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png";
        if (rootUstawieniaPane == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerUstawien): rootUstawieniaPane nie zostało wstrzyknięte!");
            return;
        }
        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);
        if (imageUrl == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerUstawien): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
        } else {
            String cssExternalUrl = imageUrl.toExternalForm();
            String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: no-repeat;";
            rootUstawieniaPane.setStyle(newStyle);
        }
    }

    @FXML
    private void handlePowrotAction() {
        if (pixelGwintAplikacja != null) {
            pixelGwintAplikacja.pokazMenuGlowne(aktualnyUzytkownik);
        }
    }
}