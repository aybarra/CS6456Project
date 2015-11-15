package hcay.pui.com.umlapp;

import java.util.ArrayList;

/**
 * Created by Dennis on 11/13/15.
 */
public class Action {
    public ActionType type = ActionType.NONE;
    public NoteView noteView;
    public UMLObject umlObject;
    public Relationship relationship;
    public boolean isUMLObject;

    public Action(ActionType type, NoteView noteView) {
        this.type = type;
        this.noteView = noteView;
        this.isUMLObject = false;
    }

    public Action(ActionType type, UMLObject umlObject) {
        this.type = type;
        this.umlObject = umlObject;
        this.noteView = umlObject instanceof ClassDiagramObject ? ((ClassDiagramObject) umlObject).noteView : null;
        this.isUMLObject = true;
    }

    public Action(ActionType type, Relationship relationship) {
        this.type = type;
        this.relationship = relationship;
        this.isUMLObject = true;
    }

    public static ArrayList<Action> create(ActionType type, NoteView noteView) {
        ArrayList<Action> list = new ArrayList<>();
        list.add(new Action(type, noteView));
        return list;
    }

    public static ArrayList<Action> create(ActionType type, UMLObject umlObject) {
        ArrayList<Action> list = new ArrayList<>();
        list.add(new Action(type, umlObject));
        return list;
    }

    public static ArrayList<Action> create(ActionType type, Relationship relationship) {
        ArrayList<Action> list = new ArrayList<>();
        list.add(new Action(type, relationship));
        return list;
    }

    public enum ActionType {
        NONE, ADDED, REMOVED
    }
}
