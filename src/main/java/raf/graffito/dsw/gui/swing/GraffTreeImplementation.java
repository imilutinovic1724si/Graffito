package raf.graffito.dsw.gui.swing;

import com.sun.tools.javac.Main;
import raf.graffito.dsw.core.ApplicationFramework;
import raf.graffito.dsw.model.GraffNode;
import raf.graffito.dsw.model.GraffNodeComposite;
import raf.graffito.dsw.model.GraffRepository;
import raf.graffito.dsw.model.implementation.Presentation;
import raf.graffito.dsw.model.implementation.Project;
import raf.graffito.dsw.model.implementation.Slide;
import raf.graffito.dsw.model.implementation.Workspace;


import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


public class GraffTreeImplementation extends JTree implements GraffTree {

    private DefaultTreeModel treeModel; // model stabla
    private GraffRepository repository; // koji sadrzi Workspace kao nas root i NodeFactory za kreiranje drugih cvorova

    public GraffTreeImplementation(){
        // uzimamo nas repozitorijum
        this.repository = ApplicationFramework.getInstance().getRepository();
        //generisemo stablo od root workspacea
        DefaultMutableTreeNode root = generateTree(repository.getWorkspace());
        treeModel = new DefaultTreeModel(root);
        // postavlja nase stalo na ekran
        setModel(treeModel);
        //menajanje naziva cvorova smo stavili da ide preko dugmeta
        setEditable(false);
        // setuje metodu za promenu redosleda slajdova
        setupDragAndDrop();

        //dodato double klik otvara prez
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e){
                if (e.getClickCount() == 2) {
                    GraffNode selected = getSelectedNode();
                    if (selected instanceof  Project){
                        MainFrame.getInstance().getProjectView().openProject((Project) selected);
                    }
                }
            }
        });
    }

     // -------------------------------------- IMPLEMENTACIJA METODA IZ INTERFEJSA -------------------------------------- //

    // METODA ZA GENERISANJE STABLA
    @Override
    public DefaultMutableTreeNode generateTree(GraffNode graffNode){
        // kreira cvor
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(graffNode);

        // ako cvor ima decu dodaje i decu
        if (graffNode instanceof GraffNodeComposite){
            GraffNodeComposite composite = (GraffNodeComposite) graffNode;
            for (GraffNode child : composite.getChildren()){
                treeNode.add(generateTree(child));
            }
        }
        return treeNode;
    }

    // DODAVANJE I KREIRANJE NOVIH CVOROVA ZA STABLO
    @Override
    public void addChild(GraffNode parent) {
        if (parent == null){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Morate selektovati roditeljski cvor");
            return;
        }
        if (parent instanceof Workspace){
            //Workspace ima samo Project
            createProject(parent);
        }else if(parent instanceof Project){
            //Project moze da ima Presentation ili Slide
            //Pitamo korisnika sta zeli
            whichProjectChild(parent);
        }else if(parent instanceof Presentation){
            //Presentation ima samo Slide
            createSlide(parent);
        }else{
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Slide ne moze imati decu!");
        }
    }

    private void whichProjectChild(GraffNode parent){
        String[] options = {"Presentation", "Slide"};
        int izbor = JOptionPane.showOptionDialog(this, "Sta zelite da dodate u projekat?", "Dodavanje", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (izbor == 0){
            createPresentation(parent);
        }else if(izbor == 1){
            createSlide(parent);
        }
    }

    // KORISTI FACTORY iz Graff Repository za kreiranje novih cvorova
    private void createProject(GraffNode parent){
        String name = JOptionPane.showInputDialog(this,"Unesite naziv projekta");
        if (name == null || name.trim().isEmpty()) {
            ApplicationFramework.getInstance().getMessageGenerator().generateWarning("Naziv projekta ne sme biti prazan");
            return;
        }
        String author = JOptionPane.showInputDialog(this, "Unesite ime autora");
        if (author == null || author.trim().isEmpty()){
            ApplicationFramework.getInstance().getMessageGenerator().generateWarning("Ime autora ne sme biti prazno");
            return;
        }

        // proverava dal imamo vec taj naziv za projekat
        if (proveriIme(parent, name)){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Projekat sa ovim imenom vec posotji");
            return;
        }

       // kreiramo projekat
        Project project = repository.createProject(name, parent, author);
        parent.addChild(project);

        // auzuriramo nase stablo
        refreshTree();

        ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Projekat '"+name+" ' uspesno kreiran");
    }
    private void createPresentation(GraffNode parent){
        String name = JOptionPane.showInputDialog(this, "Unesite naziv prezentacije:");
        if (name == null || name.trim().isEmpty()){
            ApplicationFramework.getInstance().getMessageGenerator().generateWarning("Naziv prezentacije ne moze biti prazan");
            return;
        }

        if (proveriIme(parent, name)){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Prezentacija sa ovim imenom vec posotji");
            return;
        }

        Presentation presentation = repository.createPresentation(name, parent);
        parent.addChild(presentation);

        refreshTree();

        ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Prezentacija '"+name+" ' uspesno kreirana");

    }
    private void createSlide(GraffNode parent){
        String name = JOptionPane.showInputDialog(this, "Unesite naziv slajda");
        if (name == null || name.trim().isEmpty()){
            ApplicationFramework.getInstance().getMessageGenerator().generateWarning("Naziv slajda ne moze biti prazan");
            return;
        }

        if (proveriIme(parent, name)){
            ApplicationFramework.getInstance().getMessageGenerator()
                    .generateError("Slide sa ovim imenom vec postoji");
            return;
        }

        Slide slide = repository.createSlide(name, parent);
        parent.addChild(slide);

        refreshTree();

        ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Slide '"+name+" ' uspesno kreiran");
    }


    // provera duplikata imena
    private boolean proveriIme(GraffNode parent, String name){
        if (!(parent instanceof  GraffNodeComposite)){ return false; }

        GraffNodeComposite composite = (GraffNodeComposite) parent;
        for (GraffNode child : composite.getChildren()){

            if(child.getName().equals(name)){
                return true;
            }
        }
        return false;
    }

    // BRISANJE CVORA IZ STABLA
    @Override
    public  void deleteNode(){
        GraffNode selected = getSelectedNode();
        if (selected == null){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Niste selektovali cvor za brisanje");
            return;
        }
        if (selected instanceof Workspace){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Ne mozete obrisati Workspace");
            return;
        }
        GraffNode parent = selected.getParent();
        if (parent != null){
            if (parent instanceof Project){
                GraffNodeComposite composite = (GraffNodeComposite) parent;
                if(composite.getChildCount() <= 1){
                    ApplicationFramework.getInstance().getMessageGenerator().generateError("Project mora imati bar jednu prezentaciju ili slide");
                    return;
                }
            }
            int result = JOptionPane.showConfirmDialog(this, "Da li zelite da brisete cvor '"+selected.getName()+"'?", "Potvrda brisanja", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION){
                parent.removeChild(selected);
                refreshTree();
                ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Cvor '"+selected.getName()+"' obrisan");
            }
        }
    }

    // PREIMENOVANJE CVORA
    @Override
    public void renameNode(GraffNode node, String noviName){
        if (node == null || noviName == null || noviName.trim().isEmpty()) {
            ApplicationFramework.getInstance().getMessageGenerator().generateWarning("Naziv ne sme biti prazan");
            return;
        }
        if (node instanceof Workspace){
            ApplicationFramework.getInstance().getMessageGenerator().generateError("Ne mozete preimenovati Workspace");
            return;
        }

        // proveravamo duplikat imena
        GraffNode parent = node.getParent();
        if (parent != null && proveriIme(parent, noviName)){
                    ApplicationFramework.getInstance().getMessageGenerator().generateError("Cvor sa ovim imenom vec postoji");
                    return;
            }
        String staroIme = node.getName();
        node.setName(noviName);
        refreshTree();

        ApplicationFramework.getInstance().getMessageGenerator().generateInfo("Cvor smo preimenovali iz '" +staroIme+"' u '"+noviName+"'");
    }

    // VRACANJE SELEKTOVANOG CVORA
    @Override
    public GraffNode getSelectedNode(){
        DefaultMutableTreeNode selected = (DefaultMutableTreeNode) getLastSelectedPathComponent(); // metoda iz JTree-a
        if (selected == null) return null;
        // izvlaci nas GraffNode
        Object userObject = selected.getUserObject();

        // uslovno se nikad nece desiti, al da bi nam javio gresku
        if (userObject instanceof String){
            return null;
        }

        if (userObject instanceof GraffNode){
            return (GraffNode) userObject;
        }
        return null;
    }


    // -------------------------------------- REFRESH STABLA -------------------------------------- //

    // REFRESH STABLA nakon svake promene
    private void refreshTree() {
        // uzimanje trenutnog root-a
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
        GraffNode workspace = (GraffNode) root.getUserObject();

        // regenerisemo stablo -> kreiramo novo od nule
        DefaultMutableTreeNode newRoot = generateTree(workspace);
        // postavlja novi root
        treeModel.setRoot(newRoot);
        // rilouduje model
        treeModel.reload();
    }

     // -------------------------------------- DRAG AND DROP -------------------------------------- //
     // drag and drop ima 3 faze:
     // 1. Drag -> korisnik povlaci element
     // 2. Provera -> da li element moze da se premesti tamo gde korisnik zeli
     // 3. Drop -> element se premesta na novu lokaciju

     private void setupDragAndDrop() {
         setDragEnabled(true); // elementi mogu da se povlace
         setDropMode(DropMode.ON_OR_INSERT); // moze da se spusti na ili ispod nekog elementa
         setTransferHandler(new SlideTreeTransferHandler()); // postavlja nasu dole navedenu logiku...
     }

     private class SlideTreeTransferHandler extends TransferHandler {
         private DataFlavor nodesFlavor; // "ukus" podatka koji zelimo da prenesemo
         private DataFlavor[] flavors = new DataFlavor[1]; // obicno samo jedan

         // zelimo da prenesemo ukus -> DefaultMutableTreeNode
         public SlideTreeTransferHandler() {
             try {
                 nodesFlavor = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + ";class=\"" + DefaultMutableTreeNode.class.getName() + "\"");
                 flavors[0] = nodesFlavor;
             } catch (ClassNotFoundException e) {
                 e.printStackTrace();
             }
         }

         // PROVERA: da li je drop dozvoljen
         @Override
         public boolean canImport(TransferSupport support) {
             // da li je ovo drop operacija i dal je odgovarajuci ukus
             if (!support.isDrop() || !support.isDataFlavorSupported(nodesFlavor)) return false;

             // da li je korisnik nesto selektovao
             TreePath[] paths = ((JTree) support.getComponent()).getSelectionPaths();
             if (paths == null || paths.length == 0) return false;

             // da li je selektovao Slide
             for (TreePath path : paths) {
                 GraffNode node = (GraffNode) ((DefaultMutableTreeNode) path.getLastPathComponent()).getUserObject();
                 if (!(node instanceof Slide)) return false;
             }

             // uzimamo lokaciju gde korisnik zeli da dropuje
             JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
             if (dl.getPath() == null) return false;

             // da li je cilj presentation, project ili slide
             GraffNode target = (GraffNode) ((DefaultMutableTreeNode) dl.getPath().getLastPathComponent()).getUserObject();
             return target instanceof Presentation || target instanceof Project || target instanceof Slide;
         }

         // nas kofer za slajd
         @Override
         protected Transferable createTransferable(JComponent c) {
             // uzmemo selektovanu putanju
             TreePath path = ((JTree) c).getSelectionPath();
             if (path != null) {
                 // izvlacimo objekat
                 DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                 // proveravamo dal je slajd
                 if (((GraffNode) node.getUserObject()) instanceof Slide) {
                     // ovaj objekat koji "nosi" nas slajd dalje
                     return new NodesTransferable(node);
                 }
             }
             return null;
         }

         // ne KOPIRAMO -> PREMESTAMO
         @Override
         public int getSourceActions(JComponent c) {
             return MOVE;
         }

         // final DROPUJEMO
         @Override
         public boolean importData(TransferSupport support) {
             // proveravamo dal je dozvoljeno
             if (!canImport(support)) return false;

             try {
                 // izvlacim slajd iz kutije
                 DefaultMutableTreeNode draggedNode = (DefaultMutableTreeNode) support.getTransferable().getTransferData(nodesFlavor);
                 GraffNode draggedGraff = (GraffNode) draggedNode.getUserObject();

                 // gde korisnik zeli da ga ostavi
                 JTree.DropLocation dl = (JTree.DropLocation) support.getDropLocation();
                 GraffNode targetNode = (GraffNode) ((DefaultMutableTreeNode) dl.getPath().getLastPathComponent()).getUserObject();
                 int childIndex = dl.getChildIndex();

                 // odredjivanje roditelja
                 GraffNode oldParent = draggedGraff.getParent();
                 GraffNode newParent = (targetNode instanceof Slide) ? targetNode.getParent() : targetNode;

                 GraffNodeComposite oldComp = (GraffNodeComposite) oldParent;
                 GraffNodeComposite newComp = (GraffNodeComposite) newParent;

                 // OPCIJA 1: premestanje unutar iste prezentacije
                 if (oldParent == newParent && childIndex != -1) {
                     int oldIndex = oldComp.getChildren().indexOf(draggedGraff); // gde je bio
                     oldComp.getChildren().remove(draggedGraff); // ukloni ga
                     // ako ubacujemo ispod stare pozicije index se pomera
                     if (childIndex > oldIndex) childIndex--;
                     oldComp.getChildren().add(childIndex, draggedGraff); // ubaci na novu poziciju
                 }
                 // OPCIJA 2: premestanje izmedju razlicitih prezentacija/projekata
                 else {
                     oldComp.getChildren().remove(draggedGraff); // ukloni iz starog parenta
                     if (childIndex == -1) {
                         newComp.getChildren().add(draggedGraff); // dodaj na kraj
                     } else {
                         newComp.getChildren().add(childIndex, draggedGraff); // dodaj na neku poziciju
                     }
                     draggedGraff.setParent(newParent); // postavi novog roditelja
                 }

                 // rifresujemo stablo zbog izmena
                 SwingUtilities.invokeLater(() -> refreshTree());
                 return true;
             } catch (Exception ex) {
                 ex.printStackTrace();
                 return false;
             }
         }


         // KLASA koja je nasa KUTIJA -> posluzila nam za kreiranje objekta
         class NodesTransferable implements Transferable {
             private DefaultMutableTreeNode node; // cuva slajd koji prenosimo

             NodesTransferable(DefaultMutableTreeNode node) {
                 this.node = node;
             }

             // vraca nas slajd kad ga sistem zatrazi
             public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
                 if (!isDataFlavorSupported(flavor)) throw new UnsupportedFlavorException(flavor);
                 return node;
             }

             // vraca ukus podatka koji prenosimo
             public DataFlavor[] getTransferDataFlavors() {
                 return flavors;
             }

             // da li je trazeni ukus podrzan
             public boolean isDataFlavorSupported(DataFlavor flavor) {
                 return nodesFlavor.equals(flavor);
             }
         }
     }


}
