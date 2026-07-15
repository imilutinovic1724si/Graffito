package raf.graffito.dsw.model.elementi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class TextElement extends SlideElement {

    private String text;
    @JsonIgnore
    private Font font;
    @JsonIgnore
    private Color color;

    // Default konstruktor za Jackson
    public TextElement() {
        super();
        this.text = "";
        this.font = new Font("Arial", Font.PLAIN, 12);
        this.color = Color.BLACK;
    }
    public TextElement(Point location, Dimension dimension, String text) {
        super(location, dimension);
        this.text = text;
        this.font = new Font("Arial", Font.PLAIN, 12);
        this.color = Color.BLACK;
    }

    @Override
    public void paint(Graphics2D g) {
        if (text == null || text.isEmpty()) {
            return;
        }
        // sačuvaj stari transform
        AffineTransform oldTransform = g.getTransform();
        // kreiraj novi transform za rotaciju
        AffineTransform transform = new AffineTransform(oldTransform);

        // 1. pomeri koordinatni sistem u centar elementa
        transform.translate(getLocation().x + getDimension().width / 2.0, getLocation().y + getDimension().height / 2.0);

        // 2. rotiraj
        transform.rotate(getRotation());

        // 3. vrati nazad
        transform.translate(-getDimension().width / 2.0, -getDimension().height / 2.0);

        g.setTransform(transform);

        // nacrtaj tekst
        g.setFont(font);
        g.setColor(color);
        g.drawString(text, 5, getDimension().height / 2);

        // ako je tekst selektovan nacrtaj plavi okvir
        if (isSelected()){
            g.setColor(Color.BLUE);
            g.setStroke(new BasicStroke(2));
            g.drawRect(0, 0, getDimension().width, getDimension().height);
        }

        // vrati staru transformaciju
        g.setTransform(oldTransform);
    }

    @Override
    public TextElement clone() {
        TextElement cloned = (TextElement) super.clone();
        cloned.text = this.text;
        cloned.font = this.font;
        cloned.color = this.color;
        return cloned;
    }


    // ZA SERIJALIZACIJU fonta
    @JsonProperty("fontName")
    public String getFontName() {
        return font != null ? font.getName() : "Arial";
    }

    @JsonProperty("fontName")
    public void setFontName(String name) {
        if (this.font != null) {
            this.font = new Font(name, this.font.getStyle(), this.font.getSize());
        } else {
            this.font = new Font(name, Font.PLAIN, 12);
        }
    }

    @JsonProperty("fontStyle")
    public int getFontStyle() {
        return font != null ? font.getStyle() : Font.PLAIN;
    }

    @JsonProperty("fontStyle")
    public void setFontStyle(int style) {
        if (this.font != null) {
            this.font = new Font(this.font.getName(), style, this.font.getSize());
        } else {
            this.font = new Font("Arial", style, 12);
        }
    }

    @JsonProperty("fontSize")
    public int getFontSize() {
        return font != null ? font.getSize() : 12;
    }

    @JsonProperty("fontSize")
    public void setFontSize(int size) {
        if (this.font != null) {
            this.font = new Font(this.font.getName(), this.font.getStyle(), size);
        } else {
            this.font = new Font("Arial", Font.PLAIN, size);
        }
    }

    // ZA SERIJALIZACIJU boja
    @JsonProperty("colorRGB")
    public int getColorRGB() {
        return color != null ? color.getRGB() : Color.BLACK.getRGB();
    }

    @JsonProperty("colorRGB")
    public void setColorRGB(int rgb) {
        this.color = new Color(rgb, true); // true = includes alpha
    }




    // geteri i seteri
    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }
    @JsonIgnore
    public Font getFont() {
        return font;
    }
    @JsonIgnore
    public void setFont(Font font) {
        this.font = font;
    }
    @JsonIgnore
    public Color getColor() {
        return color;
    }
    @JsonIgnore
    public void setColor(Color color) {
        this.color = color;
    }
}