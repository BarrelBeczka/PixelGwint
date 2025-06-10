package pixelgwint.widok;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.logika.MenedzerTaliiPredefiniowanych;
import pixelgwint.logika.WczytywaczKart;
import pixelgwint.model.Karta;
import javafx.animation.PauseTransition;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class KontrolerEkranuLadowania {

    @FXML private AnchorPane rootLoadingPane;
    @FXML private ImageView loadingAnimationImageView;
    @FXML private Label statusLadowaniaLabel;

    private PixelGwintAplikacja pixelGwintAplikacja;

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    public void initialize() {
        System.out.println("KontrolerEkranuLadowania: Inicjalizacja.");
        // Usunęliśmy ustawianie tła, bo jest teraz w FXML

        if (loadingAnimationImageView != null) {
            final String gifPath = "/grafiki/elementy_ui/loading_anim.gif";
            try (InputStream gifStream = getClass().getResourceAsStream(gifPath)) {
                if (gifStream != null) {
                    loadingAnimationImageView.setImage(new Image(gifStream));
                } else {
                    System.err.println("BŁĄD KRYTYCZNY: Nie można znaleźć zasobu GIF w classpath: " + gifPath);
                }
            } catch (Exception e) {
                System.err.println("Wyjątek podczas ładowania GIF-a '" + gifPath + "': " + e.getMessage());
            }
        }

        if (statusLadowaniaLabel != null) {
            statusLadowaniaLabel.setText("Wczytywanie...");
        }

        rozpocznijWczytywanieZasobow();
    }

    private void rozpocznijWczytywanieZasobow() {
        Task<Void> zadanieLadowania = new Task<>() {
            @Override
            protected Void call() throws Exception {
                // Ta metoda pozostaje bez zmian (wklejam ją dla kompletności)
                try {
                    System.out.println("Task: Wczytywanie definicji kart...");
                    List<Karta> wszystkieKarty = WczytywaczKart.wczytajWszystkieKarty();
                    if (isCancelled()) return null;
                    if (pixelGwintAplikacja != null) {
                        pixelGwintAplikacja.setWszystkieKartyWGrze(wszystkieKarty);
                    }
                    System.out.println("Task: Wczytano " + (wszystkieKarty != null ? wszystkieKarty.size() : 0) + " definicji kart.");

                    if (wszystkieKarty != null && !wszystkieKarty.isEmpty() && pixelGwintAplikacja != null) {
                        Map<String, Image> imageCache = new HashMap<>();
                        long iloscGrafikDoZaladowania = wszystkieKarty.stream().map(Karta::getGrafika).filter(g -> g != null && !g.isEmpty()).distinct().count();
                        System.out.println("Task: Rozpoczynanie wczytywania " + iloscGrafikDoZaladowania + " unikalnych grafik kart...");

                        for (Karta karta : wszystkieKarty) {
                            if (isCancelled()) return null;
                            if (karta.getGrafika() != null && !karta.getGrafika().isEmpty()) {
                                String sciezkaKarty = "/grafiki/karty/" + karta.getGrafika();
                                if (!imageCache.containsKey(sciezkaKarty)) {
                                    try (InputStream stream = getClass().getResourceAsStream(sciezkaKarty)) {
                                        if (stream != null) {
                                            imageCache.put(sciezkaKarty, new Image(stream));
                                        } else {
                                            System.err.println("PRELOAD (Task): Nie znaleziono grafiki: " + sciezkaKarty);
                                        }
                                    } catch (Exception e) {
                                        System.err.println("PRELOAD (Task): Błąd ładowania obrazka '" + sciezkaKarty + "': " + e.getMessage());
                                    }
                                }
                            }
                        }
                        pixelGwintAplikacja.setImageCache(imageCache);
                        System.out.println("Task: Zakończono wczytywanie " + imageCache.size() + " unikalnych grafik kart do cache.");
                    }

                    System.out.println("Task: Wczytywanie plików muzycznych do pamięci podręcznej...");
                    if (pixelGwintAplikacja != null && pixelGwintAplikacja.getMenedzerMuzyki() != null) {
                        pixelGwintAplikacja.getMenedzerMuzyki().wstepneLadowanie();
                    }
                    if (isCancelled()) return null;

                    System.out.println("Task: Inicjalizacja talii predefiniowanych...");
                    new MenedzerTaliiPredefiniowanych();
                    System.out.println("Task: Talie predefiniowane gotowe.");
                    if (isCancelled()) return null;

                    System.out.println("Task: Wszystkie zasoby wczytane!");
                    Thread.sleep(500);

                } catch (Exception e) {
                    System.err.println("Krytyczny błąd podczas wczytywania zasobów gry w Tasku.");
                    e.printStackTrace();
                    throw e;
                }
                return null;
            }
        };

        zadanieLadowania.setOnSucceeded(event -> {
            System.out.println("Wczytywanie zasobów (Task) zakończone pomyślnie.");
            PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
            delay.setOnFinished(e -> {
                if (pixelGwintAplikacja != null) {
                    // <<< ZMIANA TUTAJ >>>
                    // Zamiast do logowania, przechodzimy do ekranu intro
                    pixelGwintAplikacja.pokazEkranIntro();
                }
            });
            delay.play();
        });

        zadanieLadowania.setOnFailed(event -> {
            Throwable throwable = zadanieLadowania.getException();
            System.err.println("Wczytywanie zasobów (Task) nie powiodło się: " + (throwable != null ? throwable.getMessage() : "Brak informacji"));
            if(statusLadowaniaLabel != null) statusLadowaniaLabel.setText("Błąd wczytywania!");

            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> {
                if (pixelGwintAplikacja != null) {
                    // W razie błędu też przejdź do intro, a potem do logowania
                    pixelGwintAplikacja.pokazEkranIntro();
                }
            });
            delay.play();
        });

        new Thread(zadanieLadowania).start();
    }
}