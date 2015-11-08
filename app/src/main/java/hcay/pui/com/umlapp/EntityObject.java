package hcay.pui.com.umlapp;

import java.util.ArrayList;
import java.util.List;

import hcay.pui.com.recognizer.Point;
import hcay.pui.com.recognizer.Size;

/**
 * Created by andrasta on 11/7/15.
 */
public class EntityObject extends UMLObject {

    private List<UMLObject> connections;

    public EntityObject(Point point, Size size){
        super(point, size);

        connections = new ArrayList<UMLObject>();
    }

    public void addConnection(UMLObject obj){
        connections.add(obj);
    }

    public void removeConnection(UMLObject obj){
        connections.remove(obj);
    }
}
