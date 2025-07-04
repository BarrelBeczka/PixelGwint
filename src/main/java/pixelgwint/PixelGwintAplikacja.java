package pixelgwint;
import pixelgwint.widok.KontrolerWpiszNazweGracza2;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pixelgwint.logika.WczytywaczKart;
import pixelgwint.model.FrakcjaEnum;
import pixelgwint.model.TaliaUzytkownika;
import pixelgwint.model.Uzytkownik;
import pixelgwint.widok.*;
import pixelgwint.model.Gracz;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javafx.scene.image.Image;
import pixelgwint.model.Karta;
import java.util.List;
import java.util.Map;
import pixelgwint.logika.MenedzerMuzyki;
import pixelgwint.widok.KontrolerUstawien;
import pixelgwint.widok.KontrolerEkranuIntro;
import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class PixelGwintAplikacja extends Application {
    private Stage primaryStage;
    private StackPane mainLayout; // Główny kontener dla widoków
    private MenedzerMuzyki menedzerMuzyki;
    private List<Karta> wszystkieKartyWGrzeGlobalnie = null;
    private Map<String, Image> globalImageCache = null;
    private Uzytkownik profilGracza1 = null;

    public void setProfilGracza1(Uzytkownik profil) {
        this.profilGracza1 = profil;
    }

    public Uzytkownik getProfilGracza1() {
        return this.profilGracza1;
    }

    private TaliaUzytkownika wybranaTaliaGracza1 = null;
    private boolean czyGracz2WybieraTaliePredefiniowanaGlobalnie;

    public boolean getCzyGracz2WybieraTaliePredefiniowana() {
        return this.czyGracz2WybieraTaliePredefiniowanaGlobalnie;
    }

    @Override
    public void start(Stage glownaScena) throws IOException {
        this.primaryStage = glownaScena;
        this.primaryStage.setTitle("PixelGwint");
        this.menedzerMuzyki = new MenedzerMuzyki();
        mainLayout = new StackPane();

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);

        try {
            InputStream iconStream = getClass().getResourceAsStream("/grafiki/logo.png");
            if (iconStream != null) {
                primaryStage.getIcons().add(new Image(iconStream));
                System.out.println("[APP] Pomyślnie ustawiono ikonę aplikacji z /grafiki/logo.png");
                iconStream.close();
            } else {
                System.err.println("[APP BŁĄD] Nie można znaleźć pliku ikony: /grafiki/logo.png. Upewnij się, że plik istnieje w src/main/resources/grafiki/");
            }
        } catch (Exception e) {
            System.err.println("[APP BŁĄD] Wystąpił błąd podczas ładowania ikony aplikacji: " + e.getMessage());
            e.printStackTrace();
        }

        primaryStage.setFullScreenExitHint("");

        primaryStage.maximizedProperty().addListener((obs, wasMaximized, isNowMaximized) -> {
            if (isNowMaximized) {
                System.out.println("[APP] Okno zostało zmaksymalizowane, wymuszanie trybu pełnoekranowego.");
                primaryStage.setFullScreen(true);
            }
        });
        primaryStage.iconifiedProperty().addListener((obs, wasIconified, isNowIconified) -> {
            if (!isNowIconified) {
                System.out.println("[APP] Okno zostało przywrócone z minimalizacji.");
                if (!primaryStage.isFullScreen()) {
                    System.out.println("[APP] Przywrócone okno nie jest pełnoekranowe, ustawianie trybu pełnoekranowego.");
                    primaryStage.setFullScreen(true);
                }
            }
        });


        pokazEkranLadowania();
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

    private void setView(Node viewNode, String title) {
        mainLayout.getChildren().clear();
        mainLayout.getChildren().add(viewNode);
        primaryStage.setTitle("PixelGwint - " + title);
    }
    private void setViewWithFadeIn(Node viewNode, String title, Duration transitionDuration) {
        viewNode.setOpacity(0);
        mainLayout.getChildren().add(viewNode);

        FadeTransition fadeIn = new FadeTransition(transitionDuration, viewNode);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeIn.setOnFinished(event -> {
            if (mainLayout.getChildren().size() > 1) {
                mainLayout.getChildren().remove(0);
            }
        });

        fadeIn.play();
        primaryStage.setTitle("PixelGwint - " + title);
    }
    public void rozpocznijPrzejscieDoLogowania() {
        System.out.println("[DIAGNOSTYKA APLIKACJI] Metoda 'rozpocznijPrzejscieDoLogowania' ZOSTAŁA WYWOŁANA.");

        try {
            FXMLLoader loader = new FXMLLoader(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-logowania.fxml"));
            VBox viewContent = loader.load();
            KontrolerEkranuLogowania controller = loader.getController();
            controller.setApp(this);

            Duration czasTrwania = Duration.seconds(2.12);
            setViewWithFadeIn(viewContent, "Logowanie", czasTrwania);

        } catch (IOException e) {
            e.printStackTrace();
            pokazEkranLogowania();
        }
    }
    public void pokazEkranLadowania() {
        try {
            FXMLLoader loader = new FXMLLoader(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-ladowania.fxml"));
            AnchorPane rootLayout = loader.load();
            KontrolerEkranuLadowania controller = loader.getController();
            controller.setApp(this);
            mainLayout.getChildren().clear();
            mainLayout.getChildren().add(rootLayout);
            primaryStage.setTitle("PixelGwint - Wczytywanie...");
        } catch (IOException e) {
            e.printStackTrace();
            Platform.runLater(this::pokazEkranLogowania);
        }
    }

    public void pokazEkranLogowania() {
        try {
            FXMLLoader loader = new FXMLLoader(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-logowania.fxml"));
            VBox viewContent = loader.load();
            KontrolerEkranuLogowania controller = loader.getController();
            controller.setApp(this);
            setView(viewContent, "Logowanie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pokazEkranRejestracji() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-rejestracji.fxml"));
            VBox viewContent = loader.load();

            KontrolerEkranuRejestracji controller = loader.getController();
            controller.setApp(this);

            setView(viewContent, "Rejestracja");
        } catch (IOException e) {
            System.err.println("Nie udało się załadować ekranu rejestracji:");
            e.printStackTrace();
        }
    }

    public void pokazMenuGlowne(Uzytkownik uzytkownik) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-menu-glownego.fxml"));
            BorderPane viewContent = loader.load();

            KontrolerMenuGlownego controller = loader.getController();
            controller.setApp(this);
            controller.inicjalizujDane(uzytkownik);

            setView(viewContent, "Menu Główne");
        } catch (IOException e) {
            System.err.println("Nie udało się załadować menu głównego:");
            e.printStackTrace();
        }
    }

    public void pokazEkranWpisuNazwyGracza2(boolean czyTrybPredefiniowanyDlaP2) {
        this.czyGracz2WybieraTaliePredefiniowanaGlobalnie = czyTrybPredefiniowanyDlaP2;
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-wpisz-nazwe-gracza2.fxml"));
            VBox viewContent = loader.load();

            KontrolerWpiszNazweGracza2 controller = loader.getController();
            controller.setApp(this);

            setView(viewContent, "Gracz 2: Wpisz Nazwę");
        } catch (IOException e) {
            System.err.println("Nie udało się załadować ekranu wpisu nazwy Gracza 2:");
            e.printStackTrace();
        }
    }

    public void pokazEkranWyboruFrakcji(Uzytkownik graczProfil, int numerGracza, boolean czyTrybPredefiniowany) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-wyboru-frakcji.fxml"));
            VBox viewContent = loader.load();

            KontrolerWyboruFrakcji controller = loader.getController();
            controller.setApp(this);
            controller.inicjalizujDaneDlaGry(graczProfil, numerGracza, czyTrybPredefiniowany);

            setView(viewContent, "Wybierz Frakcję dla Gracza " + numerGracza);
        } catch (IOException e) {
            System.err.println("Nie udało się załadować ekranu wyboru frakcji:");
            e.printStackTrace();
        }
    }

    public void pokazEkranZarzadzaniaTaliami(Uzytkownik uzytkownik) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-zarzadzania-taliami.fxml"));
            VBox viewContent = loader.load();

            KontrolerZarzadzaniaTaliami controller = loader.getController();
            controller.setApp(this);
            controller.inicjalizujDane(uzytkownik);

            setView(viewContent, "Zarządzanie Taliami");
        } catch (IOException e) {
            System.err.println("Nie udało się załadować ekranu zarządzania taliami:");
            e.printStackTrace();
        }
    }

    public void pokazEkranWyboruFrakcjiDlaTalii(Uzytkownik uzytkownik) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-wyboru-frakcji-dla-talii.fxml"));
            VBox viewContent = loader.load();

            KontrolerWyboruFrakcjiDlaTalii controller = loader.getController();
            controller.setApp(this);
            controller.inicjalizujDane(uzytkownik);

            setView(viewContent, "Wybierz Frakcję dla Nowej Talii");
        } catch (IOException e) {
            System.err.println("Nie udało się załadować ekranu wyboru frakcji dla talii:");
            e.printStackTrace();
        }
    }

    public void pokazKreatorTalii(Uzytkownik uzytkownik, FrakcjaEnum wybranaFrakcja) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-kreatora-talii.fxml"));
            BorderPane viewContent = loader.load();

            KontrolerKreatoraTalii controller = loader.getController();
            controller.setApp(this);
            controller.inicjalizujKreator(uzytkownik, wybranaFrakcja);

            setView(viewContent, "Kreator Talii: " + wybranaFrakcja.getNazwaWyswietlana());
        } catch (IOException e) {
            System.err.println("Nie udało się załadować kreatora talii:");
            e.printStackTrace();
        }
    }

    public void pokazEkranWyboruTrybuTalii(Uzytkownik uzytkownik) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-wyboru-trybu-talii.fxml"));
            VBox viewContent = loader.load();

            KontrolerWyboruTrybuTalii controller = loader.getController();
            controller.setApp(this);
            controller.inicjalizujDane(uzytkownik);

            setView(viewContent, "Wybierz Tryb Talii");
        } catch (IOException e) {
            System.err.println("Nie udało się załadować ekranu wyboru trybu talii:");
            e.printStackTrace();
        }
    }

    public void pokazEkranWyboruZapisanejTalii(Uzytkownik uzytkownik, int numerGracza) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-wyboru-zapisanej-talii.fxml"));
            VBox viewContent = loader.load();

            KontrolerWyboruZapisanejTalii controller = loader.getController();
            controller.setApp(this);
            controller.inicjalizujEkran(uzytkownik, numerGracza);

            setView(viewContent, "Wybierz Zapisaną Talię dla Gracza " + numerGracza);
        } catch (IOException e) {
            System.err.println("Nie udało się załadować ekranu wyboru zapisanej talii:");
            e.printStackTrace();
        }
    }

    public void pokazPlanszeGry(Gracz gracz1, Gracz gracz2) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-planszy-gry.fxml"));
            AnchorPane viewContent = loader.load();

            KontrolerPlanszyGry controller = loader.getController();
            controller.setApp(this);
            controller.inicjalizujGre(gracz1, gracz2, null);

            setView(viewContent, "Rozgrywka");
        } catch (IOException e) {
            System.err.println("Nie udało się załadować planszy gry:");
            e.printStackTrace();
        }
    }

    public void setWybranaTaliaGracza1(TaliaUzytkownika talia) {
        this.wybranaTaliaGracza1 = talia;
    }
    public TaliaUzytkownika getWybranaTaliaGracza1() {
        return wybranaTaliaGracza1;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public boolean isTrybHotSeat() {
        return true;
    }
    public void setWszystkieKartyWGrze(List<Karta> karty) {
        this.wszystkieKartyWGrzeGlobalnie = karty;
    }

    public List<Karta> getWszystkieKartyWGrze() {
        if (this.wszystkieKartyWGrzeGlobalnie == null) {
            System.out.println("OSTRZEŻENIE APLIKACJI: Globalna lista kart nie została zainicjowana. Wczytywanie na żądanie...");
            this.wszystkieKartyWGrzeGlobalnie = WczytywaczKart.wczytajWszystkieKarty();
        }
        return this.wszystkieKartyWGrzeGlobalnie;
    }

    public void setImageCache(Map<String, Image> cache) {
        this.globalImageCache = cache;
    }

    public Image zapewnijObrazekKarty(String nazwaPlikuGrafikiKarty) {
        if (nazwaPlikuGrafikiKarty == null || nazwaPlikuGrafikiKarty.isEmpty()) {
            return null;
        }
        String pelnaSciezkaDoCache = "/grafiki/karty/" + nazwaPlikuGrafikiKarty;

        if (globalImageCache != null) {
            Image obrazekZCache = globalImageCache.get(pelnaSciezkaDoCache);
            if (obrazekZCache != null) {
                return obrazekZCache;
            }
            System.out.println("INFO APLIKACJI: Obrazek '" + pelnaSciezkaDoCache + "' nie znaleziony w cache, próba załadowania.");
            try (InputStream stream = getClass().getResourceAsStream(pelnaSciezkaDoCache)) {
                if (stream != null) {
                    Image img = new Image(stream);
                    globalImageCache.put(pelnaSciezkaDoCache, img);
                    return img;
                } else {
                    System.err.println("BŁĄD APLIKACJI: Nie można załadować obrazka (zapewnijObrazekKarty): " + pelnaSciezkaDoCache);
                }
            } catch (Exception e) {
                System.err.println("BŁĄD APLIKACJI: Wyjątek podczas ładowania obrazka (zapewnijObrazekKarty) " + pelnaSciezkaDoCache + ": " + e.getMessage());
            }
            return null;
        } else {
            System.out.println("OSTRZEŻENIE APLIKACJI: Globalny imageCache jest null. Ładowanie obrazka na żądanie: " + pelnaSciezkaDoCache);
            try (InputStream stream = getClass().getResourceAsStream(pelnaSciezkaDoCache)) {
                if (stream != null) {
                    return new Image(stream);
                }
            } catch (Exception e) { /* ignoruj lub loguj */ }
        }
        System.err.println("BŁĄD KRYTYCZNY APLIKACJI: Nie można zapewnić obrazka: " + pelnaSciezkaDoCache);
        return null;
    }
    public MenedzerMuzyki getMenedzerMuzyki() {
        return menedzerMuzyki;
    }
    public void pokazEkranUstawien(Uzytkownik uzytkownik) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-ustawien.fxml"));
            VBox viewContent = loader.load();

            KontrolerUstawien controller = loader.getController();
            controller.setApp(this);
            controller.inicjalizuj(uzytkownik);

            setView(viewContent, "Ustawienia");
        } catch (IOException e) {
            System.err.println("Nie udało się załadować ekranu ustawień:");
            e.printStackTrace();
        }
    }
    public void pokazEkranIntro() {
        try {
            FXMLLoader loader = new FXMLLoader(PixelGwintAplikacja.class.getResource("/pixelgwint/widok/ekran-intro.fxml"));
            StackPane viewContent = loader.load();
            KontrolerEkranuIntro controller = loader.getController();
            controller.setApp(this);
            controller.uruchomIntro();
            setView(viewContent, "PixelGwint");
        } catch (IOException e) {
            System.err.println("Nie udało się załadować ekranu intro, przechodzenie awaryjnie do logowania.");
            e.printStackTrace();
            pokazEkranLogowania();
        }
    }
}