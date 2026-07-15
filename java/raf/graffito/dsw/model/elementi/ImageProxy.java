package raf.graffito.dsw.model.elementi;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class ImageProxy {
    private BufferedImage realImage; // stvarna slika u memoriji
    private String base64Data; // slika kao Base64 string
    private boolean isLoaded; // da li je slika učitana u memoriju

    public ImageProxy() {
        this.isLoaded = false;
    }
    public ImageProxy(BufferedImage image) {
        this.realImage = image;
        this.isLoaded = true;
    }

    // Lazy loading - ucitava sliku tek kada je potrebna
    public BufferedImage getImage() {
        if (!isLoaded && base64Data != null) {
            loadImageFromBase64();
        }
        return realImage;
    }

    public void setImage(BufferedImage image) {
        this.realImage = image;
        this.isLoaded = true;
        this.base64Data = null;
    }

    // za serijalizaciju konvertuj sliku u Base64
    public String getBase64Data() {
        if (base64Data == null && realImage != null) {
            encodeToBase64();
        }
        return base64Data;
    }

    public void setBase64Data(String base64) {
        this.base64Data = base64;
        this.isLoaded = false;
        this.realImage = null;
    }


    // Učitaj sliku iz Base64 stringa
    private void loadImageFromBase64() {
        if (base64Data == null || base64Data.isEmpty()) return;
        try {
            byte[] bytes = Base64.getDecoder().decode(base64Data);
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            this.realImage = ImageIO.read(bais);
            this.isLoaded = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Konvertuj sliku u Base64 string
    private void encodeToBase64() {
        if (realImage == null) return;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(realImage, "png", baos);
            byte[] bytes = baos.toByteArray();
            this.base64Data = Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public boolean isLoaded() {
        return isLoaded;
    }
}