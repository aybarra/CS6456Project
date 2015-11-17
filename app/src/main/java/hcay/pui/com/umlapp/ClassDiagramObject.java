package hcay.pui.com.umlapp;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a classifier drawn on a canvas.
 *
 * @author Hyun Seo Chung
 * @author Andy Ybarra
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
