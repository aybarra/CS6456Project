package hcay.pui.com.umlapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import hcay.pui.com.recognizer.Gesture;
import hcay.pui.com.recognizer.Size;

/**
 * Created by andrasta on 11/13/15.
 */
public class DecoratorUtil {

    private static final int DISPLACEMENT = 20;
    public static final String TAG = "DUtil";
    private static final int STROKE_WIDTH = 20;

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
            b = new android.graphics.Point((start.x - DISPLACEMENT*3), start.y);
            c = new android.graphics.Point((b.x + DISPLACEMENT*4), start.y-DISPLACEMENT*2);
            d = new android.graphics.Point(c.x, (start.y+DISPLACEMENT*2));
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


    public static android.graphics.Point drawLineSegments(ClassDiagramObject cdoSrc,
                                                          ClassDiagramObject cdoDst,
                                                          GestureOrientation orientation,
                                                          Size size,
                                                          Paint mPaint,
                                                          Canvas relCanvas){
        android.graphics.Point lineEnd = null;
        switch(orientation){
            case LEFT_TO_RIGHT:
                // Going up
                if(cdoSrc.getLocation().y > cdoDst.getLocation().y) {
                    relCanvas.drawLine(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth(), cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       mPaint);
                    relCanvas.drawLine(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                            cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoDst.getLocation().y+cdoDst.getSize().getHeight()/2,
                            mPaint);
                    relCanvas.drawLine(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoDst.getLocation().y+cdoDst.getSize().getHeight()/2,
                            cdoDst.getLocation().x-STROKE_WIDTH*4, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
                            mPaint);
                    lineEnd = new android.graphics.Point(cdoDst.getLocation().x-STROKE_WIDTH*4,
                                                         cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2);
                    // Going down
                }else if(cdoSrc.getLocation().y <= cdoDst.getLocation().y){
                    relCanvas.drawLine(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth(), cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       cdoSrc.getLocation().x + cdoSrc.getSize().getWidth()+size.getWidth()/2,  cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       mPaint);
                    relCanvas.drawLine(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth()+size.getWidth()/2,  cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       cdoSrc.getLocation().x + cdoSrc.getSize().getWidth()+size.getWidth()/2, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
                                       mPaint);
                    relCanvas.drawLine(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth()+size.getWidth()/2, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
                                       cdoDst.getLocation().x-STROKE_WIDTH*4, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
                                       mPaint);

                    lineEnd = new android.graphics.Point(cdoDst.getLocation().x-STROKE_WIDTH*4, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2);
                }
                break;
            case RIGHT_TO_LEFT:
                // Going up
                if(cdoSrc.getLocation().y > cdoDst.getLocation().y) {
                    relCanvas.drawLine(cdoSrc.getLocation().x, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       cdoSrc.getLocation().x - size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       mPaint);
                    relCanvas.drawLine(cdoSrc.getLocation().x - size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       cdoSrc.getLocation().x - size.getWidth()/2, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
                                       mPaint);
                    relCanvas.drawLine(cdoSrc.getLocation().x - size.getWidth()/2, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
                                       cdoDst.getLocation().x+cdoDst.getSize().getWidth()+STROKE_WIDTH*4, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
                                       mPaint);
                    lineEnd = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()+STROKE_WIDTH*4, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2);
                    // Going down
                }else if(cdoSrc.getLocation().y <= cdoDst.getLocation().y){
                    relCanvas.drawLine(cdoSrc.getLocation().x, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       cdoSrc.getLocation().x - size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       mPaint);
                    relCanvas.drawLine(cdoSrc.getLocation().x - size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
                                       cdoSrc.getLocation().x - size.getWidth()/2, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
                                       mPaint);
                    relCanvas.drawLine(cdoSrc.getLocation().x - size.getWidth()/2, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
                            cdoDst.getLocation().x+cdoDst.getSize().getWidth()+STROKE_WIDTH*4, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
                                       mPaint);
                    lineEnd = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()+STROKE_WIDTH*4, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2);
                }
                break;
            case TOP_TO_BOTTOM:
                // Going left
                if(cdoSrc.getLocation().x > cdoDst.getLocation().x){
                    relCanvas.drawLine(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight(),
                                       cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2,
                                       mPaint);
                    relCanvas.drawLine(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2,
                                       cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2,
                                       mPaint);
                    relCanvas.drawLine(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2,
                                       cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoDst.getLocation().y,
                                       mPaint);
                // Going right
                } else if(cdoSrc.getLocation().x <= cdoSrc.getLocation().x){

                }
                break;
            case BOTTOM_TO_TOP:

                break;
            default:
                break;
        }
        return lineEnd;
    }

    /**
     * Draws the endpoint decorator for the relationship
     * @param start
     */
    public static void addDecorator(android.graphics.Point start, Gesture gesture,
                             GestureOrientation orientation, Canvas canvas, Paint paint){
        switch(gesture){
            case NAVIGABLE:
                DecoratorUtil.drawArrow(start, canvas, orientation, paint);
                break;
            case AGGREGATION:
                break;
            case GENERALIZATION:
                break;
            case REALIZATION:
                break;
            case COMPOSITION:
                break;
            case DEPENDENCY:
                break;
            case REALIZATION_DEPENDENCY:
                break;
            case REQUIRED:
                break;
        }
    }
}
