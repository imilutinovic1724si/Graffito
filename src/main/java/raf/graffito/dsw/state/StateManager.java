package raf.graffito.dsw.state;

import raf.graffito.dsw.gui.swing.SlideView;

// upravljačka klasa -> upravlja trenutnim stanjem
public class StateManager {

    private State currentState;
    private SlideView slideView;

    public StateManager(SlideView slideView) {
        this.slideView = slideView;
        this.currentState = new SelectState(slideView); // defaultno stanje je selektovanje
    }

    // geteri i seteri
    public State getCurrentState() {
        return currentState;
    }
    public void setCurrentState(State state) {
        this.currentState = state;
    }

    // metode za promenu stanja
    public void setAddState(){
        currentState = new AddState(slideView);
    }
    public void setSelectState(){
        currentState = new SelectState(slideView);
    }
    public void setMoveState(){
        currentState = new MoveState(slideView);
    }
    public void setDeleteState(){ currentState = new DeleteState(slideView); }
    public void setResizeState(){
        currentState = new ResizeState(slideView);
    }

}
