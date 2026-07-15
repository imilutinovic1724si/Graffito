package raf.graffito.dsw.model;

import raf.graffito.dsw.observer.Message;
import raf.graffito.dsw.observer.MessageType;

import java.util.ArrayList;
import java.util.List;

public abstract class GraffNodeComposite extends GraffNode {
    // Cvorovi koji mogu imati decu: Workspace, Project, Presentation

    private List<GraffNode> children;

    public GraffNodeComposite(String name, GraffNode parent) {
        super(name, parent);
        this.children = new ArrayList<>();
    }

    // Metode iz GraffNode-a
    @Override
    public void addChild(GraffNode child) {
        if (child != null && !children.contains(child)) {
            children.add(child);
            child.setParent(this);
            // dodaje i salje obavestenje o dodavanju
            notifySubscribers(new Message("Dodat cvor: " + child.getName(), MessageType.INFO));
        }
    }
    @Override
    public void removeChild(GraffNode child) {
        if (child != null && children.contains(child)){
            children.remove(child);
            child.setParent(null);
            // brise i salje obavestenje o brisanju
            notifySubscribers(new Message("Obrisan cvor: " + child.getName(), MessageType.INFO));
        }
    }
    @Override
    public GraffNode findByName(String name) {
        // Proveravace i sebe i svoju decu
        if (this.getName().equals(name)) {
            return this;
        }
        for (GraffNode child : children) {
            GraffNode result = child.findByName(name);
            if(result != null) {
                return result;
            }
        }
        return null;
    }

    // geteri
    public List<GraffNode> getChildren() {
        return children;
    }
    public int getChildCount() {
        return children.size();
    }

}
