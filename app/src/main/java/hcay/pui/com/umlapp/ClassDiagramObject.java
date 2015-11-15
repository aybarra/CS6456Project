package hcay.pui.com.umlapp;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import hcay.pui.com.recognizer.Gesture;
import hcay.pui.com.recognizer.Size;

/**
 * Created by andrasta on 11/12/15.
 */
public class ClassDiagramObject extends UMLObject {

    public NoteView noteView;

    private List<Relationship> relationshipList;

    public ClassDiagramObject(ClassDiagramView view) {
        super(view);
        view.classDiagramObject = this;
        relationshipList = new ArrayList<>();
    }

    public void addRelationship(Relationship relationship) {
        relationshipList.add(relationship);
    }

    public void removeRelationship(Relationship relationship) {
        relationshipList.remove(relationship);
    }
}
