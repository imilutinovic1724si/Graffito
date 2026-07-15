package raf.graffito.dsw.model;

import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.Slide;
import raf.graffito.dsw.model.implementation.Workspace;

public class GraffRepository {
    // primenjen: Factory method
    private Workspace workspace;
    private NodeFactory nodeFactory;

    public GraffRepository(){
        workspace = new Workspace("Workspace");
        nodeFactory = new NodeFactory();
    }

    public Workspace getWorkspace() {
        return workspace;
    }

    // Factory metode za kreiranje cvorova
    public Project createProject(String name, GraffNode parent, String author){
        return nodeFactory.createProject(name, parent, author);
    }
    public Presentation createPresentation(String name, GraffNode parent){
        return nodeFactory.createPresentation(name, parent);
    }
    public Slide createSlide(String name, GraffNode parent){
        return nodeFactory.createSlide(name, parent);
    }


    public NodeFactory getNodeFactory() {
        return nodeFactory;
    }


    // Inner klasa koja implementira Factory Method
    public static class NodeFactory {
        public Project createProject(String name, GraffNode parent, String author){
            if (name == null || name.trim().isEmpty()){
                throw new IllegalArgumentException("Naziv projekta ne sme biti prazan");
            }
            if (author == null || author.trim().isEmpty()){
                throw new IllegalArgumentException("Autor projekta ne sme biti prazan");
            }
            return new Project(name.trim(), parent, author.trim());
        }

        public Presentation createPresentation(String name, GraffNode parent){
            if (name == null || name.trim().isEmpty()){
                throw new IllegalArgumentException("Naziv prezentacije ne sme biti prazan");
            }
            return new Presentation(name.trim(), parent);
        }

        public Slide createSlide(String name, GraffNode parent){
            if (name == null || name.trim().isEmpty()){
                throw new IllegalArgumentException("Naziv slajda ne sme biti prazan");
            }
            return new Slide(name.trim(), parent);
        }
    }
}
