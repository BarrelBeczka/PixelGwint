package pixelgwint.widok;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.model.*;
import pixelgwint.logika.WczytywaczKart;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KontrolerWyboruFrakcjiDlaTalii {

    @FXML private VBox rootWyboruFrakcjiPane;
    @FXML private Label etykietaGracza;
    @FXML private ImageView obrazekKrolestwa;
    @FXML private ImageView obrazekNilfgaard;
    @FXML private ImageView obrazekPotwory;
    @FXML private ImageView obrazekScoiatael;
    @FXML private Label etykietaOpisFrakcji;
    @FXML private Button przyciskDalej;
    @FXML private Button przyciskPowrotDoMenu;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private Uzytkownik gracz1;
    private List<Karta> wszystkieKartyWGrze;
    private FrakcjaEnum aktualnieWybranaFrakcja;
    private ImageView aktualnieWybranyObrazek = null;

    private final DropShadow efektPodswietleniaWyboru = new DropShadow(20, Color.rgb(255, 215, 0, 0.9));
    private final DropShadow efektNajechania = new DropShadow(15, Color.rgb(173, 216, 230, 0.7));
    private final DropShadow goldGlowEffect = new DropShadow(20, Color.rgb(255, 215, 0, 0.7));

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    public void initialize() {
        ustawTloProgramistycznie();

        Button[] przyciski = {przyciskDalej, przyciskPowrotDoMenu};
        for (Button przycisk : przyciski) {
            if (przycisk != null) {
                String baseStyle = przycisk.getStyle() != null ? przycisk.getStyle() : "";
                przycisk.setOnMouseEntered(event -> przycisk.setEffect(goldGlowEffect));
                przycisk.setOnMouseExited(event -> przycisk.setEffect(null));
            }
        }
    }

    private void ustawTloProgramistycznie() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png";
        if (rootWyboruFrakcjiPane == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerWyboruFrakcjiDlaTalii): rootWyboruFrakcjiPane nie zostało wstrzyknięte!");
            return;
        }
        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);
        if (imageUrl == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerWyboruFrakcjiDlaTalii): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
        } else {
            String cssExternalUrl = imageUrl.toExternalForm();
            String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: no-repeat;";
            rootWyboruFrakcjiPane.setStyle(newStyle);
            System.out.println("INFO (KontrolerWyboruFrakcjiDlaTalii): Tło obrazkowe ustawione programistycznie.");
        }
    }

    public void inicjalizujDane(Uzytkownik gracz1) {
        this.gracz1 = gracz1;
        if (etykietaGracza != null && gracz1 != null && gracz1.getNazwaUzytkownika() != null) {
            etykietaGracza.setText(gracz1.getNazwaUzytkownika() + " wybiera frakcję dla nowej talii:");
        } else if (etykietaGracza != null) {
            etykietaGracza.setText("Wybierz frakcję dla nowej talii:");
        }

        aktualnieWybranaFrakcja = null;
        if (aktualnieWybranyObrazek != null) {
            aktualnieWybranyObrazek.setEffect(null);
        }
        aktualnieWybranyObrazek = null;

        if (etykietaOpisFrakcji != null) {
            etykietaOpisFrakcji.setText("Najedź na ikonę frakcji, aby zobaczyć jej zdolność. Kliknij, aby wybrać.");
        }
        if (przyciskDalej != null) {
            przyciskDalej.setDisable(true);
        }

        if (this.pixelGwintAplikacja != null && this.pixelGwintAplikacja.getWszystkieKartyWGrze() != null) {
            this.wszystkieKartyWGrze = this.pixelGwintAplikacja.getWszystkieKartyWGrze();
        } else {
            this.wszystkieKartyWGrze = WczytywaczKart.wczytajWszystkieKarty(); // Fallback
        }

        if (this.wszystkieKartyWGrze == null || this.wszystkieKartyWGrze.isEmpty()) {
            System.err.println("Nie wczytano żadnych kart do KontroleraWyboruFrakcjiDlaTalii!");
            if (etykietaOpisFrakcji != null) {
                etykietaOpisFrakcji.setText("Błąd krytyczny: Nie udało się wczytać kart!");
                etykietaOpisFrakcji.setTextFill(Color.RED);
            }
            return;
        }
        zaladujObrazkiFrakcji(); // Wywołanie brakującej metody
    }

    // --------- DODANA BRAKUJĄCA METODA ---------
    private void zaladujObrazkiFrakcji() {
        String sciezkaBazowa = "/grafiki/ikony_frakcji/";
        ustawObrazek(obrazekKrolestwa, sciezkaBazowa + "kru_back.jpg", FrakcjaEnum.KROLESTWA_POLNOCY);
        ustawObrazek(obrazekNilfgaard, sciezkaBazowa + "nilf_back.jpg", FrakcjaEnum.NILFGAARD);
        ustawObrazek(obrazekPotwory, sciezkaBazowa + "potw_back.jpg", FrakcjaEnum.POTWORY);
        ustawObrazek(obrazekScoiatael, sciezkaBazowa + "scoia_back.jpg", FrakcjaEnum.SCOIATAEL);
    }
    // -----------------------------------------

    private void ustawObrazek(ImageView imageView, String sciezkaDoObrazka, FrakcjaEnum frakcja) {
        if (imageView == null) return;
        try {
            InputStream stream = getClass().getResourceAsStream(sciezkaDoObrazka);
            if (stream != null) {
                Image img = new Image(stream);
                imageView.setImage(img);
                imageView.setUserData(frakcja);
            } else {
                System.err.println("Nie można załadować obrazka frakcji: " + sciezkaDoObrazka);
            }
        } catch (Exception e) {
            System.err.println("Wyjątek podczas ładowania obrazka " + sciezkaDoObrazka + ": " + e.getMessage());
        }
    }

    @FXML
    private void handleImageClicked(MouseEvent event) {
        ImageView kliknietyObrazek = (ImageView) event.getSource();
        FrakcjaEnum wybrana = (FrakcjaEnum) kliknietyObrazek.getUserData();

        if (wybrana != null) {
            if (aktualnieWybranyObrazek != null && aktualnieWybranyObrazek != kliknietyObrazek) {
                aktualnieWybranyObrazek.setEffect(null);
            }
            aktualnieWybranaFrakcja = wybrana;
            aktualnieWybranyObrazek = kliknietyObrazek;
            kliknietyObrazek.setEffect(efektPodswietleniaWyboru);
            if (etykietaOpisFrakcji != null) etykietaOpisFrakcji.setText(pobierzOpisZdolnosciFrakcji(aktualnieWybranaFrakcja));
            if (przyciskDalej != null) przyciskDalej.setDisable(false);
        }
    }

    @FXML
    private void handleImageMouseEntered(MouseEvent event) {
        ImageView najechanyObrazek = (ImageView) event.getSource();
        FrakcjaEnum frakcjaPodKursorem = (FrakcjaEnum) najechanyObrazek.getUserData();

        if (frakcjaPodKursorem != null) {
            if (etykietaOpisFrakcji != null) etykietaOpisFrakcji.setText(pobierzOpisZdolnosciFrakcji(frakcjaPodKursorem));
            if (najechanyObrazek != aktualnieWybranyObrazek) {
                najechanyObrazek.setEffect(efektNajechania);
            }
        }
        if (najechanyObrazek != null) najechanyObrazek.setCursor(Cursor.HAND); // Dodane sprawdzenie null
    }

    @FXML
    private void handleImageMouseExited(MouseEvent event) {
        ImageView opusczonyObrazek = (ImageView) event.getSource();
        if (opusczonyObrazek != aktualnieWybranyObrazek) {
            opusczonyObrazek.setEffect(null);
        }
        if (etykietaOpisFrakcji != null) {
            if (aktualnieWybranaFrakcja != null) {
                etykietaOpisFrakcji.setText(pobierzOpisZdolnosciFrakcji(aktualnieWybranaFrakcja));
            } else {
                etykietaOpisFrakcji.setText("Najedź na ikonę frakcji, aby zobaczyć jej zdolność. Kliknij, aby wybrać.");
            }
        }
        if (opusczonyObrazek != null) opusczonyObrazek.setCursor(Cursor.DEFAULT); // Dodane sprawdzenie null
    }

    private String pobierzOpisZdolnosciFrakcji(FrakcjaEnum frakcja) {
        if (frakcja == null) return "Wybierz frakcję.";
        switch (frakcja) {
            case KROLESTWA_POLNOCY:
                return "Królestwa Północy: Dobierz dodatkową kartę za każdym razem, gdy wygrasz rundę.";
            case NILFGAARD:
                return "Nilfgaard: Wygraj rundę w przypadku remisu.";
            case SCOIATAEL:
                return "Scoia'tael: Zdecyduj, kto rozpoczyna pierwszą rundę.";
            case POTWORY:
                return "Potwory: Jedna losowa karta jednostki pozostaje na planszy po zakończeniu każdej rundy.";
            default:
                return "Opis zdolności dla tej frakcji nie został zdefiniowany.";
        }
    }

    @FXML
    private void handleDalejButtonAction() {
        if (aktualnieWybranaFrakcja == null) {
            if (etykietaOpisFrakcji != null) {
                etykietaOpisFrakcji.setText("Musisz wybrać frakcję!");
                etykietaOpisFrakcji.setTextFill(Color.RED);
            }
            return;
        }
        if (gracz1 != null) {
            System.out.println(gracz1.getNazwaUzytkownika() + " wybrał frakcję: " +
                    aktualnieWybranaFrakcja.getNazwaWyswietlana() + " do stworzenia talii.");
            if (pixelGwintAplikacja != null) {
                pixelGwintAplikacja.pokazKreatorTalii(gracz1, aktualnieWybranaFrakcja);
            }
        } else {
            System.err.println("Błąd: Obiekt gracz1 jest null w KontrolerWyboruFrakcjiDlaTalii.");
            if (etykietaOpisFrakcji != null) {
                etykietaOpisFrakcji.setText("Błąd: Nie można kontynuować bez danych gracza.");
                etykietaOpisFrakcji.setTextFill(Color.RED);
            }
        }
    }

    @FXML
    private void handlePowrotDoMenuAction() {
        aktualnieWybranaFrakcja = null;
        if (aktualnieWybranyObrazek != null) {
            aktualnieWybranyObrazek.setEffect(null);
            aktualnieWybranyObrazek = null;
        }
        if (pixelGwintAplikacja != null && gracz1 != null) {
            pixelGwintAplikacja.pokazEkranZarzadzaniaTaliami(gracz1);
        }
    }
}