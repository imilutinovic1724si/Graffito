package raf.graffito.dsw.model.decorator;

import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.GraffNodeComposite;

import java.awt.*;
import java.util.List;

public class ColorDecorator extends GraffNodeComposite {
    // primenjen: Decorator sablon (primer za kafu)

    //Originalni cvor koji obmotavamo
    private GraffNode decoratedNode;
    private Color color;


    public ColorDecorator(GraffNode node) {
        super(node.getName(),node.getParent());
        this.decoratedNode = node;
        this.color = null; //posto na pocetku nema boju

    }

    // nove funkcionalnosti -> dodaci
    public Color getColor() {
        return color;
    }
    public void setColor(Color color) {
        this.color = color;
    }
    public GraffNode getDecoratedNode() {
        return decoratedNode;
    }

    // prosledjujemo sve metode koje se nalaze u originalnom cvoru
    @Override
    public String getName(){
        return decoratedNode.getName();
    }
    @Override
    public void setName(String name){
        decoratedNode.setName(name);
    }
    @Override
    public GraffNode getParent() {
        return decoratedNode.getParent();
    }
    @Override
    public void setParent(GraffNode parent) {
        decoratedNode.setParent(parent);
    }
    @Override
    public void addChild(GraffNode child) {
        if (decoratedNode instanceof GraffNodeComposite) {
            ((GraffNodeComposite) decoratedNode).addChild(child);
        }
    }
    @Override
    public void removeChild(GraffNode child) {
        if (decoratedNode instanceof GraffNodeComposite) {
            ((GraffNodeComposite) decoratedNode).removeChild(child);
        }
    }
    @Override
    public List<GraffNode> getChildren() {
        if (decoratedNode instanceof GraffNodeComposite) {
            return ((GraffNodeComposite) decoratedNode).getChildren();
        }
        return super.getChildren();
    }
    @Override
    public int getChildCount() {
        if (decoratedNode instanceof GraffNodeComposite) {
            return ((GraffNodeComposite) decoratedNode).getChildCount();
        }
        return 0;
    }

}
