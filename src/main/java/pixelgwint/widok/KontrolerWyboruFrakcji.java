package pixelgwint.widok;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox; // Upewnij się, że ten import jest
import javafx.scene.paint.Color;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.logika.MenedzerTaliiPredefiniowanych;
import pixelgwint.model.*;
import pixelgwint.logika.WczytywaczKart;

import java.io.InputStream;
import java.net.URL; // Import dla URL
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KontrolerWyboruFrakcji {

    @FXML private VBox rootPaneWyboruFrakcjiGry; // Pole dla głównego kontenera
    @FXML private Label etykietaGracza;
    @FXML private ImageView obrazekKrolestwa;
    @FXML private ImageView obrazekNilfgaard;
    @FXML private ImageView obrazekPotwory;
    @FXML private ImageView obrazekScoiatael;
    @FXML private Label etykietaOpisFrakcji;
    @FXML private Button przyciskDalej;
    @FXML private Button przyciskPowrotDoMenu;

    private PixelGwintAplikacja pixelGwintAplikacja;
    // Usunięto gracz1, ponieważ dane są przekazywane przez inicjalizujDaneDlaGry
    private List<Karta> wszystkieKartyWGrze;
    private FrakcjaEnum aktualnieWybranaFrakcja;
    private ImageView aktualnieWybranyObrazek = null;
    private Uzytkownik uzytkownikWybierajacy;
    private int numerGracza;
    private boolean czyTrybPredefiniowany;
    private MenedzerTaliiPredefiniowanych menedzerTaliiPredefiniowanych;

    private final DropShadow efektPodswietleniaWyboru = new DropShadow(20, Color.rgb(255, 215, 0, 0.9));
    private final DropShadow efektNajechania = new DropShadow(15, Color.rgb(173, 216, 230, 0.7));
    private final DropShadow goldGlowEffect = new DropShadow(20, Color.rgb(255, 215, 0, 0.7)); // Efekt dla przycisków


    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
        this.menedzerTaliiPredefiniowanych = new MenedzerTaliiPredefiniowanych();
    }

    @FXML
    public void initialize() { // Dodajemy metodę initialize
        ustawTloProgramistycznie();

        // Efekt hover dla przycisków
        Button[] przyciski = {przyciskDalej, przyciskPowrotDoMenu};
        for (Button przycisk : przyciski) {
            if (przycisk != null) {
                przycisk.setOnMouseEntered(event -> przycisk.setEffect(goldGlowEffect));
                przycisk.setOnMouseExited(event -> przycisk.setEffect(null));
            }
        }
    }

    private void ustawTloProgramistycznie() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png";
        if (rootPaneWyboruFrakcjiGry == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerWyboruFrakcji): rootPaneWyboruFrakcjiGry nie zostało wstrzyknięte!");
            return;
        }
        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);
        if (imageUrl == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerWyboruFrakcji): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
        } else {
            String cssExternalUrl = imageUrl.toExternalForm();
            String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                    "-fx-background-size: cover; " +
                    "-fx-background-position: center center; " +
                    "-fx-background-repeat: no-repeat;";
            rootPaneWyboruFrakcjiGry.setStyle(newStyle);
            System.out.println("INFO (KontrolerWyboruFrakcji): Tło obrazkowe ustawione programistycznie.");
        }
    }


    public void inicjalizujDaneDlaGry(Uzytkownik uzytkownik, int numerGracza, boolean czyTrybPredefiniowany) {
        this.uzytkownikWybierajacy = uzytkownik;
        this.numerGracza = numerGracza;
        this.czyTrybPredefiniowany = czyTrybPredefiniowany;

        if (etykietaGracza != null) { // Sprawdzenie null
            etykietaGracza.setText("Gracz " + numerGracza + " (" + uzytkownik.getNazwaUzytkownika() + ") wybiera frakcję:");
        }


        aktualnieWybranaFrakcja = null;
        if (aktualnieWybranyObrazek != null) aktualnieWybranyObrazek.setEffect(null);
        aktualnieWybranyObrazek = null;

        if (etykietaOpisFrakcji != null) { // Sprawdzenie null
            etykietaOpisFrakcji.setText("Najedź na ikonę frakcji, aby zobaczyć jej zdolność. Kliknij, aby wybrać.");
        }
        if (przyciskDalej != null) { // Sprawdzenie null
            przyciskDalej.setDisable(true);
        }


        if (this.wszystkieKartyWGrze == null) {
            if (this.pixelGwintAplikacja != null && this.pixelGwintAplikacja.getWszystkieKartyWGrze() != null) {
                this.wszystkieKartyWGrze = this.pixelGwintAplikacja.getWszystkieKartyWGrze();
            } else {
                this.wszystkieKartyWGrze = WczytywaczKart.wczytajWszystkieKarty(); // Fallback
            }
        }

        if (this.wszystkieKartyWGrze == null || this.wszystkieKartyWGrze.isEmpty()) {
            System.err.println("Nie wczytano żadnych kart!");
            if(etykietaOpisFrakcji != null) etykietaOpisFrakcji.setText("Błąd: Nie wczytano kart!");
            return;
        }
        zaladujObrazkiFrakcji();
    }

    // Metody zaladujObrazkiFrakcji, ustawObrazek, handleImageClicked, handleImageMouseEntered,
    // handleImageMouseExited, pobierzOpisZdolnosciFrakcji, przygotujGracza, handleDalejButtonAction,
    // stworzGraczaZTaliUzytkownika - pozostają takie same jak w KontrolerWyboruFrakcjiDlaTalii (lub bez zmian, jeśli były poprawne)

    private void zaladujObrazkiFrakcji() {
        String sciezkaBazowa = "/grafiki/ikony_frakcji/";
        ustawObrazek(obrazekKrolestwa, sciezkaBazowa + "kru_back.jpg", FrakcjaEnum.KROLESTWA_POLNOCY);
        ustawObrazek(obrazekNilfgaard, sciezkaBazowa + "nilf_back.jpg", FrakcjaEnum.NILFGAARD);
        ustawObrazek(obrazekPotwory, sciezkaBazowa + "potw_back.jpg", FrakcjaEnum.POTWORY);
        ustawObrazek(obrazekScoiatael, sciezkaBazowa + "scoia_back.jpg", FrakcjaEnum.SCOIATAEL);
    }

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
        if (najechanyObrazek != null) najechanyObrazek.setCursor(Cursor.HAND);
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
        if (opusczonyObrazek != null) opusczonyObrazek.setCursor(Cursor.DEFAULT);
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
    private Gracz przygotujGracza(Uzytkownik profil, FrakcjaEnum wybranaFrakcja) {
        if (wszystkieKartyWGrze == null || wszystkieKartyWGrze.isEmpty()) {
            System.err.println("Próba przygotowania gracza bez wczytanych kart!");
            return null;
        }

        Gracz nowyGracz = new Gracz(profil, wybranaFrakcja);
        List<Karta> kartyDoTaliiBazowej = new ArrayList<>();
        Karta wybranyLider = null;

        // Najpierw znajdź lidera dla frakcji
        List<Karta> potencjalniLiderzy = wszystkieKartyWGrze.stream()
                .filter(k -> k.getFrakcja() == wybranaFrakcja && k.getTyp() == TypKartyEnum.DOWODCA)
                .collect(Collectors.toList());

        if (potencjalniLiderzy.isEmpty()) {
            System.err.println("BŁĄD KRYTYCZNY: Nie znaleziono żadnego dowódcy dla frakcji: " + wybranaFrakcja.getNazwaWyswietlana());
            return null;
        }
        // Wybierz losowego lidera, jeśli jest więcej niż jeden, lub pierwszego
        Collections.shuffle(potencjalniLiderzy);
        wybranyLider = potencjalniLiderzy.get(0);
        nowyGracz.setKartaDowodcy(wybranyLider);
        System.out.println("Dla gracza " + profil.getNazwaUzytkownika() + " (" + wybranaFrakcja + ") wylosowano dowódcę: " + wybranyLider.getNazwa());


        for (Karta karta : wszystkieKartyWGrze) {
            if ((karta.getFrakcja() == wybranaFrakcja || karta.getFrakcja() == FrakcjaEnum.NEUTRALNA)
                    && karta.getTyp() != TypKartyEnum.DOWODCA) { // Dowódcy nie są częścią standardowej talii do gry
                kartyDoTaliiBazowej.add(karta);
            }
        }

        // W przypadku talii predefiniowanej, logika MenedzerTaliiPredefiniowanych powinna dostarczyć konkretne ID kart.
        // Poniższa logika jest bardziej dla ogólnego przygotowania gracza na podstawie frakcji.
        // Dla trybu predefiniowanego, talia jest już ustalona.

        nowyGracz.setTaliaBazowa(kartyDoTaliiBazowej); // To jest pula kart, z której można by budować talię

        // Jeśli to tryb predefiniowany, talia do gry zostanie załadowana później.
        // Jeśli nie, to tutaj można by zaimplementować logikę tworzenia/ładowania talii gracza.
        // Na razie zostawiamy talię do gry pustą, bo zostanie załadowana przez logikę wyboru talii.
        nowyGracz.setTaliaDoGry(new ArrayList<>());


        return nowyGracz;
    }


    @FXML
    private void handleDalejButtonAction() {
        if (aktualnieWybranaFrakcja == null) {
            if(etykietaOpisFrakcji != null) {
                etykietaOpisFrakcji.setText("Musisz wybrać frakcję!");
                etykietaOpisFrakcji.setTextFill(Color.RED);
            }
            return;
        }

        if (czyTrybPredefiniowany) {
            TaliaUzytkownika wylosowanaTaliaPredefiniowana = menedzerTaliiPredefiniowanych.pobierzLosowaTaliePredefiniowana(aktualnieWybranaFrakcja);

            if (wylosowanaTaliaPredefiniowana == null) {
                if(etykietaOpisFrakcji != null) {
                    etykietaOpisFrakcji.setText("Brak talii predefiniowanych dla frakcji: " + aktualnieWybranaFrakcja.getNazwaWyswietlana());
                    etykietaOpisFrakcji.setTextFill(Color.RED);
                }
                return;
            }

            System.out.println("Gracz " + numerGracza + " (" + uzytkownikWybierajacy.getNazwaUzytkownika() +
                    ") wybrał frakcję " + aktualnieWybranaFrakcja.getNazwaWyswietlana() +
                    ", przypisano talię predefiniowaną: " + wylosowanaTaliaPredefiniowana.getNazwaNadanaTalii());

            if (numerGracza == 1) {
                // Gracz 1 zakończył wybór. Zapisujemy jego dane.
                pixelGwintAplikacja.setWybranaTaliaGracza1(wylosowanaTaliaPredefiniowana);
                pixelGwintAplikacja.setProfilGracza1(this.uzytkownikWybierajacy); // uzytkownikWybierajacy to zalogowany gracz

                // Przechodzimy do ekranu wpisania nazwy dla Gracza 2
                pixelGwintAplikacja.pokazEkranWpisuNazwyGracza2(true); // true oznacza tryb predefiniowany dla P2

            } else { // numerGracza == 2
                // Gracz 2 (którego profil został stworzony na podstawie wpisanej nazwy) zakończył wybór
                TaliaUzytkownika taliaGracza1Dane = pixelGwintAplikacja.getWybranaTaliaGracza1();
                Uzytkownik zapisanyProfilGracza1 = pixelGwintAplikacja.getProfilGracza1();
                TaliaUzytkownika taliaGracza2Dane = wylosowanaTaliaPredefiniowana; // Talia dla Gracza 2

                if (taliaGracza1Dane == null || zapisanyProfilGracza1 == null) {
                    System.err.println("Błąd krytyczny: Talia lub profil Gracza 1 nie zostały wybrane/zapisane!");
                    if(etykietaOpisFrakcji != null) etykietaOpisFrakcji.setText("Błąd: Wybór Gracza 1 nie został zapisany.");
                    return;
                }

                Gracz graczDoGry1 = stworzGraczaZTaliUzytkownika(zapisanyProfilGracza1, taliaGracza1Dane);
                // this.uzytkownikWybierajacy to teraz profil Gracza 2 (z wpisaną nazwą)
                Gracz graczDoGry2 = stworzGraczaZTaliUzytkownika(this.uzytkownikWybierajacy, taliaGracza2Dane);

                if (graczDoGry1 != null && graczDoGry2 != null) {
                    System.out.println("Obaj gracze przygotowani z talii predefiniowanych. Przechodzenie do planszy gry...");
                    pixelGwintAplikacja.pokazPlanszeGry(graczDoGry1, graczDoGry2);
                } else {
                    System.err.println("Błąd krytyczny: Nie udało się stworzyć obiektów graczy dla talii predefiniowanych.");
                    if(etykietaOpisFrakcji != null) etykietaOpisFrakcji.setText("Błąd: Nie można rozpocząć gry z taliami predefiniowanymi.");
                }
            }
        } else {
            // Logika dla trybu niepredefiniowanego (jeśli ten kontroler miałby go obsługiwać)
            // Obecnie ten blok wydaje się nie być używany w głównym przepływie gry.
            System.out.println("Tryb NIEPREDEFINIOWANY nie jest jeszcze w pełni obsłużony w handleDalejButtonAction w KontrolerWyboruFrakcji.");
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
    private void handlePowrotDoMenuAction() {
        if (pixelGwintAplikacja != null && uzytkownikWybierajacy != null) {
            // Zawsze wracamy do ekranu wyboru trybu talii, bo stamtąd przyszliśmy do wyboru frakcji (dla predef.)
            pixelGwintAplikacja.pokazEkranWyboruTrybuTalii(uzytkownikWybierajacy);
        }
    }

}