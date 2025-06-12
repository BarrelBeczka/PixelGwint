package pixelgwint.logika;

import javafx.util.Duration;
import pixelgwint.model.*;
import pixelgwint.widok.KontrolerPlanszyGry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


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

    public Gracz getGracz1() { return gracz1; }
    public Gracz getGracz2() { return gracz2; }
    public Gracz getGraczAktualnejTury() { return graczAktualnejTury; }
    public StanRundy getStanRundy() { return aktualnyStanRundy; }
    public int getNumerRundyGry() { return numerRundyGry; }
    public boolean isCzyGraZakonczona() { return czyGraZakonczona; }
    public boolean isOczekiwanieNaPotwierdzenieTury() { return oczekiwanieNaPotwierdzenieTury; }


    //Zwraca gracza, który aktywował kartę "Manekin do ćwiczeń" i jest w trakcie wyboru celu.
    public Gracz getGraczUzywajacyManekina() {
        return this.graczUzywajacyManekina;
    }


    //Konstruktor klasy SilnikGry. Inicjalizuje stan gry, przypisując graczy, planszę oraz referencję do kontrolera widoku.
    public SilnikGry(Gracz g1, Gracz g2, Gracz graczRozpoczynajacy, KontrolerPlanszyGry kontroler, List<Karta> wszystkieKarty) {
        this.gracz1 = g1;
        this.gracz2 = g2;
        this.graczRozpoczynajacyPartie = graczRozpoczynajacy;
        this.kontrolerPlanszy = kontroler;
        this.historiaWynikowRund = new ArrayList<>();


        if (g1 != null && g2 != null && g1.getPlanszaGry() != null && g2.getPlanszaGry() != null) {
            this.aktualnyStanRundy = new StanRundy(g1.getPlanszaGry(), g2.getPlanszaGry());
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
        } else {
            System.err.println("BŁĄD KRYTYCZNY: Obiekty graczy lub ich plansze są null przy tworzeniu SilnikaGry! Nie można ustawić referencji StanRundy dla rzędów.");
                     if (this.aktualnyStanRundy == null) {
                this.aktualnyStanRundy = new StanRundy(null, null);
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

    //Metoda pomocnicza resetująca stan związany z użyciem zdolności Emhyra.
    private void resetStanuWyboruEmhyra() {
        this.oczekiwanieNaWyborKartyDlaEmhyra = false;
        this.graczAktywujacyEmhyra = null;
        this.przeciwnikDlaEmhyra = null;
    }

    private void resetStanuEredina415() {
        this.oczekiwanieNaWyborKartyZCmentarzaPrzezEredina415 = false;
        this.graczAktywujacyEredina415 = null;
    }

    //Rozpoczyna całą partię. Resetuje stan graczy, tasuje talie, rozdaje karty startowe i inicjuje fazę wymiany kart (Mulligan).
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

    //Zwraca gracza, który aktywował zdolność dowódcy Emhyra i jest w trakcie wyboru karty z cmentarza przeciwnika.
    public Gracz getGraczAktywujacyEmhyra() {
        return this.graczAktywujacyEmhyra;
    }

    //Zwraca gracza, który aktywował zdolność dowódcy Eredina (ID 415) i jest w trakcie wyboru karty ze swojego cmentarza.
    public Gracz getGraczAktywujacyEredina415() { return this.graczAktywujacyEredina415; }

    //Zwraca gracza, który aktywował zdolność dowódcy Eredina (ID 415) i jest w trakcie wyboru karty ze swojego cmentarza.
    private void dociagnijKartyDoReki(Gracz gracz, int iloscDoDociagniecia) { // Zmieniona nazwa parametru dla jasności


        if (gracz == null || gracz.getProfilUzytkownika() == null) {
            System.err.println("Błąd: Próba dociągnięcia kart dla nieistniejącego gracza lub gracza bez profilu.");
            return;
        }

        if (gracz.getReka() == null) {
            gracz.setReka(new ArrayList<>());
        }
        gracz.getReka().clear();

        List<Karta> talia = gracz.getTaliaDoGry();
        System.out.println("[SilnikGry.dociagnijKarty] Gracz: " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + ", rozmiar taliaDoGry: " + (talia != null ? talia.size() : "Talia jest NULL"));

        int faktycznieDociagnieto = 0;
        for (int i = 0; i < iloscDoDociagniecia; i++) {
            if (!talia.isEmpty()) {
                gracz.getReka().add(talia.remove(0));
                faktycznieDociagnieto++;
            } else {
                System.out.println("SILNIK OSTRZEŻENIE: Talia gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                        " jest pusta. Dociągnięto " + faktycznieDociagnieto + " z " + iloscDoDociagniecia + " kart na start.");
                break;
            }
        }
        System.out.println("SILNIK DEBUG (start): Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                " dociągnął " + gracz.getReka().size() + " kart. W talii pozostało: " + talia.size());
    }

    //Rozpoczyna nową rundę. Resetuje flagi pasowania, czyści plansze, ustala, który gracz rozpoczyna, i prosi go o potwierdzenie tury.
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
        this.graczOczekujacyNaPotwierdzenie = graczAktualnejTury;
        oczekiwanieNaPotwierdzenieTury = true;
        System.out.println("[SILNIK (rozpocznijNowaRunde)] Runda " + numerRundyGry + ". Ustalony gracz aktualnej tury (oczekujący): " +
                (this.graczOczekujacyNaPotwierdzenie != null ? this.graczOczekujacyNaPotwierdzenie.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                " (Obiekt ID: " + System.identityHashCode(this.graczOczekujacyNaPotwierdzenie) + ")");
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.pokazPanelPrzejeciaTury(this.graczOczekujacyNaPotwierdzenie);
        }
    }

    //Zwraca gracza, który zagrał Medyka i jest w trakcie wybierania miejsca dla wskrzeszonej karty.
    public Gracz getGraczKladacyKartePoWskrzeszeniu() {
        return this.graczKladacyKartePoWskrzeszeniu;
    }

    //Wykonuje akcję zamiany jednostki na Manekina do Ćwiczeń.
    public void wykonajZamianeManekinem(Gracz graczZagrywajacyManekina, Karta wybranaJednostka, RzadPlanszy rzadPochodzenia, int indeksWRzedzie) {
        if (!oczekiwanieNaWyborCeluDlaManekina || graczZagrywajacyManekina != this.graczUzywajacyManekina || this.kartaManekinaDoPolozenia == null) {
            System.err.println("SILNIK: Niepoprawny stan do wykonania zamiany manekinem.");
            if (this.graczUzywajacyManekina != null && this.kartaManekinaDoPolozenia != null &&
                    this.graczUzywajacyManekina.getReka() != null && !this.graczUzywajacyManekina.getReka().contains(this.kartaManekinaDoPolozenia)) {
                this.graczUzywajacyManekina.getReka().add(this.kartaManekinaDoPolozenia);
            }
            resetStanuManekina();
            finalizujTurePoNieudanymZagraniaKarty(graczZagrywajacyManekina);
            return;
        }

        System.out.println("SILNIK: Wykonuję zamianę manekinem. Manekin: " + kartaManekinaDoPolozenia.getNazwa() + ", Jednostka: " + wybranaJednostka.getNazwa());

        Karta usunietaKarta = rzadPochodzenia.usunKarteJednostkiZIndeksu(indeksWRzedzie);
        if (usunietaKarta != wybranaJednostka) {
            System.err.println("SILNIK: Błąd! Usunięto inną kartę niż oczekiwano podczas zamiany manekinem.");
            if (usunietaKarta != null) rzadPochodzenia.getKartyJednostekWRzedzie().add(indeksWRzedzie, usunietaKarta);
            if (graczZagrywajacyManekina.getReka() != null && !graczZagrywajacyManekina.getReka().contains(kartaManekinaDoPolozenia)) {
                graczZagrywajacyManekina.getReka().add(kartaManekinaDoPolozenia);
            }
            resetStanuManekina();
            finalizujTurePoNieudanymZagraniaKarty(graczZagrywajacyManekina);
            return;
        }

        graczZagrywajacyManekina.getReka().add(wybranaJednostka);
        System.out.println(" > Jednostka " + wybranaJednostka.getNazwa() + " wróciła do ręki gracza " + graczZagrywajacyManekina.getProfilUzytkownika().getNazwaUzytkownika());

        rzadPochodzenia.getKartyJednostekWRzedzie().add(indeksWRzedzie, kartaManekinaDoPolozenia);
        System.out.println(" > Manekin " + kartaManekinaDoPolozenia.getNazwa() + " umieszczony w rzędzie " + rzadPochodzenia.getTypRzedu() + " na pozycji " + indeksWRzedzie);

        resetStanuManekina();
        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();

        System.out.println("[SILNIK Manekin] Finalizowanie tury dla gracza: " + graczZagrywajacyManekina.getProfilUzytkownika().getNazwaUzytkownika());

        if (checkAndPerformAutoPassIfHandEmpty(graczZagrywajacyManekina)) {
            System.out.println("[SILNIK Manekin] Gracz automatycznie spasował po zagraniu Manekina.");
            return;
        }

        Gracz przeciwnik = (graczZagrywajacyManekina == this.gracz1) ? this.gracz2 : this.gracz1;
        System.out.println("[SILNIK Manekin] Przeciwnik to: " + przeciwnik.getProfilUzytkownika().getNazwaUzytkownika());
        System.out.println("[SILNIK Manekin] Sprawdzanie czy przeciwnik spasował: " + przeciwnik.isCzySpasowalWRundzie());

        if (przeciwnik.isCzySpasowalWRundzie()) {
            System.out.println("[SILNIK Manekin] Przeciwnik już spasował. Tura pozostaje u obecnego gracza.");
            graczAktualnejTury = graczZagrywajacyManekina;
            oczekiwanieNaPotwierdzenieTury = false;
            setGraczOczekujacyNaPotwierdzenie(null);
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
            }
        } else {
            System.out.println("[SILNIK Manekin] Przeciwnik nie spasował. Rozpoczynanie sekwencji zmiany tury.");
            oczekiwanieNaPotwierdzenieTury = true;
            setGraczOczekujacyNaPotwierdzenie(przeciwnik);
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
            }
        }
    }

    //Anuluje akcję zagrania Manekina i przywraca go na rękę gracza. Tura nie przechodzi, a gracz może wykonać inną akcję.
    public void anulujZagranieManekina(Gracz graczAnulujacy, boolean czyBladSystemowy) {
        if (!oczekiwanieNaWyborCeluDlaManekina || graczAnulujacy != this.graczUzywajacyManekina || this.kartaManekinaDoPolozenia == null) {
            System.err.println("SILNIK: Niepoprawny stan do anulowania zagrania manekinem lub brak danych.");
            resetStanuManekina();
            if (czyBladSystemowy && this.kontrolerPlanszy != null) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd Manekina. Spróbuj ponownie.", true);
                this.kontrolerPlanszy.resetujStanZaznaczonejKarty();
                this.kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAnulujacy == this.gracz1);
            }
            return;
        }

        System.out.println("SILNIK: Gracz " + graczAnulujacy.getProfilUzytkownika().getNazwaUzytkownika() + " anulował zagranie Manekina.");
        if (graczAnulujacy.getReka() != null && this.kartaManekinaDoPolozenia != null &&
                !graczAnulujacy.getReka().contains(this.kartaManekinaDoPolozenia)) {
            graczAnulujacy.getReka().add(this.kartaManekinaDoPolozenia);
        }

        resetStanuManekina();

        if (this.kontrolerPlanszy != null) {
            this.kontrolerPlanszy.odswiezCalakolwiekPlansze();
            this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Zagranie Manekina anulowane. Wybierz inną akcję.", false);
            this.kontrolerPlanszy.resetujStanZaznaczonejKarty();
            this.kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAnulujacy == this.gracz1);
        }
    }

    //Metoda pomocnicza resetująca stan związany z użyciem zdolności Eredina (ID 414).
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

    //Metoda pomocnicza do finalizowania tury, gdy zagranie karty nie powiodło się lub zostało anulowane. Przekazuje turę do przeciwnika.
    private void finalizujTurePoNieudanymZagraniaKarty(Gracz graczKtoryProbowałZagrać) {
        System.out.println("SILNIK: Finalizowanie tury po nieudanym/anulowanym zagraniu karty przez " + graczKtoryProbowałZagrać.getProfilUzytkownika().getNazwaUzytkownika());
        if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();

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

    //Przetwarza akcję zagrania karty przez gracza. Jest to jedna z najbardziej złożonych metod, obsługująca różne typy kart i ich umiejętności.
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
                oczekiwanieNaWyborKartyZTaliiPrzezEredin414 || oczekiwanieNaWyborKartyZCmentarzaPrzezEredina415) {
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

        if (czyZagrywanaKartaToManekin && karta.getTyp() == TypKartyEnum.SPECJALNA) {
            List<Karta> celeDlaManekina = znajdzPoprawneCeleDlaManekina(graczZagrywajacy);

            if (celeDlaManekina.isEmpty()) {
                System.out.println("SILNIK: Brak celów dla Manekina na planszy gracza " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika());
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Brak jednostek do zamiany z Manekinem. Wybierz inną kartę.", false);
                    this.kontrolerPlanszy.resetujStanZaznaczonejKarty();
                }
                return false;
            }

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
            return true;
        }

        if (!graczZagrywajacy.getReka().remove(karta)) {
            System.err.println("SILNIK: Błąd krytyczny - karty " + karta.getNazwa() + " nie udało się usunąć z ręki gracza " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika());
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
            else if (!czyZagrywanaKartaToManekin) {
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
                graczZagrywajacy.getReka().add(karta);
                return false;
            }
            planszaDocelowaDlaJednostki = (graczNaPlanszyKtoregoLadujeJednostka == gracz1) ? aktualnyStanRundy.getPlanszaGracza1() : aktualnyStanRundy.getPlanszaGracza2();

            if (planszaDocelowaDlaJednostki == null) {
                System.err.println("BŁĄD KRYTYCZNY: planszaDocelowaDlaJednostki jest null dla karty: " + karta.getNazwa());
                graczZagrywajacy.getReka().add(karta);
                return false;
            }

            TypRzeduEnum rzadDoUmieszczeniaKarty = rzadDocelowyEnumDlaKartyPierwotnej;
            if (rzadDoUmieszczeniaKarty == null) {
                rzadDoUmieszczeniaKarty = TypRzeduEnum.fromStringPozycjiKarty(karta.getPozycja());
                if (rzadDoUmieszczeniaKarty == null && karta.getPozycja().toLowerCase().contains("dowolne")) {
                    System.err.println("SILNIK: Karta '" + karta.getNazwa() + "' z pozycją 'Dowolne' wymaga wyboru rzędu przez gracza (nie przekazano).");
                    graczZagrywajacy.getReka().add(karta);
                    return false;
                }
            }

            if (rzadDoUmieszczeniaKarty == null) {
                System.err.println("SILNIK: Nie można ustalić rzędu dla karty '" + karta.getNazwa() + "' (pozycja: " + karta.getPozycja() + ").");
                graczZagrywajacy.getReka().add(karta);
                return false;
            }

            rzadNaKtoryTrafiaPierwotnaKarta = planszaDocelowaDlaJednostki.getRzad(rzadDoUmieszczeniaKarty);

            if (rzadNaKtoryTrafiaPierwotnaKarta != null) {
                rzadNaKtoryTrafiaPierwotnaKarta.dodajKarteJednostki(karta);
                czyPoprawnieZagranaPierwotnaKarta = true;
                System.out.println(" > Karta " + karta.getNazwa() + " dodana do rzędu " + rzadDoUmieszczeniaKarty + " na planszy gracza " + graczNaPlanszyKtoregoLadujeJednostka.getProfilUzytkownika().getNazwaUzytkownika());

                if (czyZagrywanaKartaToSzpieg) {
                    aktywujUmiejetnosciPoZagrywce(graczNaPlanszyKtoregoLadujeJednostka, karta, rzadNaKtoryTrafiaPierwotnaKarta, false);
                    przeliczWszystkiePunktyNaPlanszy();
                    if(kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();

                    // Teraz poproś kontroler o animację dobierania kart
                    System.out.println(" > Szpieg zagrany. Inicjowanie animacji dobierania kart.");
                    List<Karta> talia = graczZagrywajacy.getTaliaDoGry();
                    List<Karta> kartyDoDobrania = new ArrayList<>();
                    if (!talia.isEmpty()) kartyDoDobrania.add(talia.get(0));
                    if (talia.size() > 1) kartyDoDobrania.add(talia.get(1));

                    if (!kartyDoDobrania.isEmpty() && kontrolerPlanszy != null) {
                        kontrolerPlanszy.rozpocznijAnimacjeDobierania(graczZagrywajacy, kartyDoDobrania);
                    } else {
                        finalizujTurePoDobieraniu(graczZagrywajacy, new ArrayList<>());
                    }
                    return true;
                }
                if (!czyZagrywanaKartaMaZmartwychwstanie && !czyZagrywanaKartaMaPozogaK) {
                    System.out.println("  DEBUG (Braterstwo/Inne): Wywołuję aktywujUmiejetnosciPoZagrywce dla: " + karta.getNazwa());
                    aktywujUmiejetnosciPoZagrywce(graczNaPlanszyKtoregoLadujeJednostka, karta, rzadNaKtoryTrafiaPierwotnaKarta, false);
                }
            } else {
                System.err.println("SILNIK: Nie można było uzyskać konkretnego rzędu (" + rzadDoUmieszczeniaKarty + ") dla karty " + karta.getNazwa() + ".");
                graczZagrywajacy.getReka().add(karta);
                return false;
            }
        } else {
            System.err.println("SILNIK: Próba zagrania karty nieobsługiwanego typu na planszę: " + (karta.getTyp() != null ? karta.getTyp().name() : "TYP NULL"));
            graczZagrywajacy.getReka().add(karta);
            return false;
        }
        if (czyPoprawnieZagranaPierwotnaKarta) {
            if (czyZagrywanaKartaMaPozogaS) {
                aktywujPozogaGlobalna(graczZagrywajacy);
            } else if (czyZagrywanaKartaMaPozogaK) {
                aktywujPozogaNaRzedzie(graczZagrywajacy, karta, rzadNaKtoryTrafiaPierwotnaKarta);
            }

            if (czyZagrywanaKartaMaZmartwychwstanie) {
                System.out.println("SILNIK: Karta '" + karta.getNazwa() + "' (Medyk) aktywuje Zmartwychwstanie.");
                przeliczWszystkiePunktyNaPlanszy();
                if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();

                boolean emhyr71AktywnyWGrze = (gracz1.getKartaDowodcy() != null && gracz1.getKartaDowodcy().getId() == 71 && !gracz1.isZdolnoscDowodcyUzyta()) ||
                        (gracz2.getKartaDowodcy() != null && gracz2.getKartaDowodcy().getId() == 71 && !gracz2.isZdolnoscDowodcyUzyta());

                if (emhyr71AktywnyWGrze) {
                    System.out.println("SILNIK: Aktywny Emhyr 'Najeźdźca Północy'! Losowe wskrzeszenie dla gracza: " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika());
                    if (kontrolerPlanszy != null) {
                        kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Emhyr (Najeźdźca): Losowe wskrzeszenie z Twojego cmentarza...", false);
                    }
                    zastosujEfektLosowegoWskrzeszeniaPrzezNjezdzce(graczZagrywajacy);
                    finalizujTurePoWskrzeszeniu(graczZagrywajacy);
                } else {
                    System.out.println("SILNIK: Standardowe Zmartwychwstanie - otwieranie cmentarza dla gracza: " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika());
                    if (this.kontrolerPlanszy != null) {
                        this.kontrolerPlanszy.pokazPanelCmentarza(graczZagrywajacy, "Wybierz kartę do wskrzeszenia", KontrolerPlanszyGry.TrybPaneluCmentarzaKontekst.WSKRZESZANIE_MEDYK);
                    }
                }
                return true;
            }
            przeliczWszystkiePunktyNaPlanszy();
            if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();
            if (checkAndPerformAutoPassIfHandEmpty(graczZagrywajacy)) {
                return true;
            }
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
            return true;
        } else {
            if (!graczZagrywajacy.getReka().contains(karta)) {
                graczZagrywajacy.getReka().add(karta);
                System.out.println("SILNIK: Karta " + karta.getNazwa() + " zwrócona do ręki gracza " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika() + " z powodu nieudanego zagrania lub braku celu.");
            }
            if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();
            return false;
        }
    }

    //Uruchamia umiejętności karty tuż po jej umieszczeniu na planszy, takie jak 'Braterstwo' (Muster) czy 'Wysokie morale' (Morale Boost).
    private void aktywujUmiejetnosciPoZagrywce(Gracz graczNaPlanszyKtoregoLadujeKarta, Karta karta, RzadPlanszy rzadNaKtoryTrafia, boolean czyPoWskrzeszeniu) {
        if (karta == null || rzadNaKtoryTrafia == null || graczNaPlanszyKtoregoLadujeKarta == null) return;
        String umiejetnosc = karta.getUmiejetnosc();
        if (umiejetnosc == null || umiejetnosc.trim().isEmpty()) return;

        System.out.println("  DEBUG (aktywujUmiejetnosciPoZagrywce): Karta '" + karta.getNazwa() + "', Umiejętność: '" + umiejetnosc + "'");

        if (umiejetnosc.equalsIgnoreCase("Braterstwo")) {
            System.out.println("    DEBUG (Braterstwo): Rozpoznano umiejętność Braterstwo dla '" + karta.getNazwa() + "'.");
            if (graczNaPlanszyKtoregoLadujeKarta != null) {
                System.out.println("    DEBUG (Braterstwo): Aktywuję Braterstwo dla gracza: " + graczNaPlanszyKtoregoLadujeKarta.getProfilUzytkownika().getNazwaUzytkownika() + " dla karty " + karta.getNazwa());
                aktywujBraterstwo(graczNaPlanszyKtoregoLadujeKarta, karta, rzadNaKtoryTrafia);
            } else {
                System.err.println("    DEBUG (Braterstwo): Błąd - graczNaPlanszyKtoregoLadujeKarta jest null dla Braterstwa.");
            }
        } else if (umiejetnosc.equalsIgnoreCase("Wysokie morale")) {
            System.out.println(" > Karta '" + karta.getNazwa() + "' z umiejętnością 'Wysokie morale' została umieszczona w rzędzie. Efekt jest ciągły (aura).");
        }
    }

    //Metoda pomocnicza czyszcząca stan związany z użyciem karty 'Manekin do ćwiczeń', przywracając domyślne wartości.
    private void resetStanuManekina() {
        this.oczekiwanieNaWyborCeluDlaManekina = false;
        this.kartaManekinaDoPolozenia = null;
        this.graczUzywajacyManekina = null;
    }

    //Aktywuje efekt karty Pożoga (Scorch), niszcząc najsilniejszą lub najsilniejsze jednostki (nie-bohaterów) na całej planszy.
    private void aktywujPozogaGlobalna(Gracz graczZagrywajacy) {
        System.out.println("SILNIK: Aktywacja Pożogi Globalnej (Pożoga_S) przez gracza: " + graczZagrywajacy.getProfilUzytkownika().getNazwaUzytkownika());

        Map<Karta, Integer> sileEfektywneWszystkichKart = new HashMap<>();
        Map<Karta, RzadPlanszy> rzedyKart = new HashMap<>();
        Map<Karta, Gracz> wlascicieleKart = new HashMap<>();

        PlanszaGracza planszaG1 = aktualnyStanRundy.getPlanszaGracza1();
        if (planszaG1 != null) {
            for (TypRzeduEnum typRzedu : TypRzeduEnum.values()) {
                RzadPlanszy rzad = planszaG1.getRzad(typRzedu);
                if (rzad != null) {
                    for (Karta k : rzad.getKartyJednostekWRzedzie()) {
                        if (k.getTyp() != TypKartyEnum.BOHATER) {
                            int efektywnaSila = rzad.obliczSileEfektywnaKarty(k);
                            sileEfektywneWszystkichKart.put(k, efektywnaSila);
                            rzedyKart.put(k, rzad);
                            wlascicieleKart.put(k, gracz1);
                        }
                    }
                }
            }
        }
        PlanszaGracza planszaG2 = aktualnyStanRundy.getPlanszaGracza2();
        if (planszaG2 != null) {
            for (TypRzeduEnum typRzedu : TypRzeduEnum.values()) {
                RzadPlanszy rzad = planszaG2.getRzad(typRzedu);
                if (rzad != null) {
                    for (Karta k : rzad.getKartyJednostekWRzedzie()) {
                        if (k.getTyp() != TypKartyEnum.BOHATER) {
                            int efektywnaSila = rzad.obliczSileEfektywnaKarty(k);
                            sileEfektywneWszystkichKart.put(k, efektywnaSila);
                            rzedyKart.put(k, rzad);
                            wlascicieleKart.put(k, gracz2);
                        }
                    }
                }
            }
        }

        if (sileEfektywneWszystkichKart.isEmpty()) {
            System.out.println("SILNIK: Pożoga Globalna - brak jednostek nie-bohaterów na planszy.");
            return;
        }
        int maxSila = 0;
        for (Integer sila : sileEfektywneWszystkichKart.values()) {
            if (sila > maxSila) {
                maxSila = sila;
            }
        }

        if (maxSila == 0) {
            System.out.println("SILNIK: Pożoga Globalna - brak jednostek z siłą > 0.");
            return;
        }
        List<Karta> kartyDoZniszczenia = new ArrayList<>();
        for (Map.Entry<Karta, Integer> entry : sileEfektywneWszystkichKart.entrySet()) {
            if (entry.getValue() == maxSila) {
                kartyDoZniszczenia.add(entry.getKey());
            }
        }

        if (!kartyDoZniszczenia.isEmpty()) {
            System.out.print("SILNIK: Pożoga Globalna niszczy (" + maxSila + " pkt): ");
            for (Karta k : kartyDoZniszczenia) {
                Gracz wlasciciel = wlascicieleKart.get(k);
                RzadPlanszy rzad = rzedyKart.get(k);
                if (wlasciciel != null && rzad != null) {
                    rzad.usunKarteJednostki(k);
                    wlasciciel.getOdrzucone().add(k);
                    System.out.print(k.getNazwa() + " (gracza " + wlasciciel.getProfilUzytkownika().getNazwaUzytkownika() + "), ");
                }
            }
            System.out.println();
        }
    }

    //Aktywuje efekt Pożogi na konkretnym rzędzie przeciwnika, jeśli suma siły w tym rzędzie wynosi co najmniej 10. Niszczy najsilniejszą jednostkę (lub jednostki) w tym rzędzie.
    private void aktywujPozogaNaRzedzie(Gracz graczAktywujacy, Karta kartaPozogi, RzadPlanszy rzadGdzieKartaPozogiLandowala) {
        if (rzadGdzieKartaPozogiLandowala == null) {
            System.err.println("SILNIK: Pożoga_K - karta Pozogi nie została umieszczona w żadnym rzędzie.");
            return;
        }
        System.out.println("SILNIK: Aktywacja Pożogi Na Rzędzie (Pożoga_K) przez kartę: " + kartaPozogi.getNazwa());

        Gracz przeciwnik = (graczAktywujacy == gracz1) ? gracz2 : gracz1;
        RzadPlanszy rzadPrzeciwnika = przeciwnik.getPlanszaGry().getRzad(rzadGdzieKartaPozogiLandowala.getTypRzedu());

        if (rzadPrzeciwnika == null) {
            System.out.println("SILNIK: Pożoga_K - przeciwnik nie ma odpowiadającego rzędu.");
            return;
        }

        if (rzadPrzeciwnika.getSumaPunktowWRzedzie() >= 10) {
            Map<Karta, Integer> sileEfektywneWRzedzie = new HashMap<>();
            for (Karta k : rzadPrzeciwnika.getKartyJednostekWRzedzie()) {
                if (k.getTyp() != TypKartyEnum.BOHATER) {
                    sileEfektywneWRzedzie.put(k, rzadPrzeciwnika.obliczSileEfektywnaKarty(k));
                }
            }

            if (sileEfektywneWRzedzie.isEmpty()) return;

            int maxSila = sileEfektywneWRzedzie.values().stream().max(Integer::compare).orElse(0);

            if (maxSila > 0) {
                List<Karta> kartyDoZniszczenia = sileEfektywneWRzedzie.entrySet().stream()
                        .filter(entry -> entry.getValue() == maxSila)
                        .map(Map.Entry::getKey)
                        .collect(Collectors.toList());

                System.out.print("SILNIK: Pożoga_K niszczy w rzędzie " + rzadPrzeciwnika.getTypRzedu() + " przeciwnika (" + maxSila + " pkt): ");
                for (Karta k : kartyDoZniszczenia) {
                    rzadPrzeciwnika.usunKarteJednostki(k);
                    przeciwnik.getOdrzucone().add(k);
                    System.out.print(k.getNazwa() + ", ");
                }
                System.out.println();
            }
        } else {
            System.out.println("SILNIK: Pożoga_K - warunek siły rzędu przeciwnika (>=10) nie został spełniony.");
        }
    }

    //Metoda wywoływana po wybraniu przez gracza karty z cmentarza do wskrzeszenia. Usuwa kartę z cmentarza i sprawdza, czy wymaga ona wyboru rzędu.
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
        } else {
            PlanszaGracza planszaGraczaWskrzeszajacego = (graczWskrzeszajacy == gracz1) ? aktualnyStanRundy.getPlanszaGracza1() : aktualnyStanRundy.getPlanszaGracza2();
            TypRzeduEnum rzadKarty = TypRzeduEnum.fromStringPozycjiKarty(kartaDoWskrzeszenia.getPozycja());

            if (rzadKarty != null && planszaGraczaWskrzeszajacego != null && planszaGraczaWskrzeszajacego.getRzad(rzadKarty) != null) {
                RzadPlanszy rzadDocelowy = planszaGraczaWskrzeszajacego.getRzad(rzadKarty);
                rzadDocelowy.dodajKarteJednostki(kartaDoWskrzeszenia);
                System.out.println("SILNIK: Wskrzeszona karta " + kartaDoWskrzeszenia.getNazwa() + " umieszczona w rzędzie " + rzadKarty);
                aktywujUmiejetnosciPoZagrywce(graczWskrzeszajacy, kartaDoWskrzeszenia, rzadDocelowy, true);
                finalizujTurePoWskrzeszeniu(graczWskrzeszajacy);
            } else {
                System.err.println("SILNIK: Nie można umieścić wskrzeszonej karty '" + kartaDoWskrzeszenia.getNazwa() + "' - niepoprawny lub nieokreślony rząd docelowy (pozycja: '" + kartaDoWskrzeszenia.getPozycja() + "') lub plansza gracza jest null.");
                graczWskrzeszajacy.getOdrzucone().add(kartaDoWskrzeszenia);
                finalizujTurePoNieudanymWskrzeszeniu(graczWskrzeszajacy);
            }
        }
    }

    //Umieszcza wskrzeszoną kartę na wybranym przez gracza rzędzie.
    public void polozWskrzeszonaKarteNaRzedzie(Gracz graczKtoryWybralRzadFaktycznie, Karta karta, TypRzeduEnum rzadDocelowyEnum) {

        if (!oczekiwanieNaWyborRzeduPoWskrzeszeniu || karta != kartaOczekujacaNaPolozeniePoWskrzeszeniu || graczKladacyKartePoWskrzeszeniu == null) {
            System.err.println("SILNIK: Niespójny stan przy próbie położenia wskrzeszonej karty (oczekiwanie: " + oczekiwanieNaWyborRzeduPoWskrzeszeniu +
                    ", karta oczekiwana: " + (kartaOczekujacaNaPolozeniePoWskrzeszeniu != null ? kartaOczekujacaNaPolozeniePoWskrzeszeniu.getNazwa() : "null") +
                    ", karta aktualna: " + (karta != null ? karta.getNazwa() : "null") +
                    ", gracz kładący: " + (graczKladacyKartePoWskrzeszeniu != null ? graczKladacyKartePoWskrzeszeniu.getProfilUzytkownika().getNazwaUzytkownika() : "null") + ")");

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


            finalizujTurePoWskrzeszeniu(graczKladacyKartePoWskrzeszeniu);
        } else {
            System.err.println("SILNIK: Błąd krytyczny - nie znaleziono rzędu ("+ rzadDocelowyEnum +") na planszy ("+graczNaPlanszyDocelowej.getProfilUzytkownika().getNazwaUzytkownika()+") dla wskrzeszonej karty.");
            graczKladacyKartePoWskrzeszeniu.getOdrzucone().add(karta);
            finalizujTurePoNieudanymWskrzeszeniu(graczKladacyKartePoWskrzeszeniu);
        }
        resetStanuOczekiwaniaNaPolozenieWskrzeszonej();
    }

    //Metoda pomocnicza resetująca stan związany z procesem wskrzeszania karty.
    private void resetStanuOczekiwaniaNaPolozenieWskrzeszonej() {
        this.kartaOczekujacaNaPolozeniePoWskrzeszeniu = null;
        this.graczKladacyKartePoWskrzeszeniu = null;
        this.oczekiwanieNaWyborRzeduPoWskrzeszeniu = false;
    }


    //Finalizuje turę po udanej operacji wskrzeszenia. Przelicza punkty, odświeża widok i przekazuje turę do przeciwnika.
    private void finalizujTurePoWskrzeszeniu(Gracz graczKtoryWskrzeszal) {
        System.out.println("SILNIK: Finalizowanie tury po udanym wskrzeszeniu przez " + graczKtoryWskrzeszal.getProfilUzytkownika().getNazwaUzytkownika());
        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) kontrolerPlanszy.odswiezCalakolwiekPlansze();
        resetStanuOczekiwaniaNaPolozenieWskrzeszonej();

        Gracz przeciwnik = (graczKtoryWskrzeszal == this.gracz1) ? this.gracz2 : this.gracz1;
        if (przeciwnik.isCzySpasowalWRundzie()) {
            oczekiwanieNaPotwierdzenieTury = false;
            setGraczOczekujacyNaPotwierdzenie(null);
            System.out.println("SILNIK (po wskrzeszeniu): Przeciwnik spasował, tura pozostaje u " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika());
            if (kontrolerPlanszy != null) kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        } else {
            oczekiwanieNaPotwierdzenieTury = true;
            setGraczOczekujacyNaPotwierdzenie(przeciwnik);
            System.out.println("SILNIK (po wskrzeszeniu): Przekazywanie tury do " + przeciwnik.getProfilUzytkownika().getNazwaUzytkownika());
            if (kontrolerPlanszy != null) kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }

    //Anuluje proces wskrzeszania, jeśli gracz zamknie panel cmentarza bez wyboru. Tura normalnie przechodzi do przeciwnika.
    public void anulujWskrzeszanie(Gracz graczKtoryAnulowal) {
        System.out.println("SILNIK: Gracz " + graczKtoryAnulowal.getProfilUzytkownika().getNazwaUzytkownika() + " anulował wskrzeszanie.");
        resetStanuOczekiwaniaNaPolozenieWskrzeszonej();
        finalizujTurePoNieudanymWskrzeszeniu(graczKtoryAnulowal);
    }

    //Metoda pomocnicza do finalizowania tury, gdy proces wskrzeszania karty został anulowany lub zakończył się niepowodzeniem.
    private void finalizujTurePoNieudanymWskrzeszeniu(Gracz graczKtoryProbowaWskrzesic) {
        System.out.println("SILNIK: Finalizowanie tury po nieudanym/anulowanym wskrzeszeniu przez " + graczKtoryProbowaWskrzesic.getProfilUzytkownika().getNazwaUzytkownika());
        przeliczWszystkiePunktyNaPlanszy();
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

    //Metoda pomocnicza zwracająca obiekt PlanszaGracza dla podanego gracza.
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

    //Aktywuje umiejętność "Braterstwo" (Muster), wyszukując w talii i ręce gracza inne karty o tej samej nazwie i zagrywając je automatycznie.
    private void aktywujBraterstwo(Gracz graczZagrywajacy, Karta kartaInicjujaca, RzadPlanszy rzadDocelowy) {
        String grupaIDKartyInicjujacej = kartaInicjujaca.getGrupaBraterstwa();
        if (grupaIDKartyInicjujacej == null || grupaIDKartyInicjujacej.trim().isEmpty()) {
            System.out.println("    > Karta " + kartaInicjujaca.getNazwa() + " ma umiejętność Braterstwo, ale brak zdefiniowanej 'grupaBraterstwa'.");
            return;
        }
        System.out.println("    Aktywacja Braterstwa dla grupy: '" + grupaIDKartyInicjujacej + "' (karta inicjująca: " + kartaInicjujaca.getNazwa() + ")");
        Iterator<Karta> iteratorReki = graczZagrywajacy.getReka().iterator();
        List<Karta> kartyDoDodaniaZreki = new ArrayList<>();
        while (iteratorReki.hasNext()) {
            Karta kartaWRęce = iteratorReki.next();
            if (grupaIDKartyInicjujacej.equals(kartaWRęce.getGrupaBraterstwa()) &&
                    kartaWRęce.getId() != kartaInicjujaca.getId() &&
                    kartaWRęce.getTyp() != TypKartyEnum.BOHATER) {

                kartyDoDodaniaZreki.add(kartaWRęce);
            }
        }
        for (Karta kartaDoWezwania : kartyDoDodaniaZreki) {
            graczZagrywajacy.getReka().remove(kartaDoWezwania);
            rzadDocelowy.dodajKarteJednostki(kartaDoWezwania);
            System.out.println("    > Wezwano z ręki: " + kartaDoWezwania.getNazwa() + " (ID: " + kartaDoWezwania.getId() + ")");
        }
        Iterator<Karta> iteratorTalii = graczZagrywajacy.getTaliaDoGry().iterator();
        while (iteratorTalii.hasNext()) {
            Karta kartaWTalii = iteratorTalii.next();
            if (grupaIDKartyInicjujacej.equals(kartaWTalii.getGrupaBraterstwa()) &&
                    kartaWTalii.getTyp() != TypKartyEnum.BOHATER) {

                iteratorTalii.remove();
                rzadDocelowy.dodajKarteJednostki(kartaWTalii);
                System.out.println("    > Wezwano z talii: " + kartaWTalii.getNazwa() + " (ID: " + kartaWTalii.getId() + ")");
            }
        }
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
        if (reka == null) {
            reka = new ArrayList<>();
            gracz.setReka(reka);
        }

        int dociagnieto = 0;
        for (int i = 0; i < ilosc; i++) {
            if (!talia.isEmpty()) {
                reka.add(talia.remove(0));
                dociagnieto++;
            } else {
                System.out.println("SILNIK OSTRZEŻENIE: Talia gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                        " jest pusta. Dociągnięto " + dociagnieto + " z " + ilosc + " kart po zagrywce.");
                break;
            }
        }
        System.out.println("SILNIK DEBUG (po zagrywce): Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                " dociągnął " + dociagnieto + " kart(y). Rozmiar ręki: " + reka.size() +
                ". W talii pozostało: " + talia.size());
    }

    //Metoda wywoływana przez kontroler po zniknięciu panelu wyniku rundy.
    public void przejdzDoNastepnegoEtapuPoWynikuRundy() {
        System.out.println("[SILNIK] Kontynuacja po wyświetleniu panelu wyniku rundy.");
        przeniesWszystkieKartyZPlanszNaCmentarze();
        if (aktualnyStanRundy != null) {
            aktualnyStanRundy.wyczyscPogode();
            System.out.println("[SILNIK] Aktywne karty pogody zostały usunięte z planszy (jeśli jakieś były).");
        }
        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
        }
        boolean g1Przegral = (gracz1 != null && gracz1.getWygraneRundy() <= 0);
        boolean g2Przegral = (gracz2 != null && gracz2.getWygraneRundy() <= 0);
        boolean maxRundOsiagniety = (numerRundyGry >= 3);

        if (g1Przegral || g2Przegral || maxRundOsiagniety) {
            System.out.println("[SILNIK] Warunki końca gry spełnione. Kończenie gry.");
            zakonczGre();
        } else {
            System.out.println("[SILNIK] Rozpoczynanie nowej rundy (po etapie pośrednim).");
            rozpocznijNowaRunde();
        }
    }

    //Metoda wywoływana przez kontroler, gdy gracz potwierdzi przejęcie tury.
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
        graczOczekujacyNaPotwierdzenie = null;

        System.out.println("[SILNIK] Po potwierdzeniu, ustalony gracz aktualnej tury: " +
                (graczAktualnejTury != null ? graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                " (Obiekt ID: " + System.identityHashCode(graczAktualnejTury) + ")");
        if (this.kartaPotworaDoZachowaniaNaKolejnaRunde != null &&
                this.graczPotworowZkartaDoZachowania != null &&
                this.rzadKartyPotworaDoZachowania != null) {

            if (this.graczPotworowZkartaDoZachowania.getOdrzucone() != null && getPlanszaGracza(this.graczPotworowZkartaDoZachowania) != null) {
                String nazwaGraczaPotwora = this.graczPotworowZkartaDoZachowania.getProfilUzytkownika().getNazwaUzytkownika();
                String nazwaKartyPotwora = this.kartaPotworaDoZachowaniaNaKolejnaRunde.getNazwa();

                System.out.println("SILNIK (potwierdzPrzejecieTury): Potwory - Aktywacja umiejętności frakcji. Przywracanie karty '" +
                        nazwaKartyPotwora + "' gracza " + nazwaGraczaPotwora);

                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.pokazPanelUmiejetnosciPotworow(nazwaKartyPotwora);
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
                if (planszaPotwora != null) {
                    planszaPotwora.przeliczLacznaSumePunktow();
                }

            } else {
                System.err.println("SILNIK (potwierdzPrzejecieTury): Potwory - Nie można przywrócić karty, cmentarz lub plansza gracza Potworów (" +
                        (this.graczPotworowZkartaDoZachowania != null ? this.graczPotworowZkartaDoZachowania.getProfilUzytkownika().getNazwaUzytkownika() : "null") +
                        ") jest null.");
            }
            this.kartaPotworaDoZachowaniaNaKolejnaRunde = null;
            this.graczPotworowZkartaDoZachowania = null;
            this.rzadKartyPotworaDoZachowania = null;
        }
        if (graczAktualnejTury != null && checkAndPerformAutoPassIfHandEmpty(graczAktualnejTury)) {
            System.out.println("[SILNIK (potwierdzPrzejecieTury)] Gracz " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() + " automatycznie spasował na początku tury (brak kart).");
            return;
        }

        if (gracz1.isCzySpasowalWRundzie() && gracz2.isCzySpasowalWRundzie()) {
            if (!czyGraZakonczona) {
                System.out.println("[SILNIK (potwierdzPrzejecieTury)] Obaj spasowali, kończenie rundy.");
                zakonczRunde();
            }
            return;
        }
        if (graczAktualnejTury != null && graczAktualnejTury.isCzySpasowalWRundzie()) {
            Gracz aktywnyPrzeciwnik = (graczAktualnejTury == gracz1) ? gracz2 : gracz1;
            if (aktywnyPrzeciwnik != null && !aktywnyPrzeciwnik.isCzySpasowalWRundzie()) {
                System.out.println("[SILNIK] Gracz " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() +
                        " potwierdził, ale jest już spasowany. Przeciwnik " + aktywnyPrzeciwnik.getProfilUzytkownika().getNazwaUzytkownika() +
                        " jest aktywny. Tura przechodzi do przeciwnika, pokazując panel.");
                this.graczOczekujacyNaPotwierdzenie = aktywnyPrzeciwnik;
                oczekiwanieNaPotwierdzenieTury = true;

                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.odswiezCalakolwiekPlansze();
                    kontrolerPlanszy.pokazPanelPrzejeciaTury(this.graczOczekujacyNaPotwierdzenie);
                }
                return;
            }
        }

        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Tura gracza: " + (graczAktualnejTury != null ? graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() : "BŁĄD"), false);
            kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
        }
    }


    //Przetwarza akcję pasowania przez gracza.
    public void pasuj(Gracz graczKtorySpasowal) {
        if (czyGraZakonczona || oczekiwanieNaPotwierdzenieTury || graczKtorySpasowal != graczAktualnejTury || graczKtorySpasowal.isCzySpasowalWRundzie()) {
            System.err.println("[SILNIK] Nie można spasować w tym momencie.");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Nie można teraz spasować.", true);
            return;
        }

        graczKtorySpasowal.setCzySpasowalWRundzie(true);
        System.out.println("[SILNIK] Gracz " + graczKtorySpasowal.getProfilUzytkownika().getNazwaUzytkownika() + " spasował.");

        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.pokazPanelInfoPas(graczKtorySpasowal);
        } else {
            kontynuujPoWyswietleniuInfoOPasie();
        }
    }

    //Metoda wywoływana przez kontroler po zniknięciu panelu informacyjnego o pasie. Sprawdza, czy obaj gracze spasowali (kończąc rundę), czy też tura przechodzi do aktywnego przeciwnika.
    public void kontynuujPoWyswietleniuInfoOPasie() {
        Gracz spasowanyGracz = graczAktualnejTury;
        Gracz drugiGracz = (spasowanyGracz == gracz1) ? gracz2 : gracz1;

        System.out.println("[SILNIK] Kontynuacja po informacji o pasie gracza: " + spasowanyGracz.getProfilUzytkownika().getNazwaUzytkownika());

        if (drugiGracz.isCzySpasowalWRundzie()) {
            System.out.println("[SILNIK] Obaj gracze spasowali. Kończenie rundy.");
            zakonczRunde();
        } else {
            graczAktualnejTury = drugiGracz;
            oczekiwanieNaPotwierdzenieTury = false;
            setGraczOczekujacyNaPotwierdzenie(null);

            System.out.println("[SILNIK] Po pasie, tura przechodzi do (i pozostaje u): " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika());
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.odswiezCalakolwiekPlansze();
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Tura gracza: " + graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika(), false);
                kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
            }
        }
    }

    //Metoda pomocnicza, która zleca przeliczenie sumy punktów dla wszystkich rzędów na obu planszach graczy.
    private void przeliczWszystkiePunktyNaPlanszy() {
        if (aktualnyStanRundy != null) {
            if (aktualnyStanRundy.getPlanszaGracza1() != null) {
                aktualnyStanRundy.getPlanszaGracza1().przeliczLacznaSumePunktow();
            }
            if (aktualnyStanRundy.getPlanszaGracza2() != null) {
                aktualnyStanRundy.getPlanszaGracza2().przeliczLacznaSumePunktow();
            }
        }
    }

    //Kończy bieżącą rundę, gdy obaj gracze spasują. Oblicza punkty, przyznaje zwycięstwo, aktualizuje 'życia' graczy i aktywuje zdolności frakcyjne po rundzie (Potwory, Królestwa Północy).
    private void zakonczRunde() {
        if (czyGraZakonczona) return;
        System.out.println("SILNIK: Rozpoczynanie procesu zakończenia Rundy " + numerRundyGry);
        oczekiwanieNaPotwierdzenieTury = false;
        setGraczOczekujacyNaPotwierdzenie(null);

        przeliczWszystkiePunktyNaPlanszy();
        if (aktualnyStanRundy == null || aktualnyStanRundy.getPlanszaGracza1() == null || aktualnyStanRundy.getPlanszaGracza2() == null ||
                gracz1 == null || gracz1.getProfilUzytkownika() == null || gracz2 == null || gracz2.getProfilUzytkownika() == null) {
            System.err.println("Błąd krytyczny w zakonczRunde: Brak kluczowych obiektów stanu gry lub graczy/profili.");
            zakonczGre();
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
        } else {
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

            if (!nilfgaardPrzelamalRemis) {
                gracz1.stracZycie();
                gracz2.stracZycie();
                wiadomoscWynikuRundy = "Remis! Obydwaj gracze tracą żeton życia.";
            }
        }
        graczOstatnioWygralRunde = zwyciezcaRundy;

        if (this.historiaWynikowRund != null && this.historiaWynikowRund.size() < 3) {
            String podsumowanieRundy = "Runda " + numerRundyGry + ": " + wiadomoscWynikuRundy;
            this.historiaWynikowRund.add(podsumowanieRundy);
        }

        System.out.println("[SILNIK] Wynik rundy ustalony: " + wiadomoscWynikuRundy);
        this.kartaPotworaDoZachowaniaNaKolejnaRunde = null;
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
            PlanszaGracza planszaPotwora = getPlanszaGracza(graczKtoryMoglGracPotworami);

            if (planszaPotwora != null) {
                for (TypRzeduEnum typRzedu : new TypRzeduEnum[]{TypRzeduEnum.PIECHOTA, TypRzeduEnum.STRZELECKIE, TypRzeduEnum.OBLEZENIE}) {
                    RzadPlanszy rzad = planszaPotwora.getRzad(typRzedu);
                    if (rzad != null) {
                        for (Karta k : rzad.getKartyJednostekWRzedzie()) {
                            if (k.getTyp() == TypKartyEnum.JEDNOSTKA) {
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


        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            boolean czyRemisPunktowy = (punktyG1 == punktyG2);
            kontrolerPlanszy.pokazPanelWynikuRundy(wiadomoscWynikuRundy, zwyciezcaRundy, czyRemisPunktowy);
        } else {
            przejdzDoNastepnegoEtapuPoWynikuRundy();
        }
        if (zwyciezcaRundy != null && zwyciezcaRundy.getWybranaFrakcja() == FrakcjaEnum.KROLESTWA_POLNOCY) {
            System.out.println("SILNIK: " + zwyciezcaRundy.getProfilUzytkownika().getNazwaUzytkownika() +
                    " (Królestwa Północy) wygrał rundę. Aktywacja zdolności frakcji.");
            dociagnijKarteZeZdolnosci(zwyciezcaRundy, 1, "Królestwa Północy");
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy(
                        zwyciezcaRundy.getProfilUzytkownika().getNazwaUzytkownika() +
                                " dobiera kartę dzięki zdolności Królestw Północy!", false
                );
            }
        }
    }

    //Metoda pomocnicza wywoływana na końcu rundy. Przenosi wszystkie karty jednostek i karty specjalne (np. Róg Dowódcy) z plansz obu graczy na ich cmentarze.
    private void przeniesWszystkieKartyZPlanszNaCmentarze() {
        if (aktualnyStanRundy == null || gracz1 == null || gracz2 == null) {
            System.err.println("[SILNIK] Błąd: Nie można przenieść kart na cmentarze - brak stanu rundy lub graczy.");
            return;
        }
        System.out.println("[SILNIK] Przenoszenie kart z plansz na cmentarze.");

        PlanszaGracza planszaG1 = aktualnyStanRundy.getPlanszaGracza1();
        if (planszaG1 != null) {
            List<Karta> kartyG1 = planszaG1.wyczyscPlanszeIZwrocKarty();
            gracz1.getOdrzucone().addAll(kartyG1);
            System.out.println("  > Z planszy G1 przeniesiono na cmentarz: " + kartyG1.size() + " kart jednostek.");
            przeniesRogDowodcyNaCmentarz(planszaG1.getRzadPiechoty(), gracz1.getOdrzucone());
            przeniesRogDowodcyNaCmentarz(planszaG1.getRzadStrzelecki(), gracz1.getOdrzucone());
            przeniesRogDowodcyNaCmentarz(planszaG1.getRzadOblezenia(), gracz1.getOdrzucone());
        }

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
            if (rog.getTyp() == TypKartyEnum.SPECJALNA && rog.getNazwa().toLowerCase().contains("róg dowódcy")) {
                cmentarz.add(rog);
                System.out.println("    > Róg Dowódcy z rzędu " + rzad.getTypRzedu() + " przeniesiony na cmentarz.");
            }
            rzad.setKartaRoguDowodcy(null);
        }
    }

    //Definitywnie kończy partię. Ustala ostatecznego zwycięzcę na podstawie liczby pozostałych 'żyć' i zleca kontrolerowi wyświetlenie panelu końca gry.
    private void zakonczGre() {
        if (czyGraZakonczona) return;
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

        if (zyciaPozostaleG1 <= 0 && zyciaPozostaleG2 <= 0) {
            czyRemisWGrze = true;
        } else if (zyciaPozostaleG2 <= 0) {
            zwyciezcaGry = gracz1;
        } else if (zyciaPozostaleG1 <= 0) {
            zwyciezcaGry = gracz2;
        } else {
            if (zyciaPozostaleG1 > zyciaPozostaleG2) {
                zwyciezcaGry = gracz1;
            } else if (zyciaPozostaleG2 > zyciaPozostaleG1) {
                zwyciezcaGry = gracz2;
            } else {
                czyRemisWGrze = true;
            }
        }

        System.out.println("[SILNIK] Zwycięzca gry: " +
                (zwyciezcaGry != null ? zwyciezcaGry.getProfilUzytkownika().getNazwaUzytkownika() : (czyRemisWGrze ? "REMIS" : "Nieustalony")));

        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.pokazPanelKoncaGry(zwyciezcaGry, czyRemisWGrze, historiaWynikowRund);
        }
    }

    //Przetwarza użycie zdolności karty dowódcy. Zawiera logikę dla wszystkich możliwych zdolności dowódców w grze.
    public void uzyjZdolnosciDowodcy(Gracz gracz) {
        if (gracz == null || gracz.getKartaDowodcy() == null) {
            System.err.println("SILNIK: Próba użycia zdolności przez gracza null lub gracza bez karty dowódcy.");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd: Brak danych dowódcy.", true);
            return;
        }


        if (gracz.isZdolnoscDowodcyZablokowana()) {
            System.out.println("SILNIK: Próba użycia zablokowanej zdolności dowódcy przez gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika());
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Twoja zdolność dowódcy jest zablokowana!", true);
            }
            return;
        }

        if (czyGraZakonczona || oczekiwanieNaPotwierdzenieTury || gracz != graczAktualnejTury) {
            System.out.println("Nie można użyć zdolności dowódcy - zły stan gry ("+ (czyGraZakonczona ? "Gra zakończona" : "") + (oczekiwanieNaPotwierdzenieTury ? "OczekiwaniePotwierdzenie" : "") +") lub nie tura gracza (" + (graczAktualnejTury != null ? graczAktualnejTury.getProfilUzytkownika().getNazwaUzytkownika() : "null") +").");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Nie można teraz użyć zdolności dowódcy.", true);
            return;
        }

        if (oczekiwanieNaWyborRzeduPoWskrzeszeniu || oczekiwanieNaWyborCeluDlaManekina || oczekiwanieNaWyborKartyDlaEmhyra) {
            System.err.println("SILNIK: Próba użycia zdolności dowódcy podczas oczekiwania na inną akcję.");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Dokończ poprzednią akcję.", true);
            return;
        }

        Karta kartaDowodcy = gracz.getKartaDowodcy();
        int idDowodcy = kartaDowodcy.getId();

        boolean czyToPasywnaZdolnoscInformacyjna = (idDowodcy == 71 || idDowodcy == 73);
        boolean czyToAktywnaZdolnoscDoZuzycia = !czyToPasywnaZdolnoscInformacyjna;
        boolean czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;


        if (czyToAktywnaZdolnoscDoZuzycia && gracz.isZdolnoscDowodcyUzyta()) {
            System.out.println("Zdolność dowódcy " + kartaDowodcy.getNazwa() + " została już użyta.");
            if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Zdolność dowódcy już użyta.", false);
            return;
        }

        System.out.println(gracz.getProfilUzytkownika().getNazwaUzytkownika() + " używa zdolności dowódcy: " + kartaDowodcy.getNazwa());

        boolean czyZdolnoscZostalaFaktycznieUzyta = false;

        if (czyToAktywnaZdolnoscDoZuzycia && idDowodcy != 69) {
            gracz.setZdolnoscDowodcyUzyta(true);
        }

        if (kontrolerPlanszy != null && czyToAktywnaZdolnoscDoZuzycia) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
        }

        if (idDowodcy == 69) {
            Gracz przeciwnik69 = (gracz == gracz1) ? gracz2 : gracz1;
            if (przeciwnik69 == null || przeciwnik69.getOdrzucone() == null || przeciwnik69.getOdrzucone().isEmpty()) {
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Cmentarz przeciwnika jest pusty. Zdolność Emhyra nie ma celu.", false);
                }
                gracz.setZdolnoscDowodcyUzyta(true);
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;

            } else {
                this.oczekiwanieNaWyborKartyDlaEmhyra = true;
                this.graczAktywujacyEmhyra = gracz;
                this.przeciwnikDlaEmhyra = przeciwnik69;
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Emhyr: Wybierz kartę z cmentarza przeciwnika.", false);

                    kontrolerPlanszy.pokazPanelCmentarza(przeciwnik69, "Cmentarz Przeciwnika - Wybierz Kartę", KontrolerPlanszyGry.TrybPaneluCmentarzaKontekst.EMHYR_PAN_POLUDNIA);
                }
                return;
            }
        } else if (idDowodcy == 70) {
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
        } else if (idDowodcy == 71 || idDowodcy == 73 || idDowodcy == 413) {
            czyToAktywnaZdolnoscDoZuzycia = false;
            if (kontrolerPlanszy != null) {
                String komunikatPasywny = "";
                if (idDowodcy == 71) komunikatPasywny = ": Umiejętność pasywna (losowe wskrzeszenie z Medykiem).";
                else if (idDowodcy == 73) komunikatPasywny = ": Umiejętność pasywna, blokuje zdolność przeciwnika.";
                else if (idDowodcy == 413) komunikatPasywny = ": Umiejętność pasywna - podwaja siłę Szpiegów.";
                else if (idDowodcy == 489) komunikatPasywny = ": Umiejętność pasywna - jedna dodatkowa karta na początku gry.";
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + komunikatPasywny, false);
                kontrolerPlanszy.uaktywnijInterfejsDlaTury(graczAktualnejTury == this.gracz1);
            }
            return;
        } else if (idDowodcy == 72) {
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
        } else if (idDowodcy == 331) {
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
        } else if (idDowodcy == 332) {
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
        } else if (idDowodcy == 333) {
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
        } else if (idDowodcy == 334) {
            if (aktualnyStanRundy != null) {
                aktualnyStanRundy.wyczyscPogode();
                if (kontrolerPlanszy != null) kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Foltest (Dowódca Północy): Czyste niebo!", false);
                czyZdolnoscZostalaFaktycznieUzyta = true;
            } else {
                System.err.println("SILNIK: Błąd - aktualnyStanRundy jest null przy Folteście (DP).");
            }
        } else if (idDowodcy == 335) {
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
        }else if (idDowodcy == 416) {
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
                        if (!nazwaKartyLower.contains("manekin") &&
                                !nazwaKartyLower.contains("róg dowódcy") &&
                                !nazwaKartyLower.contains("pożoga")) {
                            kartyPogodyWTalii.add(k);
                        }
                    }
                }
            }

            if (kartyPogodyWTalii.isEmpty()) {
                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Brak kart pogody w Twojej talii!", false);
                }
                gracz.setZdolnoscDowodcyUzyta(true);
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
            } else {
                this.oczekiwanieNaWyborKartyPogodyPrzezEredina = true;
                this.graczAktywujacyEredina336 = gracz;
                this.dostepneKartyPogodyDlaEredina = new ArrayList<>(kartyPogodyWTalii);

                if (kontrolerPlanszy != null) {
                    kontrolerPlanszy.pokazPanelWyboruKartyPogodyDlaEredina(this.dostepneKartyPogodyDlaEredina);
                }
                return;
            }
        }else if (idDowodcy == 336) {
            if (gracz.getPlanszaGry() != null) {
                RzadPlanszy rzadPiechotyGracza = gracz.getPlanszaGry().getRzadPiechoty();
                if (rzadPiechotyGracza != null) {
                    if (rzadPiechotyGracza.getKartaRoguDowodcy() == null && !rzadPiechotyGracza.isWzmocnieniePrzezDowodceAktywne()) {
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
            gracz.setZdolnoscDowodcyUzyta(true);

        }else if (idDowodcy == 414) {
            if (gracz.getReka().size() < 2) {
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Potrzebujesz co najmniej 2 kart w ręce, aby użyć tej zdolności!", true);
                }
                gracz.setZdolnoscDowodcyUzyta(true);
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
            } else if (gracz.getTaliaDoGry().isEmpty()) {
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Twoja talia jest pusta, nie możesz dobrać karty!", true);
                }
                gracz.setZdolnoscDowodcyUzyta(true);
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
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
                return;
            }
        }else if (idDowodcy == 415) {
            if (gracz.getOdrzucone() == null || gracz.getOdrzucone().isEmpty()) {
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Twój cmentarz jest pusty!", true);
                }
                gracz.setZdolnoscDowodcyUzyta(true);
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
            } else {
                this.oczekiwanieNaWyborKartyZCmentarzaPrzezEredina415 = true;
                this.graczAktywujacyEredina415 = gracz;
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.pokazPanelCmentarzaDlaEredina415(gracz, "Eredin: Wybierz kartę z cmentarza");
                }
                return;
            }
        } else if (idDowodcy == 490) {

            Karta trzaskajacyMrozDoZagrnia = null;
            Iterator<Karta> iteratorTalii = gracz.getTaliaDoGry().iterator();
            while (iteratorTalii.hasNext()) {
                Karta kartaZTali = iteratorTalii.next();
                if (kartaZTali.getTyp() == TypKartyEnum.SPECJALNA &&
                        (kartaZTali.getNazwa().equalsIgnoreCase("Trzaskający mróz") ||
                                kartaZTali.getNazwa().toLowerCase().contains("mróz"))) {
                    trzaskajacyMrozDoZagrnia = kartaZTali;
                    iteratorTalii.remove();
                    break;
                }
            }

            if (trzaskajacyMrozDoZagrnia != null) {
                if (aktualnyStanRundy != null) {
                    aktualnyStanRundy.dodajKartePogody(trzaskajacyMrozDoZagrnia);
                    System.out.println("SILNIK: Francesca (490) zagrała '" + trzaskajacyMrozDoZagrnia.getNazwa() + "' z talii.");
                    if (this.kontrolerPlanszy != null) {
                        this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": 'Trzaskający mróz' zagrany z talii.", false);
                    }
                    czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = true;
                } else {
                    System.err.println("SILNIK: Błąd krytyczny - aktualnyStanRundy jest null przy zagrywaniu Mrozu przez Francescę 490.");
                    gracz.getTaliaDoGry().add(trzaskajacyMrozDoZagrnia);
                    czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
                }
            } else {
                System.out.println("SILNIK: Francesca (490) - nie znaleziono 'Trzaskający mróz' w talii.");
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(kartaDowodcy.getNazwa() + ": Nie znaleziono 'Trzaskający mróz' w Twojej talii.", false);
                }
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = false;
            }
            gracz.setZdolnoscDowodcyUzyta(true);
        }else if (idDowodcy == 491) {
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

                            if (obecnyRzadObiekt.usunKarteJednostki(kartaZreczna) != null) {
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
        }else if (idDowodcy == 492) {
            czyToAktywnaZdolnoscDoZuzycia = true;

            Gracz przeciwnik = (gracz == gracz1) ? gracz2 : gracz1;
            RzadPlanszy rzadBliskiegoStarciaPrzeciwnika = null;
            int sumaPunktowRzeduPrzeciwnika = 0;

            if (przeciwnik != null && przeciwnik.getPlanszaGry() != null) {
                rzadBliskiegoStarciaPrzeciwnika = przeciwnik.getPlanszaGry().getRzadPiechoty();
                if (rzadBliskiegoStarciaPrzeciwnika != null) {
                    sumaPunktowRzeduPrzeciwnika = rzadBliskiegoStarciaPrzeciwnika.getSumaPunktowWRzedzie();
                }
            }

            if (rzadBliskiegoStarciaPrzeciwnika != null && sumaPunktowRzeduPrzeciwnika >= 10) {
                System.out.println("SILNIK: Francesca (492) - Rząd bliskiego starcia przeciwnika ma " + sumaPunktowRzeduPrzeciwnika + " pkt (>=10). Aktywacja efektu.");
                boolean czyCosZniszczono = zniszczNajsilniejszeJednostkiNaRzedzie(przeciwnik, TypRzeduEnum.PIECHOTA, "Zdolność Franceski (492)", null);
                czyZdolnoscZostalaFaktycznieUzytaPrzezDowodce = czyCosZniszczono;
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
            gracz.setZdolnoscDowodcyUzyta(true);
        }else if (idDowodcy == 493) {
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
        }
        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            if (czyToAktywnaZdolnoscDoZuzycia &&
                    gracz.isZdolnoscDowodcyUzyta() &&
                    !oczekiwanieNaWyborKartyDlaEmhyra &&
                    !oczekiwanieNaWyborCeluDlaManekina &&
                    !oczekiwanieNaWyborRzeduPoWskrzeszeniu) {
                if (czyZdolnoscZostalaFaktycznieUzyta || idDowodcy == 72) {
                    kontrolerPlanszy.wyswietlKomunikatNaPlanszy(gracz.getProfilUzytkownika().getNazwaUzytkownika() + " użył zdolności: " + kartaDowodcy.getNazwa() + "!", false);
                } else if (!czyZdolnoscZostalaFaktycznieUzyta && idDowodcy != 69) {
                }
            }
        }

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
    }

    //Metoda pomocnicza używana przez zdolność Franceski Findabair. Oblicza, jaką siłę miałaby dana karta, gdyby znalazła się w określonym rzędzie, uwzględniając wszystkie aktywne efekty (pogoda, róg, morale).
    private int obliczPotencjalnaSileKartyWRzedzie(Karta kartaDoSprawdzenia, TypRzeduEnum typRzeduDocelowego, Gracz wlascicielKarty) {
        if (kartaDoSprawdzenia.getTyp() == TypKartyEnum.BOHATER) {
            int silaBohatera = kartaDoSprawdzenia.getPunktySily();
            boolean czySzpieg = kartaDoSprawdzenia.getUmiejetnosc() != null && kartaDoSprawdzenia.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
            if (czySzpieg && aktualnyStanRundy.isEredin413Active()) {
                silaBohatera *= 2;
            }
            return silaBohatera;
        }

        int efektywnaSila = kartaDoSprawdzenia.getPunktySily();

        boolean czySzpiegJednostka = kartaDoSprawdzenia.getUmiejetnosc() != null && kartaDoSprawdzenia.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
        if (czySzpiegJednostka && aktualnyStanRundy.isEredin413Active()) {
            efektywnaSila *= 2;
        }
        PlanszaGracza planszaWlasciciela = getPlanszaGracza(wlascicielKarty);
        if (planszaWlasciciela == null) return efektywnaSila;

        RzadPlanszy rzadDocelowyFaktyczny = planszaWlasciciela.getRzad(typRzeduDocelowego);
        if (rzadDocelowyFaktyczny == null) return efektywnaSila;

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
            efektywnaSila = 1;
        }

        int bonusMorale = 0;
        for (Karta kInRow : rzadDocelowyFaktyczny.getKartyJednostekWRzedzie()) {
            if (kInRow.getUmiejetnosc() != null && kInRow.getUmiejetnosc().equalsIgnoreCase("Wysokie morale") &&
                    kInRow.getTyp() != TypKartyEnum.BOHATER) {
                bonusMorale++;
            }
        }
        if (!pogodaAktywnaDlaKarty) {
            efektywnaSila += bonusMorale;
        }

        if (rzadDocelowyFaktyczny.getKartaRoguDowodcy() != null || rzadDocelowyFaktyczny.isWzmocnieniePrzezDowodceAktywne()) {
            efektywnaSila *= 2;
        }

        return efektywnaSila;
    }

    //Przetwarza pierwszy etap zdolności Eredina (ID 414), potwierdzając dwie karty z ręki wybrane do odrzucenia i inicjując drugi etap - wybór karty z talii.
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
                this.kontrolerPlanszy.pokazPanelWyboruKartDoOdrzuceniaEredin414(new ArrayList<>(graczAktywujacyEredina414.getReka()));
            }
            return;
        }

        this.kartyWybraneDoOdrzuceniaEredin414.clear();
        this.kartyWybraneDoOdrzuceniaEredin414.addAll(wybraneKartyDoOdrzucenia);
        this.oczekiwanieNaWyborKartDoOdrzuceniaEredin414 = false;
        this.oczekiwanieNaWyborKartyZTaliiPrzezEredin414 = true;

        System.out.println("SILNIK: Eredin 414 - Wybrano karty do odrzucenia: " +
                wybraneKartyDoOdrzucenia.stream().map(Karta::getNazwa).collect(Collectors.joining(", ")));

        if (graczAktywujacyEredina414.getTaliaDoGry().isEmpty()) {
            if (this.kontrolerPlanszy != null) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Talia jest pusta! Nie można dobrać karty.", true);
            }
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

    //Przetwarza akcję wyboru karty z cmentarza dla zdolności Eredina (ID 415), przenosząc wybraną kartę na rękę gracza.
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
            if (this.kontrolerPlanszy != null) {
                this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Błąd przy pobieraniu karty z cmentarza.", true);
            }
        }

        aktywnyGracz.setZdolnoscDowodcyUzyta(true);
        resetStanuEredina415();

        if (this.kontrolerPlanszy != null) {
            this.kontrolerPlanszy.odswiezCalakolwiekPlansze();
        }
        koniecTuryPoAktywnejZdolnosci(aktywnyGracz, true);
    }

    //Anuluje użycie zdolności Eredina (ID 414). Zdolność zostaje zużyta, a tura przechodzi do przeciwnika.
    public void anulujZdolnoscEredina415(boolean zPowoduBledu) {
        if (graczAktywujacyEredina415 == null && !oczekiwanieNaWyborKartyZCmentarzaPrzezEredina415) {
            resetStanuEredina415();
            return;
        }

        Gracz aktywnyGracz = graczAktywujacyEredina415;
        if (aktywnyGracz == null) aktywnyGracz = graczAktualnejTury;
        if(aktywnyGracz == null) {
            System.err.println("SILNIK: Nie można anulować zdolności Eredina 415, brak informacji o graczu.");
            resetStanuEredina415();
            return;
        }


        System.out.println("SILNIK: Anulowano zdolność Eredina 415 dla gracza " + aktywnyGracz.getProfilUzytkownika().getNazwaUzytkownika() +
                (zPowoduBledu ? " z powodu błędu." : "."));

        aktywnyGracz.setZdolnoscDowodcyUzyta(true);
        resetStanuEredina415();

        if (this.kontrolerPlanszy != null) {
            this.kontrolerPlanszy.odswiezCalakolwiekPlansze();
            String komunikat = aktywnyGracz.getKartaDowodcy() != null ? aktywnyGracz.getKartaDowodcy().getNazwa() : "Zdolność dowódcy";
            this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(komunikat +
                    (zPowoduBledu ? ": wystąpił błąd." : ": wybór anulowany."), false);
        }
        koniecTuryPoAktywnejZdolnosci(aktywnyGracz, false);
    }

    //Przeszukuje planszę gracza w poszukiwaniu prawidłowych celów dla karty "Manekin do ćwiczeń" (jednostki niebędące bohaterami).
    private List<Karta> znajdzPoprawneCeleDlaManekina(Gracz gracz) {
        List<Karta> cele = new ArrayList<>();
        if (gracz == null || gracz.getPlanszaGry() == null) {
            return cele;
        }
        PlanszaGracza planszaGracza = getPlanszaGracza(gracz);
        if (planszaGracza == null) return cele;

        for (TypRzeduEnum typRzedu : new TypRzeduEnum[]{TypRzeduEnum.PIECHOTA, TypRzeduEnum.STRZELECKIE, TypRzeduEnum.OBLEZENIE}) {
            RzadPlanszy rzad = planszaGracza.getRzad(typRzedu);
            if (rzad != null) {
                for (Karta k : rzad.getKartyJednostekWRzedzie()) {
                    if (k.getTyp() != TypKartyEnum.BOHATER) {
                        cele.add(k);
                    }
                }
            }
        }
        return cele;
    }

    //Przetwarza drugi etap zdolności Eredina (ID 414). Odrzuca wcześniej wybrane karty na cmentarz i dodaje do ręki kartę wybraną z talii.
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
            resetStanuEredina414();
            return;
        }

        Gracz aktywnyGracz = graczAktywujacyEredina414;
        if (aktywnyGracz == null) {
            aktywnyGracz = graczAktualnejTury;
            if(aktywnyGracz == null) {
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

    //Przetwarza wybór karty pogody w ramach zdolności dowódcy Eredina, zagrywając wybraną kartę z talii na planszę.
    public void wykonajWyborPogodyPrzezEredina(Karta wybranaKartaPogody) {
        if (!oczekiwanieNaWyborKartyPogodyPrzezEredina || graczAktywujacyEredina336 == null || wybranaKartaPogody == null) {
            System.err.println("SILNIK: Nieprawidłowy stan do wykonania wyboru pogody przez Eredina.");
            if (graczAktywujacyEredina336 != null) graczAktywujacyEredina336.setZdolnoscDowodcyUzyta(true);
            anulujWyborPogodyPrzezEredina(true);
            return;
        }

        if (dostepneKartyPogodyDlaEredina == null || !dostepneKartyPogodyDlaEredina.contains(wybranaKartaPogody)) {
            System.err.println("SILNIK: Wybrana karta pogody (" + wybranaKartaPogody.getNazwa() + ") nie była na liście dostępnych opcji dla Eredina.");
            anulujWyborPogodyPrzezEredina(true);
            return;
        }

        Gracz graczKtoryUzywalZdolnosci = graczAktywujacyEredina336;
        System.out.println("SILNIK: Eredin (" + graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika() +
                ") wybrał kartę pogody: " + wybranaKartaPogody.getNazwa());

        boolean usunietoZTalli = graczKtoryUzywalZdolnosci.getTaliaDoGry().remove(wybranaKartaPogody);
        if (!usunietoZTalli) {
            System.err.println("SILNIK: Błąd krytyczny - nie udało się usunąć wybranej karty pogody (" + wybranaKartaPogody.getNazwa() + ") z talii gracza " + graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika());
        }

        aktualnyStanRundy.dodajKartePogody(wybranaKartaPogody);

        graczKtoryUzywalZdolnosci.setZdolnoscDowodcyUzyta(true);
        System.out.println("SILNIK: Zdolność Eredina (336) użyta. Karta " + wybranaKartaPogody.getNazwa() + " zagrana.");

        resetStanuWyboruPogodyEredina();

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

    //Anuluje proces wyboru karty pogody dla zdolności Eredina. Zdolność zostaje zużyta, a tura przechodzi do przeciwnika.
    public void anulujWyborPogodyPrzezEredina(boolean zPowoduBledu) {
        if (!oczekiwanieNaWyborKartyPogodyPrzezEredina || graczAktywujacyEredina336 == null) {
            resetStanuWyboruPogodyEredina();
            return;
        }

        Gracz graczKtoryUzywalZdolnosci = graczAktywujacyEredina336;
        System.out.println("SILNIK: Anulowano wybór karty pogody dla Eredina (" + graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika() + ")" + (zPowoduBledu ? " z powodu błędu." : "."));

        graczKtoryUzywalZdolnosci.setZdolnoscDowodcyUzyta(true);
        resetStanuWyboruPogodyEredina();

        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
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

    //Metoda pomocnicza resetująca stan związany z użyciem zdolności Eredina (wybór pogody).
    private void resetStanuWyboruPogodyEredina() {
        this.oczekiwanieNaWyborKartyPogodyPrzezEredina = false;
        this.graczAktywujacyEredina336 = null;
        this.dostepneKartyPogodyDlaEredina = null;
    }

    //Metoda pomocnicza realizująca efekt typu "Pożoga" dla konkretnego rzędu. Niszczy najsilniejszą lub najsilniejsze jednostki (nie-bohaterów) w danym rzędzie.
    private boolean zniszczNajsilniejszeJednostkiNaRzedzie(Gracz graczCel, TypRzeduEnum typRzeduCelu, String zrodloEfektu, Karta pominKarte) {
        if (graczCel == null || graczCel.getPlanszaGry() == null) {
            System.err.println("SILNIK: (" + zrodloEfektu + ") - Brak gracza celu lub jego planszy.");
            return false;
        }
        RzadPlanszy rzadCelu = graczCel.getPlanszaGry().getRzad(typRzeduCelu);

        if (rzadCelu == null || rzadCelu.getKartyJednostekWRzedzie().isEmpty()) {
            System.out.println("SILNIK: (" + zrodloEfektu + ") - Rząd " + typRzeduCelu.getNazwaWyswietlana() + " gracza " + graczCel.getProfilUzytkownika().getNazwaUzytkownika() + " jest pusty.");
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
            if (k.getPunktySily() > maxSila) {
                maxSila = k.getPunktySily();
            }
        }

        if (maxSila == 0) {
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
                if (rzadCelu.usunKarteJednostki(k) != null) {
                    graczCel.getOdrzucone().add(k);
                    zniszczoneNazwy.append(k.getNazwa()).append(" (").append(maxSila).append(" pkt), ");
                }
            }
            if (zniszczoneNazwy.length() > 2) {
                zniszczoneNazwy.setLength(zniszczoneNazwy.length() - 2);
                System.out.println("SILNIK: " + zrodloEfektu + " niszczy w rzędzie " + typRzeduCelu.getNazwaWyswietlana() + " gracza " + graczCel.getProfilUzytkownika().getNazwaUzytkownika() + " karty: " + zniszczoneNazwy.toString());
                if (this.kontrolerPlanszy != null) {
                    this.kontrolerPlanszy.wyswietlKomunikatNaPlanszy(zrodloEfektu + " zniszczył: " + zniszczoneNazwy.toString() + "!", false);
                }
                return true;
            } else {
                System.out.println("SILNIK: (" + zrodloEfektu + ") - Nie udało się zniszczyć żadnej z wytypowanych kart (np. błąd usuwania).");
                return false;
            }
        } else {

            System.out.println("SILNIK: (" + zrodloEfektu + ") - Nie znaleziono kart do zniszczenia (oczekiwano, że będą).");
            return false;
        }
    }

    //Aktywuje pasywną zdolność dowódcy Emhyra "Najeźdźca Północy". Po zagraniu Medyka, ta metoda losowo wskrzesza jednostkę z cmentarza gracza.
    private void zastosujEfektLosowegoWskrzeszeniaPrzezNjezdzce(Gracz graczKtoremuWskrzeszamy) {
        System.out.println("SILNIK: Emhyr 'Najeźdźca Północy' - efekt losowego wskrzeszenia dla gracza: " + graczKtoremuWskrzeszamy.getProfilUzytkownika().getNazwaUzytkownika());

        List<Karta> cmentarzGracza = graczKtoremuWskrzeszamy.getOdrzucone();
        List<Karta> mozliweKartyDoLosowegoWskrzeszenia = cmentarzGracza.stream()
                .filter(k -> k.getTyp() == TypKartyEnum.JEDNOSTKA)
                .collect(Collectors.toList());

        if (mozliweKartyDoLosowegoWskrzeszenia.isEmpty()) {
            System.out.println("SILNIK: Emhyr 'Najeźdźca Północy' - brak kwalifikujących się jednostek na cmentarzu gracza " + graczKtoremuWskrzeszamy.getProfilUzytkownika().getNazwaUzytkownika() + " do losowego wskrzeszenia.");
            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.wyswietlKomunikatNaPlanszy("Emhyr (Najeźdźca): Brak jednostek na cmentarzu do wskrzeszenia.", false);
            }
            return;
        }

        Collections.shuffle(mozliweKartyDoLosowegoWskrzeszenia);
        Karta wylosowanaKarta = mozliweKartyDoLosowegoWskrzeszenia.get(0);

        cmentarzGracza.remove(wylosowanaKarta);
        System.out.println("SILNIK: Emhyr 'Najeźdźca Północy' losowo wskrzesza: " + wylosowanaKarta.getNazwa() + " dla gracza " + graczKtoremuWskrzeszamy.getProfilUzytkownika().getNazwaUzytkownika());

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
            aktywujUmiejetnosciPoZagrywce(graczNaCzyjejPlanszyLadujeAutomatycznieWskrzeszona, wylosowanaKarta, rzadDocelowyNaPlanszy, true);
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
    }

    //Przetwarza akcję wyboru karty z cmentarza przeciwnika w ramach zdolności dowódcy Emhyra "Pan Południa".
    public void wykonajWyborEmhyraPanaPoludnia(Karta wybranaKarta) {
        if (!oczekiwanieNaWyborKartyDlaEmhyra || graczAktywujacyEmhyra == null || przeciwnikDlaEmhyra == null || wybranaKarta == null) {
            System.err.println("SILNIK: Nieprawidłowy stan do wykonania wyboru dla Emhyra 'Pan Południa'.");
            if (graczAktywujacyEmhyra != null) {
                graczAktywujacyEmhyra.setZdolnoscDowodcyUzyta(true);
            }
            resetStanuWyboruEmhyra();
            if (graczAktywujacyEmhyra != null && kontrolerPlanszy != null) {
                kontrolerPlanszy.odswiezCalakolwiekPlansze();
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
        graczAktywujacyEmhyra.setZdolnoscDowodcyUzyta(true);
        Gracz graczKtoryUzywalZdolnosci = graczAktywujacyEmhyra;
        resetStanuWyboruEmhyra();

        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
            kontrolerPlanszy.wyswietlKomunikatNaPlanszy(graczKtoryUzywalZdolnosci.getProfilUzytkownika().getNazwaUzytkownika() + " użył zdolności: " + graczKtoryUzywalZdolnosci.getKartaDowodcy().getNazwa() + "!", false);
        }

        oczekiwanieNaPotwierdzenieTury = true;
        Gracz przeciwnikPoEmhyrze = (graczKtoryUzywalZdolnosci == this.gracz1) ? this.gracz2 : this.gracz1;
        setGraczOczekujacyNaPotwierdzenie(przeciwnikPoEmhyrze);
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.rozpocznijSekwencjeZmianyTury();
        }
    }

    //Anuluje proces wyboru karty dla zdolności Emhyra. Zdolność dowódcy zostaje zużyta, a tura przechodzi do przeciwnika.
    public void anulujWyborDlaEmhyra() {
        if (!oczekiwanieNaWyborKartyDlaEmhyra || graczAktywujacyEmhyra == null) {
            resetStanuWyboruEmhyra();
            return;
        }
        System.out.println("SILNIK: Anulowano wybór karty dla Emhyra (" + graczAktywujacyEmhyra.getProfilUzytkownika().getNazwaUzytkownika() + ").");

        graczAktywujacyEmhyra.setZdolnoscDowodcyUzyta(true);
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

    //Dociąga kartę dla gracza w wyniku aktywacji zdolności specjalnej (np. frakcji Królestw Północy).
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
            }
        }
    }

    //Ustawia, który gracz jest obecnie oczekiwany na potwierdzenie swojej tury.
    public void setGraczOczekujacyNaPotwierdzenie(Gracz gracz) {
        this.graczOczekujacyNaPotwierdzenie = gracz;
        System.out.println("[SILNIK] Ustawiono gracza oczekującego na potwierdzenie na: " + (gracz != null ? gracz.getProfilUzytkownika().getNazwaUzytkownika() : "null"));
    }

    //Metoda pomocnicza, która sprawdza, czy gracz nie ma już kart w ręce. Jeśli ręka jest pusta, automatycznie pasuje za tego gracza.
    private boolean checkAndPerformAutoPassIfHandEmpty(Gracz gracz) {
        if (gracz.getReka().isEmpty() && !gracz.isCzySpasowalWRundzie()) {
            System.out.println("SILNIK: Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() +
                    " zagrał ostatnią kartę i nie ma więcej kart w ręce. Automatyczne pasowanie.");
            gracz.setCzySpasowalWRundzie(true);

            if (kontrolerPlanszy != null) {
                kontrolerPlanszy.pokazPanelInfoPas(gracz);
            }
            return true;
        }
        return false;
    }

    //Inicjuje fazę wymiany kart (Mulligan) na początku gry. Rozpoczyna proces od Gracza 1.
    private void rozpocznijFazeMulligan() {
        oczekiwanieNaMulliganGracza1 = true;
        oczekiwanieNaMulliganGracza2 = false;
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.pokazPanelMulligan(gracz1);
        }
    }

    //Przeprowadza wymianę kart na początku gry. Zwraca wybrane karty do talii, tasuje ją i dociąga nowe karty na ich miejsce.
    public void wykonajMulligan(Gracz gracz, List<Karta> kartyDoWymiany) {
        if ((gracz == gracz1 && !oczekiwanieNaMulliganGracza1) || (gracz == gracz2 && !oczekiwanieNaMulliganGracza2)) {
            System.err.println("BŁĄD: Otrzymano żądanie Mulligan dla gracza " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + " w złym momencie.");
            return;
        }

        if (kartyDoWymiany != null && !kartyDoWymiany.isEmpty()) {
            System.out.println("SILNIK: Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + " wymienia " + kartyDoWymiany.size() + " kart.");

            for (Karta karta : kartyDoWymiany) {
                gracz.getReka().remove(karta);
                gracz.getTaliaDoGry().add(karta);
            }

            Collections.shuffle(gracz.getTaliaDoGry());

            int iloscDoDociagniecia = kartyDoWymiany.size();
            for (int i = 0; i < iloscDoDociagniecia; i++) {
                if (!gracz.getTaliaDoGry().isEmpty()) {
                    gracz.getReka().add(gracz.getTaliaDoGry().remove(0));
                }
            }
        } else {
            System.out.println("SILNIK: Gracz " + gracz.getProfilUzytkownika().getNazwaUzytkownika() + " nie wymienił żadnej karty.");
        }

        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
        }

        if (gracz == gracz1) {
            oczekiwanieNaMulliganGracza1 = false;
            oczekiwanieNaMulliganGracza2 = true;
            if(kontrolerPlanszy != null) {
                kontrolerPlanszy.pokazPanelPrzejeciaTury(gracz2);
                kontrolerPlanszy.pokazPanelMulligan(gracz2);
            }

        } else if (gracz == gracz2) {
            oczekiwanieNaMulliganGracza2 = false;
            System.out.println("SILNIK: Faza Mulligan zakończona. Rozpoczynanie pierwszej rundy.");
            rozpocznijNowaRunde();
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

    //Zwraca gracza, który aktualnie jest w trakcie fazy Mulligan.
    public Gracz getGraczDlaMulligan() {
        if (oczekiwanieNaMulliganGracza1) return gracz1;
        if (oczekiwanieNaMulliganGracza2) return gracz2;
        return null;
    }

    //Finalizuje turę po animacji dobierania kart (np. po zagraniu Szpiega). Ta metoda jest wywoływana przez KontrolerPlanszyGry po zakończeniu animacji.
    public void finalizujTurePoDobieraniu(Gracz gracz, List<Karta> kartyDobierane) {
        System.out.println("[SILNIK] Finalizowanie tury po animacji dobrania " + kartyDobierane.size() + " kart.");

        for (Karta karta : kartyDobierane) {
            if (gracz.getTaliaDoGry().remove(karta)) {
                gracz.getReka().add(karta);
            }
        }

        przeliczWszystkiePunktyNaPlanszy();
        if (kontrolerPlanszy != null) {
            kontrolerPlanszy.odswiezCalakolwiekPlansze();
        }

        if (checkAndPerformAutoPassIfHandEmpty(gracz)) {
            return;
        }

        Gracz przeciwnik = (gracz == this.gracz1) ? this.gracz2 : this.gracz1;
        if (przeciwnik.isCzySpasowalWRundzie()) {
            graczAktualnejTury = gracz;
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
    }
}