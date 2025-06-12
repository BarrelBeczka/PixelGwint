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
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.geometry.Point2D;
import javafx.animation.SequentialTransition;
public class KontrolerPlanszyGry {

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
    @FXML private VBox wynikiRundKontener;
    @FXML private Button przyciskRewanz;
    @FXML private Button przyciskMenuGlowneZKoncaGry;

    @FXML private StackPane panelCmentarza;
    @FXML private Label tytulPaneluCmentarza;
    @FXML private ScrollPane cmentarzScrollPaneListyKart;
    @FXML private FlowPane cmentarzListaKartFlowPane;
    @FXML private ImageView cmentarzPowiekszonaKartaImageView;
    @FXML private Label cmentarzOpisPowiekszonejKartyLabel;
    @FXML private Button przyciskZamknijCmentarz;
    @FXML private Button przyciskWskrzesKarteZCmentarza;

    @FXML private StackPane panelPodgladuKartCesarza;
    @FXML private Label tytulPaneluPodgladuCesarza;
    @FXML private HBox kontenerKartPodgladuCesarza;

    @FXML private StackPane panelWyboruKartyEredina;
    @FXML private Label tytulPaneluWyboruKartyEredina;
    @FXML private FlowPane flowPaneKartDoWyboruEredina;
    @FXML private ImageView powiekszonaKartaWyboruEredina;
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
    private Karta aktualnieWybranaDoPowiekszeniaEredin414 = null;
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
    private Image obrazekKoniecPrzegranaPng;
    private Image obrazekKoniecRemisPng;
    private Image coin1ImageRzut;
    private Image coin2ImageRzut;
    private final Random randomDoRzutu = new Random();
    private Timeline animacjaMonetyNaPlanszy;
    private Gracz wylosowanyGraczRozpoczynajacy = null;
    private Image obrazekPowiadPotwoPng;
    private PixelGwintAplikacja pixelGwintAplikacja;
    private Gracz obiektGracz1;
    private Gracz obiektGracz2;
    private SilnikGry silnikGry;
    private List<Karta> wszystkieKartyWGrzeCache;
    private Karta aktualnieZaznaczonaKartaDoZagrnia = null;
    private List<Node> podswietloneCele = new ArrayList<>();
    private static final double SZEROKOSC_KARTY_W_PANELU_CESARZA = 100.0;
    private static final double WYSOKOSC_KARTY_W_PANELU_CESARZA = 145.0;
    private final DropShadow efektObramowkiHover = new DropShadow(10, Color.rgb(255, 255, 100, 0.9));
    private final DropShadow decoyTargetGlowEffect = new DropShadow(25, Color.rgb(173, 216, 230, 0.95));

    private final String SCIEZKA_ZETON_CZERWONY = "/grafiki/elementy_ui/zetonczerw.png";
    private final String SCIEZKA_ZETON_CZARNY = "/grafiki/elementy_ui/zetonczarn.png";
    private final String SCIEZKA_INFO_TURA_G1 = "infoturag1.png";
    private final String SCIEZKA_INFO_TURA_G2 = "infoturag2.png";
    private final String SCIEZKA_REWERSU_DOWODCY = "/grafiki/karty/rewers_ogolny.png";

    private boolean trybWyboruCeluDlaManekina = false;

    private static final double STANDARDOWA_WYSOKOSC_KARTY = 116.0;
    private static final double STANDARDOWA_SZEROKOSC_KARTY = 80.0;

    public Image getObrazekWygranaRundyPng() { return obrazekWygranaRundyPng; }
    public Image getObrazekRemisRundyPng() { return obrazekRemisRundyPng; }

    //Metoda wywoływana automatycznie przez JavaFX po załadowaniu pliku FXML.
    //Inicjalizuje domyślne ustawienia kontrolek, ustawia wyrównanie dla kontenerów
    //kart oraz dodaje handlery zdarzeń do klikalnych regionów na planszy.
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
        obrazekKoniecPrzegranaPng = ladujObrazekZElementowUI("koniecprzeg.png");
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
            System.out.println("[KONTROLER INIT] Ustawiono handleCelKlikniety dla pogodaHBox.");
        } else {
            System.err.println("[KONTROLER INIT BŁĄD] pogodaHBox jest null! Nie można ustawić handlera kliknięcia.");
        }
        panelMulligan.setVisible(false);
    }

    //Handler dla kliknięcia na stos kart odrzuconych (cmentarz) gracza 1. Otwiera panel podglądu cmentarza.
    @FXML
    private void handleCmentarzG1Clicked(MouseEvent event) {
        if (!trybWskrzeszaniaAktywnyGlobalnie && obiektGracz1 != null && obiektGracz1.getProfilUzytkownika() != null) {
            pokazPanelCmentarza(obiektGracz1, "Cmentarz: " + obiektGracz1.getProfilUzytkownika().getNazwaUzytkownika(), TrybPaneluCmentarzaKontekst.PODGLAD);
        }
    }

    //Handler dla kliknięcia na stos kart odrzuconych (cmentarz) gracza 2. Otwiera panel podglądu cmentarza.
    @FXML
    private void handleCmentarzG2Clicked(MouseEvent event) {
        if (!trybWskrzeszaniaAktywnyGlobalnie && obiektGracz2 != null && obiektGracz2.getProfilUzytkownika() != null) {
            pokazPanelCmentarza(obiektGracz2, "Cmentarz: " + obiektGracz2.getProfilUzytkownika().getNazwaUzytkownika(), TrybPaneluCmentarzaKontekst.PODGLAD);
        }
    }

    //Wyświetla tymczasowy panel z listą kart (np. przy użyciu zdolności Cesarza Nilfgaardu).
    //Panel automatycznie znika po określonym czasie.
    public void pokazTymczasowyPanelZKartami(List<Karta> kartyDoPokazania, Duration czasPokazu, String tytul) {
        if (kartyDoPokazania == null || kartyDoPokazania.isEmpty() || panelPodgladuKartCesarza == null || kontenerKartPodgladuCesarza == null || tytulPaneluPodgladuCesarza == null) {
            System.err.println("[KONTROLER] Nie można pokazać panelu podglądu kart Cesarza - brak kart lub elementów UI.");
            return;
        }

        tytulPaneluPodgladuCesarza.setText(tytul);
        kontenerKartPodgladuCesarza.getChildren().clear();

        System.out.println("[KONTROLER] Pokazuję na panelu Cesarza karty: " + kartyDoPokazania.stream().map(Karta::getNazwa).collect(Collectors.joining(", ")));

        for (Karta karta : kartyDoPokazania) {
            ImageView widokKarty = stworzWidokKarty(karta, SZEROKOSC_KARTY_W_PANELU_CESARZA, WYSOKOSC_KARTY_W_PANELU_CESARZA);
            final Karta finalKarta = karta;
            widokKarty.setOnMouseEntered(event -> {
            });
            widokKarty.setOnMouseExited(event -> {
            });
            kontenerKartPodgladuCesarza.getChildren().add(widokKarty);
        }

        ukryjWszystkiePaneleOverlayOprocz(panelPodgladuKartCesarza);
        panelPodgladuKartCesarza.setVisible(true);
        panelPodgladuKartCesarza.toFront();

        PauseTransition pauza = new PauseTransition(czasPokazu);
        pauza.setOnFinished(event -> {
            panelPodgladuKartCesarza.setVisible(false);
            kontenerKartPodgladuCesarza.getChildren().clear();
            System.out.println("[KONTROLER] Panel podglądu kart Cesarza zniknął.");
        });
        pauza.play();
    }

    //Uruchamia tryb wyboru rzędu dla zagrywanej karty, która ma kilka możliwych miejsc do umieszczenia (karty ze zdolnością "Zręczność").
    public void poprosOWyborRzeduDlaKarty(Karta karta, Gracz aktywnyGracz, boolean forResurrectionPlacement) {
        if (karta == null || aktywnyGracz == null) {
            System.err.println("[KONTROLER] poprosOWyborRzeduDlaKarty: karta lub aktywnyGracz jest null.");
            return;
        }
        this.aktualnieZaznaczonaKartaDoZagrnia = karta;
        this.aktualnieWybieranyRzadPoWskrzeszeniu = forResurrectionPlacement;

        System.out.println("[KONTROLER] Prośba o wybór rzędu dla karty: " + karta.getNazwa() +
                (forResurrectionPlacement ? " (PO WSKRZESZENIU)" : " (Z RĘKI)"));

        usunPodswietlenieRzedowISlotow();
        PlanszaGracza planszaDocelowaModel;
        HBox[] rzedyPlanszyDocelowejUI;

        boolean czySzpiegZreki = !forResurrectionPlacement && karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");

        if (czySzpiegZreki) {
            rzedyPlanszyDocelowejUI = (aktywnyGracz == obiektGracz1) ?
                    new HBox[]{rzadPiechotyG2HBox, rzadStrzeleckiG2HBox, rzadOblezeniaG2HBox} :
                    new HBox[]{rzadPiechotyG1HBox, rzadStrzeleckiG1HBox, rzadOblezeniaG1HBox};
        } else {
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
                silnikGry.anulujWskrzeszanie(aktywnyGracz);
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
    //Wyświetla panel cmentarza dla określonego gracza. Panel może działać w różnych trybach, np. tylko do podglądu lub do wyboru karty w celu jej wskrzeszenia.
    public void pokazPanelCmentarza(Gracz graczWlascicielCmentarza, String tytul, TrybPaneluCmentarzaKontekst tryb) {
        if (graczWlascicielCmentarza == null || panelCmentarza == null || tytulPaneluCmentarza == null ||
                cmentarzListaKartFlowPane == null || cmentarzPowiekszonaKartaImageView == null ||
                przyciskZamknijCmentarz == null || przyciskWskrzesKarteZCmentarza == null) {
            System.err.println("Błąd: Nie można pokazać panelu cmentarza - brak gracza lub kluczowych elementów UI.");
            if (silnikGry != null) {
                Gracz graczDoAnulowaniaAkcji = null;
                switch (tryb) {
                    case WSKRZESZANIE_MEDYK:
                        graczDoAnulowaniaAkcji = graczWlascicielCmentarza;
                        if (graczDoAnulowaniaAkcji != null) silnikGry.anulujWskrzeszanie(graczDoAnulowaniaAkcji);
                        break;
                    case EMHYR_PAN_POLUDNIA:
                        if(silnikGry.getGraczAktywujacyEmhyra() != null) silnikGry.anulujWyborDlaEmhyra();
                        else silnikGry.anulujWyborDlaEmhyra();
                        break;
                    case EREDIN_ZABOJCA_AUBERONA_415:
                        graczDoAnulowaniaAkcji = graczWlascicielCmentarza;
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

    //Handler dla kliknięcia na miniaturę karty wewnątrz otwartego panelu cmentarza. Wyświetla powiększenie klikniętej karty i aktywuje przycisk akcji (np. "Wskrześ"), jeśli jest to dozwolone.
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


        if (przyciskWskrzesKarteZCmentarza != null) {
            boolean czyWaznyCel = false;
            if (kartaPodgladanaZCmentarza != null) {
                // TUTAJ JEST WAŻNA LOGIKA
                switch (aktualnyTrybPaneluCmentarza) {
                    case WSKRZESZANIE_MEDYK:
                        czyWaznyCel = (kartaPodgladanaZCmentarza.getTyp() == TypKartyEnum.JEDNOSTKA);
                        break;
                    case EMHYR_PAN_POLUDNIA:
                    case EREDIN_ZABOJCA_AUBERONA_415:
                        czyWaznyCel = true;
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

    //Handler dla głównego przycisku akcji w panelu cmentarza. W zależności od trybu,w jakim panel został otwarty, wykonuje odpowiednią akcję w SilnikuGry (np. wskrzeszenie, użycie zdolności Emhyra).
    @FXML
    private void handleWskrzesKarteZCmentarzaAction() {
        if (kartaPodgladanaZCmentarza == null || silnikGry == null) {
            System.err.println("Błąd: Brak wybranej karty lub silnika gry do wykonania akcji z cmentarza.");
            panelCmentarza.setVisible(false);
            resetStatePoOperacjiNaCmentarzu();
            return;
        }

        Gracz graczDocelowyDlaAkcji = null;

        switch (aktualnyTrybPaneluCmentarza) {
            case WSKRZESZANIE_MEDYK:
                graczDocelowyDlaAkcji = graczDokonujacyWskrzeszeniaGlobalnie;
                if (graczDocelowyDlaAkcji == null) { /* obsługa błędu */ return; }
                if (kartaPodgladanaZCmentarza.getTyp() != TypKartyEnum.JEDNOSTKA) {
                    wyswietlKomunikatNaPlanszy("Można wskrzeszać tylko jednostki (nie Bohaterów ani Specjalne).", true);
                    return;
                }
                panelCmentarza.setVisible(false);
                silnikGry.rozpocznijProcesWskrzeszaniaKarty(graczDocelowyDlaAkcji, kartaPodgladanaZCmentarza);
                break;

            case EMHYR_PAN_POLUDNIA:
                panelCmentarza.setVisible(false);
                silnikGry.wykonajWyborEmhyraPanaPoludnia(kartaPodgladanaZCmentarza);
                break;

            case EREDIN_ZABOJCA_AUBERONA_415:
                graczDocelowyDlaAkcji = silnikGry.getGraczAktywujacyEredina415();
                if (graczDocelowyDlaAkcji == null) { /* obsługa błędu */ return; }
                panelCmentarza.setVisible(false);
                silnikGry.wykonajWyborKartyZCmentarzaEredin415(kartaPodgladanaZCmentarza);
                break;

            case PODGLAD:
            default:
                System.out.println("Panel cmentarza w trybie podglądu, przycisk akcji nie powinien być aktywny.");
                panelCmentarza.setVisible(false);
                break;
        }
        resetStatePoOperacjiNaCmentarzu();
    }

    //Metoda pomocnicza, która czyści i resetuje stan kontrolera związany z panelem cmentarza po jego zamknięciu lub wykonaniu akcji.
    private void resetStatePoOperacjiNaCmentarzu() {
        this.trybWskrzeszaniaAktywnyGlobalnie = false;
        this.graczDokonujacyWskrzeszeniaGlobalnie = null;
        this.kartaPodgladanaZCmentarza = null;
        this.aktualnyTrybPaneluCmentarza = TrybPaneluCmentarzaKontekst.PODGLAD;

        if (przyciskWskrzesKarteZCmentarza != null) {
            przyciskWskrzesKarteZCmentarza.setVisible(false);
            przyciskWskrzesKarteZCmentarza.setManaged(false);
            przyciskWskrzesKarteZCmentarza.setDisable(true);
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

    //Handler dla przycisku "Zamknij" w panelu cmentarza. Anuluje ewentualną aktywną operację (np. wskrzeszanie) i chowa panel.
    @FXML
    private void handleZamknijCmentarzAction() {
        panelCmentarza.setVisible(false);
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
                    if (silnikGry.getGraczAktywujacyEredina415() != null) {
                        silnikGry.anulujZdolnoscEredina415(false);
                    }
                    break;
                default:
                    break;
            }
        }
        resetStatePoOperacjiNaCmentarzu();
    }

    //Uruchamia tryb wyboru celu dla karty "Manekin do ćwiczeń". Podświetla na planszy wszystkie dozwolone cele (karty jednostek, które nie są bohaterami).
    public void rozpocznijWyborCeluDlaManekina(Gracz graczCelujacy, List<Karta> poprawneCele) {
        if (silnikGry == null || silnikGry.isCzyGraZakonczona() || graczCelujacy == null || poprawneCele == null || poprawneCele.isEmpty()) {
            wyswietlKomunikatNaPlanszy("Brak celów dla Manekina lub błąd stanu.", true);
            if (silnikGry != null && graczCelujacy != null) {
                silnikGry.anulujZagranieManekina(graczCelujacy, true);
            } else if (silnikGry != null && silnikGry.getGraczUzywajacyManekina() != null) { // Fallback
                silnikGry.anulujZagranieManekina(silnikGry.getGraczUzywajacyManekina(), true);
            }
            resetujStanZaznaczonejKarty();
            return;
        }

        this.trybWyboruCeluDlaManekina = true;
        usunPodswietlenieRzedowISlotow();
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
                            obrazekKarty.setEffect(decoyTargetGlowEffect);

                            obrazekKarty.setCursor(Cursor.HAND);
                            obrazekKarty.setOnMouseClicked(this::handleKartaNaPlanszyKliknietaDlaManekina);
                            znalezionoCoPodswietlic = true;
                        } else {
                            obrazekKarty.setEffect(null);
                            obrazekKarty.setCursor(Cursor.DEFAULT);
                            obrazekKarty.setOnMouseClicked(null);
                        }
                    }
                }
            }
        }
        if (!znalezionoCoPodswietlic) {
            wyswietlKomunikatNaPlanszy("Brak jednostek do zamiany z Manekinem!", true);
            if (silnikGry != null && graczCelujacy != null) {
                silnikGry.anulujZagranieManekina(graczCelujacy, true);
            }
        }
    }

    //Metoda pomocnicza, która usuwa wszystkie efekty wizualne (np. podświetlenie)
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

    //Handler dla kliknięcia na kartę-jednostkę na planszy w trybie wyboru celu dla Manekina. Przekazuje wybraną jednostkę do SilnikaGry w celu dokonania zamiany.
    private void handleKartaNaPlanszyKliknietaDlaManekina(MouseEvent event) {
        if (!trybWyboruCeluDlaManekina || silnikGry == null) return;

        Object source = event.getSource();
        if (source instanceof ImageView) {
            ImageView kliknietyObrazek = (ImageView) source;
            Karta wybranaJednostka = (Karta) kliknietyObrazek.getUserData();
            Node rodzicObrazka = kliknietyObrazek.getParent();

            if (wybranaJednostka != null && wybranaJednostka.getTyp() != TypKartyEnum.BOHATER && rodzicObrazka instanceof HBox) {
                HBox rzadHBox = (HBox) rodzicObrazka;
                int indeksWRzedzie = rzadHBox.getChildren().indexOf(kliknietyObrazek);

                RzadPlanszy rzadPochodzenia = null;
                Gracz wlascicielRzedu = null;

                if (rzadHBox == rzadPiechotyG1HBox) { rzadPochodzenia = silnikGry.getGracz1().getPlanszaGry().getRzadPiechoty(); wlascicielRzedu = silnikGry.getGracz1(); }
                else if (rzadHBox == rzadStrzeleckiG1HBox) { rzadPochodzenia = silnikGry.getGracz1().getPlanszaGry().getRzadStrzelecki(); wlascicielRzedu = silnikGry.getGracz1(); }
                else if (rzadHBox == rzadOblezeniaG1HBox) { rzadPochodzenia = silnikGry.getGracz1().getPlanszaGry().getRzadOblezenia(); wlascicielRzedu = silnikGry.getGracz1(); }
                else if (rzadHBox == rzadPiechotyG2HBox) { rzadPochodzenia = silnikGry.getGracz2().getPlanszaGry().getRzadPiechoty(); wlascicielRzedu = silnikGry.getGracz2(); }
                else if (rzadHBox == rzadStrzeleckiG2HBox) { rzadPochodzenia = silnikGry.getGracz2().getPlanszaGry().getRzadStrzelecki(); wlascicielRzedu = silnikGry.getGracz2(); }
                else if (rzadHBox == rzadOblezeniaG2HBox) { rzadPochodzenia = silnikGry.getGracz2().getPlanszaGry().getRzadOblezenia(); wlascicielRzedu = silnikGry.getGracz2(); }

                if (rzadPochodzenia != null && indeksWRzedzie != -1 && silnikGry.getGraczAktualnejTury() == wlascicielRzedu) {
                    System.out.println("[KONTROLER] Wybrano jednostkę '" + wybranaJednostka.getNazwa() + "' dla Manekina w rzędzie " + rzadPochodzenia.getTypRzedu() + " na indeksie " + indeksWRzedzie);

                    silnikGry.wykonajZamianeManekinem(silnikGry.getGraczAktualnejTury(), wybranaJednostka, rzadPochodzenia, indeksWRzedzie);
                } else {
                    System.err.println("[KONTROLER] Błąd przy wyborze celu dla Manekina - nie znaleziono rzędu lub indeksu, lub próba wyboru karty przeciwnika.");
                }
            } else {
                wyswietlKomunikatNaPlanszy("Manekinem można zamienić tylko zwykłą jednostkę (nie Bohatera).", true);
            }
            resetujTrybWyboruCeluManekina();
        }
    }

    //Metoda pomocnicza, która wyłącza tryb wyboru celu dla Manekina i czyści podświetlenie z kart na planszy.
    private void resetujTrybWyboruCeluManekina() {
        this.trybWyboruCeluDlaManekina = false;
        usunPodswietlenieRzedowISlotow();

        Gracz graczUzywajacyManekina = null;
        if (silnikGry != null) {

            Gracz graczKtoregoKartyCzyscic = silnikGry.getGraczUzywajacyManekina(); // Jeśli masz taki getter w SilnikGry
            if (graczKtoregoKartyCzyscic == null) { // Fallback, jeśli getter nie istnieje lub zwrócił null
                graczKtoregoKartyCzyscic = silnikGry.getGraczAktualnejTury(); // Może nie być poprawne, jeśli tura się zmieniła
                if (graczKtoregoKartyCzyscic == null && obiektGracz1 != null) graczKtoregoKartyCzyscic = obiektGracz1; // Ostateczny fallback
            }

            if (graczKtoregoKartyCzyscic != null) {
                wyczyscEfektyKartNaPlanszyGracza(graczKtoregoKartyCzyscic);
            } else {
                if(obiektGracz1 != null) wyczyscEfektyKartNaPlanszyGracza(obiektGracz1);
                if(obiektGracz2 != null) wyczyscEfektyKartNaPlanszyGracza(obiektGracz2);
            }
        }

        odswiezCalakolwiekPlansze();
        System.out.println("[KONTROLER] Zresetowano tryb wyboru celu dla Manekina.");
    }

    //Wyświetla na ekranie panel informacyjny o tym, że jeden z graczy spasował. Panel znika automatycznie po kilku sekundach.
    public void pokazPanelInfoPas(Gracz ktoSpasowal) {
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

        if (panelRzutuMonetaOverlay != null) panelRzutuMonetaOverlay.setVisible(false);
        if (panelZmianyTury != null) panelZmianyTury.setVisible(false);
        if (panelAktywacjiDowodcy != null) panelAktywacjiDowodcy.setVisible(false);

        panelInfoPas.setVisible(true);
        panelInfoPas.toFront();

        PauseTransition pauzaInfoPas = new PauseTransition(Duration.seconds(0));
        pauzaInfoPas.setOnFinished(event -> {
            if (panelInfoPas != null) {
                panelInfoPas.setVisible(false);
            }
            if (silnikGry != null) {
                silnikGry.kontynuujPoWyswietleniuInfoOPasie();
            }
        });
        pauzaInfoPas.play();
    }

    //Wyświetla panel podsumowujący całą grę, informując o zwycięzcy lub remisie. Pokazuje również historię wyników poszczególnych rund.
    public void pokazPanelKoncaGry(Gracz zwyciezcaGry, boolean czyRemisGry, List<String> historiaRund) {
        if (panelKoncaGry == null || grafikaKoncaGryImageView == null || napisKoncowyLabel == null || wynikiRundKontener == null) {
            System.err.println("Błąd krytyczny: Elementy panelu końca gry nie są zainicjalizowane!");
            return;
        }

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
            if (zwyciezcaGry == obiektGracz1) {
                obrazekDoWyswietlenia = obrazekKoniecWygranaPng;
            } else {
                obrazekDoWyswietlenia = obrazekKoniecPrzegranaPng;
                if (obrazekDoWyswietlenia == null) obrazekDoWyswietlenia = obrazekKoniecWygranaPng;
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

        wynikiRundKontener.getChildren().clear();
        if (historiaRund != null) {
            for (String wynikRundy : historiaRund) {
                Label labelRundy = new Label(wynikRundy);
                labelRundy.setTextFill(Color.LIGHTSTEELBLUE);
                labelRundy.setStyle("-fx-font-size: 16px; -fx-font-style: italic;");
                wynikiRundKontener.getChildren().add(labelRundy);
            }
        } else {
            wynikiRundKontener.getChildren().add(new Label("Brak historii rund."));
        }

        panelKoncaGry.setVisible(true);
        panelKoncaGry.toFront();
    }

    //Wstrzykuje referencję do głównej klasy aplikacji, umożliwiając kontrolerowi komunikację z innymi częściami programu (np. zmianę ekranów, dostęp do menedżera muzyki).
    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    //Główna metoda konfigurująca nową grę. Otrzymuje obiekty graczy z poprzednich ekranów, inicjalizuje interfejs planszy danymi graczy i rozpoczyna sekwencję
    //wyboru gracza rozpoczynającego (zdolność Scoia'tael lub rzut monetą).
    public void inicjalizujGre(Gracz gracz1, Gracz gracz2, Gracz graczRozpoczynajacyOtrzymany) {
        if (pixelGwintAplikacja != null && pixelGwintAplikacja.getMenedzerMuzyki() != null) {
            System.out.println("[KontrolerPlanszyGry] Inicjalizacja gry, uruchamianie muzyki z gry.");
            pixelGwintAplikacja.getMenedzerMuzyki().uruchomPlaylistGry(true);
        }

        this.obiektGracz1 = gracz1;
        this.obiektGracz2 = gracz2;
        this.wylosowanyGraczRozpoczynajacy = null;

        System.out.println("[KONTROLER PLANSZY (inicjalizujGre)] Otrzymano gracz1: " +
                (gracz1 != null && gracz1.getProfilUzytkownika() != null ? gracz1.getProfilUzytkownika().getNazwaUzytkownika() : (gracz1 == null ? "gracz1 jest NULL" : "profil gracza1 jest NULL")) +
                " (Obiekt ID: " + System.identityHashCode(gracz1) + ")");
        System.out.println("[KONTROLER PLANSZY (inicjalizujGre)] Otrzymano gracz2: " +
                (gracz2 != null && gracz2.getProfilUzytkownika() != null ? gracz2.getProfilUzytkownika().getNazwaUzytkownika() : (gracz2 == null ? "gracz2 jest NULL" : "profil gracza2 jest NULL")) +
                " (Obiekt ID: " + System.identityHashCode(gracz2) + ")");
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

        nickGracza1Label.setText(this.obiektGracz1.getProfilUzytkownika().getNazwaUzytkownika());
        logoGracza1ImageView.setImage(ladujObrazekProfiluLubRewersuFrakcji(this.obiektGracz1.getProfilUzytkownika().getNazwaPlikuIkonyProfilu(), this.obiektGracz1.getWybranaFrakcja()));
        if (this.obiektGracz1.getKartaDowodcy() != null) {
            dowodcaGracza1ImageView.setImage(ladujObrazekKarty(this.obiektGracz1.getKartaDowodcy()));
        } else {
            dowodcaGracza1ImageView.setImage(null);
        }

        nickGracza2Label.setText(this.obiektGracz2.getProfilUzytkownika().getNazwaUzytkownika());
        logoGracza2ImageView.setImage(ladujObrazekProfiluLubRewersuFrakcji(this.obiektGracz2.getProfilUzytkownika().getNazwaPlikuIkonyProfilu(), this.obiektGracz2.getWybranaFrakcja()));
        if (this.obiektGracz2.getKartaDowodcy() != null) {
            dowodcaGracza2ImageView.setImage(ladujObrazekKarty(this.obiektGracz2.getKartaDowodcy()));
        } else {
            dowodcaGracza2ImageView.setImage(null);
        }

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

        if (this.wszystkieKartyWGrzeCache == null) {
            this.wszystkieKartyWGrzeCache = WczytywaczKart.wczytajWszystkieKarty();
            if (this.wszystkieKartyWGrzeCache.isEmpty()) {
                wyswietlKomunikatNaPlanszy("Błąd krytyczny: Nie wczytano definicji kart!", true);
                return;
            }
        }
        boolean g1JestScoiatael = (this.obiektGracz1.getWybranaFrakcja() == FrakcjaEnum.SCOIATAEL);
        boolean g2JestScoiatael = (this.obiektGracz2.getWybranaFrakcja() == FrakcjaEnum.SCOIATAEL);
        Gracz graczMajacyPrawoWyboru = null;

        if (g1JestScoiatael && g2JestScoiatael) {
            System.out.println("[KONTROLER PLANSZY] Obaj gracze to Scoia'tael. Nastąpi standardowy rzut monetą.");
        } else if (g1JestScoiatael) {
            graczMajacyPrawoWyboru = this.obiektGracz1;
            System.out.println("[KONTROLER PLANSZY] Gracz 1 (" + this.obiektGracz1.getProfilUzytkownika().getNazwaUzytkownika() + " - Scoia'tael) decyduje, kto rozpoczyna.");
        } else if (g2JestScoiatael) {
            graczMajacyPrawoWyboru = this.obiektGracz2;
            System.out.println("[KONTROLER PLANSZY] Gracz 2 (" + this.obiektGracz2.getProfilUzytkownika().getNazwaUzytkownika() + " - Scoia'tael) decyduje, kto rozpoczyna.");
        }

        if (graczMajacyPrawoWyboru != null) {
            pokazPanelWyboruRozpoczynajacegoScoiatael(graczMajacyPrawoWyboru);
        } else {
            System.out.println("[KONTROLER PLANSZY] Brak decydującego gracza Scoia'tael lub obaj są Scoia'tael. Przechodzenie do standardowego rzutu monetą.");
            rozpocznijSekwencjeRzutuMonetaNaPlanszy();
        }

    }

    //Wyświetla specjalny panel, gdy jeden z graczy gra frakcją Scoia'tael, dając mu prawo do zdecydowania, kto rozpocznie pierwszą rundę.
    private void pokazPanelWyboruRozpoczynajacegoScoiatael(Gracz graczDecydujacy) {
        if (panelWyboruRozpoczynajacegoScoiatael == null || tytulPaneluScoiatael == null ||
                przyciskScoiataelJaZaczynam == null || przyciskScoiataelPrzeciwnikZaczyna == null ||
                graczDecydujacy == null || graczDecydujacy.getProfilUzytkownika() == null) {
            System.err.println("Błąd UI: Nie można pokazać panelu wyboru Scoia'tael - brak elementów lub danych gracza.");
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
        } else {
            przyciskScoiataelPrzeciwnikZaczyna.setText("Przeciwnik Zaczyna");
        }

        ukryjWszystkiePaneleOverlayOprocz(panelWyboruRozpoczynajacegoScoiatael);
        panelWyboruRozpoczynajacegoScoiatael.setVisible(true);
        panelWyboruRozpoczynajacegoScoiatael.toFront();
    }

    //Handler dla przycisku "Ja Zaczynam" na panelu wyboru Scoia'tael. Ustawia gracza decydującego jako rozpoczynającego grę.
    @FXML
    private void handleScoiataelJaZaczynamAction() {
        if (graczDecydujacyScoiatael != null && panelWyboruRozpoczynajacegoScoiatael != null) {
            this.wylosowanyGraczRozpoczynajacy = graczDecydujacyScoiatael;
            System.out.println("[KONTROLER PLANSZY] Scoia'tael wybrał: " + this.wylosowanyGraczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika() + " zaczyna.");
            panelWyboruRozpoczynajacegoScoiatael.setVisible(false);
            graczDecydujacyScoiatael = null;
            kontynuujGrePoUstaleniuRozpoczynajacego();
        }
    }

    //Handler dla przycisku "Przeciwnik Zaczyna" na panelu wyboru Scoia'tael. Ustawia przeciwnika gracza decydującego jako rozpoczynającego grę.
    @FXML
    private void handleScoiataelPrzeciwnikZaczynaAction() {
        if (graczDecydujacyScoiatael != null && panelWyboruRozpoczynajacegoScoiatael != null) {
            Gracz przeciwnik = (graczDecydujacyScoiatael == obiektGracz1) ? obiektGracz2 : obiektGracz1;
            this.wylosowanyGraczRozpoczynajacy = przeciwnik;
            if (this.wylosowanyGraczRozpoczynajacy != null && this.wylosowanyGraczRozpoczynajacy.getProfilUzytkownika() != null) {
                System.out.println("[KONTROLER PLANSZY] Scoia'tael wybrał: " + this.wylosowanyGraczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika() + " zaczyna.");
            } else {
                System.err.println("[KONTROLER PLANSZY] Błąd krytyczny przy wyborze przeciwnika przez Scoia'tael.");
                this.wylosowanyGraczRozpoczynajacy = randomDoRzutu.nextBoolean() ? obiektGracz1 : obiektGracz2; // Awaryjny rzut
            }
            panelWyboruRozpoczynajacegoScoiatael.setVisible(false);
            graczDecydujacyScoiatael = null;
            kontynuujGrePoUstaleniuRozpoczynajacego();
        }
    }

    //Wyświetla panel rzutu monetą i uruchamia animację losującą gracza, który rozpocznie pierwszą rundę.
    private void rozpocznijSekwencjeRzutuMonetaNaPlanszy() {
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

        uruchomAnimacjeRzutuMoneta();
    }

    //Przeprowadza animację monety, a po jej zakończeniu losuje wynik i wyświetla go na ekranie.
    private void uruchomAnimacjeRzutuMoneta() {
        if (coin1ImageRzut == null || coin2ImageRzut == null || monetaDoRzutuImageView == null || infoRzutuLabel == null) {
            System.err.println("Błąd: Nie można uruchomić animacji rzutu monetą - brak zasobów.");
            this.wylosowanyGraczRozpoczynajacy = randomDoRzutu.nextBoolean() ? obiektGracz1 : obiektGracz2;
            panelRzutuMonetaOverlay.setVisible(false);
            kontynuujGrePoUstaleniuRozpoczynajacego();
            return;
        }

        infoRzutuLabel.setText("Losowanie...");

        final int liczbaZmian = 15;
        final double czasTrwaniaJednejZmiany = 200;

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

            PauseTransition pauzaPoRzucie = new PauseTransition(Duration.seconds(2));
            pauzaPoRzucie.setOnFinished(e -> {
                if (panelRzutuMonetaOverlay != null) panelRzutuMonetaOverlay.setVisible(false);
                kontynuujGrePoUstaleniuRozpoczynajacego();
            });
            pauzaPoRzucie.play();
        });
        animacjaMonetyNaPlanszy.play();
    }

    //Metoda wywoływana po zakończeniu rzutu monetą lub wyboru przez Scoia'tael. Tworzy nową instancję SilnikaGry i rozpoczyna faktyczną rozgrywkę.
    private void kontynuujGrePoUstaleniuRozpoczynajacego() {
        if (this.wylosowanyGraczRozpoczynajacy == null) {
            System.err.println("Błąd krytyczny: Nie ustalono gracza rozpoczynającego przed utworzeniem silnika gry!");
            this.wylosowanyGraczRozpoczynajacy = (obiektGracz1 != null) ? obiektGracz1 : obiektGracz2;
            if (this.wylosowanyGraczRozpoczynajacy == null) {
                wyswietlKomunikatNaPlanszy("Błąd krytyczny inicjalizacji gry!", true); return;
            }
        }
        System.out.println("[KONTROLER PLANSZY] Tworzenie silnika gry. Faktycznie rozpoczyna: " + wylosowanyGraczRozpoczynajacy.getProfilUzytkownika().getNazwaUzytkownika());
        this.silnikGry = new SilnikGry(this.obiektGracz1, this.obiektGracz2, this.wylosowanyGraczRozpoczynajacy, this, this.wszystkieKartyWGrzeCache);
        this.silnikGry.rozpocznijGre();
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
            try (InputStream stream = getClass().getResourceAsStream("/grafiki/karty/rewers_ogolny.png")) {
                if (stream != null) return new Image(stream);
            } catch (Exception e) { /* ignoruj */ }
            return null;
        }
        Image rewersFrakcji = ladujObrazekRewersuFrakcji(frakcja);
        if (rewersFrakcji == null) {
            System.err.println("Nie udało się załadować rewersu frakcji dla " + frakcja.getNazwaWyswietlana() + " jako rewersu dowódcy.");
        }
        return rewersFrakcji;
    }

    //Metoda pomocnicza, która tworzy i konfiguruje obiekt ImageView dla podanej karty.
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

    //Metoda pomocnicza, która dynamicznie rysuje listę kart w podanym kontenerze HBox.
    private void wyswietlKartyWHBox(List<Karta> listaKart, HBox kontenerHBox,
                                    double szerokoscKarty, double wysokoscKarty,
                                    boolean czyPokazacRewersy) {
        if (kontenerHBox == null) return;
        kontenerHBox.getChildren().clear();
        if (listaKart == null || listaKart.isEmpty()) {
            kontenerHBox.setSpacing(0); return;
        }

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
                    });
                    widokKarty.setOnMouseExited(event -> {
                        ImageView target = (ImageView) event.getSource();
                        if (podgladKartyImageView != null) podgladKartyImageView.setImage(null);
                        target.setEffect(null);
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

    public void pokazPanelWynikuRundy(String komunikatDoWyswietlenia, Gracz zwyciezcaRundy, boolean czyBylRemisPunktowy) {
        if (panelWynikuRundy == null || grafikaWynikuRundyImageView == null || napisWynikuRundyLabel == null) {
            System.err.println("Błąd: Nie można pokazać panelu wyniku rundy - brak elementów UI.");
            if (silnikGry != null) {
                silnikGry.przejdzDoNastepnegoEtapuPoWynikuRundy();
            }
            return;
        }

        Image grafikaDoWyswietlenia = null;
        if (zwyciezcaRundy != null) {
            grafikaDoWyswietlenia = obrazekWygranaRundyPng;
        } else if (czyBylRemisPunktowy) {
            grafikaDoWyswietlenia = obrazekRemisRundyPng;
        }
        if (grafikaDoWyswietlenia != null) {
            grafikaWynikuRundyImageView.setImage(grafikaDoWyswietlenia);
            grafikaWynikuRundyImageView.setVisible(true);
        } else {
            grafikaWynikuRundyImageView.setImage(null);
            grafikaWynikuRundyImageView.setVisible(false);
        }
        napisWynikuRundyLabel.setText(komunikatDoWyswietlenia);

        ukryjWszystkiePaneleOverlayOprocz(panelWynikuRundy);

        panelWynikuRundy.setVisible(true);
        panelWynikuRundy.toFront();

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

    }

    //Handler dla przycisku "Dalej" na panelu wyniku rundy.
    @FXML
    private void handleDalejPoWynikuRundyAction() {
        if (panelWynikuRundy != null) {
            panelWynikuRundy.setVisible(false);
        }
        if (silnikGry != null) {
            silnikGry.przejdzDoNastepnegoEtapuPoWynikuRundy();
        }
    }

    //Handler dla przycisku "Rewanż" na panelu końca gry. Inicjuje nową partię
    @FXML
    private void handleRewanzAction() {
        System.out.println("[KONTROLER PLANSZY] Wybrano REWANŻ.");
        if (panelKoncaGry != null) panelKoncaGry.setVisible(false);

        if (obiektGracz1 != null && obiektGracz2 != null && pixelGwintAplikacja != null) {
            obiektGracz1.resetDoNowejGry();
            obiektGracz2.resetDoNowejGry();
            inicjalizujGre(obiektGracz1, obiektGracz2, null);
        } else {
            System.err.println("Nie można rozpocząć rewanżu - brak obiektów graczy lub aplikacji.");
            if (pixelGwintAplikacja != null && pixelGwintAplikacja.getProfilGracza1() != null) {
                pixelGwintAplikacja.pokazMenuGlowne(pixelGwintAplikacja.getProfilGracza1());
            } else if (pixelGwintAplikacja != null) {
                pixelGwintAplikacja.pokazEkranLogowania();
            }
        }
    }

    //Handler dla przycisku "Menu Główne" na panelu końca gry.
    @FXML
    private void handleMenuGlowneZKoncaGryAction() {
        System.out.println("[KONTROLER PLANSZY] Wybrano POWRÓT DO MENU GŁÓWNEGO.");
        if (pixelGwintAplikacja != null && pixelGwintAplikacja.getMenedzerMuzyki() != null) {
            // Przełącz na losowy utwór z playlisty menu
            pixelGwintAplikacja.getMenedzerMuzyki().uruchomPlaylistMenu(true);
        }

        if (panelKoncaGry != null) panelKoncaGry.setVisible(false);
        if (pixelGwintAplikacja != null) {
            Uzytkownik profilDoMenu = (obiektGracz1 != null) ? obiektGracz1.getProfilUzytkownika() : null;
            if (profilDoMenu == null && pixelGwintAplikacja.getProfilGracza1() != null) {
                profilDoMenu = pixelGwintAplikacja.getProfilGracza1(); // Fallback
            }

            if (profilDoMenu != null) {
                pixelGwintAplikacja.pokazMenuGlowne(profilDoMenu);
            } else {
                System.err.println("Nie można wrócić do menu - brak profilu użytkownika. Pokazywanie ekranu logowania.");
                pixelGwintAplikacja.pokazEkranLogowania();
            }
        }
    }

    //Metoda odpowiedzialna za pełną synchronizację interfejsu graficznego z aktualnym stanem gry przechowywanym w modelu (w SilnikuGry). Wywoływana po każdej akcji zmieniającej stan planszy.
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

        wyswietlKartyWHBox(stanRundy.getPlanszaGracza1().getRzadPiechoty().getKartyJednostekWRzedzie(),
                rzadPiechotyG1HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);
        wyswietlKartyWHBox(stanRundy.getPlanszaGracza1().getRzadStrzelecki().getKartyJednostekWRzedzie(),
                rzadStrzeleckiG1HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);
        wyswietlKartyWHBox(stanRundy.getPlanszaGracza1().getRzadOblezenia().getKartyJednostekWRzedzie(),
                rzadOblezeniaG1HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);

        wyswietlKartyWHBox(stanRundy.getPlanszaGracza2().getRzadPiechoty().getKartyJednostekWRzedzie(),
                rzadPiechotyG2HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);
        wyswietlKartyWHBox(stanRundy.getPlanszaGracza2().getRzadStrzelecki().getKartyJednostekWRzedzie(),
                rzadStrzeleckiG2HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);
        wyswietlKartyWHBox(stanRundy.getPlanszaGracza2().getRzadOblezenia().getKartyJednostekWRzedzie(),
                rzadOblezeniaG2HBox, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY, false);

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
                            ladujRewersDowodcy(obiektGracz1.getWybranaFrakcja()) :
                            ladujObrazekKarty(obiektGracz1.getKartaDowodcy())
            );
        }
        if (obiektGracz2 != null && dowodcaGracza2ImageView != null && obiektGracz2.getKartaDowodcy() != null) {
            dowodcaGracza2ImageView.setImage(
                    obiektGracz2.isZdolnoscDowodcyUzyta() ?
                            ladujRewersDowodcy(obiektGracz2.getWybranaFrakcja()) :
                            ladujObrazekKarty(obiektGracz2.getKartaDowodcy())
            );
        }

        if (silnikGry != null) {
            uaktywnijInterfejsDlaTury(silnikGry.getGraczAktualnejTury() == obiektGracz1);
        }
        System.out.println("Odświeżono widok całej planszy.");
    }

    //Metoda pomocnicza, która rysuje obrazek karty Rogu Dowódcy w odpowiednim slocie na planszy.
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

    //Handler dla kliknięcia na kartę w ręce. Zaznacza kartę i podświetla możliwe cele na planszy, do których można ją zagrać.
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

    //Metoda pomocnicza, która podświetla na planszy możliwe cele dla zaznaczonej karty.
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

        HBox[] rzedyPlanszyDocelowejJednostek = null;
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
                } else {
                    System.err.println("[KONTROLER] pogodaHBox jest null, nie można podświetlić dla karty pogody: " + karta.getNazwa());
                    resetujStanZaznaczonejKarty();
                }
            } else if (jestManekinem) {
                System.out.println("  > Karta Manekin (" + karta.getNazwa() + ") - inicjowanie przez silnik.");
                if (silnikGry != null) {
                    boolean interakcjaManekinaRozpoczeta = silnikGry.zagrajKarte(aktywnyGracz, karta, null);
                    if (!interakcjaManekinaRozpoczeta) {
                        System.out.println("[KONTROLER] Interakcja manekina nie mogła być rozpoczęta przez silnik (np. brak celów lub błąd). Resetowanie stanu.");
                        resetujStanZaznaczonejKarty();
                    }
                } else {
                    System.err.println("[KONTROLER] SilnikGry jest null, nie można zainicjować Manekina.");
                    resetujStanZaznaczonejKarty();
                }
            } else {
                System.out.println("  > Karta specjalna globalna (" + karta.getNazwa() + ") - zagrywana bez wyboru konkretnego slotu na planszy.");
                if (silnikGry != null) {
                    silnikGry.zagrajKarte(aktywnyGracz, karta, null);
                }
                resetujStanZaznaczonejKarty();
            }
        } else if (karta.getTyp() == TypKartyEnum.JEDNOSTKA || karta.getTyp() == TypKartyEnum.BOHATER) {
            List<TypRzeduEnum> mozliweTypyRzedow = new ArrayList<>();

            if (czyKartaMaZrecznosc) {
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

            if (!cokolwiekPodswietlono && !mozliweTypyRzedow.isEmpty()) {
                wyswietlKomunikatNaPlanszy("Brak dostępnych rzędów dla karty " + karta.getNazwa() + " na odpowiedniej planszy.", true);
                resetujStanZaznaczonejKarty();
            }
        }
    }

    //Metoda pomocnicza, która dodaje wizualny efekt podświetlenia
    private void podswietlElement(Node element) {
        if (element == null) return;
        String oryginalnyStyl = element.getStyle() != null ? element.getStyle() : "";
        oryginalnyStyl = oryginalnyStyl.replaceAll("-fx-background-color: rgba\\(255, 255, 0, 0.[34]\\);?", "").replaceAll("-fx-border-style: dashed;?", "").trim();
        if (oryginalnyStyl.endsWith(";") && !oryginalnyStyl.isEmpty()) oryginalnyStyl += " ";
        else if (!oryginalnyStyl.isEmpty() && !oryginalnyStyl.endsWith(";")) oryginalnyStyl += "; ";
        element.setStyle(oryginalnyStyl + "-fx-background-color: rgba(255, 255, 0, 0.3); -fx-border-style: dashed;");
        podswietloneCele.add(element);
    }

    //Metoda pomocnicza, która usuwa efekty podświetlenia
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
            element.setStyle(stylPodstawowy);
        }
        podswietloneCele.clear();
    }

    //Główny handler dla kliknięć na planszy. Jego logika jest kontekstowa - zależy od tego, co gracz zaznaczył wcześniej. Uruchamia odpowiednie animacje i akcje w SilnikuGry.
    @FXML
    private void handleCelKlikniety(MouseEvent event) {
        Gracz aktywnyGracz = (silnikGry != null) ? silnikGry.getGraczAktualnejTury() : null;

        if (silnikGry == null || aktywnyGracz == null || silnikGry.isOczekiwanieNaPotwierdzenieTury() || silnikGry.isCzyGraZakonczona()) {
            if (silnikGry != null && silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                wyswietlKomunikatNaPlanszy("Poczekaj na potwierdzenie tury przez gracza.", false);
            }
            if (aktualnieZaznaczonaKartaDoZagrnia != null || trybWyboruCeluDlaManekina || aktualnieWybieranyRzadPoWskrzeszeniu) {
                resetujStanZaznaczonejKarty();
            }
            return;
        }

        boolean czyMogeTerazGrac = (aktywnyGracz == obiektGracz1 && !obiektGracz1.isCzySpasowalWRundzie()) ||
                (aktywnyGracz == obiektGracz2 && pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat() && !obiektGracz2.isCzySpasowalWRundzie());

        if (!czyMogeTerazGrac && !(trybWyboruCeluDlaManekina || aktualnieWybieranyRzadPoWskrzeszeniu)) {
            resetujStanZaznaczonejKarty();
            return;
        }

        if (aktualnieZaznaczonaKartaDoZagrnia == null && !trybWyboruCeluDlaManekina && !aktualnieWybieranyRzadPoWskrzeszeniu) {
            usunPodswietlenieRzedowISlotow();
            return;
        }

        Object source = event.getSource();
        TypRzeduEnum wybranyRzadEnum = null;

        if (!podswietloneCele.contains((Node) source)) {
            System.out.println("[KONTROLER] Kliknięto na niepodświetlony element. Anulowanie aktywnego wyboru.");

            if (aktualnieWybieranyRzadPoWskrzeszeniu && silnikGry != null && aktywnyGracz != null) {
                System.out.println("  > Anulowanie trybu wskrzeszania.");
                Gracz graczWskrzeszajacy = silnikGry.getGraczKladacyKartePoWskrzeszeniu();
                if (graczWskrzeszajacy == null) graczWskrzeszajacy = aktywnyGracz;
                silnikGry.anulujWskrzeszanie(graczWskrzeszajacy);
            }
            else if (trybWyboruCeluDlaManekina && silnikGry != null && silnikGry.getGraczAktualnejTury() != null) {
                System.out.println("  > Anulowanie trybu wyboru celu dla Manekina.");
                silnikGry.anulujZagranieManekina(silnikGry.getGraczAktualnejTury(), false);
            }
            resetujStanZaznaczonejKarty();
            return;
        }

        if (source == pogodaHBox && aktualnieZaznaczonaKartaDoZagrnia != null &&
                aktualnieZaznaczonaKartaDoZagrnia.getTyp() == TypKartyEnum.SPECJALNA) {

            String nazwaKarty = aktualnieZaznaczonaKartaDoZagrnia.getNazwa().toLowerCase();
            boolean czyToKartaPogody = nazwaKarty.contains("mróz") || nazwaKarty.contains("mgła") || nazwaKarty.contains("deszcz");

            if (czyToKartaPogody) {
                HBox rekaGraczaHBox = (aktywnyGracz == obiektGracz1) ? rekaGracza1HBox : rekaGracza2HBox;
                ImageView kartaViewDoAnimacji = null;
                for (Node node : rekaGraczaHBox.getChildren()) {
                    if (node.getUserData() == aktualnieZaznaczonaKartaDoZagrnia) {
                        kartaViewDoAnimacji = (ImageView) node;
                        break;
                    }
                }

                if (kartaViewDoAnimacji != null) {
                    animujZagranieKartyPogody(aktualnieZaznaczonaKartaDoZagrnia, kartaViewDoAnimacji, aktywnyGracz);
                } else {
                    System.err.println("Błąd: Nie znaleziono ImageView dla karty pogody. Zagrywam bez animacji.");
                    silnikGry.zagrajKarte(aktywnyGracz, aktualnieZaznaczonaKartaDoZagrnia, null);
                }

                resetujStanZaznaczonejKarty();
                return;
            }
        }

        if (source instanceof HBox) {
            HBox kliknietyRzadHBox = (HBox) source;
            if (kliknietyRzadHBox == rzadPiechotyG1HBox || kliknietyRzadHBox == rzadPiechotyG2HBox) wybranyRzadEnum = TypRzeduEnum.PIECHOTA;
            else if (kliknietyRzadHBox == rzadStrzeleckiG1HBox || kliknietyRzadHBox == rzadStrzeleckiG2HBox) wybranyRzadEnum = TypRzeduEnum.STRZELECKIE;
            else if (kliknietyRzadHBox == rzadOblezeniaG1HBox || kliknietyRzadHBox == rzadOblezeniaG2HBox) wybranyRzadEnum = TypRzeduEnum.OBLEZENIE;
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
                silnikGry.polozWskrzeszonaKarteNaRzedzie(graczWskrzeszajacy, aktualnieZaznaczonaKartaDoZagrnia, wybranyRzadEnum);
            } else {
                HBox rekaGraczaHBox = (aktywnyGracz == obiektGracz1) ? rekaGracza1HBox : rekaGracza2HBox;
                ImageView kartaViewDoAnimacji = null;
                for (Node node : rekaGraczaHBox.getChildren()) {
                    if (node.getUserData() == aktualnieZaznaczonaKartaDoZagrnia) {
                        kartaViewDoAnimacji = (ImageView) node;
                        break;
                    }
                }

                HBox rzadDocelowyHBox = null;
                Object kliknieteZrodlo = event.getSource();

                if (kliknieteZrodlo instanceof HBox) {
                    rzadDocelowyHBox = (HBox) kliknieteZrodlo;
                } else if (kliknieteZrodlo instanceof StackPane) {
                    if (kliknieteZrodlo == rogPiechotyG1Slot) rzadDocelowyHBox = rzadPiechotyG1HBox;
                    else if (kliknieteZrodlo == rogStrzeleckiG1Slot) rzadDocelowyHBox = rzadStrzeleckiG1HBox;
                    else if (kliknieteZrodlo == rogOblezeniaG1Slot) rzadDocelowyHBox = rzadOblezeniaG1HBox;
                    else if (kliknieteZrodlo == rogPiechotyG2Slot) rzadDocelowyHBox = rzadPiechotyG2HBox;
                    else if (kliknieteZrodlo == rogStrzeleckiG2Slot) rzadDocelowyHBox = rzadStrzeleckiG2HBox;
                    else if (kliknieteZrodlo == rogOblezeniaG2Slot) rzadDocelowyHBox = rzadOblezeniaG2HBox;
                }

                if (kartaViewDoAnimacji != null && rzadDocelowyHBox != null) {
                    animujZagranieKarty(aktualnieZaznaczonaKartaDoZagrnia, kartaViewDoAnimacji, rzadDocelowyHBox, wybranyRzadEnum, aktywnyGracz);
                } else {
                    System.err.println("Błąd: Nie znaleziono ImageView lub HBoxu docelowego dla animacji. Zagrywam kartę natychmiast.");
                    silnikGry.zagrajKarte(aktywnyGracz, aktualnieZaznaczonaKartaDoZagrnia, wybranyRzadEnum);
                }
            }
        }
        else if (trybWyboruCeluDlaManekina) {
            System.out.println("[KONTROLER] Kliknięto na rząd w trybie Manekina, ale nie wybrano jednostki. Anulowanie.");
            if (silnikGry != null && silnikGry.getGraczAktualnejTury() != null) {
                silnikGry.anulujZagranieManekina(silnikGry.getGraczAktualnejTury(), false);
            }
        } else if (aktualnieZaznaczonaKartaDoZagrnia != null &&
                !(source == pogodaHBox &&
                        aktualnieZaznaczonaKartaDoZagrnia.getTyp() == TypKartyEnum.SPECJALNA &&
                        (aktualnieZaznaczonaKartaDoZagrnia.getNazwa().toLowerCase().contains("mróz") ||
                                aktualnieZaznaczonaKartaDoZagrnia.getNazwa().toLowerCase().contains("mgła") ||
                                aktualnieZaznaczonaKartaDoZagrnia.getNazwa().toLowerCase().contains("deszcz"))
                )) {
            System.out.println("[KONTROLER] Nie udało się ustalić prawidłowej akcji dla kliknięcia z zaznaczoną kartą: " + aktualnieZaznaczonaKartaDoZagrnia.getNazwa());
        }

        resetujStanZaznaczonejKarty();
    }

    //Wyświetla panel pozwalający graczowi wybrać jedną z kart pogody ze swojej talii w ramach użycia zdolności dowódcy Eredina.
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
            Label brakKartLabel = new Label("Brak kart pogody w talii.");
            brakKartLabel.setTextFill(Color.WHITE);
            flowPaneKartDoWyboruEredina.getChildren().add(brakKartLabel);
        } else {
            for (Karta karta : kartyPogody) {
                ImageView miniatura = stworzWidokKarty(karta, STANDARDOWA_SZEROKOSC_KARTY, STANDARDOWA_WYSOKOSC_KARTY);
                miniatura.setUserData(karta);
                miniatura.setCursor(Cursor.HAND);

                miniatura.setOnMouseClicked(event -> {
                    this.wybranaKartaPrzezEredinaDoPodgladu = karta;
                    powiekszonaKartaWyboruEredina.setImage(ladujObrazekKarty(karta));
                    przyciskPotwierdzWyborEredina.setDisable(false);
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
            if (!kartyPogody.isEmpty() && flowPaneKartDoWyboruEredina.getChildren().get(0) instanceof ImageView) {
                ImageView pierwszaMiniatura = (ImageView) flowPaneKartDoWyboruEredina.getChildren().get(0);
                MouseEvent simMouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, javafx.scene.input.MouseButton.PRIMARY, 1,
                        false, false, false, false, true, false, false, true, false, false, null);
                pierwszaMiniatura.fireEvent(simMouseEvent);
            }
        }

        ukryjWszystkiePaneleOverlayOprocz(panelWyboruKartyEredina);
        panelWyboruKartyEredina.setVisible(true);
        panelWyboruKartyEredina.toFront();
    }

    //Wyświetla panel zdolności dowódcy Eredina, na którym gracz musi wybrać dwie karty z ręki do odrzucenia.
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
                    miniatura.setEffect(null);
                } else {
                    if (aktualnieWybraneDoOdrzuceniaEredin414.size() < 2) {
                        aktualnieWybraneDoOdrzuceniaEredin414.add(kliknieta);
                        miniatura.setEffect(new DropShadow(15, Color.ORANGERED));
                    }
                }
                infoWyboruOdrzucanychEredin414.setText("Wybierz 2 karty do odrzucenia (" + aktualnieWybraneDoOdrzuceniaEredin414.size() + "/2)");
                przyciskPotwierdzOdrzucenieEredin414.setDisable(aktualnieWybraneDoOdrzuceniaEredin414.size() != 2);
                aktualnieWybranaDoPowiekszeniaEredin414 = kliknieta;
                powiekszonaKartaOdrzucanaEredin414.setImage(ladujObrazekKarty(kliknieta));
            });
            miniatura.setOnMouseEntered(e -> powiekszonaKartaOdrzucanaEredin414.setImage(ladujObrazekKarty((Karta)miniatura.getUserData())));
            miniatura.setOnMouseExited(e -> {
                if(aktualnieWybranaDoPowiekszeniaEredin414 != (Karta)miniatura.getUserData() || aktualnieWybraneDoOdrzuceniaEredin414.contains((Karta)miniatura.getUserData())) {

                } else {
                    powiekszonaKartaOdrzucanaEredin414.setImage(ladujObrazekKarty(aktualnieWybranaDoPowiekszeniaEredin414));
                }
            });
            flowPaneOdrzucanychEredin414.getChildren().add(miniatura);
        }

        ukryjWszystkiePaneleOverlayOprocz(panelWyboruOdrzucanychEredin414);
        panelWyboruOdrzucanychEredin414.setVisible(true);
        panelWyboruOdrzucanychEredin414.toFront();
    }

    //Handler dla przycisku potwierdzającego wybór kart do odrzucenia dla zdolności Eredina.
    @FXML
    private void handlePotwierdzOdrzucenieEredin414Action() {
        if (silnikGry != null && aktualnieWybraneDoOdrzuceniaEredin414.size() == 2) {
            panelWyboruOdrzucanychEredin414.setVisible(false);
            silnikGry.potwierdzWyborKartDoOdrzuceniaEredin414(new ArrayList<>(aktualnieWybraneDoOdrzuceniaEredin414));
        } else {
            infoWyboruOdrzucanychEredin414.setText("Błąd: Musisz wybrać dokładnie 2 karty.");
        }
    }

    //Handler dla przycisku anulującego użycie zdolności Eredina na etapie wyboru kart do odrzucenia.
    @FXML
    private void handleAnulujOdrzucenieEredin414Action() {
        panelWyboruOdrzucanychEredin414.setVisible(false);
        if (silnikGry != null) {
            silnikGry.anulujZdolnoscEredina414(false);
        }
    }

    //Wyświetla panel zdolności Eredina, na którym gracz wybiera jedną kartę do dobrania ze swojej talii (po odrzuceniu dwóch innych).
    public void pokazPanelWyboruKartyZTalliEredin414(List<Karta> taliaGracza) {
        if (panelWyboruDobieranejEredin414 == null) {
            System.err.println("KONTROLER: Błąd UI - Panel dobierania Eredina 414 niekompletny.");
            if (silnikGry != null) silnikGry.anulujZdolnoscEredina414(true);
            return;
        }
        wybranaKartaZTaliiDlaEredina414 = null;
        aktualnieWybranaDoPowiekszeniaEredin414 = null;
        powiekszonaKartaDobieranaEredin414.setImage(null);
        przyciskPotwierdzDobranieEredin414.setDisable(true);
        flowPaneDobieranychEredin414.getChildren().clear();

        if (taliaGracza.isEmpty()) {
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
                    for(Node node : flowPaneDobieranychEredin414.getChildren()){
                        if(node instanceof ImageView){
                            node.setEffect(node.getUserData() == kliknieta ? new DropShadow(15, Color.LIGHTGREEN) : null);
                        }
                    }
                });
                miniatura.setOnMouseEntered(e -> powiekszonaKartaDobieranaEredin414.setImage(ladujObrazekKarty((Karta)miniatura.getUserData())));
                miniatura.setOnMouseExited(e -> {
                    if(wybranaKartaZTaliiDlaEredina414 != (Karta)miniatura.getUserData()){
                        powiekszonaKartaDobieranaEredin414.setImage(ladujObrazekKarty(wybranaKartaZTaliiDlaEredina414));
                    }
                });
                flowPaneDobieranychEredin414.getChildren().add(miniatura);
            }
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

    //Handler dla przycisku potwierdzającego wybór karty do dobrania w ramach zdolności Eredina.
    @FXML
    private void handlePotwierdzDobranieEredin414Action() {
        if (silnikGry != null && wybranaKartaZTaliiDlaEredina414 != null) {
            panelWyboruDobieranejEredin414.setVisible(false);
            silnikGry.wykonajWyborKartyZTalliEredin414(wybranaKartaZTaliiDlaEredina414);
        } else {
        }
    }

    //Handler dla przycisku anulującego użycie zdolności Eredina na etapie wyboru karty do dobrania.
    @FXML
    private void handleAnulujDobranieEredin414Action() {
        panelWyboruDobieranejEredin414.setVisible(false);
        if (silnikGry != null) {
            silnikGry.anulujZdolnoscEredina414(false);
        }
    }

    //Handler dla przycisku potwierdzającego wybór karty pogody dla zdolności Eredina.
    @FXML
    private void handlePotwierdzWyborEredinaAction() {
        if (silnikGry != null && this.wybranaKartaPrzezEredinaDoPodgladu != null) {
            panelWyboruKartyEredina.setVisible(false);
            Karta wybrana = this.wybranaKartaPrzezEredinaDoPodgladu;
            this.wybranaKartaPrzezEredinaDoPodgladu = null;
            silnikGry.wykonajWyborPogodyPrzezEredina(wybrana);
        } else {
            System.err.println("KONTROLER: Nie można potwierdzić wyboru dla Eredina - brak wybranej karty lub silnika.");
            if(panelWyboruKartyEredina != null) panelWyboruKartyEredina.setVisible(false);
            this.wybranaKartaPrzezEredinaDoPodgladu = null;
            if(silnikGry != null) silnikGry.anulujWyborPogodyPrzezEredina(true);
        }
    }

    //Handler dla przycisku anulującego wybór karty pogody dla zdolności Eredina.
    @FXML
    private void handleAnulujWyborEredinaAction() {
        if (panelWyboruKartyEredina != null) {
            panelWyboruKartyEredina.setVisible(false);
        }
        this.wybranaKartaPrzezEredinaDoPodgladu = null;
        if (silnikGry != null) {
            silnikGry.anulujWyborPogodyPrzezEredina(false);
        }
    }

    //Wyświetla panel z powiększoną kartą dowódcy i przyciskami pozwalającymi na aktywację jego zdolności lub anulowanie.
    private void pokazPanelAktywacjiDowodcy(Gracz gracz) {
        if (panelAktywacjiDowodcy == null || powiekszonaKartaDowodcyImageView == null ||
                przyciskUzyjUmiejetnosciDowodcy == null ||
                gracz == null || gracz.getKartaDowodcy() == null || gracz.getProfilUzytkownika() == null) {

            System.err.println("Błąd: Nie można pokazać panelu aktywacji dowódcy - brak elementów UI lub danych gracza/karty.");
            wyswietlKomunikatNaPlanszy("Błąd panelu zdolności dowódcy.", true);
            return;
        }

        Karta kartaDowodcy = gracz.getKartaDowodcy();
        powiekszonaKartaDowodcyImageView.setImage(ladujObrazekKarty(kartaDowodcy));
        przyciskUzyjUmiejetnosciDowodcy.setDisable(gracz.isZdolnoscDowodcyUzyta());
        panelAktywacjiDowodcy.setUserData(gracz);
        ukryjWszystkiePaneleOverlayOprocz(panelAktywacjiDowodcy);
        panelAktywacjiDowodcy.setVisible(true);
        panelAktywacjiDowodcy.toFront();
        System.out.println("Panel aktywacji dowódcy pokazany dla: " + gracz.getProfilUzytkownika().getNazwaUzytkownika());
    }

    //Metoda pomocnicza, która ukrywa wszystkie (overlay) z wyjątkiem jednego, podanego jako argument. Zapewnia, że na ekranie jest widoczny zawsze tylko jeden panel specjalny.
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

    //Handler dla kliknięcia na obrazek dowódcy Gracza
    @FXML private void handleDowodcaG1Clicked(MouseEvent event) {
        if (silnikGry != null && silnikGry.getGraczAktualnejTury() == obiektGracz1 && !obiektGracz1.isZdolnoscDowodcyUzyta() && !silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
            pokazPanelAktywacjiDowodcy(obiektGracz1);
        } else {
            wyswietlKomunikatNaPlanszy("Nie możesz teraz użyć zdolności dowódcy.", false);
        }
    }

    //Handler dla kliknięcia na obrazek dowódcy Gracza
    @FXML private void handleDowodcaG2Clicked(MouseEvent event) {
        if (silnikGry != null && silnikGry.getGraczAktualnejTury() == obiektGracz2 && !obiektGracz2.isZdolnoscDowodcyUzyta() && (pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat()) && !silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
            pokazPanelAktywacjiDowodcy(obiektGracz2);
        } else {
            wyswietlKomunikatNaPlanszy("Nie tura przeciwnika lub zdolność już użyta.", false);
        }
    }

    //Handler dla przycisku "Aktywuj" w panelu zdolności dowódcy.
    @FXML
    private void handleUzyjUmiejetnosciDowodcyAction() {
        Gracz graczAktywujacy = (panelAktywacjiDowodcy != null) ? (Gracz) panelAktywacjiDowodcy.getUserData() : null;

        if (silnikGry != null && graczAktywujacy != null && !graczAktywujacy.isZdolnoscDowodcyUzyta()) {
            silnikGry.uzyjZdolnosciDowodcy(graczAktywujacy);
        }
        if (panelAktywacjiDowodcy != null) {
            panelAktywacjiDowodcy.setVisible(false);
        }
    }

    //Handler dla przycisku "Anuluj" w panelu aktywacji zdolności dowódcy.
    @FXML
    private void handleAnulujUmiejetnoscDowodcyAction() {
        if (panelAktywacjiDowodcy != null) {
            panelAktywacjiDowodcy.setVisible(false);
        }
    }

    //Wyświetla specjalny panel informujący o aktywacji pasywnej zdolności frakcji Potworów
    public void pokazPanelUmiejetnosciPotworow(String nazwaKartyWskrzeszonej) { // Możemy przekazać nazwę karty dla personalizacji
        if (panelUmiejetnosciPotworow == null || grafikaUmiejetnosciPotworowImageView == null || napisUmiejetnosciPotworowLabel == null) {
            System.err.println("Błąd: Nie można pokazać panelu umiejętności Potworów - brak elementów UI.");
            wyswietlKomunikatNaPlanszy("Umiejętność Potworów: " + nazwaKartyWskrzeszonej + " pozostaje!", false);
            return;
        }

        if (obrazekPowiadPotwoPng != null) {
            grafikaUmiejetnosciPotworowImageView.setImage(obrazekPowiadPotwoPng);
        } else {
            grafikaUmiejetnosciPotworowImageView.setImage(null);
        }
        napisUmiejetnosciPotworowLabel.setText("Pasywna umiejętność frakcji Potwory wskrzesza losową kartę");


        ukryjWszystkiePaneleOverlayOprocz(panelUmiejetnosciPotworow);
        panelUmiejetnosciPotworow.setVisible(true);
        panelUmiejetnosciPotworow.toFront();

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
            if (silnikGry != null && !silnikGry.isCzyGraZakonczona() && silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                silnikGry.potwierdzPrzejecieTury();
            }
            return;
        }

        przyciskPasuj.setDisable(true);
        if (przyciskPasujG2 != null) przyciskPasujG2.setDisable(true);
        if (dowodcaGracza1ImageView != null) dowodcaGracza1ImageView.setDisable(true);
        if (dowodcaGracza2ImageView != null) dowodcaGracza2ImageView.setDisable(true);

        System.out.println("[KONTROLER PLANSZY] Rozpoczęto sekwencję zmiany tury. Czekanie 3s.");

        PauseTransition pauza = new PauseTransition(Duration.seconds(3));
        pauza.setOnFinished(event -> {
            System.out.println("[KONTROLER PLANSZY] Pauza 3s zakończona. Próba pokazania panelu.");
            Platform.runLater(() -> {
                System.out.println("[KONTROLER PLANSZY] Wewnątrz Platform.runLater przed pokazaniem panelu.");
                if (silnikGry != null) {
                    odswiezCalakolwiekPlansze();
                    Gracz nastepnyGraczDoTury;
                    if (silnikGry.getGraczAktualnejTury() == obiektGracz1) {
                        nastepnyGraczDoTury = obiektGracz2;
                    } else {
                        nastepnyGraczDoTury = obiektGracz1;
                    }
                    System.out.println("[KONTROLER PLANSZY] Następny gracz do tury (dla panelu): " + (nastepnyGraczDoTury != null ? nastepnyGraczDoTury.getProfilUzytkownika().getNazwaUzytkownika() : "null"));
                    silnikGry.setGraczOczekujacyNaPotwierdzenie(nastepnyGraczDoTury);
                    pokazPanelPrzejeciaTury(nastepnyGraczDoTury);
                } else{
                    System.out.println("[KONTROLER PLANSZY] SilnikGry jest null po pauzie, nie można pokazać panelu.");
                }
            });
        });
        pauza.play();
    }

    //Inicjuje krótką pauzę, po której wyświetlony zostanie panel zmiany tury.
    public void pokazPanelPrzejeciaTury(Gracz graczKtoregoTuraNadchodzi) {
        System.out.println("[KONTROLER PLANSZY] Wejście do pokazPanelPrzejeciaTury dla gracza: " +
                (graczKtoregoTuraNadchodzi != null ? graczKtoregoTuraNadchodzi.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                " (Spasował? " + (graczKtoregoTuraNadchodzi != null ? graczKtoregoTuraNadchodzi.isCzySpasowalWRundzie() : "N/A") + ")");

        if (panelZmianyTury == null) {
            System.err.println("[KONTROLER PLANSZY] panelZmianyTury jest null. Wychodzenie.");
            if (silnikGry != null && !silnikGry.isCzyGraZakonczona() && silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                silnikGry.potwierdzPrzejecieTury();
            }
            return;
        }
        if (silnikGry == null) {
            System.err.println("[KONTROLER PLANSZY] silnikGry jest null. Wychodzenie.");
            panelZmianyTury.setVisible(false);
            return;
        }
        if (graczKtoregoTuraNadchodzi == null) {
            System.err.println("[KONTROLER PLANSZY] graczKtoregoTuraNadchodzi jest null. Wychodzenie.");
            if (silnikGry != null && !silnikGry.isCzyGraZakonczona() && silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                silnikGry.potwierdzPrzejecieTury();
            }
            return;
        }

        if (silnikGry.isCzyGraZakonczona()) {
            System.out.println("[KONTROLER PLANSZY] Gra zakończona, panel nie będzie pokazany.");
            panelZmianyTury.setVisible(false);
            return;
        }

        Gracz aktywnyPrzeciwnik = (graczKtoregoTuraNadchodzi == obiektGracz1) ? obiektGracz2 : obiektGracz1;
        if (aktywnyPrzeciwnik == null) {
            System.err.println("[KONTROLER PLANSZY] aktywnyPrzeciwnik jest null. To nie powinno się zdarzyć. Wychodzenie.");
            return;
        }
        System.out.println("[KONTROLER PLANSZY] Sprawdzanie warunków pasowania: Gracz panelu (" + graczKtoregoTuraNadchodzi.getProfilUzytkownika().getNazwaUzytkownika() +
                ") spasował: " + graczKtoregoTuraNadchodzi.isCzySpasowalWRundzie() +
                ". Przeciwnik (" + aktywnyPrzeciwnik.getProfilUzytkownika().getNazwaUzytkownika() +
                ") spasował: " + aktywnyPrzeciwnik.isCzySpasowalWRundzie());


        if (obiektGracz1.isCzySpasowalWRundzie() && obiektGracz2.isCzySpasowalWRundzie()) {
            System.out.println("[KONTROLER PLANSZY] Obaj gracze (obiektGracz1, obiektGracz2) spasowali. Panel niepotrzebny.");
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

    //Handler dla przycisku "Zacznij Turę" na panelu zmiany tury. Informuje SilnikGry, że nowy gracz jest gotowy do wykonania ruchu.
    @FXML
    private void handlePotwierdzTureAction() {
        if (panelZmianyTury == null || silnikGry == null) {
            System.err.println("Błąd w handlePotwierdzTureAction: panelZmianyTury lub silnikGry jest null.");
            return;
        }
        panelZmianyTury.setVisible(false);
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

    //Wyświetla krótki komunikat tekstowy w dedykowanym miejscu na planszy
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

    //Dezaktywuje wszystkie interaktywne elementy na planszy (przyciski, karty), koniec gry lub podczas animacji.
    public void zablokujInterakcjePlanszy() {
        przyciskPasuj.setDisable(true);
        if (przyciskPasujG2 != null) przyciskPasujG2.setDisable(true);
        if (rekaGracza1HBox != null) rekaGracza1HBox.getChildren().forEach(n -> n.setDisable(true));
        if (rekaGracza2HBox != null) rekaGracza2HBox.getChildren().forEach(n -> n.setDisable(true));
        if (dowodcaGracza1ImageView != null) dowodcaGracza1ImageView.setDisable(true);
        if (dowodcaGracza2ImageView != null) dowodcaGracza2ImageView.setDisable(true);
        System.out.println("Interakcje na planszy zablokowane z powodu końca gry.");
    }

    //Handler dla przycisku "Pasuj" dla Gracza 1. Informuje SilnikGry o decyzji spasowania przez gracza.
    @FXML
    private void handlePasujAction() {
        if (aktualnieZaznaczonaKartaDoZagrnia != null) {
            resetujStanZaznaczonejKarty();
        }
        if (silnikGry != null && !silnikGry.isCzyGraZakonczona() &&
                silnikGry.getGraczAktualnejTury() == obiektGracz1 &&
                !obiektGracz1.isCzySpasowalWRundzie() &&
                !silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
            silnikGry.pasuj(obiektGracz1);
        } else {
            wyswietlKomunikatNaPlanszy("Nie możesz teraz spasować.", false);
        }
    }
    @FXML
    private void handlePasujG2Action() {
        if (aktualnieZaznaczonaKartaDoZagrnia != null) {
            resetujStanZaznaczonejKarty();
        }
        if (silnikGry != null && !silnikGry.isCzyGraZakonczona() &&
                silnikGry.getGraczAktualnejTury() == obiektGracz2 &&
                (pixelGwintAplikacja != null && pixelGwintAplikacja.isTrybHotSeat()) &&
                !obiektGracz2.isCzySpasowalWRundzie() &&
                !silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
            silnikGry.pasuj(obiektGracz2);
        } else {
            wyswietlKomunikatNaPlanszy("Nie możesz teraz spasować.", false);
        }
    }

    //Handler dla przycisku "Opcje". Wyświetla panel z opcjami.
    @FXML
    private void handleOpcjeAction() {
        System.out.println("[KONTROLER PLANSZY] Przycisk 'Opcje' wciśnięty.");
        if (panelOpcjiGry != null) {
            if (silnikGry != null && silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
                wyswietlKomunikatNaPlanszy("Poczekaj na potwierdzenie tury przez gracza.", false);
                return;
            }

            ukryjWszystkiePaneleOverlayOprocz(panelOpcjiGry);
            panelOpcjiGry.setVisible(true);
            panelOpcjiGry.toFront();
        } else {
            System.err.println("Panel opcji gry nie został poprawnie załadowany!");
        }
    }

    //Handler dla przycisku "Kontynuuj" w panelu opcji. Zamyka panel i pozwala na wznowienie rozgrywki.
    @FXML
    private void handleOpcjeKontynuujAction() {
        if (panelOpcjiGry != null) {
            panelOpcjiGry.setVisible(false);
        }
        System.out.println("[KONTROLER PLANSZY] Wybrano 'Kontynuuj' z panelu opcji.");
    }

    //Handler dla przycisku "Restart Gry" w panelu opcji. Inicjalizuje całą rozgrywkę od nowa z tymi samymi taliami.
    @FXML
    private void handleOpcjeRestartGryAction() {
        System.out.println("[KONTROLER PLANSZY] Wybrano 'Restart Gry' z panelu opcji.");
        if (panelOpcjiGry != null) {
            panelOpcjiGry.setVisible(false);
        }

        if (obiektGracz1 != null && obiektGracz2 != null && pixelGwintAplikacja != null) {
            this.inicjalizujGre(this.obiektGracz1, this.obiektGracz2, null);
        } else {
            System.err.println("Nie można zrestartować gry - brak obiektów graczy lub aplikacji.");
            handleOpcjePowrotDoMenuAction();
        }
    }

    //Handler dla przycisku "Powrót do Menu" w panelu opcji. Przełącza widok z powrotem do menu głównego i wznawia muzykę menu.
    @FXML
    private void handleOpcjePowrotDoMenuAction() {
        System.out.println("[KONTROLER PLANSZY] Wybrano 'Powrót do Menu' z panelu opcji.");
        if (pixelGwintAplikacja != null && pixelGwintAplikacja.getMenedzerMuzyki() != null) {
            pixelGwintAplikacja.getMenedzerMuzyki().uruchomPlaylistMenu(true);
        }
        if (panelOpcjiGry != null) {
            panelOpcjiGry.setVisible(false);
        }
        if (pixelGwintAplikacja != null) {
            Uzytkownik profilDoMenu = (obiektGracz1 != null && obiektGracz1.getProfilUzytkownika() != null)
                    ? obiektGracz1.getProfilUzytkownika()
                    : pixelGwintAplikacja.getProfilGracza1();

            if (profilDoMenu != null) {
                pixelGwintAplikacja.pokazMenuGlowne(profilDoMenu);
            } else {
                System.err.println("Nie można wrócić do menu - brak profilu użytkownika. Pokazywanie ekranu logowania.");
                pixelGwintAplikacja.pokazEkranLogowania();
            }
        }
    }

    //Metoda pomocnicza, która czyści stan interakcji gracza. Odznacza zaznaczoną kartę, wyłącza tryby specjalne (np. wybór celu dla manekina) i usuwa podświetlenie z planszy.
    public void resetujStanZaznaczonejKarty() {
        this.aktualnieZaznaczonaKartaDoZagrnia = null;
        this.trybWyboruCeluDlaManekina = false;
        this.aktualnieWybieranyRzadPoWskrzeszeniu = false;
        usunPodswietlenieRzedowISlotow();
        System.out.println("[KONTROLER] Zresetowano stan zaznaczonej karty i podświetleń.");
    }


    //Sprawdza, czy Gracz 1 może użyć zdolności dowódcy (czy jest jego tura,  czy zdolność nie została już użyta) i jeśli tak, wyświetla panel aktywacji.
    private void probaAktywacjiZdolnosciDowodcyG1() {
        if (silnikGry != null && silnikGry.getGraczAktualnejTury() == obiektGracz1 && !obiektGracz1.isZdolnoscDowodcyUzyta() && !silnikGry.isOczekiwanieNaPotwierdzenieTury()) {
            pokazPanelAktywacjiDowodcy(obiektGracz1);
        } else {
            wyswietlKomunikatNaPlanszy("Nie możesz teraz użyć zdolności dowódcy.", false);
        }
    }

    //Handler dla kliknięcia na obrazek dowódcy Gracza 1. Wywołuje metodę próbującą aktywować zdolność.
    @FXML
    private void handleZdolnoscDowodcyG1Action() { // Możesz dodać (ActionEvent event) jeśli potrzebujesz
        probaAktywacjiZdolnosciDowodcyG1();
    }

    //Metoda, która wywołuje główną funkcję pokazania panelu cmentarza, ustawiając jej kontekst na potrzeby zdolności dowódcy Eredina (ID 415).
    public void pokazPanelCmentarzaDlaEredina415(Gracz gracz, String tytul) {
        pokazPanelCmentarza(gracz, tytul, TrybPaneluCmentarzaKontekst.EREDIN_ZABOJCA_AUBERONA_415);
    }

    public void pokazPanelCmentarzaDlaWyboru(Gracz graczKtoregoCmentarzPokazac, String tytul) {
        if (graczKtoregoCmentarzPokazac == null || panelCmentarza == null || tytulPaneluCmentarza == null ||
                cmentarzListaKartFlowPane == null || cmentarzPowiekszonaKartaImageView == null ||
                przyciskZamknijCmentarz == null || przyciskWskrzesKarteZCmentarza == null ) {
            System.err.println("Błąd: Nie można pokazać panelu cmentarza dla wyboru (Emhyr) - brak gracza lub kluczowych elementów UI.");
            if (silnikGry != null && silnikGry.getGraczAktualnejTury() != null) {
                silnikGry.anulujWyborDlaEmhyra();
            }
            resetStatePoOperacjiNaCmentarzu();
            return;
        }

        resetStatePoOperacjiNaCmentarzu();

        this.trybWyboruKartyDlaEmhyra = true;

        tytulPaneluCmentarza.setText(tytul);
        cmentarzListaKartFlowPane.getChildren().clear();
        cmentarzPowiekszonaKartaImageView.setImage(null);
        if (cmentarzOpisPowiekszonejKartyLabel != null) cmentarzOpisPowiekszonejKartyLabel.setText("Wybierz kartę z cmentarza przeciwnika...");

        List<Karta> kartyNaCmentarzu = new ArrayList<>(graczKtoregoCmentarzPokazac.getOdrzucone());
        Collections.reverse(kartyNaCmentarzu);

        if (kartyNaCmentarzu.isEmpty()) {
            if (cmentarzOpisPowiekszonejKartyLabel != null) cmentarzOpisPowiekszonejKartyLabel.setText("Cmentarz przeciwnika jest pusty.");
            przyciskWskrzesKarteZCmentarza.setText("Wybierz Kartę");
            przyciskWskrzesKarteZCmentarza.setDisable(true);
            przyciskWskrzesKarteZCmentarza.setVisible(true);
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
                handleCmentarzMiniaturaKartyKliknieta(kartyNaCmentarzu.get(0));
            }
            przyciskWskrzesKarteZCmentarza.setText("Wybierz dla Emhyra");
            przyciskWskrzesKarteZCmentarza.setVisible(true);
            przyciskWskrzesKarteZCmentarza.setManaged(true);
            przyciskWskrzesKarteZCmentarza.setDisable(kartaPodgladanaZCmentarza == null);
        }

        przyciskZamknijCmentarz.setVisible(true);
        przyciskZamknijCmentarz.setManaged(true);

        ukryjWszystkiePaneleOverlayOprocz(panelCmentarza);
        panelCmentarza.setVisible(true);
        panelCmentarza.toFront();
    }

    public static enum TrybPaneluCmentarzaKontekst {
        PODGLAD,
        WSKRZESZANIE_MEDYK,
        EMHYR_PAN_POLUDNIA,
        EREDIN_ZABOJCA_AUBERONA_415
    }

    private TrybPaneluCmentarzaKontekst aktualnyTrybPaneluCmentarza = TrybPaneluCmentarzaKontekst.PODGLAD;

    //Wyświetla panel wymiany kart (Mulligan) na początku gry, pozwalając graczowi na wymianę do dwóch kart z ręki startowej.
    public void pokazPanelMulligan(Gracz gracz) {
        if (gracz == null || panelMulligan == null) {
            System.err.println("BŁĄD: Nie można pokazać panelu Mulligan - gracz lub panel jest null.");
            if (silnikGry != null) silnikGry.pominMulligan(gracz);
            return;
        }

        kartyWybraneDoWymiany.clear();
        mulliganHandPane.getChildren().clear();
        mulliganInfoLabel.setText(gracz.getProfilUzytkownika().getNazwaUzytkownika() + ", wymień do 2 kart");
        mulliganCounterLabel.setText("Wybrano: 0 / 2");

        for (Karta karta : gracz.getReka()) {
            ImageView widokKarty = stworzWidokKarty(karta, 140, 203);
            widokKarty.setUserData(karta);
            widokKarty.setCursor(Cursor.HAND);

            widokKarty.setOnMouseClicked(this::handleMulliganCardClicked);

            mulliganHandPane.getChildren().add(widokKarty);
        }
        ukryjWszystkiePaneleOverlayOprocz(panelMulligan);
        panelMulligan.setVisible(true);
        panelMulligan.toFront();
    }

    //Handler dla przycisku potwierdzającego wymianę kart w fazie Mulligan. Przekazuje wybrane karty do SilnikaGry.
    private void handleMulliganCardClicked(MouseEvent event) {
        ImageView kliknietyWidok = (ImageView) event.getSource();
        Karta kliknietaKarta = (Karta) kliknietyWidok.getUserData();

        if (kartyWybraneDoWymiany.contains(kliknietaKarta)) {
            kartyWybraneDoWymiany.remove(kliknietaKarta);
            kliknietyWidok.setEffect(null);
        } else {
            if (kartyWybraneDoWymiany.size() < 2) {
                kartyWybraneDoWymiany.add(kliknietaKarta);
                kliknietyWidok.setEffect(new DropShadow(20, Color.DEEPSKYBLUE));
            } else {
                wyswietlKomunikatNaPlanszy("Możesz wymienić maksymalnie 2 karty.", false);
            }
        }
        mulliganCounterLabel.setText("Wybrano: " + kartyWybraneDoWymiany.size() + " / 2");
    }

    //Handler dla przycisku potwierdzającego wymianę kart w fazie Mulligan. Przekazuje wybrane karty do SilnikaGry.
    @FXML
    private void handleMulliganConfirmAction() {
        if (silnikGry != null) {
            Gracz aktualnyGracz = silnikGry.getGraczDlaMulligan();
            if (aktualnyGracz != null) {
                panelMulligan.setVisible(false);
                silnikGry.wykonajMulligan(aktualnyGracz, new ArrayList<>(kartyWybraneDoWymiany));
            } else {
                System.err.println("BŁĄD: Nie można potwierdzić wymiany, silnik nie wie, który gracz jest aktywny.");
            }
        }
    }

    //Animuje "przelot" karty jednostki z ręki gracza na wskazane miejsce na planszy. Uruchamiana przez handlery kliknięć, a po zakończeniu animacji wywołuje zwrotnie SilnikGry w celu aktualizacji modelu.
    private void animujZagranieKarty(Karta kartaDoZagrnia, ImageView kartaView, HBox rzadDocelowyHBox, TypRzeduEnum rzadDocelowyEnum, Gracz aktywnyGracz) {
        zablokujInterakcjePlanszy();

        Point2D startPointScene = kartaView.localToScene(kartaView.getBoundsInLocal().getWidth() / 2, kartaView.getBoundsInLocal().getHeight() / 2);
        Point2D startPointInPane = glownyKontenerPlanszy.sceneToLocal(startPointScene);

        HBox rekaHBox = (HBox) kartaView.getParent();
        if (rekaHBox != null) {
            rekaHBox.getChildren().remove(kartaView);
        }
        kartaView.setManaged(false);
        glownyKontenerPlanszy.getChildren().add(kartaView);
        kartaView.setLayoutX(startPointInPane.getX() - kartaView.getBoundsInLocal().getWidth() / 2);
        kartaView.setLayoutY(startPointInPane.getY() - kartaView.getBoundsInLocal().getHeight() / 2);

        int iloscKartJuzWRzedzie = rzadDocelowyHBox.getChildren().size();
        double szerokoscKartyWRzedzie = STANDARDOWA_SZEROKOSC_KARTY;
        double odstep = rzadDocelowyHBox.getSpacing();
        double przyszlaSzerokoscZawartosci = (iloscKartJuzWRzedzie + 1) * szerokoscKartyWRzedzie + (iloscKartJuzWRzedzie) * odstep;
        double startBlokuKartX = (rzadDocelowyHBox.getWidth() - przyszlaSzerokoscZawartosci) / 2.0;
        double doceloweXwewnatrzHBox = startBlokuKartX + iloscKartJuzWRzedzie * (szerokoscKartyWRzedzie + odstep) + (szerokoscKartyWRzedzie / 2.0);
        double doceloweYwewnatrzHBox = rzadDocelowyHBox.getHeight() / 2.0;
        Point2D endPointScene = rzadDocelowyHBox.localToScene(doceloweXwewnatrzHBox, doceloweYwewnatrzHBox);
        Point2D endPointInPane = glownyKontenerPlanszy.sceneToLocal(endPointScene);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.7), kartaView);
        tt.setToX(endPointInPane.getX() - startPointInPane.getX());
        tt.setToY(endPointInPane.getY() - startPointInPane.getY());
        tt.setOnFinished(event -> {
            glownyKontenerPlanszy.getChildren().remove(kartaView);
            silnikGry.zagrajKarte(aktywnyGracz, kartaDoZagrnia, rzadDocelowyEnum);
        });

        tt.play();
    }

    //Uruchamia sekwencyjną animację "przelotu" dobieranych kart z talii do ręki gracza. Wywoływana przez SilnikGry po zagraniu Szpiega
    public void rozpocznijAnimacjeDobierania(Gracz gracz, List<Karta> kartyDoAnimacji) {
        if (kartyDoAnimacji.isEmpty()) {
            silnikGry.finalizujTurePoDobieraniu(gracz, kartyDoAnimacji);
            return;
        }

        ImageView taliaView = (gracz == obiektGracz1) ? taliaGracza1ImageView : taliaGracza2ImageView;
        HBox rekaView = (gracz == obiektGracz1) ? rekaGracza1HBox : rekaGracza2HBox;

        Point2D startPoint = taliaView.localToScene(taliaView.getBoundsInLocal().getWidth() / 2, taliaView.getBoundsInLocal().getHeight() / 2);
        Point2D endPoint = rekaView.localToScene(rekaView.getWidth() / 2, rekaView.getHeight() / 2);

        Point2D startPointInPane = glownyKontenerPlanszy.sceneToLocal(startPoint);
        Point2D endPointInPane = glownyKontenerPlanszy.sceneToLocal(endPoint);

        SequentialTransition sekwencjaAnimacji = new SequentialTransition();

        for (Karta karta : kartyDoAnimacji) {
            Image awers = ladujObrazekKarty(karta);
            ImageView animowanaKartaView = new ImageView(awers);
            animowanaKartaView.setFitWidth(STANDARDOWA_SZEROKOSC_KARTY);
            animowanaKartaView.setFitHeight(STANDARDOWA_WYSOKOSC_KARTY);
            animowanaKartaView.setManaged(false);

            glownyKontenerPlanszy.getChildren().add(animowanaKartaView);
            animowanaKartaView.setLayoutX(startPointInPane.getX() - STANDARDOWA_SZEROKOSC_KARTY / 2);
            animowanaKartaView.setLayoutY(startPointInPane.getY() - STANDARDOWA_WYSOKOSC_KARTY / 2);

            TranslateTransition tt = new TranslateTransition(Duration.seconds(0.6), animowanaKartaView);
            tt.setToX(endPointInPane.getX() - startPointInPane.getX());
            tt.setToY(endPointInPane.getY() - startPointInPane.getY());
            tt.setOnFinished(e -> glownyKontenerPlanszy.getChildren().remove(animowanaKartaView));

            sekwencjaAnimacji.getChildren().add(tt);
            sekwencjaAnimacji.getChildren().add(new PauseTransition(Duration.millis(200)));
        }

        sekwencjaAnimacji.setOnFinished(event -> {
            silnikGry.finalizujTurePoDobieraniu(gracz, kartyDoAnimacji);
        });

        sekwencjaAnimacji.play();

    }

    //Animuje przelot karty pogodowej z ręki gracza do rzędu pogody.
    private void animujZagranieKartyPogody(Karta kartaPogody, ImageView kartaView, Gracz aktywnyGracz) {
        zablokujInterakcjePlanszy();

        Point2D startPointScene = kartaView.localToScene(kartaView.getBoundsInLocal().getWidth() / 2, kartaView.getBoundsInLocal().getHeight() / 2);
        Point2D startPointInPane = glownyKontenerPlanszy.sceneToLocal(startPointScene);

        HBox rekaHBox = (HBox) kartaView.getParent();
        if (rekaHBox != null) {
            rekaHBox.getChildren().remove(kartaView);
        }
        kartaView.setManaged(false);
        glownyKontenerPlanszy.getChildren().add(kartaView);
        kartaView.setLayoutX(startPointInPane.getX() - kartaView.getBoundsInLocal().getWidth() / 2);
        kartaView.setLayoutY(startPointInPane.getY() - kartaView.getBoundsInLocal().getHeight() / 2);

        int iloscKartJuzWRzedzie = pogodaHBox.getChildren().size();
        double szerokoscKarty = STANDARDOWA_SZEROKOSC_KARTY;
        double odstep = pogodaHBox.getSpacing();

        double doceloweXwewnatrzHBox = iloscKartJuzWRzedzie * (szerokoscKarty + odstep) + (szerokoscKarty / 2.0);
        double doceloweYwewnatrzHBox = pogodaHBox.getHeight() / 2.0;

        Point2D endPointScene = pogodaHBox.localToScene(doceloweXwewnatrzHBox, doceloweYwewnatrzHBox);
        Point2D endPointInPane = glownyKontenerPlanszy.sceneToLocal(endPointScene);

        TranslateTransition tt = new TranslateTransition(Duration.seconds(0.7), kartaView);
        tt.setToX(endPointInPane.getX() - startPointInPane.getX());
        tt.setToY(endPointInPane.getY() - startPointInPane.getY());
        tt.setInterpolator(Interpolator.EASE_OUT);

        tt.setOnFinished(event -> {
            glownyKontenerPlanszy.getChildren().remove(kartaView);
            silnikGry.zagrajKarte(aktywnyGracz, kartaPogody, null);
        });

        tt.play();
    }
}