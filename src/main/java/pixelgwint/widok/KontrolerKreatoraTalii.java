package pixelgwint.widok;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane; // Potrzebne, jeśli chcesz manipulować Scrollem
import javafx.scene.control.TextField;
// import javafx.scene.effect.DropShadow; // Ten import nie jest używany w dostarczonym kodzie, można usunąć jeśli niepotrzebny
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
// import javafx.scene.input.MouseEvent; // Ten import nie jest używany w dostarczonym kodzie, można usunąć jeśli niepotrzebny
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox; // Upewnij się, że ten import jest, jeśli VBox jest używany jako typ pola
import javafx.scene.layout.BorderPane; // Import dla rootDeckBuilderPane
// import javafx.scene.paint.Color; // Ten import nie jest używany po usunięciu etykietyWalidacjiTalii, można usunąć
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.logika.MenedzerTalii;
import pixelgwint.logika.WczytywaczKart;
import pixelgwint.model.*; // Importuje Karta, FrakcjaEnum, TypKartyEnum, TaliaUzytkownika, Uzytkownik

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
// import java.util.Collections; // Ten import nie jest używany w dostarczonym kodzie, można usunąć jeśli niepotrzebny
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class KontrolerKreatoraTalii {
    private Map<String, Image> imageCache = new HashMap<>();
    @FXML private BorderPane rootDeckBuilderPane; // Dodane pole dla głównego kontenera

    @FXML private Label etykietaTytulowa;
    @FXML private TextField poleNazwaTalii;
    @FXML private ImageView obrazekPodgladuKarty;
    @FXML private Label etykietaWybranyDowodca;
    @FXML private Label licznikKartTotal;
    @FXML private Label licznikKartJednostek;
    @FXML private Label licznikKartSpecjalnych;
    @FXML private Label licznikDowodcow;
    @FXML private Button przyciskZapiszTalie;
    @FXML private Button przyciskAnulujKreator;

    // Usunięto @FXML private ScrollPane scrollPaneDostepneKarty; i scrollPaneKartyWTalii;
    // ponieważ nie odwołujemy się do nich bezpośrednio w kodzie Java,
    // tylko do ich zawartości (FlowPane). Jeśli jednak chcesz nimi manipulować, odkomentuj.
    // @FXML private HBox kontenerFiltrow; // Podobnie, jeśli nie jest używany w Javie

    @FXML private FlowPane flowPaneDostepneKarty;
    @FXML private FlowPane flowPaneKartyWTalii;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private Uzytkownik aktualnyUzytkownik;
    private FrakcjaEnum wybranaFrakcjaDoTalii;
    private MenedzerTalii menedzerTalii;
    private List<Karta> wszystkieKartyWGrze;
    private ObservableList<Karta> dostepneKartyDoWyboruObservable = FXCollections.observableArrayList();
    private ObservableList<Karta> kartyWTaliiObservable = FXCollections.observableArrayList();

    private Karta wybranyDowodca = null;

    private static final int MIN_KART_JEDNOSTEK_BOHATEROW = 22;
    private static final int MAX_KART_SPECJALNYCH = 10;
    // Rozmiary kart w FlowPane, które ostatnio ustaliliśmy:
    private static final double SZEROKOSC_OBRAZKA_KARTY = 180.0;
    private static final double WYSOKOSC_OBRAZKA_KARTY = 260.0;

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    public void initialize() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png"; // Twoja potwierdzona ścieżka

        if (rootDeckBuilderPane == null) {
            System.err.println("BŁĄD KRYTYCZNY: rootDeckBuilderPane nie zostało wstrzyknięte! Sprawdź fx:id w FXML.");
            return;
        }

        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);

        if (imageUrl == null) {
            System.err.println("--------------------------------------------------------------------------");
            System.err.println("BŁĄD KRYTYCZNY: Nie można znaleźć obrazka tła dla Kreatora Talii w classpath!");
            System.err.println("Oczekiwana ścieżka: " + backgroundImageClasspathPath);
            System.err.println("Upewnij się, że plik istnieje w: src/main/resources" + backgroundImageClasspathPath);
            System.err.println("Domyślny styl (kolor) zostanie użyty z FXML lub ustawiony tutaj na awaryjny.");
            System.err.println("--------------------------------------------------------------------------");
            // FXML już ma -fx-background-color: #2c2c2c;, więc nie trzeba tu nic więcej robić,
            // chyba że chcesz inny kolor awaryjny.
        } else {
            System.out.println("INFO: Obrazek tła '" + backgroundImageClasspathPath + "' znaleziony. Próba ustawienia programistycznego.");
            String cssExternalUrl = imageUrl.toExternalForm(); // Tworzy URL np. 'file:/...'

            rootDeckBuilderPane.setStyle(
                    "-fx-background-image: url('" + cssExternalUrl + "'); " +
                            "-fx-background-size: cover; " + // Lub stretch, lub 100% 100%
                            "-fx-background-position: center center; " +
                            "-fx-background-repeat: no-repeat;"
            );
            System.out.println("INFO: Tło obrazkowe powinno być ustawione programistycznie.");
        }
    }

    public void inicjalizujKreator(Uzytkownik uzytkownik, FrakcjaEnum frakcja) {
        this.menedzerTalii = new MenedzerTalii();
        this.aktualnyUzytkownik = uzytkownik;
        this.wybranaFrakcjaDoTalii = frakcja;
        etykietaTytulowa.setText("Kreator Talii - " + frakcja.getNazwaWyswietlana());
        poleNazwaTalii.setText("Moja Talia " + frakcja.getNazwaWyswietlana());

        if (this.pixelGwintAplikacja != null) {
            this.wszystkieKartyWGrze = this.pixelGwintAplikacja.getWszystkieKartyWGrze();
        } else {
            System.err.println("OSTRZEŻENIE (KreatorTalii): Brak referencji do PixelGwintAplikacja, karty wczytywane lokalnie.");
            this.wszystkieKartyWGrze = WczytywaczKart.wczytajWszystkieKarty(); // Fallback
        }
        if (this.wszystkieKartyWGrze == null || this.wszystkieKartyWGrze.isEmpty()) {
            System.err.println("BŁĄD KRYTYCZNY (KreatorTalii): Nie udało się uzyskać listy kart!");
            // Dodaj obsługę błędu, np. wyświetl komunikat i zablokuj funkcjonalność
            return;
        }
        dostepneKartyDoWyboruObservable.clear();
        kartyWTaliiObservable.clear();
        if (flowPaneDostepneKarty != null) flowPaneDostepneKarty.getChildren().clear();
        if (flowPaneKartyWTalii != null) flowPaneKartyWTalii.getChildren().clear();
        wybranyDowodca = null;
        if(etykietaWybranyDowodca != null) etykietaWybranyDowodca.setText("Dowódca: (Wybierz z dostępnych kart)");


        ladujDostepneKartyDoWyboru();
        aktualizujLicznikiIWaliacje();
    }

    private void ladujDostepneKartyDoWyboru() {
        dostepneKartyDoWyboruObservable.clear();
        if (wszystkieKartyWGrze == null) return; // Dodatkowe zabezpieczenie
        List<Karta> filtrowane = wszystkieKartyWGrze.stream()
                .filter(k -> k.getFrakcja() == wybranaFrakcjaDoTalii || k.getFrakcja() == FrakcjaEnum.NEUTRALNA)
                .collect(Collectors.toList());
        dostepneKartyDoWyboruObservable.addAll(filtrowane);
        odswiezWizualizacjeDostepnychKart();
    }

    private void odswiezWizualizacjeDostepnychKart() {
        if (flowPaneDostepneKarty == null) return;
        flowPaneDostepneKarty.getChildren().clear();
        for (Karta karta : dostepneKartyDoWyboruObservable) {
            boolean czyJuzWTalii = kartyWTaliiObservable.contains(karta);
            if (!czyJuzWTalii) {
                if (karta.getTyp() == TypKartyEnum.DOWODCA && wybranyDowodca != null && wybranyDowodca.getId() != karta.getId()){
                    continue;
                }
                ImageView obrazekKarty = stworzObrazekKarty(karta, true);
                flowPaneDostepneKarty.getChildren().add(obrazekKarty);
            }
        }
    }

    private void odswiezWizualizacjeKartWTalii() {
        if (flowPaneKartyWTalii == null) return;
        flowPaneKartyWTalii.getChildren().clear();
        for (Karta karta : kartyWTaliiObservable) {
            ImageView obrazekKarty = stworzObrazekKarty(karta, false);
            flowPaneKartyWTalii.getChildren().add(obrazekKarty);
        }
    }

    private ImageView stworzObrazekKarty(Karta karta, boolean czyDodawanie) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(SZEROKOSC_OBRAZKA_KARTY);
        imageView.setFitHeight(WYSOKOSC_OBRAZKA_KARTY);
        imageView.setPreserveRatio(true);
        imageView.setCursor(Cursor.HAND);
        imageView.setUserData(karta); // Ważne, aby przechować obiekt Karta

        if (karta != null && karta.getGrafika() != null && !karta.getGrafika().isEmpty()) {
            Image img = null;
            if (pixelGwintAplikacja != null) {
                img = pixelGwintAplikacja.zapewnijObrazekKarty(karta.getGrafika());
            } else {
                // Fallback, jeśli pixelGwintAplikacja nie jest ustawiona (mniej wydajne)
                System.err.println("OSTRZEŻENIE (stworzObrazekKarty): pixelGwintAplikacja jest null. Ładowanie obrazka bez globalnego cache'a.");
                try (InputStream stream = getClass().getResourceAsStream("/grafiki/karty/" + karta.getGrafika())) {
                    if (stream != null) {
                        img = new Image(stream);
                    }
                } catch (Exception e) {
                    System.err.println("Błąd ładowania grafiki karty (fallback) " + karta.getGrafika() + ": " + e.getMessage());
                }
            }

            if (img != null) {
                imageView.setImage(img);
            } else {
                System.err.println("Nie udało się załadować grafiki dla karty: " + karta.getNazwa() + " (plik: " + karta.getGrafika() + ")");
                ustawPlaceholderDlaKarty(imageView, karta); // Metoda pomocnicza dla placeholdera
            }
        } else {
            String nazwaKartyProblem = (karta != null) ? karta.getNazwa() : "[KARTA NULL]";
            System.err.println("Karta ("+ nazwaKartyProblem +") lub jej grafika jest null/pusta.");
            ustawPlaceholderDlaKarty(imageView, karta);
        }

        imageView.setOnMouseEntered(event -> pokazPodgladKarty(karta));
        if (czyDodawanie) {
            imageView.setOnMouseClicked(event -> dodajKarteDoTalii(karta));
        } else {
            imageView.setOnMouseClicked(event -> usunKarteZTalli(karta));
        }
        return imageView;
    }

    // Metoda pomocnicza do ustawiania placeholdera (jeśli chcesz bardziej zaawansowany niż tylko log)
    private void ustawPlaceholderDlaKarty(ImageView imageView, Karta kartaJeśliJest) {
        // Możesz tu ustawić domyślny obrazek "brak karty" lub np. Label z nazwą karty
        // Na razie, dla przykładu, ustawiamy pusty obrazek (lub nie robimy nic, jeśli domyślnie jest null)
        imageView.setImage(null);
        String nazwaKarty = (kartaJeśliJest != null && kartaJeśliJest.getNazwa() != null) ? kartaJeśliJest.getNazwa() : "[Nieznana Karta]";
        System.err.println("Placeholder dla karty: " + nazwaKarty);
        // Alternatywnie, możesz chcieć pokazać Label jako placeholder, ale metoda zwraca ImageView.
        // Wymagałoby to bardziej złożonej obsługi typu zwracanego lub opakowania.
    }

    private Image ladujObrazekKarty(Karta karta) {
        if (karta == null || karta.getGrafika() == null || karta.getGrafika().isEmpty()) {
            return null;
        }
        if (pixelGwintAplikacja != null) {
            return pixelGwintAplikacja.zapewnijObrazekKarty(karta.getGrafika());
        } else {
            // Fallback, jeśli pixelGwintAplikacja nie jest ustawiona
            System.err.println("OSTRZEŻENIE (ladujObrazekKarty): pixelGwintAplikacja jest null. Ładowanie obrazka bez globalnego cache'a.");
            String sciezka = "/grafiki/karty/" + karta.getGrafika();
            try (InputStream stream = getClass().getResourceAsStream(sciezka)) {
                if (stream != null) {
                    return new Image(stream);
                } else {
                    System.err.println("Nie znaleziono grafiki karty (fallback): " + sciezka);
                }
            } catch (Exception e) {
                System.err.println("Błąd ładowania grafiki karty (fallback) " + karta.getGrafika() + ": " + e.getMessage());
            }
            return null;
        }
    }

    private void pokazPodgladKarty(Karta karta) {
        if (obrazekPodgladuKarty == null) return; // Zabezpieczenie

        if (karta == null) {
            obrazekPodgladuKarty.setImage(null);
            return;
        }

        Image img = ladujObrazekKarty(karta); // Użyj nowej, zaktualizowanej metody ladujObrazekKarty

        if (img != null) {
            obrazekPodgladuKarty.setImage(img);
        } else {
            obrazekPodgladuKarty.setImage(null); // Ustaw null lub obrazek placeholdera, jeśli masz
            System.err.println("Nie udało się załadować podglądu dla karty: " + karta.getNazwa() + " (plik: " + karta.getGrafika() + ")");
        }
    }
    private void dodajKarteDoTalii(Karta karta) {
        if (kartyWTaliiObservable.contains(karta)) {
            System.out.println("Info: Ta karta jest już w Twojej talii.");
            return;
        }
        if (karta.getTyp() == TypKartyEnum.DOWODCA) {
            if (wybranyDowodca == null) {
                wybranyDowodca = karta;
                if(etykietaWybranyDowodca != null) etykietaWybranyDowodca.setText("Dowódca: " + karta.getNazwa());
            } else {
                System.out.println("Info: Możesz wybrać tylko jednego dowódcę.");
                return;
            }
        }
        if (karta.getTyp() == TypKartyEnum.SPECJALNA) {
            long liczbaSpecjalnych = kartyWTaliiObservable.stream()
                    .filter(k -> k.getTyp() == TypKartyEnum.SPECJALNA && (wybranyDowodca == null || k.getId() != wybranyDowodca.getId()))
                    .count();
            if (liczbaSpecjalnych >= MAX_KART_SPECJALNYCH) {
                System.out.println("Info: Osiągnięto limit " + MAX_KART_SPECJALNYCH + " kart specjalnych.");
                return;
            }
        }

        // Aktualizacja modelu danych
        dostepneKartyDoWyboruObservable.remove(karta);
        kartyWTaliiObservable.add(karta); // Możesz chcieć sortować tę listę

        // Bezpośrednia aktualizacja UI
        if (flowPaneDostepneKarty != null) {
            findAndRemoveCardImageView(flowPaneDostepneKarty, karta);
        }
        if (flowPaneKartyWTalii != null) {
            ImageView obrazekKartyDoTalii = stworzObrazekKarty(karta, false); // false -> akcja to usuń z talii
            flowPaneKartyWTalii.getChildren().add(obrazekKartyDoTalii);
            // Jeśli chcesz zachować sortowanie wizualne w flowPaneKartyWTalii,
            // musiałbyś tu dodać logikę wstawiania w odpowiednie miejsce lub pełne odświeżenie tego panelu.
            // Dla uproszczenia na razie dodajemy na koniec.
        }

        aktualizujLicznikiIWaliacje();
    }
    private void usunKarteZTalli(Karta karta) {
        boolean usunietoZModelu = kartyWTaliiObservable.remove(karta);
        if (usunietoZModelu) {
            if (karta.getTyp() == TypKartyEnum.DOWODCA && wybranyDowodca != null && wybranyDowodca.getId() == karta.getId()) {
                wybranyDowodca = null;
                if(etykietaWybranyDowodca != null) etykietaWybranyDowodca.setText("Dowódca: (Wybierz z dostępnych kart)");
            }

            if (!dostepneKartyDoWyboruObservable.contains(karta)) {
                dostepneKartyDoWyboruObservable.add(karta);
                // Rozważ sortowanie dostepneKartyDoWyboruObservable tutaj, jeśli potrzebne
                // np. FXCollections.sort(dostepneKartyDoWyboruObservable, Comparator.comparing(Karta::getNazwa));
            }

            // Bezpośrednia aktualizacja UI
            if (flowPaneKartyWTalii != null) {
                findAndRemoveCardImageView(flowPaneKartyWTalii, karta);
            }
            if (flowPaneDostepneKarty != null) {
                ImageView obrazekKartyDostepnej = stworzObrazekKarty(karta, true); // true -> akcja to dodaj do talii
                flowPaneDostepneKarty.getChildren().add(obrazekKartyDostepnej);
                // Podobnie jak wyżej, jeśli potrzebne sortowanie wizualne w flowPaneDostepneKarty,
                // wymaga to bardziej złożonej logiki lub odświeżenia tego panelu.
                // Dla uproszczenia dodajemy na koniec. Jeśli posortowałeś listę observable,
                // możesz zawołać odswiezWizualizacjeDostepnychKart() tylko dla tego panelu.
            }
            aktualizujLicznikiIWaliacje();
        }
    }
    private void aktualizujLicznikiIWaliacje() {
        long liczbaWszystkich = kartyWTaliiObservable.size();
        long liczbaDowodcow = kartyWTaliiObservable.stream().filter(k -> k.getTyp() == TypKartyEnum.DOWODCA).count();
        long liczbaJednostekBohaterow = kartyWTaliiObservable.stream()
                .filter(k -> k.getTyp() == TypKartyEnum.JEDNOSTKA || k.getTyp() == TypKartyEnum.BOHATER || k.getTyp() == TypKartyEnum.DOWODCA)
                .count();
        long liczbaSpecjalnych = kartyWTaliiObservable.stream()
                .filter(k -> k.getTyp() == TypKartyEnum.SPECJALNA && (wybranyDowodca == null || k.getId() != wybranyDowodca.getId()))
                .count();


        if(licznikKartTotal != null) licznikKartTotal.setText("Łącznie kart w talii: " + liczbaWszystkich);
        if(licznikDowodcow != null) licznikDowodcow.setText("Dowódcy: " + liczbaDowodcow + " (wymagany 1)");
        if(licznikKartJednostek != null) licznikKartJednostek.setText("Karty Jednostek/Bohaterów/Dowódcy: " + liczbaJednostekBohaterow + " (min. " + MIN_KART_JEDNOSTEK_BOHATEROW + ")");
        if(licznikKartSpecjalnych != null) licznikKartSpecjalnych.setText("Karty Specjalne: " + liczbaSpecjalnych + " (max. " + MAX_KART_SPECJALNYCH + ")");

        boolean czyTaliaPoprawna = true;
        StringBuilder komunikatyBleduDlaKonsoli = new StringBuilder();

        if (liczbaDowodcow != 1) {
            komunikatyBleduDlaKonsoli.append("Wymagany dokładnie jeden Dowódca.\n");
            czyTaliaPoprawna = false;
        }
        if (liczbaJednostekBohaterow < MIN_KART_JEDNOSTEK_BOHATEROW) {
            komunikatyBleduDlaKonsoli.append("Minimalna liczba kart Jednostek/Bohaterów/Dowódcy to " + MIN_KART_JEDNOSTEK_BOHATEROW + ".\n");
            czyTaliaPoprawna = false;
        }
        if (liczbaSpecjalnych > MAX_KART_SPECJALNYCH) {
            komunikatyBleduDlaKonsoli.append("Maksymalna liczba kart Specjalnych to " + MAX_KART_SPECJALNYCH + ".\n");
            czyTaliaPoprawna = false;
        }
        if (poleNazwaTalii.getText() == null || poleNazwaTalii.getText().trim().isEmpty()) {
            komunikatyBleduDlaKonsoli.append("Nazwa talii nie może być pusta.\n");
            czyTaliaPoprawna = false;
        }

        if (przyciskZapiszTalie != null) {
            if (czyTaliaPoprawna) {
                System.out.println("Walidacja kreatora: Talia jest poprawna.");
                przyciskZapiszTalie.setDisable(false);
            } else {
                System.out.println("Walidacja kreatora: Talia niepoprawna. Błędy:\n" + komunikatyBleduDlaKonsoli.toString());
                przyciskZapiszTalie.setDisable(true);
            }
        }
    }

    @FXML
    private void handleZapiszTalieAction() {
        if (przyciskZapiszTalie.isDisabled()) {
            System.err.println("Próba zapisu niepoprawnej talii.");
            return;
        }
        String nazwaTalii = poleNazwaTalii.getText().trim();
        if (wybranyDowodca == null) {
            System.err.println("Próba zapisu talii bez dowódcy.");
            return;
        }

        List<Integer> idKartWDecku = kartyWTaliiObservable.stream()
                .filter(karta -> karta.getTyp() != TypKartyEnum.DOWODCA)
                .map(Karta::getId)
                .collect(Collectors.toList());

        TaliaUzytkownika nowaTalia = new TaliaUzytkownika(
                aktualnyUzytkownik.getNazwaUzytkownika(),
                nazwaTalii,
                wybranaFrakcjaDoTalii,
                wybranyDowodca.getId(),
                idKartWDecku
        );

        if (menedzerTalii == null) menedzerTalii = new MenedzerTalii();

        if (menedzerTalii.zapiszTalie(nowaTalia)) {
            System.out.println("Talia '" + nazwaTalii + "' została pomyślnie zapisana!");
            if (pixelGwintAplikacja != null) {
                pixelGwintAplikacja.pokazEkranZarzadzaniaTaliami(aktualnyUzytkownik);
            }
        } else {
            System.err.println("Wystąpił błąd podczas zapisu talii. Sprawdź konsolę.");
        }
    }

    @FXML
    private void handleAnulujKreatorAction() {
        if (pixelGwintAplikacja != null) {
            // Powrót do ekranu zarządzania taliami, bo tam jest przycisk "Stwórz nową"
            // lub do wyboru frakcji dla talii, jeśli to była część tego przepływu.
            // Zgodnie z obecną logiką, po anulowaniu kreatora wracamy do ekranu wyboru frakcji dla nowej talii.
            pixelGwintAplikacja.pokazEkranWyboruFrakcjiDlaTalii(aktualnyUzytkownik);
        }
    }

    private ImageView findAndRemoveCardImageView(FlowPane pane, Karta karta) {
        ImageView foundImageView = null;
        for (Node node : pane.getChildren()) {
            if (node instanceof ImageView) {
                ImageView iv = (ImageView) node;
                // Porównujemy obiekty Karta przechowywane w UserData
                if (iv.getUserData() == karta) {
                    foundImageView = iv;
                    break;
                }
            }
        }
        if (foundImageView != null) {
            pane.getChildren().remove(foundImageView);
        }
        return foundImageView; // Zwracamy usunięty ImageView, jeśli chcemy go przenieść, lub null
    }

}