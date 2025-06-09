package pixelgwint.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class StanRundy {
    private PlanszaGracza planszaGracza1; // Usunięto inicjalizację 'new PlanszaGracza()'
    private PlanszaGracza planszaGracza2; // Usunięto inicjalizację 'new PlanszaGracza()'
    private final ObservableList<Karta> aktywneKartyWRzedziePogody;

    private int liderGracza1Id = -1; // Domyślna wartość oznaczająca brak informacji
    private int liderGracza2Id = -1;

    public StanRundy(PlanszaGracza planszaG1, PlanszaGracza planszaG2) {
        this.planszaGracza1 = planszaG1; // Przypisanie referencji
        this.planszaGracza2 = planszaG2; // Przypisanie referencji
        this.aktywneKartyWRzedziePogody = FXCollections.observableArrayList();
    }

    public void setLeaderIds(int idLideraG1, int idLideraG2) {
        this.liderGracza1Id = idLideraG1;
        this.liderGracza2Id = idLideraG2;
    }

    public boolean isEredin413Active() {
        return this.liderGracza1Id == 413 || this.liderGracza2Id == 413;
    }

    public PlanszaGracza getPlanszaGracza1() { return planszaGracza1; }
    public PlanszaGracza getPlanszaGracza2() { return planszaGracza2; }
    public ObservableList<Karta> getAktywneKartyWRzedziePogody() { return aktywneKartyWRzedziePogody; }

    public void dodajKartePogody(Karta kartaPogody) {
        // W Gwincie z W3, zagranie nowej karty pogody tego samego typu usuwa starą.
        // Np. Mróz -> Czyste Niebo usuwa Mróz. Mróz -> drugi Mróz nie robi nic. Mróz -> Mgła - obie są aktywne.
        // Na razie uproszczone: pozwalamy na max 3 różne pogody (Mróz, Mgła, Deszcz).
        // Czyste Niebo będzie usuwać wszystkie.

        if (kartaPogody == null || kartaPogody.getTyp() != TypKartyEnum.SPECJALNA) return; // Tylko specjalne mogą być pogodą

        String nazwaKarty = kartaPogody.getNazwa().toLowerCase();
        boolean czyToPogodaFaktycznie = nazwaKarty.contains("mróz") || nazwaKarty.contains("mgła") || nazwaKarty.contains("deszcz");
        boolean czyToCzysteNiebo = nazwaKarty.contains("czyste niebo") || nazwaKarty.contains("słoneczna pogoda");

        if (czyToCzysteNiebo) {
            wyczyscPogode();
            return;
        }

        if (czyToPogodaFaktycznie) {
            boolean juzJestTakaPogoda = aktywneKartyWRzedziePogody.stream()
                    .anyMatch(p -> p.getNazwa().equalsIgnoreCase(kartaPogody.getNazwa()));
            if (!juzJestTakaPogoda && aktywneKartyWRzedziePogody.size() < 3) {
                aktywneKartyWRzedziePogody.add(kartaPogody);
            } else if (juzJestTakaPogoda) {
                System.out.println("Pogoda '" + kartaPogody.getNazwa() + "' już jest aktywna.");
            } else {
                System.out.println("Osiągnięto limit aktywnych kart pogody.");
            }
        }
    }

    public void wyczyscPogode() {
        this.aktywneKartyWRzedziePogody.clear();
    }

    public void resetujStanRundy() {
        // Plansze graczy (planszaGracza1, planszaGracza2) są referencjami do plansz w obiektach Gracz.
        // Ich czyszczenie odbywa się poprzez gracz.resetujDoNastepnejRundy() -> plansza.wyczyscPlansze().
        // Nie ma potrzeby ponownego czyszczenia ich tutaj, jeśli SilnikGry to robi.
        // Wystarczy wyczyścić pogodę.
        wyczyscPogode();
        System.out.println("[STAN RUNDY] Zresetowano stan rundy (pogoda wyczyszczona).");
    }
}