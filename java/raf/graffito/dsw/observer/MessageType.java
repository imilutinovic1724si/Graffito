package raf.graffito.dsw.observer;

public enum MessageType {
    ERROR("GRESKA"),
    WARNING("UPOZORENJE"),
    INFO("OBAVESTENJE");

    private final String displayName;

    MessageType(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
