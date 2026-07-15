package raf.graffito.dsw.model.implementation;

import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.GraffNodeComposite;

public class Workspace extends GraffNodeComposite {

    public Workspace() {
        super("", null);
    }

    // Konkretna implementacija i nas root (parent = null)
    public Workspace(String name) {
        super(name, null);
    }

    // Imamo ogranicenje da Workspace moze sadrzati samo Project-e
    @Override
    public void addChild(GraffNode child) {
        if (child instanceof Project){
            super.addChild(child);
        }else{
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Workspace moze sadrzati samo Projecte");
        }
    }
}