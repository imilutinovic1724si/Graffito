package raf.graffito.dsw.gui.swing;

import raf.graffito.dsw.model.GraffNode;

import javax.swing.tree.DefaultMutableTreeNode;

public interface GraffTree {
    // Interfejs za operacije nad stablom

    //Generisanje stabla od GraffNode strukture
    DefaultMutableTreeNode generateTree(GraffNode root);

    //Dodavanje deteta selektovanom cvoru
    void addChild(GraffNode parent);

    // Brisanje selektovanog cvora
    void deleteNode();

    // Preimenovanje selektovanog cvora
    void renameNode(GraffNode node, String newName);

    // Vracanje selektovanog cvora
    GraffNode getSelectedNode();


}
