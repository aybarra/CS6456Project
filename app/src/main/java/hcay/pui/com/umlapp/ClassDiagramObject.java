package hcay.pui.com.umlapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrasta on 11/12/15.
 */
public class ClassDiagramObject extends UMLObject {

    public NoteView noteView;
    public List<Relationship> relationships;

    public ClassDiagramObject(ClassDiagramView view) {
        super(view);
        view.classDiagramObject = this;
        relationships = new ArrayList<>();
    }

    public void addRelationship(Relationship relationship) {
        if (!relationships.contains(relationship))
            relationships.add(relationship);
    }

    public void removeRelationship(Relationship relationship) {
        relationships.remove(relationship);
    }
}
