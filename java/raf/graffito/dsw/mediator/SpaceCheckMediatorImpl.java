package raf.graffito.dsw.mediator;

import raf.graffito.dsw.bridge.space.CheckSpaceImplementation;
import raf.graffito.dsw.bridge.space.PixelMatrixImplementation;
import raf.graffito.dsw.bridge.space.SimpleAreaImplementation;
import raf.graffito.dsw.bridge.space.SlideSpaceChecker;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.model.implementation.Slide;

public class SpaceCheckMediatorImpl implements SpaceCheckMediator {

    private SpaceCheckUI ui;
    private SlideSpaceChecker checker;
    private Slide currentSlide;

    // Dostupne implementacije
    private CheckSpaceImplementation simpleArea;
    private CheckSpaceImplementation pixelMatrix;

    public SpaceCheckMediatorImpl() {
        this.simpleArea = new SimpleAreaImplementation();
        this.pixelMatrix = new PixelMatrixImplementation();
    }

    @Override
    public void registerUI(SpaceCheckUI ui) {
        this.ui = ui;
    }

    @Override
    public void registerChecker(SlideSpaceChecker checker) {
        this.checker = checker;
    }

    @Override
    public void onMethodChanged(String methodName) {
        if (checker == null) return;

        // Promeni implementaciju na osnovu izbora
        if (methodName.contains("Simple")) {
            checker.setImplementation(simpleArea);
            System.out.println("Promenjena metoda na: Simple Area");
        } else if (methodName.contains("Pixel")) {
            checker.setImplementation(pixelMatrix);
            System.out.println("Promenjena metoda na: Pixel Matrix");
        }

        // Ažuriraj UI
        if (currentSlide != null && ui != null) {
            ui.updateDisplay(currentSlide, checker);
        }
    }
    @Override
    public void onCheckRequested() {
        if (currentSlide == null) {
            ApplicationFramework.getInstance().getMessageGenerator()
                    .generateWarning("Nema otvorenog slajda");
            return;
        }

        if (checker == null) return;

        // Proveri prostor
        boolean hasSpace = checker.checkSpace(currentSlide);
        double percentage = checker.getOccupiedPercentage(currentSlide) * 100;

        // Prikaz rezultata
        String message = String.format(
                "Zauzeto: %.1f%%\n%s",
                percentage,
                hasSpace ? "Ima dovoljno prostora (≥20% slobodno)"
                        : "Nema dovoljno prostora"
        );

        if (hasSpace) {
            ApplicationFramework.getInstance().getMessageGenerator()
                    .generateInfo(message);
        } else {
            ApplicationFramework.getInstance().getMessageGenerator()
                    .generateWarning(message);
        }

        // Prikaži u konzoli
        checker.displayStatus(currentSlide);

        // Ažuriraj UI
        if (ui != null) {
            ui.updateDisplay(currentSlide, checker);
        }
    }
    @Override
    public void onSlideChanged(Slide newSlide) {
        this.currentSlide = newSlide;

        // Ažuriraj UI odmah
        if (newSlide != null && checker != null && ui != null) {
            ui.updateDisplay(newSlide, checker);
        }
    }
}
