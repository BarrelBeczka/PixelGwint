package pixelgwint.widok;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.model.Gracz;

import java.io.InputStream;
import java.util.Random;

public class KontrolerRzutuMoneta {

    @FXML private ImageView monetaImageView;
    @FXML private Label etykietaGraczyRzut;
    @FXML private Button przyciskRzucMoneta;
    @FXML private Label etykietaWynikuRzutu;
    @FXML private Button przyciskRozpocznijGrePoRzucie;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private Gracz gracz1;
    private Gracz gracz2;
    private Gracz graczRozpoczynajacy;

    private Image coin1Image;
    private Image coin2Image;
    private final Random random = new Random();
    private boolean rzutWykonany = false;
    private Timeline animacjaMonety;

    private final String SCIEZKA_COIN1 = "/grafiki/elementy_ui/coin1.png";
    private final String SCIEZKA_COIN2 = "/grafiki/elementy_ui/coin2.png";

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    public void inicjalizujRzutMoneta(Gracz g1, Gracz g2) {
        this.gracz1 = g1;
        this.gracz2 = g2;
        this.rzutWykonany = false;

        etykietaGraczyRzut.setText(g1.getProfilUzytkownika().getNazwaUzytkownika() + " vs " + g2.getProfilUzytkownika().getNazwaUzytkownika());
        etykietaWynikuRzutu.setText("Kto rozpocznie?");
        przyciskRzucMoneta.setDisable(false);
        przyciskRozpocznijGrePoRzucie.setVisible(false);

        coin1Image = ladujObrazekMonety(SCIEZKA_COIN1);
        coin2Image = ladujObrazekMonety(SCIEZKA_COIN2);

        if (coin1Image != null) {
            monetaImageView.setImage(coin1Image); // Ustaw domyślny obrazek monety
        } else {
            etykietaWynikuRzutu.setText("Błąd: Brak grafiki monety!");
            przyciskRzucMoneta.setDisable(true);
        }
    }

    private Image ladujObrazekMonety(String sciezka) {
        try (InputStream stream = getClass().getResourceAsStream(sciezka)) {
            if (stream != null) {
                return new Image(stream);
            } else {
                System.err.println("Nie znaleziono grafiki monety: " + sciezka);
            }
        } catch (Exception e) {
            System.err.println("Błąd ładowania grafiki monety " + sciezka + ": " + e.getMessage());
        }
        return null;
    }

    @FXML
    private void handleRzucMonetaAction() {
        if (coin1Image == null || coin2Image == null) {
            etykietaWynikuRzutu.setText("Błąd: Nie można wykonać rzutu - brak grafik monet.");
            return;
        }

        przyciskRzucMoneta.setDisable(true);
        etykietaWynikuRzutu.setText("Rzucanie...");

        // Prosta animacja "kręcenia się" monety
        final int liczbaZmian = 10; // Ile razy obrazek się zmieni
        final double czasTrwaniaZmiany = 100; // milisekundy

        animacjaMonety = new Timeline();
        animacjaMonety.setCycleCount(liczbaZmian);
        animacjaMonety.getKeyFrames().add(
                new KeyFrame(Duration.millis(czasTrwaniaZmiany), event -> {
                    if (monetaImageView.getImage() == coin1Image) {
                        monetaImageView.setImage(coin2Image);
                    } else {
                        monetaImageView.setImage(coin1Image);
                    }
                })
        );

        animacjaMonety.setOnFinished(event -> {
            boolean wynikPierwszyGracz = random.nextBoolean();
            graczRozpoczynajacy = wynikPierwszyGracz ? gracz1 : gracz2;

            // LOGOWANIE
            System.out.println("[RZUT MONETĄ] Wylosowany gracz rozpoczynający: " +
                    (graczRozpoczynajacy != null ? graczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                    " (Obiekt ID: " + System.identityHashCode(graczRozpoczynajacy) + ")");
            System.out.println("[RZUT MONETĄ] this.gracz1: " +
                    (this.gracz1 != null ? this.gracz1.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                    " (Obiekt ID: " + System.identityHashCode(this.gracz1) + ")");
            System.out.println("[RZUT MONETĄ] this.gracz2: " +
                    (this.gracz2 != null ? this.gracz2.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                    " (Obiekt ID: " + System.identityHashCode(this.gracz2) + ")");


            monetaImageView.setImage(wynikPierwszyGracz ? coin1Image : coin2Image);
            etykietaWynikuRzutu.setText("Rozpoczyna: " + (graczRozpoczynajacy != null ? graczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika() : "BŁĄD") + "!");
            przyciskRozpocznijGrePoRzucie.setVisible(true);
            rzutWykonany = true;
        });

        animacjaMonety.play();
    }
}