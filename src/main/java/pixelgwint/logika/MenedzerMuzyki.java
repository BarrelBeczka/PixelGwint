package pixelgwint.logika;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MenedzerMuzyki {

    private MediaPlayer aktualnyOdtwarzacz;
    private Timeline animacjaSciszania;
    private double aktualnaGlosnosc = 0.3;

    private final List<String> playlistaMenu;
    private final List<String> playlistaGry;
    private List<String> aktualnaPlaylista;
    private int aktualnyIndeks = 0;
    private final Map<String, Media> mediaCache = new HashMap<>();

    private static final double CZAS_PRZEJSCIA = 4.0;

    public MenedzerMuzyki() {
        playlistaMenu = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> "/muzyka/soundtrack" + i + ".mp3")
                .collect(Collectors.toList());
        playlistaGry = new ArrayList<>(playlistaMenu);
    }

    public void wstepneLadowanie() {
        // ... (ta metoda pozostaje bez zmian)
        System.out.println("[MenedzerMuzyki] Rozpoczynanie wstępnego ładowania muzyki...");
        List<String> wszystkieUtwory = new ArrayList<>();
        wszystkieUtwory.add("/muzyka/intro.mp3");
        wszystkieUtwory.addAll(playlistaMenu);
        wszystkieUtwory.addAll(playlistaGry);

        for (String sciezka : wszystkieUtwory.stream().distinct().collect(Collectors.toList())) {
            try {
                if (!mediaCache.containsKey(sciezka)) {
                    URL resource = getClass().getResource(sciezka);
                    if (resource != null) {
                        mediaCache.put(sciezka, new Media(resource.toExternalForm()));
                    } else {
                        System.err.println("BŁĄD (wstępne ładowanie): Nie można znaleźć pliku muzycznego: " + sciezka);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("[MenedzerMuzyki] Zakończono wstępne ładowanie. Wczytano " + mediaCache.size() + " unikalnych utworów.");
    }

    public void odtwarzajIntro() {
        System.out.println("[MenedzerMuzyki] Odtwarzanie utworu intro.");
        // <<< ZMIANA: Wywołujemy metodę z flagą zFadeIn = false >>>
        uruchomNowyUtwor("/muzyka/intro.mp3", () -> uruchomPlaylistMenu(true), false);
    }

    public void uruchomPlaylistMenu(boolean czyPrzetasowac) {
        System.out.println("[MenedzerMuzyki] Uruchamianie playlisty MENU.");
        this.aktualnaPlaylista = playlistaMenu;
        if (czyPrzetasowac) {
            Collections.shuffle(this.aktualnaPlaylista);
        }
        this.aktualnyIndeks = 0;
        rozpocznijPrzejscieDoNastepnegoUtworu();
    }

    public void uruchomPlaylistGry(boolean czyPrzetasowac) {
        System.out.println("[MenedzerMuzyki] Uruchamianie playlisty GRY.");
        this.aktualnaPlaylista = playlistaGry;
        if (czyPrzetasowac) {
            Collections.shuffle(this.aktualnaPlaylista);
        }
        this.aktualnyIndeks = 0;
        rozpocznijPrzejscieDoNastepnegoUtworu();
    }

    private void rozpocznijPrzejscieDoNastepnegoUtworu() {
        if (aktualnyOdtwarzacz != null) {
            if (animacjaSciszania != null) {
                animacjaSciszania.stop();
            }
            final MediaPlayer staryOdtwarzacz = aktualnyOdtwarzacz;
            animacjaSciszania = new Timeline(
                    new KeyFrame(Duration.seconds(CZAS_PRZEJSCIA),
                            new KeyValue(staryOdtwarzacz.volumeProperty(), 0.0))
            );
            animacjaSciszania.setOnFinished(event -> staryOdtwarzacz.stop());
            animacjaSciszania.play();
        }

        if (aktualnaPlaylista == null || aktualnaPlaylista.isEmpty()) return;

        if (aktualnyIndeks >= aktualnaPlaylista.size()) {
            Collections.shuffle(aktualnaPlaylista);
            aktualnyIndeks = 0;
        }
        String sciezkaNastepnegoUtworu = aktualnaPlaylista.get(aktualnyIndeks);
        aktualnyIndeks++;

        // <<< ZMIANA: Przejścia między utworami na playliście ZAWSZE mają fade-in >>>
        uruchomNowyUtwor(sciezkaNastepnegoUtworu, null, true);
    }

    // <<< ZMIANA: Dodano parametr 'zFadeIn' >>>
    private void uruchomNowyUtwor(String sciezka, Runnable akcjaPoZakonczeniu, boolean zFadeIn) {
        Media media = mediaCache.get(sciezka);
        if (media == null) {
            System.err.println("BŁĄD: Utwór '" + sciezka + "' nie jest w pamięci podręcznej. Pomijanie.");
            Platform.runLater(this::rozpocznijPrzejscieDoNastepnegoUtworu);
            return;
        }

        MediaPlayer nowyOdtwarzacz = new MediaPlayer(media);
        this.aktualnyOdtwarzacz = nowyOdtwarzacz;

        // <<< ZMIANA: Logika warunkowego fade-in >>>
        if (zFadeIn) {
            // Startuje cicho i jest podgłaśniany
            nowyOdtwarzacz.setVolume(0);
            Timeline animacjaPodglasniania = new Timeline(
                    new KeyFrame(Duration.seconds(CZAS_PRZEJSCIA),
                            new KeyValue(nowyOdtwarzacz.volumeProperty(), aktualnaGlosnosc))
            );
            animacjaPodglasniania.play();
        } else {
            // Startuje od razu z docelową głośnością
            nowyOdtwarzacz.setVolume(aktualnaGlosnosc);
        }

        nowyOdtwarzacz.setOnReady(() -> {
            Duration calkowityCzas = media.getDuration();
            Duration czasStartuPrzejscia = calkowityCzas.subtract(Duration.seconds(CZAS_PRZEJSCIA));

            Runnable akcjaDoWykonania = (akcjaPoZakonczeniu != null) ? akcjaPoZakonczeniu : this::rozpocznijPrzejscieDoNastepnegoUtworu;

            if (czasStartuPrzejscia.lessThanOrEqualTo(Duration.ZERO)) {
                nowyOdtwarzacz.setOnEndOfMedia(() -> Platform.runLater(akcjaDoWykonania));
            } else {
                PauseTransition timerDoPrzejscia = new PauseTransition(czasStartuPrzejscia);
                timerDoPrzejscia.setOnFinished(event -> Platform.runLater(akcjaDoWykonania));
                timerDoPrzejscia.play();
            }
        });

        nowyOdtwarzacz.play();
        System.out.println("[MenedzerMuzyki] Odtwarzanie" + (zFadeIn ? " (z fade-in): " : ": ") + sciezka);
    }

    public void zatrzymajOdtwarzanie() {
        if (animacjaSciszania != null) {
            animacjaSciszania.stop();
        }
        if (aktualnyOdtwarzacz != null) {
            aktualnyOdtwarzacz.setOnEndOfMedia(null);
            aktualnyOdtwarzacz.stop();
            aktualnyOdtwarzacz = null;
        }
    }

    public void ustawGlosnosc(double poziom) {
        if (poziom < 0.0) poziom = 0.0;
        if (poziom > 1.0) poziom = 1.0;
        this.aktualnaGlosnosc = poziom;
        if (aktualnyOdtwarzacz != null && (animacjaSciszania == null || animacjaSciszania.getStatus() != Timeline.Status.RUNNING)) {
            aktualnyOdtwarzacz.setVolume(this.aktualnaGlosnosc);
        }
    }

    public double getAktualnaGlosnosc() {
        return this.aktualnaGlosnosc;
    }
}