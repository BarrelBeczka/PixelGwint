package pixelgwint.widok;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.model.Uzytkownik;

import java.net.URL;
import java.util.ArrayList;
import java.util.List; // Import dla List
import java.util.Random;

public class KontrolerWpiszNazweGracza2 {

    @FXML private VBox rootPaneWpisuNazwy;
    @FXML private TextField poleNazwaGracza2;
    @FXML private Button przyciskDalejPoWpisaniuNazwy;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private final DropShadow goldGlowEffect = new DropShadow(20, Color.rgb(255, 215, 0, 0.7));
    private final Random random = new Random();

    // Dynamicznie generowana lista dostępnych ikon
    private final List<String> dostepneIkony = new ArrayList<>();
    private static final int LICZBA_IKON_PROFILU = 25; // Od ico1.png do ico25.png
    private static final String PRZEDROSTEK_IKONY = "ico";
    private static final String ROZSZERZENIE_IKONY = ".png";
    // Upewnij się, że ścieżka do folderu jest poprawna w kontekście zasobów,
    // sama nazwa pliku jest generowana, ale zasób musi istnieć pod tą nazwą.
    // Folder w resources: /grafiki/ikony_profilu/

    public KontrolerWpiszNazweGracza2() {
        // Generowanie nazw plików ikon
        for (int i = 1; i <= LICZBA_IKON_PROFILU; i++) {
            dostepneIkony.add(PRZEDROSTEK_IKONY + i + ROZSZERZENIE_IKONY);
        }
        // Możesz dodać tu "default_icon.png" jeśli nadal chcesz jej używać jako fallback
        // lub jeśli nie wszystkie od 1 do 25 istnieją.
        // np. dostepneIkony.add("default_icon.png");
    }


    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    public void initialize() {
        ustawTloProgramistycznie();

        if (przyciskDalejPoWpisaniuNazwy != null) {
            przyciskDalejPoWpisaniuNazwy.setOnMouseEntered(event -> przyciskDalejPoWpisaniuNazwy.setEffect(goldGlowEffect));
            przyciskDalejPoWpisaniuNazwy.setOnMouseExited(event -> przyciskDalejPoWpisaniuNazwy.setEffect(null));
        }
    }

    private void ustawTloProgramistycznie() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png";
        if (rootPaneWpisuNazwy == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerWpiszNazweGracza2): rootPaneWpisuNazwy nie zostało wstrzyknięte!");
            return;
        }
        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);
        if (imageUrl == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerWpiszNazweGracza2): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
        } else {
            String cssExternalUrl = imageUrl.toExternalForm();
            String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: no-repeat;";
            rootPaneWpisuNazwy.setStyle(newStyle);
            System.out.println("INFO (KontrolerWpiszNazweGracza2): Tło obrazkowe ustawione programistycznie.");
        }
    }

    @FXML
    private void handleDalejPoWpisaniuNazwyAction() {
        String nazwaP2 = poleNazwaGracza2.getText().trim();
        if (nazwaP2.isEmpty()) {
            nazwaP2 = "Gracz 2";
        }

        String wylosowanaIkonaP2 = "default_icon.png"; // Domyślna, jeśli lista ikon jest pusta
        if (!dostepneIkony.isEmpty()) {
            String ikonaGracza1 = null;
            if (pixelGwintAplikacja.getProfilGracza1() != null) {
                ikonaGracza1 = pixelGwintAplikacja.getProfilGracza1().getNazwaPlikuIkonyProfilu();
            }

            int randomIndex;
            String potencjalnaIkona;
            int proby = 0; // Zabezpieczenie przed nieskończoną pętlą, jeśli wszystkie ikony są takie same jak P1

            // Losuj, próbując uniknąć ikony gracza 1, jeśli to możliwe
            do {
                randomIndex = random.nextInt(dostepneIkony.size());
                potencjalnaIkona = dostepneIkony.get(randomIndex);
                proby++;
            } while (potencjalnaIkona.equals(ikonaGracza1) && dostepneIkony.size() > 1 && proby < dostepneIkony.size() * 2);
            // Jeśli po kilku próbach nie udało się znaleźć innej, użyj ostatnio wylosowanej
            wylosowanaIkonaP2 = potencjalnaIkona;
        }

        System.out.println("Gracz 2 (" + nazwaP2 + ") otrzymał wylosowaną ikonę: " + wylosowanaIkonaP2);
        Uzytkownik profilGracza2 = new Uzytkownik(nazwaP2, "---", wylosowanaIkonaP2);

        boolean czyTrybPredefiniowanyDlaP2 = pixelGwintAplikacja.getCzyGracz2WybieraTaliePredefiniowana();

        if (czyTrybPredefiniowanyDlaP2) {
            pixelGwintAplikacja.pokazEkranWyboruFrakcji(profilGracza2, 2, true);
        } else {
            pixelGwintAplikacja.pokazEkranWyboruZapisanejTalii(profilGracza2, 2);
        }
    }
}