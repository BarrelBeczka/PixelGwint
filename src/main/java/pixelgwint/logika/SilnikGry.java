package pixelgwint.logika;

import javafx.util.Duration;
import pixelgwint.model.*;
import pixelgwint.widok.KontrolerPlanszyGry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Collections; // Upewnij się, że jest ten import
import java.util.HashMap; // Upewnij się, że jest ten import
import java.util.Map; // Upewnij się, że jest ten import

import javafx.scene.image.Image;
// import java.util.Random; // Usunięty, jeśli nie jest używany

public class SilnikGry {

    private Gracz gracz1;
    private Gracz gracz2;
    private Gracz graczAktualnejTury;
    private final Gracz graczRozpoczynajacyPartie;
    private Gracz graczOczekujacyNaPotwierdzenie = null;
    private Karta kartaOczekujacaNaPolozeniePoWskrzeszeniu = null;
    private Gracz graczKladacyKartePoWskrzeszeniu = null; // Gracz, który zagrywał medyka
    private boolean oczekiwanieNaWyborRzeduPoWskrzeszeniu = false;
    private StanRundy aktualnyStanRundy;
    private int numerRundyGry;
    private final KontrolerPlanszyGry kontrolerPlanszy;
    private List<String> historiaWynikowRund;
    private boolean czyGraZakonczona;
    private Gracz graczOstatnioWygralRunde = null;
    private Gracz graczPoprzednioZaczynajacyRunde = null;
    private boolean oczekiwanieNaPotwierdzenieTury = false;
    private boolean oczekiwanieNaWyborCeluDlaManekina = false;
    private Karta kartaManekinaDoPolozenia = null;
    private Gracz graczUzywajacyManekina = null;
    private static final int ILOSC_KART_STARTOWYCH = 10;
    private boolean oczekiwanieNaWyborKartyDlaEmhyra = false;
    private Gracz graczAktywujacyEmhyra = null;
    private Gracz przeciwnikDlaEmhyra = null;
    private Karta kartaPotworaDoZachowaniaNaKolejnaRunde = null;
    private TypRzeduEnum rzadKartyPotworaDoZachowania = null;
    private Gracz graczPotworowZkartaDoZachowania = null;
    private boolean oczekiwanieNaWyborKartyPogodyPrzezEredina = false;
    private Gracz graczAktywujacyEredina336 = null;
    private List<Karta> dostepneKartyPogodyDlaEredina = null;
    private boolean oczekiwanieNaWyborKartDoOdrzuceniaEredin414 = false;
    private boolean oczekiwanieNaWyborKartyZTaliiPrzezEredin414 = false;
    private Gracz graczAktywujacyEredina414 = null;
    private List<Karta> kartyWybraneDoOdrzuceniaEredin414 = new ArrayList<>();
    private boolean oczekiwanieNaWyborKartyZCmentarzaPrzezEredina415 = false;
    private Gracz graczAktywujacyEredina415 = null;
    private boolean oczekiwanieNaMulliganGracza1 = false;
    private boolean oczekiwanieNaMulliganGracza2 = false;

    public SilnikGry(Gracz g1, Gracz g2, Gracz graczRozpoczynajacy, KontrolerPlanszyGry kontroler, List<Karta> wszystkieKarty) {
        this.gracz1 = g1;
        this.gracz2 = g2;
        this.graczRozpoczynajacyPartie = graczRozpoczynajacy;
        this.kontrolerPlanszy = kontroler;
        this.historiaWynikowRund = new ArrayList<>();


        if (g1 != null && g2 != null && g1.getPlanszaGry() != null && g2.getPlanszaGry() != null) { // Dodatkowe sprawdzenie plansz graczy
            this.aktualnyStanRundy = new StanRundy(g1.getPlanszaGry(), g2.getPlanszaGry());

            // --- POCZĄTEK DODANEGO FRAGMENTU ---
            // Ustawianie referencji do StanRundy dla każdego rzędu
            // Upewnij się, że plansze i rzędy nie są null
            PlanszaGracza planszaG1 = this.aktualnyStanRundy.getPlanszaGracza1();
            if (planszaG1 != null) {
                if (planszaG1.getRzadPiechoty() != null) {
                    planszaG1.getRzadPiechoty().setStanRundyRef(this.aktualnyStanRundy);
                }
                if (planszaG1.getRzadStrzelecki() != null) {
                    planszaG1.getRzadStrzelecki().setStanRundyRef(this.aktualnyStanRundy);
                }
                if (planszaG1.getRzadOblezenia() != null) {
                    planszaG1.getRzadOblezenia().setStanRundyRef(this.aktualnyStanRundy);
                }
            }

            PlanszaGracza planszaG2 = this.aktualnyStanRundy.getPlanszaGracza2();
            if (planszaG2 != null) {
                if (planszaG2.getRzadPiechoty() != null) {
                    planszaG2.getRzadPiechoty().setStanRundyRef(this.aktualnyStanRundy);
                }
                if (planszaG2.getRzadStrzelecki() != null) {
                    planszaG2.getRzadStrzelecki().setStanRundyRef(this.aktualnyStanRundy);
                }
                if (planszaG2.getRzadOblezenia() != null) {
                    planszaG2.getRzadOblezenia().setStanRundyRef(this.aktualnyStanRundy);
                }
            }
            // --- KONIEC DODANEGO FRAGMENTU ---

        } else {
            System.err.println("BŁĄD KRYTYCZNY: Obiekty graczy lub ich plansze są null przy tworzeniu SilnikaGry! Nie można ustawić referencji StanRundy dla rzędów.");
            // Jeśli aktualnyStanRundy nie mógł zostać poprawnie utworzony,
            // to nie ma sensu kontynuować z ustawianiem referencji.
            // Można tu rzucić wyjątek lub odpowiednio obsłużyć błąd.
            // Na razie, jeśli this.aktualnyStanRundy nie zostanie utworzony,
            // to kod ustawiający referencje nie wykona się.
            // Jeśli jednak aktualnyStanRundy jest tworzony z nullami (co jest złe),
            // to powyższy kod też będzie miał problemy.
            // Poprawiłem warunek `if` powyżej, aby obejmował sprawdzenie plansz graczy.
            if (this.aktualnyStanRundy == null) { // Jeśli mimo wszystko aktualnyStanRundy jest null
                this.aktualnyStanRundy = new StanRundy(null, null); // Awaryjne, aby uniknąć NullPointerException dalej, ale to maskuje problem
            }
        }

        this.czyGraZakonczona = false;

        System.out.println("[SILNIK (konstruktor)] this.gracz1: " +
                (this.gracz1 != null && this.gracz1.getProfilUzytkownika() != null ? this.gracz1.getProfilUzytkownika().getNazwaUzytkownika() : "null lub profil null") +
                " (Obiekt ID: " + System.identityHashCode(this.gracz1) + ")");
        System.out.println("[SILNIK (konstruktor)] this.gracz2: " +
                (this.gracz2 != null && this.gracz2.getProfilUzytkownika() != null ? this.gracz2.getProfilUzytkownika().getNazwaUzytkownika() : "null lub profil null") +
                " (Obiekt ID: " + System.identityHashCode(this.gracz2) + ")");
        System.out.println("[SILNIK (konstruktor)] this.graczRozpoczynajacyPartie: " +
                (this.graczRozpoczynajacyPartie != null && this.graczRozpoczynajacyPartie.getProfilUzytkownika() != null ? this.graczRozpoczynajacyPartie.getProfilUzytkownika().getNazwaUzytkownika() : "null lub profil null") +
                " (Obiekt ID: " + System.identityHashCode(this.graczRozpoczynajacyPartie) + ")");
    }
    // Gettery
    public Gracz getGracz1() { return gracz1; }
    public Gracz getGracz2() { return gracz2; }
    public Gracz getGraczAktualnejTury() { return graczAktualnejTury; }
    public StanRundy getStanRundy() { return aktualnyStanRundy; }
    public int getNumerRundyGry() { return numerRundyGry; }
    public boolean isCzyGraZakonczona() { return czyGraZakonczona; }
    public boolean isOczekiwanieNaPotwierdzenieTury() { return oczekiwanieNaPotwierdzenieTury; }
    public Gracz getGraczUzywajacyManekina() {
        return this.graczUzywajacyManekina;
    }
    private void resetStanuWyboruEmhyra() {
        this.oczekiwanieNaWyborKartyDlaEmhyra = false;
        this.graczAktywujacyEmhyra = null;
        this.przeciwnikDlaEmhyra = null;
    }

    private void resetStanuEredina415() {
        this.oczekiwanieNaWyborKartyZCmentarzaPrzezEredina415 = false;
        this.graczAktywujacyEredina415 = null;
    }

    public void rozpocznijGre() {
        this.historiaWynikowRund.clear();
        this.numerRundyGry = 0;
        this.czyGraZakonczona = false;
        this.graczOstatnioWygralRunde = null;
        this.oczekiwanieNaPotwierdzenieTury = false;
        resetStanuManekina();
        resetStanuWyboruEmhyra();
        resetStanuOczekiwaniaNaPolozenieWskrzeszonej();
        resetStanuWyboruPogodyEredina();
        resetStanuEredina414();
        resetStanuEredina415();

        if (gracz1 == null || gracz2 == null) {
            System.err.println("BŁĄD KRYTYCZNY: Nie można rozpocząć gry, gracze są null!");
            return;
        }

        gracz1.resetDoNowejGry();
        gracz2.resetDoNowejGry();

        if (aktualnyStanRundy != null && gracz1.getKartaDowodcy() != null && gracz2.getKartaDowodcy() != null) {
            aktualnyStanRundy.setLeaderIds(gracz1.getKartaDowodcy().getId(), gracz2.getKartaDowodcy().getId());
            System.out.println("SILNIK (rozpocznijGre): ID Liderów ustawione w StanRundy: G1_Lider_ID=" + gracz1.getKartaDowodcy().getId() +
                    ", G2_Lider_ID=" + gracz2.getKartaDowodcy().getId());
        } else {
            System.err.println("SILNIK (rozpocznijGre): Nie można ustawić ID liderów w StanRundy. " +
                    "Gracz1 dowódca: " + (gracz1.getKartaDowodcy() != null) +
                    ", Gracz2 dowódca: " + (gracz2.getKartaDowodcy() != null) +
                    ", StanRundy: " + (aktualnyStanRundy != null));
        }

        if (gracz1.getKartaDowodcy() != null && gracz1.getKartaDowodcy().getId() == 73) {
            gracz2.setZdolnoscDowodcyZablokowana(true);
            System.out.println("SILNIK: Zdolność dowódcy gracza " + gracz2.getProfilUzytkownika().getNazwaUzytkownika() +
                    " została zablokowana przez Emhyra (Biały Płomień) gracza " + gracz1.getProfilUzytkownika().getNazwaUzytkownika() + ".");
        }
        if (gracz2.getKartaDowodcy() != null && gracz2.getKartaDowodcy().getId() == 73) {
            gracz1.setZdolnoscDowodcyZablokowana(true);
            System.out.println("SILNIK: Zdolność dowódcy gracza " + gracz1.getProfilUzytkownika().getNazwaUzytkownika() +
                    " została zablokowana przez Emhyra (Biały Płomień) gracza " + gracz2.getProfilUzytkownika().getNazwaUzytkownika() + ".");
        }

        if (gracz1.getTaliaDoGry() != null) Collections.shuffle(gracz1.getTaliaDoGry());
        if (gracz2.getTaliaDoGry() != null) Collections.shuffle(gracz2.getTaliaDoGry());

        // Ustal liczbę kart startowych z uwzględnieniem Franceski ID 489
        int iloscKartStartowychG1 = ILOSC_KART_STARTOWYCH;
        int iloscKartStartowychG2 = ILOSC_KART_STARTOWYCH;

        if (gracz1.getKartaDowodcy() != null && gracz1.getKartaDowodcy().getId() == 489) {
            iloscKartStartowychG1++;
            System.out.println("SILNIK: Gracz 1 (Francesca ID 489) dociąga dodatkową kartę na start (" + iloscKartStartowychG1 + ").");
        }
        if (gracz2.getKartaDowodcy() != null && gracz2.getKartaDowodcy().getId() == 489) {
            iloscKartStartowychG2++;
            System.out.println("SILNIK: Gracz 2 (Francesca ID 489) dociąga dodatkową kartę na start (" + iloscKartStartowychG2 + ").");
        }

        dociagnijKartyDoReki(gracz1, iloscKartStartowychG1);
        dociagnijKartyDoReki(gracz2, iloscKartStartowychG2);

        System.out.println("SILNIK: Gra rozpoczęta, karty rozdane. Przechodzenie do fazy wymiany kart (Mulligan).");
        rozpocznijFazeMulligan();
    }

    public Gracz getGraczAktywujacyEmhyra() {
        return this.graczAktywujacyEmhyra;
    }

    public Gracz getGraczAktywujacyEredina415() { return this.graczAktywujacyEredina415; }

    private void dociagnijKartyDoReki(Gracz gracz, int iloscDoDociagniecia) { // Zmieniona nazwa parametru dla jasności


        if (gracz == null || gracz.getProfilUzytkownika() == null) {
            System.err.println("Błąd: Próba dociągnięcia kart dla nieistniejącego gracza lub gracza bez profilu.");
            return;
        }

        if (gracz.getReka() == null) {
            gracz.setReka(new ArrayList<>());
        }
        gracz.getReka().clear(); // Czyścimy rękę PRZED początkowym dociągnięciem

        List<Karta> talia = gracz.getTaliaDoGry();
        System.out.println("[SilnikGry.dociagnijKarty] Gracz: " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + ", rozmiar taliaDoGry: " + (talia != null ? talia.size() : "Talia jest NULL"));

        int faktycznieDociagnieto = 0;
        for (int i = 0; i < iloscDoDociagniecia; i++) { // Dociągamy żądaną ilość
            if (!talia.isEmpty()) {
                gracz.getReka().add(talia.remove(0));
                faktycznieDociagnieto++;
            } else {
                System.out.println("SILNIK OSTRZEŻENIE: Talia gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                        " jest pusta. Dociągnięto " + faktycznieDociagnieto + " z " + iloscDoDociagniecia + " kart na start.");
                break; // Nie ma więcej kart do dociągnięcia
            }
        }
        System.out.println("SILNIK DEBUG (start): Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                " dociągnął " + gracz.getReka().size() + " kart. W talii pozostało: " + talia.size());
    }
    public void rozpocznijNowaRunde() {
        resetStanuWyboruEmhyra();
        if (czyGraZakonczona) return;
        if (gracz1 == null || gracz2 == null || aktualnyStanRundy == null) {
            System.err.println("BŁĄD KRYTYCZNY: Nie można rozpocząć nowej rundy, kluczowe obiekty są null!");
            return;
        }
        numerRundyGry++;
        System.out.println("SILNIK: Rozpoczynanie Rundy: " + numerRundyGry);

        gracz1.resetujDoNastepnejRundy();
        gracz2.resetujDoNastepnejRundy();

        aktualnyStanRundy.resetujStanRundy();

        if (numerRundyGry == 1) {
            graczAktualnejTury = graczRozpoczynajacyPartie;
        } else {
            if (graczOstatnioWygralRunde == this.gracz1) {
                graczAktualnejTury = this.gracz2;
            } else if (graczOstatnioWygralRunde == this.gracz2) {
                graczAktualnejTury = this.gracz1;
            } else {
                graczAktualnejTury = (graczPoprzednioZaczynajacyRunde == this.gracz1) ? this.gracz2 : this.gracz1;
            }
        }
        graczPoprzednioZaczynajacyRunde = graczAktualnejTury;
        this.graczOczekujacyNaPotwierdzenie = graczAktualnejTury; // <<< Kluczowa zmiana: ustawiamy kto ma potwierdzić
        oczekiwanieNaPotwierdzenieTury = true;
        System.out.println("[SILNIK (rozpocznijNowaRunde)] Runda " + numerRundyGry + ". Ustalony gracz aktualnej tury (oczekujący): " +
                (this.graczOczekujacyNaPotwierdzenie != null ? this.graczOczekujacyNaPotwierdzenie.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                " (Obiekt ID: " + System.identityHashCode(this.graczOczekujacyNaPotwierdzenie) + ")");
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.pokazPanelPrzejeciaTury(this.graczOczekujacyNaPotwierdzenie); // Panel dla tego, kto ma zacząć
        }
    }

    public Gracz getGraczKladacyKartePoWskrzeszeniu() {
        return this.graczKladacyKartePoWskrzeszeniu;
    }

    public void wykonajZamianeManekinem(Gracz graczZagrywajacyManekina, Karta wybranaJednostka, RzadPlanszy rzadPochodzenia, int indeksWRzedzie) {
        if (!oczekiwanieNaWyborCeluDlaManekina || graczZagrywajacyManekina != this.graczUzywajacyManekina || this.kartaManekinaDoPolozenia == null) {
            System.err.println("SILNIK: Niepoprawny stan do wykonania zamiany manekinem.");
            // Zwróć oryginalny manekin do ręki, jeśli coś poszło nie tak
            if (this.graczUzywajacyManekina != null && this.kartaManekinaDoPolozenia != null &&
                    this.graczUzywajacyManekina.getReka() != null && !this.graczUzywajacyManekina.getReka().contains(this.kartaManekinaDoPolozenia)) {
                this.graczUzywajacyManekina.getReka().add(this.kartaManekinaDoPolozenia);
            }
            resetStanuManekina();
            finalizujTurePoNieudanymZagraniaKarty(graczZagrywajacyManekina); // Przekaż turę, jakby nic się nie stało
            return;
        }

        System.out.println("SILNIK: Wykonuję zamianę manekinem. Manekin: " + kartaManekinaDoPolozenia.getNazwa() + ", Jednostka: " + wybranaJednostka.getNazwa());

        // 1. Usuń wybraną jednostkę z rzędu
        Karta usunietaKarta = rzadPochodzenia.usunKarteJednostkiZIndeksu(indeksWRzedzie);
        if (usunietaKarta != wybranaJednostka) { // Sanity check
            System.err.println("SILNIK: Błąd! Usunięto inną kartę (" + (usunietaKarta != null ? usunietaKarta.getNazwa() : "null") + ") niż oczekiwano (" + wybranaJednostka.getNazwa() + ") podczas zamiany manekinem.");
            // Spróbuj przywrócić usuniętą kartę (jeśli to możliwe) i zwróć manekina do ręki
            if (usunietaKarta != null) rzadPochodzenia.getKartyJednostekWRzedzie().add(indeksWRzedzie, usunietaKarta);
            if (graczUzywajacyManekina.getReka() != null && !graczUzywajacyManekina.getReka().contains(kartaManekinaDoPolozenia)) {
                graczUzywajacyManekina.getReka().add(kartaManekinaDoPolozenia);
            }
            resetStanuManekina();
            finalizujTurePoNieudanymZagraniaKarty(graczUzywajacyManekina);
            return;
        }

        // 2. Dodaj wybraną jednostkę do ręki gracza
        graczUzywajacyManekina.getReka().add(wybranaJednostka);
        System.out.println(" > Jednostka " + wybranaJednostka.getNazwa() + " wróciła do ręki gracza " + graczUzywajacyManekina.getProfilUzytkownika().getNazwaUzytkownika());

        // 3. Umieść kartę Manekina na planszy w miejscu usuniętej jednostki
        // Karta manekina z CSV ma typ SPECJALNA, ale dodajemy ją do listy jednostek w rzędzie.
        // RzadPlanszy.kartyJednostekWRzedzie jest ObservableList<Karta>, więc może przyjąć kartę specjalną.
        // Jej siła (0) będzie poprawnie uwzględniana przez przeliczSumePunktow.
        rzadPochodzenia.getKartyJednostekWRzedzie().add(indeksWRzedzie, kartaManekinaDoPolozenia);
        System.out.println(" > Manekin " + kartaManekinaDoPolozenia.getNazwa() + " umieszczony w rzędzie " + rzadPochodzenia.getTypRzedu() + " na pozycji " + indeksWRzedzie);

        // 4. Zresetuj stan manekina i sfinalizuj turę
        resetStanuManekina();
        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();

        // Finalizuj turę - przekaż ruch przeciwnikowi
        Gracz przeciwnik = (graczUzywajacyManekina == this.gracz1) ? this.gracz2 : this.gracz1;
        if (przeciwnik.isCzySpasowalWRundzie()) {
            graczAktualnejTury = graczUzywajacyManekina;
            oczekiwanieNaPotwierdzenieTury = false;
            setGraczOczekujacyNaPotwierdzenie(null);
            if (kontrolerPlanszy != null) kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        } else {
            oczekiwanieNaPotwierdzenieTury = true;
            setGraczOczekujacyNaPotwierdzenie(przeciwnik);
            if (kontrolerPlanszy != null) kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }

    public void anulujZagranieManekina(Gracz graczAnulujacy, boolean czyBladSystemowy) { // Dodajmy parametr, czy to błąd
        if (!oczekiwanieNaWyborCeluDlaManekina || graczAnulujacy != this.graczUzywajacyManekina || this.kartaManekinaDoPolozenia == null) {
            System.err.println("SILNIK: Niepoprawny stan do anulowania zagrania manekinem lub brak danych.");
            resetStanuManekina();
            // Jeśli to błąd systemowy, można by rozważyć inne zakończenie tury.
            // Na razie, jeśli stan jest zły, po prostu resetujemy i nic więcej.
            if (czyBladSystemowy && this.kontrolerPlanszy != null) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd Manekina. Spróbuj ponownie.", true);
                // W przypadku błędu, tura nie powinna przechodzić, gracz powinien móc wybrać inną akcję.
                // Interfejs powinien zostać odblokowany dla tego samego gracza.
                this.kontrolerPlanszy.resetujStanZaznaczonejKarty();
                this.kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAnulujacy == this.gracz1);
            }
            return;
        }

        System.out.println("SILNIK: Gracz " + graczAnulujacy.getProfilUzytkownika().getNazwaUzytkownika() + " anulował zagranie Manekina.");

        // Zwróć kartę Manekina do ręki (bo została usunięta, gdy znaleziono cele)
        if (graczAnulujacy.getReka() != null && this.kartaManekinaDoPolozenia != null &&
                !graczAnulujacy.getReka().contains(this.kartaManekinaDoPolozenia)) {
            graczAnulujacy.getReka().add(this.kartaManekinaDoPolozenia);
        }

        resetStanuManekina();

        if (this.kontrolerPlanszy != null) {
            this.kontrolerPlanszy.odswiezCalakolwiekPlansze(); // Pokaż Manekina z powrotem w ręce
            this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Zagranie Manekina anulowane. Wybierz inną akcję.", false);
            this.kontrolerPlanszy.resetujStanZaznaczonejKarty(); // Ważne, aby zresetować stan w kontrolerze
            this.kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAnulujacy == this.gracz1); // Uaktywnij interfejs dla tego samego gracza
        }
        // WAŻNE: Tura NIE przechodzi. Gracz może wybrać inną kartę/akcję.
    }

    private void resetStanuEredina414() {
        this.oczekiwanieNaWyborKartDoOdrzuceniaEredin414 = false;
        this.oczekiwanieNaWyborKartyZTaliiPrzezEredin414 = false;
        this.graczAktywujacyEredina414 = null;
        if (this.kartyWybraneDoOdrzuceniaEredin414 != null) {
            this.kartyWybraneDoOdrzuceniaEredin414.clear();
        } else {
            this.kartyWybraneDoOdrzuceniaEredin414 = new ArrayList<>();
        }
    }

    // Metoda pomocnicza do finalizowania tury, gdy karta nie została zagrana poprawnie lub akcja została anulowana
    private void finalizujTurePoNieudanymZagraniaKarty(Gracz graczKtoryProbowałZagrać) {
        System.out.println("SILNIK: Finalizowanie tury po nieudanym/anulowanym zagraniu karty przez " + graczKtoryProbowałZagrać.getProfilUzytkownika().getNazwaUzytkownika());
        // Nie przeliczamy punktów, bo zakładamy, że stan planszy się nie zmienił lub został przywrócony
        if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze(); // Aby odświeżyć np. rękę

        Gracz przeciwnik = (graczKtoryProbowałZagrać == this.gracz1) ? this.gracz2 : this.gracz1;
        if (przeciwnik.isCzySpasowalWRundzie()) {
            graczAktualnejTury = graczKtoryProbowałZagrać;
            oczekiwanieNaPotwierdzenieTury = false;
            setGraczOczekujacyNaPotwierdzenie(null);
            if (kontrolerPlanszy != null) kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        } else {
            oczekiwanieNaPotwierdzenieTury = true;
            setGraczOczekujacyNaPotwierdzenie(przeciwnik);
            if (kontrolerPlanszy != null) kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }

// W klasie SilnikGry.java

    public boolean zagrajKarte(Gracz graczZagrywajacy, Karta karta, TypRzeduEnum rzadDocelowyEnumDlaKartyPierwotnej) {
        if (graczZagrywajacy == null || karta == null || aktualnyStanRundy == null) {
            System.err.println("Błąd w zagrajKarte: graczZagrywajacy, karta lub aktualnyStanRundy jest null");
            return false;
        }
        if (czyGraZakonczona || oczekiwanieNaPotwierdzenieTury || graczZagrywajacy != graczAktualnejTury ||
                graczZagrywajacy.getReka() == null ||
                !graczZagrywajacy.getReka().contains(karta) ||
                graczZagrywajacy.isCzySpasowalWRundzie()) {
            System.err.println("SILNIK: Nie można zagrać karty - niepoprawny stan lub nie tura gracza. Gracz: " +
                    (graczZagrywajacy.getProfilUzytkownika() != null ? graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika() : "B/D") +
                    ", Karta: " + karta.getNazwa());
            if(kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Nie można teraz zagrać tej karty.", true);
            return false;
        }

        if (oczekiwanieNaWyborRzeduPoWskrzeszeniu || oczekiwanieNaWyborCeluDlaManekina || oczekiwanieNaWyborKartyDlaEmhyra ||
                oczekiwanieNaWyborKartyPogodyPrzezEredina || oczekiwanieNaWyborKartDoOdrzuceniaEredin414 ||
                oczekiwanieNaWyborKartyZTaliiPrzezEredin414 || oczekiwanieNaWyborKartyZCmentarzaPrzezEredina415) { // Sprawdź wszystkie stany oczekiwania
            System.err.println("SILNIK: Próba zagrania karty podczas oczekiwania na inną akcję specjalną.");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Dokończ poprzednią akcję specjalną.", true);
            return false;
        }

        boolean czyZagrywanaKartaToSzpieg = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
        boolean czyZagrywanaKartaMaZmartwychwstanie = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Zmartwychwstanie");
        boolean czyZagrywanaKartaMaPozogaS = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Pożoga_S");
        boolean czyZagrywanaKartaMaPozogaK = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Pożoga_K");
        boolean czyZagrywanaKartaToManekin = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Manekin do ćwiczeń");

        System.out.println("SILNIK: Gracz " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika() + " gra: " + karta.getNazwa() +
                (rzadDocelowyEnumDlaKartyPierwotnej != null ? " na rząd " + rzadDocelowyEnumDlaKartyPierwotnej.getNazwaWyswietlana() : "") +
                (czyZagrywanaKartaToSzpieg ? " (SZPIEG)" : "") +
                (czyZagrywanaKartaMaZmartwychwstanie ? " (ZMARTWYCHWSTANIE)" : "") +
                (czyZagrywanaKartaMaPozogaS ? " (POŻOGA_S)" : "") +
                (czyZagrywanaKartaMaPozogaK ? " (POŻOGA_K)" : "") +
                (czyZagrywanaKartaToManekin ? " (MANEKIN)" : "")
        );

        // Logika dla Manekina - musi być przed usunięciem karty z ręki, jeśli ma zwracać false
        if (czyZagrywanaKartaToManekin && karta.getTyp() == TypKartyEnum.SPECJALNA) {
            List<Karta> celeDlaManekina = znajdzPoprawneCeleDlaManekina(graczZagrywajacy);

            if (celeDlaManekina.isEmpty()) {
                System.out.println("SILNIK: Brak celów dla Manekina na planszy gracza " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika());
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Brak jednostek do zamiany z Manekinem. Wybierz inną kartę.", false);
                    this.kontrolerPlanszy.resetujStanZaznaczonejKarty();
                }
                return false; // Karta nie zagrana, nie usuwamy z ręki
            }

            // Cele istnieją, usuń kartę z ręki i rozpocznij interakcję
            if (!graczZagrywajacy.getReka().remove(karta)) {
                System.err.println("SILNIK: Błąd krytyczny - karty Manekin nie udało się usunąć z ręki.");
                if (this.kontrolerPlanszy != null) this.kontrolerPlanszy.resetujStanZaznaczonejKarty();
                return false;
            }

            this.oczekiwanieNaWyborCeluDlaManekina = true;
            this.kartaManekinaDoPolozenia = karta;
            this.graczUzywajacyManekina = graczZagrywajacy;

            if (this.kontrolerPlanszy != null) {
                this.kontrolerPlanszy.rozpocznijWyborCeluDlaManekina(graczZagrywajacy, celeDlaManekina);
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Wybierz jednostkę (nie Bohatera) do zamiany z Manekinem.", false);
            }
            // Interakcja z manekinem rozpoczęta, tura nie kończy się tutaj.
            // Zakończenie tury nastąpi w wykonajZamianeManekinem() lub anulujZagranieManekina().
            return true;
        }

        // Usunięcie karty z ręki dla wszystkich innych kart (poza Manekinem, który jest obsługiwany wyżej)
        if (!graczZagrywajacy.getReka().remove(karta)) {
            System.err.println("SILNIK: Błąd krytyczny - karty " + karta.getNazwa() + " nie udało się usunąć z ręki gracza " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika());
            // Może nie być potrzeby resetowania stanu zaznaczonej karty w kontrolerze tutaj,
            // bo jeśli karta nie mogła być usunięta z ręki, to jest to błąd wewnętrzny silnika.
            return false;
        }

        boolean czyPoprawnieZagranaPierwotnaKarta = false;
        RzadPlanszy rzadNaKtoryTrafiaPierwotnaKarta = null;

        if (karta.getTyp() == TypKartyEnum.SPECJALNA) {
            String nazwaKartySpecjalnej = karta.getNazwa().toLowerCase();
            if (nazwaKartySpecjalnej.contains("mróz") || nazwaKartySpecjalnej.contains("mgła") || nazwaKartySpecjalnej.contains("deszcz")) {
                aktualnyStanRundy.dodajKartePogody(karta);
                czyPoprawnieZagranaPierwotnaKarta = true;
            } else if (nazwaKartySpecjalnej.contains("czyste niebo") || nazwaKartySpecjalnej.contains("słoneczna pogoda")) {
                aktualnyStanRundy.wyczyscPogode();
                if (graczZagrywajacy.getOdrzucone() != null) graczZagrywajacy.getOdrzucone().add(karta);
                else System.err.println("Cmentarz gracza zagrywającego jest null!");
                czyPoprawnieZagranaPierwotnaKarta = true;
            } else if (nazwaKartySpecjalnej.contains("róg dowódcy")) {
                PlanszaGracza planszaGraczaZgr = (graczZagrywajacy == this.gracz1) ? aktualnyStanRundy.getPlanszaGracza1() : aktualnyStanRundy.getPlanszaGracza2();
                if (rzadDocelowyEnumDlaKartyPierwotnej != null && planszaGraczaZgr != null && planszaGraczaZgr.getRzad(rzadDocelowyEnumDlaKartyPierwotnej) != null) {
                    if (planszaGraczaZgr.getRzad(rzadDocelowyEnumDlaKartyPierwotnej).getKartaRoguDowodcy() == null && !planszaGraczaZgr.getRzad(rzadDocelowyEnumDlaKartyPierwotnej).isWzmocnieniePrzezDowodceAktywne()) {
                        planszaGraczaZgr.getRzad(rzadDocelowyEnumDlaKartyPierwotnej).setKartaRoguDowodcy(karta);
                        czyPoprawnieZagranaPierwotnaKarta = true;
                    } else {
                        System.err.println("SILNIK: Róg Dowódcy (" + karta.getNazwa() + ") nie może być zagrany - slot w rzędzie " + rzadDocelowyEnumDlaKartyPierwotnej + " jest już zajęty lub wzmocniony przez dowódcę.");
                    }
                } else {
                    System.err.println("SILNIK: Róg Dowódcy (" + karta.getNazwa() + ") wymaga wyboru poprawnego rzędu lub plansza/rząd gracza jest null.");
                }
            } else if (czyZagrywanaKartaMaPozogaS) {
                // Pożoga_S sama w sobie nie idzie na planszę, efekt aktywowany, karta na cmentarz
                if (graczZagrywajacy.getOdrzucone() != null) graczZagrywajacy.getOdrzucone().add(karta);
                else System.err.println("Cmentarz gracza zagrywającego jest null dla Pożoga_S!");
                czyPoprawnieZagranaPierwotnaKarta = true;
            }
            // Manekin do ćwiczeń jest już obsłużony na początku metody
            else if (!czyZagrywanaKartaToManekin) { // Upewnijmy się, że Manekin nie jest tu ponownie przetwarzany
                System.out.println("SILNIK: Zagrano inną kartę specjalną: " + karta.getNazwa() + " (nie pogodę, czyste niebo, róg, pożogę S, manekina). Trafia na cmentarz.");
                if (graczZagrywajacy.getOdrzucone() != null) graczZagrywajacy.getOdrzucone().add(karta);
                else System.err.println("Cmentarz gracza zagrywającego jest null dla karty: " + karta.getNazwa());
                czyPoprawnieZagranaPierwotnaKarta = true;
            }
        } else if (karta.getTyp() == TypKartyEnum.JEDNOSTKA || karta.getTyp() == TypKartyEnum.BOHATER) {
            PlanszaGracza planszaDocelowaDlaJednostki;
            Gracz graczNaPlanszyKtoregoLadujeJednostka;

            if (czyZagrywanaKartaToSzpieg) {
                graczNaPlanszyKtoregoLadujeJednostka = (graczZagrywajacy == this.gracz1) ? this.gracz2 : this.gracz1;
            } else {
                graczNaPlanszyKtoregoLadujeJednostka = graczZagrywajacy;
            }

            if (graczNaPlanszyKtoregoLadujeJednostka == null) {
                System.err.println("SILNIK: Błąd krytyczny - nie można ustalić gracza docelowego dla jednostki/bohatera: " + karta.getNazwa());
                graczZagrywajacy.getReka().add(karta); // Zwróć kartę
                return false;
            }
            planszaDocelowaDlaJednostki = (graczNaPlanszyKtoregoLadujeJednostka == gracz1) ? aktualnyStanRundy.getPlanszaGracza1() : aktualnyStanRundy.getPlanszaGracza2();

            if (planszaDocelowaDlaJednostki == null) {
                System.err.println("BŁĄD KRYTYCZNY: planszaDocelowaDlaJednostki jest null dla karty: " + karta.getNazwa());
                graczZagrywajacy.getReka().add(karta); // Zwróć kartę
                return false;
            }

            TypRzeduEnum rzadDoUmieszczeniaKarty = rzadDocelowyEnumDlaKartyPierwotnej;
            if (rzadDoUmieszczeniaKarty == null) {
                rzadDoUmieszczeniaKarty = TypRzeduEnum.fromStringPozycjiKarty(karta.getPozycja());
                if (rzadDoUmieszczeniaKarty == null && karta.getPozycja().toLowerCase().contains("dowolne")) {
                    System.err.println("SILNIK: Karta '" + karta.getNazwa() + "' z pozycją 'Dowolne' wymaga wyboru rzędu przez gracza (nie przekazano).");
                    graczZagrywajacy.getReka().add(karta); // Zwróć kartę
                    return false;
                }
            }

            if (rzadDoUmieszczeniaKarty == null) {
                System.err.println("SILNIK: Nie można ustalić rzędu dla karty '" + karta.getNazwa() + "' (pozycja: " + karta.getPozycja() + ").");
                graczZagrywajacy.getReka().add(karta); // Zwróć kartę
                return false;
            }

            rzadNaKtoryTrafiaPierwotnaKarta = planszaDocelowaDlaJednostki.getRzad(rzadDoUmieszczeniaKarty);

            if (rzadNaKtoryTrafiaPierwotnaKarta != null) {
                rzadNaKtoryTrafiaPierwotnaKarta.dodajKarteJednostki(karta);
                czyPoprawnieZagranaPierwotnaKarta = true;
                System.out.println(" > Karta " + karta.getNazwa() + " dodana do rzędu " + rzadDoUmieszczeniaKarty + " na planszy gracza " + graczNaPlanszyKtoregoLadujeJednostka.getProfilUzytkownika().getNazwaUzytkownika());

                if (czyZagrywanaKartaToSzpieg) {
                    System.out.println(" > Gracz " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika() + " dociąga 2 karty za Szpiegostwo.");
                    dociagnijKartyDoRekiPoZagrywce(graczZagrywajacy, 2);
                }

                // Umiejętności Pożoga_K i Zmartwychwstanie mają specjalne timingi i nie są aktywowane przez aktywujUmiejetnosciPoZagrywce.
                // Manekin jest już obsłużony.
                if (!czyZagrywanaKartaMaZmartwychwstanie && !czyZagrywanaKartaMaPozogaK) {
                    System.out.println("  DEBUG (Braterstwo/Inne): Wywołuję aktywujUmiejetnosciPoZagrywce dla: " + karta.getNazwa());
                    aktywujUmiejetnosciPoZagrywce(graczNaPlanszyKtoregoLadujeJednostka, karta, rzadNaKtoryTrafiaPierwotnaKarta, false);
                }
            } else {
                System.err.println("SILNIK: Nie można było uzyskać konkretnego rzędu (" + rzadDoUmieszczeniaKarty + ") dla karty " + karta.getNazwa() + ".");
                graczZagrywajacy.getReka().add(karta); // Zwróć kartę
                return false;
            }
        } else { // Typ karty nie jest ani SPECJALNA, ani JEDNOSTKA, ani BOHATER
            System.err.println("SILNIK: Próba zagrania karty nieobsługiwanego typu na planszę: " + (karta.getTyp() != null ? karta.getTyp().name() : "TYP NULL"));
            graczZagrywajacy.getReka().add(karta); // Zwróć kartę
            return false;
        }

        // Jeśli karta została poprawnie umieszczona na planszy lub jej efekt (jak pogoda) został zastosowany
        if (czyPoprawnieZagranaPierwotnaKarta) {
            // Aktywacja efektów, które dzieją się PO umieszczeniu karty lub globalnie
            if (czyZagrywanaKartaMaPozogaS) {
                aktywujPozogaGlobalna(graczZagrywajacy);
            } else if (czyZagrywanaKartaMaPozogaK) {
                // Pożoga_K aktywowana jest z jednostki, rzadNaKtoryTrafiaPierwotnaKarta powinien być rzędem tej jednostki
                aktywujPozogaNaRzedzie(graczZagrywajacy, karta, rzadNaKtoryTrafiaPierwotnaKarta);
            }

            // Specjalna obsługa dla Medyka - rozpoczyna interakcję wyboru karty z cmentarza
            if (czyZagrywanaKartaMaZmartwychwstanie) {
                System.out.println("SILNIK: Karta '" + karta.getNazwa() + "' (Medyk) aktywuje Zmartwychwstanie.");
                przeliczWszystkiePunktyNaPlanszy(); // Przelicz punkty PRZED pokazaniem panelu cmentarza
                if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();

                boolean emhyr71AktywnyWGrze = (gracz1.getKartaDowodcy() != null && gracz1.getKartaDowodcy().getId() == 71 && !gracz1.isZdolnoscDowodcyUzyta()) ||
                        (gracz2.getKartaDowodcy() != null && gracz2.getKartaDowodcy().getId() == 71 && !gracz2.isZdolnoscDowodcyUzyta());

                if (emhyr71AktywnyWGrze) {
                    System.out.println("SILNIK: Aktywny Emhyr 'Najeźdźca Północy'! Losowe wskrzeszenie dla gracza: " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika());
                    if (kontrolerPlanszy != null) {
                        kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Emhyr (Najeźdźca): Losowe wskrzeszenie z Twojego cmentarza...", false);
                    }
                    zastosujEfektLosowegoWskrzeszeniaPrzezNjezdzce(graczZagrywajacy); // Ta metoda umieści kartę
                    // Po losowym wskrzeszeniu, turę finalizuje finalizujTurePoWskrzeszeniu
                    // Ale najpierw musimy sprawdzić auto-pass
                    finalizujTurePoWskrzeszeniu(graczZagrywajacy); // Ta metoda powinna zawierać auto-pass check
                } else {
                    System.out.println("SILNIK: Standardowe Zmartwychwstanie - otwieranie cmentarza dla gracza: " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika());
                    if (this.kontrolerPlanszy != null) {
                        this.kontrolerPlanszy.pokazPanelCmentarza(graczZagrywajacy, "Wybierz kartę do wskrzeszenia", KontrolerPlanszyGry.TrybPaneluCmentarzaKontekst.WSKRZESZANIE_MEDYK);
                    }
                    // Tura nie kończy się tutaj, czeka na wybór z cmentarza lub anulowanie.
                }
                return true; // Interakcja Medyka rozpoczęta
            }

            // Przelicz punkty i odśwież planszę po wszystkich innych efektach
            przeliczWszystkiePunktyNaPlanszy();
            if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();

            // *** NOWA LOGIKA AUTO-PASS (dla kart innych niż Medyk/Manekin, które nie zakończyły tury wcześniej) ***
            if (checkAndPerformAutoPassIfHandEmpty(graczZagrywajacy)) {
                return true; // Auto-pass zainicjowany, dalsza logika zmiany tury obsłużona przez kontynuujPoWyswietleniuInfoOPasie()
            }
            // *** KONIEC NOWEJ LOGIKI AUTO-PASS ***

            // Standardowa logika zmiany tury, jeśli nie było auto-passu
            Gracz przeciwnik = (graczZagrywajacy == this.gracz1) ? this.gracz2 : this.gracz1;
            if (przeciwnik.isCzySpasowalWRundzie()) {
                graczAktualnejTury = graczZagrywajacy;
                oczekiwanieNaPotwierdzenieTury = false;
                setGraczOczekujacyNaPotwierdzenie(null);
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
                }
            } else {
                oczekiwanieNaPotwierdzenieTury = true;
                setGraczOczekujacyNaPotwierdzenie(przeciwnik);
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
                }
            }
            return true; // Karta zagrana pomyślnie

        } else { // Karta nie została poprawnie zagrana (czyPoprawnieZagranaPierwotnaKarta == false)
            // LUB nie była to karta specjalna, jednostka ani bohater (co już powinno zwrócić false wcześniej)

            // Jeśli karta została usunięta z ręki, ale jej zagranie się nie powiodło
            // (np. Róg na zajęty slot, zła pozycja dla jednostki, która nie została przechwycona wcześniej),
            // powinna wrócić do ręki.
            // Sprawdzenie !graczZagrywajacy.getReka().contains(karta) jest ważne, bo karta została usunięta na początku.
            if (!graczZagrywajacy.getReka().contains(karta)) { // Jeśli nie ma jej już w ręce (bo została usunięta)
                graczZagrywajacy.getReka().add(karta); // Zwróć ją do ręki
                System.out.println("SILNIK: Karta " + karta.getNazwa() + " zwrócona do ręki gracza " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika() + " z powodu nieudanego zagrania lub braku celu.");
            }
            if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze(); // Odśwież rękę
            return false; // Zagranie nie powiodło się
        }
    }
    private void aktywujUmiejetnosciPoZagrywce(Gracz graczNaPlanszyKtoregoLadujeKarta, Karta karta, RzadPlanszy rzadNaKtoryTrafia, boolean czyPoWskrzeszeniu) {
        if (karta == null || rzadNaKtoryTrafia == null || graczNaPlanszyKtoregoLadujeKarta == null) return;
        String umiejetnosc = karta.getUmiejetnosc();
        if (umiejetnosc == null || umiejetnosc.trim().isEmpty()) return;

        System.out.println("  DEBUG (aktywujUmiejetnosciPoZagrywce): Karta '" + karta.getNazwa() + "', Umiejętność: '" + umiejetnosc + "'");

        // Zakładamy, że "Braterstwo" to umiejętność typu Muster (wzywanie jednostek)
        if (umiejetnosc.equalsIgnoreCase("Braterstwo")) {
            System.out.println("    DEBUG (Braterstwo): Rozpoznano umiejętność Braterstwo dla '" + karta.getNazwa() + "'.");

            // Efekt Braterstwa (Muster) aktywuje się dla gracza, który KONTROLUJE kartę inicjującą na planszy
            // i ciągnie z WŁASNEJ ręki/talii.
            // 'graczNaPlanszyKtoregoLadujeKarta' to ten, kto kontroluje kartę.
            if (graczNaPlanszyKtoregoLadujeKarta != null) {
                System.out.println("    DEBUG (Braterstwo): Aktywuję Braterstwo dla gracza: " + graczNaPlanszyKtoregoLadujeKarta.getProfilUzytkownika().getNazwaUzytkownika() + " dla karty " + karta.getNazwa());
                aktywujBraterstwo(graczNaPlanszyKtoregoLadujeKarta, karta, rzadNaKtoryTrafia);
            } else {
                System.err.println("    DEBUG (Braterstwo): Błąd - graczNaPlanszyKtoregoLadujeKarta jest null dla Braterstwa.");
            }
        } else if (umiejetnosc.equalsIgnoreCase("Wysokie morale")) {
            System.out.println(" > Karta '" + karta.getNazwa() + "' z umiejętnością 'Wysokie morale' została umieszczona w rzędzie. Efekt jest ciągły (aura).");
            // Nie ma tu dodatkowej akcji, RzadPlanszy.przeliczSumePunktow() obsłuży aurę.
        }
        // TODO: Inne umiejętności aktywowane po zagraniu/wskrzeszeniu (np. Pożoga_K jednostki, jeśli nie jest obsługiwana bezpośrednio w zagrajKarte)
    }
    private void resetStanuManekina() {
        this.oczekiwanieNaWyborCeluDlaManekina = false;
        this.kartaManekinaDoPolozenia = null;
        this.graczUzywajacyManekina = null;
    }



    private void aktywujPozogaGlobalna(Gracz graczZagrywajacy) {
        System.out.println("SILNIK: Aktywacja Pożogi Globalnej (Pożoga_S) przez gracza: " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika());
        List<Karta> wszystkieJednostkiNieBohaterowieNaPlanszy = new ArrayList<>();
        Map<Karta, Gracz> wlascicieleKart = new HashMap<>(); // Do śledzenia, na czyj cmentarz ma trafić karta
        Map<Karta, RzadPlanszy> rzedyKart = new HashMap<>(); // Do śledzenia, z którego rzędu usunąć

        // Zbierz jednostki od gracza 1
        if (aktualnyStanRundy.getPlanszaGracza1() != null) {
            for (TypRzeduEnum typRzedu : TypRzeduEnum.values()) {
                RzadPlanszy rzad = aktualnyStanRundy.getPlanszaGracza1().getRzad(typRzedu);
                if (rzad != null) {
                    for (Karta k : rzad.getKartyJednostekWRzedzie()) {
                        if (k.getTyp() != TypKartyEnum.BOHATER) {
                            wszystkieJednostkiNieBohaterowieNaPlanszy.add(k);
                            wlascicieleKart.put(k, gracz1);
                            rzedyKart.put(k, rzad);
                        }
                    }
                }
            }
        }
        // Zbierz jednostki od gracza 2
        if (aktualnyStanRundy.getPlanszaGracza2() != null) {
            for (TypRzeduEnum typRzedu : TypRzeduEnum.values()) {
                RzadPlanszy rzad = aktualnyStanRundy.getPlanszaGracza2().getRzad(typRzedu);
                if (rzad != null) {
                    for (Karta k : rzad.getKartyJednostekWRzedzie()) {
                        if (k.getTyp() != TypKartyEnum.BOHATER) {
                            wszystkieJednostkiNieBohaterowieNaPlanszy.add(k);
                            wlascicieleKart.put(k, gracz2);
                            rzedyKart.put(k, rzad);
                        }
                    }
                }
            }
        }

        if (wszystkieJednostkiNieBohaterowieNaPlanszy.isEmpty()) {
            System.out.println("SILNIK: Pożoga Globalna - brak jednostek nie-bohaterów na planszy.");
            return;
        }

        // Znajdź maksymalną siłę (uwzględniając aktualne modyfikatory jak Więź, Morale, Róg)
        // Musimy uzyskać aktualną siłę każdej jednostki z jej rzędu, po przeliczeniu punktów rzędu.
        // To jest skomplikowane, bo siła jednostki nie jest bezpośrednio przechowywana jako 'efektywnaSiła'.
        // Prostsze podejście: Pożoga patrzy na bazową siłę karty LUB na siłę wyświetlaną (po wszystkich buffach).
        // Standardowo Pożoga patrzy na aktualną siłę WIDOCZNĄ na planszy.
        // Aby to zrobić poprawnie, musielibyśmy mieć sposób na odpytanie o efektywną siłę KAŻDEJ karty.
        // Na razie, dla uproszczenia, załóżmy, że Pożoga działa na PUNKTY SIŁY karty (bazowe).
        // Jeśli chcesz, aby działała na EFEKTYWNĄ siłę, logika musi być bardziej złożona.
        // W tej implementacji użyję `k.getPunktySily()`, co oznacza bazową siłę.
        // Dla bardziej zaawansowanej wersji, trzeba by iterować po rzędach, tymczasowo obliczać siłę każdej karty w kontekście jej rzędu.

        // Wersja uproszczona - działa na `k.getPunktySily()`
        int maxSila = 0;
        for (Karta k : wszystkieJednostkiNieBohaterowieNaPlanszy) {
            if (k.getPunktySily() > maxSila) {
                maxSila = k.getPunktySily();
            }
        }

        if (maxSila == 0) {
            System.out.println("SILNIK: Pożoga Globalna - brak jednostek z siłą > 0.");
            return;
        }

        List<Karta> kartyDoZniszczenia = new ArrayList<>();
        for (Karta k : wszystkieJednostkiNieBohaterowieNaPlanszy) {
            if (k.getPunktySily() == maxSila) {
                kartyDoZniszczenia.add(k);
            }
        }

        if (!kartyDoZniszczenia.isEmpty()) {
            System.out.print("SILNIK: Pożoga Globalna niszczy (" + maxSila + " pkt): ");
            for (Karta k : kartyDoZniszczenia) {
                Gracz wlasciciel = wlascicieleKart.get(k);
                RzadPlanszy rzad = rzedyKart.get(k);
                if (wlasciciel != null && rzad != null) {
                    rzad.usunKarteJednostki(k); // Usuwa z rzędu i wywołuje przeliczSumePunktow dla tego rzędu
                    wlasciciel.getOdrzucone().add(k);
                    System.out.print(k.getNazwa() + " (gracza " + wlasciciel.getProfilUzytkownika().getNazwaUzytkownika() + "), ");
                }
            }
            System.out.println();
        } else {
            System.out.println("SILNIK: Pożoga Globalna - nie znaleziono kart do zniszczenia.");
        }
        // Punkty zostaną przeliczone globalnie na końcu tury lub przez odswiezCalakolwiekPlansze
    }

    private void aktywujPozogaNaRzedzie(Gracz graczAktywujacy, Karta kartaPozogi, RzadPlanszy rzadGdzieKartaPozogiLandowala) {
        if (rzadGdzieKartaPozogiLandowala == null) {
            System.err.println("SILNIK: Pożoga_K - karta Pozogi nie została umieszczona w żadnym rzędzie.");
            return;
        }
        System.out.println("SILNIK: Aktywacja Pożogi Na Rzędzie (Pożoga_K) przez kartę: " + kartaPozogi.getNazwa() + " (gracza " + graczAktywujacy.getProfilUzytkownika().getNazwaUzytkownika() + ")");

        Gracz przeciwnik = (graczAktywujacy == gracz1) ? gracz2 : gracz1;
        TypRzeduEnum typRzeduDocelowego = rzadGdzieKartaPozogiLandowala.getTypRzedu(); // Typ rzędu, w którym leży karta z Pożogą_K

        PlanszaGracza planszaPrzeciwnika = (przeciwnik == gracz1) ? aktualnyStanRundy.getPlanszaGracza1() : aktualnyStanRundy.getPlanszaGracza2();
        if (planszaPrzeciwnika == null) {
            System.err.println("SILNIK: Pożoga_K - brak planszy przeciwnika.");
            return;
        }
        RzadPlanszy rzadPrzeciwnikaDoSprawdzenia = planszaPrzeciwnika.getRzad(typRzeduDocelowego);

        if (rzadPrzeciwnikaDoSprawdzenia == null) {
            System.out.println("SILNIK: Pożoga_K - przeciwnik nie ma odpowiadającego rzędu (" + typRzeduDocelowego + ").");
            return;
        }

        // Warunek: suma punktów w rzędzie przeciwnika >= 10
        // getSumaPunktowWRzedzie() powinno być aktualne
        int sumaPunktowRzeduPrzeciwnika = rzadPrzeciwnikaDoSprawdzenia.getSumaPunktowWRzedzie();
        System.out.println("SILNIK: Pożoga_K - suma punktów w rzędzie (" + typRzeduDocelowego + ") przeciwnika: " + sumaPunktowRzeduPrzeciwnika);

        if (sumaPunktowRzeduPrzeciwnika >= 10) {
            List<Karta> jednostkiNieBohaterowieWRzedziePrzeciwnika = new ArrayList<>();
            for (Karta k : rzadPrzeciwnikaDoSprawdzenia.getKartyJednostekWRzedzie()) {
                if (k.getTyp() != TypKartyEnum.BOHATER) {
                    jednostkiNieBohaterowieWRzedziePrzeciwnika.add(k);
                }
            }

            if (jednostkiNieBohaterowieWRzedziePrzeciwnika.isEmpty()) {
                System.out.println("SILNIK: Pożoga_K - brak jednostek nie-bohaterów w docelowym rzędzie przeciwnika.");
                return;
            }

            // Podobnie jak w Pożoga_S, tutaj też użyjemy bazowej siły k.getPunktySily() dla uproszczenia.
            // Dla działania na efektywnej sile, potrzebna byłaby bardziej złożona logika.
            int maxSilaNaRzedziePrzeciwnika = 0;
            for (Karta k : jednostkiNieBohaterowieWRzedziePrzeciwnika) {
                if (k.getPunktySily() > maxSilaNaRzedziePrzeciwnika) {
                    maxSilaNaRzedziePrzeciwnika = k.getPunktySily();
                }
            }

            if (maxSilaNaRzedziePrzeciwnika == 0) {
                System.out.println("SILNIK: Pożoga_K - brak jednostek z siłą > 0 w docelowym rzędzie przeciwnika.");
                return;
            }

            List<Karta> kartyDoZniszczenia = new ArrayList<>();
            for (Karta k : jednostkiNieBohaterowieWRzedziePrzeciwnika) {
                if (k.getPunktySily() == maxSilaNaRzedziePrzeciwnika) {
                    kartyDoZniszczenia.add(k);
                }
            }

            if (!kartyDoZniszczenia.isEmpty()) {
                System.out.print("SILNIK: Pożoga_K niszczy w rzędzie " + typRzeduDocelowego + " przeciwnika (" + maxSilaNaRzedziePrzeciwnika + " pkt): ");
                for (Karta k : kartyDoZniszczenia) {
                    rzadPrzeciwnikaDoSprawdzenia.usunKarteJednostki(k); // Usuwa z rzędu i wywołuje przeliczSumePunktow
                    przeciwnik.getOdrzucone().add(k);
                    System.out.print(k.getNazwa() + ", ");
                }
                System.out.println();
            } else {
                System.out.println("SILNIK: Pożoga_K - nie znaleziono kart do zniszczenia w rzędzie przeciwnika.");
            }
        } else {
            System.out.println("SILNIK: Pożoga_K - warunek sumy punktów (>=10) w rzędzie przeciwnika nie został spełniony.");
        }
        // Punkty zostaną przeliczone globalnie na końcu tury lub przez odswiezCalakolwiekPlansze
    }

    public void rozpocznijProcesWskrzeszaniaKarty(Gracz graczWskrzeszajacy, Karta kartaDoWskrzeszenia) {
        System.out.println("SILNIK: Gracz " + graczWskrzeszajacy.getProfilUzytkownika().getNazwaUzytkownika() + " wybrał do wskrzeszenia: " + kartaDoWskrzeszenia.getNazwa());

        if (kartaDoWskrzeszenia.getTyp() != TypKartyEnum.JEDNOSTKA) {
            System.err.println("SILNIK: Próba wskrzeszenia niepoprawnego typu karty: " + kartaDoWskrzeszenia.getTyp() + " ("+kartaDoWskrzeszenia.getNazwa()+")");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Można wskrzeszać tylko zwykłe jednostki (nie Bohaterów ani Specjalne).", true);
            finalizujTurePoNieudanymWskrzeszeniu(graczWskrzeszajacy);
            return;
        }

        if (!graczWskrzeszajacy.getOdrzucone().remove(kartaDoWskrzeszenia)) {
            System.err.println("SILNIK: Błąd krytyczny - karty " + kartaDoWskrzeszenia.getNazwa() + " nie było na cmentarzu gracza " + graczWskrzeszajacy.getProfilUzytkownika().getNazwaUzytkownika());
            finalizujTurePoNieudanymWskrzeszeniu(graczWskrzeszajacy);
            return;
        }

        boolean czySzpieg = kartaDoWskrzeszenia.getUmiejetnosc() != null && kartaDoWskrzeszenia.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
        boolean czyZrecznosc = kartaDoWskrzeszenia.getUmiejetnosc() != null && kartaDoWskrzeszenia.getUmiejetnosc().equalsIgnoreCase("Zręczność");
        boolean czyDowolne = kartaDoWskrzeszenia.getPozycja() != null && kartaDoWskrzeszenia.getPozycja().toLowerCase().contains("dowolne");

        if (czySzpieg) {
            Gracz przeciwnik = (graczWskrzeszajacy == gracz1) ? gracz2 : gracz1;
            PlanszaGracza planszaPrzeciwnika = (przeciwnik == gracz1) ? aktualnyStanRundy.getPlanszaGracza1() : aktualnyStanRundy.getPlanszaGracza2();
            TypRzeduEnum rzadSzpiega = TypRzeduEnum.fromStringPozycjiKarty(kartaDoWskrzeszenia.getPozycja());

            if (rzadSzpiega == null && czyDowolne) {
                System.out.println("SILNIK: Wskrzeszany Szpieg '" + kartaDoWskrzeszenia.getNazwa() + "' ma pozycję 'Dowolne'. Oczekiwanie na wybór rzędu.");
                this.kartaOczekujacaNaPolozeniePoWskrzeszeniu = kartaDoWskrzeszenia;
                this.graczKladacyKartePoWskrzeszeniu = graczWskrzeszajacy;
                this.oczekiwanieNaWyborRzeduPoWskrzeszeniu = true;
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.poprosOWyborRzeduDlaKarty(kartaDoWskrzeszenia, przeciwnik, true);
                }
                return;
            }

            if (rzadSzpiega != null && planszaPrzeciwnika != null && planszaPrzeciwnika.getRzad(rzadSzpiega) != null) {
                RzadPlanszy rzadDocelowy = planszaPrzeciwnika.getRzad(rzadSzpiega);
                rzadDocelowy.dodajKarteJednostki(kartaDoWskrzeszenia);
                System.out.println("SILNIK: Wskrzeszony szpieg " + kartaDoWskrzeszenia.getNazwa() + " umieszczony na planszy przeciwnika w rzędzie " + rzadSzpiega);
                dociagnijKartyDoRekiPoZagrywce(graczWskrzeszajacy, 2);
                aktywujUmiejetnosciPoZagrywce(przeciwnik, kartaDoWskrzeszenia, rzadDocelowy, true);
                // USUNIĘTO: sprawdzIZastosujEfektEmhyraNjezdzcy(graczWskrzeszajacy, kartaDoWskrzeszenia);
                finalizujTurePoWskrzeszeniu(graczWskrzeszajacy);
            } else {
                System.err.println("SILNIK: Nie można umieścić wskrzeszonego szpiega ("+kartaDoWskrzeszenia.getNazwa()+") - niepoprawny rząd docelowy: " + rzadSzpiega + " lub plansza przeciwnika jest null.");
                graczWskrzeszajacy.getOdrzucone().add(kartaDoWskrzeszenia);
                finalizujTurePoNieudanymWskrzeszeniu(graczWskrzeszajacy);
            }
        } else if (czyZrecznosc || czyDowolne) {
            System.out.println("SILNIK: Wskrzeszana karta '" + kartaDoWskrzeszenia.getNazwa() + "' ma Zręczność/Dowolne. Oczekiwanie na wybór rzędu.");
            this.kartaOczekujacaNaPolozeniePoWskrzeszeniu = kartaDoWskrzeszenia;
            this.graczKladacyKartePoWskrzeszeniu = graczWskrzeszajacy;
            this.oczekiwanieNaWyborRzeduPoWskrzeszeniu = true;
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.poprosOWyborRzeduDlaKarty(kartaDoWskrzeszenia, graczWskrzeszajacy, true);
            }
        } else { // Standardowa jednostka (nie szpieg, nie zręczność/dowolne)
            PlanszaGracza planszaGraczaWskrzeszajacego = (graczWskrzeszajacy == gracz1) ? aktualnyStanRundy.getPlanszaGracza1() : aktualnyStanRundy.getPlanszaGracza2();
            TypRzeduEnum rzadKarty = TypRzeduEnum.fromStringPozycjiKarty(kartaDoWskrzeszenia.getPozycja());

            if (rzadKarty != null && planszaGraczaWskrzeszajacego != null && planszaGraczaWskrzeszajacego.getRzad(rzadKarty) != null) {
                RzadPlanszy rzadDocelowy = planszaGraczaWskrzeszajacego.getRzad(rzadKarty);
                rzadDocelowy.dodajKarteJednostki(kartaDoWskrzeszenia);
                System.out.println("SILNIK: Wskrzeszona karta " + kartaDoWskrzeszenia.getNazwa() + " umieszczona w rzędzie " + rzadKarty);
                aktywujUmiejetnosciPoZagrywce(graczWskrzeszajacy, kartaDoWskrzeszenia, rzadDocelowy, true);
                // USUNIĘTO: sprawdzIZastosujEfektEmhyraNjezdzcy(graczWskrzeszajacy, kartaDoWskrzeszenia);
                finalizujTurePoWskrzeszeniu(graczWskrzeszajacy);
            } else {
                System.err.println("SILNIK: Nie można umieścić wskrzeszonej karty '" + kartaDoWskrzeszenia.getNazwa() + "' - niepoprawny lub nieokreślony rząd docelowy (pozycja: '" + kartaDoWskrzeszenia.getPozycja() + "') lub plansza gracza jest null.");
                graczWskrzeszajacy.getOdrzucone().add(kartaDoWskrzeszenia);
                finalizujTurePoNieudanymWskrzeszeniu(graczWskrzeszajacy);
            }
        }
    }    public void polozWskrzeszonaKarteNaRzedzie(Gracz graczKtoryWybralRzadFaktycznie, Karta karta, TypRzeduEnum rzadDocelowyEnum) {
        // graczKladacyKartePoWskrzeszeniu to ten, kto oryginalnie zagrał medyka
        // graczKtoryWybralRzadFaktycznie to ten, kto fizycznie kliknął rząd (w hotseat to będzie graczKladacyKartePoWskrzeszeniu)

        if (!oczekiwanieNaWyborRzeduPoWskrzeszeniu || karta != kartaOczekujacaNaPolozeniePoWskrzeszeniu || graczKladacyKartePoWskrzeszeniu == null) {
            System.err.println("SILNIK: Niespójny stan przy próbie położenia wskrzeszonej karty (oczekiwanie: " + oczekiwanieNaWyborRzeduPoWskrzeszeniu +
                    ", karta oczekiwana: " + (kartaOczekujacaNaPolozeniePoWskrzeszeniu != null ? kartaOczekujacaNaPolozeniePoWskrzeszeniu.getNazwa() : "null") +
                    ", karta aktualna: " + (karta != null ? karta.getNazwa() : "null") +
                    ", gracz kładący: " + (graczKladacyKartePoWskrzeszeniu != null ? graczKladacyKartePoWskrzeszeniu.getProfilUzytkownika().getNazwaUzytkownika() : "null") + ")");

            // Zwróć kartę na cmentarz gracza, który miał ją wskrzesić
            if(kartaOczekujacaNaPolozeniePoWskrzeszeniu != null && graczKladacyKartePoWskrzeszeniu != null) {
                graczKladacyKartePoWskrzeszeniu.getOdrzucone().add(kartaOczekujacaNaPolozeniePoWskrzeszeniu);
            } else if (karta != null) {
                Gracz graczDoZwrotuKarty = graczKladacyKartePoWskrzeszeniu != null ? graczKladacyKartePoWskrzeszeniu : graczKtoryWybralRzadFaktycznie;
                if (graczDoZwrotuKarty != null) {
                    graczDoZwrotuKarty.getOdrzucone().add(karta);
                }
            }
            finalizujTurePoNieudanymWskrzeszeniu(graczKladacyKartePoWskrzeszeniu != null ? graczKladacyKartePoWskrzeszeniu : graczKtoryWybralRzadFaktycznie);
            resetStanuOczekiwaniaNaPolozenieWskrzeszonej();
            return;
        }

        // Używamy graczKladacyKartePoWskrzeszeniu jako tego, kto inicjował wskrzeszanie
        System.out.println("SILNIK: Gracz " + graczKtoryWybralRzadFaktycznie.getProfilUzytkownika().getNazwaUzytkownika() +
                " wybrał rząd " + rzadDocelowyEnum +
                " dla wskrzeszonej karty " + karta.getNazwa() +
                " (wskrzeszanej przez " + graczKladacyKartePoWskrzeszeniu.getProfilUzytkownika().getNazwaUzytkownika() + ")");

        PlanszaGracza planszaDocelowa;
        Gracz graczNaPlanszyDocelowej;

        boolean czySzpieg = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
        if (czySzpieg) {
            graczNaPlanszyDocelowej = (graczKladacyKartePoWskrzeszeniu == gracz1) ? gracz2 : gracz1;
            if (graczKtoryWybralRzadFaktycznie != graczNaPlanszyDocelowej) {
                System.err.println("SILNIK (Szpieg): Niespójność! Gracz wybierający rząd dla szpiega ("+graczKtoryWybralRzadFaktycznie.getProfilUzytkownika().getNazwaUzytkownika()+
                        ") nie jest poprawnym przeciwnikiem gracza wskrzeszającego ("+graczKladacyKartePoWskrzeszeniu.getProfilUzytkownika().getNazwaUzytkownika()+
                        "). Oczekiwano: " + graczNaPlanszyDocelowej.getProfilUzytkownika().getNazwaUzytkownika());
                graczKladacyKartePoWskrzeszeniu.getOdrzucone().add(karta);
                finalizujTurePoNieudanymWskrzeszeniu(graczKladacyKartePoWskrzeszeniu);
                resetStanuOczekiwaniaNaPolozenieWskrzeszonej();
                return;
            }
        } else {
            graczNaPlanszyDocelowej = graczKladacyKartePoWskrzeszeniu;
            if (graczKtoryWybralRzadFaktycznie != graczNaPlanszyDocelowej) {
                System.err.println("SILNIK (Nie-Szpieg): Niespójność! Gracz wybierający rząd ("+graczKtoryWybralRzadFaktycznie.getProfilUzytkownika().getNazwaUzytkownika()+
                        ") nie jest graczem wskrzeszającym ("+graczKladacyKartePoWskrzeszeniu.getProfilUzytkownika().getNazwaUzytkownika()+")");
                graczKladacyKartePoWskrzeszeniu.getOdrzucone().add(karta);
                finalizujTurePoNieudanymWskrzeszeniu(graczKladacyKartePoWskrzeszeniu);
                resetStanuOczekiwaniaNaPolozenieWskrzeszonej();
                return;
            }
        }

        planszaDocelowa = (graczNaPlanszyDocelowej == gracz1) ? aktualnyStanRundy.getPlanszaGracza1() : aktualnyStanRundy.getPlanszaGracza2();
        if (planszaDocelowa == null) {
            System.err.println("SILNIK: Błąd krytyczny - planszaDocelowa jest null po ustaleniu graczNaPlanszyDocelowej.");
            graczKladacyKartePoWskrzeszeniu.getOdrzucone().add(karta);
            finalizujTurePoNieudanymWskrzeszeniu(graczKladacyKartePoWskrzeszeniu);
            resetStanuOczekiwaniaNaPolozenieWskrzeszonej();
            return;
        }
        RzadPlanszy rzadNaPlanszy = planszaDocelowa.getRzad(rzadDocelowyEnum);

        if (rzadNaPlanszy != null) {
            rzadNaPlanszy.dodajKarteJednostki(karta);
            System.out.println(" > Wskrzeszona karta " + karta.getNazwa() + " dodana do rzędu " + rzadDocelowyEnum + " na planszy gracza " + graczNaPlanszyDocelowej.getProfilUzytkownika().getNazwaUzytkownika());

            if (czySzpieg) {
                dociagnijKartyDoRekiPoZagrywce(graczKladacyKartePoWskrzeszeniu, 2);
            }
            aktywujUmiejetnosciPoZagrywce(graczNaPlanszyDocelowej, karta, rzadNaPlanszy, true);

            // USUNIĘTO: sprawdzIZastosujEfektEmhyraNjezdzcy(graczKladacyKartePoWskrzeszeniu, karta);

            finalizujTurePoWskrzeszeniu(graczKladacyKartePoWskrzeszeniu);
        } else {
            System.err.println("SILNIK: Błąd krytyczny - nie znaleziono rzędu ("+ rzadDocelowyEnum +") na planszy ("+graczNaPlanszyDocelowej.getProfilUzytkownika().getNazwaUzytkownika()+") dla wskrzeszonej karty.");
            graczKladacyKartePoWskrzeszeniu.getOdrzucone().add(karta);
            finalizujTurePoNieudanymWskrzeszeniu(graczKladacyKartePoWskrzeszeniu);
        }
        resetStanuOczekiwaniaNaPolozenieWskrzeszonej();
    }

    private void resetStanuOczekiwaniaNaPolozenieWskrzeszonej() {
        this.kartaOczekujacaNaPolozeniePoWskrzeszeniu = null;
        this.graczKladacyKartePoWskrzeszeniu = null;
        this.oczekiwanieNaWyborRzeduPoWskrzeszeniu = false;
    }


    private void finalizujTurePoWskrzeszeniu(Gracz graczKtoryWskrzeszal) {
        System.out.println("SILNIK: Finalizowanie tury po udanym wskrzeszeniu przez " + graczKtoryWskrzeszal.getProfilUzytkownika().getNazwaUzytkownika());
        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();
        resetStanuOczekiwaniaNaPolozenieWskrzeszonej();

        Gracz przeciwnik = (graczKtoryWskrzeszal == this.gracz1) ? this.gracz2 : this.gracz1;
        // graczAktualnejTury to wciąż graczKtoryWskrzeszal (ten, kto zagrał medyka)
        if (przeciwnik.isCzySpasowalWRundzie()) {
            // Tura pozostaje u gracza, który wskrzeszał (graczAktualnejTury)
            oczekiwanieNaPotwierdzenieTury = false;
            setGraczOczekujacyNaPotwierdzenie(null);
            System.out.println("SILNIK (po wskrzeszeniu): Przeciwnik spasował, tura pozostaje u " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika());
            if (kontrolerPlanszy != null) kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        } else {
            // Tura przechodzi do przeciwnika
            oczekiwanieNaPotwierdzenieTury = true;
            // setGraczOczekujacyNaPotwierdzenie nie zmienia graczAktualnejTury, tylko kto ma kliknąć "Potwierdź"
            // graczAktualnejTury w silniku zmieni się dopiero po potwierdzeniu przez kontrolerPlanszy -> silnik.potwierdzPrzejecieTury()
            setGraczOczekujacyNaPotwierdzenie(przeciwnik);
            System.out.println("SILNIK (po wskrzeszeniu): Przekazywanie tury do " + przeciwnik.getProfilUzytkownika().getNazwaUzytkownika());
            if (kontrolerPlanszy != null) kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }

    public void anulujWskrzeszanie(Gracz graczKtoryAnulowal) {
        System.out.println("SILNIK: Gracz " + graczKtoryAnulowal.getProfilUzytkownika().getNazwaUzytkownika() + " anulował wskrzeszanie.");
        resetStanuOczekiwaniaNaPolozenieWskrzeszonej(); // Na wszelki wypadek
        // Tura normalnie przechodzi do przeciwnika, tak jakby medyk nie miał dodatkowego efektu
        // lub jakby gracz po prostu zakończył swój ruch.
        finalizujTurePoNieudanymWskrzeszeniu(graczKtoryAnulowal);
    }

    private void finalizujTurePoNieudanymWskrzeszeniu(Gracz graczKtoryProbowaWskrzesic) {
        System.out.println("SILNIK: Finalizowanie tury po nieudanym/anulowanym wskrzeszeniu przez " + graczKtoryProbowaWskrzesic.getProfilUzytkownika().getNazwaUzytkownika());
        przeliczWszystkiePunktyNaPlanszy(); // Przelicz punkty (np. za położonego medyka)
        if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();
        resetStanuOczekiwaniaNaPolozenieWskrzeszonej();

        // graczAktualnejTury to graczKtoryProbowaWskrzesic
        Gracz przeciwnik = (graczKtoryProbowaWskrzesic == this.gracz1) ? this.gracz2 : this.gracz1;
        if (przeciwnik.isCzySpasowalWRundzie()) {
            oczekiwanieNaPotwierdzenieTury = false;
            setGraczOczekujacyNaPotwierdzenie(null);
            System.out.println("SILNIK (po anul. wskrzeszenia): Przeciwnik spasował, tura pozostaje u " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika());
            if (kontrolerPlanszy != null) kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        } else {
            oczekiwanieNaPotwierdzenieTury = true;
            setGraczOczekujacyNaPotwierdzenie(przeciwnik);
            System.out.println("SILNIK (po anul. wskrzeszenia): Przekazywanie tury do " + przeciwnik.getProfilUzytkownika().getNazwaUzytkownika());
            if (kontrolerPlanszy != null) kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }

    private PlanszaGracza getPlanszaGracza(Gracz gracz) {
        if (gracz == null || aktualnyStanRundy == null) {
            System.err.println("SILNIK (getPlanszaGracza): Gracz lub aktualnyStanRundy jest null.");
            return null;
        }
        if (gracz == this.gracz1) {
            return aktualnyStanRundy.getPlanszaGracza1();
        } else if (gracz == this.gracz2) {
            return aktualnyStanRundy.getPlanszaGracza2();
        }
        System.err.println("SILNIK (getPlanszaGracza): Nie udało się zidentyfikować planszy dla podanego gracza.");
        return null;
    }
    private void aktywujBraterstwo(Gracz graczZagrywajacy, Karta kartaInicjujaca, RzadPlanszy rzadDocelowy) {
        String grupaIDKartyInicjujacej = kartaInicjujaca.getGrupaBraterstwa();
        if (grupaIDKartyInicjujacej == null || grupaIDKartyInicjujacej.trim().isEmpty()) {
            System.out.println("    > Karta " + kartaInicjujaca.getNazwa() + " ma umiejętność Braterstwo, ale brak zdefiniowanej 'grupaBraterstwa'.");
            return;
        }
        System.out.println("    Aktywacja Braterstwa dla grupy: '" + grupaIDKartyInicjujacej + "' (karta inicjująca: " + kartaInicjujaca.getNazwa() + ")");

        // 1. Wezwij z ręki
        // Używamy iteratora, aby bezpiecznie usuwać elementy z ręki podczas iteracji
        Iterator<Karta> iteratorReki = graczZagrywajacy.getReka().iterator();
        List<Karta> kartyDoDodaniaZreki = new ArrayList<>(); // Tymczasowa lista, aby uniknąć modyfikacji podczas iteracji
        while (iteratorReki.hasNext()) {
            Karta kartaWRęce = iteratorReki.next();
            // Wzywamy INNE karty (różne ID) z tej samej grupy Braterstwa.
            // Nie wzywamy Bohaterów przez Braterstwo (typowa zasada).
            if (grupaIDKartyInicjujacej.equals(kartaWRęce.getGrupaBraterstwa()) &&
                    kartaWRęce.getId() != kartaInicjujaca.getId() &&
                    kartaWRęce.getTyp() != TypKartyEnum.BOHATER) {

                kartyDoDodaniaZreki.add(kartaWRęce); // Dodaj do listy do późniejszego usunięcia z ręki
            }
        }
        // Teraz usuń z ręki i dodaj na planszę
        for (Karta kartaDoWezwania : kartyDoDodaniaZreki) {
            graczZagrywajacy.getReka().remove(kartaDoWezwania); // Usuń z ręki
            rzadDocelowy.dodajKarteJednostki(kartaDoWezwania);   // Dodaj do rzędu
            System.out.println("    > Wezwano z ręki: " + kartaDoWezwania.getNazwa() + " (ID: " + kartaDoWezwania.getId() + ")");
        }


        // 2. Wezwij z talii
        // Używamy iteratora, aby bezpiecznie usuwać elementy z talii podczas iteracji
        Iterator<Karta> iteratorTalii = graczZagrywajacy.getTaliaDoGry().iterator();
        while (iteratorTalii.hasNext()) {
            Karta kartaWTalii = iteratorTalii.next();
            // Wzywamy karty z tej samej grupy Braterstwa.
            // Nie musimy sprawdzać ID, bo karta inicjująca jest już na planszy, nie w talii.
            // Nie wzywamy Bohaterów.
            if (grupaIDKartyInicjujacej.equals(kartaWTalii.getGrupaBraterstwa()) &&
                    kartaWTalii.getTyp() != TypKartyEnum.BOHATER) {

                iteratorTalii.remove(); // Usuń z talii
                rzadDocelowy.dodajKarteJednostki(kartaWTalii); // Dodaj do rzędu
                System.out.println("    > Wezwano z talii: " + kartaWTalii.getNazwa() + " (ID: " + kartaWTalii.getId() + ")");
            }
        }
        // Punkty zostaną przeliczone globalnie przez przeliczWszystkiePunktyNaPlanszy()
        // wywołane na końcu metody zagrajKarte, jeśli czyPoprawnieZagrana jest true.
    }

    // Upewnij się, że masz tę metodę pomocniczą w klasie SilnikGry:
    private void dociagnijKartyDoRekiPoZagrywce(Gracz gracz, int ilosc) {
        if (gracz == null || gracz.getProfilUzytkownika() == null) {
            System.err.println("Błąd w dociagnijKartyDoRekiPoZagrywce: gracz lub jego profil jest null.");
            return;
        }
        List<Karta> talia = gracz.getTaliaDoGry();
        List<Karta> reka = gracz.getReka();

        if (talia == null) {
            System.err.println("Błąd: Talia gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + " jest null. Nie można dociągnąć kart.");
            return;
        }
        if (reka == null) { // Na wszelki wypadek, gdyby ręka była null
            reka = new ArrayList<>();
            gracz.setReka(reka);
        }

        int dociagnieto = 0;
        for (int i = 0; i < ilosc; i++) {
            if (!talia.isEmpty()) {
                // USUWAMY WARUNEK: if (reka.size() < ILOSC_KART_STARTOWYCH)
                reka.add(talia.remove(0));
                dociagnieto++;
            } else {
                System.out.println("SILNIK OSTRZEŻENIE: Talia gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                        " jest pusta. Dociągnięto " + dociagnieto + " z " + ilosc + " kart po zagrywce.");
                break; // Nie ma więcej kart do dociągnięcia
            }
        }
        System.out.println("SILNIK DEBUG (po zagrywce): Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                " dociągnął " + dociagnieto + " kart(y). Rozmiar ręki: " + reka.size() +
                ". W talii pozostało: " + talia.size());
    }

    public void przejdzDoNastepnegoEtapuPoWynikuRundy() {
        System.out.println("[SILNIK] Kontynuacja po wyświetleniu panelu wyniku rundy.");

        // Krok 1: Przenieś WSZYSTKIE karty z planszy na cmentarze (STANDARDOWO)
        przeniesWszystkieKartyZPlanszNaCmentarze(); // Karta Potwora też idzie na cmentarz

        // Krok 2: Usuń karty pogody
        if (aktualnyStanRundy != null) {
            aktualnyStanRundy.wyczyscPogode();
            System.out.println("[SILNIK] Aktywne karty pogody zostały usunięte z planszy (jeśli jakieś były).");
        }

        // Krok 3: Przelicz punkty (będą 0) i odśwież UI
        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
        }

        // Krok 4: Sprawdź warunki końca gry
        boolean g1Przegral = (gracz1 != null && gracz1.getWygraneRundy() <= 0);
        boolean g2Przegral = (gracz2 != null && gracz2.getWygraneRundy() <= 0);
        boolean maxRundOsiagniety = (numerRundyGry >= 3);

        if (g1Przegral || g2Przegral || maxRundOsiagniety) {
            System.out.println("[SILNIK] Warunki końca gry spełnione. Kończenie gry.");
            zakonczGre();
        } else {
            System.out.println("[SILNIK] Rozpoczynanie nowej rundy (po etapie pośrednim).");
            // Tu NIE ma dociągania kart ani przywracania Potwora
            rozpocznijNowaRunde(); // To ustawi panel przejęcia tury
        }
    }

    public void potwierdzPrzejecieTury() {
        oczekiwanieNaPotwierdzenieTury = false;
        System.out.println("[SILNIK (potwierdzPrzejecieTury)] Wejście. Gracz, który kliknął potwierdzenie (oczekujący): " +
                (graczOczekujacyNaPotwierdzenie != null ? graczOczekujacyNaPotwierdzenie.getProfilUzytkownika().getNazwaUzytkownika() : "BRAK OCZEKUJĄCEGO!") +
                " (Obiekt ID: " + System.identityHashCode(graczOczekujacyNaPotwierdzenie) + ")");

        if (graczOczekujacyNaPotwierdzenie == null) {
            System.err.println("[SILNIK] Krytyczny błąd: graczOczekujacyNaPotwierdzenie jest null przy potwierdzeniu tury! Próba odzyskania...");
            if (gracz1.isCzySpasowalWRundzie() && gracz2.isCzySpasowalWRundzie()) { if (!czyGraZakonczona) zakonczRunde(); return; }
            if (gracz1.isCzySpasowalWRundzie()) graczAktualnejTury = gracz2;
            else if (gracz2.isCzySpasowalWRundzie()) graczAktualnejTury = gracz1;
            else {
                System.err.println("[SILNIK] Awaryjna zmiana tury z: " + (graczAktualnejTury != null ? graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika(): "null"));
                graczAktualnejTury = (graczAktualnejTury == gracz1) ? gracz2 : gracz1;
            }
        } else {
            graczAktualnejTury = graczOczekujacyNaPotwierdzenie;
        }
        graczOczekujacyNaPotwierdzenie = null; // Wyzeruj po użyciu

        System.out.println("[SILNIK] Po potwierdzeniu, ustalony gracz aktualnej tury: " +
                (graczAktualnejTury != null ? graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                " (Obiekt ID: " + System.identityHashCode(graczAktualnejTury) + ")");


        // ---- POCZĄTEK LOGIKI AKTYWACJI UMIEJĘTNOŚCI POTWORA ----
        if (this.kartaPotworaDoZachowaniaNaKolejnaRunde != null &&
                this.graczPotworowZkartaDoZachowania != null &&
                this.rzadKartyPotworaDoZachowania != null) {

            if (this.graczPotworowZkartaDoZachowania.getOdrzucone() != null && getPlanszaGracza(this.graczPotworowZkartaDoZachowania) != null) {
                String nazwaGraczaPotwora = this.graczPotworowZkartaDoZachowania.getProfilUzytkownika().getNazwaUzytkownika();
                String nazwaKartyPotwora = this.kartaPotworaDoZachowaniaNaKolejnaRunde.getNazwa();

                System.out.println("SILNIK (potwierdzPrzejecieTury): Potwory - Aktywacja umiejętności frakcji. Przywracanie karty '" +
                        nazwaKartyPotwora + "' gracza " + nazwaGraczaPotwora);

                // Wywołaj nowy panel *PRZED* dodaniem karty do modelu i odświeżeniem planszy
                if (kontrolerPlanszy != null) {
                    // Przekazujemy nazwę karty do wyświetlenia na panelu, jeśli panel to obsługuje
                    kontrolerPlanszy.pokazPanelUmiejetnosciPotworow(nazwaKartyPotwora);
                    // Poniższy komunikat jest teraz opcjonalny, bo główny jest na panelu
                    // kontrolerPlanszy.wyswietlKomunikatNaPlanszy(
                    //    "Umiejętność Potworów: " + nazwaKartyPotwora + " gracza " + nazwaGraczaPotwora + " pozostaje!", false
                    // );
                }

                boolean usunietoZCmentarza = this.graczPotworowZkartaDoZachowania.getOdrzucone().remove(this.kartaPotworaDoZachowaniaNaKolejnaRunde);
                if (!usunietoZCmentarza) {
                    System.err.println("SILNIK (potwierdzPrzejecieTury): BŁĄD Potwory - nie udało się usunąć karty '" +
                            nazwaKartyPotwora + "' z cmentarza gracza " + nazwaGraczaPotwora + ".");
                } else {
                    System.out.println("SILNIK (potwierdzPrzejecieTury): Potwory - Karta '" +
                            nazwaKartyPotwora + "' usunięta z cmentarza gracza " + nazwaGraczaPotwora + ".");
                }

                PlanszaGracza planszaPotwora = getPlanszaGracza(this.graczPotworowZkartaDoZachowania);
                RzadPlanszy rzadDocelowy = planszaPotwora.getRzad(this.rzadKartyPotworaDoZachowania);

                if (rzadDocelowy != null) {
                    rzadDocelowy.dodajKarteJednostki(this.kartaPotworaDoZachowaniaNaKolejnaRunde);
                    System.out.println("SILNIK (potwierdzPrzejecieTury): Potwory - Karta '" +
                            nazwaKartyPotwora + "' przywrócona do rzędu " + this.rzadKartyPotworaDoZachowania + " na planszy gracza " + nazwaGraczaPotwora);
                } else {
                    System.err.println("SILNIK (potwierdzPrzejecieTury): BŁĄD Potwory - nie znaleziono rzędu docelowego " +
                            this.rzadKartyPotworaDoZachowania + " dla karty '" + nazwaKartyPotwora + "'. Zwracanie na cmentarz, jeśli usunięto.");
                    if(usunietoZCmentarza) this.graczPotworowZkartaDoZachowania.getOdrzucone().add(this.kartaPotworaDoZachowaniaNaKolejnaRunde);
                }
                // Aktualizacja sumy punktów planszy gracza Potworów
                if (planszaPotwora != null) { // Dodatkowe sprawdzenie na wszelki wypadek
                    planszaPotwora.przeliczLacznaSumePunktow();
                }

            } else {
                System.err.println("SILNIK (potwierdzPrzejecieTury): Potwory - Nie można przywrócić karty, cmentarz lub plansza gracza Potworów (" +
                        (this.graczPotworowZkartaDoZachowania != null ? this.graczPotworowZkartaDoZachowania.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                        ") jest null.");
            }
            // Wyzeruj pola po próbie aktywacji, niezależnie od sukcesu
            this.kartaPotworaDoZachowaniaNaKolejnaRunde = null;
            this.graczPotworowZkartaDoZachowania = null;
            this.rzadKartyPotworaDoZachowania = null;
        }
        // ---- KONIEC LOGIKI AKTYWACJI UMIEJĘTNOŚCI POTWORA ----

        if (graczAktualnejTury != null && checkAndPerformAutoPassIfHandEmpty(graczAktualnejTury)) {
            // Jeśli gracz automatycznie spasował (np. zaczął turę bez kart),
            // metoda checkAndPerformAutoPassIfHandEmpty już wywołała pokazPanelInfoPas.
            // Dalsza logika (zmiana tury/koniec rundy) zostanie obsłużona przez
            // KontrolerPlanszyGry.kontynuujPoWyswietleniuInfoOPasie().
            // Dlatego tutaj po prostu kończymy tę metodę.
            System.out.println("[SILNIK (potwierdzPrzejecieTury)] Gracz " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() + " automatycznie spasował na początku tury (brak kart).");
            return;
        }

        // Standardowa kontynuacja sprawdzania stanu gry i aktywacji interfejsu
        if (gracz1.isCzySpasowalWRundzie() && gracz2.isCzySpasowalWRundzie()) {
            if (!czyGraZakonczona) {
                System.out.println("[SILNIK (potwierdzPrzejecieTury)] Obaj spasowali, kończenie rundy.");
                zakonczRunde();
            }
            return; // Zakończ, jeśli obaj spasowali
        }

        // Sprawdzenie, czy gracz, który potwierdził turę, już spasował
        if (graczAktualnejTury != null && graczAktualnejTury.isCzySpasowalWRundzie()) {
            Gracz aktywnyPrzeciwnik = (graczAktualnejTury == gracz1) ? gracz2 : gracz1;
            if (aktywnyPrzeciwnik != null && !aktywnyPrzeciwnik.isCzySpasowalWRundzie()) {
                System.out.println("[SILNIK] Gracz " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() +
                        " potwierdził, ale jest już spasowany. Przeciwnik " + aktywnyPrzeciwnik.getProfilUzytkownika().getNazwaUzytkownika() +
                        " jest aktywny. Tura przechodzi do przeciwnika, pokazując panel.");
                this.graczOczekujacyNaPotwierdzenie = aktywnyPrzeciwnik; // Kto ma potwierdzić następnym razem
                oczekiwanieNaPotwierdzenieTury = true; // Ustaw ponownie, bo panel dla przeciwnika

                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.odswiezCalakolwiekPlansze(); // Odśwież po ewentualnej aktywacji Potworów
                    kontrolerPlanszy.pokazPanelPrzejeciaTury(this.graczOczekujacyNaPotwierdzenie);
                }
                return; // Zakończ, panel dla aktywnego przeciwnika
            }
        }

        // Jeśli doszło tutaj, graczAktualnejTury jest poprawny i nie jest spasowany (lub jego przeciwnik też spasował)
        if (kontrolerPlanszy != null) {
            // To jest kluczowe odświeżenie, które pokaże kartę Potwora, jeśli została dodana,
            // oraz wszelkie inne zmiany stanu.
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Tura gracza: " + (graczAktualnejTury != null ? graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() : "BŁĄD"), false);
            kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        }
    }
    private void zmienTureFaktycznie() {
        Gracz poprzedniaTura = graczAktualnejTury;
        graczAktualnejTury = (graczAktualnejTury == this.gracz1) ? this.gracz2 : this.gracz1;

        if (graczAktualnejTury.isCzySpasowalWRundzie()) {
            if (poprzedniaTura.isCzySpasowalWRundzie()) {
                System.err.println("SILNIK: Obaj gracze spasowali, runda powinna się już zakończyć.");
                return;
            }
            System.out.println("SILNIK: Gracz " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() + " już spasował, tura wraca do " + poprzedniaTura.getProfilUzytkownika().getNazwaUzytkownika());
            graczAktualnejTury = poprzedniaTura;
        }

        System.out.println("SILNIK: Zmiana tury na: " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika());
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Tura gracza: " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika(), false);
            kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        }
    }

    public void pasuj(Gracz graczKtorySpasowal) {
        if (czyGraZakonczona || oczekiwanieNaPotwierdzenieTury || graczKtorySpasowal != graczAktualnejTury || graczKtorySpasowal.isCzySpasowalWRundzie()) {
            System.err.println("[SILNIK] Nie można spasować w tym momencie.");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Nie można teraz spasować.", true);
            return;
        }

        graczKtorySpasowal.setCzySpasowalWRundzie(true);
        System.out.println("[SILNIK] Gracz " + graczKtorySpasowal.getProfilUzytkownika().getNazwaUzytkownika() + " spasował.");

        // Poinformuj kontroler, aby pokazał panel "Gracz X spasował!"
        // Dalsza logika (zmiana tury, koniec rundy) zostanie obsłużona po zniknięciu tego panelu.
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze(); // Odśwież UI, aby np. wygasić przycisk "Pasuj" spasowanego gracza
            kontrolerPlanszy.pokazPanelInfoPas(graczKtorySpasowal);
        } else {
            // Jeśli nie ma kontrolera (np. testy), od razu przejdź dalej
            kontynuujPoWyswietleniuInfoOPasie();
        }
    }

    public void kontynuujPoWyswietleniuInfoOPasie() {
        // graczAktualnejTury to wciąż ten, który WŁAŚNIE spasował.
        Gracz spasowanyGracz = graczAktualnejTury;
        Gracz drugiGracz = (spasowanyGracz == gracz1) ? gracz2 : gracz1;

        System.out.println("[SILNIK] Kontynuacja po informacji o pasie gracza: " + spasowanyGracz.getProfilUzytkownika().getNazwaUzytkownika());

        if (drugiGracz.isCzySpasowalWRundzie()) {
            // Obaj gracze spasowali, więc zakończ rundę.
            System.out.println("[SILNIK] Obaj gracze spasowali. Kończenie rundy.");
            zakonczRunde();
        } else {
            // Drugi gracz jeszcze nie spasował. Tura przechodzi do niego.
            // Ten gracz będzie teraz kontynuował swój "blok" tur.
            graczAktualnejTury = drugiGracz;
            oczekiwanieNaPotwierdzenieTury = false; // Nie ma potrzeby potwierdzania jego tury panelem "Tura gracza..."
            setGraczOczekujacyNaPotwierdzenie(null); // Nie ma oczekującego na potwierdzenie

            System.out.println("[SILNIK] Po pasie, tura przechodzi do (i pozostaje u): " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika());
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.odswiezCalakolwiekPlansze();
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Tura gracza: " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika(), false);
                kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
            }
        }
    }

    private void przeliczWszystkiePunktyNaPlanszy() {
        // Ta metoda działa na planszach przechowywanych w aktualnyStanRundy,
        // które są teraz tymi samymi planszami, co w obiektach Gracz.
        if (aktualnyStanRundy != null) {
            if (aktualnyStanRundy.getPlanszaGracza1() != null) {
                aktualnyStanRundy.getPlanszaGracza1().przeliczLacznaSumePunktow();
            }
            if (aktualnyStanRundy.getPlanszaGracza2() != null) {
                aktualnyStanRundy.getPlanszaGracza2().przeliczLacznaSumePunktow();
            }
        }
    }

    private void zakonczRunde() {
        if (czyGraZakonczona) return;
        System.out.println("SILNIK: Rozpoczynanie procesu zakończenia Rundy " + numerRundyGry);
        oczekiwanieNaPotwierdzenieTury = false;
        setGraczOczekujacyNaPotwierdzenie(null);

        przeliczWszystkiePunktyNaPlanszy();
        // Sprawdzenie null obiektów przed dostępem
        if (aktualnyStanRundy == null || aktualnyStanRundy.getPlanszaGracza1() == null || aktualnyStanRundy.getPlanszaGracza2() == null ||
                gracz1 == null || gracz1.getProfilUzytkownika() == null || gracz2 == null || gracz2.getProfilUzytkownika() == null) {
            System.err.println("Błąd krytyczny w zakonczRunde: Brak kluczowych obiektów stanu gry lub graczy/profili.");
            zakonczGre(); // Awaryjne zakończenie gry
            return;
        }

        int punktyG1 = aktualnyStanRundy.getPlanszaGracza1().getLacznaSumaPunktowGracza();
        int punktyG2 = aktualnyStanRundy.getPlanszaGracza2().getLacznaSumaPunktowGracza();
        String wiadomoscWynikuRundy = "Błąd: Wynik rundy nie został ustalony.";
        Gracz zwyciezcaRundy = null;

        if (punktyG1 > punktyG2) {
            gracz2.stracZycie();
            zwyciezcaRundy = gracz1;
            wiadomoscWynikuRundy = "Gracz " + gracz1.getProfilUzytkownika().getNazwaUzytkownika() + " wygrywa rundę! (" + punktyG1 + " vs " + punktyG2 + ")";
        } else if (punktyG2 > punktyG1) {
            gracz1.stracZycie();
            zwyciezcaRundy = gracz2;
            wiadomoscWynikuRundy = "Gracz " + gracz2.getProfilUzytkownika().getNazwaUzytkownika() + " wygrywa rundę! (" + punktyG2 + " vs " + punktyG1 + ")";
        } else { // Remis punktowy
            boolean nilfgaardPrzelamalRemis = false;
            if (gracz1.getWybranaFrakcja() == FrakcjaEnum.NILFGAARD && gracz2.getWybranaFrakcja() != FrakcjaEnum.NILFGAARD) {
                gracz2.stracZycie();
                zwyciezcaRundy = gracz1;
                nilfgaardPrzelamalRemis = true;
                wiadomoscWynikuRundy = "Remis! Gracz : " + gracz1.getProfilUzytkownika().getNazwaUzytkownika() + " wygrywa dzięki zdolności talii Nilfgaardu.";
            } else if (gracz2.getWybranaFrakcja() == FrakcjaEnum.NILFGAARD && gracz1.getWybranaFrakcja() != FrakcjaEnum.NILFGAARD) {
                gracz1.stracZycie();
                zwyciezcaRundy = gracz2;
                nilfgaardPrzelamalRemis = true;
                wiadomoscWynikuRundy = "Remis! Gracz : " + gracz2.getProfilUzytkownika().getNazwaUzytkownika() + " wygrywa dzięki zdolności talii Nilfgaardu.";
            }

            if (!nilfgaardPrzelamalRemis) { // Standardowy remis, obaj tracą życie
                gracz1.stracZycie();
                gracz2.stracZycie();
                // ZwyciezcaRundy pozostaje null
                wiadomoscWynikuRundy = "Remis! Obydwaj gracze tracą żeton życia.";
            }
        }
        graczOstatnioWygralRunde = zwyciezcaRundy;

        if (this.historiaWynikowRund != null && this.historiaWynikowRund.size() < 3) { // Ograniczenie do 3 rund
            String podsumowanieRundy = "Runda " + numerRundyGry + ": " + wiadomoscWynikuRundy;
            this.historiaWynikowRund.add(podsumowanieRundy);
        }

        System.out.println("[SILNIK] Wynik rundy ustalony: " + wiadomoscWynikuRundy);

        // ---- POCZĄTEK LOGIKI ZAZNACZANIA KARTY POTWORA ----
        this.kartaPotworaDoZachowaniaNaKolejnaRunde = null; // Resetuj przed każdym sprawdzeniem
        this.rzadKartyPotworaDoZachowania = null;
        this.graczPotworowZkartaDoZachowania = null;

        Gracz graczKtoryMoglGracPotworami = null;
        if (gracz1.getWybranaFrakcja() == FrakcjaEnum.POTWORY) {
            graczKtoryMoglGracPotworami = gracz1;
        } else if (gracz2.getWybranaFrakcja() == FrakcjaEnum.POTWORY) {
            graczKtoryMoglGracPotworami = gracz2;
        }

        if (graczKtoryMoglGracPotworami != null) {
            List<Karta> jednostkiNaPlanszyPotwora = new ArrayList<>();
            Map<Karta, TypRzeduEnum> mapaKartaNaRzad = new HashMap<>();
            // Użyj getPlanszaGracza, która odwołuje się do aktualnyStanRundy
            PlanszaGracza planszaPotwora = getPlanszaGracza(graczKtoryMoglGracPotworami);

            if (planszaPotwora != null) {
                // Iteruj tylko po rzędach jednostek
                for (TypRzeduEnum typRzedu : new TypRzeduEnum[]{TypRzeduEnum.PIECHOTA, TypRzeduEnum.STRZELECKIE, TypRzeduEnum.OBLEZENIE}) {
                    RzadPlanszy rzad = planszaPotwora.getRzad(typRzedu);
                    if (rzad != null) {
                        for (Karta k : rzad.getKartyJednostekWRzedzie()) {
                            if (k.getTyp() == TypKartyEnum.JEDNOSTKA) { // Tylko zwykłe jednostki (nie Bohater, nie Specjalna)
                                jednostkiNaPlanszyPotwora.add(k);
                                mapaKartaNaRzad.put(k, typRzedu);
                            }
                        }
                    }
                }
            }

            if (!jednostkiNaPlanszyPotwora.isEmpty()) {
                Collections.shuffle(jednostkiNaPlanszyPotwora);
                this.kartaPotworaDoZachowaniaNaKolejnaRunde = jednostkiNaPlanszyPotwora.get(0);
                this.rzadKartyPotworaDoZachowania = mapaKartaNaRzad.get(this.kartaPotworaDoZachowaniaNaKolejnaRunde);
                this.graczPotworowZkartaDoZachowania = graczKtoryMoglGracPotworami;
                System.out.println("SILNIK (zakonczRunde): Potwory - Karta '" + this.kartaPotworaDoZachowaniaNaKolejnaRunde.getNazwa() +
                        "' z rzędu " + this.rzadKartyPotworaDoZachowania + " gracza " +
                        this.graczPotworowZkartaDoZachowania.getProfilUzytkownika().getNazwaUzytkownika() + " została ZAZNACZONA do zachowania.");
            } else {
                System.out.println("SILNIK (zakonczRunde): Potwory - brak jednostek na planszy do zaznaczenia.");
            }
        }
        // ---- KONIEC LOGIKI ZAZNACZANIA KARTY POTWORA ----


        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze(); // Odśwież stan przed pokazaniem panelu wyniku
            boolean czyRemisPunktowy = (punktyG1 == punktyG2);
            kontrolerPlanszy.pokazPanelWynikuRundy(wiadomoscWynikuRundy, zwyciezcaRundy, czyRemisPunktowy);
        } else {
            // Jeśli nie ma kontrolera (np. testy jednostkowe), przejdź dalej bezpośrednio
            przejdzDoNastepnegoEtapuPoWynikuRundy();
        }

        // Zdolność frakcji Królestw Północy (pozostaje bez zmian)
        if (zwyciezcaRundy != null && zwyciezcaRundy.getWybranaFrakcja() == FrakcjaEnum.KROLESTWA_POLNOCY) {
            System.out.println("SILNIK: " + zwyciezcaRundy.getProfilUzytkownika().getNazwaUzytkownika() +
                    " (Królestwa Północy) wygrał rundę. Aktywacja zdolności frakcji.");
            dociagnijKarteZeZdolnosci(zwyciezcaRundy, 1, "Królestwa Północy"); // Metoda dociagnijKarteZeZdolnosci zamiast dociagnijKartyDoReki
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy(
                        zwyciezcaRundy.getProfilUzytkownika().getNazwaUzytkownika() +
                                " dobiera kartę dzięki zdolności Królestw Północy!", false
                );
            }
        }
    }
    private void przeniesWszystkieKartyZPlanszNaCmentarze() {
        if (aktualnyStanRundy == null || gracz1 == null || gracz2 == null) {
            System.err.println("[SILNIK] Błąd: Nie można przenieść kart na cmentarze - brak stanu rundy lub graczy.");
            return;
        }
        System.out.println("[SILNIK] Przenoszenie kart z plansz na cmentarze.");

        // Plansza Gracza 1
        PlanszaGracza planszaG1 = aktualnyStanRundy.getPlanszaGracza1();
        if (planszaG1 != null) {
            List<Karta> kartyG1 = planszaG1.wyczyscPlanszeIZwrocKarty(); // Ta metoda zbiera karty jednostek
            gracz1.getOdrzucone().addAll(kartyG1);
            System.out.println("  > Z planszy G1 przeniesiono na cmentarz: " + kartyG1.size() + " kart jednostek.");
            // Obsługa Rogów Dowódcy (jeśli były specjalnymi kartami w slotach)
            przeniesRogDowodcyNaCmentarz(planszaG1.getRzadPiechoty(), gracz1.getOdrzucone());
            przeniesRogDowodcyNaCmentarz(planszaG1.getRzadStrzelecki(), gracz1.getOdrzucone());
            przeniesRogDowodcyNaCmentarz(planszaG1.getRzadOblezenia(), gracz1.getOdrzucone());
        }

        // Plansza Gracza 2
        PlanszaGracza planszaG2 = aktualnyStanRundy.getPlanszaGracza2();
        if (planszaG2 != null) {
            List<Karta> kartyG2 = planszaG2.wyczyscPlanszeIZwrocKarty();
            gracz2.getOdrzucone().addAll(kartyG2);
            System.out.println("  > Z planszy G2 przeniesiono na cmentarz: " + kartyG2.size() + " kart jednostek.");
            przeniesRogDowodcyNaCmentarz(planszaG2.getRzadPiechoty(), gracz2.getOdrzucone());
            przeniesRogDowodcyNaCmentarz(planszaG2.getRzadStrzelecki(), gracz2.getOdrzucone());
            przeniesRogDowodcyNaCmentarz(planszaG2.getRzadOblezenia(), gracz2.getOdrzucone());
        }
    }

    // Metoda pomocnicza do przenoszenia Rogu Dowódcy
    private void przeniesRogDowodcyNaCmentarz(RzadPlanszy rzad, List<Karta> cmentarz) {
        if (rzad != null && rzad.getKartaRoguDowodcy() != null) {
            Karta rog = rzad.getKartaRoguDowodcy();
            // Upewnij się, że Róg jest kartą specjalną, a nie np. jednostką z efektem rogu
            if (rog.getTyp() == TypKartyEnum.SPECJALNA && rog.getNazwa().toLowerCase().contains("róg dowódcy")) {
                cmentarz.add(rog);
                System.out.println("    > Róg Dowódcy z rzędu " + rzad.getTypRzedu() + " przeniesiony na cmentarz.");
            }
            rzad.setKartaRoguDowodcy(null); // Usuń róg ze slotu
        }
    }

    private void zakonczGre() {
        if (czyGraZakonczona) return; // Zapobiegaj wielokrotnemu wywołaniu
        czyGraZakonczona = true;
        oczekiwanieNaPotwierdzenieTury = false;
        setGraczOczekujacyNaPotwierdzenie(null);

        System.out.println("[SILNIK] Gra definitywnie zakończona.");
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.zablokujInterakcjePlanszy();
        }

        Gracz zwyciezcaGry = null;
        boolean czyRemisWGrze = false;

        int zyciaPozostaleG1 = (gracz1 != null) ? gracz1.getWygraneRundy() : 0;
        int zyciaPozostaleG2 = (gracz2 != null) ? gracz2.getWygraneRundy() : 0;

        // Kto wygrał grę? Ten, kto ma więcej pozostałych "żyć" (kamieni wygranych rund).
        // Standardowo każdy ma 2 życia na start. Jeśli ktoś ma 0, przegrał.
        if (zyciaPozostaleG1 <= 0 && zyciaPozostaleG2 <= 0) { // Obaj mają 0 żyć (np. remis w ostatniej możliwej rundzie)
            czyRemisWGrze = true;
        } else if (zyciaPozostaleG2 <= 0) { // Gracz 2 nie ma żyć -> Gracz 1 wygrywa
            zwyciezcaGry = gracz1;
        } else if (zyciaPozostaleG1 <= 0) { // Gracz 1 nie ma żyć -> Gracz 2 wygrywa
            zwyciezcaGry = gracz2;
        } else {
            // Jeśli nikt nie stracił wszystkich żyć (np. po 3 rundach, gdzie były remisy nieprzełamane przez Nilfgaard)
            // Wygrywa ten, kto ma więcej żyć.
            if (zyciaPozostaleG1 > zyciaPozostaleG2) {
                zwyciezcaGry = gracz1;
            } else if (zyciaPozostaleG2 > zyciaPozostaleG1) {
                zwyciezcaGry = gracz2;
            } else { // Tyle samo żyć po max rundach
                czyRemisWGrze = true;
            }
        }

        System.out.println("[SILNIK] Zwycięzca gry: " +
                (zwyciezcaGry != null ? zwyciezcaGry.getProfilUzytkownika().getNazwaUzytkownika() : (czyRemisWGrze ? "REMIS" : "Nieustalony")));

        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze(); // Ostatnie odświeżenie UI
            kontrolerPlanszy.pokazPanelKoncaGry(zwyciezcaGry, czyRemisWGrze, historiaWynikowRund);
        }
    }

    public void uzyjZdolnosciDowodcy(Gracz gracz) {
        if (gracz == null || gracz.getKartaDowodcy() == null) {
            System.err.println("SILNIK: Próba użycia zdolności przez gracza null lub gracza bez karty dowódcy.");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd: Brak danych dowódcy.", true);
            return;
        }

        // 1. Sprawdź, czy zdolność dowódcy gracza jest zablokowana przez Emhyra ID 73
        if (gracz.isZdolnoscDowodcyZablokowana()) {
            System.out.println("SILNIK: Próba użycia zablokowanej zdolności dowódcy przez gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika());
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Twoja zdolność dowódcy jest zablokowana!", true);
            }
            return;
        }

        // 2. Sprawdź ogólne warunki gry
        if (czyGraZakonczona || oczekiwanieNaPotwierdzenieTury || gracz != graczAktualnejTury) {
            System.out.println("Nie można użyć zdolności dowódcy - zły stan gry ("+ (czyGraZakonczona ? "Gra zakończona" : "") + (oczekiwanieNaPotwierdzenieTury ? "OczekiwaniePotwierdzenie" : "") +") lub nie tura gracza (" + (graczAktualnejTury != null ? graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() : "null") +").");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Nie można teraz użyć zdolności dowódcy.", true);
            return;
        }

        // 3. Sprawdź, czy gracz nie jest w trakcie innej specjalnej akcji
        if (oczekiwanieNaWyborRzeduPoWskrzeszeniu || oczekiwanieNaWyborCeluDlaManekina || oczekiwanieNaWyborKartyDlaEmhyra) {
            System.err.println("SILNIK: Próba użycia zdolności dowódcy podczas oczekiwania na inną akcję.");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Dokończ poprzednią akcję.", true);
            return;
        }

        Karta kartaDowodcy = gracz.getKartaDowodcy();
        int idDowodcy = kartaDowodcy.getId();

        // --- POCZĄTEK POPRAWKI: Deklaracja i inicjalizacja 'czyToAktywnaZdolnoscDoZuzycia' ---
        // 4. Określ, czy to zdolność pasywna (której "użycie" przez kliknięcie jest tylko informacyjne)
        boolean czyToPasywnaZdolnoscInformacyjna = (idDowodcy == 71 || idDowodcy == 73); // ID 71 (Najeźdźca), ID 73 (Biały Płomień)
        boolean czyToAktywnaZdolnoscDoZuzycia = !czyToPasywnaZdolnoscInformacyjna; // Wszystkie inne są traktowane jako aktywne
        boolean czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;

        // 5. Sprawdź, czy AKTYWNA zdolność nie została już użyta
        if (czyToAktywnaZdolnoscDoZuzycia && gracz.isZdolnoscDowodcyUzyta()) {
            System.out.println("Zdolność dowódcy " + kartaDowodcy.getNazwa() + " została już użyta.");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Zdolność dowódcy już użyta.", false);
            return;
        }

        System.out.println(gracz.getProfilUzytkownika().getNazwaUzytkownika() + " używa zdolności dowódcy: " + kartaDowodcy.getNazwa());

        boolean czyZdolnoscZostalaFaktycznieUzyta = false;

        // Oznacz zdolność jako użytą dla aktywnych zdolności (z wyjątkiem tych, które wchodzą w stan oczekiwania,
        // np. ID 69 - Pan Południa; dla nich flaga zostanie ustawiona w metodach wykonaj/anuluj wybór)
        if (czyToAktywnaZdolnoscDoZuzycia && idDowodcy != 69) {
            gracz.setZdolnoscDowodcyUzyta(true);
        }

        if (kontrolerPlanszy != null && czyToAktywnaZdolnoscDoZuzycia) { // Odśwież UI tylko jeśli to była aktywna zdolność
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
        }

        // --- Logika specyficzna dla dowódców ---
        if (idDowodcy == 69) { // Emhyr var Emreis "Pan południa" (AKTYWNA, Z INTERAKCJĄ)
            Gracz przeciwnik69 = (gracz == gracz1) ? gracz2 : gracz1;
            if (przeciwnik69 == null || przeciwnik69.getOdrzucone() == null || przeciwnik69.getOdrzucone().isEmpty()) {
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Cmentarz przeciwnika jest pusty. Zdolność Emhyra nie ma celu.", false);
                }
                gracz.setZdolnoscDowodcyUzyta(true); // Mimo braku celu, zdolność zużyta
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false; // Zmienione z czyZdolnoscZostalaFaktycznieUzyta
                // Tura zakończy się w standardowym bloku poniżej, bo nie ma 'return'
            } else {
                this.oczekiwanieNaWyborKartyDlaEmhyra = true;
                this.graczAktywujacyEmhyra = gracz;
                this.przeciwnikDlaEmhyra = przeciwnik69;
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Emhyr: Wybierz kartę z cmentarza przeciwnika.", false);

                    // --- ZMIEŃ TO WYWOŁANIE ---
                    // ZAMIEN: kontrolerPlanszy.pokazPanelCmentarzaDlaWyboru(przeciwnik69, "Cmentarz Przeciwnika - Wybierz Kartę");
                    // NA:
                    kontrolerPlanszy.pokazPanelCmentarza(przeciwnik69, "Cmentarz Przeciwnika - Wybierz Kartę", KontrolerPlanszyGry.TrybPaneluCmentarzaKontekst.EMHYR_PAN_POLUDNIA);
                }
                return; // Czekamy na interakcję, zakończenie tury w metodach wykonaj/anuluj wybór
            }
        } else if (idDowodcy == 70) { // Emhyr var Emreis "Jeż z Erlenwaldu" (AKTYWNA, NATYCHMIASTOWA)
            Karta kartaDeszczuDoZagrnia = null;
            List<Karta> taliaGracza = gracz.getTaliaDoGry();
            Iterator<Karta> iteratorTalii = taliaGracza.iterator();
            while (iteratorTalii.hasNext()) {
                Karta kartaZTali = iteratorTalii.next();
                if (kartaZTali.getNazwa().trim().equalsIgnoreCase("Ulewny deszcz")) {
                    kartaDeszczuDoZagrnia = kartaZTali;
                    iteratorTalii.remove();
                    break;
                }
            }
            if (kartaDeszczuDoZagrnia != null) {
                aktualnyStanRundy.dodajKartePogody(kartaDeszczuDoZagrnia);
                if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": 'Ulewny deszcz' zagrany z talii.", false);
                czyZdolnoscZostalaFaktycznieUzyta = true;
            } else {
                if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Nie znaleziono 'Ulewny deszcz' w Twojej talii.", false);
            }
            // gracz.setZdolnoscDowodcyUzyta(true); // Już ustawione wyżej
        } else if (idDowodcy == 71 || idDowodcy == 73 || idDowodcy == 413) { // Pasywne zdolności lub informacyjne kliknięcie
            czyToAktywnaZdolnoscDoZuzycia = false; // Nie jest to typowa aktywna zdolność do zużycia
            if (kontrolerPlanszy != null) {
                String komunikatPasywny = "";
                if (idDowodcy == 71) komunikatPasywny = ": Umiejętność pasywna (losowe wskrzeszenie z Medykiem).";
                else if (idDowodcy == 73) komunikatPasywny = ": Umiejętność pasywna, blokuje zdolność przeciwnika.";
                else if (idDowodcy == 413) komunikatPasywny = ": Umiejętność pasywna - podwaja siłę Szpiegów.";
                else if (idDowodcy == 489) komunikatPasywny = ": Umiejętność pasywna - jedna dodatkowa karta na początku gry.";
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + komunikatPasywny, false);
                kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1); // Upewnij się, że UI jest aktywne
            }
            return; // Pasywne zdolności nie kończą tury i nie są "używane" w sensie flagi
        } else if (idDowodcy == 72) { // Emhyr "Cesarz Nilfgaardu" (AKTYWNA, NATYCHMIASTOWA)
            Gracz przeciwnik72 = (gracz == gracz1) ? gracz2 : gracz1;
            if (przeciwnik72 == null || przeciwnik72.getReka() == null || przeciwnik72.getReka().isEmpty()) {
                if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Ręka przeciwnika jest pusta.", false);
            } else {
                List<Karta> kartyDoOdkrycia = new ArrayList<>();
                List<Karta> kopiaRekiPrzeciwnika = new ArrayList<>(przeciwnik72.getReka());
                Collections.shuffle(kopiaRekiPrzeciwnika);
                int ileOdkryc = Math.min(3, kopiaRekiPrzeciwnika.size());
                for(int i=0; i<ileOdkryc; i++) kartyDoOdkrycia.add(kopiaRekiPrzeciwnika.get(i));

                if (!kartyDoOdkrycia.isEmpty() && kontrolerPlanszy != null) {
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy(gracz.getProfilUzytkownika().getNazwaUzytkownika() + " (Cesarz) podgląda " + ileOdkryc + " karty.", false);
                    kontrolerPlanszy.pokazTymczasowyPanelZKartami(kartyDoOdkrycia, Duration.seconds(4), "Podejrzane Karty Przeciwnika");
                    czyZdolnoscZostalaFaktycznieUzyta = true;
                }
            }
            // gracz.setZdolnoscDowodcyUzyta(true); // Już ustawione wyżej
        } else if (idDowodcy == 331) { // Foltest "Syn Medella" (AKTYWNA, NATYCHMIASTOWA)
            Gracz przeciwnik331 = (gracz == gracz1) ? gracz2 : gracz1;
            if (przeciwnik331 != null && przeciwnik331.getPlanszaGry() != null) {
                RzadPlanszy rzadStrzelecki = przeciwnik331.getPlanszaGry().getRzadStrzelecki();
                if (rzadStrzelecki != null && rzadStrzelecki.getSumaPunktowWRzedzie() >= 10) {
                    zniszczNajsilniejszeJednostkiNaRzedzie(przeciwnik331, TypRzeduEnum.STRZELECKIE, "Zdolność Foltesta \"Syn Medella\"", null);
                    czyZdolnoscZostalaFaktycznieUzyta = true;
                } else {
                    if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Foltest (SM): Warunek siły rzędu strzeleckiego przeciwnika niespełniony.", false);
                }
            } else { if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd: Brak danych przeciwnika dla Foltesta (SM).", true); }
            // gracz.setZdolnoscDowodcyUzyta(true); // Już ustawione wyżej
        } else if (idDowodcy == 332) { // Foltest "Żelazny Władca" (AKTYWNA, NATYCHMIASTOWA)
            Gracz przeciwnik332 = (gracz == gracz1) ? gracz2 : gracz1;
            if (przeciwnik332 != null && przeciwnik332.getPlanszaGry() != null) {
                RzadPlanszy rzadOblezniczy = przeciwnik332.getPlanszaGry().getRzadOblezenia();
                if (rzadOblezniczy != null && rzadOblezniczy.getSumaPunktowWRzedzie() >= 10) {
                    zniszczNajsilniejszeJednostkiNaRzedzie(przeciwnik332, TypRzeduEnum.OBLEZENIE, "Zdolność Foltesta \"Żelazny Władca\"", null);
                    czyZdolnoscZostalaFaktycznieUzyta = true;
                } else {
                    if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Foltest (ŻW): Warunek siły rzędu oblężniczego przeciwnika niespełniony.", false);
                }
            } else { if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd: Brak danych przeciwnika dla Foltesta (ŻW).", true); }
            // gracz.setZdolnoscDowodcyUzyta(true); // Już ustawione wyżej
        } else if (idDowodcy == 333) { // Foltest "Zdobywca" (AKTYWNA, NATYCHMIASTOWA)
            RzadPlanszy rzadOblezniczyGracza = gracz.getPlanszaGry().getRzadOblezenia();
            if (rzadOblezniczyGracza != null) {
                if (rzadOblezniczyGracza.getKartaRoguDowodcy() == null && !rzadOblezniczyGracza.isWzmocnieniePrzezDowodceAktywne()) {
                    rzadOblezniczyGracza.setKartaRoguDowodcy(kartaDowodcy);
                    czyZdolnoscZostalaFaktycznieUzyta = true;
                    if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Foltest (Zdobywca) wzmacnia Twoje maszyny oblężnicze!", false);
                } else {
                    if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Foltest (Zdobywca): Twój rząd oblężniczy jest już wzmocniony!", false);
                }
            } else {
                if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Foltest (Zdobywca): Nie posiadasz rzędu oblężniczego.", false);
            }
            // gracz.setZdolnoscDowodcyUzyta(true); // Już ustawione wyżej
        } else if (idDowodcy == 334) { // Foltest "Dowódca Północy" (AKTYWNA, NATYCHMIASTOWA)
            if (aktualnyStanRundy != null) {
                aktualnyStanRundy.wyczyscPogode();
                if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Foltest (Dowódca Północy): Czyste niebo!", false);
                czyZdolnoscZostalaFaktycznieUzyta = true;
            } else {
                System.err.println("SILNIK: Błąd - aktualnyStanRundy jest null przy Folteście (DP).");
            }
            // gracz.setZdolnoscDowodcyUzyta(true); // Już ustawione wyżej
        } else if (idDowodcy == 335) { // Foltest "Król Temerii" (AKTYWNA, NATYCHMIASTOWA)
            Karta kartaMglyDoZagrnia = null;
            List<Karta> taliaGracza = gracz.getTaliaDoGry();
            Iterator<Karta> iteratorTalii = taliaGracza.iterator();
            while (iteratorTalii.hasNext()) {
                Karta kartaZTali = iteratorTalii.next();
                if (kartaZTali.getNazwa().trim().equalsIgnoreCase("Gęsta mgła")) {
                    kartaMglyDoZagrnia = kartaZTali;
                    iteratorTalii.remove();
                    break;
                }
            }
            if (kartaMglyDoZagrnia != null) {
                aktualnyStanRundy.dodajKartePogody(kartaMglyDoZagrnia);
                if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": 'Gęsta mgła' zagrana z talii.", false);
                czyZdolnoscZostalaFaktycznieUzyta = true;
            } else {
                if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Nie znaleziono 'Gęsta mgła' w Twojej talii.", false);
            }
            // gracz.setZdolnoscDowodcyUzyta(true); // Już ustawione wyżej
        }else if (idDowodcy == 416) { // Eredin Bréacc Glas "Król Dzikiego Gonu"
            List<Karta> kartyPogodyWTalii = new ArrayList<>();
            for (Karta k : gracz.getTaliaDoGry()) {
                if (k.getTyp() == TypKartyEnum.SPECJALNA) {
                    String nazwaKartyLower = k.getNazwa().toLowerCase();
                    boolean jestKartaPogodyNegatywnej = nazwaKartyLower.contains("mróz") ||
                            nazwaKartyLower.contains("mgła") ||
                            nazwaKartyLower.contains("deszcz");
                    boolean jestKartaPogodyPozytywnej = nazwaKartyLower.contains("czyste niebo") ||
                            nazwaKartyLower.contains("słoneczna pogoda");

                    if (jestKartaPogodyNegatywnej || jestKartaPogodyPozytywnej) {
                        // Dodatkowe wykluczenie innych kart specjalnych, które mogłyby przypadkiem zawierać te słowa
                        if (!nazwaKartyLower.contains("manekin") &&
                                !nazwaKartyLower.contains("róg dowódcy") &&
                                !nazwaKartyLower.contains("pożoga")) { // Dodaj inne, jeśli potrzeba
                            kartyPogodyWTalii.add(k);
                        }
                    }
                }
            }

            if (kartyPogodyWTalii.isEmpty()) {
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Brak kart pogody w Twojej talii!", false);
                }
                gracz.setZdolnoscDowodcyUzyta(true); // Zużyj zdolność
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                // Tura zakończy się w standardowym bloku poniżej
            } else {
                this.oczekiwanieNaWyborKartyPogodyPrzezEredina = true;
                this.graczAktywujacyEredina336 = gracz;
                this.dostepneKartyPogodyDlaEredina = new ArrayList<>(kartyPogodyWTalii);

                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.pokazPanelWyboruKartyPogodyDlaEredina(this.dostepneKartyPogodyDlaEredina);
                }
                // Nie oznaczamy zdolności jako użytej i nie kończymy tury - czekamy na wybór
                return; // Wyjdź z metody, czekając na interakcję gracza
            }
        }else if (idDowodcy == 336) { // Eredin Bréacc Glas "Dowódca Czerwonych Jeźdźców"
            if (gracz.getPlanszaGry() != null) {
                RzadPlanszy rzadPiechotyGracza = gracz.getPlanszaGry().getRzadPiechoty();
                if (rzadPiechotyGracza != null) {
                    if (rzadPiechotyGracza.getKartaRoguDowodcy() == null && !rzadPiechotyGracza.isWzmocnieniePrzezDowodceAktywne()) {
                        // Ustawiamy kartę dowódcy jako "źródło" wzmocnienia dla tego rzędu.
                        // Metoda setKartaRoguDowodcy w RzadPlanszy powinna obsłużyć ustawienie
                        // zarówno karty, jak i flagi wzmocnieniePrzezDowodceAktywne.
                        rzadPiechotyGracza.setKartaRoguDowodcy(kartaDowodcy);
                        czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = true;
                        if (kontrolerPlanszy != null) {
                            kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + " wzmacnia Twoje jednostki bliskiego starcia!", false);
                        }
                    } else {
                        if (kontrolerPlanszy != null) {
                            kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": Twój rząd piechoty jest już wzmocniony!", false);
                        }
                        czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                    }
                } else {
                    if (kontrolerPlanszy != null) {
                        kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": Nie posiadasz rzędu piechoty.", false);
                    }
                    czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                }
            } else {
                System.err.println("SILNIK: Błąd - plansza gracza jest null przy próbie użycia zdolności Eredina 336.");
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd: Brak planszy gracza dla Eredina (336).", true);
                }
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
            }
            gracz.setZdolnoscDowodcyUzyta(true); // Zdolność jest zużywana niezależnie od efektu (jeśli próbowano jej użyć)

        }else if (idDowodcy == 414) { // Eredin Bréacc Glas: Władca Tir ná Lia
            if (gracz.getReka().size() < 2) {
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Potrzebujesz co najmniej 2 kart w ręce, aby użyć tej zdolności!", true);
                }
                gracz.setZdolnoscDowodcyUzyta(true);
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                // Przejdzie do standardowego bloku zakończenia tury
            } else if (gracz.getTaliaDoGry().isEmpty()) {
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Twoja talia jest pusta, nie możesz dobrać karty!", true);
                }
                gracz.setZdolnoscDowodcyUzyta(true);
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                // Przejdzie do standardowego bloku zakończenia tury
            } else {
                this.oczekiwanieNaWyborKartDoOdrzuceniaEredin414 = true;
                this.graczAktywujacyEredina414 = gracz;
                if (this.kartyWybraneDoOdrzuceniaEredin414 != null) {
                    this.kartyWybraneDoOdrzuceniaEredin414.clear();
                } else {
                    this.kartyWybraneDoOdrzuceniaEredin414 = new ArrayList<>();
                }

                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.pokazPanelWyboruKartDoOdrzuceniaEredin414(new ArrayList<>(gracz.getReka()));
                }
                return; // Czekaj na wybór gracza (2 karty do odrzucenia)
            }
        }else if (idDowodcy == 415) { // Eredin Bréacc Glas: Zabójca Auberona
            if (gracz.getOdrzucone() == null || gracz.getOdrzucone().isEmpty()) {
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Twój cmentarz jest pusty!", true);
                }
                gracz.setZdolnoscDowodcyUzyta(true); // Zdolność zużyta mimo braku kart
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                // Przejdzie do standardowego bloku zakończenia tury
            } else {
                this.oczekiwanieNaWyborKartyZCmentarzaPrzezEredina415 = true;
                this.graczAktywujacyEredina415 = gracz;
                if (this.kontrolerPlanszy != null) {
                    // Użyjemy istniejącego panelu cmentarza, ale kontroler musi wiedzieć, że to dla Eredina 415
                    // Można dodać nowy parametr do pokazPanelCmentarza lub nową metodę w kontrolerze
                    this.kontrolerPlanszy.pokazPanelCmentarzaDlaEredina415(gracz, "Eredin: Wybierz kartę z cmentarza");
                }
                return; // Czekaj na wybór gracza
            }
        } else if (idDowodcy == 490) { // Francesca Findabair "Elfka czystej krwi"
            // Nie trzeba sprawdzać gracz.isZdolnoscDowodcyUzyta(), bo to jest robione na początku dla 'czyToAktywnaZdolnoscDoZuzycia'

            Karta trzaskajacyMrozDoZagrnia = null;
            Iterator<Karta> iteratorTalii = gracz.getTaliaDoGry().iterator();
            while (iteratorTalii.hasNext()) {
                Karta kartaZTali = iteratorTalii.next();
                // Sprawdzamy nazwę i typ, aby upewnić się, że to karta specjalna pogody
                if (kartaZTali.getTyp() == TypKartyEnum.SPECJALNA &&
                        (kartaZTali.getNazwa().equalsIgnoreCase("Trzaskający mróz") ||
                                kartaZTali.getNazwa().toLowerCase().contains("mróz"))) { // Dodatkowa elastyczność dla "mróz"
                    trzaskajacyMrozDoZagrnia = kartaZTali;
                    iteratorTalii.remove(); // Usuń z talii
                    break;
                }
            }

            if (trzaskajacyMrozDoZagrnia != null) {
                if (aktualnyStanRundy != null) {
                    aktualnyStanRundy.dodajKartePogody(trzaskajacyMrozDoZagrnia);
                    // Karta pogody typu Mróz nie idzie na cmentarz po zagraniu, zostaje w strefie pogody.
                    System.out.println("SILNIK: Francesca (490) zagrała '" + trzaskajacyMrozDoZagrnia.getNazwa() + "' z talii.");
                    if (this.kontrolerPlanszy != null) {
                        this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": 'Trzaskający mróz' zagrany z talii.", false);
                    }
                    czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = true;
                } else {
                    System.err.println("SILNIK: Błąd krytyczny - aktualnyStanRundy jest null przy zagrywaniu Mrozu przez Francescę 490.");
                    gracz.getTaliaDoGry().add(trzaskajacyMrozDoZagrnia); // Spróbuj zwrócić kartę do talii, jeśli nie można zagrać
                    czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                }
            } else {
                System.out.println("SILNIK: Francesca (490) - nie znaleziono 'Trzaskający mróz' w talii.");
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": Nie znaleziono 'Trzaskający mróz' w Twojej talii.", false);
                }
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
            }
            gracz.setZdolnoscDowodcyUzyta(true); // Zdolność jest zużywana niezależnie od tego, czy karta została znaleziona
            // Tura zakończy się w standardowym bloku poniżej, ponieważ ta zdolność nie robi 'return'
        }else if (idDowodcy == 491) { // Francesca Findabair "Nadzieja Dol Blathanna"
            czyToAktywnaZdolnoscDoZuzycia = true;
            boolean dokonanoJakiegokolwiekPrzesuniecia = false;

            List<Karta> jednostkiZreczneNaPlanszy = new ArrayList<>();
            Map<Karta, RzadPlanszy> mapaKartaNaObecnyRzad = new HashMap<>();

            if (gracz.getPlanszaGry() != null) {
                for (TypRzeduEnum typRzedu : new TypRzeduEnum[]{TypRzeduEnum.PIECHOTA, TypRzeduEnum.STRZELECKIE, TypRzeduEnum.OBLEZENIE}) {
                    RzadPlanszy rzad = gracz.getPlanszaGry().getRzad(typRzedu);
                    if (rzad != null) {
                        for (Karta k : rzad.getKartyJednostekWRzedzie()) {
                            if (k.getTyp() == TypKartyEnum.JEDNOSTKA &&
                                    k.getUmiejetnosc() != null && k.getUmiejetnosc().equalsIgnoreCase("Zręczność")) {
                                jednostkiZreczneNaPlanszy.add(k);
                                mapaKartaNaObecnyRzad.put(k, rzad);
                            }
                        }
                    }
                }
            }

            if (jednostkiZreczneNaPlanszy.isEmpty()) {
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": Brak jednostek ze 'Zręcznością' na Twojej planszy.", false);
                }
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
            } else {
                System.out.println("SILNIK: Francesca (491) analizuje pozycje " + jednostkiZreczneNaPlanszy.size() + " jednostek ze Zręcznością.");
                for (Karta kartaZreczna : jednostkiZreczneNaPlanszy) {
                    RzadPlanszy obecnyRzadObiekt = mapaKartaNaObecnyRzad.get(kartaZreczna);
                    if (obecnyRzadObiekt == null) continue;

                    TypRzeduEnum obecnyTypRzedu = obecnyRzadObiekt.getTypRzedu();
                    int obecnaSilaKarty = obliczPotencjalnaSileKartyWRzedzie(kartaZreczna, obecnyTypRzedu, gracz);

                    TypRzeduEnum najlepszyRzadDocelowyDlaTejKarty = obecnyTypRzedu;
                    int maksymalnaPotencjalnaSilaDlaTejKarty = obecnaSilaKarty;

                    List<TypRzeduEnum> dozwoloneRzedyDlaKarty = new ArrayList<>();
                    String poz1 = kartaZreczna.getPozycja().toLowerCase();
                    String poz2 = (kartaZreczna.getPozycja2() != null && !kartaZreczna.getPozycja2().equalsIgnoreCase("N/D")) ? kartaZreczna.getPozycja2().toLowerCase() : "";

                    if (poz1.contains("dowolne") || poz2.contains("dowolne")) {
                        dozwoloneRzedyDlaKarty.add(TypRzeduEnum.PIECHOTA);
                        dozwoloneRzedyDlaKarty.add(TypRzeduEnum.STRZELECKIE);
                        dozwoloneRzedyDlaKarty.add(TypRzeduEnum.OBLEZENIE);
                    } else {
                        TypRzeduEnum typRzeduPoz1 = TypRzeduEnum.fromStringPozycjiKarty(kartaZreczna.getPozycja());
                        if (typRzeduPoz1 != null) dozwoloneRzedyDlaKarty.add(typRzeduPoz1);
                        if (!poz2.isEmpty()) {
                            TypRzeduEnum typRzeduPoz2 = TypRzeduEnum.fromStringPozycjiKarty(kartaZreczna.getPozycja2());
                            if (typRzeduPoz2 != null && !dozwoloneRzedyDlaKarty.contains(typRzeduPoz2)) {
                                dozwoloneRzedyDlaKarty.add(typRzeduPoz2);
                            }
                        }
                    }
                    dozwoloneRzedyDlaKarty = dozwoloneRzedyDlaKarty.stream().distinct().collect(Collectors.toList());

                    for (TypRzeduEnum potencjalnyTypRzedu : dozwoloneRzedyDlaKarty) {
                        if (potencjalnyTypRzedu == obecnyTypRzedu) continue;

                        int silaWPotencjalnymRzedzie = obliczPotencjalnaSileKartyWRzedzie(kartaZreczna, potencjalnyTypRzedu, gracz);
                        if (silaWPotencjalnymRzedzie > maksymalnaPotencjalnaSilaDlaTejKarty) {
                            maksymalnaPotencjalnaSilaDlaTejKarty = silaWPotencjalnymRzedzie;
                            najlepszyRzadDocelowyDlaTejKarty = potencjalnyTypRzedu;
                        }
                    }

                    if (najlepszyRzadDocelowyDlaTejKarty != obecnyTypRzedu) {
                        RzadPlanszy nowyRzadObiekt = gracz.getPlanszaGry().getRzad(najlepszyRzadDocelowyDlaTejKarty);
                        if (nowyRzadObiekt != null) {
                            System.out.println("  > Francesca (491) przesuwa '" + kartaZreczna.getNazwa() +
                                    "' z " + obecnyTypRzedu + " (siła: " + obecnaSilaKarty +
                                    ") do " + najlepszyRzadDocelowyDlaTejKarty + " (potencjalna siła: " + maksymalnaPotencjalnaSilaDlaTejKarty + ")");

                            // **** POPRAWKA TUTAJ ****
                            if (obecnyRzadObiekt.usunKarteJednostki(kartaZreczna) != null) { // Sprawdź, czy usunięcie się powiodło (zwrócona karta nie jest null)
                                nowyRzadObiekt.dodajKarteJednostki(kartaZreczna);
                                dokonanoJakiegokolwiekPrzesuniecia = true;
                            } else {
                                System.err.println("  > Francesca (491): Nie udało się usunąć karty " + kartaZreczna.getNazwa() + " z rzędu " + obecnyTypRzedu);
                            }
                        }
                    }
                }

                if (dokonanoJakiegokolwiekPrzesuniecia) {
                    czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = true;
                    if (this.kontrolerPlanszy != null) {
                        this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": Zoptymalizowano pozycje jednostek ze 'Zręcznością'.", false);
                    }
                } else if (!jednostkiZreczneNaPlanszy.isEmpty()){
                    if (this.kontrolerPlanszy != null) {
                        this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": Jednostki ze 'Zręcznością' są już w optymalnych pozycjach.", false);
                    }
                    czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                }
            }
            gracz.setZdolnoscDowodcyUzyta(true);
            // ... reszta bloku if/else if dla innych dowódców ...
        }else if (idDowodcy == 492) { // Francesca Findabair "Królowa Dol Blathanna"
            czyToAktywnaZdolnoscDoZuzycia = true; // To jest aktywna zdolność

            Gracz przeciwnik = (gracz == gracz1) ? gracz2 : gracz1;
            RzadPlanszy rzadBliskiegoStarciaPrzeciwnika = null;
            int sumaPunktowRzeduPrzeciwnika = 0;

            if (przeciwnik != null && przeciwnik.getPlanszaGry() != null) {
                rzadBliskiegoStarciaPrzeciwnika = przeciwnik.getPlanszaGry().getRzadPiechoty();
                if (rzadBliskiegoStarciaPrzeciwnika != null) {
                    // getSumaPunktowWRzedzie() zwraca aktualną, efektywną siłę rzędu
                    sumaPunktowRzeduPrzeciwnika = rzadBliskiegoStarciaPrzeciwnika.getSumaPunktowWRzedzie();
                }
            }

            if (rzadBliskiegoStarciaPrzeciwnika != null && sumaPunktowRzeduPrzeciwnika >= 10) {
                System.out.println("SILNIK: Francesca (492) - Rząd bliskiego starcia przeciwnika ma " + sumaPunktowRzeduPrzeciwnika + " pkt (>=10). Aktywacja efektu.");
                // Wywołaj zmodyfikowaną metodę niszczącą
                boolean czyCosZniszczono = zniszczNajsilniejszeJednostkiNaRzedzie(przeciwnik, TypRzeduEnum.PIECHOTA, "Zdolność Franceski (492)", null);
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = czyCosZniszczono; // Ustaw flagę na podstawie wyniku
            } else {
                String przyczynaBrakuEfektu = "nieznana";
                if (rzadBliskiegoStarciaPrzeciwnika == null) {
                    przyczynaBrakuEfektu = "przeciwnik nie ma rzędu bliskiego starcia.";
                } else { // sumaPunktowRzeduPrzeciwnika < 10
                    przyczynaBrakuEfektu = "suma siły rzędu bliskiego starcia przeciwnika (" + sumaPunktowRzeduPrzeciwnika + " pkt) jest mniejsza niż 10.";
                }
                System.out.println("SILNIK: Francesca (492) - Warunek nie spełniony: " + przyczynaBrakuEfektu);
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": " + przyczynaBrakuEfektu, false);
                }
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
            }
            gracz.setZdolnoscDowodcyUzyta(true); // Zdolność jest zużywana niezależnie od efektu
            // Tura zakończy się w standardowym bloku poniżej, bo ta zdolność nie robi 'return'
        }else if (idDowodcy == 493) { // Francesca Findabair "Najpiękniejsza kobieta na świecie"
            czyToAktywnaZdolnoscDoZuzycia = true;

            if (gracz.getPlanszaGry() != null) {
                RzadPlanszy rzadStrzeleckiGracza = gracz.getPlanszaGry().getRzadStrzelecki();
                if (rzadStrzeleckiGracza != null) {
                    if (rzadStrzeleckiGracza.getKartaRoguDowodcy() == null && !rzadStrzeleckiGracza.isWzmocnieniePrzezDowodceAktywne()) {
                        rzadStrzeleckiGracza.setKartaRoguDowodcy(kartaDowodcy);
                        czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = true;
                        if (this.kontrolerPlanszy != null) {
                            this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + " wzmacnia Twoje jednostki dalekiego zasięgu!", false);
                        }
                    } else {
                        if (this.kontrolerPlanszy != null) {
                            this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": Twój rząd dalekiego zasięgu jest już wzmocniony!", false);
                        }
                        czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                    }
                } else {
                    if (this.kontrolerPlanszy != null) {
                        this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": Nie posiadasz rzędu dalekiego zasięgu.", false);
                    }
                    czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                }
            } else {
                System.err.println("SILNIK: Błąd - plansza gracza jest null przy próbie użycia zdolności Francesci 493.");
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd: Brak planszy gracza dla Francesci (493).", true);
                }
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
            }
            gracz.setZdolnoscDowodcyUzyta(true);
            // Tura zakończy się w standardowym bloku poniżej
        }
        // TODO: Inne AKTYWNE zdolności dowódców

        // Wspólne zakończenie
        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            // Komunikat o użyciu zdolności
            if (czyToAktywnaZdolnoscDoZuzycia && // Tylko jeśli to była próba użycia aktywnej zdolności
                    gracz.isZdolnoscDowodcyUzyta() && // I została oznaczona jako użyta
                    !oczekiwanieNaWyborKartyDlaEmhyra &&
                    !oczekiwanieNaWyborCeluDlaManekina &&
                    !oczekiwanieNaWyborRzeduPoWskrzeszeniu) {
                // Dodatkowy warunek, aby nie wyświetlać komunikatu dla zdolności, które nie miały efektu, a tylko zużyły użycie
                if (czyZdolnoscZostalaFaktycznieUzyta || idDowodcy == 72) { // Dla Cesarza zawsze pokazuj, że użył, nawet jak ręka pusta
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy(gracz.getProfilUzytkownika().getNazwaUzytkownika() + " użył zdolności: " + kartaDowodcy.getNazwa() + "!", false);
                } else if (!czyZdolnoscZostalaFaktycznieUzyta && idDowodcy != 69) { // Dla ID69 komunikat jest w metodach wykonaj/anuluj
                    // Można dodać komunikat "Zdolność użyta, ale bez efektu", jeśli chcesz
                }
            }
        }

        // ZAKOŃCZENIE TURY - tylko dla AKTYWNYCH zdolności, które nie weszły w stan oczekiwania
        if (czyToAktywnaZdolnoscDoZuzycia &&
                !oczekiwanieNaWyborKartyDlaEmhyra &&
                !oczekiwanieNaWyborCeluDlaManekina &&
                !oczekiwanieNaWyborRzeduPoWskrzeszeniu) {

            System.out.println("SILNIK: Użycie aktywnej zdolności dowódcy (" + kartaDowodcy.getNazwa() + ") kończy turę.");
            oczekiwanieNaPotwierdzenieTury = true;
            Gracz nastepnyPrzeciwnik = (gracz == this.gracz1) ? this.gracz2 : this.gracz1;
            setGraczOczekujacyNaPotwierdzenie(nastepnyPrzeciwnik);
            if (kontrolerPlanszy != null) kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
        // Jeśli to było kliknięcie na pasywną zdolność (ID 71, 73), metoda zwróciła wcześniej.
        // Jeśli zdolność aktywna weszła w stan oczekiwania (ID 69), metoda też zwróciła wcześniej.
    }

    private int obliczPotencjalnaSileKartyWRzedzie(Karta kartaDoSprawdzenia, TypRzeduEnum typRzeduDocelowego, Gracz wlascicielKarty) {
        if (kartaDoSprawdzenia.getTyp() == TypKartyEnum.BOHATER) { // Bohaterowie mają stałą siłę
            int silaBohatera = kartaDoSprawdzenia.getPunktySily();
            boolean czySzpieg = kartaDoSprawdzenia.getUmiejetnosc() != null && kartaDoSprawdzenia.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
            if (czySzpieg && aktualnyStanRundy.isEredin413Active()) {
                silaBohatera *= 2;
            }
            return silaBohatera;
        }

        int efektywnaSila = kartaDoSprawdzenia.getPunktySily();

        // 1. Efekt Eredina 413 dla Szpiegów (na bazową siłę)
        boolean czySzpiegJednostka = kartaDoSprawdzenia.getUmiejetnosc() != null && kartaDoSprawdzenia.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
        if (czySzpiegJednostka && aktualnyStanRundy.isEredin413Active()) {
            efektywnaSila *= 2;
        }

        // Pobierz docelowy rząd z planszy właściciela
        // Zakładamy, że jednostki ze Zręcznością są przesuwane w ramach planszy ich właściciela
        PlanszaGracza planszaWlasciciela = getPlanszaGracza(wlascicielKarty);
        if (planszaWlasciciela == null) return efektywnaSila; // Błąd, zwróć bazową

        RzadPlanszy rzadDocelowyFaktyczny = planszaWlasciciela.getRzad(typRzeduDocelowego);
        if (rzadDocelowyFaktyczny == null) return efektywnaSila; // Błąd

        // 2. Efekt Pogody na tym rzędzie
        boolean pogodaAktywnaDlaKarty = false;
        if (aktualnyStanRundy != null) {
            for (Karta kartaPogody : aktualnyStanRundy.getAktywneKartyWRzedziePogody()) {
                String nazwaPogody = kartaPogody.getNazwa().toLowerCase();
                if ((typRzeduDocelowego == TypRzeduEnum.PIECHOTA && nazwaPogody.contains("mróz")) ||
                        (typRzeduDocelowego == TypRzeduEnum.STRZELECKIE && nazwaPogody.contains("mgła")) ||
                        (typRzeduDocelowego == TypRzeduEnum.OBLEZENIE && nazwaPogody.contains("deszcz"))) {
                    pogodaAktywnaDlaKarty = true;
                    break;
                }
            }
        }
        if (pogodaAktywnaDlaKarty) {
            efektywnaSila = 1; // Pogoda redukuje siłę jednostki nie-Bohatera do 1
        }

        // 3. Efekt Wysokich Morali od INNYCH jednostek w rzędzie docelowym
        // Ten bonus jest dodawany PO efekcie pogody
        int bonusMorale = 0;
        for (Karta kInRow : rzadDocelowyFaktyczny.getKartyJednostekWRzedzie()) {
            // Zakładamy, że 'kartaDoSprawdzenia' nie jest jeszcze w tym rzędzie,
            // więc nie musimy jej wykluczać z 'kInRow' jeśli chodzi o dawanie morale.
            // Jeśli 'kartaDoSprawdzenia' sama daje morale, to nie wpływa na jej własną siłę w tym kroku.
            if (kInRow.getUmiejetnosc() != null && kInRow.getUmiejetnosc().equalsIgnoreCase("Wysokie morale") &&
                    kInRow.getTyp() != TypKartyEnum.BOHATER) { // Zazwyczaj jednostki dają morale
                bonusMorale++;
            }
        }
        if (!pogodaAktywnaDlaKarty) { // Jeśli pogoda ustawiła siłę na 1, morale się do tego nie dodaje w taki sposób.
            // Morale zwiększa siłę, która jest > 1. Jeśli siła jest 1 przez pogodę, morale nie ma efektu.
            // Typowa interpretacja: pogoda > morale > róg.
            // Alternatywnie: morale dodaje do siły 1 (po pogodzie). Na razie załóżmy, że pogoda jest silniejsza.
            // Aby morale działało na siłę 1 po pogodzie, ten if musiałby być usunięty.
            // Na razie zostawiam tak, że morale nie działa na jednostki osłabione pogodą do 1.
            // Dla uproszczenia, jeśli jest pogoda, siła jest 1, a potem róg.
            // Jeśli NIE MA pogody, wtedy liczymy morale:
            efektywnaSila += bonusMorale;
        }


        // 4. Efekt Rogu Dowódcy na tym rzędzie (działa na siłę PO pogodzie i morale)
        if (rzadDocelowyFaktyczny.getKartaRoguDowodcy() != null || rzadDocelowyFaktyczny.isWzmocnieniePrzezDowodceAktywne()) {
            efektywnaSila *= 2;
        }

        // Umiejętność Więź nie jest tutaj bezpośrednio obliczana dla 'kartaDoSprawdzenia',
        // ponieważ jej aktywacja zależałaby od tego, czy inne pasujące karty już są w rzędzie docelowym.
        // Dla tej umiejętności skupiamy się na optymalizacji pod kątem pogody i rogu.

        return efektywnaSila;
    }

    public void potwierdzWyborKartDoOdrzuceniaEredin414(List<Karta> wybraneKartyDoOdrzucenia) {
        if (!oczekiwanieNaWyborKartDoOdrzuceniaEredin414 || graczAktywujacyEredina414 == null) {
            System.err.println("SILNIK: Nieprawidłowy stan do potwierdzenia odrzucenia kart dla Eredina 414.");
            anulujZdolnoscEredina414(true); // true = błąd
            return;
        }
        if (wybraneKartyDoOdrzucenia == null || wybraneKartyDoOdrzucenia.size() != 2) {
            System.err.println("SILNIK: Eredin 414 - Należy wybrać dokładnie 2 karty do odrzucenia.");
            if (this.kontrolerPlanszy != null) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Wybierz dokładnie 2 karty do odrzucenia.", true);
                // Pozwól graczowi poprawić wybór, ponownie pokazując panel
                this.kontrolerPlanszy.pokazPanelWyboruKartDoOdrzuceniaEredin414(new ArrayList<>(graczAktywujacyEredina414.getReka()));
            }
            return; // Nie anuluj zdolności, daj szansę na ponowny wybór
        }

        this.kartyWybraneDoOdrzuceniaEredin414.clear(); // Wyczyść na wszelki wypadek
        this.kartyWybraneDoOdrzuceniaEredin414.addAll(wybraneKartyDoOdrzucenia);
        this.oczekiwanieNaWyborKartDoOdrzuceniaEredin414 = false;
        this.oczekiwanieNaWyborKartyZTaliiPrzezEredin414 = true;

        System.out.println("SILNIK: Eredin 414 - Wybrano karty do odrzucenia: " +
                wybraneKartyDoOdrzucenia.stream().map(Karta::getNazwa).collect(Collectors.joining(", ")));

        if (graczAktywujacyEredina414.getTaliaDoGry().isEmpty()) {
            if (this.kontrolerPlanszy != null) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Talia jest pusta! Nie można dobrać karty.", true);
            }
            // Karty zostały wybrane do odrzucenia, więc muszą zostać odrzucone.
            for (Karta kartaDoOdrzucenia : this.kartyWybraneDoOdrzuceniaEredin414) {
                if(graczAktywujacyEredina414.getReka().remove(kartaDoOdrzucenia)){
                    graczAktywujacyEredina414.getOdrzucone().add(kartaDoOdrzucenia);
                }
            }
            System.out.println("SILNIK: Eredin 414 - Odrzucono " + this.kartyWybraneDoOdrzuceniaEredin414.size() + " kart. Talia pusta, brak doboru.");
            graczAktywujacyEredina414.setZdolnoscDowodcyUzyta(true);
            koniecTuryPoAktywnejZdolnosci(graczAktywujacyEredina414, true);
            resetStanuEredina414();
            return;
        }

        if (this.kontrolerPlanszy != null) {
            this.kontrolerPlanszy.pokazPanelWyboruKartyZTalliEredin414(new ArrayList<>(graczAktywujacyEredina414.getTaliaDoGry()));
        }
    }

    public void wykonajWyborKartyZCmentarzaEredin415(Karta wybranaKartaZCmentarza) {
        if (!oczekiwanieNaWyborKartyZCmentarzaPrzezEredina415 || graczAktywujacyEredina415 == null || wybranaKartaZCmentarza == null) {
            System.err.println("SILNIK: Nieprawidłowy stan do wykonania wyboru karty z cmentarza dla Eredina 415.");
            anulujZdolnoscEredina415(true); // true = błąd
            return;
        }

        Gracz aktywnyGracz = graczAktywujacyEredina415;
        System.out.println("SILNIK: Eredin 415 (" + aktywnyGracz.getProfilUzytkownika().getNazwaUzytkownika() +
                ") wybrał z cmentarza: " + wybranaKartaZCmentarza.getNazwa());

        if (aktywnyGracz.getOdrzucone().remove(wybranaKartaZCmentarza)) {
            aktywnyGracz.getReka().add(wybranaKartaZCmentarza);
            System.out.println("SILNIK: Eredin 415 - Karta '" + wybranaKartaZCmentarza.getNazwa() + "' dodana do ręki.");
            if (this.kontrolerPlanszy != null) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Karta '" + wybranaKartaZCmentarza.getNazwa() + "' wzięta na rękę.", false);
            }
        } else {
            System.err.println("SILNIK: Eredin 415 - Nie udało się usunąć karty '" + wybranaKartaZCmentarza.getNazwa() + "' z cmentarza.");
            // Jeśli nie udało się usunąć, zdolność i tak jest zużywana, ale bez efektu dobrania
            if (this.kontrolerPlanszy != null) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd przy pobieraniu karty z cmentarza.", true);
            }
        }

        aktywnyGracz.setZdolnoscDowodcyUzyta(true);
        resetStanuEredina415();

        // Finalizacja tury
        if (this.kontrolerPlanszy != null) { // Odśwież rękę gracza
            this.kontrolerPlanszy.odswiezCalakolwiekPlansze();
        }
        koniecTuryPoAktywnejZdolnosci(aktywnyGracz, true); // true = zdolność miała efekt
    }

    public void anulujZdolnoscEredina415(boolean zPowoduBledu) {
        if (graczAktywujacyEredina415 == null && !oczekiwanieNaWyborKartyZCmentarzaPrzezEredina415) {
            resetStanuEredina415();
            return;
        }

        Gracz aktywnyGracz = graczAktywujacyEredina415;
        if (aktywnyGracz == null) aktywnyGracz = graczAktualnejTury; // Fallback
        if(aktywnyGracz == null) {
            System.err.println("SILNIK: Nie można anulować zdolności Eredina 415, brak informacji o graczu.");
            resetStanuEredina415();
            return;
        }


        System.out.println("SILNIK: Anulowano zdolność Eredina 415 dla gracza " + aktywnyGracz.getProfilUzytkownika().getNazwaUzytkownika() +
                (zPowoduBledu ? " z powodu błędu." : "."));

        aktywnyGracz.setZdolnoscDowodcyUzyta(true); // Zdolność jest zużywana
        resetStanuEredina415();

        if (this.kontrolerPlanszy != null) {
            this.kontrolerPlanszy.odswiezCalakolwiekPlansze();
            String komunikat = aktywnyGracz.getKartaDowodcy() != null ? aktywnyGracz.getKartaDowodcy().getNazwa() : "Zdolność dowódcy";
            this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(komunikat +
                    (zPowoduBledu ? ": wystąpił błąd." : ": wybór anulowany."), false);
        }
        koniecTuryPoAktywnejZdolnosci(aktywnyGracz, false); // false = zdolność nie miała pełnego efektu
    }

    private List<Karta> znajdzPoprawneCeleDlaManekina(Gracz gracz) {
        List<Karta> cele = new ArrayList<>();
        if (gracz == null || gracz.getPlanszaGry() == null) {
            return cele;
        }
        // Manekin zamienia jednostki na WŁASNEJ planszy gracza
        PlanszaGracza planszaGracza = getPlanszaGracza(gracz); // Użyj istniejącej metody getPlanszaGracza
        if (planszaGracza == null) return cele;

        for (TypRzeduEnum typRzedu : new TypRzeduEnum[]{TypRzeduEnum.PIECHOTA, TypRzeduEnum.STRZELECKIE, TypRzeduEnum.OBLEZENIE}) {
            RzadPlanszy rzad = planszaGracza.getRzad(typRzedu);
            if (rzad != null) {
                for (Karta k : rzad.getKartyJednostekWRzedzie()) {
                    // Manekin nie może zamienić Bohatera ani innego Manekina (jeśli karta manekina ma specyficzne ID)
                    // Załóżmy, że Manekin nie może podmienić samego siebie, jeśli np. jakimś cudem byłaby to ta sama instancja.
                    // Głównie chodzi o to, by nie podmieniać Bohaterów.
                    if (k.getTyp() != TypKartyEnum.BOHATER) {
                        // Dodatkowe sprawdzenie, czy karta 'k' to nie jest przypadkiem sam Manekin (jeśli Manekiny są unikalne lub mają ID)
                        // Jeśli Manekin do Ćwiczeń ma stałe ID, np. MANEKIN_ID, można dodać: && k.getId() != MANEKIN_ID
                        cele.add(k);
                    }
                }
            }
        }
        return cele;
    }

    public void wykonajWyborKartyZTalliEredin414(Karta wybranaKartaZTalii) {
        if (!oczekiwanieNaWyborKartyZTaliiPrzezEredin414 || graczAktywujacyEredina414 == null || wybranaKartaZTalii == null || kartyWybraneDoOdrzuceniaEredin414 == null || kartyWybraneDoOdrzuceniaEredin414.size() != 2) {
            System.err.println("SILNIK: Nieprawidłowy stan do wykonania wyboru karty z talii dla Eredina 414.");
            anulujZdolnoscEredina414(true); // true = błąd
            return;
        }

        Gracz aktywnyGracz = graczAktywujacyEredina414;
        System.out.println("SILNIK: Eredin 414 - Wybrano kartę z talii: " + wybranaKartaZTalii.getNazwa());

        for (Karta kartaDoOdrzucenia : this.kartyWybraneDoOdrzuceniaEredin414) {
            if (aktywnyGracz.getReka().remove(kartaDoOdrzucenia)) {
                aktywnyGracz.getOdrzucone().add(kartaDoOdrzucenia);
            } else {
                System.err.println("SILNIK: Eredin 414 - Nie udało się usunąć karty '" + kartaDoOdrzucenia.getNazwa() + "' z ręki podczas odrzucania.");
            }
        }
        System.out.println("SILNIK: Eredin 414 - Odrzucono na cmentarz: " +
                this.kartyWybraneDoOdrzuceniaEredin414.stream().map(Karta::getNazwa).collect(Collectors.joining(", ")));

        if (aktywnyGracz.getTaliaDoGry().remove(wybranaKartaZTalii)) {
            aktywnyGracz.getReka().add(wybranaKartaZTalii);
            System.out.println("SILNIK: Eredin 414 - Dobrano do ręki: " + wybranaKartaZTalii.getNazwa());
        } else {
            System.err.println("SILNIK: Eredin 414 - Nie udało się usunąć wybranej karty '" + wybranaKartaZTalii.getNazwa() + "' z talii.");
        }

        aktywnyGracz.setZdolnoscDowodcyUzyta(true);
        resetStanuEredina414();

        przeliczWszystkiePunktyNaPlanszy();
        if (this.kontrolerPlanszy != null) {
            this.kontrolerPlanszy.odswiezCalakolwiekPlansze();
            this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(aktywnyGracz.getProfilUzytkownika().getNazwaUzytkownika() +
                    " użył zdolności Eredina: odrzucił 2 karty i dobrał \"" + wybranaKartaZTalii.getNazwa() + "\" z talii!", false);
        }
        koniecTuryPoAktywnejZdolnosci(aktywnyGracz, true);
    }

    public void anulujZdolnoscEredina414(boolean zPowoduBledu) {
        if (graczAktywujacyEredina414 == null && !oczekiwanieNaWyborKartDoOdrzuceniaEredin414 && !oczekiwanieNaWyborKartyZTaliiPrzezEredin414) {
            // Zdolność nie została nawet zainicjowana, lub stan został już zresetowany
            resetStanuEredina414(); // Na wszelki wypadek
            return;
        }

        Gracz aktywnyGracz = graczAktywujacyEredina414; // Może być null, jeśli anulowanie zanim gracz został ustawiony
        if (aktywnyGracz == null) { // Jeśli anulujemy zanim graczAktywujacyEredina414 został ustawiony
            aktywnyGracz = graczAktualnejTury; // Użyj gracza aktualnej tury jako fallback
            if(aktywnyGracz == null) { // Ostateczny fallback
                System.err.println("SILNIK: Nie można anulować zdolności Eredina 414, brak informacji o graczu.");
                resetStanuEredina414();
                return;
            }
        }

        System.out.println("SILNIK: Anulowano zdolność Eredina 414 dla gracza " + aktywnyGracz.getProfilUzytkownika().getNazwaUzytkownika() +
                (zPowoduBledu ? " z powodu błędu." : "."));

        aktywnyGracz.setZdolnoscDowodcyUzyta(true);
        resetStanuEredina414();

        if (this.kontrolerPlanszy != null) {
            this.kontrolerPlanszy.odswiezCalakolwiekPlansze();
            String komunikat = aktywnyGracz.getKartaDowodcy() != null ? aktywnyGracz.getKartaDowodcy().getNazwa() : "Zdolność dowódcy";
            this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(komunikat +
                    (zPowoduBledu ? ": wystąpił błąd." : ": wybór anulowany."), false);
        }
        koniecTuryPoAktywnejZdolnosci(aktywnyGracz, false);
    }

    // Metoda pomocnicza do zakończenia tury po zdolności aktywnej (może już istnieć)
    private void koniecTuryPoAktywnejZdolnosci(Gracz graczKtoryUzywal, boolean czyBylEfekt) {
        System.out.println("SILNIK: Zdolność gracza " + graczKtoryUzywal.getProfilUzytkownika().getNazwaUzytkownika() + " zakończona (efekt: " + czyBylEfekt + "), przekazywanie tury.");
        oczekiwanieNaPotwierdzenieTury = true;
        Gracz nastepnyPrzeciwnik = (graczKtoryUzywal == this.gracz1) ? this.gracz2 : this.gracz1;

        if (nastepnyPrzeciwnik.isCzySpasowalWRundzie()) {
            graczAktualnejTury = graczKtoryUzywal;
            oczekiwanieNaPotwierdzenieTury = false;
            setGraczOczekujacyNaPotwierdzenie(null);
            if (this.kontrolerPlanszy != null) this.kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        } else {
            setGraczOczekujacyNaPotwierdzenie(nastepnyPrzeciwnik);
            if (this.kontrolerPlanszy != null) this.kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }

    public void wykonajWyborPogodyPrzezEredina(Karta wybranaKartaPogody) {
        if (!oczekiwanieNaWyborKartyPogodyPrzezEredina || graczAktywujacyEredina336 == null || wybranaKartaPogody == null) {
            System.err.println("SILNIK: Nieprawidłowy stan do wykonania wyboru pogody przez Eredina.");
            // Jeśli coś poszło nie tak, a zdolność była w trakcie aktywacji, zużyj ją i zakończ turę
            if (graczAktywujacyEredina336 != null) graczAktywujacyEredina336.setZdolnoscDowodcyUzyta(true);
            anulujWyborPogodyPrzezEredina(true); // true oznacza, że anulowanie jest z powodu błędu
            return;
        }

        if (dostepneKartyPogodyDlaEredina == null || !dostepneKartyPogodyDlaEredina.contains(wybranaKartaPogody)) {
            System.err.println("SILNIK: Wybrana karta pogody (" + wybranaKartaPogody.getNazwa() + ") nie była na liście dostępnych opcji dla Eredina.");
            anulujWyborPogodyPrzezEredina(true);
            return;
        }

        Gracz graczKtoryUzywalZdolnosci = graczAktywujacyEredina336; // Zapamiętaj przed resetem
        System.out.println("SILNIK: Eredin (" + graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika() +
                ") wybrał kartę pogody: " + wybranaKartaPogody.getNazwa());

        // 1. Usuń kartę z talii
        boolean usunietoZTalli = graczKtoryUzywalZdolnosci.getTaliaDoGry().remove(wybranaKartaPogody);
        if (!usunietoZTalli) {
            System.err.println("SILNIK: Błąd krytyczny - nie udało się usunąć wybranej karty pogody (" + wybranaKartaPogody.getNazwa() + ") z talii gracza " + graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika());
            // Mimo błędu, spróbujmy kontynuować, ale zdolność będzie zużyta
        }

        // 2. Zagraj kartę pogody
        aktualnyStanRundy.dodajKartePogody(wybranaKartaPogody);
        // Karty pogody nie idą na cmentarz od razu, chyba że to "Czyste niebo"
        // (obsługiwane wewnątrz dodajKartePogody)

        graczKtoryUzywalZdolnosci.setZdolnoscDowodcyUzyta(true);
        System.out.println("SILNIK: Zdolność Eredina (336) użyta. Karta " + wybranaKartaPogody.getNazwa() + " zagrana.");

        resetStanuWyboruPogodyEredina();

        // Finalizacja UI i tury
        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.wyswietlKomunikatNaPlanszy(graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika() + " użył zdolności: " + graczKtoryUzywalZdolnosci.getKartaDowodcy().getNazwa() + ", zagrywając " + wybranaKartaPogody.getNazwa() + "!", false);
        }

        oczekiwanieNaPotwierdzenieTury = true;
        Gracz przeciwnikPoEredinie = (graczKtoryUzywalZdolnosci == this.gracz1) ? this.gracz2 : this.gracz1;

        if (przeciwnikPoEredinie.isCzySpasowalWRundzie()) {
            graczAktualnejTury = graczKtoryUzywalZdolnosci;
            oczekiwanieNaPotwierdzenieTury = false;
            setGraczOczekujacyNaPotwierdzenie(null);
            if (kontrolerPlanszy != null) kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        } else {
            setGraczOczekujacyNaPotwierdzenie(przeciwnikPoEredinie);
            if (kontrolerPlanszy != null) kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }

    public void anulujWyborPogodyPrzezEredina(boolean zPowoduBledu) {
        if (!oczekiwanieNaWyborKartyPogodyPrzezEredina || graczAktywujacyEredina336 == null) {
            resetStanuWyboruPogodyEredina();
            return;
        }

        Gracz graczKtoryUzywalZdolnosci = graczAktywujacyEredina336; // Zapamiętaj przed resetem
        System.out.println("SILNIK: Anulowano wybór karty pogody dla Eredina (" + graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika() + ")" + (zPowoduBledu ? " z powodu błędu." : "."));

        graczKtoryUzywalZdolnosci.setZdolnoscDowodcyUzyta(true); // Zdolność zużyta
        resetStanuWyboruPogodyEredina();

        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze(); // Odśwież, aby pokazać zużytą zdolność
            kontrolerPlanszy.wyswietlKomunikatNaPlanszy(graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika() + " użył zdolności: " + graczKtoryUzywalZdolnosci.getKartaDowodcy().getNazwa() + (zPowoduBledu ? " (Błąd)" : " (Wybór anulowany)."), false);
        }

        oczekiwanieNaPotwierdzenieTury = true;
        Gracz przeciwnikPoAnulowaniu = (graczKtoryUzywalZdolnosci == this.gracz1) ? this.gracz2 : this.gracz1;

        if (przeciwnikPoAnulowaniu.isCzySpasowalWRundzie()) {
            graczAktualnejTury = graczKtoryUzywalZdolnosci;
            oczekiwanieNaPotwierdzenieTury = false;
            setGraczOczekujacyNaPotwierdzenie(null);
            if (kontrolerPlanszy != null) kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        } else {
            setGraczOczekujacyNaPotwierdzenie(przeciwnikPoAnulowaniu);
            if (kontrolerPlanszy != null) kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }

    private void resetStanuWyboruPogodyEredina() {
        this.oczekiwanieNaWyborKartyPogodyPrzezEredina = false;
        this.graczAktywujacyEredina336 = null;
        this.dostepneKartyPogodyDlaEredina = null;
    }

    private boolean zniszczNajsilniejszeJednostkiNaRzedzie(Gracz graczCel, TypRzeduEnum typRzeduCelu, String zrodloEfektu, Karta pominKarte) {
        if (graczCel == null || graczCel.getPlanszaGry() == null) {
            System.err.println("SILNIK: (" + zrodloEfektu + ") - Brak gracza celu lub jego planszy.");
            return false; // Nic nie zniszczono
        }
        RzadPlanszy rzadCelu = graczCel.getPlanszaGry().getRzad(typRzeduCelu);

        if (rzadCelu == null || rzadCelu.getKartyJednostekWRzedzie().isEmpty()) {
            System.out.println("SILNIK: (" + zrodloEfektu + ") - Rząd " + typRzeduCelu.getNazwaWyswietlana() + " gracza " + graczCel.getProfilUzytkownika().getNazwaUzytkownika() + " jest pusty.");
            // Komunikat dla gracza, jeśli to nie Foltest (Foltest ma swoje komunikaty)
            if (this.kontrolerPlanszy != null && zrodloEfektu != null && !zrodloEfektu.toLowerCase().contains("foltest")) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(zrodloEfektu + ": Rząd " + typRzeduCelu.getNazwaWyswietlana() + " przeciwnika jest pusty.", false);
            }
            return false;
        }

        List<Karta> jednostkiNieBohaterowieNaRzedzie = new ArrayList<>();
        for (Karta k : rzadCelu.getKartyJednostekWRzedzie()) {
            if (k.getTyp() != TypKartyEnum.BOHATER && k != pominKarte) {
                jednostkiNieBohaterowieNaRzedzie.add(k);
            }
        }

        if (jednostkiNieBohaterowieNaRzedzie.isEmpty()) {
            System.out.println("SILNIK: (" + zrodloEfektu + ") - Brak kwalifikujących się jednostek (nie-Bohaterów, niepominiętych) w rzędzie " + typRzeduCelu.getNazwaWyswietlana() + " gracza " + graczCel.getProfilUzytkownika().getNazwaUzytkownika());
            if (this.kontrolerPlanszy != null && zrodloEfektu != null && !zrodloEfektu.toLowerCase().contains("foltest")) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(zrodloEfektu + ": Brak celów w rzędzie " + typRzeduCelu.getNazwaWyswietlana() + " przeciwnika.", false);
            }
            return false;
        }

        int maxSila = 0;
        for (Karta k : jednostkiNieBohaterowieNaRzedzie) {
            if (k.getPunktySily() > maxSila) { // Działa na bazowej sile
                maxSila = k.getPunktySily();
            }
        }

        if (maxSila == 0) { // Jeśli wszystkie kwalifikujące się jednostki mają 0 siły bazowej
            System.out.println("SILNIK: (" + zrodloEfektu + ") - Brak jednostek z siłą > 0 w rzędzie " + typRzeduCelu.getNazwaWyswietlana() + " gracza " + graczCel.getProfilUzytkownika().getNazwaUzytkownika());
            if (this.kontrolerPlanszy != null && zrodloEfektu != null && !zrodloEfektu.toLowerCase().contains("foltest")) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(zrodloEfektu + ": Brak wartościowych celów w rzędzie " + typRzeduCelu.getNazwaWyswietlana() + " przeciwnika.", false);
            }
            return false;
        }

        List<Karta> kartyDoZniszczenia = new ArrayList<>();
        for (Karta k : jednostkiNieBohaterowieNaRzedzie) {
            if (k.getPunktySily() == maxSila) {
                kartyDoZniszczenia.add(k);
            }
        }

        if (!kartyDoZniszczenia.isEmpty()) {
            StringBuilder zniszczoneNazwy = new StringBuilder();
            for (Karta k : kartyDoZniszczenia) {
                if (rzadCelu.usunKarteJednostki(k) != null) { // Sprawdź, czy faktycznie usunięto
                    graczCel.getOdrzucone().add(k);
                    zniszczoneNazwy.append(k.getNazwa()).append(" (").append(maxSila).append(" pkt), ");
                }
            }
            if (zniszczoneNazwy.length() > 2) {
                zniszczoneNazwy.setLength(zniszczoneNazwy.length() - 2); // Usuń ostatni przecinek i spację
                System.out.println("SILNIK: " + zrodloEfektu + " niszczy w rzędzie " + typRzeduCelu.getNazwaWyswietlana() + " gracza " + graczCel.getProfilUzytkownika().getNazwaUzytkownika() + " karty: " + zniszczoneNazwy.toString());
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(zrodloEfektu + " zniszczył: " + zniszczoneNazwy.toString() + "!", false);
                }
                return true; // Coś zostało zniszczone
            } else {
                System.out.println("SILNIK: (" + zrodloEfektu + ") - Nie udało się zniszczyć żadnej z wytypowanych kart (np. błąd usuwania).");
                return false; // Nic nie zostało zniszczone
            }
        } else {
            // Ten warunek nie powinien być osiągnięty, jeśli maxSila > 0 i jednostkiNieBohaterowieNaRzedzie nie była pusta
            System.out.println("SILNIK: (" + zrodloEfektu + ") - Nie znaleziono kart do zniszczenia (oczekiwano, że będą).");
            return false;
        }
    }
    private void zastosujEfektLosowegoWskrzeszeniaPrzezNjezdzce(Gracz graczKtoremuWskrzeszamy) {
        // Ta metoda jest wywoływana, gdy Emhyr "Najeźdźca Północy" jest aktywny
        // i umiejętność "Zmartwychwstanie" została zagrana przez graczKtoremuWskrzeszamy.
        // Zastępuje ona standardowy wybór karty z cmentarza.

        System.out.println("SILNIK: Emhyr 'Najeźdźca Północy' - efekt losowego wskrzeszenia dla gracza: " + graczKtoremuWskrzeszamy.getProfilUzytkownika().getNazwaUzytkownika());

        List<Karta> cmentarzGracza = graczKtoremuWskrzeszamy.getOdrzucone();
        List<Karta> mozliweKartyDoLosowegoWskrzeszenia = cmentarzGracza.stream()
                .filter(k -> k.getTyp() == TypKartyEnum.JEDNOSTKA) // Tylko jednostki (nie Bohater, nie Specjalna)
                .collect(Collectors.toList());

        if (mozliweKartyDoLosowegoWskrzeszenia.isEmpty()) {
            System.out.println("SILNIK: Emhyr 'Najeźdźca Północy' - brak kwalifikujących się jednostek na cmentarzu gracza " + graczKtoremuWskrzeszamy.getProfilUzytkownika().getNazwaUzytkownika() + " do losowego wskrzeszenia.");
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Emhyr (Najeźdźca): Brak jednostek na cmentarzu do wskrzeszenia.", false);
            }
            // Mimo braku losowej karty, karta Medyka została zagrana, więc tura powinna być sfinalizowana.
            // Finalizacja tury nastąpi w metodzie wołającej (zagrajKarte -> finalizujTurePoWskrzeszeniu)
            return;
        }

        Collections.shuffle(mozliweKartyDoLosowegoWskrzeszenia);
        Karta wylosowanaKarta = mozliweKartyDoLosowegoWskrzeszenia.get(0);

        cmentarzGracza.remove(wylosowanaKarta); // Usuń z cmentarza
        System.out.println("SILNIK: Emhyr 'Najeźdźca Północy' losowo wskrzesza: " + wylosowanaKarta.getNazwa() + " dla gracza " + graczKtoremuWskrzeszamy.getProfilUzytkownika().getNazwaUzytkownika());

        // Logika umieszczania wylosowanej karty (skopiowana i dostosowana z poprzedniej wersji `sprawdzIZastosujEfektEmhyraNjezdzcy`)
        boolean czySzpieg = wylosowanaKarta.getUmiejetnosc() != null && wylosowanaKarta.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
        boolean czyZrecznosc = wylosowanaKarta.getUmiejetnosc() != null && wylosowanaKarta.getUmiejetnosc().equalsIgnoreCase("Zręczność");
        boolean czyDowolne = wylosowanaKarta.getPozycja() != null && wylosowanaKarta.getPozycja().toLowerCase().contains("dowolne");

        PlanszaGracza planszaDocelowaAutomatycznegoWskrzeszenia;
        Gracz graczNaCzyjejPlanszyLadujeAutomatycznieWskrzeszona;
        TypRzeduEnum typRzeduDoAutomatycznegoPolozenia = null;

        if (czySzpieg) {
            graczNaCzyjejPlanszyLadujeAutomatycznieWskrzeszona = (graczKtoremuWskrzeszamy == gracz1) ? gracz2 : gracz1;
            typRzeduDoAutomatycznegoPolozenia = TypRzeduEnum.fromStringPozycjiKarty(wylosowanaKarta.getPozycja());
            if (typRzeduDoAutomatycznegoPolozenia == null && czyDowolne) {
                TypRzeduEnum[] mozliweRzedyPrzeciwnika = {TypRzeduEnum.PIECHOTA, TypRzeduEnum.STRZELECKIE, TypRzeduEnum.OBLEZENIE};
                typRzeduDoAutomatycznegoPolozenia = mozliweRzedyPrzeciwnika[new Random().nextInt(mozliweRzedyPrzeciwnika.length)];
            }
        } else {
            graczNaCzyjejPlanszyLadujeAutomatycznieWskrzeszona = graczKtoremuWskrzeszamy;
            typRzeduDoAutomatycznegoPolozenia = TypRzeduEnum.fromStringPozycjiKarty(wylosowanaKarta.getPozycja());
            if (typRzeduDoAutomatycznegoPolozenia == null && (czyZrecznosc || czyDowolne)) {
                List<TypRzeduEnum> dostepneRzedy = new ArrayList<>();
                String poz1 = wylosowanaKarta.getPozycja().toLowerCase();
                String poz2 = (wylosowanaKarta.getPozycja2() != null && !wylosowanaKarta.getPozycja2().equalsIgnoreCase("N/D")) ? wylosowanaKarta.getPozycja2().toLowerCase() : "";

                if (czyDowolne || (czyZrecznosc && (poz1.contains("piechota") || poz2.contains("piechota")))) dostepneRzedy.add(TypRzeduEnum.PIECHOTA);
                if (czyDowolne || (czyZrecznosc && (poz1.contains("strzeleckie") || poz2.contains("strzeleckie")))) dostepneRzedy.add(TypRzeduEnum.STRZELECKIE);
                if (czyDowolne || (czyZrecznosc && (poz1.contains("oblężnicze") || poz1.contains("obleznicze") || poz2.contains("oblężnicze") || poz2.contains("obleznicze")))) dostepneRzedy.add(TypRzeduEnum.OBLEZENIE);

                if (!dostepneRzedy.isEmpty()) {
                    typRzeduDoAutomatycznegoPolozenia = dostepneRzedy.get(new Random().nextInt(dostepneRzedy.size()));
                } else {
                    System.out.println(" > Emhyr (Najeźdźca): Nie można ustalić dozwolonych rzędów dla '" + wylosowanaKarta.getNazwa()+"', umieszczanie w Piechocie.");
                    typRzeduDoAutomatycznegoPolozenia = TypRzeduEnum.PIECHOTA;
                }
            }
        }
        planszaDocelowaAutomatycznegoWskrzeszenia = (graczNaCzyjejPlanszyLadujeAutomatycznieWskrzeszona == gracz1) ? aktualnyStanRundy.getPlanszaGracza1() : aktualnyStanRundy.getPlanszaGracza2();

        RzadPlanszy rzadDocelowyNaPlanszy = null;
        if (typRzeduDoAutomatycznegoPolozenia != null && planszaDocelowaAutomatycznegoWskrzeszenia != null) {
            rzadDocelowyNaPlanszy = planszaDocelowaAutomatycznegoWskrzeszenia.getRzad(typRzeduDoAutomatycznegoPolozenia);
        }

        if (rzadDocelowyNaPlanszy != null) {
            rzadDocelowyNaPlanszy.dodajKarteJednostki(wylosowanaKarta);
            System.out.println(" > Losowo wskrzeszona karta '" + wylosowanaKarta.getNazwa() + "' (przez Najeźdźcę) umieszczona w rzędzie " + typRzeduDoAutomatycznegoPolozenia + " na planszy gracza " + graczNaCzyjejPlanszyLadujeAutomatycznieWskrzeszona.getProfilUzytkownika().getNazwaUzytkownika());
            if (czySzpieg) {
                dociagnijKartyDoRekiPoZagrywce(graczKtoremuWskrzeszamy, 2);
            }
            aktywujUmiejetnosciPoZagrywce(graczNaCzyjejPlanszyLadujeAutomatycznieWskrzeszona, wylosowanaKarta, rzadDocelowyNaPlanszy, true); // true => po wskrzeszeniu
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Emhyr (Najeźdźca) wskrzesił: " + wylosowanaKarta.getNazwa() + "!", false);
            }
        } else {
            System.err.println("SILNIK: Emhyr 'Najeźdźca Północy' - nie udało się umieścić losowo wskrzeszonej karty '" + wylosowanaKarta.getNazwa() + "' (ustalony rząd: " + typRzeduDoAutomatycznegoPolozenia + ")");
            graczKtoremuWskrzeszamy.getOdrzucone().add(wylosowanaKarta); // Zwróć kartę na cmentarz
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Emhyr (Najeźdźca): Błąd przy umieszczaniu losowej jednostki.", true);
            }
        }
        // Ta metoda sama w sobie nie finalizuje tury, robi to metoda, która ją wywołała.
    }
    public void wykonajWyborEmhyraPanaPoludnia(Karta wybranaKarta) {
        if (!oczekiwanieNaWyborKartyDlaEmhyra || graczAktywujacyEmhyra == null || przeciwnikDlaEmhyra == null || wybranaKarta == null) {
            System.err.println("SILNIK: Nieprawidłowy stan do wykonania wyboru dla Emhyra 'Pan Południa'.");
            // Jeśli stan jest nieprawidłowy, a graczAktywujacyEmhyra jest znany, to mimo wszystko jego zdolność została "zużyta"
            if (graczAktywujacyEmhyra != null) {
                graczAktywujacyEmhyra.setZdolnoscDowodcyUzyta(true); // Oznacz jako użytą, nawet jeśli błąd
            }
            resetStanuWyboruEmhyra();
            if (graczAktywujacyEmhyra != null && kontrolerPlanszy != null) {
                kontrolerPlanszy.odswiezCalakolwiekPlansze();
                // Tura powinna przejść dalej, nawet jeśli wystąpił błąd z wyborem
                oczekiwanieNaPotwierdzenieTury = true;
                Gracz przeciwnikPoEmhyrze = (graczAktywujacyEmhyra == this.gracz1) ? this.gracz2 : this.gracz1;
                setGraczOczekujacyNaPotwierdzenie(przeciwnikPoEmhyrze);
                kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
            }
            return;
        }

        System.out.println("SILNIK: Emhyr (" + graczAktywujacyEmhyra.getProfilUzytkownika().getNazwaUzytkownika() +
                ") wybrał kartę '" + wybranaKarta.getNazwa() + "' z cmentarza gracza " +
                przeciwnikDlaEmhyra.getProfilUzytkownika().getNazwaUzytkownika());

        if (przeciwnikDlaEmhyra.getOdrzucone().remove(wybranaKarta)) {
            graczAktywujacyEmhyra.getReka().add(wybranaKarta);
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Karta '" + wybranaKarta.getNazwa() + "' dodana do Twojej ręki.", false);
            }
        } else {
            System.err.println("SILNIK: Nie udało się usunąć wybranej karty (" + wybranaKarta.getNazwa() + ") z cmentarza przeciwnika.");
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd: Nie można było wziąć wybranej karty.", true);
            }
        }
        graczAktywujacyEmhyra.setZdolnoscDowodcyUzyta(true); // Oznacz jako użytą
        Gracz graczKtoryUzywalZdolnosci = graczAktywujacyEmhyra;
        resetStanuWyboruEmhyra();

        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.wyswietlKomunikatNaPlanszy(graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika() + " użył zdolności: " + graczKtoryUzywalZdolnosci.getKartaDowodcy().getNazwa() + "!", false);
        }

        // ZAKOŃCZ TURĘ
        oczekiwanieNaPotwierdzenieTury = true;
        Gracz przeciwnikPoEmhyrze = (graczKtoryUzywalZdolnosci == this.gracz1) ? this.gracz2 : this.gracz1;
        setGraczOczekujacyNaPotwierdzenie(przeciwnikPoEmhyrze);
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }
    public void anulujWyborDlaEmhyra() {
        if (!oczekiwanieNaWyborKartyDlaEmhyra || graczAktywujacyEmhyra == null) {
            resetStanuWyboruEmhyra();
            return;
        }
        System.out.println("SILNIK: Anulowano wybór karty dla Emhyra (" + graczAktywujacyEmhyra.getProfilUzytkownika().getNazwaUzytkownika() + ").");

        graczAktywujacyEmhyra.setZdolnoscDowodcyUzyta(true); // Zdolność zużyta mimo anulowania wyboru
        Gracz graczKtoryUzywalZdolnosci = graczAktywujacyEmhyra;
        resetStanuWyboruEmhyra();

        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.wyswietlKomunikatNaPlanszy(graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika() + " użył zdolności: " + graczKtoryUzywalZdolnosci.getKartaDowodcy().getNazwa() + " (anulowano wybór).", false);
        }

        // ZAKOŃCZ TURĘ
        oczekiwanieNaPotwierdzenieTury = true;
        Gracz przeciwnikPoAnulowaniuEmhyra = (graczKtoryUzywalZdolnosci == this.gracz1) ? this.gracz2 : this.gracz1;
        setGraczOczekujacyNaPotwierdzenie(przeciwnikPoAnulowaniuEmhyra);
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }
    private void dociagnijKarteZeZdolnosci(Gracz gracz, int ilosc, String zrodloZdolnosci) {
        if (gracz == null || gracz.getProfilUzytkownika() == null) {
            System.err.println("Błąd w dociagnijKarteZeZdolnosci: gracz lub jego profil jest null.");
            return;
        }
        List<Karta> talia = gracz.getTaliaDoGry();
        List<Karta> reka = gracz.getReka();

        if (talia == null) {
            System.err.println("Błąd: Talia gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + " jest null. Nie można dociągnąć kart.");
            return;
        }
        if (reka == null) { // Na wszelki wypadek
            reka = new ArrayList<>();
            gracz.setReka(reka);
        }

        int dociagnietoFaktycznie = 0;
        for (int i = 0; i < ilosc; i++) {
            // Brak limitu ręki
            if (!talia.isEmpty()) {
                Karta dociagnietaKarta = talia.remove(0);
                reka.add(dociagnietaKarta);
                dociagnietoFaktycznie++;
                System.out.println(" > Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + " dobrał kartę ("+ zrodloZdolnosci +"): " + dociagnietaKarta.getNazwa());
            } else {
                System.out.println("SILNIK OSTRZEŻENIE: Talia gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                        " jest pusta. Nie można dociągnąć karty dla zdolności: " + zrodloZdolnosci + ".");
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy(gracz.getProfilUzytkownika().getNazwaUzytkownika() + ": Talia pusta, nie można dobrać karty ("+zrodloZdolnosci+").", false);
                }
                break;
            }
        }

        if (dociagnietoFaktycznie > 0) {
            System.out.println("SILNIK ("+zrodloZdolnosci+"): Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                    " dociągnął " + dociagnietoFaktycznie + " kart(y). Rozmiar ręki: " + reka.size() +
                    ". W talii pozostało: " + talia.size());
            if (kontrolerPlanszy != null && dociagnietoFaktycznie == ilosc) {
                // Ogólny komunikat o sukcesie dociągnięcia może być tutaj, lub w miejscu wywołania
            }
        }
    }



    public void setGraczOczekujacyNaPotwierdzenie(Gracz gracz) {
        this.graczOczekujacyNaPotwierdzenie = gracz;
        System.out.println("[SILNIK] Ustawiono gracza oczekującego na potwierdzenie na: " + (gracz != null ? gracz.getProfilUzytkownika().getNazwaUzytkownika() : "null"));
    }

    private boolean checkAndPerformAutoPassIfHandEmpty(Gracz gracz) {
        if (gracz.getReka().isEmpty() && !gracz.isCzySpasowalWRundzie()) {
            System.out.println("SILNIK: Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                    " zagrał ostatnią kartę i nie ma więcej kart w ręce. Automatyczne pasowanie.");
            gracz.setCzySpasowalWRundzie(true); // Ustaw flagę pasowania dla gracza

            // Poinformuj kontroler, aby wyświetlił panel pasowania.
            // Kontroler po zniknięciu panelu wywoła kontynuujPoWyswietleniuInfoOPasie(),
            // która obsłuży logikę końca rundy lub zmiany tury.
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.pokazPanelInfoPas(gracz);
            }
            return true; // Auto-pass został zainicjowany
        }
        return false; // Gracz nadal ma karty lub już spasował, brak auto-passu
    }

    private void rozpocznijFazeMulligan() {
        // Zacznij od Gracza 1
        oczekiwanieNaMulliganGracza1 = true;
        oczekiwanieNaMulliganGracza2 = false;
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.pokazPanelMulligan(gracz1);
        }
    }

    public void wykonajMulligan(Gracz gracz, List<Karta> kartyDoWymiany) {
        if ((gracz == gracz1 && !oczekiwanieNaMulliganGracza1) || (gracz == gracz2 && !oczekiwanieNaMulliganGracza2)) {
            System.err.println("BŁĄD: Otrzymano żądanie Mulligan dla gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + " w złym momencie.");
            return;
        }

        if (kartyDoWymiany != null && !kartyDoWymiany.isEmpty()) {
            System.out.println("SILNIK: Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + " wymienia " + kartyDoWymiany.size() + " kart.");

            // Zwróć wybrane karty do talii
            for (Karta karta : kartyDoWymiany) {
                gracz.getReka().remove(karta);
                gracz.getTaliaDoGry().add(karta);
            }

            // Przetasuj talię
            Collections.shuffle(gracz.getTaliaDoGry());

            // Dobierz tyle samo nowych kart
            int iloscDoDociagniecia = kartyDoWymiany.size();
            for (int i = 0; i < iloscDoDociagniecia; i++) {
                if (!gracz.getTaliaDoGry().isEmpty()) {
                    gracz.getReka().add(gracz.getTaliaDoGry().remove(0));
                }
            }
        } else {
            System.out.println("SILNIK: Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + " nie wymienił żadnej karty.");
        }

        // Zaktualizuj widok po wymianie (ważne!)
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
        }

        // Przejdź do następnego etapu
        if (gracz == gracz1) {
            // Gracz 1 skończył, teraz kolej Gracza 2
            oczekiwanieNaMulliganGracza1 = false;
            oczekiwanieNaMulliganGracza2 = true;

            // W trybie hot-seat warto pokazać panel zmiany tury
            if(kontrolerPlanszy != null) {
                // Możesz użyć istniejącego panelu zmiany tury, aby ukryć planszę
                kontrolerPlanszy.pokazPanelPrzejeciaTury(gracz2); // Ten panel ma przycisk "Potwierdź", ale handler tego przycisku musimy dostosować...
                // Prostsze rozwiązanie na teraz: po prostu pokaż panel dla gracza 2.
                // Logika Hot-Seat wymaga ukrycia ekranu. Na razie załóżmy, że gracze są uczciwi.
                kontrolerPlanszy.pokazPanelMulligan(gracz2);
            }

        } else if (gracz == gracz2) {
            // Gracz 2 skończył, faza mulligan zakończona. Rozpocznij grę.
            oczekiwanieNaMulliganGracza2 = false;
            System.out.println("SILNIK: Faza Mulligan zakończona. Rozpoczynanie pierwszej rundy.");
            rozpocznijNowaRunde(); // To rozpocznie właściwą grę (rzut monetą itp. jeśli tam jest)
        }
    }

    // Metoda awaryjna, jeśli UI dla mulligan zawiedzie
    public void pominMulligan(Gracz gracz) {
        if (gracz == gracz1) {
            oczekiwanieNaMulliganGracza1 = false;
            oczekiwanieNaMulliganGracza2 = true;
            if(kontrolerPlanszy != null) kontrolerPlanszy.pokazPanelMulligan(gracz2);
        } else {
            oczekiwanieNaMulliganGracza2 = false;
            rozpocznijNowaRunde();
        }
    }

    // Getter potrzebny dla kontrolera
    public Gracz getGraczDlaMulligan() {
        if (oczekiwanieNaMulliganGracza1) return gracz1;
        if (oczekiwanieNaMulliganGracza2) return gracz2;
        return null;
    }

}