package hcay.pui.com.umlapp;

import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import hcay.pui.com.recognizer.Size;

/**
 * Created by andrasta on 11/12/15.
 */
public class ClassDiagramObject extends UMLObject {

    private List<Tuple> relationships;
    private List<String> memberVars;
    private List<String> methods;

    public ClassDiagramObject(ClassDiagramView view){
        super(view);

        relationships = new ArrayList<>();
        memberVars = new ArrayList<>();
        methods = new ArrayList<>();
    }

    private class Tuple<UMLObject, Side> {
        UMLObject object;
        Side side;
        public Tuple(UMLObject obj, Side side){
            this.object = obj;
            this.side = side;
        }
    }
}
