package raf.graffito.dsw.model.implementation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import raf.graffito.dsw.model.GraffLeaf;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.elementi.SlideElement;
import raf.graffito.dsw.observer.Message;
import raf.graffito.dsw.observer.MessageType;

import java.util.ArrayList;
import java.util.List;

public class Slide extends GraffLeaf {
    private int slideNumber;
    private List<SlideElement> elementi;

    // Default konstruktor za Jackson
    public Slide() {
        super("", null);
        this.elementi = new ArrayList<>();
    }

    public Slide(String name, GraffNode parent){
        super(name, parent);
        this.elementi = new ArrayList<>();
    }

    // geteri i seteri
    public int getSlideNumber() {
        return slideNumber;
    }
    public void setSlideNumber(int slideNumber) {
        this.slideNumber = slideNumber;
    }

    // metode za rad sa elementima
    public void addElement(SlideElement element) {
        if (element != null && !elementi.contains(element)) {
            elementi.add(element);
            notifySubscribers(new Message(
                    "Element dodat na slajd: " + getName(),
                    MessageType.INFO
            ));
        }
    }

    public void removeElement(SlideElement element) {
        if (elementi.remove(element)) {
            notifySubscribers(new Message(
                    "Element uklonjen sa slajda: " + getName(),
                    MessageType.INFO
            ));
        }
    }

    public List<SlideElement> getElementi() {
        return elementi;
    }

    @JsonIgnore
    public List<SlideElement> getSelectedElements() {
        List<SlideElement> selected = new ArrayList<>();
        for (SlideElement element : elementi) {
            if (element.isSelected()) {
                selected.add(element);
            }
        }
        return selected;
    }

    public void clearSelection(){
        for (SlideElement element : elementi) {
            element.setSelected(false);
        }
    }
}