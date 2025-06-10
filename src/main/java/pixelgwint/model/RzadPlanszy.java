package pixelgwint.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class RzadPlanszy {
    private final TypRzeduEnum typRzedu;
    private final ObservableList<Karta> kartyJednostekWRzedzie; // Tylko jednostki i bohaterowie
    private Karta kartaRoguDowodcy; // Karta Rogu Dowódcy aktywna w tym rzędzie (może być null)
    private int sumaPunktowWRzedzie;
    private StanRundy stanRundyRef;
    private boolean wzmocnieniePrzezDowodceAktywne = false;
    public RzadPlanszy(TypRzeduEnum typRzedu) {
        this.typRzedu = typRzedu;
        this.kartyJednostekWRzedzie = FXCollections.observableArrayList();
        this.kartaRoguDowodcy = null;
        this.sumaPunktowWRzedzie = 0;
        this.stanRundyRef = null;
        this.wzmocnieniePrzezDowodceAktywne = false;
    }

    public void setStanRundyRef(StanRundy stanRundy) {
        this.stanRundyRef = stanRundy;
    }

    public TypRzeduEnum getTypRzedu() { return typRzedu; }
    public ObservableList<Karta> getKartyJednostekWRzedzie() { return kartyJednostekWRzedzie; }
    public Karta getKartaRoguDowodcy() { return kartaRoguDowodcy; }

    public void setKartaRoguDowodcy(Karta kartaRogu) {
        if (this.wzmocnieniePrzezDowodceAktywne && kartaRogu != null) {
            System.out.println("Rząd " + typRzedu + " jest już wzmocniony przez dowódcę. Nie można dodać fizycznego Rogu.");
            if (this.kartaRoguDowodcy != null && this.kartaRoguDowodcy.getTyp() == TypKartyEnum.DOWODCA) {
                // Jeśli to karta dowódcy już tam jest, nie rób nic (nie nadpisuj jej przez null jeśli kartaRogu to null)
            } else if (kartaRogu == null) { // Pozwól na usunięcie, jeśli to nie dowódca
                this.kartaRoguDowodcy = null;
            }
            // Nie pozwól na nadpisanie wzmocnienia dowódcy przez zwykły róg
            // Można dodać komunikat dla gracza w Kontrolerze
            przeliczSumePunktow();
            return;
        }

        // Jeśli to karta dowódcy (np. Foltest) jest ustawiana jako "Róg"
        if (kartaRogu != null && kartaRogu.getTyp() == TypKartyEnum.DOWODCA) {
            this.kartaRoguDowodcy = kartaRogu;
            this.wzmocnieniePrzezDowodceAktywne = true; // Ustawiamy, że wzmocnienie jest aktywne
            System.out.println("Rząd " + typRzedu + " wzmocniony przez dowódcę: " + kartaRogu.getNazwa());
        }
        // Jeśli to zwykły Róg Dowódcy (karta specjalna)
        else if (kartaRogu != null && kartaRogu.getNazwa().toLowerCase().contains("róg dowódcy") && kartaRogu.getTyp() == TypKartyEnum.SPECJALNA) {
            this.kartaRoguDowodcy = kartaRogu;
            this.wzmocnieniePrzezDowodceAktywne = false; // Upewnij się, że wzmocnienie od dowódcy nie jest aktywne
        }
        // Jeśli usuwamy Róg (przekazano null)
        else if (kartaRogu == null) {
            // Jeśli usuniemy kartę dowódcy ze slotu rogu, musimy też zresetować flagę wzmocnienia.
            // Jednak zdolność Foltesta jest jednorazowa, więc po jej użyciu nie powinna być "wyłączana" przez usunięcie karty ze slotu.
            // Flaga `wzmocnieniePrzezDowodceAktywne` powinna być resetowana tylko na końcu rundy.
            // Usunięcie karty Rogu (czy to specjalnej, czy dowódcy w tym slocie) powinno być możliwe na koniec rundy.
            this.kartaRoguDowodcy = null;
            // NIE resetuj tutaj wzmocnieniePrzezDowodceAktywne, jeśli było ustawione przez zdolność.
            // To jest resetowane w wyczyscRzadIZwrocKarty.
        }
        przeliczSumePunktow();
    }



    public void dodajKarteJednostki(Karta karta) {
        // Prosta logika grupowania wizualnego dla Więzi
        if (karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Więź")) {
            int ostatniIndexTejSamejNazwy = -1;
            for (int i = 0; i < kartyJednostekWRzedzie.size(); i++) {
                Karta kNaPlanszy = kartyJednostekWRzedzie.get(i);
                if (kNaPlanszy.getNazwa().equals(karta.getNazwa()) &&
                        kNaPlanszy.getUmiejetnosc() != null && kNaPlanszy.getUmiejetnosc().equalsIgnoreCase("Więź")) {
                    ostatniIndexTejSamejNazwy = i;
                }
            }
            if (ostatniIndexTejSamejNazwy != -1) {
                // Wstaw za ostatnią znalezioną kartą z tej samej grupy Więzi
                this.kartyJednostekWRzedzie.add(ostatniIndexTejSamejNazwy + 1, karta);
            } else {
                // Pierwsza karta z tej grupy Więzi lub brak innych, dodaj na końcu
                this.kartyJednostekWRzedzie.add(karta);
            }
        } else {
            this.kartyJednostekWRzedzie.add(karta);
        }
        przeliczSumePunktow();
    }

    public Karta usunKarteJednostki(Karta karta) {
        boolean usunieto = this.kartyJednostekWRzedzie.remove(karta);
        if (usunieto) {
            przeliczSumePunktow();
            return karta;
        }
        return null;
    }
    public Karta usunKarteJednostkiZIndeksu(int index) {
        if (index >= 0 && index < kartyJednostekWRzedzie.size()) {
            Karta usunieta = kartyJednostekWRzedzie.remove(index);
            przeliczSumePunktow();
            return usunieta;
        }
        return null;
    }



    public List<Karta> wyczyscRzadIZwrocKarty() {
        List<Karta> usunieteKartyZJednostek = new ArrayList<>(this.kartyJednostekWRzedzie);
        this.kartyJednostekWRzedzie.clear();
        this.kartaRoguDowodcy = null;                 // <-- DODAJ RESET ROGU
        this.wzmocnieniePrzezDowodceAktywne = false; // <-- DODAJ RESET WZMOCNIENIA OD DOWÓDCY
        przeliczSumePunktow(); // Suma powinna spaść do 0
        return usunieteKartyZJednostek;
    }

    // Metoda wyczyscRzad() może pozostać bez zmian:
    public void wyczyscRzad() {
        wyczyscRzadIZwrocKarty();
    }

    public int getSumaPunktowWRzedzie() {
        przeliczSumePunktow(); // Zawsze aktualna suma
        return sumaPunktowWRzedzie;
    }

    public void przeliczSumePunktow() {
        int sumaBohaterowWRzedzie = 0;
        int sumaPunktowJaskraDoDodania = 0; // Siła Jaskrów (nie będzie podwajana przez Róg)
        int sumaInnychJednostekNieHero = 0; // Siła pozostałych jednostek, która będzie podwajana

        List<Karta> kopiaKartWRzedzie = new ArrayList<>(kartyJednostekWRzedzie);
        List<Integer> idKartyJaskra = Arrays.asList(44, 328, 466, 542);

        // --- Ustalenie aktywnych efektów globalnych dla rzędu ---
        boolean hornAktywnyZeSlotaLubZdolnosciDowodcy = (this.kartaRoguDowodcy != null || this.wzmocnieniePrzezDowodceAktywne);
        boolean jaskierAktywujacyRogObecny = false;
        for (Karta k : kopiaKartWRzedzie) {
            if (idKartyJaskra.contains(k.getId())) {
                jaskierAktywujacyRogObecny = true;
                break;
            }
        }
        boolean czyRzadMaAktywnyEfektRogu = hornAktywnyZeSlotaLubZdolnosciDowodcy || jaskierAktywujacyRogObecny;

        boolean pogodaAktywnaDlaTegoRzedu = false;
        if (stanRundyRef != null) {
            for (Karta kartaPogody : stanRundyRef.getAktywneKartyWRzedziePogody()) {
                String nazwaPogody = kartaPogody.getNazwa().toLowerCase();
                if ((this.typRzedu == TypRzeduEnum.PIECHOTA && nazwaPogody.contains("mróz")) ||
                        (this.typRzedu == TypRzeduEnum.STRZELECKIE && nazwaPogody.contains("mgła")) ||
                        (this.typRzedu == TypRzeduEnum.OBLEZENIE && nazwaPogody.contains("deszcz"))) {
                    pogodaAktywnaDlaTegoRzedu = true;
                    break;
                }
            }
        }

        boolean czyEredin413AktywnyGlobalnie = (stanRundyRef != null && stanRundyRef.isEredin413Active());

        int bonusMoraleOdInnychJednostek = 0;
        for (Karta potencjalnyDawcaMorale : kopiaKartWRzedzie) {
            if (potencjalnyDawcaMorale.getTyp() != TypKartyEnum.BOHATER &&
                    !idKartyJaskra.contains(potencjalnyDawcaMorale.getId()) &&
                    potencjalnyDawcaMorale.getUmiejetnosc() != null &&
                    potencjalnyDawcaMorale.getUmiejetnosc().equalsIgnoreCase("Wysokie morale")) {
                bonusMoraleOdInnychJednostek++;
            }
        }

        Map<String, List<Karta>> grupyWiez = new HashMap<>();
        for (Karta k : kopiaKartWRzedzie) {
            if (k.getTyp() != TypKartyEnum.BOHATER &&
                    k.getUmiejetnosc() != null && k.getUmiejetnosc().equalsIgnoreCase("Więź")) {
                grupyWiez.computeIfAbsent(k.getNazwa(), key -> new ArrayList<>()).add(k);
            }
        }

        // --- Przetwarzanie siły każdej karty indywidualnie ---
        for (Karta karta : kopiaKartWRzedzie) {
            int silaObliczeniowaKarty = karta.getPunktySily();

            if (karta.getTyp() == TypKartyEnum.BOHATER) {
                boolean czySzpiegBohater = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
                if (czySzpiegBohater && czyEredin413AktywnyGlobalnie) {
                    silaObliczeniowaKarty *= 2;
                }
                sumaBohaterowWRzedzie += silaObliczeniowaKarty;
            } else { // Karta jest Jednostką (w tym Jaskier)
                boolean czySzpiegJednostka = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
                if (czySzpiegJednostka && czyEredin413AktywnyGlobalnie) {
                    silaObliczeniowaKarty *= 2;
                }

                if (karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Więź")) {
                    List<Karta> grupaBiezacejKarty = grupyWiez.get(karta.getNazwa());
                    if (grupaBiezacejKarty != null && grupaBiezacejKarty.size() > 1) {
                        silaObliczeniowaKarty *= grupaBiezacejKarty.size();
                    }
                }

                boolean czyKartaSamaDajeMoralePrzezUmiejetnosc = karta.getUmiejetnosc() != null && karta.getUmiejetnosc().equalsIgnoreCase("Wysokie morale");
                if (!czyKartaSamaDajeMoralePrzezUmiejetnosc) {
                    silaObliczeniowaKarty += bonusMoraleOdInnychJednostek;
                }

                if (pogodaAktywnaDlaTegoRzedu) {
                    silaObliczeniowaKarty = 1;
                }

                if (idKartyJaskra.contains(karta.getId())) {
                    sumaPunktowJaskraDoDodania += silaObliczeniowaKarty;
                } else {
                    sumaInnychJednostekNieHero += silaObliczeniowaKarty;
                }
            }
        }

        // Zastosuj efekt Rogu Dowódcy tylko do "innych jednostek nie-bohaterów"
        if (czyRzadMaAktywnyEfektRogu) {
            sumaInnychJednostekNieHero *= 2;
        }

        this.sumaPunktowWRzedzie = sumaBohaterowWRzedzie + sumaPunktowJaskraDoDodania + sumaInnychJednostekNieHero;

        // Logowanie do debugowania (możesz odkomentować w razie potrzeby)
    /*
    System.out.println("Rząd " + typRzedu +
                       ": Bohaterowie=" + sumaBohaterowWRzedzie +
                       ", Jaskier=" + sumaPunktowJaskraDoDodania +
                       ", Inne Jednostki (po Rogu)=" + sumaInnychJednostekNieHero +
                       " (Róg aktywny? " + czyRzadMaAktywnyEfektRogu +
                       ", Jaskier aktywujący? " + jaskierAktywujacyRogObecny +
                       ", Pogoda aktywna? " + pogodaAktywnaDlaTegoRzedu +
                       "), Razem=" + this.sumaPunktowWRzedzie);
    */
    }
    public boolean isWzmocnieniePrzezDowodceAktywne() {
        return wzmocnieniePrzezDowodceAktywne;
    }

    public void setWzmocnieniePrzezDowodceAktywne(boolean aktywne) {
        // Nie pozwól aktywować wzmocnienia dowódcy, jeśli Róg już jest w rzędzie
        if (aktywne && this.kartaRoguDowodcy != null) {
            System.out.println("Nie można aktywować wzmocnienia dowódcy w rzędzie " + typRzedu + ", ponieważ Róg Dowódcy jest już aktywny.");
            // Ta logika jest też w Silniku Gry, ale dodatkowe zabezpieczenie tutaj nie zaszkodzi.
            return;
        }
        this.wzmocnieniePrzezDowodceAktywne = aktywne;
        przeliczSumePunktow();
    }
    public int obliczSileEfektywnaKarty(Karta kartaDoObliczenia) {
        // Ta kalkulacja jest dla POJEDYNCZEJ karty w kontekście TEGO rzędu.

        // 1. Bohaterowie - ich siła jest generalnie stała (z wyjątkiem Szpiegów z Eredinem 413)
        if (kartaDoObliczenia.getTyp() == TypKartyEnum.BOHATER) {
            int silaBohatera = kartaDoObliczenia.getPunktySily();
            boolean czySzpiegBohater = kartaDoObliczenia.getUmiejetnosc() != null && kartaDoObliczenia.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
            if (czySzpiegBohater && stanRundyRef != null && stanRundyRef.isEredin413Active()) {
                silaBohatera *= 2;
            }
            return silaBohatera;
        }

        // 2. Dla jednostek nie-bohaterów:
        int silaObliczeniowa = kartaDoObliczenia.getPunktySily();

        // a. Eredin dla Szpiegów
        boolean czyEredin413AktywnyGlobalnie = (stanRundyRef != null && stanRundyRef.isEredin413Active());
        boolean czySzpiegJednostka = kartaDoObliczenia.getUmiejetnosc() != null && kartaDoObliczenia.getUmiejetnosc().equalsIgnoreCase("Szpiegostwo");
        if (czySzpiegJednostka && czyEredin413AktywnyGlobalnie) {
            silaObliczeniowa *= 2;
        }

        // b. Więź
        if (kartaDoObliczenia.getUmiejetnosc() != null && kartaDoObliczenia.getUmiejetnosc().equalsIgnoreCase("Więź")) {
            long liczbaKartWGrupie = kartyJednostekWRzedzie.stream()
                    .filter(k -> k.getTyp() != TypKartyEnum.BOHATER && k.getNazwa().equals(kartaDoObliczenia.getNazwa()))
                    .count();
            if (liczbaKartWGrupie > 1) {
                silaObliczeniowa *= liczbaKartWGrupie;
            }
        }

        // c. Morale (od innych kart)
        int bonusMorale = (int) kartyJednostekWRzedzie.stream()
                .filter(k -> k != kartaDoObliczenia &&
                        k.getTyp() != TypKartyEnum.BOHATER &&
                        k.getUmiejetnosc() != null &&
                        k.getUmiejetnosc().equalsIgnoreCase("Wysokie morale"))
                .count();

        if (kartaDoObliczenia.getUmiejetnosc() == null || !kartaDoObliczenia.getUmiejetnosc().equalsIgnoreCase("Wysokie morale")) {
            silaObliczeniowa += bonusMorale;
        }

        // d. Pogoda
        boolean pogodaAktywnaDlaTegoRzedu = false;
        if (stanRundyRef != null) {
            for (Karta kartaPogody : stanRundyRef.getAktywneKartyWRzedziePogody()) {
                String nazwaPogody = kartaPogody.getNazwa().toLowerCase();
                if ((this.typRzedu == TypRzeduEnum.PIECHOTA && nazwaPogody.contains("mróz")) ||
                        (this.typRzedu == TypRzeduEnum.STRZELECKIE && nazwaPogody.contains("mgła")) ||
                        (this.typRzedu == TypRzeduEnum.OBLEZENIE && nazwaPogody.contains("deszcz"))) {
                    pogodaAktywnaDlaTegoRzedu = true;
                    break;
                }
            }
        }
        if (pogodaAktywnaDlaTegoRzedu) {
            silaObliczeniowa = 1;
        }

        // e. Róg Dowódcy / Jaskier
        boolean hornAktywnyZeSlotaLubZdolnosciDowodcy = (this.kartaRoguDowodcy != null || this.wzmocnieniePrzezDowodceAktywne);
        List<Integer> idKartyJaskra = Arrays.asList(44, 328, 466, 542);
        boolean jaskierObecny = kartyJednostekWRzedzie.stream().anyMatch(k -> idKartyJaskra.contains(k.getId()));

        if (hornAktywnyZeSlotaLubZdolnosciDowodcy || jaskierObecny) {
            // Róg nie podwaja samego Jaskra, ale podwaja inne jednostki
            if (!idKartyJaskra.contains(kartaDoObliczenia.getId())) {
                silaObliczeniowa *= 2;
            }
        }

        return silaObliczeniowa;
    }
}
