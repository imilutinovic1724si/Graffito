package raf.graffito.dsw.model.elementi;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.awt.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextElement.class, name = "text"),
        @JsonSubTypes.Type(value = LogoElement.class, name = "logo"),
        @JsonSubTypes.Type(value = ImageElement.class, name = "image")
})

// Primena: Prototype Pattern
public abstract class SlideElement implements Cloneable {

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private Point location; // lokacija el

    @JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
    private Dimension dimension; // dimenzija el

    private double rotation; // rotacija u radijanima

    @JsonIgnore
    private boolean selected; // da li je el selektovan

    // Default konstruktor za Jackson
    public SlideElement() {
        this.location = new Point(0, 0);
        this.dimension = new Dimension(100, 100);
        this.rotation = 0.0;
        this.selected = false;
    }
    public SlideElement(Point location, Dimension dimension){
        this.location = location;
        this.dimension = dimension;
        this.rotation = 0.0;
        this.selected = false;
    }

    // kloniranje elemenata
    @Override
    public SlideElement clone() {
        try {
            SlideElement cloned = (SlideElement) super.clone();
            cloned.location = new Point(this.location);
            cloned.dimension = new Dimension(this.dimension);
            return cloned;
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
            return null;
        }
    }

    // svaki element mora da zna kako da se nacrta
    public abstract void paint(Graphics2D g2d);

    // da li klik na tačku p pogađa element
    public boolean contains(Point p){
        Rectangle bounds = new Rectangle(location, dimension);
        return bounds.contains(p);
    }

    // geteri i seteri
    public Point getLocation() {
        return location;
    }
    public void setLocation(Point location) {
        this.location = location;
    }
    public Dimension getDimension() {
        return dimension;
    }
    public void setDimension(Dimension dimension) {
        this.dimension = dimension;
    }
    public double getRotation() {
        return rotation;
    }
    public void setRotation(double rotation) {
        this.rotation = rotation;
    }
    @JsonIgnore
    public boolean isSelected() {
        return selected;
    }
    @JsonIgnore
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    @JsonIgnore
    public Rectangle getBounds() {
        return new Rectangle(location, dimension);
    }
}