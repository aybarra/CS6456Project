package hcay.pui.com.umlapp;

import hcay.pui.com.recognizer.Gesture;

/**
 * Object containing information about each relationship.
 *
 * @author Hyun Seo Chung
 * @author Andy Ybarra
 */
public class RelationshipObject extends UMLObject {

    // Figure I'd have src and dst so we can tell the orientation
    ClassDiagramObject cdoSrc;
    ClassDiagramObject cdoDst;

    Gesture mGesture;
    GestureOrientation mOrientation;

    public RelationshipObject(RelationshipView view, ClassDiagramObject cdoSrc, ClassDiagramObject cdoDst,
                              GestureOrientation orientation, Gesture gesture){
        super(view);

        view.relObject = this;
        this.cdoSrc = cdoSrc;
        this.cdoDst = cdoDst;
        this.mGesture = gesture;
        this.mOrientation = orientation;
    }
}
