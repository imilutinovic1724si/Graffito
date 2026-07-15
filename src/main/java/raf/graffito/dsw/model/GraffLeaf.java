package raf.graffito.dsw.model;

public abstract class GraffLeaf extends GraffNode {
    // Cvorovi koji ne mogu imati decu: Slide

    public GraffLeaf(String name, GraffNode parent) {
        super(name, parent);
    }

    // Ova metoda nece Override-ovati addChlidren() i removeChildren() -> Exceptione smo uhvatili u GraffNode-u

    @Override
    public GraffNode findByName(String name) {
        // provarava samo samog sebe
        if (this.getName().equals(name)) {
            return this;
        }
        return null;
    }
}
