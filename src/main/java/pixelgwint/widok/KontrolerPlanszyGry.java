package pixelgwint.widok;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.logika.SilnikGry;
import pixelgwint.logika.WczytywaczKart;
import pixelgwint.model.*;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class KontrolerPlanszyGry {

    // --- Pola FXML ---
    @FXML private AnchorPane glownyKontenerPlanszy;
    @FXML private HBox pogodaHBox;
    @FXML private ImageView dowodcaGracza1ImageView;
    @FXML private ImageView dowodcaGracza2ImageView;
    @FXML private ImageView cmentarzGracza1ImageView;
    @FXML private Label licznikCmentarzaG1Label;
    @FXML private ImageView cmentarzGracza2ImageView;
    @FXML private Label licznikCmentarzaG2Label;
    @FXML private ImageView taliaGracza1ImageView;
    @FXML private Label licznikTaliiG1Label;
    @FXML private ImageView taliaGracza2ImageView;
    @FXML private Label licznikTaliiG2Label;
    @FXML private HBox rekaGracza1HBox;
    @FXML private HBox rekaGracza2HBox;
    @FXML private StackPane rogPiechotyG1Slot;
    @FXML private HBox rzadPiechotyG1HBox;
    @FXML private Label punktyPiechotyG1Label;
    @FXML private StackPane rogStrzeleckiG1Slot;
    @FXML private HBox rzadStrzeleckiG1HBox;
    @FXML private Label punktyStrzeleckieG1Label;
    @FXML private StackPane rogOblezeniaG1Slot;
    @FXML private HBox rzadOblezeniaG1HBox;
    @FXML private Label punktyOblezeniaG1Label;
    @FXML private StackPane rogPiechotyG2Slot;
    @FXML private HBox rzadPiechotyG2HBox;
    @FXML private Label punktyPiechotyG2Label;
    @FXML private StackPane rogStrzeleckiG2Slot;
    @FXML private HBox rzadStrzeleckiG2HBox;
    @FXML private Label punktyStrzeleckieG2Label;
    @FXML private StackPane rogOblezeniaG2Slot;
    @FXML private HBox rzadOblezeniaG2HBox;
    @FXML private Label punktyOblezeniaG2Label;
    @FXML private Label sumaPunktowG1Label;
    @FXML private Label sumaPunktowG2Label;
    @FXML private Label nickGracza1Label;
    @FXML private Label dodatkoweInfoG1Label;
    @FXML private ImageView zycie1G1ImageView;
    @FXML private ImageView zycie2G1ImageView;
    @FXML private ImageView logoGracza1ImageView;
    @FXML private Label nickGracza2Label;
    @FXML private Label dodatkoweInfoG2Label;
    @FXML private ImageView zycie1G2ImageView;
    @FXML private ImageView zycie2G2ImageView;
    @FXML private ImageView logoGracza2ImageView;
    @FXML private VBox kontenerPodgladuKarty;
    @FXML private ImageView podgladKartyImageView;
    @FXML private Button przyciskPasuj;
    @FXML private Button przyciskPasujG2;
    @FXML private Button przyciskOpcje;
    @FXML private StackPane panelInfoPas;
    @FXML private ImageView pasGrafikaImageView;
    @FXML private Label pasInfoLabel;

    @FXML private StackPane panelUmiejetnosciPotworow;
    @FXML private ImageView grafikaUmiejetnosciPotworowImageView;
    @FXML private Label napisUmiejetnosciPotworowLabel;

    @FXML private StackPane panelAktywacjiDowodcy;
    @FXML private ImageView powiekszonaKartaDowodcyImageView;
    @FXML private Button przyciskUzyjUmiejetnosciDowodcy;
    @FXML private Button przyciskAnulujUmiejetnoscDowodcy;

    @FXML private StackPane panelZmianyTury;
    @FXML private ImageView grafikaTuryImageView;
    @FXML private Label napisTuryLabel;
    @FXML private Button przyciskPotwierdzTure;

    // NOWE Pola FXML dla panelu rzutu monetą na planszy
    @FXML private StackPane panelRzutuMonetaOverlay;
    @FXML private ImageView monetaDoRzutuImageView;
    @FXML private Label infoRzutuLabel;

    @FXML private StackPane panelWynikuRundy;
    @FXML private ImageView grafikaWynikuRundyImageView;
    @FXML private Label napisWynikuRundyLabel;
    @FXML private Button przyciskDalejPoWynikuRundy;

    @FXML private StackPane panelKoncaGry;
    @FXML private ImageView grafikaKoncaGryImageView;
    @FXML private Label napisKoncowyLabel;
    @FXML private VBox wynikiRundKontener; // Kontener na labele z wynikami rund
    @FXML private Button przyciskRewanz;
    @FXML private Button przyciskMenuGlowneZKoncaGry;

    @FXML private StackPane panelCmentarza;
    @FXML private Label tytulPaneluCmentarza;
    @FXML private ScrollPane cmentarzScrollPaneListyKart;
    @FXML private FlowPane cmentarzListaKartFlowPane;
    @FXML private ImageView cmentarzPowiekszonaKartaImageView;
    @FXML private Label cmentarzOpisPowiekszonejKartyLabel; // Opcjonalnie, dla opisu dużej karty
    @FXML private Button przyciskZamknijCmentarz;
    @FXML private Button przyciskWskrzesKarteZCmentarza;

    @FXML private StackPane panelPodgladuKartCesarza;
    @FXML private Label tytulPaneluPodgladuCesarza;
    @FXML private HBox kontenerKartPodgladuCesarza;

    @FXML private StackPane panelWyboruKartyEredina;
    @FXML private Label tytulPaneluWyboruKartyEredina;
    @FXML private FlowPane flowPaneKartDoWyboruEredina;
    @FXML private ImageView powiekszonaKartaWyboruEredina;
    // @FXML private Label opisPowiekszonejKartyWyboruEredina; // Opcjonalnie
    @FXML private Button przyciskPotwierdzWyborEredina;
    @FXML private Button przyciskAnulujWyborEredina;

    @FXML private StackPane panelWyboruOdrzucanychEredin414;
    @FXML private Label tytulPaneluOdrzucanychEredin414;
    @FXML private FlowPane flowPaneOdrzucanychEredin414;
    @FXML private ImageView powiekszonaKartaOdrzucanaEredin414;
    @FXML private Label infoWyboruOdrzucanychEredin414;
    @FXML private Button przyciskPotwierdzOdrzucenieEredin414;
    @FXML private Button przyciskAnulujOdrzucenieEredin414;

    @FXML private StackPane panelWyboruDobieranejEredin414;
    @FXML private Label tytulPaneluDobieranejEredin414;
    @FXML private FlowPane flowPaneDobieranychEredin414;
    @FXML private ImageView powiekszonaKartaDobieranaEredin414;
    @FXML private Button przyciskPotwierdzDobranieEredin414;
    @FXML private Button przyciskAnulujDobranieEredin414;

    @FXML private StackPane panelWyboruRozpoczynajacegoScoiatael;
    @FXML private Label tytulPaneluScoiatael;
    @FXML private Button przyciskScoiataelJaZaczynam;
    @FXML private Button przyciskScoiataelPrzeciwnikZaczyna;

    @FXML private StackPane panelOpcjiGry;
    @FXML private Button przyciskOpcjeKontynuuj;
    @FXML private Button przyciskOpcjeRestart;
    @FXML private Button przyciskOpcjePowrotDoMenu;

    @FXML private StackPane panelMulligan;
    @FXML private Label mulliganInfoLabel;
    @FXML private Label mulliganCounterLabel;
    @FXML private FlowPane mulliganHandPane;
    @FXML private Button mulliganConfirmButton;

    private Gracz graczDecydujacyScoiatael = null;
    private List<Karta> kartyWybraneDoWymiany = new ArrayList<>();
    private List<Karta> aktualnieWybraneDoOdrzuceniaEredin414 = new ArrayList<>();
    private Karta aktualnieWybranaDoPowiekszeniaEredin414 = null; // Dla obu paneli (podgląd)
    private Karta wybranaKartaZTaliiDlaEredina414 = null;
    private Karta wybranaKartaPrzezEredinaDoPodgladu = null;
    private boolean trybWyboruKartyDlaEmhyra = false;
    private boolean trybWskrzeszaniaAktywnyGlobalnie = false;
    private Gracz graczDokonujacyWskrzeszeniaGlobalnie = null;
    private boolean aktualnieWybieranyRzadPoWskrzeszeniu = false;
    private Karta kartaPodgladanaZCmentarza = null;
    private Image obrazekPassPng;
    private Image obrazekWygranaRundyPng;
    private Image obrazekRemisRundyPng;
    private Image obrazekKoniecWygranaPng;
    private Image obrazekKoniecPrzegranaPng; // Na wypadek, gdybyś chciał różne dla P1 i P2
    private Image obrazekKoniecRemisPng;
    // Pola do logiki rzutu monetą (przeniesione i dostosowane)
    private Image coin1ImageRzut; // Użyj innych nazw, aby uniknąć konfliktu, jeśli masz globalne coin1Image
    private Image coin2ImageRzut;
    private final Random randomDoRzutu = new Random();
    private Timeline animacjaMonetyNaPlanszy;
    private Gracz wylosowanyGraczRozpoczynajacy = null; // Kto faktycznie zaczął po rzucie na planszy
    private Image obrazekPowiadPotwoPng;
    private PixelGwintAplikacja pixelGwintAplikacja;
    private Gracz obiektGracz1;
    private Gracz obiektGracz2;
    private SilnikGry silnikGry;
    private List<Karta> wszystkieKartyWGrzeCache;
    private Karta aktualnieZaznaczonaKartaDoZagrnia = null;
    private List<Node> podswietloneCele = new ArrayList<>();
    private static final double SZEROKOSC_KARTY_W_PANELU_CESARZA = 100.0; // Dostosuj wg potrzeb
    private static final double WYSOKOSC_KARTY_W_PANELU_CESARZA = 145.0;  // Dostosuj wg potrzeb
    private final DropShadow efektObramowkiHover = new DropShadow(10, Color.rgb(255, 255, 100, 0.9));
    private final DropShadow decoyTargetGlowEffect = new DropShadow(25, Color.rgb(173, 216, 230, 0.95)); // Jasnoniebieska poświata (promień, kolor, intensywność)
    private final String SCIEZKA_ZETON_CZERWONY = "/grafiki/elementy_ui/zetonczerw.png";
    private final String SCIEZKA_ZETON_CZARNY = "/grafiki/elementy_ui/zetonczarn.png";
    private final String SCIEZKA_INFO_TURA_G1 = "infoturag1.png"; // Upewnij się, że plik jest w /grafiki/elementy_ui/
    private final String SCIEZKA_INFO_TURA_G2 = "infoturag2.png"; // Upewnij się, że plik jest w /grafiki/elementy_ui/
    private final String SCIEZKA_REWERSU_DOWODCY = "/grafiki/karty/rewers_ogolny.png"; // Upewnij się, że ten plik istnieje!

    private boolean trybWyboruCeluDlaManekina = false;

    private static final double STANDARDOWA_WYSOKOSC_KARTY = 116.0;
    private static final double STANDARDOWA_SZEROKOSC_KARTY = 80.0;

    public Image getObrazekWygranaRundyPng() { return obrazekWygranaRundyPng; }
    public Image getObrazekRemisRundyPng() { return obrazekRemisRundyPng; }

    @FXML
    public void initialize() {
        System.out.println("KontrolerPlanszyGry zainicjalizowany.");
        if (rekaGracza1HBox != null) rekaGracza1HBox.setAlignment(Pos.CENTER);
        if (rekaGracza2HBox != null) rekaGracza2HBox.setAlignment(Pos.CENTER);
        if (pogodaHBox != null) pogodaHBox.setAlignment(Pos.CENTER_LEFT);
        HBox[] rzedy = {rzadPiechotyG1HBox, rzadStrzeleckiG1HBox, rzadOblezeniaG1HBox, rzadPiechotyG2HBox, rzadStrzeleckiG2HBox, rzadOblezeniaG2HBox};
        for(HBox rzad : rzedy) {
            if(rzad != null) rzad.setAlignment(Pos.CENTER);
        }

        if (panelAktywacjiDowodcy != null) panelAktywacjiDowodcy.setVisible(false);
        if (panelZmianyTury != null) panelZmianyTury.setVisible(false);
        if (panelCmentarza != null) panelCmentarza.setVisible(false);
        List<HBox> klikalneRzedy = Arrays.asList(
                rzadPiechotyG1HBox, rzadStrzeleckiG1HBox, rzadOblezeniaG1HBox,
                rzadPiechotyG2HBox, rzadStrzeleckiG2HBox, rzadOblezeniaG2HBox);
        for(HBox rzad : klikalneRzedy) {
            if(rzad != null) rzad.setOnMouseClicked(this::handleCelKlikniety);
        }
        List<StackPane> klikalneSlotyRogu = Arrays.asList(
                rogPiechotyG1Slot, rogStrzeleckiG1Slot, rogOblezeniaG1Slot,
                rogPiechotyG2Slot, rogStrzeleckiG2Slot, rogOblezeniaG2Slot
        );
        for(StackPane slotRogu : klikalneSlotyRogu) {
            if(slotRogu != null) slotRogu.setOnMouseClicked(this::handleCelKlikniety);
        }
        if (panelZmianyTury == null) {
            System.err.println("--------------------------------------------------------------------------");
            System.err.println("FATALNY BŁĄD FXML: panelZmianyTury jest null w KontrolerPlanszyGry.initialize()!");
            System.err.println("Sprawdź plik ekran-planszy-gry.fxml pod kątem poprawności fx:id=\"panelZmianyTury\"");
            System.err.println("oraz deklarację @FXML private StackPane panelZmianyTury; w kontrolerze.");
            System.err.println("--------------------------------------------------------------------------");
        } else {
            System.out.println("[KONTROLER PLANSZY initialize()] pole panelZmianyTury zostało poprawnie wstrzyknięte.");
        }
        coin1ImageRzut = ladujObrazekZElementowUI("coin1.png");
        coin2ImageRzut = ladujObrazekZElementowUI("coin2.png");
        if (panelRzutuMonetaOverlay == null) System.err.println("FATAL FXML: panelRzutuMonetaOverlay jest null w initialize()!");
        if (monetaDoRzutuImageView == null) System.err.println("FATAL FXML: monetaDoRzutuImageView jest null w initialize()!");
        if (infoRzutuLabel == null) System.err.println("FATAL FXML: infoRzutuLabel jest null w initialize()!");

        if (monetaDoRzutuImageView != null && coin1ImageRzut != null) {
            monetaDoRzutuImageView.setImage(coin1ImageRzut);
        } else if (infoRzutuLabel != null) {
            infoRzutuLabel.setText("Błąd: Brak obrazków monet!");
        }
        obrazekPassPng = ladujObrazekZElementowUI("pass.png");
        if (panelInfoPas == null) System.err.println("FATAL FXML: panelInfoPas jest null w initialize()!");
        if (pasGrafikaImageView == null) System.err.println("FATAL FXML: pasGrafikaImageView jest null w initialize()!");
        if (pasInfoLabel == null) System.err.println("FATAL FXML: pasInfoLabel jest null w initialize()!");

        if (pasGrafikaImageView != null && obrazekPassPng != null) {
            // Możesz tu ustawić obrazek od razu, albo dopiero w metodzie pokazującej panel
            // pasGrafikaImageView.setImage(obrazekPassPng);
        } else if (pasGrafikaImageView != null) {
            System.err.println("Nie udało się załadować grafiki pass.png dla pasGrafikaImageView");
        }
        obrazekWygranaRundyPng = ladujObrazekZElementowUI("wygrana.png");
        obrazekRemisRundyPng = ladujObrazekZElementowUI("remis.png");
        if (panelWynikuRundy == null) System.err.println("FATAL FXML: panelWynikuRundy jest null w initialize()!");
        if (grafikaWynikuRundyImageView == null) System.err.println("FATAL FXML: grafikaWynikuRundyImageView jest null w initialize()!");
        if (napisWynikuRundyLabel == null) System.err.println("FATAL FXML: napisWynikuRundyLabel jest null w initialize()!");
        if (przyciskDalejPoWynikuRundy == null) System.err.println("INFO FXML: przyciskDalejPoWynikuRundy jest null (może być opcjonalny).");

        obrazekKoniecWygranaPng = ladujObrazekZElementowUI("koniecwygr.png");
        obrazekKoniecPrzegranaPng = ladujObrazekZElementowUI("koniecprzeg.png"); // Jeśli masz osobną
        obrazekKoniecRemisPng = ladujObrazekZElementowUI("koniecremis.png");

        if (panelKoncaGry == null) System.err.println("FATAL FXML: panelKoncaGry jest null!");
        if (grafikaKoncaGryImageView == null) System.err.println("FATAL FXML: grafikaKoncaGryImageView jest null!");
        if (napisKoncowyLabel == null) System.err.println("FATAL FXML: napisKoncowyLabel jest null!");
        if (wynikiRundKontener == null) System.err.println("FATAL FXML: wynikiRundKontener jest null!");
        if (przyciskRewanz == null) System.err.println("FATAL FXML: przyciskRewanz jest null!");
        if (przyciskMenuGlowneZKoncaGry == null) System.err.println("FATAL FXML: przyciskMenuGlowneZKoncaGry jest null!");

        if (cmentarzGracza1ImageView != null) {
            cmentarzGracza1ImageView.setOnMouseClicked(this::handleCmentarzG1Clicked);
            cmentarzGracza1ImageView.setCursor(Cursor.HAND);
        }
        if (cmentarzGracza2ImageView != null) {
            cmentarzGracza2ImageView.setOnMouseClicked(this::handleCmentarzG2Clicked);
            cmentarzGracza2ImageView.setCursor(Cursor.HAND);
        }

        if (panelCmentarza == null) System.err.println("FATAL FXML: panelCmentarza jest null!");
        if (tytulPaneluCmentarza == null) System.err.println("FATAL FXML: tytulPaneluCmentarza jest null!");
        if (cmentarzScrollPaneListyKart == null) System.err.println("FATAL FXML: cmentarzScrollPaneListyKart jest null!");
        if (cmentarzListaKartFlowPane == null) System.err.println("FATAL FXML: cmentarzListaKartFlowPane jest null!");
        if (cmentarzPowiekszonaKartaImageView == null) System.err.println("FATAL FXML: cmentarzPowiekszonaKartaImageView jest null!");
        if (cmentarzOpisPowiekszonejKartyLabel == null) System.err.println("INFO FXML: cmentarzOpisPowiekszonejKartyLabel jest null (opcjonalne).");
        if (przyciskZamknijCmentarz == null) System.err.println("FATAL FXML: przyciskZamknijCmentarz jest null!");
        // USUNIĘTE: Sprawdzenie dla przyciskWskrzesKarteZCmentarza
        if (panelPodgladuKartCesarza != null) {
            panelPodgladuKartCesarza.setVisible(false);
        }
        obrazekPowiadPotwoPng = ladujObrazekZElementowUI("powiadpotwo.png");
        if (obrazekPowiadPotwoPng == null) {
            System.err.println("Nie udało się załadować grafiki: powiadpotwo.png");
        }

        if (panelUmiejetnosciPotworow != null) {
            panelUmiejetnosciPotworow.setVisible(false);
        } else {
            System.err.println("FATAL FXML: panelUmiejetnosciPotworow jest null w KontrolerPlanszyGry.initialize()!");
        }
        if (grafikaUmiejetnosciPotworowImageView == null) System.err.println("FATAL FXML: grafikaUmiejetnosciPotworowImageView jest null!");
        if (napisUmiejetnosciPotworowLabel == null) System.err.println("FATAL FXML: napisUmiejetnosciPotworowLabel jest null!");
        if (panelWyboruKartyEredina != null) {
            panelWyboruKartyEredina.setVisible(false);
            if(przyciskPotwierdzWyborEredina != null) przyciskPotwierdzWyborEredina.setDisable(true);
        } else {
            System.err.println("FATAL FXML: panelWyboruKartyEredina nie został wstrzyknięty!");
        }
        if (panelWyboruOdrzucanychEredin414 != null) panelWyboruOdrzucanychEredin414.setVisible(false);
        else System.err.println("FATAL FXML: panelWyboruOdrzucanychEredin414 jest null!");
        if (panelWyboruDobieranejEredin414 != null) panelWyboruDobieranejEredin414.setVisible(false);
        else System.err.println("FATAL FXML: panelWyboruDobieranejEredin414 jest null!");

        if (panelWyboruRozpoczynajacegoScoiatael != null) {
            panelWyboruRozpoczynajacegoScoiatael.setVisible(false);
        } else {
            System.err.println("FATAL FXML: panelWyboruRozpoczynajacegoScoiatael jest null!");
        }
        if (panelOpcjiGry != null) {
            panelOpcjiGry.setVisible(false);
        } else {
            System.err.println("FATAL FXML: panelOpcjiGry jest null!");
        }
        if (pogodaHBox != null) {
            pogodaHBox.setOnMouseClicked(this::handleCelKlikniety);
            System.out.println("[KONTROLER INIT] Ustawiono handleCelKlikniety dla pogodaHBox."); // Log dla potwierdzenia
        } else {
            System.err.println("[KONTROLER INIT BŁĄD] pogodaHBox jest null! Nie można ustawić handlera kliknięcia.");
        }
        panelMulligan.setVisible(false);
    }

    @FXML
    private void handleCmentarzG1Clicked(MouseEvent event) {
        if (!trybWskrzeszaniaAktywnyGlobalnie && obiektGracz1 != null && obiektGracz1.getProfilUzytkownika() != null) {
            // STARE WYWOŁANIE:
            // pokazPanelCmentarza(obiektGracz1, "Cmentarz: " + obiektGracz1.getProfilUzytkownika().getNazwaUzytkownika(), false);
            // NOWE WYWOŁANIE:
            pokazPanelCmentarza(obiektGracz1, "Cmentarz: " + obiektGracz1.getProfilUzytkownika().getNazwaUzytkownika(), TrybPaneluCmentarzaKontekst.PODGLAD);
        }
    }

    @FXML
    private void handleCmentarzG2Clicked(MouseEvent event) {
        if (!trybWskrzeszaniaAktywnyGlobalnie && obiektGracz2 != null && obiektGracz2.getProfilUzytkownika() != null) {
            // STARE WYWOŁANIE:
            // pokazPanelCmentarza(obiektGracz2, "Cmentarz: " + obiektGracz2.getProfilUzytkownika().getNazwaUzytkownika(), false);
            // NOWE WYWOŁANIE:
            pokazPanelCmentarza(obiektGracz2, "Cmentarz: " + obiektGracz2.getProfilUzytkownika().getNazwaUzytkownika(), TrybPaneluCmentarzaKontekst.PODGLAD);
        }
    }

    public void pokazTymczasowyPanelZKartami(List<Karta> kartyDoPokazania, Duration czasPokazu, String tytul) {
        if (kartyDoPokazania == null || kartyDoPokazania.isEmpty() || panelPodgladuKartCesarza == null || kontenerKartPodgladuCesarza == null || tytulPaneluPodgladuCesarza == null) {
            System.err.println("[KONTROLER] Nie można pokazać panelu podglądu kart Cesarza - brak kart lub elementów UI.");
            return;
        }

        tytulPaneluPodgladuCesarza.setText(tytul);
        kontenerKartPodgladuCesarza.getChildren().clear(); // Wyczyść poprzednie karty

        System.out.println("[KONTROLER] Pokazuję na panelu Cesarza karty: " + kartyDoPokazania.stream().map(Karta::getNazwa).collect(Collectors.joining(", ")));

        for (Karta karta : kartyDoPokazania) {
            ImageView widokKarty = stworzWidokKarty(karta, SZEROKOSC_KARTY_W_PANELU_CESARZA, WYSOKOSC_KARTY_W_PANELU_CESARZA);
            // Możesz dodać prosty podgląd przy najechaniu, jeśli chcesz, ale panel jest tymczasowy
            final Karta finalKarta = karta; // Dla lambdy
            widokKarty.setOnMouseEntered(event -> {
                // Można by pokazać duży podgląd w głównym slocie podglądu, jeśli jest wolny
                // if (podgladKartyImageView != null) podgladKartyImageView.setImage(ladujObrazekKarty(finalKarta));
            });
            widokKarty.setOnMouseExited(event -> {
                // if (podgladKartyImageView != null) podgladKartyImageView.setImage(null);
            });
            kontenerKartPodgladuCesarza.getChildren().add(widokKarty);
        }

        ukryjWszystkiePaneleOverlayOprocz(panelPodgladuKartCesarza); // Schowaj inne panele
        panelPodgladuKartCesarza.setVisible(true);
        panelPodgladuKartCesarza.toFront();

        PauseTransition pauza = new PauseTransition(czasPokazu);
        pauza.setOnFinished(event -> {
            panelPodgladuKartCesarza.setVisible(false);
            kontenerKartPodgladuCesarza.getChildren().clear(); // Wyczyść karty po zniknięciu
            System.out.println("[KONTROLER] Panel podglądu kart Cesarza zniknął.");
            // Nie ma potrzeby specjalnego odświeżania całej planszy tutaj,
            // bo ta akcja nie zmieniała stanu gry na planszy.
            // Ewentualne ogólne odświeżenie na koniec tury w Silniku Gry wystarczy.
        });
        pauza.play();
    }

    public void poprosOWyborRzeduDlaKarty(Karta karta, Gracz aktywnyGracz, boolean forResurrectionPlacement) {
        if (karta == null || aktywnyGracz == null) {
            System.err.println("[KONTROLER] poprosOWyborRzeduDlaKarty: karta lub aktywnyGracz jest null.");
            return;
        }
        this.aktualnieZaznaczonaKartaDoZagrnia = karta;
        this.aktualnieWybieranyRzadPoWskrzeszeniu = forResurrectionPlacement; // Kluczowe dla handleCelKlikniety

        System.out.println("[KONTROLER] Prośba o wybór rzędu dla karty: " + karta.getNazwa() +
                (forResurrectionPlacement ? " (PO WSKRZESZENIU)" : " (Z RĘKI)"));

        usunPodswietlenieRzedowISlotow();

        // Dla wskrzeszania, karta zawsze trafia na planszę gracza wskrzeszającego (chyba że to szpieg - obsłużone w silniku)
        // Dla kart z ręki - Szpieg idzie na planszę przeciwnika, reszta na własną.
        PlanszaGracza planszaDocelowaModel;
        HBox[] rzedyPlanszyDocelowejUI;

        boolean czySzpiegZreki = !forResurrectionPlacement && karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");

        if (czySzpiegZreki) {
            rzedyPlanszyDocelowejUI = (aktywnyGracz == obiektGracz1) ?
                    new HBox[]{rzadPiechotyG2HBox, rzadStrzeleckiG2HBox, rzadOblezeniaG2HBox} :
                    new HBox[]{rzadPiechotyG1HBox, rzadStrzeleckiG1HBox, rzadOblezeniaG1HBox};
        } else { // Karta na własną planszę (z ręki nie-szpieg LUB wskrzeszana nie-szpieg)
            rzedyPlanszyDocelowejUI = (aktywnyGracz == obiektGracz1) ?
                    new HBox[]{rzadPiechotyG1HBox, rzadStrzeleckiG1HBox, rzadOblezeniaG1HBox} :
                    new HBox[]{rzadPiechotyG2HBox, rzadStrzeleckiG2HBox, rzadOblezeniaG2HBox};
        }

        List<TypRzeduEnum> mozliweTypyRzedow = new ArrayList<>();
        boolean czyKartaMaZrecznosc = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Zręczność");
        String pozycjaKarty = karta.getPozycja() != null ? karta.getPozycja().toLowerCase() : "";

        if (czyKartaMaZrecznosc) {
            TypRzeduEnum typRzedu1 = TypRzeduEnum.fromStringPozycjiKarty(karta.getPozycja());
            if (typRzedu1 != null) mozliweTypyRzedow.add(typRzedu1);

            String pozycja2 = karta.getPozycja2();
            if (pozycja2 != null && !pozycja2.trim().isEmpty() && !pozycja2.equalsIgnoreCase("N/D")) {
                TypRzeduEnum typRzedu2 = TypRzeduEnum.fromStringPozycjiKarty(pozycja2);
                if (typRzedu2 != null && !mozliweTypyRzedow.contains(typRzedu2)) mozliweTypyRzedow.add(typRzedu2);
            }
        } else if (pozycjaKarty.contains("dowolne")) {
            mozliweTypyRzedow.add(TypRzeduEnum.PIECHOTA);
            mozliweTypyRzedow.add(TypRzeduEnum.STRZELECKIE);
            mozliweTypyRzedow.add(TypRzeduEnum.OBLEZENIE);
        } else {
            TypRzeduEnum typRzedu = TypRzeduEnum.fromStringPozycjiKarty(karta.getPozycja());
            if (typRzedu != null) mozliweTypyRzedow.add(typRzedu);
        }

        if (mozliweTypyRzedow.isEmpty()) {
            wyswietlKomunikatNaPlanszy("Karta " + karta.getNazwa() + " nie ma określonej prawidłowej pozycji do zagrania.", true);
            if (forResurrectionPlacement && silnikGry != null) {
                silnikGry.anulujWskrzeszanie(aktywnyGracz); // Anuluj, jeśli wskrzeszana karta nie ma gdzie pójść
            }
            this.aktualnieZaznaczonaKartaDoZagrnia = null;
            this.aktualnieWybieranyRzadPoWskrzeszeniu = false;
            return;
        }

        System.out.print("    > Podświetlanie rzędów typu dla " + karta.getNazwa() + ": ");
        for (TypRzeduEnum typRzedu : mozliweTypyRzedow) {
            System.out.print(typRzedu.name() + " ");
            switch (typRzedu) {
                case PIECHOTA:    if (rzedyPlanszyDocelowejUI.length > 0) podswietlElement(rzedyPlanszyDocelowejUI[0]); break;
                case STRZELECKIE: if (rzedyPlanszyDocelowejUI.length > 1) podswietlElement(rzedyPlanszyDocelowejUI[1]); break;
                case OBLEZENIE:   if (rzedyPlanszyDocelowejUI.length > 2) podswietlElement(rzedyPlanszyDocelowejUI[2]); break;
            }
        }
        System.out.println();
    }

    public void pokazPanelCmentarza(Gracz graczWlascicielCmentarza, String tytul, TrybPaneluCmentarzaKontekst tryb) {
        if (graczWlascicielCmentarza == null || panelCmentarza == null || tytulPaneluCmentarza == null ||
                cmentarzListaKartFlowPane == null || cmentarzPowiekszonaKartaImageView == null ||
                przyciskZamknijCmentarz == null || przyciskWskrzesKarteZCmentarza == null) {
            System.err.println("Błąd: Nie można pokazać panelu cmentarza - brak gracza lub kluczowych elementów UI.");
            if (silnikGry != null) {
                Gracz graczDoAnulowaniaAkcji = null;
                switch (tryb) {
                    case WSKRZESZANIE_MEDYK:
                        graczDoAnulowaniaAkcji = graczWlascicielCmentarza; // Ten, kto użył medyka
                        if (graczDoAnulowaniaAkcji != null) silnikGry.anulujWskrzeszanie(graczDoAnulowaniaAkcji);
                        break;
                    case EMHYR_PAN_POLUDNIA:
                        // graczWlascicielCmentarza to przeciwnik. Anulowanie dotyczy gracza aktywującego Emhyra.
                        if(silnikGry.getGraczAktywujacyEmhyra() != null) silnikGry.anulujWyborDlaEmhyra();
                        else silnikGry.anulujWyborDlaEmhyra();
                        break;
                    case EREDIN_ZABOJCA_AUBERONA_415:
                        graczDoAnulowaniaAkcji = graczWlascicielCmentarza; // Ten, kto użył Eredina
                        if(graczDoAnulowaniaAkcji != null) silnikGry.anulujZdolnoscEredina415(true);
                        break;
                    default: break;
                }
            }
            resetStatePoOperacjiNaCmentarzu();
            return;
        }

        this.aktualnyTrybPaneluCmentarza = tryb;
        this.kartaPodgladanaZCmentarza = null;

        this.trybWskrzeszaniaAktywnyGlobalnie = (tryb == TrybPaneluCmentarzaKontekst.WSKRZESZANIE_MEDYK);
        this.graczDokonujacyWskrzeszeniaGlobalnie = this.trybWskrzeszaniaAktywnyGlobalnie ? graczWlascicielCmentarza : null;

        tytulPaneluCmentarza.setText(tytul);
        cmentarzListaKartFlowPane.getChildren().clear();
        cmentarzPowiekszonaKartaImageView.setImage(null);
        if (cmentarzOpisPowiekszonejKartyLabel != null) cmentarzOpisPowiekszonejKartyLabel.setText("Wybierz kartę z listy...");

        List<Karta> kartyDoWyswietlenia = new ArrayList<>(graczWlascicielCmentarza.getOdrzucone());
        boolean czyJestCoWybrac = !kartyDoWyswietlenia.isEmpty();
        String tekstPrzyciskuAkcji = "";

        switch (tryb) {
            case WSKRZESZANIE_MEDYK:
                kartyDoWyswietlenia = kartyDoWyswietlenia.stream()
                        .filter(k -> k.getTyp() == TypKartyEnum.JEDNOSTKA)
                        .collect(Collectors.toList());
                czyJestCoWybrac = !kartyDoWyswietlenia.isEmpty();
                if (!czyJestCoWybrac && cmentarzOpisPowiekszonejKartyLabel != null) {
                    cmentarzOpisPowiekszonejKartyLabel.setText("Brak jednostek na cmentarzu do wskrzeszenia.");
                }
                tekstPrzyciskuAkcji = "Wskrześ Wybraną";
                break;
            case EMHYR_PAN_POLUDNIA:
                if (!czyJestCoWybrac && cmentarzOpisPowiekszonejKartyLabel != null) {
                    cmentarzOpisPowiekszonejKartyLabel.setText("Cmentarz przeciwnika jest pusty.");
                }
                tekstPrzyciskuAkcji = "Wybierz dla Emhyra";
                break;
            case EREDIN_ZABOJCA_AUBERONA_415:
                if (!czyJestCoWybrac && cmentarzOpisPowiekszonejKartyLabel != null) {
                    cmentarzOpisPowiekszonejKartyLabel.setText("Twój cmentarz jest pusty.");
                }
                tekstPrzyciskuAkcji = "Weź na Rękę";
                break;
            case PODGLAD:
            default:
                if (!czyJestCoWybrac && cmentarzOpisPowiekszonejKartyLabel != null) {
                    cmentarzOpisPowiekszonejKartyLabel.setText("Cmentarz jest pusty.");
                }
                break;
        }
        if(przyciskWskrzesKarteZCmentarza != null) przyciskWskrzesKarteZCmentarza.setText(tekstPrzyciskuAkcji);

        Collections.reverse(kartyDoWyswietlenia);

        if (kartyDoWyswietlenia.isEmpty() && tryb == TrybPaneluCmentarzaKontekst.PODGLAD) {
            Label placeholder = new Label("Cmentarz jest pusty.");
            placeholder.setTextFill(Color.LIGHTSLATEGRAY);
            placeholder.setStyle("-fx-font-size: 16px;");
            cmentarzListaKartFlowPane.getChildren().add(placeholder);
        } else if (!kartyDoWyswietlenia.isEmpty()) {
            for (Karta kartaZCmentarza : kartyDoWyswietlenia) {
                ImageView miniatura = stworzWidokKarty(kartaZCmentarza, 70, 100);
                miniatura.setUserData(kartaZCmentarza);
                miniatura.setCursor(Cursor.HAND);
                miniatura.setOnMouseClicked(event -> handleCmentarzMiniaturaKartyKliknieta(kartaZCmentarza));

                DropShadow hoverEffect = new DropShadow(10, Color.GOLD);
                miniatura.setOnMouseEntered(e -> {
                    if (kartaPodgladanaZCmentarza != kartaZCmentarza) miniatura.setEffect(hoverEffect);
                });
                miniatura.setOnMouseExited(e -> {
                    if (kartaPodgladanaZCmentarza != kartaZCmentarza) miniatura.setEffect(null);
                });
                cmentarzListaKartFlowPane.getChildren().add(miniatura);
            }
            if (czyJestCoWybrac && tryb != TrybPaneluCmentarzaKontekst.PODGLAD && cmentarzPowiekszonaKartaImageView.getImage() == null) {
                if (!kartyDoWyswietlenia.isEmpty()) {
                    handleCmentarzMiniaturaKartyKliknieta(kartyDoWyswietlenia.get(0));
                }
            }
        }

        boolean czyTrybAkcji = (tryb != TrybPaneluCmentarzaKontekst.PODGLAD);
        if (przyciskWskrzesKarteZCmentarza != null) {
            przyciskWskrzesKarteZCmentarza.setVisible(czyTrybAkcji && czyJestCoWybrac);
            przyciskWskrzesKarteZCmentarza.setManaged(czyTrybAkcji && czyJestCoWybrac);
            przyciskWskrzesKarteZCmentarza.setDisable(true);
        }

        ukryjWszystkiePaneleOverlayOprocz(panelCmentarza);
        panelCmentarza.setVisible(true);
        panelCmentarza.toFront();
    }
    private void handleCmentarzMiniaturaKartyKliknieta(Karta karta) {
        if (karta == null || cmentarzPowiekszonaKartaImageView == null) return;

        cmentarzPowiekszonaKartaImageView.setImage(ladujObrazekKarty(karta));
        this.kartaPodgladanaZCmentarza = karta;

        for(Node node : cmentarzListaKartFlowPane.getChildren()){
            if(node instanceof ImageView){
                node.setEffect(node.getUserData() == karta ? new DropShadow(15, Color.SKYBLUE) : null);
            }
        }

        if (cmentarzOpisPowiekszonejKartyLabel != null) {
            String opis = karta.getNazwa() + " (Siła: " + karta.getPunktySily() +
                    (karta.getTyp() != null ? ", Typ: " + karta.getTyp().getNazwaWyswietlana() : "") + ")";
            if (karta.getUmiejetnosc() != null && !karta.getUmiejetnosc().trim().isEmpty() && !karta.getUmiejetnosc().equalsIgnoreCase("Brak")) {
                opis += "\nUmiejętność: " + karta.getUmiejetnosc();
            }
            cmentarzOpisPowiekszonejKartyLabel.setText(opis);
        }


        // Aktywuj przycisk akcji w zależności od trybu i wybranej karty
        if (przyciskWskrzesKarteZCmentarza != null) {
            boolean czyWaznyCel = false;
            if (kartaPodgladanaZCmentarza != null) {
                // TUTAJ JEST WAŻNA LOGIKA
                switch (aktualnyTrybPaneluCmentarza) {
                    case WSKRZESZANIE_MEDYK:
                        czyWaznyCel = (kartaPodgladanaZCmentarza.getTyp() == TypKartyEnum.JEDNOSTKA);
                        break;
                    case EMHYR_PAN_POLUDNIA: // <-- TEN PRZYPADEK TERAZ ZADZIAŁA
                    case EREDIN_ZABOJCA_AUBERONA_415:
                        // Emhyr 69 i Eredin 415 mogą wybrać dowolną kartę
                        czyWaznyCel = true; // <-- Poprawnie ustawia na true
                        break;
                    case PODGLAD:
                    default:
                        czyWaznyCel = false;
                        break;
                }
            }
            przyciskWskrzesKarteZCmentarza.setDisable(!czyWaznyCel);
        }
    }
    @FXML
    private void handleWskrzesKarteZCmentarzaAction() { // Nazwa przycisku może pozostać, ale jego tekst się zmienia
        if (kartaPodgladanaZCmentarza == null || silnikGry == null) {
            System.err.println("Błąd: Brak wybranej karty lub silnika gry do wykonania akcji z cmentarza.");
            panelCmentarza.setVisible(false);
            resetStatePoOperacjiNaCmentarzu();
            return;
        }

        Gracz graczDocelowyDlaAkcji = null; // Gracz, którego dotyczy akcja (kto użył medyka, Emhyra, Eredina)

        switch (aktualnyTrybPaneluCmentarza) {
            case WSKRZESZANIE_MEDYK:
                graczDocelowyDlaAkcji = graczDokonujacyWskrzeszeniaGlobalnie; // Ustawiane w pokazPanelCmentarza
                if (graczDocelowyDlaAkcji == null) { /* obsługa błędu */ return; }
                if (kartaPodgladanaZCmentarza.getTyp() != TypKartyEnum.JEDNOSTKA) {
                    wyswietlKomunikatNaPlanszy("Można wskrzeszać tylko jednostki (nie Bohaterów ani Specjalne).", true);
                    return; // Nie zamykaj panelu, pozwól wybrać inną kartę
                }
                panelCmentarza.setVisible(false);
                silnikGry.rozpocznijProcesWskrzeszaniaKarty(graczDocelowyDlaAkcji, kartaPodgladanaZCmentarza);
                break;

            case EMHYR_PAN_POLUDNIA:
                // Silnik gry przechowuje graczAktywujacyEmhyra
                // Karta jest z cmentarza przeciwnika (przekazana do pokazPanelCmentarza)
                panelCmentarza.setVisible(false);
                silnikGry.wykonajWyborEmhyraPanaPoludnia(kartaPodgladanaZCmentarza);
                break;

            case EREDIN_ZABOJCA_AUBERONA_415:
                graczDocelowyDlaAkcji = silnikGry.getGraczAktywujacyEredina415(); // Potrzebny getter w SilnikGry
                if (graczDocelowyDlaAkcji == null) { /* obsługa błędu */ return; }
                panelCmentarza.setVisible(false);
                silnikGry.wykonajWyborKartyZCmentarzaEredin415(kartaPodgladanaZCmentarza);
                break;

            case PODGLAD:
            default:
                System.out.println("Panel cmentarza w trybie podglądu, przycisk akcji nie powinien być aktywny.");
                panelCmentarza.setVisible(false); // Po prostu zamknij
                break;
        }
        resetStatePoOperacjiNaCmentarzu(); // Zawsze resetuj stan kontrolera po akcji
    }

    private void resetStatePoOperacjiNaCmentarzu() {
        this.trybWskrzeszaniaAktywnyGlobalnie = false;
        this.graczDokonujacyWskrzeszeniaGlobalnie = null;
        // Usunęliśmy trybWyboruKartyDlaEmhyra z kontrolera, silnik tym zarządza
        this.kartaPodgladanaZCmentarza = null;
        this.aktualnyTrybPaneluCmentarza = TrybPaneluCmentarzaKontekst.PODGLAD; // Reset do domyślnego trybu

        if (przyciskWskrzesKarteZCmentarza != null) {
            przyciskWskrzesKarteZCmentarza.setVisible(false);
            przyciskWskrzesKarteZCmentarza.setManaged(false);
            przyciskWskrzesKarteZCmentarza.setDisable(true);
            // Domyślny tekst można ustawić w pokazPanelCmentarza zależnie od trybu
        }
        if(cmentarzListaKartFlowPane != null) {
            for(Node node : cmentarzListaKartFlowPane.getChildren()){
                if(node instanceof ImageView){
                    node.setEffect(null);
                }
            }
        }
        if (cmentarzPowiekszonaKartaImageView != null) cmentarzPowiekszonaKartaImageView.setImage(null);
        if (cmentarzOpisPowiekszonejKartyLabel != null) cmentarzOpisPowiekszonejKartyLabel.setText("Wybierz kartę z listy...");
    }

    @FXML
    private void handleZamknijPanelCmentarzaAction() {
        if (panelCmentarza != null) {
            panelCmentarza.setVisible(false);
        }
        if(cmentarzListaKartFlowPane != null) cmentarzListaKartFlowPane.getChildren().clear();
        if(cmentarzPowiekszonaKartaImageView != null) cmentarzPowiekszonaKartaImageView.setImage(null);
        if(cmentarzOpisPowiekszonejKartyLabel != null) cmentarzOpisPowiekszonejKartyLabel.setText("");

        this.kartaPodgladanaZCmentarza = null;
        // USUNIĘTO: Logika związana z trybem wskrzeszania (trybWskrzeszaniaAktywny, itp.)
    }

    @FXML
    private void handleZamknijCmentarzAction() {
        panelCmentarza.setVisible(false);
        // Poinformuj silnik o anulowaniu, jeśli panel był w trybie interakcji
        if (silnikGry != null) {
            switch (aktualnyTrybPaneluCmentarza) {
                case WSKRZESZANIE_MEDYK:
                    if (graczDokonujacyWskrzeszeniaGlobalnie != null) {
                        silnikGry.anulujWskrzeszanie(graczDokonujacyWskrzeszeniaGlobalnie);
                    }
                    break;
                case EMHYR_PAN_POLUDNIA:
                    silnikGry.anulujWyborDlaEmhyra();
                    break;
                case EREDIN_ZABOJCA_AUBERONA_415:
                    if (silnikGry.getGraczAktywujacyEredina415() != null) { // Użyj gettera
                        silnikGry.anulujZdolnoscEredina415(false); // false = anulowanie przez gracza
                    }
                    break;
                default:
                    // Brak specjalnej akcji anulowania dla trybu PODGLAD
                    break;
            }
        }
        resetStatePoOperacjiNaCmentarzu(); // Zawsze resetuj stan kontrolera
    }

    public void rozpocznijWyborCeluDlaManekina(Gracz graczCelujacy, List<Karta> poprawneCele) {
        if (silnikGry == null || silnikGry.isCzyGraZakonczona() || graczCelujacy == null || poprawneCele == null || poprawneCele.isEmpty()) {
            wyswietlKomunikatNaPlanszy("Brak celów dla Manekina lub błąd stanu.", true);
            if (silnikGry != null && graczCelujacy != null) { // Dodatkowe sprawdzenie graczCelujacy
                silnikGry.anulujZagranieManekina(graczCelujacy, true);
            } else if (silnikGry != null && silnikGry.getGraczUzywajacyManekina() != null) { // Fallback
                silnikGry.anulujZagranieManekina(silnikGry.getGraczUzywajacyManekina(), true);
            }
            resetujStanZaznaczonejKarty(); // To powinno wywołać resetujTrybWyboruCeluManekina
            return;
        }

        this.trybWyboruCeluDlaManekina = true;
        usunPodswietlenieRzedowISlotow(); // Czyści poprzednie style, ale niekoniecznie efekty na kartach
        // jeśli resetujTrybWyboruCeluManekina nie był wywołany poprawnie wcześniej.
        // Dla pewności, wyczyśćmy efekty przed nowym podświetleniem.
        wyczyscEfektyKartNaPlanszyGracza(graczCelujacy);


        System.out.println("[KONTROLER] Rozpoczęto wybór celu dla Manekina przez gracza: " + graczCelujacy.getProfilUzytkownika().getNazwaUzytkownika());

        HBox[] rzedyGracza = (graczCelujacy == obiektGracz1) ?
                new HBox[]{rzadPiechotyG1HBox, rzadStrzeleckiG1HBox, rzadOblezeniaG1HBox} :
                new HBox[]{rzadPiechotyG2HBox, rzadStrzeleckiG2HBox, rzadOblezeniaG2HBox};

        boolean znalezionoCoPodswietlic = false;
        for (HBox rzadHBox : rzedyGracza) {
            if (rzadHBox != null) {
                for (Node nodeKarty : rzadHBox.getChildren()) {
                    if (nodeKarty instanceof ImageView) {
                        ImageView obrazekKarty = (ImageView) nodeKarty;
                        Karta kartaNaPlanszy = (Karta) obrazekKarty.getUserData();

                        if (kartaNaPlanszy != null && poprawneCele.contains(kartaNaPlanszy)) {
                            // Zamiast podswietlElement(obrazekKarty), użyj nowego efektu:
                            obrazekKarty.setEffect(decoyTargetGlowEffect);
                            // Nie dodajemy już do podswietloneCele, bo reset efektów będzie globalny
                            // dla kart na planszy gracza lub przez odswiezCalakolwiekPlansze.

                            obrazekKarty.setCursor(Cursor.HAND);
                            obrazekKarty.setOnMouseClicked(this::handleKartaNaPlanszyKliknietaDlaManekina);
                            znalezionoCoPodswietlic = true;
                        } else {
                            obrazekKarty.setEffect(null); // Upewnij się, że inne karty nie mają efektu
                            obrazekKarty.setCursor(Cursor.DEFAULT);
                            obrazekKarty.setOnMouseClicked(null);
                        }
                    }
                }
            }
        }
        if (!znalezionoCoPodswietlic) {
            wyswietlKomunikatNaPlanszy("Brak jednostek do zamiany z Manekinem!", true);
            if (silnikGry != null && graczCelujacy != null) { // Dodatkowe sprawdzenie
                silnikGry.anulujZagranieManekina(graczCelujacy, true);
            }
            // resetujTrybWyboruCeluManekina() jest częścią resetujStanZaznaczonejKarty(),
            // a anulujZagranieManekina powinno prowadzić do resetu stanu.
        }
    }

    // Dodaj tę metodę pomocniczą do czyszczenia efektów z kart na planszy danego gracza
    private void wyczyscEfektyKartNaPlanszyGracza(Gracz gracz) {
        if (gracz == null) return;

        HBox[] rzedyGraczaDoWyczyszczenia = (gracz == obiektGracz1) ?
                new HBox[]{rzadPiechotyG1HBox, rzadStrzeleckiG1HBox, rzadOblezeniaG1HBox} :
                new HBox[]{rzadPiechotyG2HBox, rzadStrzeleckiG2HBox, rzadOblezeniaG2HBox};

        for (HBox rzadHBox : rzedyGraczaDoWyczyszczenia) {
            if (rzadHBox != null) {
                for (Node nodeKarty : rzadHBox.getChildren()) {
                    if (nodeKarty instanceof ImageView) {
                        ((ImageView) nodeKarty).setEffect(null);
                    }
                }
            }
        }
    }
    private void handleKartaNaPlanszyKliknietaDlaManekina(MouseEvent event) {
        if (!trybWyboruCeluDlaManekina || silnikGry == null) return;

        Object source = event.getSource();
        if (source instanceof ImageView) {
            ImageView kliknietyObrazek = (ImageView) source;
            Karta wybranaJednostka = (Karta) kliknietyObrazek.getUserData();
            Node rodzicObrazka = kliknietyObrazek.getParent(); // To powinien być HBox rzędu

            if (wybranaJednostka != null && wybranaJednostka.getTyp() != TypKartyEnum.BOHATER && rodzicObrazka instanceof HBox) {
                HBox rzadHBox = (HBox) rodzicObrazka;
                int indeksWRzedzie = rzadHBox.getChildren().indexOf(kliknietyObrazek); // Indeks w HBoxie

                // Znajdź odpowiedni obiekt RzadPlanszy na podstawie HBoxu
                RzadPlanszy rzadPochodzenia = null;
                Gracz wlascicielRzedu = null;

                if (rzadHBox == rzadPiechotyG1HBox) { rzadPochodzenia = silnikGry.getGracz1().getPlanszaGry().getRzadPiechoty(); wlascicielRzedu = silnikGry.getGracz1(); }
                else if (rzadHBox == rzadStrzeleckiG1HBox) { rzadPochodzenia = silnikGry.getGracz1().getPlanszaGry().getRzadStrzelecki(); wlascicielRzedu = silnikGry.getGracz1(); }
                else if (rzadHBox == rzadOblezeniaG1HBox) { rzadPochodzenia = silnikGry.getGracz1().getPlanszaGry().getRzadOblezenia(); wlascicielRzedu = silnikGry.getGracz1(); }
                else if (rzadHBox == rzadPiechotyG2HBox) { rzadPochodzenia = silnikGry.getGracz2().getPlanszaGry().getRzadPiechoty(); wlascicielRzedu = silnikGry.getGracz2(); }
                else if (rzadHBox == rzadStrzeleckiG2HBox) { rzadPochodzenia = silnikGry.getGracz2().getPlanszaGry().getRzadStrzelecki(); wlascicielRzedu = silnikGry.getGracz2(); }
                else if (rzadHBox == rzadOblezeniaG2HBox) { rzadPochodzenia = silnikGry.getGracz2().getPlanszaGry().getRzadOblezenia(); wlascicielRzedu = silnikGry.getGracz2(); }

                if (rzadPochodzenia != null && indeksWRzedzie != -1 && silnikGry.getGraczAktualnejTury() == wlascicielRzedu) { // Upewnij się, że gracz klika na swoją kartę
                    System.out.println("[KONTROLER] Wybrano jednostkę '" + wybranaJednostka.getNazwa() + "' dla Manekina w rzędzie " + rzadPochodzenia.getTypRzedu() + " na indeksie " + indeksWRzedzie);

                    // Przekaż informację do silnika gry
                    // Gracz aktualnej tury to ten, kto używa manekina
                    silnikGry.wykonajZamianeManekinem(silnikGry.getGraczAktualnejTury(), wybranaJednostka, rzadPochodzenia, indeksWRzedzie);
                } else {
                    System.err.println("[KONTROLER] Błąd przy wyborze celu dla Manekina - nie znaleziono rzędu lub indeksu, lub próba wyboru karty przeciwnika.");
                }
            } else {
                // Kliknięto na Bohatera lub nieprawidłowy element
                wyswietlKomunikatNaPlanszy("Manekinem można zamienić tylko zwykłą jednostkę (nie Bohatera).", true);
            }
            // Zawsze resetuj tryb wyboru i podświetlenie po kliknięciu
            resetujTrybWyboruCeluManekina();
        }
    }

    private void resetujTrybWyboruCeluManekina() {
        this.trybWyboruCeluDlaManekina = false;
        usunPodswietlenieRzedowISlotow(); // To czyści style (tło, ramki) z listy podswietloneCele

        // Dodatkowe czyszczenie efektów poświaty z kart na planszy gracza,
        // który używał manekina.
        Gracz graczUzywajacyManekina = null;
        if (silnikGry != null) {
            // Spróbuj uzyskać gracza, który faktycznie używał manekina (z silnika)
            // Wcześniej mieliśmy pole this.graczUzywajacyManekina w SilnikGry
            // Jeśli taka referencja jest dostępna przez getter:
            // graczUzywajacyManekina = silnikGry.getGraczUzywajacyManekinaAktualnie(); // Załóżmy, że jest taki getter
            // Jeśli nie, użyjemy gracza aktualnej tury jako przybliżenie,
            // chociaż może to nie być idealne, jeśli tura zdążyła się zmienić.
            // Jednak Manekin operuje na kartach gracza, który go zagrał.
            // Gracz, który używał manekina jest przechowywany w SilnikGry jako this.graczUzywajacyManekina
            // ale potrzebujemy do niego dostępu.
            // Załóżmy na razie, że odswiezCalakolwiekPlansze() na końcu sobie z tym poradzi,
            // lub musimy mieć pewność, który gracz był aktywny.
            // Silnik gry po udanej zamianie manekinem (lub anulowaniu) powinien wywołać odswiezCalakolwiekPlansze,
            // co przebuduje ImageView i efekty znikną.
            // Dla pewności, dodajmy jednak czyszczenie.

            Gracz graczKtoregoKartyCzyscic = silnikGry.getGraczUzywajacyManekina(); // Jeśli masz taki getter w SilnikGry
            if (graczKtoregoKartyCzyscic == null) { // Fallback, jeśli getter nie istnieje lub zwrócił null
                graczKtoregoKartyCzyscic = silnikGry.getGraczAktualnejTury(); // Może nie być poprawne, jeśli tura się zmieniła
                if (graczKtoregoKartyCzyscic == null && obiektGracz1 != null) graczKtoregoKartyCzyscic = obiektGracz1; // Ostateczny fallback
            }

            if (graczKtoregoKartyCzyscic != null) {
                wyczyscEfektyKartNaPlanszyGracza(graczKtoregoKartyCzyscic);
            } else {
                // Jeśli nie możemy ustalić gracza, wyczyśćmy dla obu na wszelki wypadek (choć to mniej optymalne)
                if(obiektGracz1 != null) wyczyscEfektyKartNaPlanszyGracza(obiektGracz1);
                if(obiektGracz2 != null) wyczyscEfektyKartNaPlanszyGracza(obiektGracz2);
            }
        }


        // Przywróć domyślne handlery kliknięć dla kart na planszy (ważne!)
        // Najprościej jest odświeżyć całą planszę, co przywróci standardowe interakcje.
        odswiezCalakolwiekPlansze();
        System.out.println("[KONTROLER] Zresetowano tryb wyboru celu dla Manekina.");
    }

    public void pokazPanelInfoPas(Gracz ktoSpasowal) {
        // Sprawdzamy tylko te elementy, które pozostały (bez przycisku)
        if (panelInfoPas == null || pasGrafikaImageView == null || pasInfoLabel == null || ktoSpasowal == null || ktoSpasowal.getProfilUzytkownika() == null) {
            System.err.println("Błąd: Nie można pokazać panelu informacji o pasie - brak elementów UI lub danych gracza.");
            if (silnikGry != null) {
                silnikGry.kontynuujPoWyswietleniuInfoOPasie();
            }
            return;
        }

        if (obrazekPassPng != null) {
            pasGrafikaImageView.setImage(obrazekPassPng);
        } else {
            pasGrafikaImageView.setImage(null);
            System.err.println("Nie załadowano grafiki pass.png dla pasGrafikaImageView");
        }
        pasInfoLabel.setText(ktoSpasowal.getProfilUzytkownika().getNazwaUzytkownika() + " spasował!");

        // Ukryj inne potencjalnie aktywne panele overlay
        if (panelRzutuMonetaOverlay != null) panelRzutuMonetaOverlay.setVisible(false);
        if (panelZmianyTury != null) panelZmianyTury.setVisible(false);
        if (panelAktywacjiDowodcy != null) panelAktywacjiDowodcy.setVisible(false);

        panelInfoPas.setVisible(true);
        panelInfoPas.toFront();

        // Automatyczne znikanie panelu po określonym czasie
        PauseTransition pauzaInfoPas = new PauseTransition(Duration.seconds(3.0)); // Panel będzie widoczny przez 3 sekundy
        pauzaInfoPas.setOnFinished(event -> {
            if (panelInfoPas != null) {
                panelInfoPas.setVisible(false);
            }
            if (silnikGry != null) {
                silnikGry.kontynuujPoWyswietleniuInfoOPasie(); // Sygnalizuj silnikowi, aby kontynuował
            }
        });
        pauzaInfoPas.play();
    }

    public void pokazPanelKoncaGry(Gracz zwyciezcaGry, boolean czyRemisGry, List<String> historiaRund) {
        if (panelKoncaGry == null || grafikaKoncaGryImageView == null || napisKoncowyLabel == null || wynikiRundKontener == null) {
            System.err.println("Błąd krytyczny: Elementy panelu końca gry nie są zainicjalizowane!");
            return;
        }

        // Ukryj inne aktywne panele overlay
        if (panelRzutuMonetaOverlay != null) panelRzutuMonetaOverlay.setVisible(false);
        if (panelZmianyTury != null) panelZmianyTury.setVisible(false);
        if (panelInfoPas != null) panelInfoPas.setVisible(false);
        if (panelAktywacjiDowodcy != null) panelAktywacjiDowodcy.setVisible(false);
        if (panelWynikuRundy != null) panelWynikuRundy.setVisible(false);

        String komunikatGlowny;
        Image obrazekDoWyswietlenia = null;

        if (czyRemisGry) {
            komunikatGlowny = "REMIS W CAŁEJ PARTII!";
            obrazekDoWyswietlenia = obrazekKoniecRemisPng;
        } else if (zwyciezcaGry != null && zwyciezcaGry.getProfilUzytkownika() != null) {
            komunikatGlowny = "WYGRYWA: " + zwyciezcaGry.getProfilUzytkownika().getNazwaUzytkownika() + "!";
            // Załóżmy, że obiektGracz1 to "główny" gracz z perspektywy UI
            if (zwyciezcaGry == obiektGracz1) {
                obrazekDoWyswietlenia = obrazekKoniecWygranaPng;
            } else { // Wygrał obiektGracz2
                obrazekDoWyswietlenia = obrazekKoniecPrzegranaPng; // Użyj grafiki przegranej, jeśli ją masz
                if (obrazekDoWyswietlenia == null) obrazekDoWyswietlenia = obrazekKoniecWygranaPng; // Fallback na wygraną (np. dla AI)
            }
        } else {
            komunikatGlowny = "GRA ZAKOŃCZONA"; // Sytuacja awaryjna
            System.err.println("Nie udało się jednoznacznie ustalić zwycięzcy ani remisu dla panelu końca gry.");
        }

        napisKoncowyLabel.setText(komunikatGlowny.toUpperCase());
        if (obrazekDoWyswietlenia != null) {
            grafikaKoncaGryImageView.setImage(obrazekDoWyswietlenia);
            grafikaKoncaGryImageView.setVisible(true);
        } else {
            grafikaKoncaGryImageView.setVisible(false);
        }

        // Wyświetlanie historii rund
        wynikiRundKontener.getChildren().clear(); // Wyczyść poprzednie wyniki, jeśli były
        if (historiaRund != null) {
            for (String wynikRundy : historiaRund) {
                Label labelRundy = new Label(wynikRundy);
                labelRundy.setTextFill(Color.LIGHTSTEELBLUE); // Kolor tekstu dla wyników rund
                labelRundy.setStyle("-fx-font-size: 16px; -fx-font-style: italic;");
                wynikiRundKontener.getChildren().add(labelRundy);
            }
        } else {
            wynikiRundKontener.getChildren().add(new Label("Brak historii rund."));
        }

        panelKoncaGry.setVisible(true);
        panelKoncaGry.toFront();
    }

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    public void inicjalizujGre(Gracz gracz1, Gracz gracz2, Gracz graczRozpoczynajacyOtrzymany) { // graczRozpoczynajacyOtrzymany jest zawsze null z PixelGwintAplikacja
        this.obiektGracz1 = gracz1;
        this.obiektGracz2 = gracz2;
        this.wylosowanyGraczRozpoczynajacy = null; // Reset przed podjęciem decyzji

        // Logowanie otrzymanych parametrów
        System.out.println("[KONTROLER PLANSZY (inicjalizujGre)] Otrzymano gracz1: " +
                (gracz1 != null && gracz1.getProfilUzytkownika() != null ? gracz1.getProfilUzytkownika().getNazwaUzytkownika() : (gracz1 == null ? "gracz1 jest NULL" : "profil gracza1 jest NULL")) +
                " (Obiekt ID: " + System.identityHashCode(gracz1) + ")");
        System.out.println("[KONTROLER PLANSZY (inicjalizujGre)] Otrzymano gracz2: " +
                (gracz2 != null && gracz2.getProfilUzytkownika() != null ? gracz2.getProfilUzytkownika().getNazwaUzytkownika() : (gracz2 == null ? "gracz2 jest NULL" : "profil gracza2 jest NULL")) +
                " (Obiekt ID: " + System.identityHashCode(gracz2) + ")");
        // graczRozpoczynajacyOtrzymany jest zawsze null, więc ten log nie jest już tak istotny:
        // System.out.println("[KONTROLER PLANSZY (inicjalizujGre)] Otrzymano graczRozpoczynajacyOtrzymany: " +
        //        (graczRozpoczynajacyOtrzymany != null && graczRozpoczynajacyOtrzymany.getProfilUzytkownika() != null ? graczRozpoczynajacyOtrzymany.getProfilUzytkownika().getNazwaUzytkownika() : "NULL lub jego profil jest NULL") +
        //        " (Obiekt ID: " + System.identityHashCode(graczRozpoczynajacyOtrzymany) + ")");

        if (this.obiektGracz1 == null || this.obiektGracz1.getProfilUzytkownika() == null) {
            System.err.println("[KONTROLER PLANSZY (inicjalizujGre)] BŁĄD KRYTYCZNY: obiektGracz1 lub jego profil jest null!");
            wyswietlKomunikatNaPlanszy("Błąd inicjalizacji gracza 1!", true);
            return;
        }
        if (this.obiektGracz2 == null || this.obiektGracz2.getProfilUzytkownika() == null) {
            System.err.println("[KONTROLER PLANSZY (inicjalizujGre)] BŁĄD KRYTYCZNY: obiektGracz2 lub jego profil jest null!");
            wyswietlKomunikatNaPlanszy("Błąd inicjalizacji gracza 2!", true);
            return;
        }

        System.out.println("Inicjalizowanie gry dla: " + this.obiektGracz1.getProfilUzytkownika().getNazwaUzytkownika() + " vs " + this.obiektGracz2.getProfilUzytkownika().getNazwaUzytkownika());

        // Ustawianie elementów UI związanych z graczami
        nickGracza1Label.setText(this.obiektGracz1.getProfilUzytkownika().getNazwaUzytkownika());
        logoGracza1ImageView.setImage(ladujObrazekProfiluLubRewersuFrakcji(this.obiektGracz1.getProfilUzytkownika().getNazwaPlikuIkonyProfilu(), this.obiektGracz1.getWybranaFrakcja()));
        if (this.obiektGracz1.getKartaDowodcy() != null) {
            // Przy starcie gry zdolność nie jest użyta, więc pokazujemy awers
            dowodcaGracza1ImageView.setImage(ladujObrazekKarty(this.obiektGracz1.getKartaDowodcy()));
        } else {
            dowodcaGracza1ImageView.setImage(null); // Brak karty dowódcy
        }

        nickGracza2Label.setText(this.obiektGracz2.getProfilUzytkownika().getNazwaUzytkownika());
        logoGracza2ImageView.setImage(ladujObrazekProfiluLubRewersuFrakcji(this.obiektGracz2.getProfilUzytkownika().getNazwaPlikuIkonyProfilu(), this.obiektGracz2.getWybranaFrakcja()));
        if (this.obiektGracz2.getKartaDowodcy() != null) {
            // Przy starcie gry zdolność nie jest użyta
            dowodcaGracza2ImageView.setImage(ladujObrazekKarty(this.obiektGracz2.getKartaDowodcy()));
        } else {
            dowodcaGracza2ImageView.setImage(null);
        }

        // Ustawianie tła planszy (kod bez zmian)
        if (glownyKontenerPlanszy != null) {
            glownyKontenerPlanszy.setStyle(null);
            boolean tloUstawione = false;
            try (InputStream stream = getClass().getResourceAsStream("/grafiki/tla/planszahotseat.png")) {
                if (stream != null) {
                    Image backgroundImage = new Image(stream);
                    if (!backgroundImage.isError() && backgroundImage.getWidth() > 0) {
                        glownyKontenerPlanszy.setBackground(new javafx.scene.layout.Background(new javafx.scene.layout.BackgroundImage(backgroundImage,
                                javafx.scene.layout.BackgroundRepeat.NO_REPEAT, javafx.scene.layout.BackgroundRepeat.NO_REPEAT,
                                javafx.scene.layout.BackgroundPosition.CENTER,
                                new javafx.scene.layout.BackgroundSize(1920, 1080, false, false, false, false))));
                        tloUstawione = true;
                    } else {
                        System.err.println("[KONTROLER PLANSZY] Tło 'planszahotseat.png' jest uszkodzone lub puste.");
                    }
                } else {
                    System.err.println("[KONTROLER PLANSZY] Nie znaleziono tła 'planszahotseat.png'.");
                }
            } catch (Exception e) {
                System.err.println("[KONTROLER PLANSZY] Wyjątek podczas ładowania tła: " + e.getMessage());
                e.printStackTrace();
            }
            if (!tloUstawione && glownyKontenerPlanszy != null) {
                System.out.println("[KONTROLER PLANSZY] Ustawianie domyślnego koloru tła.");
                glownyKontenerPlanszy.setStyle("-fx-background-color: #33691E;");
            }
        } else {
            System.err.println("[KONTROLER PLANSZY (inicjalizujGre)] glownyKontenerPlanszy jest null!");
        }

        // Wczytywanie kart, jeśli jeszcze nie wczytane (kod bez zmian)
        if (this.wszystkieKartyWGrzeCache == null) {
            this.wszystkieKartyWGrzeCache = WczytywaczKart.wczytajWszystkieKarty();
            if (this.wszystkieKartyWGrzeCache.isEmpty()) {
                wyswietlKomunikatNaPlanszy("Błąd krytyczny: Nie wczytano definicji kart!", true);
                return;
            }
        }

        // --- POCZĄTEK LOGIKI DLA SCOIA'TAEL ---
        boolean g1JestScoiatael = (this.obiektGracz1.getWybranaFrakcja() == FrakcjaEnum.SCOIATAEL);
        boolean g2JestScoiatael = (this.obiektGracz2.getWybranaFrakcja() == FrakcjaEnum.SCOIATAEL);
        Gracz graczMajacyPrawoWyboru = null;

        if (g1JestScoiatael && g2JestScoiatael) {
            // Obaj gracze to Scoia'tael. Rozstrzygnij losowo lub daj wybór Graczowi 1.
            // Dla uproszczenia, zróbmy standardowy rzut monetą.
            System.out.println("[KONTROLER PLANSZY] Obaj gracze to Scoia'tael. Nastąpi standardowy rzut monetą.");
            // graczMajacyPrawoWyboru pozostaje null, co spowoduje rzut monetą.
        } else if (g1JestScoiatael) {
            graczMajacyPrawoWyboru = this.obiektGracz1;
            System.out.println("[KONTROLER PLANSZY] Gracz 1 (" + this.obiektGracz1.getProfilUzytkownika().getNazwaUzytkownika() + " - Scoia'tael) decyduje, kto rozpoczyna.");
        } else if (g2JestScoiatael) {
            graczMajacyPrawoWyboru = this.obiektGracz2;
            System.out.println("[KONTROLER PLANSZY] Gracz 2 (" + this.obiektGracz2.getProfilUzytkownika().getNazwaUzytkownika() + " - Scoia'tael) decyduje, kto rozpoczyna.");
        }

        if (graczMajacyPrawoWyboru != null) {
            // Jeden z graczy jest Scoia'tael i ma prawo wyboru
            pokazPanelWyboruRozpoczynajacegoScoiatael(graczMajacyPrawoWyboru);
        } else {
            // Żaden z graczy nie jest Scoia'tael LUB obaj są i zdecydowaliśmy o rzucie monetą
            System.out.println("[KONTROLER PLANSZY] Brak decydującego gracza Scoia'tael lub obaj są Scoia'tael. Przechodzenie do standardowego rzutu monetą.");
            rozpocznijSekwencjeRzutuMonetaNaPlanszy();
        }
        // --- KONIEC LOGIKI DLA SCOIA'TAEL ---

        // Usunięto bezpośrednie wywołanie rozpocznijSekwencjeRzutuMonetaNaPlanszy() stąd,
        // ponieważ jest ono teraz częścią powyższej logiki warunkowej.
    }
    private void pokazPanelWyboruRozpoczynajacegoScoiatael(Gracz graczDecydujacy) {
        if (panelWyboruRozpoczynajacegoScoiatael == null || tytulPaneluScoiatael == null ||
                przyciskScoiataelJaZaczynam == null || przyciskScoiataelPrzeciwnikZaczyna == null ||
                graczDecydujacy == null || graczDecydujacy.getProfilUzytkownika() == null) {
            System.err.println("Błąd UI: Nie można pokazać panelu wyboru Scoia'tael - brak elementów lub danych gracza.");
            // Awaryjnie, jeśli panel nie działa, przejdź do standardowego rzutu monetą
            rozpocznijSekwencjeRzutuMonetaNaPlanszy();
            return;
        }

        this.graczDecydujacyScoiatael = graczDecydujacy;
        Gracz przeciwnik = (graczDecydujacy == obiektGracz1) ? obiektGracz2 : obiektGracz1;

        tytulPaneluScoiatael.setText(graczDecydujacy.getProfilUzytkownika().getNazwaUzytkownika() +
                " (Scoia'tael),\nwybierz kto rozpoczyna pierwszą rundę:");
        przyciskScoiataelJaZaczynam.setText("Ja Zaczynam (" + graczDecydujacy.getProfilUzytkownika().getNazwaUzytkownika() + ")");

        if (przeciwnik != null && przeciwnik.getProfilUzytkownika() != null) {
            przyciskScoiataelPrzeciwnikZaczyna.setText(przeciwnik.getProfilUzytkownika().getNazwaUzytkownika() + " Zaczyna");
        } else { // Powinno się nie zdarzyć przy poprawnej inicjalizacji
            przyciskScoiataelPrzeciwnikZaczyna.setText("Przeciwnik Zaczyna");
        }

        ukryjWszystkiePaneleOverlayOprocz(panelWyboruRozpoczynajacegoScoiatael);
        panelWyboruRozpoczynajacegoScoiatael.setVisible(true);
        panelWyboruRozpoczynajacegoScoiatael.toFront();
    }

    @FXML
    private void handleScoiataelJaZaczynamAction() {
        if (graczDecydujacyScoiatael != null && panelWyboruRozpoczynajacegoScoiatael != null) {
            this.wylosowanyGraczRozpoczynajacy = graczDecydujacyScoiatael;
            System.out.println("[KONTROLER PLANSZY] Scoia'tael wybrał: " + this.wylosowanyGraczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika() + " zaczyna.");
            panelWyboruRozpoczynajacegoScoiatael.setVisible(false);
            graczDecydujacyScoiatael = null; // Zresetuj po użyciu
            kontynuujGrePoUstaleniuRozpoczynajacego();
        }
    }

    @FXML
    private void handleScoiataelPrzeciwnikZaczynaAction() {
        if (graczDecydujacyScoiatael != null && panelWyboruRozpoczynajacegoScoiatael != null) {
            Gracz przeciwnik = (graczDecydujacyScoiatael == obiektGracz1) ? obiektGracz2 : obiektGracz1;
            this.wylosowanyGraczRozpoczynajacy = przeciwnik;
            if (this.wylosowanyGraczRozpoczynajacy != null && this.wylosowanyGraczRozpoczynajacy.getProfilUzytkownika() != null) {
                System.out.println("[KONTROLER PLANSZY] Scoia'tael wybrał: " + this.wylosowanyGraczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika() + " zaczyna.");
            } else {
                // Awaryjne, jeśli przeciwnik jest null - nie powinno się zdarzyć
                System.err.println("[KONTROLER PLANSZY] Błąd krytyczny przy wyborze przeciwnika przez Scoia'tael.");
                this.wylosowanyGraczRozpoczynajacy = randomDoRzutu.nextBoolean() ? obiektGracz1 : obiektGracz2; // Awaryjny rzut
            }
            panelWyboruRozpoczynajacegoScoiatael.setVisible(false);
            graczDecydujacyScoiatael = null; // Zresetuj po użyciu
            kontynuujGrePoUstaleniuRozpoczynajacego();
        }
    }


    private void rozpocznijSekwencjeRzutuMonetaNaPlanszy() {
        // Sprawdzamy tylko te elementy, które pozostały
        if (panelRzutuMonetaOverlay == null || infoRzutuLabel == null || monetaDoRzutuImageView == null) {
            System.err.println("Błąd krytyczny: Elementy UI panelu rzutu monetą nie są zainicjalizowane! Awaryjne losowanie.");
            this.wylosowanyGraczRozpoczynajacy = randomDoRzutu.nextBoolean() ? obiektGracz1 : obiektGracz2;
            System.err.println("Awaryjnie wylosowano (z powodu błędu UI): " + (wylosowanyGraczRozpoczynajacy != null && wylosowanyGraczRozpoczynajacy.getProfilUzytkownika() != null ? wylosowanyGraczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika() : "BŁĄD GRACZA"));
            kontynuujGrePoUstaleniuRozpoczynajacego();
            return;
        }

        infoRzutuLabel.setText("Losowanie kto zaczyna...");
        if (coin1ImageRzut != null) monetaDoRzutuImageView.setImage(coin1ImageRzut);

        panelRzutuMonetaOverlay.setVisible(true);
        panelRzutuMonetaOverlay.toFront();

        uruchomAnimacjeRzutuMoneta(); // Od razu uruchom animację
    }
    private void uruchomAnimacjeRzutuMoneta() {
        if (coin1ImageRzut == null || coin2ImageRzut == null || monetaDoRzutuImageView == null || infoRzutuLabel == null) {
            System.err.println("Błąd: Nie można uruchomić animacji rzutu monetą - brak zasobów.");
            // Awaryjnie zakończ rzut i kontynuuj
            this.wylosowanyGraczRozpoczynajacy = randomDoRzutu.nextBoolean() ? obiektGracz1 : obiektGracz2;
            panelRzutuMonetaOverlay.setVisible(false);
            kontynuujGrePoUstaleniuRozpoczynajacego();
            return;
        }

        infoRzutuLabel.setText("Losowanie..."); // Napis podczas animacji

        final int liczbaZmian = 15; // Około 3 sekundy (15 * 200ms = 3000ms)
        final double czasTrwaniaJednejZmiany = 200; // ms

        if (animacjaMonetyNaPlanszy != null) {
            animacjaMonetyNaPlanszy.stop();
        }
        animacjaMonetyNaPlanszy = new Timeline();
        animacjaMonetyNaPlanszy.setCycleCount(liczbaZmian);
        animacjaMonetyNaPlanszy.getKeyFrames().add(
                new KeyFrame(Duration.millis(czasTrwaniaJednejZmiany), event -> {
                    if (monetaDoRzutuImageView.getImage() == coin1ImageRzut) {
                        monetaDoRzutuImageView.setImage(coin2ImageRzut);
                    } else {
                        monetaDoRzutuImageView.setImage(coin1ImageRzut);
                    }
                })
        );

        animacjaMonetyNaPlanszy.setOnFinished(event -> {
            boolean wygralGracz1 = randomDoRzutu.nextBoolean();
            this.wylosowanyGraczRozpoczynajacy = wygralGracz1 ? obiektGracz1 : obiektGracz2;

            monetaDoRzutuImageView.setImage(wygralGracz1 ? coin1ImageRzut : coin2ImageRzut);
            infoRzutuLabel.setText("Rozpoczyna: " + this.wylosowanyGraczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika() + "!");

            System.out.println("[KONTROLER PLANSZY] Rzut monetą zakończony. Rozpoczyna: " + this.wylosowanyGraczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika());

            PauseTransition pauzaPoRzucie = new PauseTransition(Duration.seconds(2)); // Pauza 2s na wynik
            pauzaPoRzucie.setOnFinished(e -> {
                if (panelRzutuMonetaOverlay != null) panelRzutuMonetaOverlay.setVisible(false);
                kontynuujGrePoUstaleniuRozpoczynajacego();
            });
            pauzaPoRzucie.play();
        });
        animacjaMonetyNaPlanszy.play();
    }
    private void kontynuujGrePoUstaleniuRozpoczynajacego() {
        if (this.wylosowanyGraczRozpoczynajacy == null) {
            System.err.println("Błąd krytyczny: Nie ustalono gracza rozpoczynającego przed utworzeniem silnika gry!");
            // Awaryjne przypisanie, jeśli coś poszło nie tak
            this.wylosowanyGraczRozpoczynajacy = (obiektGracz1 != null) ? obiektGracz1 : obiektGracz2;
            if (this.wylosowanyGraczRozpoczynajacy == null) { // Ostateczna awaria
                wyswietlKomunikatNaPlanszy("Błąd krytyczny inicjalizacji gry!", true); return;
            }
        }
        System.out.println("[KONTROLER PLANSZY] Tworzenie silnika gry. Faktycznie rozpoczyna: " + wylosowanyGraczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika());
        // Teraz tworzymy silnik gry, przekazując ustalonego gracza rozpoczynającego
        this.silnikGry = new SilnikGry(this.obiektGracz1, this.obiektGracz2, this.wylosowanyGraczRozpoczynajacy, this, this.wszystkieKartyWGrzeCache);
        this.silnikGry.rozpocznijGre(); // To zainicjuje pierwszą rundę i pokaże panel "Tura gracza..."
    }

    private Image ladujObrazekKarty(Karta karta) {
        if (karta == null || karta.getGrafika() == null || karta.getGrafika().isEmpty()) return null;
        String sciezka = "/grafiki/karty/" + karta.getGrafika();
        try (InputStream stream = getClass().getResourceAsStream(sciezka)) {
            if (stream != null) return new Image(stream);
        } catch (Exception e) { System.err.println("Błąd ładowania grafiki karty " + karta.getGrafika() + ": " + e.getMessage()); }
        System.err.println("Nie znaleziono grafiki karty: " + sciezka);
        return null;
    }
    private Image ladujObrazekZetonu(boolean czyAktywny) {
        String sciezka = czyAktywny ? SCIEZKA_ZETON_CZERWONY : SCIEZKA_ZETON_CZARNY;
        try (InputStream stream = getClass().getResourceAsStream(sciezka)) {
            if (stream != null) return new Image(stream);
        } catch (Exception e) { System.err.println("Błąd ładowania grafiki żetonu " + sciezka + ": " + e.getMessage()); }
        System.err.println("Nie znaleziono grafiki żetonu: " + sciezka);
        return null;
    }
    private Image ladujObrazekRewersuFrakcji(FrakcjaEnum frakcja) {
        if (frakcja == null) return null;
        String nazwaPlikuRewersu = "";
        switch (frakcja) {
            case KROLESTWA_POLNOCY: nazwaPlikuRewersu = "kru_back.jpg"; break;
            case NILFGAARD: nazwaPlikuRewersu = "nilf_back.jpg"; break;
            case POTWORY: nazwaPlikuRewersu = "potw_back.jpg"; break;
            case SCOIATAEL: nazwaPlikuRewersu = "scoia_back.jpg"; break;
            default: return null;
        }
        String sciezka = "/grafiki/ikony_frakcji/" + nazwaPlikuRewersu;
        try (InputStream stream = getClass().getResourceAsStream(sciezka)) {
            if (stream != null) return new Image(stream);
        } catch (Exception e) { System.err.println("Błąd ładowania rewersu frakcji " + sciezka + ": " + e.getMessage()); }
        System.err.println("Nie znaleziono rewersu frakcji: " + sciezka);
        return null;
    }
    private Image ladujObrazekProfiluLubRewersuFrakcji(String nazwaPlikuIkonyProfilu, FrakcjaEnum frakcja) {
        String sciezkaIkonyProfilu = (nazwaPlikuIkonyProfilu != null && !nazwaPlikuIkonyProfilu.isEmpty()) ? "/grafiki/ikony_profilu/" + nazwaPlikuIkonyProfilu : null;
        Image img = null;
        if (sciezkaIkonyProfilu != null) {
            try (InputStream stream = getClass().getResourceAsStream(sciezkaIkonyProfilu)) {
                if (stream != null) img = new Image(stream);
            } catch (Exception e) {}
        }
        if (img == null || img.isError()) {
            return ladujObrazekRewersuFrakcji(frakcja);
        }
        return img;
    }
    private Image ladujObrazekZElementowUI(String nazwaPliku) {
        if (nazwaPliku == null || nazwaPliku.isEmpty()) return null;
        String sciezka = "/grafiki/elementy_ui/" + nazwaPliku;
        try (InputStream stream = getClass().getResourceAsStream(sciezka)) {
            if (stream != null) return new Image(stream);
        } catch (Exception e) {}
        System.err.println("Nie znaleziono grafiki UI: " + sciezka);
        return null;
    }
    private Image ladujRewersDowodcy(FrakcjaEnum frakcja) {
        if (frakcja == null) {
            System.err.println("Nie można załadować rewersu dowódcy - frakcja jest null.");
            // Możesz zwrócić domyślny rewers ogólny, jeśli taki masz, lub null
            try (InputStream stream = getClass().getResourceAsStream("/grafiki/karty/rewers_ogolny.png")) { // Awaryjny rewers
                if (stream != null) return new Image(stream);
            } catch (Exception e) { /* ignoruj */ }
            return null;
        }
        // Użyj istniejącej metody do ładowania rewersu frakcji
        Image rewersFrakcji = ladujObrazekRewersuFrakcji(frakcja);
        if (rewersFrakcji == null) {
            System.err.println("Nie udało się załadować rewersu frakcji dla " + frakcja.getNazwaWyswietlana() + " jako rewersu dowódcy.");
        }
        return rewersFrakcji;
    }

    private ImageView stworzWidokKarty(Karta karta, double szerokosc, double wysokosc) {
        ImageView obrazekKarty = new ImageView();
        obrazekKarty.setFitWidth(szerokosc);
        obrazekKarty.setFitHeight(wysokosc);
        obrazekKarty.setPreserveRatio(true);
        obrazekKarty.setSmooth(true);
        obrazekKarty.setCache(true);
        if (karta != null) {
            Image img = ladujObrazekKarty(karta);
            if (img != null && !img.isError()) obrazekKarty.setImage(img);
        }
        return obrazekKarty;
    }

    private void wyswietlKartyWHBox(List<Karta> listaKart, HBox kontenerHBox,
                                    double szerokoscKarty, double wysokoscKarty,
                                    boolean czyPokazacRewersy) {
        if (kontenerHBox == null) return;
        kontenerHBox.getChildren().clear();
        if (listaKart == null || listaKart.isEmpty()) {
            kontenerHBox.setSpacing(0); return;
        }
        // Alignment jest już ustawiony w FXML lub initialize()
        // kontenerHBox.setAlignment(Pos.CENTER);

        double dostepnaSzerokosc = kontenerHBox.getPrefWidth() - (kontenerHBox.getPadding().getLeft() + kontenerHBox.getPadding().getRight());
        int liczbaKart = listaKart.size();
        double preferowanyOdstęp = 0.0;
        double potrzebnaSzerokoscStykajacychSie = (liczbaKart * szerokoscKarty) + (Math.max(0, liczbaKart - 1) * preferowanyOdstęp);

        if (potrzebnaSzerokoscStykajacychSie <= dostepnaSzerokosc) {
            kontenerHBox.setSpacing(preferowanyOdstęp);
        } else {
            if (liczbaKart > 1) {
                double odstepMiedzyPoczatkami = (dostepnaSzerokosc - szerokoscKarty) / (double)(liczbaKart - 1);
                double ujemnySpacing = odstepMiedzyPoczatkami - szerokoscKarty;
                double minimalnieWidocznePoprzedniej = 20.0;
                if (szerokoscKarty + ujemnySpacing < minimalnieWidocznePoprzedniej) {
                    ujemnySpacing = -(szerokoscKarty - minimalnieWidocznePoprzedniej);
                }
                kontenerHBox.setSpacing(ujemnySpacing);
            } else {
                kontenerHBox.setSpacing(0);
            }
        }

        for (Karta karta : listaKart) {
            ImageView widokKarty;
            Gracz wlascicielReki = null;
            if(kontenerHBox == rekaGracza1HBox) wlascicielReki = obiektGracz1;
            else if(kontenerHBox == rekaGracza2HBox) wlascicielReki = obiektGracz2;

            if (czyPokazacRewersy && wlascicielReki != null) {
                Image rewersImg = ladujObrazekRewersuFrakcji(wlascicielReki.getWybranaFrakcja());
                widokKarty = new ImageView(rewersImg);
                widokKarty.setFitWidth(szerokoscKarty);
                widokKarty.setFitHeight(wysokoscKarty);
                widokKarty.setPreserveRatio(true);
            } else {
                widokKarty = stworzWidokKarty(karta, szerokoscKarty, wysokoscKarty);
                widokKarty.setUserData(karta);

                boolean czyInteraktywnaKarta = false;
                if (silnikGry != null && !silnikGry.isCzyGraZakonczona() && !silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                    if (wlascicielReki == silnikGry.getGraczAktualnejTury()) {
                        if( (wlascicielReki == obiektGracz1) || (wlascicielReki == obiektGracz2 && pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat()) ) {
                            czyInteraktywnaKarta = true;
                        }
                    }
                }

                if (czyInteraktywnaKarta && kontenerHBox.getId() != null && (kontenerHBox.getId().equals("rekaGracza1HBox") || kontenerHBox.getId().equals("rekaGracza2HBox")) ) {
                    widokKarty.setCursor(Cursor.HAND);
                    widokKarty.setOnMouseClicked(event -> {
                        if (event.getSource() instanceof ImageView) {
                            handleKartaWRęceKliknięta((Karta)((ImageView)event.getSource()).getUserData());
                        }
                    });
                    widokKarty.setOnMouseEntered(event -> {
                        ImageView target = (ImageView) event.getSource();
                        Karta kartaPdKurs = (Karta) target.getUserData();
                        if (podgladKartyImageView != null && kartaPdKurs != null) podgladKartyImageView.setImage(ladujObrazekKarty(kartaPdKurs));
                        target.setEffect(efektObramowkiHover);
                        // target.setTranslateY(-15); // Usunięte dla stabilności HBox
                        // target.toFront();
                    });
                    widokKarty.setOnMouseExited(event -> {
                        ImageView target = (ImageView) event.getSource();
                        if (podgladKartyImageView != null) podgladKartyImageView.setImage(null);
                        target.setEffect(null);
                        // target.setTranslateY(0);
                    });
                } else {
                    widokKarty.setCursor(Cursor.DEFAULT);
                    widokKarty.setOnMouseClicked(null);
                    if (!czyPokazacRewersy) {
                        widokKarty.setOnMouseEntered(event -> {
                            if (podgladKartyImageView != null && event.getSource() instanceof ImageView) {
                                Karta kartaPdKurs = (Karta)((ImageView)event.getSource()).getUserData();
                                if (kartaPdKurs != null) podgladKartyImageView.setImage(ladujObrazekKarty(kartaPdKurs));
                            }
                        });
                        widokKarty.setOnMouseExited(event -> {
                            if (podgladKartyImageView != null) podgladKartyImageView.setImage(null);
                        });
                    }
                }
            }
            if (widokKarty != null) kontenerHBox.getChildren().add(widokKarty);
        }
    }

    public void pokazPanelWynikuRundy(String komunikatDoWyswietlenia, Gracz zwyciezcaRundy, boolean czyBylRemisPunktowy) { // NOWA SYGNATURA
        if (panelWynikuRundy == null || grafikaWynikuRundyImageView == null || napisWynikuRundyLabel == null) {
            System.err.println("Błąd: Nie można pokazać panelu wyniku rundy - brak elementów UI.");
            if (silnikGry != null) {
                silnikGry.przejdzDoNastepnegoEtapuPoWynikuRundy();
            }
            return;
        }

        Image grafikaDoWyswietlenia = null;
        if (zwyciezcaRundy != null) {
            // Jeśli jest zwycięzca, zawsze pokazujemy grafikę wygranej
            // (nawet jeśli Nilfgaard wygrał po remisie, to jest to forma wygranej)
            grafikaDoWyswietlenia = obrazekWygranaRundyPng; // Zakładam, że 'obrazekWygranaRundyPng' jest poprawnie załadowany w initialize()
        } else if (czyBylRemisPunktowy) {
            // Jeśli nie ma zwycięzcy (zwyciezcaRundy == null) ORAZ był remis punktowy
            // (co oznacza, że obaj gracze stracili życie lub gra zakończyła się remisem bez przełamania przez Nilfgaard)
            grafikaDoWyswietlenia = obrazekRemisRundyPng; // Zakładam, że 'obrazekRemisRundyPng' jest poprawnie załadowany
        }
        // Jeśli ani jedno, ani drugie, grafikaDoWyswietlenia pozostanie null (np. błąd w logice silnika)

        if (grafikaDoWyswietlenia != null) {
            grafikaWynikuRundyImageView.setImage(grafikaDoWyswietlenia);
            grafikaWynikuRundyImageView.setVisible(true);
        } else {
            grafikaWynikuRundyImageView.setImage(null);
            grafikaWynikuRundyImageView.setVisible(false); // Ukryj, jeśli nie ma odpowiedniej grafiki
        }
        napisWynikuRundyLabel.setText(komunikatDoWyswietlenia);

        // Ukryj inne potencjalnie aktywne panele overlay
        ukryjWszystkiePaneleOverlayOprocz(panelWynikuRundy); // Upewnij się, że masz taką metodę pomocniczą

        panelWynikuRundy.setVisible(true);
        panelWynikuRundy.toFront();

        // Jeśli nie ma przycisku "Dalej", panel zniknie automatycznie
        if (przyciskDalejPoWynikuRundy == null || !przyciskDalejPoWynikuRundy.isVisible() || !przyciskDalejPoWynikuRundy.isManaged()) {
            PauseTransition pauzaWynikuRundy = new PauseTransition(Duration.seconds(4.0));
            pauzaWynikuRundy.setOnFinished(event -> {
                if (panelWynikuRundy != null) {
                    panelWynikuRundy.setVisible(false);
                }
                if (silnikGry != null) {
                    silnikGry.przejdzDoNastepnegoEtapuPoWynikuRundy();
                }
            });
            pauzaWynikuRundy.play();
        }
        // Jeśli jest przycisk "Dalej", to on wywoła handleDalejPoWynikuRundyAction,
        // a w tej metodzie już jest logika przejścia dalej w silniku gry.
    }
    @FXML
    private void handleDalejPoWynikuRundyAction() {
        if (panelWynikuRundy != null) {
            panelWynikuRundy.setVisible(false);
        }
        if (silnikGry != null) {
            silnikGry.przejdzDoNastepnegoEtapuPoWynikuRundy(); // POPRAWNA NAZWA
        }
    }

    @FXML
    private void handleRewanzAction() {
        System.out.println("[KONTROLER PLANSZY] Wybrano REWANŻ.");
        if (panelKoncaGry != null) panelKoncaGry.setVisible(false);

        if (obiektGracz1 != null && obiektGracz2 != null && pixelGwintAplikacja != null) {
            // Resetuj stan istniejących obiektów Gracz
            obiektGracz1.resetDoNowejGry();
            obiektGracz2.resetDoNowejGry();

            // Ponownie inicjalizuj grę na tej samej instancji kontrolera.
            // Metoda inicjalizujGre powinna stworzyć nowy SilnikGry.
            inicjalizujGre(obiektGracz1, obiektGracz2, null); // null jako trzeci argument, aby wymusić rzut monetą na planszy
        } else {
            System.err.println("Nie można rozpocząć rewanżu - brak obiektów graczy lub aplikacji.");
            // Awaryjnie, wróć do menu, jeśli stan jest niepoprawny
            if (pixelGwintAplikacja != null && pixelGwintAplikacja.getProfilGracza1() != null) {
                pixelGwintAplikacja.pokazMenuGlowne(pixelGwintAplikacja.getProfilGracza1());
            } else if (pixelGwintAplikacja != null) {
                pixelGwintAplikacja.pokazEkranLogowania();
            }
        }
    }

    @FXML
    private void handleMenuGlowneZKoncaGryAction() {
        System.out.println("[KONTROLER PLANSZY] Wybrano POWRÓT DO MENU GŁÓWNEGO.");
        if (panelKoncaGry != null) panelKoncaGry.setVisible(false);
        if (pixelGwintAplikacja != null) {
            // Wróć do menu głównego, przekazując profil zalogowanego użytkownika.
            // Załóżmy, że obiektGracz1.getProfilUzytkownika() to główny zalogowany użytkownik.
            // Jeśli graczem głównym mógł być też obiektGracz2, trzeba by to inaczej obsłużyć.
            Uzytkownik profilDoMenu = (obiektGracz1 != null) ? obiektGracz1.getProfilUzytkownika() : null;
            if (profilDoMenu == null && pixelGwintAplikacja.getProfilGracza1() != null) {
                profilDoMenu = pixelGwintAplikacja.getProfilGracza1(); // Fallback
            }

            if (profilDoMenu != null) {
                pixelGwintAplikacja.pokazMenuGlowne(profilDoMenu);
            } else {
                System.err.println("Nie można wrócić do menu - brak profilu użytkownika. Pokazywanie ekranu logowania.");
                pixelGwintAplikacja.pokazEkranLogowania(); // Ostateczność
            }
        }
    }


    public void odswiezCalakolwiekPlansze() {
        if (silnikGry == null) { return; }
        Gracz aktualnyG1 = silnikGry.getGracz1();
        Gracz aktualnyG2 = silnikGry.getGracz2();
        StanRundy stanRundy = silnikGry.getStanRundy();
        if (aktualnyG1 == null || aktualnyG2 == null || stanRundy == null) { return; }

        sumaPunktowG1Label.setText(String.valueOf(aktualnyG1.getPlanszaGry().getLacznaSumaPunktowGracza()));
        sumaPunktowG2Label.setText(String.valueOf(aktualnyG2.getPlanszaGry().getLacznaSumaPunktowGracza()));
        punktyPiechotyG1Label.setText(String.valueOf(aktualnyG1.getPlanszaGry().getRzadPiechoty().getSumaPunktowWRzedzie()));
        punktyStrzeleckieG1Label.setText(String.valueOf(aktualnyG1.getPlanszaGry().getRzadStrzelecki().getSumaPunktowWRzedzie()));
        punktyOblezeniaG1Label.setText(String.valueOf(aktualnyG1.getPlanszaGry().getRzadOblezenia().getSumaPunktowWRzedzie()));
        punktyPiechotyG2Label.setText(String.valueOf(aktualnyG2.getPlanszaGry().getRzadPiechoty().getSumaPunktowWRzedzie()));
        punktyStrzeleckieG2Label.setText(String.valueOf(aktualnyG2.getPlanszaGry().getRzadStrzelecki().getSumaPunktowWRzedzie()));
        punktyOblezeniaG2Label.setText(String.valueOf(aktualnyG2.getPlanszaGry().getRzadOblezenia().getSumaPunktowWRzedzie()));

        zycie1G1ImageView.setImage(ladujObrazekZetonu(aktualnyG1.getWygraneRundy() >= 1));
        zycie2G1ImageView.setImage(ladujObrazekZetonu(aktualnyG1.getWygraneRundy() >= 2));
        zycie1G2ImageView.setImage(ladujObrazekZetonu(aktualnyG2.getWygraneRundy() >= 1));
        zycie2G2ImageView.setImage(ladujObrazekZetonu(aktualnyG2.getWygraneRundy() >= 2));

        licznikTaliiG1Label.setText(String.valueOf(aktualnyG1.getTaliaDoGry().size()));
        licznikTaliiG2Label.setText(String.valueOf(aktualnyG2.getTaliaDoGry().size()));

        if (aktualnyG1.getTaliaDoGry().isEmpty()) taliaGracza1ImageView.setImage(null);
        else taliaGracza1ImageView.setImage(ladujObrazekRewersuFrakcji(aktualnyG1.getWybranaFrakcja()));
        if (aktualnyG2.getTaliaDoGry().isEmpty()) taliaGracza2ImageView.setImage(null);
        else taliaGracza2ImageView.setImage(ladujObrazekRewersuFrakcji(aktualnyG2.getWybranaFrakcja()));

        licznikCmentarzaG1Label.setText(String.valueOf(aktualnyG1.getOdrzucone().size()));
        if (aktualnyG1.getOdrzucone().isEmpty()) cmentarzGracza1ImageView.setImage(null);
        else cmentarzGracza1ImageView.setImage(ladujObrazekKarty(aktualnyG1.getOdrzucone().get(aktualnyG1.getOdrzucone().size() - 1)));
        licznikCmentarzaG2Label.setText(String.valueOf(aktualnyG2.getOdrzucone().size()));
        if (aktualnyG2.getOdrzucone().isEmpty()) cmentarzGracza2ImageView.setImage(null);
        else cmentarzGracza2ImageView.setImage(ladujObrazekKarty(aktualnyG2.getOdrzucone().get(aktualnyG2.getOdrzucone().size() - 1)));

        boolean pokazujOdkryteG1 = (silnikGry.getGraczAktualnejTury() == aktualnyG1 && !silnikGry.isOczekiwanieNaPotwierdzenieTury());
        boolean pokazujOdkryteG2 = (silnikGry.getGraczAktualnejTury() == aktualnyG2 && !silnikGry.isOczekiwanieNaPotwierdzenieTury());
        if(pixelGwintAplikacja != null && !pixelGwintAplikacja.isTrybHotSeat()){
            if(silnikGry.getGraczAktualnejTury() == aktualnyG1) pokazujOdkryteG2 = false;
            else if (silnikGry.getGraczAktualnejTury() == aktualnyG2) pokazujOdkryteG1 = false;
        }
        if (silnikGry.isOczekiwanieNaPotwierdzenieTury() && (pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat())) {
            pokazujOdkryteG1 = false;
            pokazujOdkryteG2 = false;
        }

        wyswietlKartyWHBox(aktualnyG1.getReka(), rekaGracza1HBox,
                STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, !pokazujOdkryteG1);
        wyswietlKartyWHBox(aktualnyG2.getReka(), rekaGracza2HBox,
                STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, !pokazujOdkryteG2);

        // Aktualizacja wyświetlania rzędów gracza 1
        wyswietlKartyWHBox(stanRundy.getPlanszaGracza1().getRzadPiechoty().getKartyJednostekWRzedzie(),
                rzadPiechotyG1HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);
        wyswietlKartyWHBox(stanRundy.getPlanszaGracza1().getRzadStrzelecki().getKartyJednostekWRzedzie(),
                rzadStrzeleckiG1HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);
        wyswietlKartyWHBox(stanRundy.getPlanszaGracza1().getRzadOblezenia().getKartyJednostekWRzedzie(),
                rzadOblezeniaG1HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);

        // Aktualizacja wyświetlania rzędów gracza 2
        wyswietlKartyWHBox(stanRundy.getPlanszaGracza2().getRzadPiechoty().getKartyJednostekWRzedzie(),
                rzadPiechotyG2HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);
        wyswietlKartyWHBox(stanRundy.getPlanszaGracza2().getRzadStrzelecki().getKartyJednostekWRzedzie(),
                rzadStrzeleckiG2HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);
        wyswietlKartyWHBox(stanRundy.getPlanszaGracza2().getRzadOblezenia().getKartyJednostekWRzedzie(),
                rzadOblezeniaG2HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);

        // Aktualizacja wyświetlania rzędu pogody
        if (pogodaHBox != null && stanRundy.getAktywneKartyWRzedziePogody() != null) {
            wyswietlKartyWHBox(stanRundy.getAktywneKartyWRzedziePogody(), pogodaHBox,
                    STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);
        }

        ustawWidokRogu(stanRundy.getPlanszaGracza1().getRzadPiechoty().getKartaRoguDowodcy(), rogPiechotyG1Slot);
        ustawWidokRogu(stanRundy.getPlanszaGracza1().getRzadStrzelecki().getKartaRoguDowodcy(), rogStrzeleckiG1Slot);
        ustawWidokRogu(stanRundy.getPlanszaGracza1().getRzadOblezenia().getKartaRoguDowodcy(), rogOblezeniaG1Slot);
        ustawWidokRogu(stanRundy.getPlanszaGracza2().getRzadPiechoty().getKartaRoguDowodcy(), rogPiechotyG2Slot);
        ustawWidokRogu(stanRundy.getPlanszaGracza2().getRzadStrzelecki().getKartaRoguDowodcy(), rogStrzeleckiG2Slot);
        ustawWidokRogu(stanRundy.getPlanszaGracza2().getRzadOblezenia().getKartaRoguDowodcy(), rogOblezeniaG2Slot);

        if (obiektGracz1 != null && dowodcaGracza1ImageView != null && obiektGracz1.getKartaDowodcy() != null) {
            dowodcaGracza1ImageView.setImage(
                    obiektGracz1.isZdolnoscDowodcyUzyta() ?
                            ladujRewersDowodcy(obiektGracz1.getWybranaFrakcja()) : // <<< ZMIANA TUTAJ
                            ladujObrazekKarty(obiektGracz1.getKartaDowodcy())
            );
        }
        if (obiektGracz2 != null && dowodcaGracza2ImageView != null && obiektGracz2.getKartaDowodcy() != null) {
            dowodcaGracza2ImageView.setImage(
                    obiektGracz2.isZdolnoscDowodcyUzyta() ?
                            ladujRewersDowodcy(obiektGracz2.getWybranaFrakcja()) : // <<< ZMIANA TUTAJ
                            ladujObrazekKarty(obiektGracz2.getKartaDowodcy())
            );
        }

        if (silnikGry != null) {
            uaktywnijInterfejsDlaTury(silnikGry.getGraczAktualnejTury() == obiektGracz1);
        }
        System.out.println("Odświeżono widok całej planszy.");
    }

    private void ustawWidokRogu(Karta kartaRogu, StackPane slotRogu) {
        if (slotRogu == null) return;
        slotRogu.getChildren().clear();
        if (kartaRogu != null) {
            double slotWidth = slotRogu.getPrefWidth();
            if(slotWidth <=0 && slotRogu.getWidth() > 0) slotWidth = slotRogu.getWidth(); else if(slotWidth <=0) slotWidth = 95;
            double slotHeight = slotRogu.getPrefHeight();
            if(slotHeight <=0 && slotRogu.getHeight() > 0) slotHeight = slotRogu.getHeight(); else if(slotHeight <=0) slotHeight = 122;
            ImageView obrazekRogu = stworzWidokKarty(kartaRogu, slotWidth - 4, slotHeight - 4);
            if (obrazekRogu != null && obrazekRogu.getImage() != null) slotRogu.getChildren().add(obrazekRogu);
        }
    }

    @FXML private void handleKartaWRęceKliknięta(Karta wybranaKarta) {
        Gracz aktywnyGracz = (silnikGry != null) ? silnikGry.getGraczAktualnejTury() : null;
        boolean czyMogeTerazGrac = (aktywnyGracz == obiektGracz1) || (aktywnyGracz == obiektGracz2 && pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat());

        if (silnikGry == null || silnikGry.isCzyGraZakonczona() || !czyMogeTerazGrac || silnikGry.isOczekiwanieNaPotwierdzenieTury() ) {
            wyswietlKomunikatNaPlanszy("Nie Twoja tura lub poczekaj.", false);
            return;
        }
        if (aktywnyGracz == null || !aktywnyGracz.getReka().contains(wybranaKarta)) {
            wyswietlKomunikatNaPlanszy("Tej karty nie ma w Twojej ręce lub błąd gracza.", true);
            return;
        }
        aktualnieZaznaczonaKartaDoZagrnia = wybranaKarta;
        podswietlMozliweCeleDlaKarty(wybranaKarta, aktywnyGracz);
    }

    private void podswietlMozliweCeleDlaKarty(Karta karta, Gracz aktywnyGracz) {
        usunPodswietlenieRzedowISlotow();
        if (karta == null || aktywnyGracz == null) {
            System.err.println("[KONTROLER] podswietlMozliweCeleDlaKarty: karta lub aktywnyGracz jest null.");
            return;
        }

        boolean czyKartaToSzpieg = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
        boolean czyKartaMaZrecznosc = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Zręczność");

        System.out.println("[KONTROLER] Podświetlanie celów dla karty: " + karta.getNazwa() +
                (czyKartaToSzpieg ? " (Szpiegostwo)" : "") +
                (czyKartaMaZrecznosc ? " (Zręczność)" : ""));

        HBox[] rzedyPlanszyDocelowejJednostek = null; // Zdefiniuj tutaj, aby było dostępne dla bloku jednostek/bohaterów
        if (karta.getTyp() == TypKartyEnum.JEDNOSTKA || karta.getTyp() == TypKartyEnum.BOHATER) {
            if (czyKartaToSzpieg) {
                rzedyPlanszyDocelowejJednostek = (aktywnyGracz == obiektGracz1) ?
                        new HBox[]{rzadPiechotyG2HBox, rzadStrzeleckiG2HBox, rzadOblezeniaG2HBox} :
                        new HBox[]{rzadPiechotyG1HBox, rzadStrzeleckiG1HBox, rzadOblezeniaG1HBox};
                System.out.println("  > Cel: Plansza przeciwnika dla szpiega.");
            } else {
                rzedyPlanszyDocelowejJednostek = (aktywnyGracz == obiektGracz1) ?
                        new HBox[]{rzadPiechotyG1HBox, rzadStrzeleckiG1HBox, rzadOblezeniaG1HBox} :
                        new HBox[]{rzadPiechotyG2HBox, rzadStrzeleckiG2HBox, rzadOblezeniaG2HBox};
                System.out.println("  > Cel: Własna plansza dla jednostki/bohatera.");
            }
        }


        if (karta.getTyp() == TypKartyEnum.SPECJALNA) {
            String nazwaKartySpecjalnej = karta.getNazwa().toLowerCase();
            boolean jestKartaPogody = nazwaKartySpecjalnej.contains("mróz") ||
                    nazwaKartySpecjalnej.contains("mgła") ||
                    nazwaKartySpecjalnej.contains("deszcz");
            // Użyj umiejętności do identyfikacji Manekina, jest bardziej niezawodna niż nazwa
            boolean jestManekinem = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Manekin do ćwiczeń");

            if (nazwaKartySpecjalnej.contains("róg dowódcy")) {
                StackPane[] wlasneSlotyRogu = (aktywnyGracz == obiektGracz1) ?
                        new StackPane[]{rogPiechotyG1Slot, rogStrzeleckiG1Slot, rogOblezeniaG1Slot} :
                        new StackPane[]{rogPiechotyG2Slot, rogStrzeleckiG2Slot, rogOblezeniaG2Slot};
                boolean znalezionoPustySlot = false;
                for (StackPane slot : wlasneSlotyRogu) {
                    if (slot != null && slot.getChildren().isEmpty()) {
                        podswietlElement(slot);
                        znalezionoPustySlot = true;
                    }
                }
                if (!znalezionoPustySlot) {
                    wyswietlKomunikatNaPlanszy("Wszystkie Twoje sloty na Róg Dowódcy są już zajęte.", false);
                    resetujStanZaznaczonejKarty();
                }
            } else if (jestKartaPogody) {
                System.out.println("  > Karta pogody (" + karta.getNazwa() + ") - podświetlanie slotu pogody.");
                if (pogodaHBox != null) {
                    podswietlElement(pogodaHBox);
                    // Karta pozostaje zaznaczona, nie resetujemy stanu
                } else {
                    System.err.println("[KONTROLER] pogodaHBox jest null, nie można podświetlić dla karty pogody: " + karta.getNazwa());
                    resetujStanZaznaczonejKarty();
                }
            } else if (jestManekinem) {
                System.out.println("  > Karta Manekin (" + karta.getNazwa() + ") - inicjowanie przez silnik.");
                if (silnikGry != null) {
                    // silnikGry.zagrajKarte dla Manekina powinien:
                    // 1. Sprawdzić, czy są cele. Jeśli nie, zwrócić false.
                    // 2. Usunąć Manekina z ręki gracza.
                    // 3. Ustawić stan w silniku (oczekiwanieNaWyborCeluDlaManekina = true).
                    // 4. Wywołać kontrolerPlanszy.rozpocznijWyborCeluDlaManekina(...).
                    // 5. Zwrócić true, jeśli interakcja się rozpoczęła.
                    boolean interakcjaManekinaRozpoczeta = silnikGry.zagrajKarte(aktywnyGracz, karta, null);

                    if (!interakcjaManekinaRozpoczeta) {
                        // Jeśli silnik nie mógł rozpocząć interakcji (np. brak celów),
                        // karta powinna zostać zwrócona do ręki (co silnik powinien obsłużyć w zagrajKarte zwracając false),
                        // a stan w kontrolerze zresetowany.
                        System.out.println("[KONTROLER] Interakcja manekina nie mogła być rozpoczęta przez silnik (np. brak celów lub błąd). Resetowanie stanu.");
                        resetujStanZaznaczonejKarty();
                    }
                    // Jeśli interakcjaManekinaRozpoczeta == true, to NIE resetujemy tutaj stanu.
                    // Karta jest "w użyciu", kontroler czeka na wybór celu.
                    // `aktualnieZaznaczonaKartaDoZagrnia` pozostaje ustawiona na Manekina.
                } else {
                    System.err.println("[KONTROLER] SilnikGry jest null, nie można zainicjować Manekina.");
                    resetujStanZaznaczonejKarty();
                }
            } else {
                // Inne karty specjalne (np. Czyste Niebo, Pożoga S), które są zagrywane natychmiast
                System.out.println("  > Karta specjalna globalna (" + karta.getNazwa() + ") - zagrywana bez wyboru konkretnego slotu na planszy.");
                if (silnikGry != null) {
                    silnikGry.zagrajKarte(aktywnyGracz, karta, null);
                }
                resetujStanZaznaczonejKarty(); // Reset po natychmiastowym zagraniu
            }
        } else if (karta.getTyp() == TypKartyEnum.JEDNOSTKA || karta.getTyp() == TypKartyEnum.BOHATER) {
            List<TypRzeduEnum> mozliweTypyRzedow = new ArrayList<>();

            if (czyKartaMaZrecznosc) {
                // System.out.println("  > Karta '" + karta.getNazwa() + "' ma Zręczność. Pozycja1: '" + karta.getPozycja() + "', Pozycja2: '" + karta.getPozycja2() + "'");
                TypRzeduEnum typRzedu1 = TypRzeduEnum.fromStringPozycjiKarty(karta.getPozycja());
                if (typRzedu1 != null) {
                    mozliweTypyRzedow.add(typRzedu1);
                }
                if (karta.getPozycja2() != null && !karta.getPozycja2().trim().isEmpty() && !karta.getPozycja2().equalsIgnoreCase("N/D")) {
                    TypRzeduEnum typRzedu2 = TypRzeduEnum.fromStringPozycjiKarty(karta.getPozycja2());
                    if (typRzedu2 != null && !mozliweTypyRzedow.contains(typRzedu2)) {
                        mozliweTypyRzedow.add(typRzedu2);
                    }
                }
            } else {
                String pozycjaKartyString = karta.getPozycja().toLowerCase();
                if (pozycjaKartyString.contains("dowolne")) {
                    mozliweTypyRzedow.add(TypRzeduEnum.PIECHOTA);
                    mozliweTypyRzedow.add(TypRzeduEnum.STRZELECKIE);
                    mozliweTypyRzedow.add(TypRzeduEnum.OBLEZENIE);
                } else {
                    TypRzeduEnum pojedynczyTypRzedu = TypRzeduEnum.fromStringPozycjiKarty(karta.getPozycja());
                    if (pojedynczyTypRzedu != null) {
                        mozliweTypyRzedow.add(pojedynczyTypRzedu);
                    }
                }
            }

            if (mozliweTypyRzedow.isEmpty()) {
                wyswietlKomunikatNaPlanszy("Karta " + karta.getNazwa() + " nie ma określonej prawidłowej pozycji do zagrania.", true);
                resetujStanZaznaczonejKarty();
                return;
            }

            boolean cokolwiekPodswietlono = false;
            if (rzedyPlanszyDocelowejJednostek != null) {
                System.out.print("    > Podświetlanie rzędów typu: ");
                for (TypRzeduEnum typRzedu : mozliweTypyRzedow) {
                    System.out.print(typRzedu.name() + " ");
                    switch (typRzedu) {
                        case PIECHOTA:
                            if (rzedyPlanszyDocelowejJednostek.length > 0 && rzedyPlanszyDocelowejJednostek[0] != null) {
                                podswietlElement(rzedyPlanszyDocelowejJednostek[0]);
                                cokolwiekPodswietlono = true;
                            }
                            break;
                        case STRZELECKIE:
                            if (rzedyPlanszyDocelowejJednostek.length > 1 && rzedyPlanszyDocelowejJednostek[1] != null) {
                                podswietlElement(rzedyPlanszyDocelowejJednostek[1]);
                                cokolwiekPodswietlono = true;
                            }
                            break;
                        case OBLEZENIE:
                            if (rzedyPlanszyDocelowejJednostek.length > 2 && rzedyPlanszyDocelowejJednostek[2] != null) {
                                podswietlElement(rzedyPlanszyDocelowejJednostek[2]);
                                cokolwiekPodswietlono = true;
                            }
                            break;
                    }
                }
                System.out.println();
            } else {
                System.err.println("[KONTROLER] rzedyPlanszyDocelowejJednostek są null dla karty jednostki/bohatera " + karta.getNazwa());
            }

            if (!cokolwiekPodswietlono && !mozliweTypyRzedow.isEmpty()) { // Sprawdzamy też czy były jakieś możliwe rzędy
                wyswietlKomunikatNaPlanszy("Brak dostępnych rzędów dla karty " + karta.getNazwa() + " na odpowiedniej planszy.", true);
                resetujStanZaznaczonejKarty();
            }
        }
    }
    private void podswietlElement(Node element) {
        if (element == null) return;
        String oryginalnyStyl = element.getStyle() != null ? element.getStyle() : "";
        oryginalnyStyl = oryginalnyStyl.replaceAll("-fx-background-color: rgba\\(255, 255, 0, 0.[34]\\);?", "").replaceAll("-fx-border-style: dashed;?", "").trim();
        if (oryginalnyStyl.endsWith(";") && !oryginalnyStyl.isEmpty()) oryginalnyStyl += " ";
        else if (!oryginalnyStyl.isEmpty() && !oryginalnyStyl.endsWith(";")) oryginalnyStyl += "; ";
        element.setStyle(oryginalnyStyl + "-fx-background-color: rgba(255, 255, 0, 0.3); -fx-border-style: dashed;");
        podswietloneCele.add(element);
    }

    private void usunPodswietlenieRzedowISlotow() {
        for (Node element : podswietloneCele) {
            if (element == null) continue;
            String stylPodstawowy = "-fx-background-color: transparent;";
            // Jeśli HBoxy (rzędy) mają mieć zachowany padding
            if (element instanceof HBox &&
                    (element == rzadPiechotyG1HBox || element == rzadStrzeleckiG1HBox || element == rzadOblezeniaG1HBox ||
                            element == rzadPiechotyG2HBox || element == rzadStrzeleckiG2HBox || element == rzadOblezeniaG2HBox ||
                            element == rekaGracza1HBox || element == rekaGracza2HBox || element == pogodaHBox)) {
                stylPodstawowy += " -fx-padding: 2px;";
            }
            // Dla StackPane (sloty Rogu) nie było oryginalnie paddingu w FXML, więc -fx-background-color: transparent; wystarczy.
            element.setStyle(stylPodstawowy);
        }
        podswietloneCele.clear();
    }
    @FXML
    private void handleCelKlikniety(MouseEvent event) {
        // Sprawdź, czy w ogóle można teraz wykonywać akcje na planszy
        Gracz aktywnyGracz = (silnikGry != null) ? silnikGry.getGraczAktualnejTury() : null;

        // Jeśli nie ma aktywnego gracza, lub silnik nie istnieje, lub jest oczekiwanie na potwierdzenie tury,
        // to generalnie nie powinniśmy przetwarzać kliknięć na cele (chyba że to panel potwierdzenia).
        // Ten handler jest dla kliknięć na rzędy/sloty po wybraniu karty.
        if (silnikGry == null || aktywnyGracz == null || silnikGry.isOczekiwanieNaPotwierdzenieTury() || silnikGry.isCzyGraZakonczona()) {
            if (silnikGry != null && silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                wyswietlKomunikatNaPlanszy("Poczekaj na potwierdzenie tury przez gracza.", false);
            }
            // Jeśli kliknięto, a nie powinno się dać, na wszelki wypadek resetuj stan wyboru
            if (aktualnieZaznaczonaKartaDoZagrnia != null || trybWyboruCeluDlaManekina || aktualnieWybieranyRzadPoWskrzeszeniu) {
                resetujStanZaznaczonejKarty();
            }
            return;
        }

        // Sprawdź, czy gracz, którego jest tura, może teraz grać
        boolean czyMogeTerazGrac = (aktywnyGracz == obiektGracz1 && !obiektGracz1.isCzySpasowalWRundzie()) ||
                (aktywnyGracz == obiektGracz2 && pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat() && !obiektGracz2.isCzySpasowalWRundzie());

        if (!czyMogeTerazGrac && !(trybWyboruCeluDlaManekina || aktualnieWybieranyRzadPoWskrzeszeniu)) {
            // Jeśli nie jest to specjalny tryb wyboru, a gracz nie może grać (np. spasował), to nic nie rób
            resetujStanZaznaczonejKarty();
            return;
        }

        // Jeśli nie ma zaznaczonej karty do zagrania I nie jesteśmy w żadnym trybie wyboru celu, to kliknięcie na rząd nic nie robi
        if (aktualnieZaznaczonaKartaDoZagrnia == null && !trybWyboruCeluDlaManekina && !aktualnieWybieranyRzadPoWskrzeszeniu) {
            usunPodswietlenieRzedowISlotow(); // Na wszelki wypadek
            return;
        }

        Object source = event.getSource();
        TypRzeduEnum wybranyRzadEnum = null;

        // Sprawdź, czy kliknięto na podświetlony (dozwolony) cel
        if (!podswietloneCele.contains((Node) source)) {
            System.out.println("[KONTROLER] Kliknięto na niepodświetlony element. Anulowanie aktywnego wyboru.");

            // Sprawdź, czy byliśmy w jakimś specjalnym trybie wyboru, który trzeba anulować w silniku
            if (aktualnieWybieranyRzadPoWskrzeszeniu && silnikGry != null && aktywnyGracz != null) {
                System.out.println("  > Anulowanie trybu wskrzeszania.");
                Gracz graczWskrzeszajacy = silnikGry.getGraczKladacyKartePoWskrzeszeniu();
                if (graczWskrzeszajacy == null) graczWskrzeszajacy = aktywnyGracz; // Fallback
                silnikGry.anulujWskrzeszanie(graczWskrzeszajacy);
            }
            else if (trybWyboruCeluDlaManekina && silnikGry != null && silnikGry.getGraczAktualnejTury() != null) {
                System.out.println("  > Anulowanie trybu wyboru celu dla Manekina.");
                silnikGry.anulujZagranieManekina(silnikGry.getGraczAktualnejTury(), false);
            }
            resetujStanZaznaczonejKarty();
            return;
        }

        // --- POCZĄTEK BLOKU Z DEBUGAMI DLA KART POGODY ---
        if (source == pogodaHBox && aktualnieZaznaczonaKartaDoZagrnia != null &&
                aktualnieZaznaczonaKartaDoZagrnia.getTyp() == TypKartyEnum.SPECJALNA) {

            System.out.println("[DEBUG handleCelKlikniety] Kliknięto pogodaHBox.");
            System.out.println("[DEBUG handleCelKlikniety] Zaznaczona karta: " + aktualnieZaznaczonaKartaDoZagrnia.getNazwa() + ", Typ: " + aktualnieZaznaczonaKartaDoZagrnia.getTyp());

            String nazwaKarty = aktualnieZaznaczonaKartaDoZagrnia.getNazwa().toLowerCase();
            System.out.println("[DEBUG handleCelKlikniety] Nazwa karty (lowercase): '" + nazwaKarty + "'");

            boolean czyToKartaPogody = nazwaKarty.contains("mróz") || nazwaKarty.contains("mgła") || nazwaKarty.contains("deszcz");
            System.out.println("[DEBUG handleCelKlikniety] Czy rozpoznano jako kartę pogody (mróz/mgła/deszcz)? " + czyToKartaPogody);

            if (czyToKartaPogody) {
                if (silnikGry != null && aktywnyGracz != null) { // aktywnyGracz powinien być już sprawdzony, ale dla pewności
                    System.out.println("[KONTROLER] Wybrano slot pogody dla karty: " + aktualnieZaznaczonaKartaDoZagrnia.getNazwa() + ". Wywołuję silnikGry.zagrajKarte...");
                    silnikGry.zagrajKarte(aktywnyGracz, aktualnieZaznaczonaKartaDoZagrnia, null);
                    System.out.println("[KONTROLER] silnikGry.zagrajKarte wywołane.");
                } else {
                    System.err.println("[KONTROLER BŁĄD DEBUG] silnikGry lub aktywnyGracz jest null przy próbie zagrania pogody!");
                }
                resetujStanZaznaczonejKarty();
                return; // Akcja obsłużona
            } else {
                System.out.println("[KONTROLER DEBUG] Kliknięto pogodaHBox z kartą specjalną, ale NIE rozpoznano jej jako 'mróz', 'mgła' ani 'deszcz'. Karta: " + aktualnieZaznaczonaKartaDoZagrnia.getNazwa());
                // Jeśli to nie była karta pogody, a pogodaHBox został kliknięty,
                // pozwalamy kodowi kontynuować, aby sprawdzić, czy to nie jest np. błąd w podświetlaniu.
                // Jednak poprawnie działająca `podswietlMozliweCeleDlaKarty` powinna podświetlić `pogodaHBox`
                // tylko dla kart pogody.
            }
        }
        // --- KONIEC BLOKU Z DEBUGAMI DLA KART POGODY ---

        if (source instanceof HBox) {
            HBox kliknietyRzadHBox = (HBox) source;
            if (kliknietyRzadHBox == rzadPiechotyG1HBox || kliknietyRzadHBox == rzadPiechotyG2HBox) wybranyRzadEnum = TypRzeduEnum.PIECHOTA;
            else if (kliknietyRzadHBox == rzadStrzeleckiG1HBox || kliknietyRzadHBox == rzadStrzeleckiG2HBox) wybranyRzadEnum = TypRzeduEnum.STRZELECKIE;
            else if (kliknietyRzadHBox == rzadOblezeniaG1HBox || kliknietyRzadHBox == rzadOblezeniaG2HBox) wybranyRzadEnum = TypRzeduEnum.OBLEZENIE;
            // celowo nie obsługujemy tutaj pogodaHBox jako rzędu jednostek
        } else if (source instanceof StackPane && aktualnieZaznaczonaKartaDoZagrnia != null &&
                aktualnieZaznaczonaKartaDoZagrnia.getTyp() == TypKartyEnum.SPECJALNA &&
                aktualnieZaznaczonaKartaDoZagrnia.getNazwa().toLowerCase().contains("róg dowódcy")) {
            StackPane kliknietySlotRogu = (StackPane) source;
            boolean czySlotGracza1 = kliknietySlotRogu == rogPiechotyG1Slot || kliknietySlotRogu == rogStrzeleckiG1Slot || kliknietySlotRogu == rogOblezeniaG1Slot;
            boolean czySlotGracza2 = kliknietySlotRogu == rogPiechotyG2Slot || kliknietySlotRogu == rogStrzeleckiG2Slot || kliknietySlotRogu == rogOblezeniaG2Slot;

            if (!((aktywnyGracz == obiektGracz1 && czySlotGracza1) || (aktywnyGracz == obiektGracz2 && czySlotGracza2))) {
                wyswietlKomunikatNaPlanszy("Róg Dowódcy można zagrać tylko na własne rzędy.", true);
                resetujStanZaznaczonejKarty();
                return;
            }
            // Sprawdzenie czy slot jest pusty (ważne, jeśli Róg to karta w slocie)
            if (kliknietySlotRogu.getChildren().isEmpty()) {
                if (kliknietySlotRogu == rogPiechotyG1Slot || kliknietySlotRogu == rogPiechotyG2Slot) wybranyRzadEnum = TypRzeduEnum.PIECHOTA;
                else if (kliknietySlotRogu == rogStrzeleckiG1Slot || kliknietySlotRogu == rogStrzeleckiG2Slot) wybranyRzadEnum = TypRzeduEnum.STRZELECKIE;
                else if (kliknietySlotRogu == rogOblezeniaG1Slot || kliknietySlotRogu == rogOblezeniaG2Slot) wybranyRzadEnum = TypRzeduEnum.OBLEZENIE;
            } else {
                wyswietlKomunikatNaPlanszy("Ten slot na Róg Dowódcy jest już zajęty.", false);
                resetujStanZaznaczonejKarty();
                return;
            }
        }

        if (wybranyRzadEnum != null && aktualnieZaznaczonaKartaDoZagrnia != null && aktywnyGracz != null) {
            if (aktualnieWybieranyRzadPoWskrzeszeniu) {
                Gracz graczWskrzeszajacy = silnikGry.getGraczKladacyKartePoWskrzeszeniu();
                if (graczWskrzeszajacy == null) graczWskrzeszajacy = aktywnyGracz;

                System.out.println("[KONTROLER] Wybrano rząd " + wybranyRzadEnum + " dla wskrzeszanej karty " + aktualnieZaznaczonaKartaDoZagrnia.getNazwa());
                silnikGry.polozWskrzeszonaKarteNaRzedzie(graczWskrzeszajacy, aktualnieZaznaczonaKartaDoZagrnia, wybranyRzadEnum);
            } else {
                System.out.println("[KONTROLER] Wybrano rząd " + wybranyRzadEnum + " dla karty z ręki " + aktualnieZaznaczonaKartaDoZagrnia.getNazwa());
                silnikGry.zagrajKarte(aktywnyGracz, aktualnieZaznaczonaKartaDoZagrnia, wybranyRzadEnum);
            }
        } else if (trybWyboruCeluDlaManekina) {
            System.out.println("[KONTROLER] Kliknięto na rząd w trybie Manekina, ale nie wybrano jednostki. Anulowanie.");
            if (silnikGry != null && silnikGry.getGraczAktualnejTury() != null) {
                silnikGry.anulujZagranieManekina(silnikGry.getGraczAktualnejTury(), false);
            }
        } else if (aktualnieZaznaczonaKartaDoZagrnia != null &&
                !(source == pogodaHBox && // Jeśli nie obsłużono już pogody
                        aktualnieZaznaczonaKartaDoZagrnia.getTyp() == TypKartyEnum.SPECJALNA &&
                        (aktualnieZaznaczonaKartaDoZagrnia.getNazwa().toLowerCase().contains("mróz") ||
                                aktualnieZaznaczonaKartaDoZagrnia.getNazwa().toLowerCase().contains("mgła") ||
                                aktualnieZaznaczonaKartaDoZagrnia.getNazwa().toLowerCase().contains("deszcz"))
                )) {
            // Ten blok `else` jest na wypadek, gdybyśmy doszli tutaj z zaznaczoną kartą,
            // ale nie został spełniony żaden z wcześniejszych warunków zagrania (np. wybranyRzadEnum jest null,
            // a nie jesteśmy w trybie manekina i nie jest to karta pogody kliknięta na slot pogody).
            // To może oznaczać np. próbę zagrania karty specjalnej (nie-pogody) na rząd jednostek, co jest niepoprawne.
            System.out.println("[KONTROLER] Nie udało się ustalić prawidłowej akcji dla kliknięcia z zaznaczoną kartą: " + aktualnieZaznaczonaKartaDoZagrnia.getNazwa());
        }

        resetujStanZaznaczonejKarty();
    }

    public void pokazPanelWyboruKartyPogodyDlaEredina(List<Karta> kartyPogody) {
        if (panelWyboruKartyEredina == null || tytulPaneluWyboruKartyEredina == null ||
                flowPaneKartDoWyboruEredina == null || powiekszonaKartaWyboruEredina == null ||
                przyciskPotwierdzWyborEredina == null || przyciskAnulujWyborEredina == null) {
            System.err.println("KONTROLER: Błąd krytyczny - elementy panelu wyboru karty dla Eredina są null.");
            if (silnikGry != null) silnikGry.anulujWyborPogodyPrzezEredina(true); // true = błąd UI
            return;
        }

        this.wybranaKartaPrzezEredinaDoPodgladu = null;
        tytulPaneluWyboruKartyEredina.setText("Eredin: Wybierz kartę pogody z talii");
        flowPaneKartDoWyboruEredina.getChildren().clear();
        powiekszonaKartaWyboruEredina.setImage(null);
        przyciskPotwierdzWyborEredina.setDisable(true);

        if (kartyPogody == null || kartyPogody.isEmpty()) {
            // Ten przypadek powinien być obsłużony w SilnikuGry, ale dla pewności:
            Label brakKartLabel = new Label("Brak kart pogody w talii.");
            brakKartLabel.setTextFill(Color.WHITE);
            flowPaneKartDoWyboruEredina.getChildren().add(brakKartLabel);
            // Można też pokazać to w miejscu opisu karty
        } else {
            for (Karta karta : kartyPogody) {
                ImageView miniatura = stworzWidokKarty(karta, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY);
                miniatura.setUserData(karta);
                miniatura.setCursor(Cursor.HAND);

                miniatura.setOnMouseClicked(event -> {
                    this.wybranaKartaPrzezEredinaDoPodgladu = karta;
                    powiekszonaKartaWyboruEredina.setImage(ladujObrazekKarty(karta));
                    przyciskPotwierdzWyborEredina.setDisable(false);
                    // Podświetl wybraną miniaturę
                    for(Node node : flowPaneKartDoWyboruEredina.getChildren()){
                        if(node instanceof ImageView){
                            node.setEffect(node.getUserData() == karta ? new DropShadow(15, Color.LIGHTSKYBLUE) : null);
                        }
                    }
                });

                DropShadow hoverEffect = new DropShadow(10, Color.GOLD);
                miniatura.setOnMouseEntered(e -> {
                    if (this.wybranaKartaPrzezEredinaDoPodgladu != karta) miniatura.setEffect(hoverEffect);
                });
                miniatura.setOnMouseExited(e -> {
                    if (this.wybranaKartaPrzezEredinaDoPodgladu != karta) miniatura.setEffect(null);
                });
                flowPaneKartDoWyboruEredina.getChildren().add(miniatura);
            }
            // Opcjonalnie: automatycznie zaznacz pierwszą kartę, jeśli lista nie jest pusta
            if (!kartyPogody.isEmpty() && flowPaneKartDoWyboruEredina.getChildren().get(0) instanceof ImageView) {
                ImageView pierwszaMiniatura = (ImageView) flowPaneKartDoWyboruEredina.getChildren().get(0);
                // Symulacja kliknięcia, aby zainicjować podgląd i stan przycisku
                MouseEvent simMouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, javafx.scene.input.MouseButton.PRIMARY, 1,
                        false, false, false, false, true, false, false, true, false, false, null);
                pierwszaMiniatura.fireEvent(simMouseEvent);
            }
        }

        ukryjWszystkiePaneleOverlayOprocz(panelWyboruKartyEredina);
        panelWyboruKartyEredina.setVisible(true);
        panelWyboruKartyEredina.toFront();
    }

    public void pokazPanelWyboruKartDoOdrzuceniaEredin414(List<Karta> rekaGracza) {
        if (panelWyboruOdrzucanychEredin414 == null /* ...sprawdź resztę FXML dla tego panelu... */) {
            System.err.println("KONTROLER: Błąd UI - Panel odrzucania Eredina 414 niekompletny.");
            if (silnikGry != null) silnikGry.anulujZdolnoscEredina414(true);
            return;
        }
        aktualnieWybraneDoOdrzuceniaEredin414.clear();
        aktualnieWybranaDoPowiekszeniaEredin414 = null;
        powiekszonaKartaOdrzucanaEredin414.setImage(null);
        infoWyboruOdrzucanychEredin414.setText("Wybierz 2 karty do odrzucenia (0/2)");
        przyciskPotwierdzOdrzucenieEredin414.setDisable(true);
        flowPaneOdrzucanychEredin414.getChildren().clear();

        for (Karta kartaZReki : rekaGracza) {
            ImageView miniatura = stworzWidokKarty(kartaZReki, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY);
            miniatura.setUserData(kartaZReki);
            miniatura.setCursor(Cursor.HAND);

            miniatura.setOnMouseClicked(event -> {
                Karta kliknieta = (Karta) miniatura.getUserData();
                if (aktualnieWybraneDoOdrzuceniaEredin414.contains(kliknieta)) {
                    aktualnieWybraneDoOdrzuceniaEredin414.remove(kliknieta);
                    miniatura.setEffect(null); // Usuń podświetlenie
                } else {
                    if (aktualnieWybraneDoOdrzuceniaEredin414.size() < 2) {
                        aktualnieWybraneDoOdrzuceniaEredin414.add(kliknieta);
                        miniatura.setEffect(new DropShadow(15, Color.ORANGERED)); // Podświetl wybraną do odrzucenia
                    }
                }
                infoWyboruOdrzucanychEredin414.setText("Wybierz 2 karty do odrzucenia (" + aktualnieWybraneDoOdrzuceniaEredin414.size() + "/2)");
                przyciskPotwierdzOdrzucenieEredin414.setDisable(aktualnieWybraneDoOdrzuceniaEredin414.size() != 2);

                // Podgląd ostatnio klikniętej/wybranej
                aktualnieWybranaDoPowiekszeniaEredin414 = kliknieta;
                powiekszonaKartaOdrzucanaEredin414.setImage(ladujObrazekKarty(kliknieta));
            });
            // Prosty hover dla podglądu
            miniatura.setOnMouseEntered(e -> powiekszonaKartaOdrzucanaEredin414.setImage(ladujObrazekKarty((Karta)miniatura.getUserData())));
            miniatura.setOnMouseExited(e -> {
                if(aktualnieWybranaDoPowiekszeniaEredin414 != (Karta)miniatura.getUserData() || aktualnieWybraneDoOdrzuceniaEredin414.contains((Karta)miniatura.getUserData())) {
                    // Jeśli kursor opuścił kartę, która nie jest tą "głównie" powiększoną (ostatnio klikniętą)
                    // LUB jeśli jest wybrana do odrzucenia, to zostawiamy podgląd ostatnio klikniętej/wybranej.
                    // Można też czyścić podgląd: powiekszonaKartaOdrzucanaEredin414.setImage(null);
                    // Dla prostoty, zostawmy podgląd ostatnio klikniętej.
                } else {
                    powiekszonaKartaOdrzucanaEredin414.setImage(ladujObrazekKarty(aktualnieWybranaDoPowiekszeniaEredin414)); // Przywróć ostatnio klikniętą
                }
            });
            flowPaneOdrzucanychEredin414.getChildren().add(miniatura);
        }

        ukryjWszystkiePaneleOverlayOprocz(panelWyboruOdrzucanychEredin414);
        panelWyboruOdrzucanychEredin414.setVisible(true);
        panelWyboruOdrzucanychEredin414.toFront();
    }

    @FXML
    private void handlePotwierdzOdrzucenieEredin414Action() {
        if (silnikGry != null && aktualnieWybraneDoOdrzuceniaEredin414.size() == 2) {
            panelWyboruOdrzucanychEredin414.setVisible(false);
            silnikGry.potwierdzWyborKartDoOdrzuceniaEredin414(new ArrayList<>(aktualnieWybraneDoOdrzuceniaEredin414));
        } else {
            infoWyboruOdrzucanychEredin414.setText("Błąd: Musisz wybrać dokładnie 2 karty.");
        }
    }

    @FXML
    private void handleAnulujOdrzucenieEredin414Action() {
        panelWyboruOdrzucanychEredin414.setVisible(false);
        if (silnikGry != null) {
            silnikGry.anulujZdolnoscEredina414(false); // false = anulowanie przez gracza
        }
    }

    // Metoda do pokazywania panelu wyboru 1 karty z talii
    public void pokazPanelWyboruKartyZTalliEredin414(List<Karta> taliaGracza) {
        if (panelWyboruDobieranejEredin414 == null /* ...sprawdź resztę FXML dla tego panelu... */) {
            System.err.println("KONTROLER: Błąd UI - Panel dobierania Eredina 414 niekompletny.");
            if (silnikGry != null) silnikGry.anulujZdolnoscEredina414(true);
            return;
        }
        wybranaKartaZTaliiDlaEredina414 = null;
        aktualnieWybranaDoPowiekszeniaEredin414 = null; // Reset podglądu
        powiekszonaKartaDobieranaEredin414.setImage(null);
        przyciskPotwierdzDobranieEredin414.setDisable(true);
        flowPaneDobieranychEredin414.getChildren().clear();

        if (taliaGracza.isEmpty()) { // SilnikGry powinien to obsłużyć, ale jako fallback
            Label brakKartLabel = new Label("Twoja talia jest pusta!");
            brakKartLabel.setTextFill(Color.WHITE);
            flowPaneDobieranychEredin414.getChildren().add(brakKartLabel);
        } else {
            for (Karta kartaZTali : taliaGracza) {
                ImageView miniatura = stworzWidokKarty(kartaZTali, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY);
                miniatura.setUserData(kartaZTali);
                miniatura.setCursor(Cursor.HAND);

                miniatura.setOnMouseClicked(event -> {
                    Karta kliknieta = (Karta) miniatura.getUserData();
                    wybranaKartaZTaliiDlaEredina414 = kliknieta;
                    powiekszonaKartaDobieranaEredin414.setImage(ladujObrazekKarty(kliknieta));
                    przyciskPotwierdzDobranieEredin414.setDisable(false);
                    // Podświetl wybraną
                    for(Node node : flowPaneDobieranychEredin414.getChildren()){
                        if(node instanceof ImageView){
                            node.setEffect(node.getUserData() == kliknieta ? new DropShadow(15, Color.LIGHTGREEN) : null);
                        }
                    }
                });
                // Prosty hover dla podglądu
                miniatura.setOnMouseEntered(e -> powiekszonaKartaDobieranaEredin414.setImage(ladujObrazekKarty((Karta)miniatura.getUserData())));
                miniatura.setOnMouseExited(e -> {
                    if(wybranaKartaZTaliiDlaEredina414 != (Karta)miniatura.getUserData()){
                        // Jeśli kursor opuścił kartę, która nie jest wybraną, przywróć podgląd wybranej (lub wyczyść)
                        powiekszonaKartaDobieranaEredin414.setImage(ladujObrazekKarty(wybranaKartaZTaliiDlaEredina414));
                    }
                });
                flowPaneDobieranychEredin414.getChildren().add(miniatura);
            }
            // Opcjonalnie automatycznie wybierz pierwszą, jeśli chcesz
            if (!taliaGracza.isEmpty() && flowPaneDobieranychEredin414.getChildren().get(0) instanceof ImageView) {
                MouseEvent simMouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, javafx.scene.input.MouseButton.PRIMARY, 1,
                        false, false, false, false, true, false, false, true, false, false, null);
                ((ImageView)flowPaneDobieranychEredin414.getChildren().get(0)).fireEvent(simMouseEvent);
            }
        }
        ukryjWszystkiePaneleOverlayOprocz(panelWyboruDobieranejEredin414);
        panelWyboruDobieranejEredin414.setVisible(true);
        panelWyboruDobieranejEredin414.toFront();
    }

    @FXML
    private void handlePotwierdzDobranieEredin414Action() {
        if (silnikGry != null && wybranaKartaZTaliiDlaEredina414 != null) {
            panelWyboruDobieranejEredin414.setVisible(false);
            silnikGry.wykonajWyborKartyZTalliEredin414(wybranaKartaZTaliiDlaEredina414);
        } else {
            // Komunikat błędu, jeśli potrzebny
        }
    }

    @FXML
    private void handleAnulujDobranieEredin414Action() {
        // Anulowanie na tym etapie (po odrzuceniu kart) jest problematyczne.
        // Najprościej: zdolność jest marnowana. Gracz już odrzucił karty.
        panelWyboruDobieranejEredin414.setVisible(false);
        if (silnikGry != null) {
            // Informujemy silnik, że gracz anulował na etapie dobierania.
            // Silnik powinien zużyć zdolność i zakończyć turę.
            // Karty do odrzucenia są już w silniku, więc silnik musi je odrzucić.
            silnikGry.anulujZdolnoscEredina414(false); // false = anulowanie przez gracza (ale po pierwszym kroku)
        }
    }


    @FXML
    private void handlePotwierdzWyborEredinaAction() {
        if (silnikGry != null && this.wybranaKartaPrzezEredinaDoPodgladu != null) {
            panelWyboruKartyEredina.setVisible(false);
            Karta wybrana = this.wybranaKartaPrzezEredinaDoPodgladu;
            this.wybranaKartaPrzezEredinaDoPodgladu = null; // Reset
            silnikGry.wykonajWyborPogodyPrzezEredina(wybrana);
        } else {
            System.err.println("KONTROLER: Nie można potwierdzić wyboru dla Eredina - brak wybranej karty lub silnika.");
            if(panelWyboruKartyEredina != null) panelWyboruKartyEredina.setVisible(false);
            this.wybranaKartaPrzezEredinaDoPodgladu = null; // Reset
            if(silnikGry != null) silnikGry.anulujWyborPogodyPrzezEredina(true); // true = błąd
        }
    }

    @FXML
    private void handleAnulujWyborEredinaAction() {
        if (panelWyboruKartyEredina != null) {
            panelWyboruKartyEredina.setVisible(false);
        }
        this.wybranaKartaPrzezEredinaDoPodgladu = null; // Reset
        if (silnikGry != null) {
            silnikGry.anulujWyborPogodyPrzezEredina(false); // false = anulowanie przez gracza
        }
    }

    private void pokazPanelAktywacjiDowodcy(Gracz gracz) {
        // Usunięto 'opisUmiejetnosciDowodcyLabel' ze sprawdzenia
        if (panelAktywacjiDowodcy == null || powiekszonaKartaDowodcyImageView == null ||
                przyciskUzyjUmiejetnosciDowodcy == null ||
                gracz == null || gracz.getKartaDowodcy() == null || gracz.getProfilUzytkownika() == null) {

            System.err.println("Błąd: Nie można pokazać panelu aktywacji dowódcy - brak elementów UI lub danych gracza/karty.");
            wyswietlKomunikatNaPlanszy("Błąd panelu zdolności dowódcy.", true);
            return;
        }

        Karta kartaDowodcy = gracz.getKartaDowodcy();
        powiekszonaKartaDowodcyImageView.setImage(ladujObrazekKarty(kartaDowodcy));

        // Blok kodu ustawiający tekst dla 'opisUmiejetnosciDowodcyLabel' został całkowicie usunięty.

        przyciskUzyjUmiejetnosciDowodcy.setDisable(gracz.isZdolnoscDowodcyUzyta());
        panelAktywacjiDowodcy.setUserData(gracz);
        ukryjWszystkiePaneleOverlayOprocz(panelAktywacjiDowodcy);
        panelAktywacjiDowodcy.setVisible(true);
        panelAktywacjiDowodcy.toFront();
        System.out.println("Panel aktywacji dowódcy pokazany dla: " + gracz.getProfilUzytkownika().getNazwaUzytkownika());
    }
    private void ukryjWszystkiePaneleOverlayOprocz(StackPane panelDoPozostawieniaWidocznym) {
        StackPane[] wszystkiePanele = {
                panelRzutuMonetaOverlay, panelZmianyTury, panelInfoPas,
                panelWynikuRundy, panelKoncaGry, panelAktywacjiDowodcy,
                panelCmentarza, panelUmiejetnosciPotworow, panelWyboruKartyEredina, panelWyboruKartyEredina, panelWyboruOdrzucanychEredin414, panelWyboruDobieranejEredin414, panelWyboruRozpoczynajacegoScoiatael, panelOpcjiGry
        };
        for (StackPane panel : wszystkiePanele) {
            if (panel != null && panel != panelDoPozostawieniaWidocznym) {
                panel.setVisible(false);
            }
        }
    }
    @FXML private void handleDowodcaG1Clicked(MouseEvent event) {
        if (silnikGry != null && silnikGry.getGraczAktualnejTury() == obiektGracz1 && !obiektGracz1.isZdolnoscDowodcyUzyta() && !silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
            pokazPanelAktywacjiDowodcy(obiektGracz1);
        } else {
            wyswietlKomunikatNaPlanszy("Nie możesz teraz użyć zdolności dowódcy.", false);
        }
    }
    @FXML private void handleDowodcaG2Clicked(MouseEvent event) {
        if (silnikGry != null && silnikGry.getGraczAktualnejTury() == obiektGracz2 && !obiektGracz2.isZdolnoscDowodcyUzyta() && (pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat()) && !silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
            pokazPanelAktywacjiDowodcy(obiektGracz2);
        } else {
            wyswietlKomunikatNaPlanszy("Nie tura przeciwnika lub zdolność już użyta.", false);
        }
    }

    @FXML
    private void handleUzyjUmiejetnosciDowodcyAction() {
        Gracz graczAktywujacy = (panelAktywacjiDowodcy != null) ? (Gracz) panelAktywacjiDowodcy.getUserData() : null;

        if (silnikGry != null && graczAktywujacy != null && !graczAktywujacy.isZdolnoscDowodcyUzyta()) {
            silnikGry.uzyjZdolnosciDowodcy(graczAktywujacy);
            // SilnikGry po użyciu zdolności wywoła kontrolerPlanszy.odswiezCalakolwiekPlansze(),
            // co zaktualizuje obrazek dowódcy na rewers.
        }
        if (panelAktywacjiDowodcy != null) {
            panelAktywacjiDowodcy.setVisible(false);
        }
    }

    @FXML
    private void handleAnulujUmiejetnoscDowodcyAction() {
        if (panelAktywacjiDowodcy != null) {
            panelAktywacjiDowodcy.setVisible(false);
        }
        // Ewentualne odblokowanie interfejsu, jeśli panel go blokował
        // if (silnikGry != null && silnikGry.getGraczAktualnejTury() != null) {
        //     uaktywnijInterfejsDlaTury(silnikGry.getGraczAktualnejTury() == obiektGracz1);
        // }
    }

    public void pokazPanelUmiejetnosciPotworow(String nazwaKartyWskrzeszonej) { // Możemy przekazać nazwę karty dla personalizacji
        if (panelUmiejetnosciPotworow == null || grafikaUmiejetnosciPotworowImageView == null || napisUmiejetnosciPotworowLabel == null) {
            System.err.println("Błąd: Nie można pokazać panelu umiejętności Potworów - brak elementów UI.");
            // Zamiast wyświetlać na panelu, można użyć wyswietlKomunikatNaPlanszy jako fallback
            wyswietlKomunikatNaPlanszy("Umiejętność Potworów: " + nazwaKartyWskrzeszonej + " pozostaje!", false);
            return;
        }

        if (obrazekPowiadPotwoPng != null) {
            grafikaUmiejetnosciPotworowImageView.setImage(obrazekPowiadPotwoPng);
        } else {
            grafikaUmiejetnosciPotworowImageView.setImage(null); // Jeśli obrazek nie załadowany
        }

        // Tekst jest już ustawiony w FXML, ale można go tu dostosować, np. dodając nazwę karty:
        // napisUmiejetnosciPotworowLabel.setText("Umiejętność Potworów: " + nazwaKartyWskrzeszonej + " pozostaje na planszy!");
        // Lub zostawić statyczny tekst z FXML:
        napisUmiejetnosciPotworowLabel.setText("Pasywna umiejętność frakcji Potwory wskrzesza losową kartę");


        ukryjWszystkiePaneleOverlayOprocz(panelUmiejetnosciPotworow); // Ukryj inne panele
        panelUmiejetnosciPotworow.setVisible(true);
        panelUmiejetnosciPotworow.toFront();

        // Automatyczne znikanie panelu
        PauseTransition pauza = new PauseTransition(Duration.seconds(3.0));
        pauza.setOnFinished(event -> {
            if (panelUmiejetnosciPotworow != null) {
                panelUmiejetnosciPotworow.setVisible(false);
            }
        });
        pauza.play();
    }


    public void rozpocznijSekwencjeZmianyTury() {
        if (silnikGry == null || obiektGracz1 == null || obiektGracz2 == null) {
            System.err.println("Błąd w rozpocznijSekwencjeZmianyTury: kluczowe obiekty są null");
            // Awaryjne przejście, jeśli coś poszło bardzo nie tak
            if (silnikGry != null && !silnikGry.isCzyGraZakonczona() && silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                silnikGry.potwierdzPrzejecieTury();
            }
            return;
        }

        // 1. Wyłącz możliwość interakcji na czas 3-sekundowej pauzy
        //    Gracz, który właśnie zagrał, i tak nie powinien móc nic zrobić.
        //    Przycisk pasowania i zdolności dowódcy powinny być nieaktywne.
        przyciskPasuj.setDisable(true);
        if (przyciskPasujG2 != null) przyciskPasujG2.setDisable(true);
        if (dowodcaGracza1ImageView != null) dowodcaGracza1ImageView.setDisable(true);
        if (dowodcaGracza2ImageView != null) dowodcaGracza2ImageView.setDisable(true);
        // Karty w rękach staną się nieaktywne przez logikę w uaktywnijInterfejsDlaTury po faktycznej zmianie tury.

        // 2. Uruchom 3-sekundową pauzę
        System.out.println("[KONTROLER PLANSZY] Rozpoczęto sekwencję zmiany tury. Czekanie 3s."); // LOG

        PauseTransition pauza = new PauseTransition(Duration.seconds(3));
        pauza.setOnFinished(event -> {
            System.out.println("[KONTROLER PLANSZY] Pauza 3s zakończona. Próba pokazania panelu."); // LOG
            Platform.runLater(() -> {
                System.out.println("[KONTROLER PLANSZY] Wewnątrz Platform.runLater przed pokazaniem panelu.");
                if (silnikGry != null) {
                    odswiezCalakolwiekPlansze();
                    Gracz nastepnyGraczDoTury;
                    if (silnikGry.getGraczAktualnejTury() == obiektGracz1) { // GraczAktualnejTury to ten, kto właśnie zagrał
                        nastepnyGraczDoTury = obiektGracz2;
                    } else {
                        nastepnyGraczDoTury = obiektGracz1;
                    }
                    System.out.println("[KONTROLER PLANSZY] Następny gracz do tury (dla panelu): " + (nastepnyGraczDoTury != null ? nastepnyGraczDoTury.getProfilUzytkownika().getNazwaUzytkownika() : "null"));
                    silnikGry.setGraczOczekujacyNaPotwierdzenie(nastepnyGraczDoTury); // <<< Ustawiamy w silniku, kto ma potwierdzić
                    pokazPanelPrzejeciaTury(nastepnyGraczDoTury);
                } else{
                    System.out.println("[KONTROLER PLANSZY] SilnikGry jest null po pauzie, nie można pokazać panelu."); // LOG
                }
            });
        });
        pauza.play();
    }
    public void pokazPanelPrzejeciaTury(Gracz graczKtoregoTuraNadchodzi) {
        System.out.println("[KONTROLER PLANSZY] Wejście do pokazPanelPrzejeciaTury dla gracza: " +
                (graczKtoregoTuraNadchodzi != null ? graczKtoregoTuraNadchodzi.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                " (Spasował? " + (graczKtoregoTuraNadchodzi != null ? graczKtoregoTuraNadchodzi.isCzySpasowalWRundzie() : "N/A") + ")");

        if (panelZmianyTury == null) {
            System.err.println("[KONTROLER PLANSZY] panelZmianyTury jest null. Wychodzenie.");
            // Awaryjne, jeśli panel nie istnieje, a gra powinna iść dalej
            if (silnikGry != null && !silnikGry.isCzyGraZakonczona() && silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                silnikGry.potwierdzPrzejecieTury();
            }
            return;
        }
        if (silnikGry == null) {
            System.err.println("[KONTROLER PLANSZY] silnikGry jest null. Wychodzenie.");
            panelZmianyTury.setVisible(false); // Na wszelki wypadek ukryj, jeśli był widoczny
            return;
        }
        if (graczKtoregoTuraNadchodzi == null) {
            System.err.println("[KONTROLER PLANSZY] graczKtoregoTuraNadchodzi jest null. Wychodzenie.");
            if (silnikGry != null && !silnikGry.isCzyGraZakonczona() && silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                silnikGry.potwierdzPrzejecieTury(); // Awaryjne
            }
            return;
        }

        if (silnikGry.isCzyGraZakonczona()) {
            System.out.println("[KONTROLER PLANSZY] Gra zakończona, panel nie będzie pokazany.");
            panelZmianyTury.setVisible(false);
            return;
        }

        Gracz aktywnyPrzeciwnik = (graczKtoregoTuraNadchodzi == obiektGracz1) ? obiektGracz2 : obiektGracz1;
        if (aktywnyPrzeciwnik == null) { // Dodatkowe zabezpieczenie
            System.err.println("[KONTROLER PLANSZY] aktywnyPrzeciwnik jest null. To nie powinno się zdarzyć. Wychodzenie.");
            return;
        }
        System.out.println("[KONTROLER PLANSZY] Sprawdzanie warunków pasowania: Gracz panelu (" + graczKtoregoTuraNadchodzi.getProfilUzytkownika().getNazwaUzytkownika() +
                ") spasował: " + graczKtoregoTuraNadchodzi.isCzySpasowalWRundzie() +
                ". Przeciwnik (" + aktywnyPrzeciwnik.getProfilUzytkownika().getNazwaUzytkownika() +
                ") spasował: " + aktywnyPrzeciwnik.isCzySpasowalWRundzie());


        if (obiektGracz1.isCzySpasowalWRundzie() && obiektGracz2.isCzySpasowalWRundzie()) {
            System.out.println("[KONTROLER PLANSZY] Obaj gracze (obiektGracz1, obiektGracz2) spasowali. Panel niepotrzebny.");
            // Nie pokazuj panelu, silnik powinien zakończyć rundę.
            // Można rozważyć ukrycie panelu, jeśli byłby widoczny z jakiegoś powodu.
            panelZmianyTury.setVisible(false);
            return;
        }

        if (graczKtoregoTuraNadchodzi.isCzySpasowalWRundzie() && !aktywnyPrzeciwnik.isCzySpasowalWRundzie()) {
            System.out.println("[KONTROLER PLANSZY] Gracz panelu (" + graczKtoregoTuraNadchodzi.getProfilUzytkownika().getNazwaUzytkownika() +
                    ") już spasował, a przeciwnik (" + aktywnyPrzeciwnik.getProfilUzytkownika().getNazwaUzytkownika() +
                    ") jest aktywny. Automatyczne potwierdzenie tury.");
            silnikGry.potwierdzPrzejecieTury();
            return;
        }

        // Jeśli doszliśmy tutaj, panel powinien się pokazać
        napisTuryLabel.setText("Tura: " + graczKtoregoTuraNadchodzi.getProfilUzytkownika().getNazwaUzytkownika());
        if (graczKtoregoTuraNadchodzi == obiektGracz1) {
            grafikaTuryImageView.setImage(ladujObrazekZElementowUI(SCIEZKA_INFO_TURA_G1));
        } else {
            grafikaTuryImageView.setImage(ladujObrazekZElementowUI(SCIEZKA_INFO_TURA_G2));
        }

        System.out.println("[KONTROLER PLANSZY] Ustawianie panelZmianyTury.toFront() i setVisible(true)");
        panelZmianyTury.toFront();
        panelZmianyTury.setVisible(true);
        System.out.println("[KONTROLER PLANSZY] Panel przejęcia tury faktycznie pokazany dla: " + graczKtoregoTuraNadchodzi.getProfilUzytkownika().getNazwaUzytkownika());
    }
    @FXML
    private void handlePotwierdzTureAction() {
        if (panelZmianyTury == null || silnikGry == null) {
            System.err.println("Błąd w handlePotwierdzTureAction: panelZmianyTury lub silnikGry jest null.");
            return;
        }
        panelZmianyTury.setVisible(false);
        // SilnikGry po wywołaniu potwierdzPrzejecieTury() powinien:
        // 1. Ustawić oczekiwanieNaPotwierdzenieTury = false.
        // 2. Zmienić gracza aktualnej tury (jeśli to konieczne).
        // 3. Odświeżyć planszę (co odsłoni karty nowego gracza).
        // 4. Uaktywnić interfejs dla nowego gracza.
        silnikGry.potwierdzPrzejecieTury();
    }

    public void uaktywnijInterfejsDlaTury(boolean czyTuraGracza1) {
        boolean czyMoznaInterakcja = silnikGry != null && !silnikGry.isOczekiwanieNaPotwierdzenieTury() && !silnikGry.isCzyGraZakonczona();

        przyciskPasuj.setDisable(! (czyTuraGracza1 && czyMoznaInterakcja && obiektGracz1 != null && !obiektGracz1.isCzySpasowalWRundzie()) );
        if (przyciskPasujG2 != null) przyciskPasujG2.setDisable(! (!czyTuraGracza1 && czyMoznaInterakcja && (pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat()) && obiektGracz2 != null && !obiektGracz2.isCzySpasowalWRundzie()) );

        if (rekaGracza1HBox != null) {
            boolean interaktywnaRekaG1 = czyTuraGracza1 && czyMoznaInterakcja;
            for(Node n : rekaGracza1HBox.getChildren()){
                if(n instanceof ImageView) n.setDisable(!interaktywnaRekaG1);
            }
        }
        if (rekaGracza2HBox != null) {
            boolean interaktywnaRekaG2 = !czyTuraGracza1 && (pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat()) && czyMoznaInterakcja;
            for(Node n : rekaGracza2HBox.getChildren()){
                if(n instanceof ImageView) n.setDisable(!interaktywnaRekaG2);
            }
        }

        if (dowodcaGracza1ImageView != null && obiektGracz1 != null) {
            boolean moznaUzycG1 = czyTuraGracza1 && czyMoznaInterakcja && !obiektGracz1.isZdolnoscDowodcyUzyta();
            dowodcaGracza1ImageView.setDisable(!moznaUzycG1);
            dowodcaGracza1ImageView.setCursor(moznaUzycG1 ? Cursor.HAND : Cursor.DEFAULT);
        }
        if (dowodcaGracza2ImageView != null && obiektGracz2 != null) {
            boolean moznaUzycG2 = !czyTuraGracza1 && czyMoznaInterakcja && (pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat()) && !obiektGracz2.isZdolnoscDowodcyUzyta();
            dowodcaGracza2ImageView.setDisable(!moznaUzycG2);
            dowodcaGracza2ImageView.setCursor(moznaUzycG2 ? Cursor.HAND : Cursor.DEFAULT);
        }

        System.out.println("Interfejs uaktywniony dla gracza: " + (czyTuraGracza1 ? (obiektGracz1 != null ? obiektGracz1.getProfilUzytkownika().getNazwaUzytkownika() : "G1") : (obiektGracz2 != null ? obiektGracz2.getProfilUzytkownika().getNazwaUzytkownika() : "G2")));
    }

    public void wyswietlKomunikatNaPlanszy(String komunikat, boolean czyBlad) {
        System.out.println((czyBlad ? "BŁĄD GRY: " : "KOMUNIKAT GRY: ") + komunikat);
        Label labelDoKomunikatu = dodatkoweInfoG1Label;
        if (silnikGry != null && silnikGry.getGraczAktualnejTury() == obiektGracz2 && dodatkoweInfoG2Label != null) {
            labelDoKomunikatu = dodatkoweInfoG2Label;
        } else if (silnikGry == null && dodatkoweInfoG1Label == null && dodatkoweInfoG2Label != null){
            labelDoKomunikatu = dodatkoweInfoG2Label;
        } else if (dodatkoweInfoG1Label == null && dodatkoweInfoG2Label == null) {
            return;
        } else if (dodatkoweInfoG1Label == null) {
            labelDoKomunikatu = dodatkoweInfoG2Label;
        }

        if (labelDoKomunikatu != null) {
            labelDoKomunikatu.setText(komunikat);
            labelDoKomunikatu.setTextFill(czyBlad ? Color.RED : Color.LIGHTSKYBLUE);
        }
    }
    public void zablokujInterakcjePlanszy() {
        przyciskPasuj.setDisable(true);
        if (przyciskPasujG2 != null) przyciskPasujG2.setDisable(true);
        if (rekaGracza1HBox != null) rekaGracza1HBox.getChildren().forEach(n -> n.setDisable(true));
        if (rekaGracza2HBox != null) rekaGracza2HBox.getChildren().forEach(n -> n.setDisable(true));
        if (dowodcaGracza1ImageView != null) dowodcaGracza1ImageView.setDisable(true);
        if (dowodcaGracza2ImageView != null) dowodcaGracza2ImageView.setDisable(true);
        System.out.println("Interakcje na planszy zablokowane z powodu końca gry.");
    }

    @FXML
    private void handlePasujAction() {// Dla Gracza 1
        if (aktualnieZaznaczonaKartaDoZagrnia != null) {
            resetujStanZaznaczonejKarty(); // To usunie podświetlenie i odznaczy kartę
        }
        if (silnikGry != null && !silnikGry.isCzyGraZakonczona() &&
                silnikGry.getGraczAktualnejTury() == obiektGracz1 &&
                !obiektGracz1.isCzySpasowalWRundzie() &&
                !silnikGry.isOczekiwanieNaPotwierdzenieTury()) { // Dodatkowe sprawdzenie
            silnikGry.pasuj(obiektGracz1);
        } else {
            wyswietlKomunikatNaPlanszy("Nie możesz teraz spasować.", false);
        }
    }
    @FXML
    private void handlePasujG2Action() { // Dla Gracza 2
        if (aktualnieZaznaczonaKartaDoZagrnia != null) {
            resetujStanZaznaczonejKarty(); // To usunie podświetlenie i odznaczy kartę
        }
        if (silnikGry != null && !silnikGry.isCzyGraZakonczona() &&
                silnikGry.getGraczAktualnejTury() == obiektGracz2 &&
                (pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat()) &&
                !obiektGracz2.isCzySpasowalWRundzie() &&
                !silnikGry.isOczekiwanieNaPotwierdzenieTury()) { // Dodatkowe sprawdzenie
            silnikGry.pasuj(obiektGracz2);
        } else {
            wyswietlKomunikatNaPlanszy("Nie możesz teraz spasować.", false);
        }
    }

    @FXML
    private void handleOpcjeAction() {
        System.out.println("[KONTROLER PLANSZY] Przycisk 'Opcje' wciśnięty.");
        if (panelOpcjiGry != null) {
            if (silnikGry != null && silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                wyswietlKomunikatNaPlanszy("Poczekaj na potwierdzenie tury przez gracza.", false);
                return;
            }
            // Tutaj możesz wstrzymać timery lub inne dynamiczne elementy gry, jeśli takie masz
            // np. wstrzymajTimeryGry();

            ukryjWszystkiePaneleOverlayOprocz(panelOpcjiGry);
            panelOpcjiGry.setVisible(true);
            panelOpcjiGry.toFront();
        } else {
            System.err.println("Panel opcji gry nie został poprawnie załadowany!");
        }
    }

    @FXML
    private void handleOpcjeKontynuujAction() {
        if (panelOpcjiGry != null) {
            panelOpcjiGry.setVisible(false);
        }
        // Tutaj możesz wznowić timery gry, jeśli były wstrzymane
        // np. wznowTimeryGry();
        System.out.println("[KONTROLER PLANSZY] Wybrano 'Kontynuuj' z panelu opcji.");
    }

    @FXML
    private void handleOpcjeRestartGryAction() {
        System.out.println("[KONTROLER PLANSZY] Wybrano 'Restart Gry' z panelu opcji.");
        if (panelOpcjiGry != null) {
            panelOpcjiGry.setVisible(false);
        }

        if (obiektGracz1 != null && obiektGracz2 != null && pixelGwintAplikacja != null) {
            // Reset stanu graczy jest robiony wewnątrz inicjalizujGre -> graczX.resetDoNowejGry()
            // inicjalizujGre stworzy nowy silnik i rozpocznie grę od nowa z tymi samymi obiektami Gracz (ale zresetowanymi)
            // i tymi samymi taliami (które są częścią obiektów Gracz)
            // Rzut monetą/wybór Scoia'tael również odbędzie się ponownie.
            this.inicjalizujGre(this.obiektGracz1, this.obiektGracz2, null);
        } else {
            System.err.println("Nie można zrestartować gry - brak obiektów graczy lub aplikacji.");
            // Awaryjnie, wróć do menu
            handleOpcjePowrotDoMenuAction();
        }
    }

    @FXML
    private void handleOpcjePowrotDoMenuAction() {
        System.out.println("[KONTROLER PLANSZY] Wybrano 'Powrót do Menu' z panelu opcji.");
        if (panelOpcjiGry != null) {
            panelOpcjiGry.setVisible(false);
        }
        if (pixelGwintAplikacja != null) {
            // Użyj profilu gracza 1 jako zalogowanego użytkownika do powrotu do menu.
            // Jeśli grasz z dwoma różnymi zalogowanymi użytkownikami (co nie jest typowe dla hot-seat),
            // musiałbyś zdecydować, który profil jest "główny".
            Uzytkownik profilDoMenu = (obiektGracz1 != null && obiektGracz1.getProfilUzytkownika() != null)
                    ? obiektGracz1.getProfilUzytkownika()
                    : pixelGwintAplikacja.getProfilGracza1(); // Fallback na profil przechowywany w aplikacji

            if (profilDoMenu != null) {
                pixelGwintAplikacja.pokazMenuGlowne(profilDoMenu);
            } else {
                System.err.println("Nie można wrócić do menu - brak profilu użytkownika. Pokazywanie ekranu logowania.");
                pixelGwintAplikacja.pokazEkranLogowania();
            }
        }
    }

    public void resetujStanZaznaczonejKarty() {
        this.aktualnieZaznaczonaKartaDoZagrnia = null;
        this.trybWyboruCeluDlaManekina = false;
        this.aktualnieWybieranyRzadPoWskrzeszeniu = false;
        // Usuń wszelkie inne flagi stanu związane z wyborem karty/celu
        usunPodswietlenieRzedowISlotow();
        System.out.println("[KONTROLER] Zresetowano stan zaznaczonej karty i podświetleń.");
        // Nie ma potrzeby wywoływania uaktywnijInterfejsDlaTury tutaj,
        // zrobi to SilnikGry w anulujZagranieManekina lub gdy zagrajKarte zwróci false.
    }



    private void probaAktywacjiZdolnosciDowodcyG1() {
        if (silnikGry != null && silnikGry.getGraczAktualnejTury() == obiektGracz1 && !obiektGracz1.isZdolnoscDowodcyUzyta() && !silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
            pokazPanelAktywacjiDowodcy(obiektGracz1);
        } else {
            wyswietlKomunikatNaPlanszy("Nie możesz teraz użyć zdolności dowódcy.", false);
        }
    }

    @FXML
    private void handleZdolnoscDowodcyG1Action() { // Możesz dodać (ActionEvent event) jeśli potrzebujesz
        probaAktywacjiZdolnosciDowodcyG1();
    }

    public void pokazPanelCmentarzaDlaEredina415(Gracz gracz, String tytul) {
        // Ta metoda powinna już wywoływać Twoją główną, zrefaktoryzowaną metodę pokazPanelCmentarza
        pokazPanelCmentarza(gracz, tytul, TrybPaneluCmentarzaKontekst.EREDIN_ZABOJCA_AUBERONA_415);
    }

    public void pokazPanelCmentarzaDlaWyboru(Gracz graczKtoregoCmentarzPokazac, String tytul) {
        if (graczKtoregoCmentarzPokazac == null || panelCmentarza == null || tytulPaneluCmentarza == null ||
                cmentarzListaKartFlowPane == null || cmentarzPowiekszonaKartaImageView == null ||
                przyciskZamknijCmentarz == null || przyciskWskrzesKarteZCmentarza == null ) { // Zakładamy, że przyciskWskrzesKarteZCmentarza będzie reużywany
            System.err.println("Błąd: Nie można pokazać panelu cmentarza dla wyboru (Emhyr) - brak gracza lub kluczowych elementów UI.");
            if (silnikGry != null && silnikGry.getGraczAktualnejTury() != null) {
                silnikGry.anulujWyborDlaEmhyra(); // Poinformuj silnik o problemie
            }
            resetStatePoOperacjiNaCmentarzu(); // Resetuj stany kontrolera
            return;
        }

        resetStatePoOperacjiNaCmentarzu(); // Wyczyść poprzednie stany cmentarza na wszelki wypadek

        this.trybWyboruKartyDlaEmhyra = true;
        // this.graczPrzeciwnikaDlaEmhyra = graczKtoregoCmentarzPokazac; // Niepotrzebne, silnik zarządza tym, kto jest przeciwnikiem

        tytulPaneluCmentarza.setText(tytul);
        cmentarzListaKartFlowPane.getChildren().clear();
        cmentarzPowiekszonaKartaImageView.setImage(null);
        if (cmentarzOpisPowiekszonejKartyLabel != null) cmentarzOpisPowiekszonejKartyLabel.setText("Wybierz kartę z cmentarza przeciwnika...");

        List<Karta> kartyNaCmentarzu = new ArrayList<>(graczKtoregoCmentarzPokazac.getOdrzucone());
        Collections.reverse(kartyNaCmentarzu); // Pokaż ostatnio odrzucone jako pierwsze

        if (kartyNaCmentarzu.isEmpty()) {
            if (cmentarzOpisPowiekszonejKartyLabel != null) cmentarzOpisPowiekszonejKartyLabel.setText("Cmentarz przeciwnika jest pusty.");
            przyciskWskrzesKarteZCmentarza.setText("Wybierz Kartę"); // Ogólny tekst
            przyciskWskrzesKarteZCmentarza.setDisable(true);
            przyciskWskrzesKarteZCmentarza.setVisible(true); // Może być widoczny, ale wyłączony
            przyciskWskrzesKarteZCmentarza.setManaged(true);
        } else {
            for (Karta kartaZCmentarza : kartyNaCmentarzu) {
                ImageView miniatura = stworzWidokKarty(kartaZCmentarza, 70, 100);
                miniatura.setUserData(kartaZCmentarza);
                miniatura.setCursor(Cursor.HAND);
                miniatura.setOnMouseClicked(event -> handleCmentarzMiniaturaKartyKliknieta(kartaZCmentarza));

                DropShadow hoverEffect = new DropShadow(10, Color.GOLD);
                miniatura.setOnMouseEntered(e -> {
                    if (kartaPodgladanaZCmentarza != kartaZCmentarza) miniatura.setEffect(hoverEffect);
                });
                miniatura.setOnMouseExited(e -> {
                    if (kartaPodgladanaZCmentarza != kartaZCmentarza) miniatura.setEffect(null);
                });
                cmentarzListaKartFlowPane.getChildren().add(miniatura);
            }
            if (!kartyNaCmentarzu.isEmpty() && cmentarzPowiekszonaKartaImageView.getImage() == null) {
                handleCmentarzMiniaturaKartyKliknieta(kartyNaCmentarzu.get(0)); // Pokaż pierwszą kartę
            }
            przyciskWskrzesKarteZCmentarza.setText("Wybierz dla Emhyra"); // Zmień tekst przycisku
            przyciskWskrzesKarteZCmentarza.setVisible(true);
            przyciskWskrzesKarteZCmentarza.setManaged(true);
            przyciskWskrzesKarteZCmentarza.setDisable(kartaPodgladanaZCmentarza == null); // Włącz, jeśli coś jest już podglądane
        }

        przyciskZamknijCmentarz.setVisible(true);
        przyciskZamknijCmentarz.setManaged(true);

        ukryjWszystkiePaneleOverlayOprocz(panelCmentarza);
        panelCmentarza.setVisible(true);
        panelCmentarza.toFront();
    }

    public static enum TrybPaneluCmentarzaKontekst { // <<< ZMIEŃ NA PUBLIC STATIC
        PODGLAD,
        WSKRZESZANIE_MEDYK,
        EMHYR_PAN_POLUDNIA,
        EREDIN_ZABOJCA_AUBERONA_415
    }

    private TrybPaneluCmentarzaKontekst aktualnyTrybPaneluCmentarza = TrybPaneluCmentarzaKontekst.PODGLAD; // To pole pozostaje private


    public void pokazPanelMulligan(Gracz gracz) {
        if (gracz == null || panelMulligan == null) {
            System.err.println("BŁĄD: Nie można pokazać panelu Mulligan - gracz lub panel jest null.");
            // Awaryjne pominięcie fazy mulligan, jeśli coś pójdzie nie tak z UI
            if (silnikGry != null) silnikGry.pominMulligan(gracz);
            return;
        }

        // Reset stanu dla nowego gracza
        kartyWybraneDoWymiany.clear();
        mulliganHandPane.getChildren().clear();

        mulliganInfoLabel.setText(gracz.getProfilUzytkownika().getNazwaUzytkownika() + ", wymień do 2 kart");
        mulliganCounterLabel.setText("Wybrano: 0 / 2");

        // Wypełnij panel kartami z ręki gracza
        for (Karta karta : gracz.getReka()) {
            ImageView widokKarty = stworzWidokKarty(karta, 140, 203); // Nieco większe karty dla lepszej widoczności
            widokKarty.setUserData(karta);
            widokKarty.setCursor(Cursor.HAND);

            widokKarty.setOnMouseClicked(this::handleMulliganCardClicked);

            mulliganHandPane.getChildren().add(widokKarty);
        }

        ukryjWszystkiePaneleOverlayOprocz(panelMulligan);
        panelMulligan.setVisible(true);
        panelMulligan.toFront();
    }

    private void handleMulliganCardClicked(MouseEvent event) {
        ImageView kliknietyWidok = (ImageView) event.getSource();
        Karta kliknietaKarta = (Karta) kliknietyWidok.getUserData();

        if (kartyWybraneDoWymiany.contains(kliknietaKarta)) {
            // Odznacz kartę
            kartyWybraneDoWymiany.remove(kliknietaKarta);
            kliknietyWidok.setEffect(null); // Usuń efekt podświetlenia
        } else {
            // Zaznacz kartę, jeśli nie przekroczono limitu 2 kart
            if (kartyWybraneDoWymiany.size() < 2) {
                kartyWybraneDoWymiany.add(kliknietaKarta);
                kliknietyWidok.setEffect(new DropShadow(20, Color.DEEPSKYBLUE)); // Efekt zaznaczenia
            } else {
                wyswietlKomunikatNaPlanszy("Możesz wymienić maksymalnie 2 karty.", false);
            }
        }
        mulliganCounterLabel.setText("Wybrano: " + kartyWybraneDoWymiany.size() + " / 2");
    }

    @FXML
    private void handleMulliganConfirmAction() {
        if (silnikGry != null) {
            Gracz aktualnyGracz = silnikGry.getGraczDlaMulligan(); // Potrzebujemy nowego gettera w SilnikGry
            if (aktualnyGracz != null) {
                // Przekaż wybrane karty do silnika i ukryj panel
                panelMulligan.setVisible(false);
                silnikGry.wykonajMulligan(aktualnyGracz, new ArrayList<>(kartyWybraneDoWymiany));
            } else {
                System.err.println("BŁĄD: Nie można potwierdzić wymiany, silnik nie wie, który gracz jest aktywny.");
            }
        }
    }

}