package pixelgwint.widok;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox; // Upewnij się, że ten import jest
import javafx.scene.paint.Color;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.logika.ZarzadzanieUzytkownikami;

import java.io.InputStream;
import java.net.URL; // Potrzebny dla getResource()
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KontrolerEkranuRejestracji {

    @FXML
    private VBox rootRejestracjaPane; // Dodane pole dla głównego kontenera VBox

    @FXML
    private TextField poleNazwaUzytkownikaRejestracja;
    @FXML
    private PasswordField poleHasloRejestracja;
    @FXML
    private ImageView obrazekIkonyRejestracja;
    @FXML
    private Button przyciskZmienIkone;
    @FXML
    private Button przyciskPotwierdzRejestracje;
    @FXML
    private Button przyciskAnulujRejestracje;
    @FXML
    private Label etykietaStatusuRejestracji;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private ZarzadzanieUzytkownikami zarzadzanieUzytkownikami;
    private String aktualnieWybranaIkonaNazwaPliku;
    private final Random random = new Random();
    private static final int LICZBA_IKON = 25;
    private final List<String> dostepneIkony = new ArrayList<>(); // Inicjalizacja tutaj lub w konstruktorze

    public KontrolerEkranuRejestracji() { // Konstruktor do inicjalizacji listy ikon
        for (int i = 1; i <= LICZBA_IKON; i++) {
            dostepneIkony.add("ico" + i + ".png");
        }
    }

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    public void initialize() {
        ustawTloProgramistycznie(); // Ustawienie tła
        zarzadzanieUzytkownikami = new ZarzadzanieUzytkownikami();
        if (etykietaStatusuRejestracji != null) { // Dodatkowe sprawdzenie null
            etykietaStatusuRejestracji.setText("");
        }
        losujIWyswietlIkone();
    }

    private void ustawTloProgramistycznie() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png"; // Użyj tej samej ścieżki co w innych kontrolerach

        if (rootRejestracjaPane == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerRejestracji): rootRejestracjaPane nie zostało wstrzyknięte!");
            return;
        }

        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);

        if (imageUrl == null) {
            System.err.println("--------------------------------------------------------------------------");
            System.err.println("BŁĄD KRYTYCZNY (KontrolerRejestracji): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
            System.err.println("Upewnij się, że plik istnieje w: src/main/resources" + backgroundImageClasspathPath);
            // Można ustawić awaryjny styl, jeśli FXML go nie ma
            // rootRejestracjaPane.setStyle("-fx-background-color: #2c2c2c;"); // Awaryjny kolor
            System.err.println("--------------------------------------------------------------------------");
        } else {
            System.out.println("Diagnostyka (KontrolerRejestracji): Obrazek tła '" + backgroundImageClasspathPath + "' znaleziony. URL: " + imageUrl.toExternalForm());
            String cssExternalUrl = imageUrl.toExternalForm();
            String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: no-repeat;";
            rootRejestracjaPane.setStyle(newStyle);
            System.out.println("INFO (KontrolerRejestracji): Tło obrazkowe Ekranu Rejestracji powinno być ustawione programistycznie.");
        }
    }

    private void losujIWyswietlIkone() {
        if (dostepneIkony.isEmpty()) { // Zabezpieczenie, jeśli lista ikon jest pusta
            System.err.println("Brak dostępnych ikon do wylosowania.");
            if (obrazekIkonyRejestracja != null) obrazekIkonyRejestracja.setImage(null);
            aktualnieWybranaIkonaNazwaPliku = null;
            return;
        }
        int numerIkony = random.nextInt(dostepneIkony.size()); // Losuje indeks z listy
        aktualnieWybranaIkonaNazwaPliku = dostepneIkony.get(numerIkony);

        try {
            InputStream stream = getClass().getResourceAsStream("/grafiki/ikony_profilu/" + aktualnieWybranaIkonaNazwaPliku);
            if (stream != null && obrazekIkonyRejestracja != null) {
                obrazekIkonyRejestracja.setImage(new Image(stream));
                stream.close(); // Ważne: zamknij strumień
            } else {
                if (obrazekIkonyRejestracja != null) obrazekIkonyRejestracja.setImage(null); // Wyczyść, jeśli błąd
                System.err.println("Nie można załadować ikony: /grafiki/ikony_profilu/" + aktualnieWybranaIkonaNazwaPliku);
                if (etykietaStatusuRejestracji != null) etykietaStatusuRejestracji.setText("Błąd ładowania ikony: " + aktualnieWybranaIkonaNazwaPliku);
            }
        } catch (Exception e) {
            if (obrazekIkonyRejestracja != null) obrazekIkonyRejestracja.setImage(null);
            System.err.println("Wyjątek podczas ładowania ikony " + aktualnieWybranaIkonaNazwaPliku + ": " + e.getMessage());
            if (etykietaStatusuRejestracji != null) etykietaStatusuRejestracji.setText("Błąd ładowania obrazka.");
        }
    }

    @FXML
    private void handleZmienIkone() {
        losujIWyswietlIkone();
        if (etykietaStatusuRejestracji != null) etykietaStatusuRejestracji.setText("");
    }

    @FXML
    private void handlePotwierdzRejestracje() {
        String nazwaUzytkownika = "";
        if (poleNazwaUzytkownikaRejestracja != null) {
            nazwaUzytkownika = poleNazwaUzytkownikaRejestracja.getText().trim();
        }

        String haslo = "";
        if (poleHasloRejestracja != null) {
            haslo = poleHasloRejestracja.getText();
        }


        if (nazwaUzytkownika.isEmpty() || haslo.isEmpty()) {
            pokazStatus("Nazwa użytkownika i hasło nie mogą być puste.", Color.RED);
            return;
        }
        if (aktualnieWybranaIkonaNazwaPliku == null || aktualnieWybranaIkonaNazwaPliku.isEmpty()) {
            pokazStatus("Proszę wybrać ikonę (spróbuj kliknąć 'Zmień Ikonę').", Color.RED);
            return;
        }

        boolean czyZarejestrowano = zarzadzanieUzytkownikami.rejestrujUzytkownika(nazwaUzytkownika, haslo, aktualnieWybranaIkonaNazwaPliku);

        if (czyZarejestrowano) {
            pokazStatus("Rejestracja zakończona pomyślnie dla: " + nazwaUzytkownika + ". Możesz się teraz zalogować.", Color.GREEN);
            if (przyciskPotwierdzRejestracje != null) przyciskPotwierdzRejestracje.setDisable(true);
        } else {
            pokazStatus("Rejestracja nie powiodła się. Użytkownik o tej nazwie może już istnieć.", Color.RED);
        }
    }

    @FXML
    private void handleAnulujRejestracje() {
        if (pixelGwintAplikacja != null) {
            pixelGwintAplikacja.pokazEkranLogowania();
        }
    }

    private void pokazStatus(String wiadomosc, Color kolor) {
        if (etykietaStatusuRejestracji != null) {
            etykietaStatusuRejestracji.setText(wiadomosc);
            etykietaStatusuRejestracji.setTextFill(kolor);
        }
    }
}