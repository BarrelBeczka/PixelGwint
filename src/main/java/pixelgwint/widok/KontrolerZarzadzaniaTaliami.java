package pixelgwint.widok;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell; // WAŻNY IMPORT
import javafx.scene.control.ListView;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pixelgwint.PixelGwintAplikacja;
import pixelgwint.logika.MenedzerTalii;
import pixelgwint.model.TaliaUzytkownika;
import pixelgwint.model.Uzytkownik;

import java.net.URL;
import java.util.List;

public class KontrolerZarzadzaniaTaliami {

    @FXML
    private VBox rootZarzadzanieTaliamiPane;

    @FXML private ListView<TaliaUzytkownika> listaZapisanychTalii;
    @FXML private Label etykietaInfo;
    @FXML private Button przyciskStworzNowaTalie;
    @FXML private Button przyciskPowrotDoMenu;

    private PixelGwintAplikacja pixelGwintAplikacja;
    private Uzytkownik aktualnyUzytkownik;
    private MenedzerTalii menedzerTalii;

    private final DropShadow goldGlowEffect = new DropShadow(20, Color.rgb(255, 215, 0, 0.7));

    public void setApp(PixelGwintAplikacja app) {
        this.pixelGwintAplikacja = app;
    }

    @FXML
    public void initialize() {
        ustawTloProgramistycznie();

        Button[] przyciski = {przyciskStworzNowaTalie, przyciskPowrotDoMenu};
        for (Button przycisk : przyciski) {
            if (przycisk != null) {
                przycisk.setOnMouseEntered(event -> przycisk.setEffect(goldGlowEffect));
                przycisk.setOnMouseExited(event -> przycisk.setEffect(null));
            }
        }

        if (listaZapisanychTalii != null) {
            listaZapisanychTalii.setCellFactory(lv -> new ListCell<TaliaUzytkownika>() {
                @Override
                protected void updateItem(TaliaUzytkownika talia, boolean empty) {
                    super.updateItem(talia, empty);
                    if (empty || talia == null) {
                        setText(null);
                        setGraphic(null);
                        setStyle("-fx-background-color: transparent;");
                    } else {
                        setText(talia.toString());
                        // Początkowy styl dla komórki z danymi
                        if (isSelected()) {
                            setStyle("-fx-background-color: #B8860B; -fx-text-fill: black;");
                        } else if (isHover()) {
                            setStyle("-fx-background-color: rgba(255, 215, 0, 0.15); -fx-text-fill: #FFEEAA;");
                        } else {
                            setStyle("-fx-background-color: transparent; -fx-text-fill: #FFD700;");
                        }

                        // Listener dla stanu hover
                        hoverProperty().addListener((obs, oldVal, isHovering) -> {
                            if (!isEmpty() && !isSelected()) {
                                if (isHovering) {
                                    setStyle("-fx-background-color: rgba(255, 215, 0, 0.15); -fx-text-fill: #FFEEAA;");
                                } else {
                                    setStyle("-fx-background-color: transparent; -fx-text-fill: #FFD700;");
                                }
                            }
                        });

                        // Listener dla stanu selected
                        selectedProperty().addListener((obs, oldVal, isSelected) -> {
                            if (!isEmpty()) {
                                if (isSelected) {
                                    setStyle("-fx-background-color: #B8860B; -fx-text-fill: black;");
                                } else if (isHover()) {
                                    setStyle("-fx-background-color: rgba(255, 215, 0, 0.15); -fx-text-fill: #FFEEAA;");
                                } else {
                                    setStyle("-fx-background-color: transparent; -fx-text-fill: #FFD700;");
                                }
                            }
                        });
                    }
                }
            });
        }
    }

    private void ustawTloProgramistycznie() {
        final String backgroundImageClasspathPath = "/grafiki/tla_gry/gwentbackground.png";
        if (rootZarzadzanieTaliamiPane == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerZarzTalii): rootZarzadzanieTaliamiPane nie zostało wstrzyknięte!");
            return;
        }
        URL imageUrl = getClass().getResource(backgroundImageClasspathPath);
        if (imageUrl == null) {
            System.err.println("BŁĄD KRYTYCZNY (KontrolerZarzTalii): Nie można znaleźć obrazka tła: " + backgroundImageClasspathPath);
        } else {
            String cssExternalUrl = imageUrl.toExternalForm();
            String aktualnyStyl = rootZarzadzanieTaliamiPane.getStyle();
            if (aktualnyStyl == null || !aktualnyStyl.contains(cssExternalUrl.replace("'", ""))) {
                String newStyle = "-fx-background-image: url('" + cssExternalUrl + "'); " +
                        "-fx-background-size: cover; " +
                        "-fx-background-position: center center; " +
                        "-fx-background-repeat: no-repeat;";
                rootZarzadzanieTaliamiPane.setStyle(newStyle);
                System.out.println("INFO (KontrolerZarzTalii): Tło obrazkowe zostało ustawione programistycznie.");
            }
        }
    }

    public void inicjalizujDane(Uzytkownik uzytkownik) {
        this.aktualnyUzytkownik = uzytkownik;
        this.menedzerTalii = new MenedzerTalii();

        if (etykietaInfo != null && uzytkownik != null && uzytkownik.getNazwaUzytkownika() != null) {
            etykietaInfo.setText("Witaj, " + uzytkownik.getNazwaUzytkownika() + "! Tutaj zarządzasz swoimi taliami.");
        }
        wczytajIZaktualizujListeTalii();

        if (listaZapisanychTalii != null) {
            listaZapisanychTalii.setOnMouseClicked(event -> {
                TaliaUzytkownika wybrana = listaZapisanychTalii.getSelectionModel().getSelectedItem();
                if (wybrana != null) {
                    System.out.println("Wybrano talię: " + wybrana.getNazwaNadanaTalii());
                }
            });
        }
    }

    private void wczytajIZaktualizujListeTalii() {
        if (aktualnyUzytkownik == null || menedzerTalii == null || listaZapisanychTalii == null) {
            if (listaZapisanychTalii != null) {
                listaZapisanychTalii.setPlaceholder(new Label("Brak danych użytkownika do wczytania talii."));
                listaZapisanychTalii.getItems().clear();
            }
            return;
        }

        List<TaliaUzytkownika> talieObj = menedzerTalii.wczytajTalieUzytkownika(aktualnyUzytkownik.getNazwaUzytkownika());

        if (talieObj.isEmpty()) {
            listaZapisanychTalii.setPlaceholder(new Label("Nie masz jeszcze żadnych zapisanych talii.\nStwórz nową, klikając przycisk poniżej."));
            listaZapisanychTalii.getItems().clear();
        } else {
            listaZapisanychTalii.getItems().setAll(talieObj);
        }
    }

    @FXML
    private void handleStworzNowaTalieAction() {
        // POPRAWKA TUTAJ:
        System.out.println("Akcja: Stwórz Nową Talię dla " + (aktualnyUzytkownik != null ? aktualnyUzytkownik.getNazwaUzytkownika() : "B/D"));
        if (pixelGwintAplikacja != null && aktualnyUzytkownik != null) {
            pixelGwintAplikacja.pokazEkranWyboruFrakcjiDlaTalii(aktualnyUzytkownik);
        }
    }

    @FXML
    private void handlePowrotDoMenuAction() {
        if (pixelGwintAplikacja != null && aktualnyUzytkownik != null) {
            pixelGwintAplikacja.pokazMenuGlowne(aktualnyUzytkownik);
        }
    }
}