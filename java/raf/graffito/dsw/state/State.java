package raf.graffito.dsw.state;

import java.awt.event.MouseEvent;

public interface State {

    // primena: State Pattern
    // svako stanje drugačije reaguje na akcije miša

    void mousePressed(MouseEvent e);
    void mouseReleased(MouseEvent e);
    void mouseDragged(MouseEvent e);
    void mouseClicked(MouseEvent e);
}
