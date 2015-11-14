package hcay.pui.com.umlapp;

import android.util.Log;

import hcay.pui.com.recognizer.Point;

/**
 * Created by andrasta on 11/13/15.
 */
public class OrientLocUtil {

    /**
     * Figures out whether the gesture was done vertically or horizontally and which axis
     * @param bounds
     * @return
     */
    public static GestureOrientation getGestureOrientation(Point[] bounds){
        // If the width is longer then we're horizontal...otherwise its vertical
        if(Math.abs(bounds[1].x-bounds[0].x) > Math.abs(bounds[2].y-bounds[3].y)) {
            // Looking a the strokeID we can determine which way the relationship is pointing
            // The left strokeid has to be less than the right strokeid
            if (bounds[0].strokeID < bounds[1].strokeID && bounds[0].strokeID <= bounds[3].strokeID && bounds[0].strokeID <= bounds[2].strokeID) {
                return GestureOrientation.LEFT_TO_RIGHT;
            } else {
                return GestureOrientation.RIGHT_TO_LEFT;
            }
        } else {
            // The top has to be less than the bottom strokeid
            if(bounds[2].strokeID < bounds[3].strokeID && bounds[2].strokeID <= bounds[1].strokeID && bounds[2].strokeID <= bounds[0].strokeID) {
                return GestureOrientation.TOP_TO_BOTTOM;
            } else {
                return GestureOrientation.BOTTOM_TO_TOP;
            }
        }
    }

    public static android.graphics.Point getPlacementLocation(GestureOrientation orientation, UMLObject src){

        int height = src.getSize().getHeight();
        int width = src.getSize().getWidth();
        android.graphics.Point location = src.getLocation();
        if(orientation == GestureOrientation.LEFT_TO_RIGHT){
            location.x += width;
            location.y += height/2;
        } else if(orientation == GestureOrientation.RIGHT_TO_LEFT){
            location.y += height/2;
        } else if(orientation == GestureOrientation.TOP_TO_BOTTOM){
            location.x += width/2;
            location.y += height;
        } else if(orientation == GestureOrientation.BOTTOM_TO_TOP){
            location.x += width/2;
        }
//        Log.i(TAG, "Placement location is: " + location.toString());
        return location;
    }
}
