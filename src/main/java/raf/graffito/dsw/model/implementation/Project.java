package raf.graffito.dsw.model.implementation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.GraffNodeComposite;
import raf.graffito.dsw.observer.Message;
import raf.graffito.dsw.observer.MessageType;

public class Project extends GraffNodeComposite {
    // Konkretna implementacija cvora
    private String author;
    private int number;

    @JsonIgnore
    private String filePath; // NOVO - putanja gde je projekat sačuvan

    @JsonIgnore
    private boolean changed; // NOVO - da li je projekat promenjen posle čuvanja

    // default konstruktor za Jackson deserijalizaciju
    public Project() {
        super("", null);
        this.changed = false;
    }

    public Project(String name, GraffNode parent, String author){
        super(name, parent);
        this.author = author;
        this.number = 0;
        this.filePath = null;
        this.changed = false;
    }

    // Validacija Project-a posto mora imati barem jednu Presentation ili Slide
    public boolean isValid(){
        return !getChildren().isEmpty();
    }
    public void validate() {
        if (!isValid()) {
            throw new IllegalStateException("Project '" + getName() + "' mora imati barem jednu prezentaciju ili slajd!");
        }
    }

    // NOVO -> SVUDA DODAJEMO markAsChanged()
    //Imamo ogranicenje da Project sadrzi ili Presentation ili Slide
    @Override
    public void addChild(GraffNode child){
        if(child instanceof  Presentation || child instanceof Slide){
            super.addChild(child);
            updateSlideCount(); // bitno nam je da znamo za polje number
            markAsChanged();
        }else{
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Project moze sadrzati samo Presentation ili Slide");
        }
    }

    //Imamo ogranicenje da Project ne sme biti prazan
    @Override public void removeChild(GraffNode child){
        if (getChildren().size() <= 1){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Ne moze se izbrisati poslednji element, jer Project mora imati barem jedan");
            return;
        }
        super.removeChild(child);
        updateSlideCount(); // opet moramo da znamo zbog number-a
        markAsChanged();
    }

    // Ovo imamo zbog naseg polja number koje oznacava broj Slide-ova u Project-u
    private void updateSlideCount(){
        int count = 0;
        for (GraffNode child : getChildren()) {
            if (child instanceof Slide){
                count++;
            } else if (child instanceof Presentation){
                count += ((Presentation) child).getSlideCount();
            }
        }
        this.number = count;
    }

    // vodimo racuna ukoliko se menjaju autor i title korisnik mora biti obavesten
    public void setName(String name) {
        String oldName= super.getName();
        super.setName(name);
        markAsChanged();
        notifySubscribers(new Message("Naslov promenjen u "+name, MessageType.INFO));
    }
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        String oldAuthor = this.author;
        this.author = author;
        markAsChanged();
        notifySubscribers(new Message("Auto promenjen u "+author, MessageType.INFO));
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public boolean isChanged() {
        return changed;
    }
    public void setChanged(boolean changed) {
        this.changed = changed;
    }


    // NOVO za promenu i čuvanje
    public void markAsChanged() {
        this.changed = true;
        System.out.println("Projekat " + getName() + " označen je kao promenjen.");
    }

    public void markAsSaved(){
        this.changed = false;
        System.out.println("Projekat " + getName() + " označen je kao sačuvan.");
    }
}
