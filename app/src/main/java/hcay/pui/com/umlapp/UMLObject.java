package hcay.pui.com.umlapp;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import hcay.pui.com.recognizer.Point;
import hcay.pui.com.recognizer.Size;

/**
 * Created by andrasta on 11/7/15.
 */
public class UMLObject {
    public View view;

    public UMLObject(View view){
        this.view = view;
    }

    public android.graphics.Point getLocation(){
        return new android.graphics.Point((int)view.getX(), (int)view.getY());
    }

    public Size getSize(){
        return new Size(view.getMeasuredWidth(), view.getMeasuredHeight());
    }

}
