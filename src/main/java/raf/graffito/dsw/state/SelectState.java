package raf.graffito.dsw.state;

import raf.graffito.dsw.gui.swing.SlideView;
import raf.graffito.dsw.model.elementi.SlideElement;

import java.awt.*;
import java.awt.event.MouseEvent;

public class SelectState implements  State{

    private SlideView slideView;
    private Point selectionStartPoint; // pocetak pravougaonika -> gde je korisnik počeo da vuče miša
    private Point selectionEndPoint; // kraj pravougaonika -> gde se nalazi miš kada ga ne prevlači
    private boolean isDraggingSelection; // da li dragujemo pravougaonik -> da li pomera miša

    public SelectState(SlideView slideView) {
        this.slideView = slideView;
        this.isDraggingSelection = false;
    }


    @Override
    public void mousePressed(MouseEvent e) {
        if (slideView == null) return;

        Point clickPoint = e.getPoint(); // tačka na koju je korisnik kliknuo
        boolean found = false; // fleg koji označava da li smo našli element na mestu klika

        // proveravamo da li smo kliknuli na neki element
        for (SlideElement element : slideView.getSlide().getElementi()) {
            if (element.contains(clickPoint)) { // poziva contains iz SlideElement klase

                if (e.isControlDown()) { // da li korisnik drži CTRL
                    element.setSelected(!element.isSelected()); // obrni stanje selektovanja
                } else {
                    // ako nije pritisnut CTRL disselektuju se svi elementi na slajdu, a selektuje se samo onaj na kojem je KLIK
                    slideView.getSlide().clearSelection();
                    element.setSelected(true);
                }

                found = true;
                break;
            }
        }

        // ako nismo kliknuli ni na šta, poništavamo selekciju
        if (!found && !e.isControlDown()) {
            slideView.getSlide().clearSelection();
        }

        //pocetak dragovanja pravougaonika
        selectionStartPoint = clickPoint;
        selectionEndPoint = clickPoint;
        isDraggingSelection = true;
    }
    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDraggingSelection){
            // azuriramo kraj pravougaonika
            selectionEndPoint = e.getPoint();
            // kreira pravougaonik i šalje ga na prikaz
            slideView.setSelectionRectangle(createSelectionRectangle());
            slideView.repaint();
        }
    }
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDraggingSelection){
            // kreiranje finalnog pravougaonika
            Rectangle selectionRect = createSelectionRectangle();

            for (SlideElement element : slideView.getSlide().getElementi()){
                // selektujemo elemente obuhvaćene pravougaonikom
                if (selectionRect.intersects(element.getBounds())){
                    element.setSelected(true);
                }
            }

            // vraćamo na početno stanje ostaju samo selektovani elementi
            isDraggingSelection = false;
            selectionStartPoint = null;
            selectionEndPoint = null;
            slideView.setSelectionRectangle(null);
            slideView.repaint();
        }
    }


    @Override
    public void mouseClicked(MouseEvent e) {
        Point p = e.getPoint();

        // obrisi staru selekciju
        slideView.getSlide().clearSelection();

        // selektuj samo novi element
        for (SlideElement element : slideView.getSlide().getElementi()) {
            if (element.contains(p)) {
                element.setSelected(true);
                break;
            }
        }
        slideView.repaint();
    }


    // privatna metoda za kreiranje pravougaonika
    private Rectangle createSelectionRectangle() {
        if (selectionStartPoint == null || selectionEndPoint == null){
            return null;
        }

        int x = Math.min(selectionStartPoint.x, selectionEndPoint.x);
        int y = Math.min(selectionStartPoint.y, selectionEndPoint.y);
        int width = Math.abs(selectionEndPoint.x - selectionStartPoint.x);
        int height = Math.abs(selectionEndPoint.y - selectionStartPoint.y);

        return new Rectangle(x, y, width, height);
    }
}
