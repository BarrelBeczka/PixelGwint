package pixelgwint.widok;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell; // Dodaj, jeśli będziesz chciał bardziej zaawansowanych ListCell
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow; // Import dla DropShadow
import javafx.scene.layout.VBox;       // Import dla VBox
import javafx.scene.paint.Color;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.logika.MenedzerTalii;
import pixelgwint.model.FrakcjaEnum;
import pixelgwint.model.Gracz;
import pixelgwint.model.Karta;
import pixelgwint.model.TaliaUzytkownika;
import pixelgwint.model.TypKartyEnum;
import pixelgwint.model.Uzytkownik;
import pixelgwint.logika.WczytywaczKart;

import java.net.URL; // Import dla URL
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KontrolerWyboruZapisanejTalii {

    @FXML private VBox rootWyboruZapisanejTaliiPane; // Pole dla głównego kontenera
    @FXML private Label etykietaTytulowaWyboruTalii;
    @FXML private ListView<TaliaUzytkownika> listaZapisanychTaliiDoGry;
    @FXML private Label etykietaSzczegolyTalii;
    @FXML private Button przyciskWybierzTalie;
    @FXML private Button przyciskPowrotZWyboruTalii;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private Uzytkownik uzytkownikWybierajacy;
    private int numerGraczaDlaKtoregoWybieramy;
    private MenedzerTalii menedzerTalii;
    private List<Karta> wszystkieKartyWGrze;

    // Efekt złotej poświaty
    private final DropShadow goldGlowEffect = new DropShadow(20, Color.rgb(255, 215, 0, 0.7));

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    public void initialize() {
        ustawTloProgramistycznie();

        // Dodanie efektu hover do przycisków
        Button[] przyciski = {przyciskWybierzTalie, przyciskPowrotZWyboruTalii};
        for (Button przycisk : przyciski) {
            if (przycisk != null) {
                przycisk.setOnMouseEntered(event -> przycisk.setEffect(goldGlowEffect));
                przycisk.setOnMouseExited(event -> przycisk.setEffect(null));
            }
        }
        if (listaZapisanychTaliiDoGry != null) {
            listaZapisanychTaliiDoGry.setCellFactory(lv -> new ListCell<TaliaUzytkownika>() {
                @Override
                protected void updateItem(TaliaUzytkownika talia, boolean empty) {
                    super.updateItem(talia, empty);
                    if (empty || talia == null) {
                        setText(null);
                        setGraphic(null);
                        setStyle("-fx-background-color: transparent;"); // Komórki przezroczyste
                    } else {
                        setText(talia.toString());
                        // Style dla tekstu i zaznaczenia
                        if (isSelected()) {
                            setStyle("-fx-background-color: #B8860B; -fx-text-fill: black;"); // Ciemnozłote zaznaczenie
                        } else if (isHover()) {
                            setStyle("-fx-background-color: rgba(255, 215, 0, 0.15); -fx-text-fill: #FFEEAA;"); // Złota poświata hover
                        } else {
                            setStyle("-fx-background-color: transparent; -fx-text-fill: #FFD700;"); // Złoty tekst, przezroczyste tło
                        }
                    }
                }
            });
        }
    }

    private void ustawTloProgramistycznie() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png";
        if (rootWyboruZapisanejTaliiPane == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerWyboruZapisanejTalii): rootWyboruZapisanejTaliiPane nie zostało wstrzyknięte!");
            return;
        }
        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);
        if (imageUrl == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerWyboruZapisanejTalii): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
        } else {
            String cssExternalUrl = imageUrl.toExternalForm();
            String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: no-repeat;";
            rootWyboruZapisanejTaliiPane.setStyle(newStyle);
            System.out.println("INFO (KontrolerWyboruZapisanejTalii): Tło obrazkowe ustawione programistycznie.");
        }
    }

    public void inicjalizujEkran(Uzytkownik uzytkownik, int numerGracza) {
        this.uzytkownikWybierajacy = uzytkownik; // To jest profil gracza, który aktualnie wybiera (dla P2 to będzie tymczasowy profil)
        this.numerGraczaDlaKtoregoWybieramy = numerGracza;
        this.menedzerTalii = new MenedzerTalii();

        if (this.wszystkieKartyWGrze == null) {
            if (this.pixelGwintAplikacja != null && this.pixelGwintAplikacja.getWszystkieKartyWGrze() != null) {
                this.wszystkieKartyWGrze = this.pixelGwintAplikacja.getWszystkieKartyWGrze();
            } else {
                this.wszystkieKartyWGrze = WczytywaczKart.wczytajWszystkieKarty(); // Fallback
            }
        }

        przyciskWybierzTalie.setDisable(true);

        List<TaliaUzytkownika> talieDoWyswietlenia;
        String profilDoWczytaniaTaliiUzytkownika;

        if (numerGracza == 2 && pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat() && !pixelGwintAplikacja.getCzyGracz2WybieraTaliePredefiniowana()) {
            // Gracz 2 wybiera "własną" talię - w tym scenariuszu chcemy pokazać talie Gracza 1
            Uzytkownik profilGracza1 = pixelGwintAplikacja.getProfilGracza1();
            if (profilGracza1 != null) {
                profilDoWczytaniaTaliiUzytkownika = profilGracza1.getNazwaUzytkownika();
                etykietaTytulowaWyboruTalii.setText("Gracz 2 (" + uzytkownikWybierajacy.getNazwaUzytkownika() +
                        ") wybiera z talii gracza: " + profilGracza1.getNazwaUzytkownika());
                System.out.println("INFO: Gracz 2 wybiera z talii Gracza 1 (" + profilGracza1.getNazwaUzytkownika() + ")");
            } else {
                // Sytuacja awaryjna - profil Gracza 1 nie jest dostępny
                System.err.println("BŁĄD: Profil Gracza 1 nie jest ustawiony w PixelGwintAplikacja. Gracz 2 nie może wybrać jego talii.");
                profilDoWczytaniaTaliiUzytkownika = uzytkownikWybierajacy.getNazwaUzytkownika(); // Wczytaj (prawdopodobnie puste) talie dla profilu Gracza 2
                etykietaTytulowaWyboruTalii.setText("Wybierz Talię dla Gracza " + numerGraczaDlaKtoregoWybieramy +
                        " (" + uzytkownikWybierajacy.getNazwaUzytkownika() + ")");
            }
        } else {
            // Domyślne zachowanie: Gracz 1 wybiera swoją talię, lub inny scenariusz
            profilDoWczytaniaTaliiUzytkownika = uzytkownikWybierajacy.getNazwaUzytkownika();
            etykietaTytulowaWyboruTalii.setText("Wybierz Talię dla Gracza " + numerGraczaDlaKtoregoWybieramy +
                    " (" + uzytkownikWybierajacy.getNazwaUzytkownika() + ")");
        }

        talieDoWyswietlenia = menedzerTalii.wczytajTalieUzytkownika(profilDoWczytaniaTaliiUzytkownika); // [cite: 1]

        if (talieDoWyswietlenia.isEmpty()) {
            if (numerGracza == 2 && profilDoWczytaniaTaliiUzytkownika.equals(pixelGwintAplikacja.getProfilGracza1().getNazwaUzytkownika())) {
                listaZapisanychTaliiDoGry.setPlaceholder(new Label("Gracz " + profilDoWczytaniaTaliiUzytkownika + " nie ma żadnych zapisanych talii.\nStwórz talię w menu 'Zarządzanie Taliami'."));
            } else {
                listaZapisanychTaliiDoGry.setPlaceholder(new Label("Nie masz żadnych zapisanych talii.\nStwórz talię w menu 'Zarządzanie Taliami'."));
            }
            etykietaSzczegolyTalii.setText("Brak talii do wyboru.");
        } else {
            listaZapisanychTaliiDoGry.setItems(FXCollections.observableArrayList(talieDoWyswietlenia));
        }

        listaZapisanychTaliiDoGry.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                wyswietlSzczegolyTalii(newVal);
                przyciskWybierzTalie.setDisable(false);
            } else {
                etykietaSzczegolyTalii.setText("Wybierz talię z listy...");
                przyciskWybierzTalie.setDisable(true);
            }
        });
    }
    // Metody wyswietlSzczegolyTalii, znajdzNazweKartyPoId, handleWybierzTalieAction,
    // stworzGraczaZTaliUzytkownika, handlePowrotZWyboruTaliiAction - pozostają bez zmian.
    private void wyswietlSzczegolyTalii(TaliaUzytkownika talia) {
        if (talia == null) return;
        long liczbaKart = talia.getIdKartWDecku() != null ? talia.getIdKartWDecku().size() : 0;
        String nazwaDowodcy = znajdzNazweKartyPoId(talia.getIdKartyDowodcy());

        etykietaSzczegolyTalii.setText(
                "Nazwa: " + talia.getNazwaNadanaTalii() + "\n" +
                        "Frakcja: " + talia.getFrakcja().getNazwaWyswietlana() + "\n" +
                        "Dowódca: " + nazwaDowodcy + "\n" +
                        "Liczba kart (bez dowódcy): " + liczbaKart
        );
    }

    private String znajdzNazweKartyPoId(int idKarty) {
        if (wszystkieKartyWGrze == null) return "Błąd: Nie wczytano kart";
        return wszystkieKartyWGrze.stream()
                .filter(k -> k.getId() == idKarty)
                .map(Karta::getNazwa)
                .findFirst()
                .orElse("Nieznana karta (ID: " + idKarty + ")");
    }


    @FXML
    private void handleWybierzTalieAction() {
        TaliaUzytkownika wybranaTalia = listaZapisanychTaliiDoGry.getSelectionModel().getSelectedItem();
        if (wybranaTalia == null) {
            etykietaSzczegolyTalii.setText("Musisz wybrać talię!");
            etykietaSzczegolyTalii.setTextFill(Color.RED);
            return;
        }

        System.out.println("Gracz " + numerGraczaDlaKtoregoWybieramy + " (" + uzytkownikWybierajacy.getNazwaUzytkownika() +
                ") wybrał talię: " + wybranaTalia.getNazwaNadanaTalii());

        if (numerGraczaDlaKtoregoWybieramy == 1) {
            // Gracz 1 zakończył wybór. Zapisujemy jego dane.
            pixelGwintAplikacja.setWybranaTaliaGracza1(wybranaTalia);
            pixelGwintAplikacja.setProfilGracza1(this.uzytkownikWybierajacy); // uzytkownikWybierajacy to zalogowany gracz

            // Przechodzimy do ekranu wpisania nazwy dla Gracza 2
            pixelGwintAplikacja.pokazEkranWpisuNazwyGracza2(false); // false oznacza tryb własnych talii dla P2

        } else { // numerGraczaDlaKtoregoWybieramy == 2
            // Gracz 2 (którego profil został stworzony na podstawie wpisanej nazwy) zakończył wybór
            TaliaUzytkownika taliaGracza1Dane = pixelGwintAplikacja.getWybranaTaliaGracza1();
            Uzytkownik zapisanyProfilGracza1 = pixelGwintAplikacja.getProfilGracza1();
            TaliaUzytkownika taliaGracza2Dane = wybranaTalia; // Talia dla Gracza 2

            if (taliaGracza1Dane == null || zapisanyProfilGracza1 == null) {
                System.err.println("Błąd krytyczny: Talia lub profil Gracza 1 nie zostały wybrane/zapisane!");
                etykietaSzczegolyTalii.setText("Błąd: Wybór Gracza 1 nie został zapisany.");
                etykietaSzczegolyTalii.setTextFill(Color.RED);
                return;
            }

            Gracz graczDoGry1 = stworzGraczaZTaliUzytkownika(zapisanyProfilGracza1, taliaGracza1Dane);
            // this.uzytkownikWybierajacy to teraz profil Gracza 2 (z wpisaną nazwą)
            Gracz graczDoGry2 = stworzGraczaZTaliUzytkownika(this.uzytkownikWybierajacy, taliaGracza2Dane);


            if (graczDoGry1 != null && graczDoGry2 != null) {
                System.out.println("Obaj gracze przygotowani z własnych talii. Przechodzenie do planszy gry...");
                pixelGwintAplikacja.pokazPlanszeGry(graczDoGry1, graczDoGry2);
            } else {
                System.err.println("Błąd krytyczny: Nie udało się stworzyć obiektów graczy.");
                etykietaSzczegolyTalii.setText("Błąd: Nie można rozpocząć gry.");
                etykietaSzczegolyTalii.setTextFill(Color.RED);
            }
        }
    }

    private Gracz stworzGraczaZTaliUzytkownika(Uzytkownik profil, TaliaUzytkownika taliaUzytkownika) {
        if (this.wszystkieKartyWGrze == null || this.wszystkieKartyWGrze.isEmpty() || taliaUzytkownika == null) {
            System.err.println("Nie można stworzyć gracza - brak danych kart lub definicji talii.");
            return null;
        }

        Map<Integer, Karta> mapaKart = this.wszystkieKartyWGrze.stream()
                .collect(Collectors.toMap(Karta::getId, karta -> karta));

        Karta kartaDowodcy = mapaKart.get(taliaUzytkownika.getIdKartyDowodcy());
        if (kartaDowodcy == null) {
            System.err.println("Nie znaleziono karty dowódcy o ID: " + taliaUzytkownika.getIdKartyDowodcy() + " dla talii " + taliaUzytkownika.getNazwaNadanaTalii());
            return null;
        }

        List<Karta> odtworzonaTaliaDoGry = new ArrayList<>();
        for (Integer idKarty : taliaUzytkownika.getIdKartWDecku()) {
            Karta kartaZTali = mapaKart.get(idKarty);
            if (kartaZTali != null) {
                odtworzonaTaliaDoGry.add(kartaZTali);
            } else {
                System.err.println("Nie znaleziono karty o ID: " + idKarty + " przy odtwarzaniu talii '" + taliaUzytkownika.getNazwaNadanaTalii() + "'");
            }
        }

        // Logi, które mogą być przydatne do dalszego debugowania, jeśli problem będzie nadal występował:
        System.out.println("[stworzGracza] Profil: " + profil.getNazwaUzytkownika() + ", Frakcja: " + taliaUzytkownika.getFrakcja().getNazwaWyswietlana());
        System.out.println("[stworzGracza] Oczekiwano ID kart: " + taliaUzytkownika.getIdKartWDecku().size());
        System.out.println("[stworzGracza] Stworzono obiektów Karta: " + odtworzonaTaliaDoGry.size());
        if (taliaUzytkownika.getIdKartWDecku().size() != odtworzonaTaliaDoGry.size()) {
            System.err.println("[stworzGracza] UWAGA: Niezgodność liczby kart! Sprawdź logi 'Nie znaleziono karty o ID'.");
        }

        Gracz gracz = new Gracz(profil, taliaUzytkownika.getFrakcja());
        gracz.setKartaDowodcy(kartaDowodcy);
        gracz.setTaliaBazowa(new ArrayList<>(odtworzonaTaliaDoGry)); // <<< KLUCZOWA ZMIANA: Ustaw talię bazową
        gracz.setTaliaDoGry(new ArrayList<>(odtworzonaTaliaDoGry)); // Ustaw talię do gry (również jako nowa kopia)
        Collections.shuffle(gracz.getTaliaDoGry());

        return gracz;
    }

    @FXML
    private void handlePowrotZWyboruTaliiAction() {
        if (pixelGwintAplikacja != null) {
            // Zawsze wracamy do ekranu wyboru trybu talii, przekazując zalogowanego użytkownika
            pixelGwintAplikacja.pokazEkranWyboruTrybuTalii(uzytkownikWybierajacy);
        }
    }

}