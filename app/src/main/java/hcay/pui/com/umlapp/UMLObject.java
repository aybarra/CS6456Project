package hcay.pui.com.umlapp;

import java.util.ArrayList;
import java.util.List;

import hcay.pui.com.recognizer.Point;
import hcay.pui.com.recognizer.Size;

/**
 * Created by andrasta on 11/7/15.
 */
public class UMLObject {
    private Point position;
    private Size size;

    public UMLObject(Point position, Size size){
        this.position = position;
        this.size = size;
    }
}
