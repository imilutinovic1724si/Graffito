package raf.graffito.dsw.model.implementation;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.GraffNodeComposite;

public class Presentation extends GraffNodeComposite {

    public Presentation() {
        super("", null);
    }

    // Konkretna implementacija cvora
    public Presentation(String name, GraffNode parent){
        super(name, parent);
    }

    // Imamo ogranicenje da Presentation moze sadrzati samo Slide-ove
    @Override
    public void addChild(GraffNode child) {
        if (child instanceof Slide){
            super.addChild(child);
        }else{
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Presentation moze sadrzati samo Slide-ove");
        }
    }

    // Odredjujemo broj Slide-ova u Presentation-u
    public int getSlideCount() {
        return getChildren().size();
    }
}