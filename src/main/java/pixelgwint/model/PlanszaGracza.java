package pixelgwint.model;

import java.util.ArrayList;
import java.util.List;

public class PlanszaGracza {
    private RzadPlanszy rzadPiechoty;
    private RzadPlanszy rzadStrzelecki;
    private RzadPlanszy rzadOblezenia;
    private int lacznaSumaPunktowGracza; // To pole jest aktualizowane przez przeliczLacznaSumePunktow

    public PlanszaGracza() {
        this.rzadPiechoty = new RzadPlanszy(TypRzeduEnum.PIECHOTA);
        this.rzadStrzelecki = new RzadPlanszy(TypRzeduEnum.STRZELECKIE);
        this.rzadOblezenia = new RzadPlanszy(TypRzeduEnum.OBLEZENIE);
        this.lacznaSumaPunktowGracza = 0;
    }

    public RzadPlanszy getRzadPiechoty() { return rzadPiechoty; }
    public RzadPlanszy getRzadStrzelecki() { return rzadStrzelecki; }
    public RzadPlanszy getRzadOblezenia() { return rzadOblezenia; }

    /**
     * Zwraca sumę punktów na planszy gracza, uprzednio ją przeliczając.
     */
    public int getLacznaSumaPunktowGracza() {
        przeliczLacznaSumePunktow(); // Zawsze aktualizuj sumę przed jej zwróceniem
        return lacznaSumaPunktowGracza;
    }

    /**
     * Przelicza sumę punktów wszystkich rzędów i aktualizuje łączną sumę punktów gracza.
     * Ta metoda powinna być publiczna, aby SilnikGry mógł ją wywołać.
     */
    public void przeliczLacznaSumePunktow() {
        // Upewnij się, że każdy rząd przelicza swoje punkty wewnętrznie
        rzadPiechoty.przeliczSumePunktow();
        rzadStrzelecki.przeliczSumePunktow();
        rzadOblezenia.przeliczSumePunktow();

        this.lacznaSumaPunktowGracza = rzadPiechoty.getSumaPunktowWRzedzie() +
                rzadStrzelecki.getSumaPunktowWRzedzie() +
                rzadOblezenia.getSumaPunktowWRzedzie();
    }

    public void wyczyscPlansze() {
        wyczyscPlanszeIZwrocKarty();
    }

    public List<Karta> wyczyscPlanszeIZwrocKarty() {
        List<Karta> wszystkieUsunieteKarty = new ArrayList<>();
        // Zbieramy karty jednostek z każdego rzędu
        wszystkieUsunieteKarty.addAll(rzadPiechoty.wyczyscRzadIZwrocKarty());
        wszystkieUsunieteKarty.addAll(rzadStrzelecki.wyczyscRzadIZwrocKarty());
        wszystkieUsunieteKarty.addAll(rzadOblezenia.wyczyscRzadIZwrocKarty());

        // Karty Rogu Dowódcy (jeśli są specjalne i leżą w slotach) obsłużymy osobno w silniku gry,
        // pobierając je przed wyczyszczeniem i dodając do cmentarza.
        // Tutaj rzędy już wyzerowały swoje punkty i wewnętrzne listy jednostek.

        przeliczLacznaSumePunktow(); // Po wyczyszczeniu suma punktów gracza powinna być 0
        return wszystkieUsunieteKarty;
    }

    public RzadPlanszy getRzad(TypRzeduEnum typRzedu) {
        if (typRzedu == null) return null;
        switch (typRzedu) {
            case PIECHOTA: return rzadPiechoty;
            case STRZELECKIE: return rzadStrzelecki;
            case OBLEZENIE: return rzadOblezenia;
            default: return null;
        }
    }
}