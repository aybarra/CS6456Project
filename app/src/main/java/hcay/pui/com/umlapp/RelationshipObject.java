package hcay.pui.com.umlapp;

import hcay.pui.com.recognizer.Gesture;

/**
 * Created by andrasta on 11/12/15.
 */
public class RelationshipObject extends UMLObject {

    // Figure I'd have src and dst so we can tell the orientation
    ClassDiagramObject cdoSrc;
    ClassDiagramObject cdoDst;

    Gesture mGesture;

    public RelationshipObject(RelationshipView view, ClassDiagramObject cdoSrc, ClassDiagramObject cdoDst, Gesture gesture){
        super(view);

        this.cdoSrc = cdoSrc;
        this.cdoDst = cdoDst;
        this.mGesture = gesture;
    }
}
