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

    private List<Tuple> relationshipList;
//    private List<String> memberVars;
//    private List<String> methods;

    public ClassDiagramObject(ClassDiagramView view){
        super(view);

        view.classDiagramObject = this;

        relationshipList = new ArrayList<>();
//        memberVars = new ArrayList<>();
//        methods = new ArrayList<>();
    }

    public void addRelationship(ClassDiagramObject dst, Gesture gesture){
        relationshipList.add(new Tuple(dst,gesture));
    }

    private class Tuple<ClassDiagramObject, Gesture> {
        ClassDiagramObject object;
        Gesture gesture;
        public Tuple(ClassDiagramObject obj, Gesture gesture){
            this.object = obj;
            this.gesture = gesture;
        }
    }
}
