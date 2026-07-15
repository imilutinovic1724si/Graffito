package raf.graffito.dsw.bridge.space;

import raf.graffito.dsw.model.implementation.Slide;

public class SlideSpaceChecker extends SpaceChecker {

    public SlideSpaceChecker(CheckSpaceImplementation implementation) {
        super(implementation);
    }
    @Override
    public void displayStatus(Slide slide) {
        double percentage = getOccupiedPercentage(slide) * 100;
        boolean hasSpace = checkSpace(slide);

        String status = String.format(
                "[%s] Zauzeto: %.1f%% | Status: %s",
                implementation.getMethodName(),
                percentage,
                hasSpace ? "Ima prostora" : "Nema prostora"
        );

        System.out.println(status);
    }
    // Dodatne metode specifične za slajd
    public String getDetailedReport(Slide slide) {
        double occupied = getOccupiedPercentage(slide) * 100;
        double free = 100 - occupied;

        return String.format(
                "=== Izveštaj o prostoru na slajdu ===\n" + "Metoda: %s\n" + "Zauzeto: %.1f%%\n" + "Slobodno: %.1f%%\n" + "Potrebno: 20.0%%\n" + "Može dodati element: %s",
                implementation.getMethodName(),
                occupied,
                free,
                checkSpace(slide) ? "DA" : "NE"
        );
    }

}
