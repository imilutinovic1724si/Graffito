package raf.graffito.dsw.model.elementi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class ImageElement extends SlideElement{

    @JsonIgnore
    private ImageProxy imageProxy;

    public ImageElement(){
        super();
        this.imageProxy = new ImageProxy();
    }
    public ImageElement(Point location, Dimension dimension, BufferedImage image){
        super(location, dimension);
        this.imageProxy = new ImageProxy(image);
    }

    // primena: Proxy Pattern
    @Override
    public void paint(Graphics2D g2d){
        BufferedImage image = imageProxy.getImage(); // Lazy loading
        if (image == null) return;

        AffineTransform oldTransform = g2d.getTransform();

        AffineTransform transform = new AffineTransform(oldTransform);
        transform.translate(getLocation().x + getDimension().width / 2.0, getLocation().y + getDimension().height / 2.0);
        transform.rotate(getRotation());
        transform.translate(-getDimension().width/2.0, -getDimension().height/2.0);
        g2d.setTransform(transform);

        g2d.drawImage(image, 0, 0, getDimension().width, getDimension().height, null);


        if (isSelected()){
            g2d.setColor(Color.BLUE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRect(0, 0, getDimension().width, getDimension().height);
        }
        g2d.setTransform(oldTransform);
    }

    @Override
    public ImageElement clone() {
        ImageElement cloned = (ImageElement) super.clone();
        cloned.imageProxy = new ImageProxy(this.imageProxy.getImage());
        return cloned;
    }

    // Čuvanje slike kao Base64 string
    @JsonProperty("imageData")
    public String getImageAsBase64(){
        return imageProxy.getBase64Data();
    }

    @JsonProperty("imageData")
    public void setImageFromBase64(String base64) {
        imageProxy.setBase64Data(base64);
    }

    @JsonIgnore
    public BufferedImage getImage() {
        return imageProxy.getImage();
    }

    @JsonIgnore
    public void setImage(BufferedImage image) {
        imageProxy.setImage(image);
    }
}