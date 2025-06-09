package pixelgwint.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Gracz {
    private Uzytkownik profilUzytkownika;    // Profil gracza (z logowania, jeśli dotyczy)
    private FrakcjaEnum wybranaFrakcja;
    private Karta kartaDowodcy;
    private List<Karta> taliaBazowa;         // Wszystkie karty dostępne dla gracza dla tej frakcji (do budowy talii)
    private List<Karta> taliaDoGry;          // Talia używana w aktualnej rozgrywce (po przetasowaniu, przed dociągnięciem)
    private List<Karta> reka;
    private List<Karta> odrzucone;           // Cmentarz
    // Usunęliśmy pole 'punktyMocyWRundzie', ponieważ punkty są teraz na 'planszaGry'
    private int wygraneRundy;              // Liczba "klejnotów" życia (zwykle 2 na start)
    private boolean czySpasowalWRundzie;
    private boolean zdolnoscDowodcyUzyta;
    private PlanszaGracza planszaGry; // Plansza przypisana do tego gracza
    private boolean zdolnoscDowodcyZablokowana;

    public Gracz(Uzytkownik profilUzytkownika, FrakcjaEnum wybranaFrakcja) {
        this.profilUzytkownika = profilUzytkownika;
        this.wybranaFrakcja = wybranaFrakcja;
        this.taliaBazowa = new ArrayList<>();
        this.taliaDoGry = new ArrayList<>();
        this.reka = new ArrayList<>();
        this.odrzucone = new ArrayList<>();
        // this.punktyMocyWRundzie = 0; // Już niepotrzebne
        this.wygraneRundy = 2; // Zaczynamy z 2 "życiami"
        this.czySpasowalWRundzie = false;
        this.zdolnoscDowodcyUzyta = false;
        this.planszaGry = new PlanszaGracza(); // Każdy gracz dostaje swoją nową planszę
        this.zdolnoscDowodcyZablokowana = false;
    }

    // --- Gettery i Settery ---

    public PlanszaGracza getPlanszaGry() {
        return planszaGry;
    }

    public void resetDoNowejGry() {
        System.out.println("[GRACZ] Resetowanie gracza " + (profilUzytkownika != null ? profilUzytkownika.getNazwaUzytkownika() : "N/A") + " do nowej gry.");
        // Talia bazowa (wszystkie dostępne karty) nie zmienia się.
        // Talia do gry jest tworzona na nowo z talii bazowej (lub wg logiki tworzenia talii).
        // Jeśli taliaDoGry była modyfikowana (karty usuwane), musi być odtworzona.
        // Zakładając, że taliaBazowa jest kompletna:
        if (this.taliaBazowa != null && !this.taliaBazowa.isEmpty()) {
            this.taliaDoGry = new ArrayList<>(this.taliaBazowa);
            Collections.shuffle(this.taliaDoGry);
        } else {
            // Jeśli taliaBazowa jest pusta lub null, taliaDoGry musi być odtworzona
            // w taki sam sposób, jak była tworzona na początku (np. z TaliaUzytkownika).
            // To jest bardziej skomplikowane i zależy od Twojej logiki tworzenia gracza.
            // Na razie zakładamy, że taliaBazowa jest źródłem.
            // Jeśli nie, musisz tu odtworzyć talięDoGry.
            System.err.println("OSTRZEŻENIE: Talia bazowa dla gracza " + getProfilUzytkownika().getNazwaUzytkownika() + " jest pusta/null. Talia do gry może być niepoprawna dla rewanżu.");
            this.taliaDoGry = new ArrayList<>(); // Pusta, jeśli nie ma z czego odtworzyć
        }

        this.reka = new ArrayList<>();
        this.odrzucone = new ArrayList<>();
        this.wygraneRundy = 2; // Reset liczby "żyć"
        this.czySpasowalWRundzie = false;
        this.zdolnoscDowodcyUzyta = false;

        if (this.planszaGry != null) {
            this.planszaGry.wyczyscPlansze(); // Czyści rzędy i punkty na planszy
        } else {
            this.planszaGry = new PlanszaGracza(); // Na wszelki wypadek, jeśli plansza była null
        }
        // Frakcja i karta dowódcy pozostają takie same dla rewanżu z tymi samymi ustawieniami.
    }

    public Uzytkownik getProfilUzytkownika() { return profilUzytkownika; }
    public void setProfilUzytkownika(Uzytkownik profilUzytkownika) { this.profilUzytkownika = profilUzytkownika; }

    public FrakcjaEnum getWybranaFrakcja() { return wybranaFrakcja; }
    public void setWybranaFrakcja(FrakcjaEnum wybranaFrakcja) { this.wybranaFrakcja = wybranaFrakcja; }

    public Karta getKartaDowodcy() { return kartaDowodcy; }
    public void setKartaDowodcy(Karta kartaDowodcy) { this.kartaDowodcy = kartaDowodcy; }

    public List<Karta> getTaliaBazowa() { return taliaBazowa; }
    public void setTaliaBazowa(List<Karta> taliaBazowa) { this.taliaBazowa = taliaBazowa; }

    public List<Karta> getTaliaDoGry() { return taliaDoGry; }
    public void setTaliaDoGry(List<Karta> taliaDoGry) { this.taliaDoGry = taliaDoGry; }

    public List<Karta> getReka() { return reka; }
    public void setReka(List<Karta> reka) { this.reka = reka; }

    public List<Karta> getOdrzucone() { return odrzucone; }
    public void setOdrzucone(List<Karta> odrzucone) { this.odrzucone = odrzucone; }

    /**
     * Zwraca aktualną sumę punktów mocy gracza w rundzie, obliczoną na podstawie jego planszy.
     * @return suma punktów mocy na planszy gracza.
     */
    public int getPunktyMocyWRundzie() {
        if (this.planszaGry != null) {
            return this.planszaGry.getLacznaSumaPunktowGracza();
        }
        return 0; // Jeśli plansza nie istnieje, zwraca 0
    }

    // Usunęliśmy setPunktyMocyWRundzie, dodajPunktyMocy i resetujPunktyMocyRundy,
    // ponieważ punkty są teraz własnością PlanszaGracza i tam są modyfikowane
    // przez dodawanie/usuwanie kart i ich umiejętności.

    /**
     * Resetuje stan gracza na potrzeby nowej rundy (ale nie całej gry).
     * Czyści jego planszę i flagę pasowania. Ręka i odrzucone karty zwykle przechodzą.
     */
    public void resetujDoNastepnejRundy() {
        this.czySpasowalWRundzie = false;
        if (this.planszaGry != null) {
            this.planszaGry.wyczyscPlansze(); // Czyści planszę i resetuje jej punkty
        }
        // Zdolność dowódcy jest zwykle raz na grę, nie resetujemy jej co rundę, chyba że zasady mówią inaczej
    }

    public int getWygraneRundy() { return wygraneRundy; }
    public void setWygraneRundy(int wygraneRundy) { this.wygraneRundy = wygraneRundy; }
    public void stracZycie() { if(this.wygraneRundy > 0) this.wygraneRundy--; }

    public boolean isCzySpasowalWRundzie() { return czySpasowalWRundzie; }
    public void setCzySpasowalWRundzie(boolean czySpasowalWRundzie) { this.czySpasowalWRundzie = czySpasowalWRundzie; }

    public boolean isZdolnoscDowodcyUzyta() { return zdolnoscDowodcyUzyta; }
    public void setZdolnoscDowodcyUzyta(boolean zdolnoscDowodcyUzyta) { this.zdolnoscDowodcyUzyta = zdolnoscDowodcyUzyta; }

    public boolean isZdolnoscDowodcyZablokowana() {
        return zdolnoscDowodcyZablokowana;
    }

    public void setZdolnoscDowodcyZablokowana(boolean zdolnoscDowodcyZablokowana) {
        this.zdolnoscDowodcyZablokowana = zdolnoscDowodcyZablokowana;
    }


    @Override
    public String toString() {
        return "Gracz{" +
                "profil=" + (profilUzytkownika != null ? profilUzytkownika.getNazwaUzytkownika() : "Brak profilu") +
                ", frakcja=" + wybranaFrakcja +
                ", dowodca=" + (kartaDowodcy != null ? kartaDowodcy.getNazwa() : "Brak") +
                ", pktWRundzie=" + getPunktyMocyWRundzie() + // Używamy gettera, który odwołuje się do planszy
                ", wygraneRundy=" + wygraneRundy +
                '}';
    }
}