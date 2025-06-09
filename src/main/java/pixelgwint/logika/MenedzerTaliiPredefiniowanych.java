package pixelgwint.logika;

import pixelgwint.model.FrakcjaEnum;
import pixelgwint.model.TaliaUzytkownika; // Użyjemy tej samej struktury

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MenedzerTaliiPredefiniowanych {

    private static final Map<FrakcjaEnum, List<TaliaUzytkownika>> PREDEFINIOWANE_TALIE = new HashMap<>();
    private static final Random random = new Random();
    private static final String PREDEFINIOWANY_UZYTKOWNIK = "Predefiniowana";

    // Blok statyczny inicjalizujący talie z Twoimi danymi
    static {
        // --- Królestwa Północy ---
        List<TaliaUzytkownika> talieKP = new ArrayList<>();
        talieKP.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Stal z Mahakamu", FrakcjaEnum.KROLESTWA_POLNOCY,
                334, // ID Dowódcy
                Arrays.asList(272, 273, 275, 278, 281, 284, 287, 290, 291, 292, 263, 264, 265, 266, 267, 268, 269, 270, 271, 293, 294, 295, 300, 302, 303, 304, 306, 307, 308, 315, 316, 318, 319, 323, 326, 325, 327, 328, 329)
        ));
        talieKP.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Oblężenie Vizimy", FrakcjaEnum.KROLESTWA_POLNOCY,
                333, // ID Dowódcy
                Arrays.asList(272, 275, 278, 281, 284, 287, 290, 291, 292, 273, 266, 267, 268, 296, 297, 298, 299, 301, 302, 303, 314, 327, 315, 316, 318, 319, 320, 322, 323, 325, 326, 329, 330, 269, 270, 271, 300, 304, 306, 307)
        ));
        talieKP.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Rycerze Temerii", FrakcjaEnum.KROLESTWA_POLNOCY,
                331, // ID Dowódcy
                Arrays.asList(272, 273, 274, 275, 284, 287, 290, 291, 292, 278, 293, 294, 269, 270, 271, 304, 305, 306, 307, 309, 310, 311, 312, 313, 314, 315, 316, 317, 319, 320, 321, 323, 324, 325, 326, 327, 328, 329, 330)
        ));
        PREDEFINIOWANE_TALIE.put(FrakcjaEnum.KROLESTWA_POLNOCY, talieKP);

        // --- Nilfgaard ---
        List<TaliaUzytkownika> talieNG = new ArrayList<>();
        talieNG.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Cień Imperatora", FrakcjaEnum.NILFGAARD,
                73, // ID Dowódcy
                Arrays.asList(15, 16, 17, 18, 23, 25, 28, 31, 34, 24, 2, 5, 4, 7, 8, 9, 10, 11, 12, 13, 14, 20, 21, 22, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 50, 51, 53, 56, 57, 58, 60, 62)
        ));
        talieNG.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Prawo i Ogień", FrakcjaEnum.NILFGAARD,
                72, // ID Dowódcy
                Arrays.asList(23, 25, 26, 28, 29, 31, 32, 34, 35, 15, 1, 2, 4, 10, 9, 37, 38, 39, 40, 41, 42, 43, 45, 47, 48, 50, 51, 52, 53, 49, 54, 55, 58, 61, 62, 65)
        ));
        talieNG.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Czarna Flota", FrakcjaEnum.NILFGAARD,
                69, // ID Dowódcy (uwaga: powtarza się w liście kart poniżej, jeśli 69 to dowódca, nie powinno go być w liście kart)
                Arrays.asList(3, 35, 36, 34, 39, 15, 16, 1, 4, 7, 10, 38, 60, 61, 62, 63, 64, 65, 66, 67, 68, 11, 12, 13, 14, 51, 46, 47, 49)
        ));
        PREDEFINIOWANE_TALIE.put(FrakcjaEnum.NILFGAARD, talieNG);

        // --- Potwory ---
        List<TaliaUzytkownika> taliePT = new ArrayList<>();
        taliePT.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Horda Dzikiego Gonu", FrakcjaEnum.POTWORY,
                336, // ID Dowódcy
                Arrays.asList(417, 418, 420, 421, 423, 424, 426, 432, 435, 429, 438, 439, 440, 441, 442, 443, 444, 445, 446, 447, 448, 449, 455, 456, 457, 458, 459, 461, 462, 463, 464, 469, 470, 472, 485, 486, 488)
        ));
        taliePT.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Nienasycony Głód", FrakcjaEnum.POTWORY,
                416, // ID Dowódcy
                Arrays.asList(417, 420, 423, 426, 429, 430, 431, 432, 435, 436, 438, 439, 441, 442, 443, 450, 451, 452, 453, 454, 455, 458, 459, 460, 461, 462, 463, 465, 466, 467, 469, 470, 471, 472, 473)
        ));
        taliePT.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Szpony i Kły", FrakcjaEnum.POTWORY,
                415, // ID Dowódcy
                Arrays.asList(417, 420, 421, 423, 429, 432, 435, 436, 437, 430, 438, 439, 441, 442, 444, 445, 447, 448, 450, 451, 454, 455, 456, 457, 458, 459, 462, 464, 466, 468, 470, 474, 477, 483, 487, 485)
        ));
        PREDEFINIOWANE_TALIE.put(FrakcjaEnum.POTWORY, taliePT);

        // --- Scoia'tael ---
        List<TaliaUzytkownika> talieST = new ArrayList<>();
        talieST.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Leśna Zasadzka", FrakcjaEnum.SCOIATAEL,
                491, // ID Dowódcy
                Arrays.asList(494, 495, 496, 497, 498, 499, 500, 501, 512, 513, 518, 519, 534, 535, 536, 538, 543, 544, 552, 559, 560, 562, 561, 539, 556, 554, 555, 557, 520, 521, 522, 528, 527, 526, 529)
        ));
        talieST.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Gniew Driad", FrakcjaEnum.SCOIATAEL,
                492, // ID Dowódcy
                Arrays.asList(494, 495, 496, 497, 498, 499, 500, 501, 512, 513, 515, 516, 517, 520, 521, 522, 523, 524, 525, 526, 527, 528, 529, 530, 531, 532, 533, 537, 538, 539, 540, 541, 542, 547, 554, 555, 557, 558, 560, 561)
        ));
        talieST.add(new TaliaUzytkownika(PREDEFINIOWANY_UZYTKOWNIK, "Wolność lub Śmierć", FrakcjaEnum.SCOIATAEL,
                489, // ID Dowódcy
                Arrays.asList(494, 495, 496, 497, 498, 499, 500, 501, 512, 513, 529, 530, 531, 532, 533, 537, 539, 543, 544, 545, 546, 547, 548, 553, 554, 555, 556, 557, 558, 559, 561, 562, 520, 521, 522)
        ));
        PREDEFINIOWANE_TALIE.put(FrakcjaEnum.SCOIATAEL, talieST);

    }

    /**
     * Pobiera losową predefiniowaną talię dla podanej frakcji.
     * @param frakcja Frakcja, dla której ma być pobrana talia.
     * @return Losowa TaliaUzytkownika lub null, jeśli brak predefiniowanych talii dla tej frakcji.
     */
    public TaliaUzytkownika pobierzLosowaTaliePredefiniowana(FrakcjaEnum frakcja) {
        List<TaliaUzytkownika> dostepneTalie = PREDEFINIOWANE_TALIE.get(frakcja);
        if (dostepneTalie == null || dostepneTalie.isEmpty()) {
            System.err.println("Brak predefiniowanych talii dla frakcji: " + frakcja);
            // Można zwrócić jakąś absolutnie domyślną talię lub null
            return null;
        }
        // Zwróć losowy element z listy dostępnych talii dla danej frakcji
        return dostepneTalie.get(random.nextInt(dostepneTalie.size()));
    }
}