package pixelgwint.widok;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import pixelgwint.PixelGwintAplikacja;

import java.io.InputStream;

public class KontrolerEkranuIntro {

    @FXML private StackPane rootIntroPane;
    @FXML private ImageView logoImageView;

    private PixelGwintAplikacja pixelGwintAplikacja;

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    public void initialize() {
        logoImageView.setOpacity(0.0);

        try (InputStream stream = getClass().getResourceAsStream("/grafiki/logo.png")) {
            if (stream != null) {
                logoImageView.setImage(new Image(stream));
                System.out.println("[Intro] Pomyślnie załadowano obrazek logo.png");
            } else {
                System.err.println("BŁĄD KRYTYCZNY: Nie można załadować /grafiki/logo.png.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uruchomIntro() {
        if (pixelGwintAplikacja == null || pixelGwintAplikacja.getMenedzerMuzyki() == null) {
            System.err.println("Błąd krytyczny w Intro: Brak referencji do aplikacji lub menedżera muzyki!");
            if (pixelGwintAplikacja != null) {
                pixelGwintAplikacja.pokazEkranLogowania();
            }
            return;
        }

        pixelGwintAplikacja.getMenedzerMuzyki().odtwarzajIntro();

        // --- SEKWENCJA ANIMACJI ---

        // Timer 1: Poczekaj 3.20s i zacznij POKAZYWAĆ logo.
        PauseTransition timerPojawienia = new PauseTransition(Duration.seconds(3.20));
        timerPojawienia.setOnFinished(event -> {
            System.out.println("Intro @ 3.20s: Start animacji pojawiania się logo (Fade In).");

            FadeTransition pojawienieSieLogo = new FadeTransition(Duration.seconds(1.5), logoImageView);
            pojawienieSieLogo.setFromValue(0.0);
            pojawienieSieLogo.setToValue(1.0);
            pojawienieSieLogo.play();
        });

        // Timer 2: Poczekaj do 7.0s i zacznij ZNIKANIE logo.
        // Animacja zanikania będzie trwała 2.0s, aby zakończyć się dokładnie w 9.0s.
        PauseTransition timerZanikania = new PauseTransition(Duration.seconds(7.0));
        timerZanikania.setOnFinished(event -> {
            System.out.println("Intro @ 7.0s: Start animacji zanikania logo (Fade Out).");

            FadeTransition zanikanieLogo = new FadeTransition(Duration.seconds(2.0), logoImageView);
            zanikanieLogo.setFromValue(1.0);
            zanikanieLogo.setToValue(0.0);

            // Po zakończeniu animacji zanikania (w 9.0s), uruchom SPECJALNE przejście do ekranu logowania.
            zanikanieLogo.setOnFinished(e -> {
                System.out.println("Intro @ 9.0s: Zakończono zanikanie, wywołuję metodę 'rozpocznijPrzejscieDoLogowania()'.");

                // <<< TO JEST KLUCZOWA POPRAWKA >>>
                // Wywołujemy metodę, która gwarantuje animowane przejście.
                pixelGwintAplikacja.rozpocznijPrzejscieDoLogowania();
            });
            zanikanieLogo.play();
        });

        // Uruchom oba timery
        timerPojawienia.play();
        timerZanikania.play();
    }
}