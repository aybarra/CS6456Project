package hcay.pui.com.umlapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

/**
 * Created by andrasta on 11/13/15.
 */
public class DecoratorUtil {

    private static final int DISPLACEMENT = 20;
    public static final String TAG = "DUtil";

    public DecoratorUtil(){}

    /**
     * This needs to be of fixed size...
     * @param start
     */
    public static void drawArrow(android.graphics.Point start, Canvas drawCanvas, GestureOrientation orientation, Paint mPaint){

        android.graphics.Point a = start;
        android.graphics.Point b = null;
        android.graphics.Point c = null;
        android.graphics.Point d = null;
        if(orientation == GestureOrientation.LEFT_TO_RIGHT){
            b = new android.graphics.Point((start.x + DISPLACEMENT*3), start.y);
            c = new android.graphics.Point((b.x - DISPLACEMENT*4), start.y-DISPLACEMENT*2);
            d = new android.graphics.Point(c.x, (start.y+DISPLACEMENT*2));
//            Log.i(TAG, "Value of start is: "+ start.toString());
//            Log.i(TAG, "Value of b is: " + b.toString());
//            Log.i(TAG, "Value of c is: " + c.toString());
//            Log.i(TAG, "Value of d is: " + d.toString());

        } else if(orientation == GestureOrientation.RIGHT_TO_LEFT){

//            b = new android.graphics.Point((start.x + DISPLACEMENT*3), start.y);
//            c = new android.graphics.Point((b.x - DISPLACEMENT*4), start.y-DISPLACEMENT*2);
//            d = new android.graphics.Point(c.x, (start.y+DISPLACEMENT*2));
        } else if(orientation == GestureOrientation.TOP_TO_BOTTOM){

        } else if(orientation == GestureOrientation.BOTTOM_TO_TOP){

        }

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.moveTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.moveTo(b.x, b.y);
        path.lineTo(d.x, d.y);
        path.close();

        drawCanvas.drawPath(path, mPaint);
    }
}
