package pixelgwint.widok;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView; // Upewnij się, że ten import jest
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import pixelgwint.PixelGwintAplikacja;
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
    @FXML private ImageView loadingAnimationImageView; // To pole musi odpowiadać fx:id w FXML
    @FXML private Label statusLadowaniaLabel;      // Dla napisu "Wczytywanie..."

    private PixelGwintAplikacja pixelGwintAplikacja;

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    public void initialize() {
        System.out.println("KontrolerEkranuLadowania: Inicjalizacja.");
        ustawTloProgramistycznie(); // Ustawia tło dla rootLoadingPane

        // Programistyczne ustawienie animacji GIF
        if (loadingAnimationImageView != null) {
            final String gifPath = "/grafiki/elementy_ui/loading_anim.gif"; // Upewnij się, że to poprawna ścieżka i nazwa pliku
            try {
                InputStream gifStream = getClass().getResourceAsStream(gifPath);
                if (gifStream != null) {
                    Image loadingGif = new Image(gifStream);
                    if (loadingGif.isError()) {
                        System.err.println("Błąd podczas tworzenia obiektu Image dla GIF-a: " + gifPath);
                        if (loadingGif.getException() != null) {
                            loadingGif.getException().printStackTrace();
                        }
                    } else {
                        loadingAnimationImageView.setImage(loadingGif);
                        System.out.println("INFO: Animacja GIF '" + gifPath + "' powinna być ustawiona na ImageView.");
                    }
                    gifStream.close(); // Zamknij strumień po użyciu
                } else {
                    System.err.println("BŁĄD KRYTYCZNY: Nie można znaleźć zasobu GIF w classpath: " + gifPath);
                    System.err.println("Upewnij się, że plik istnieje w src/main/resources" + gifPath + " i jest poprawnie skompilowany.");
                }
            } catch (Exception e) {
                System.err.println("Wyjątek podczas ładowania GIF-a '" + gifPath + "': " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.err.println("BŁĄD FXML: loadingAnimationImageView nie zostało wstrzyknięte! Sprawdź fx:id.");
        }

        if (statusLadowaniaLabel != null) { // Ustawienie tekstu dla labelki, jeśli istnieje
            statusLadowaniaLabel.setText("Wczytywanie...");
        }

        rozpocznijWczytywanieZasobow(); // Rozpocznij wczytywanie zasobów gry
    }

    private void ustawTloProgramistycznie() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/pixelgwintbackground.png";
        if (rootLoadingPane == null) {
            System.err.println("BŁĄD KRYTYCZNY (ustawTlo): rootLoadingPane nie zostało wstrzyknięte!");
            return;
        }
        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);
        if (imageUrl == null) {
            System.err.println("BŁĄD KRYTYCZNY (ustawTlo): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
        } else {
            System.out.println("Diagnostyka (ustawTlo): Obrazek tła '" + backgroundImageClasspathPath + "' znaleziony. URL: " + imageUrl.toExternalForm());
            String cssExternalUrl = imageUrl.toExternalForm();
            String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: no-repeat;";
            rootLoadingPane.setStyle(newStyle);
            System.out.println("INFO (ustawTlo): Tło obrazkowe Ekranu Ładowania ustawione programistycznie.");
        }
    }

    private void rozpocznijWczytywanieZasobow() {
        Task<Void> zadanieLadowania = new Task<>() {
            // ... (treść metody call() bez zmian - wczytywanie kart i grafik do cache)
            @Override
            protected Void call() throws Exception {
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
                        long zaladowaneGrafiki = 0;
                        System.out.println("Task: Rozpoczynanie wczytywania " + iloscGrafikDoZaladowania + " unikalnych grafik kart...");

                        for (Karta karta : wszystkieKarty) {
                            if (isCancelled()) return null;
                            if (karta.getGrafika() != null && !karta.getGrafika().isEmpty()) {
                                String sciezkaKarty = "/grafiki/karty/" + karta.getGrafika();
                                if (!imageCache.containsKey(sciezkaKarty)) {
                                    try (InputStream stream = getClass().getResourceAsStream(sciezkaKarty)) {
                                        if (stream != null) {
                                            Image img = new Image(stream);
                                            imageCache.put(sciezkaKarty, img);
                                            zaladowaneGrafiki++;
                                            if (zaladowaneGrafiki % 50 == 0) {
                                                System.out.println("Task: Załadowano " + zaladowaneGrafiki + "/" + iloscGrafikDoZaladowania + " grafik...");
                                            }
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

        // Usunięto bindowanie ProgressIndicator, ponieważ ProgressIndicator został usunięty z FXML
        // (lub jest tylko prostym kręciołkiem bez dynamicznego postępu)

        zadanieLadowania.setOnSucceeded(event -> {
            System.out.println("Wczytywanie zasobów (Task) zakończone pomyślnie.");
            PauseTransition delay = new PauseTransition(Duration.seconds(0.5));
            delay.setOnFinished(e -> {
                if (pixelGwintAplikacja != null) {
                    pixelGwintAplikacja.pokazEkranLogowania();
                }
            });
            delay.play();
        });

        zadanieLadowania.setOnFailed(event -> {
            // ... (obsługa błędu, jak poprzednio, dostosowana do braku szczegółowego statusLabel/progressIndicator) ...
            Throwable throwable = zadanieLadowania.getException();
            System.err.println("Wczytywanie zasobów (Task) nie powiodło się: " + (throwable != null ? throwable.getMessage() : "Brak informacji"));
            if(statusLadowaniaLabel != null) statusLadowaniaLabel.setText("Błąd wczytywania!");

            PauseTransition delay = new PauseTransition(Duration.seconds(2));
            delay.setOnFinished(e -> {
                if (pixelGwintAplikacja != null) {
                    pixelGwintAplikacja.pokazEkranLogowania();
                }
            });
            delay.play();
        });

        new Thread(zadanieLadowania).start();
    }
}