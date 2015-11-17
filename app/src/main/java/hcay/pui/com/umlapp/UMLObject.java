package hcay.pui.com.umlapp;

import android.view.View;

import hcay.pui.com.recognizer.Size;

/**
 * Represents any UML element object to be drawn on the canvas.
 *
 * @author Hyun Seo Chung
 * @author Andy Ybarra
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
