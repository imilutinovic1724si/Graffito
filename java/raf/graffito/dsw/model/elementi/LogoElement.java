package raf.graffito.dsw.model.elementi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

public class LogoElement extends SlideElement{

    @JsonIgnore
    private Color color;

    public LogoElement() {
        super();
        this.color = Color.ORANGE;
    }
    public LogoElement(Point location, Dimension dimension){
        super(location, dimension);
        this.color = Color.ORANGE;
    }

    @Override
    public void paint(Graphics2D g) {
        AffineTransform oldAt = g.getTransform();

        System.out.println(String.format(
                "LogoElement.paint() - pozicija: (%d,%d), dimenzija: %dx%d, transform scale: %.2fx, %.2fx",
                getLocation().x, getLocation().y,
                getDimension().width, getDimension().height,
                oldAt.getScaleX(), oldAt.getScaleY()
        ));

        AffineTransform at = new AffineTransform(oldAt);
        at.translate(getLocation().x + getDimension().width / 2.0, getLocation().y + getDimension().height / 2.0);
        at.rotate(getRotation());
        at.translate(-getDimension().width / 2.0, -getDimension().height / 2.0);
        g.setTransform(at);

        Path2D.Double star = createStar(getDimension().width, getDimension().height);
        g.setColor(color);
        g.fill(star);
        g.setColor(Color.BLACK);
        g.draw(star);

        if (isSelected()){
            g.setColor(Color.BLUE);
            g.setStroke(new BasicStroke(2));
            g.drawRect(0,0,getDimension().width,getDimension().height);
        }

        g.setTransform(oldAt);
    }

    private Path2D.Double createStar(int width, int height){
        Path2D.Double star = new Path2D.Double();
        double centerX = width / 2.0;
        double centerY = height / 2.0;
        double outerRadius = Math.min(width, height)/2.0;
        double innerRadius = outerRadius/2.5;

        for (int i = 0; i<10; i++){
            double angle = Math.PI/2 - (i * Math.PI/5);
            double radius = (i%2 == 0) ?outerRadius : innerRadius;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);

            if (i==0){
                star.moveTo(x, y);
            }else{
                star.lineTo(x, y);
            }
        }
        star.closePath();
        return star;
    }

    @Override
    public LogoElement clone(){
        LogoElement cloned = (LogoElement)super.clone();
        cloned.color = this.color;
        return cloned;
    }

    // ZA SERIJALIZACIJU boje
    @JsonProperty("colorRGB")
    public int getColorRGB() {
        return color != null ? color.getRGB() : Color.ORANGE.getRGB();
    }

    @JsonProperty("colorRGB")
    public void setColorRGB(int rgb) {
        this.color = new Color(rgb, true);
    }


    // geteri i seteri
    @JsonIgnore
    public Color getColor() {
        return color;
    }
    @JsonIgnore
    public void setColor(Color color) {
        this.color = color;
    }
}