package raf.graffito.dsw.controller;

public class ActionManager {
    // primenjen: dijagram sa vezbi
    // Ovo je klasa koja upravlja SVIM akcijama u projektu i iz rog razloga ima polja za sve akcije

    private ExitAction exitAction;
    private AboutUs aboutUsAction;
    private AddNodeAction addNodeAction;
    private DeleteNodeAction deleteNodeAction;
    private RenameNodeAction renameNodeAction;
    private EditProjectTitleAction editProjectTitleAction;
    private EditProjectAuthorAction editProjectAuthorAction;
    // dodala nove ispod

    private SelectAction selectAction;
    private AddElementAction addElementAction;
    private DeleteElementAction deleteElementAction;
    private MoveElementAction moveElementAction;
    private ResizeElementAction resizeElementAction;
    private LoadImageAction loadImageAction; // DODATO
    private RotateLeftAction rotateLeftAction;
    private RotateRightAction rotateRightAction;

    private UndoAction undoAction;
    private RedoAction redoAction;

    private SaveAction saveAction;
    private SaveAsAction saveAsAction;
    private OpenAction openAction;

    private SaveAsTemplateAction saveAsTemplateAction;
    private LoadTemplateAction loadTemplateAction;

    public ActionManager(){
        initializeActions();
    }

    private void initializeActions(){
        exitAction = new ExitAction();
        aboutUsAction = new AboutUs();
        addNodeAction = new AddNodeAction();
        deleteNodeAction = new DeleteNodeAction();
        renameNodeAction = new RenameNodeAction();
        editProjectTitleAction = new EditProjectTitleAction();
        editProjectAuthorAction = new EditProjectAuthorAction();

        //dodala
        selectAction = new SelectAction();
        addElementAction = new AddElementAction();
        deleteElementAction = new DeleteElementAction();
        moveElementAction = new MoveElementAction();
        resizeElementAction = new ResizeElementAction();
        loadImageAction = new LoadImageAction();
        rotateLeftAction = new RotateLeftAction();
        rotateRightAction = new RotateRightAction();

        undoAction = new UndoAction();
        redoAction = new RedoAction();

        saveAction = new SaveAction();
        saveAsAction = new SaveAsAction();
        openAction = new OpenAction();

        saveAsTemplateAction = new SaveAsTemplateAction();
        loadTemplateAction = new LoadTemplateAction();

    }

    // imamo getere da bi svakoj akciji mogli da pristupimo preko ove klase
    public ExitAction getExitAction() {
        return exitAction;
    }
    public AboutUs getAboutUsAction() {
        return aboutUsAction;
    }
    public AddNodeAction getAddNodeAction() {
        return addNodeAction;
    }
    public DeleteNodeAction getDeleteNodeAction() {
        return deleteNodeAction;
    }
    public RenameNodeAction getRenameNodeAction() {
        return renameNodeAction;
    }
    public EditProjectTitleAction getEditProjectTitleAction() {
        return editProjectTitleAction;
    }
    public EditProjectAuthorAction getEditProjectAuthorAction() {
        return editProjectAuthorAction;
    }
    public SelectAction getSelectAction() {return selectAction;}
    public AddElementAction getAddElementAction() { return addElementAction;}
    public DeleteElementAction getDeleteElementAction() { return deleteElementAction; }
    public MoveElementAction getMoveElementAction() { return moveElementAction; }
    public ResizeElementAction getResizeElementAction() { return resizeElementAction; }
    public LoadImageAction getLoadImageAction() { return loadImageAction; }
    public RotateLeftAction getRotateLeftAction() { return rotateLeftAction; }
    public RotateRightAction getRotateRightAction() { return rotateRightAction; }
    public UndoAction getUndoAction() {
        return undoAction;
    }
    public RedoAction getRedoAction() {
        return redoAction;
    }
    public SaveAction getSaveAction() {
        return saveAction;
    }

    public SaveAsAction getSaveAsAction() {
        return saveAsAction;
    }

    public OpenAction getOpenAction() {
        return openAction;
    }

    public SaveAsTemplateAction getSaveAsTemplateAction() {
        return saveAsTemplateAction;
    }

    public LoadTemplateAction getLoadTemplateAction() {
        return loadTemplateAction;
    }
}


