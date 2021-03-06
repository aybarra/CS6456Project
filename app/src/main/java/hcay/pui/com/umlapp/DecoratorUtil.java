package hcay.pui.com.umlapp;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;

import hcay.pui.com.recognizer.Gesture;
import hcay.pui.com.recognizer.Size;

/**
 * Util for drawing decorators for relationships.
 *
 * @author Hyun Seo Chung
 * @author Andy Ybarra
 */
public class DecoratorUtil {

    private static int DISPLACEMENT;
    private static int STROKE_WIDTH;
    public static final String TAG = "DUtil";

    public static final int MULTIPLIER = 12;
    public static int PADDING;
    public DecoratorUtil(){}

    public static void setStrokeWidth(int value) {
        DISPLACEMENT = value*2;
        STROKE_WIDTH = value;
        PADDING = DISPLACEMENT*MULTIPLIER;
    }

    /**
     *
     * @param tuple
     * @param orientation
     */
    public static Path drawArrow(SegmentTuple tuple, GestureOrientation orientation){
        android.graphics.Point start = tuple.lastPoint;
//        float old = mPaint.getStrokeWidth();
//        mPaint.setStrokeWidth(STROKE_WIDTH);
        android.graphics.Point a = start;
        android.graphics.Point b = null;
        android.graphics.Point c = null;
        android.graphics.Point d = null;
        if(orientation == GestureOrientation.LEFT_TO_RIGHT){
            b = new android.graphics.Point((start.x + PADDING), start.y);
            c = new android.graphics.Point((b.x - DISPLACEMENT*4), start.y-DISPLACEMENT*2);
            d = new android.graphics.Point(c.x, (start.y+DISPLACEMENT*2));
//            Log.i(TAG, "Value of start is: "+ start.toString());
//            Log.i(TAG, "Value of b is: " + b.toString());
//            Log.i(TAG, "Value of c is: " + c.toString());
//            Log.i(TAG, "Value of d is: " + d.toString());
        } else if(orientation == GestureOrientation.RIGHT_TO_LEFT){
            b = new android.graphics.Point((start.x - PADDING), start.y);
            c = new android.graphics.Point((b.x + DISPLACEMENT*4), start.y-DISPLACEMENT*2);
            d = new android.graphics.Point(c.x, (start.y+DISPLACEMENT*2));
        } else if(orientation == GestureOrientation.TOP_TO_BOTTOM){
            b = new android.graphics.Point(start.x, start.y + PADDING);
            c = new android.graphics.Point(start.x-DISPLACEMENT*2, (b.y - DISPLACEMENT*4));
            d = new android.graphics.Point(start.x+DISPLACEMENT*2, c.y);
        } else if(orientation == GestureOrientation.BOTTOM_TO_TOP){
            b = new android.graphics.Point(start.x, start.y - PADDING);
            c = new android.graphics.Point(start.x-DISPLACEMENT*2, (b.y + DISPLACEMENT*4));
            d = new android.graphics.Point(start.x+DISPLACEMENT*2, c.y);
        }

//        Path path = new Path();
        tuple.segPath.setFillType(Path.FillType.EVEN_ODD);
        tuple.segPath.moveTo(a.x, a.y);
        tuple.segPath.lineTo(b.x, b.y);
        tuple.segPath.moveTo(b.x, b.y);
        tuple.segPath.lineTo(c.x, c.y);
        tuple.segPath.moveTo(b.x, b.y);
        tuple.segPath.lineTo(d.x, d.y);
//        tuple.segPath.close();
//        drawCanvas.drawPath(path, mPaint);
//        mPaint.setStrokeWidth(old);

        return tuple.segPath;
    }

    /**
     * Only want the path for the arrow
     * @param start
     * @param orientation
     */
    public static Path getArrow(Point start, GestureOrientation orientation){
        android.graphics.Point a = start;
        android.graphics.Point b = null;
        android.graphics.Point c = null;
        android.graphics.Point d = null;
        if(orientation == GestureOrientation.LEFT_TO_RIGHT){
            b = new android.graphics.Point((start.x + PADDING), start.y);
            c = new android.graphics.Point((b.x - DISPLACEMENT*4), start.y-DISPLACEMENT*2);
            d = new android.graphics.Point(c.x, (start.y+DISPLACEMENT*2));
        } else if(orientation == GestureOrientation.RIGHT_TO_LEFT){
            b = new android.graphics.Point((start.x - PADDING), start.y);
            c = new android.graphics.Point((b.x + DISPLACEMENT*4), start.y-DISPLACEMENT*2);
            d = new android.graphics.Point(c.x, (start.y+DISPLACEMENT*2));
        } else if(orientation == GestureOrientation.TOP_TO_BOTTOM){
            b = new android.graphics.Point(start.x, start.y + PADDING);
            c = new android.graphics.Point(start.x-DISPLACEMENT*2, (b.y - DISPLACEMENT*4));
            d = new android.graphics.Point(start.x+DISPLACEMENT*2, c.y);
        } else if(orientation == GestureOrientation.BOTTOM_TO_TOP){
            b = new android.graphics.Point(start.x, start.y - PADDING);
            c = new android.graphics.Point(start.x-DISPLACEMENT*2, (b.y + DISPLACEMENT*4));
            d = new android.graphics.Point(start.x+DISPLACEMENT*2, c.y);
        }

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.moveTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.moveTo(b.x, b.y);
        path.lineTo(d.x, d.y);

        return path;
    }

    public static Path getTriangle(Point start, GestureOrientation orientation){
        android.graphics.Point a = start;
        android.graphics.Point b = null;
        android.graphics.Point c = null;
        android.graphics.Point d = null;
        android.graphics.Point e = null;
        if(orientation == GestureOrientation.LEFT_TO_RIGHT){
            b = new android.graphics.Point((start.x)+DISPLACEMENT*2, start.y);
            c = new android.graphics.Point(b.x, start.y-DISPLACEMENT*6);
            d = new android.graphics.Point(start.x + PADDING, start.y);
            e = new android.graphics.Point(b.x, (start.y+DISPLACEMENT*6));
        } else if(orientation == GestureOrientation.RIGHT_TO_LEFT){
            b = new android.graphics.Point((start.x)-DISPLACEMENT*2, start.y);
            c = new android.graphics.Point(b.x, start.y+DISPLACEMENT*6);
            d = new android.graphics.Point(start.x - PADDING, start.y);
            e = new android.graphics.Point(b.x, (start.y-DISPLACEMENT*6));
        } else if(orientation == GestureOrientation.TOP_TO_BOTTOM){
            b = new android.graphics.Point((start.x), start.y+DISPLACEMENT*2);
            c = new android.graphics.Point(start.x+DISPLACEMENT*6, b.y);
            d = new android.graphics.Point(start.x, start.y+PADDING);
            e = new android.graphics.Point(start.x-DISPLACEMENT*6, b.y);
        } else if(orientation == GestureOrientation.BOTTOM_TO_TOP){
            b = new android.graphics.Point((start.x), start.y-DISPLACEMENT*2);
            c = new android.graphics.Point(start.x+DISPLACEMENT*6, b.y);
            d = new android.graphics.Point(start.x, start.y-PADDING);
            e = new android.graphics.Point(start.x-DISPLACEMENT*6, b.y);
        }

        Path path = new Path();
        path.setFillType(Path.FillType.EVEN_ODD);
        path.moveTo(a.x, a.y);
        path.lineTo(b.x, b.y);
        path.moveTo(b.x, b.y);
        path.lineTo(c.x, c.y);
        path.moveTo(c.x, c.y);
        path.lineTo(d.x, d.y);
        path.moveTo(d.x, d.y);
        path.lineTo(e.x, e.y);
        path.moveTo(e.x, e.y);
        path.lineTo(b.x, b.y);

        return path;
    }


    public static Path drawDiamond(SegmentTuple tuple, GestureOrientation orientation){
        android.graphics.Point start = tuple.lastPoint;
//        float old = mPaint.getStrokeWidth();
//        Paint.Style oldStyle = mPaint.getStyle();;
//        mPaint.setStrokeWidth(STROKE_WIDTH);
        android.graphics.Point a = start;
        android.graphics.Point b = null;
        android.graphics.Point c = null;
        android.graphics.Point d = null;
        if(orientation == GestureOrientation.LEFT_TO_RIGHT){
            b = new android.graphics.Point((start.x + DISPLACEMENT*6), start.y-DISPLACEMENT*3);
            c = new android.graphics.Point((start.x + DISPLACEMENT*12), start.y);
            d = new android.graphics.Point(b.x, (start.y+DISPLACEMENT*3));
        } else if(orientation == GestureOrientation.RIGHT_TO_LEFT){
            b = new android.graphics.Point((start.x - DISPLACEMENT*6), start.y-DISPLACEMENT*3);
            c = new android.graphics.Point((start.x - DISPLACEMENT*12), start.y);
            d = new android.graphics.Point(b.x, (start.y+DISPLACEMENT*3));
        } else if(orientation == GestureOrientation.TOP_TO_BOTTOM){
            b = new android.graphics.Point((start.x-DISPLACEMENT*3), start.y+DISPLACEMENT*6);
            c = new android.graphics.Point(start.x, start.y+DISPLACEMENT*12);
            d = new android.graphics.Point(start.x+DISPLACEMENT*3, b.y);
        } else if(orientation == GestureOrientation.BOTTOM_TO_TOP){
            b = new android.graphics.Point((start.x-DISPLACEMENT*3), start.y-DISPLACEMENT*6);
            c = new android.graphics.Point(start.x, start.y-DISPLACEMENT*12);
            d = new android.graphics.Point(start.x+DISPLACEMENT*3, b.y);
        }

//        Path path = new Path();
//        if(filled){
//            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
//        }
//        path.setFillType(Path.FillType.WINDING);
        tuple.segPath.setFillType(Path.FillType.EVEN_ODD);
        tuple.segPath.moveTo(a.x, a.y);
        tuple.segPath.lineTo(b.x, b.y);
        tuple.segPath.moveTo(b.x, b.y);
        tuple.segPath.lineTo(c.x, c.y);
        tuple.segPath.moveTo(c.x, c.y);
        tuple.segPath.lineTo(d.x, d.y);
        tuple.segPath.moveTo(d.x, d.y);
        tuple.segPath.lineTo(a.x, a.y);

//        drawCanvas.drawPath(path, mPaint);
//        mPaint.setStrokeWidth(old);
//        if(filled){
//            mPaint.setStyle(oldStyle);
//        }

        return tuple.segPath;
    }

    public static Path drawTriangle(SegmentTuple tuple, GestureOrientation orientation){

//        float old = mPaint.getStrokeWidth();
//        mPaint.setStrokeWidth(STROKE_WIDTH);
        android.graphics.Point start = tuple.lastPoint;
        android.graphics.Point a = start;
        android.graphics.Point b = null;
        android.graphics.Point c = null;
        android.graphics.Point d = null;
        android.graphics.Point e = null;
        if(orientation == GestureOrientation.LEFT_TO_RIGHT){
            b = new android.graphics.Point((start.x)+DISPLACEMENT*2, start.y);
            c = new android.graphics.Point(b.x, start.y-DISPLACEMENT*6);
            d = new android.graphics.Point(start.x + PADDING, start.y);
            e = new android.graphics.Point(b.x, (start.y+DISPLACEMENT*6));
        } else if(orientation == GestureOrientation.RIGHT_TO_LEFT){
            b = new android.graphics.Point((start.x)-DISPLACEMENT*2, start.y);
            c = new android.graphics.Point(b.x, start.y+DISPLACEMENT*6);
            d = new android.graphics.Point(start.x - PADDING, start.y);
            e = new android.graphics.Point(b.x, (start.y-DISPLACEMENT*6));
        } else if(orientation == GestureOrientation.TOP_TO_BOTTOM){
            b = new android.graphics.Point((start.x), start.y+DISPLACEMENT*2);
            c = new android.graphics.Point(start.x+DISPLACEMENT*6, b.y);
            d = new android.graphics.Point(start.x, start.y+PADDING);
            e = new android.graphics.Point(start.x-DISPLACEMENT*6, b.y);
        } else if(orientation == GestureOrientation.BOTTOM_TO_TOP){
            b = new android.graphics.Point((start.x), start.y-DISPLACEMENT*2);
            c = new android.graphics.Point(start.x+DISPLACEMENT*6, b.y);
            d = new android.graphics.Point(start.x, start.y-PADDING);
            e = new android.graphics.Point(start.x-DISPLACEMENT*6, b.y);
        }

//        Path path = new Path();
        tuple.segPath.setFillType(Path.FillType.EVEN_ODD);
        tuple.segPath.moveTo(a.x, a.y);
        tuple.segPath.lineTo(b.x, b.y);
        tuple.segPath.moveTo(b.x, b.y);
        tuple.segPath.lineTo(c.x, c.y);
        tuple.segPath.moveTo(c.x, c.y);
        tuple.segPath.lineTo(d.x, d.y);
        tuple.segPath.moveTo(d.x, d.y);
        tuple.segPath.lineTo(e.x, e.y);
        tuple.segPath.moveTo(e.x, e.y);
        tuple.segPath.lineTo(b.x, b.y);
//        tuple.segPath.close();

//        drawCanvas.drawPath(path, mPaint);
//        mPaint.setStrokeWidth(old);

        return tuple.segPath;
    }

    public static Path drawCircle(SegmentTuple tuple, GestureOrientation orientation) {
//        float old = mPaint.getStrokeWidth();
//        mPaint.setStrokeWidth(STROKE_WIDTH);
        android.graphics.Point start = tuple.lastPoint;
        switch(orientation){
            case LEFT_TO_RIGHT:
                tuple.segPath.addCircle(start.x + PADDING/2, start.y, PADDING/2, Path.Direction.CW);
//                drawCanvas.drawCircle(start.x + PADDING/2, start.y, PADDING/2, mPaint);
                break;
            case RIGHT_TO_LEFT:
                tuple.segPath.addCircle(start.x - PADDING/2, start.y, PADDING/2, Path.Direction.CW);
//                drawCanvas.drawCircle(start.x - PADDING/2, start.y, PADDING/2, mPaint);
                break;
            case TOP_TO_BOTTOM:
                tuple.segPath.addCircle(start.x, start.y+PADDING/2, PADDING/2, Path.Direction.CW);
//                drawCanvas.drawCircle(start.x, start.y+PADDING/2, PADDING/2, mPaint);
                break;
            case BOTTOM_TO_TOP:
                tuple.segPath.addCircle(start.x, start.y-PADDING/2, PADDING/2, Path.Direction.CW);
//                drawCanvas.drawCircle(start.x, start.y-PADDING/2, PADDING/2, mPaint);
                break;
        }
//        mPaint.setStrokeWidth(old);

        return tuple.segPath;
    }

    public static Path drawSemiCircle(SegmentTuple tuple, GestureOrientation orientation) {

        android.graphics.Point start = tuple.lastPoint;
        int SEMI_CIRCLE_DISPLACEMENT = DISPLACEMENT*11/2;
        switch(orientation){
            case LEFT_TO_RIGHT:
                // scoot to the right by SEMI_CIRCLE DISPLACEMENT
                tuple.segPath.moveTo(start.x, start.y);
                tuple.segPath.lineTo(start.x+SEMI_CIRCLE_DISPLACEMENT, start.y);
                tuple.segPath.addArc(new RectF(start.x+SEMI_CIRCLE_DISPLACEMENT, start.y-PADDING/2, start.x+PADDING+SEMI_CIRCLE_DISPLACEMENT, start.y+PADDING/2), 90, 180) ;
                break;
            case RIGHT_TO_LEFT:
                // scoot to the left by SEMI_CIRCLE DISPLACEMENT
                tuple.segPath.moveTo(start.x, start.y);
                tuple.segPath.lineTo(start.x-SEMI_CIRCLE_DISPLACEMENT, start.y);
                tuple.segPath.addArc(new RectF(start.x - PADDING - SEMI_CIRCLE_DISPLACEMENT, start.y - PADDING / 2, start.x - SEMI_CIRCLE_DISPLACEMENT, start.y + PADDING / 2), -90, 180) ;
                break;
            case TOP_TO_BOTTOM:
                // scoot down by SEMI_CIRCLE DISPLACEMENT
                tuple.segPath.moveTo(start.x, start.y);
                tuple.segPath.lineTo(start.x, start.y+SEMI_CIRCLE_DISPLACEMENT);
                tuple.segPath.addArc(new RectF(start.x - PADDING / 2, start.y + SEMI_CIRCLE_DISPLACEMENT, start.x + PADDING / 2, start.y + PADDING + SEMI_CIRCLE_DISPLACEMENT), 180, 180);
                break;
            case BOTTOM_TO_TOP:
                // scoot up by SEMI_CIRCLE DISPLACEMENT
                tuple.segPath.moveTo(start.x, start.y);
                tuple.segPath.lineTo(start.x, start.y - SEMI_CIRCLE_DISPLACEMENT);
                tuple.segPath.addArc(new RectF(start.x - PADDING / 2, start.y - PADDING - SEMI_CIRCLE_DISPLACEMENT, start.x + PADDING / 2, start.y - SEMI_CIRCLE_DISPLACEMENT), 0, 180);
                break;
        }

        return tuple.segPath;
    }

    public static Path drawLine(SegmentTuple tuple, GestureOrientation orientation){
        Point start = tuple.lastPoint;
        switch(orientation){
            case LEFT_TO_RIGHT:
                tuple.segPath.moveTo(start.x, start.y);
                tuple.segPath.lineTo(start.x + PADDING, start.y);
                break;
            case RIGHT_TO_LEFT:
                tuple.segPath.moveTo(start.x, start.y);
                tuple.segPath.lineTo(start.x - PADDING, start.y);
                break;
            case TOP_TO_BOTTOM:
                tuple.segPath.moveTo(start.x, start.y);
                tuple.segPath.lineTo(start.x , start.y + PADDING);
                break;
            case BOTTOM_TO_TOP:
                tuple.segPath.moveTo(start.x, start.y);
                tuple.segPath.lineTo(start.x, start.y - PADDING);
                break;
        }
        return tuple.segPath;
    }


    public static SegmentTuple drawLineSegments(ClassDiagramObject cdoSrc,
                                                          ClassDiagramObject cdoDst,
                                                          GestureOrientation orientation,
                                                          Size size,
                                                          Paint mPaint,
                                                          Canvas relCanvas){

        float old = mPaint.getStrokeWidth();
        mPaint.setStrokeWidth(STROKE_WIDTH);
//        android.graphics.Point lineEnd = null;
        Path segmentPath = new Path();
        android.graphics.Point s1Start = null;
        android.graphics.Point s1End = null;
        android.graphics.Point s2Start = null;
        android.graphics.Point s2End = null;
        android.graphics.Point s3Start = null;
        android.graphics.Point s3End = null;

        switch(orientation){
            case LEFT_TO_RIGHT:
//                relCanvas.drawLine(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth(), cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
//                                   cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
//                                   mPaint);
//                relCanvas.drawLine(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
//                                   cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoDst.getLocation().y+cdoDst.getSize().getHeight()/2,
//                                   mPaint);
//                relCanvas.drawLine(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoDst.getLocation().y+cdoDst.getSize().getHeight()/2,
//                                   cdoDst.getLocation().x-PADDING, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
//                                   mPaint);
//                lineEnd = new android.graphics.Point(cdoDst.getLocation().x-PADDING,
//                                                     cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2);
                s1Start = new android.graphics.Point(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth(), cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2);
                s1End = new android.graphics.Point(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2);
                s2Start = new android.graphics.Point(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2);
                s2End = new android.graphics.Point(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoDst.getLocation().y+cdoDst.getSize().getHeight()/2);
                s3Start = new android.graphics.Point(cdoSrc.getLocation().x + cdoSrc.getSize().getWidth() + size.getWidth()/2, cdoDst.getLocation().y+cdoDst.getSize().getHeight()/2);
                s3End = new android.graphics.Point(cdoDst.getLocation().x-PADDING, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2);
                break;
            case RIGHT_TO_LEFT:
//                relCanvas.drawLine(cdoSrc.getLocation().x, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
//                                   cdoSrc.getLocation().x - size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
//                                   mPaint);
//                relCanvas.drawLine(cdoSrc.getLocation().x - size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2,
//                                   cdoSrc.getLocation().x - size.getWidth()/2, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
//                                   mPaint);
//                relCanvas.drawLine(cdoSrc.getLocation().x - size.getWidth()/2, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
//                                   cdoDst.getLocation().x+cdoDst.getSize().getWidth()+PADDING, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2,
//                                   mPaint);
//                lineEnd = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()+PADDING, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2);
                s1Start = new android.graphics.Point(cdoSrc.getLocation().x, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2);
                s1End = new android.graphics.Point(cdoSrc.getLocation().x - size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2);
                s2Start = new android.graphics.Point(cdoSrc.getLocation().x - size.getWidth()/2, cdoSrc.getLocation().y + cdoSrc.getSize().getHeight()/2);
                s2End = new android.graphics.Point(cdoSrc.getLocation().x - size.getWidth()/2, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2);
                s3Start = new android.graphics.Point(cdoSrc.getLocation().x - size.getWidth()/2, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2);
                s3End = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()+PADDING, cdoDst.getLocation().y + cdoDst.getSize().getHeight()/2);

                break;
            case TOP_TO_BOTTOM:
//                relCanvas.drawLine(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight(),
//                                   cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2,
//                                   mPaint);
//                relCanvas.drawLine(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2,
//                                   cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2,
//                                   mPaint);
//                relCanvas.drawLine(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2,
//                                   cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoDst.getLocation().y-PADDING,
//                                   mPaint);
//                lineEnd = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoDst.getLocation().y-PADDING);
                s1Start = new android.graphics.Point(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight());
                s1End = new android.graphics.Point(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2);
                s2Start = new android.graphics.Point(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2);
                s2End = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2);
                s3Start = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoSrc.getLocation().y+cdoSrc.getSize().getHeight() + size.getHeight()/2);
                s3End = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoDst.getLocation().y-PADDING);

                break;
            case BOTTOM_TO_TOP:
//                relCanvas.drawLine(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y,
//                                   cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y - size.getHeight()/2,
//                                   mPaint);
//                relCanvas.drawLine(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y - size.getHeight()/2,
//                                   cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoSrc.getLocation().y - size.getHeight()/2,
//                                   mPaint);
//                relCanvas.drawLine(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoSrc.getLocation().y - size.getHeight()/2,
//                                   cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoDst.getLocation().y+cdoDst.getSize().getHeight()+PADDING,
//                                   mPaint);
//                lineEnd = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoDst.getLocation().y+cdoDst.getSize().getHeight()+PADDING);
                s1Start = new android.graphics.Point(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y);
                s1End = new android.graphics.Point(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y - size.getHeight()/2);
                s2Start = new android.graphics.Point(cdoSrc.getLocation().x+cdoSrc.getSize().getWidth()/2, cdoSrc.getLocation().y - size.getHeight()/2);
                s2End = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoSrc.getLocation().y - size.getHeight()/2);
                s3Start = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoSrc.getLocation().y - size.getHeight()/2);
                s3End = new android.graphics.Point(cdoDst.getLocation().x+cdoDst.getSize().getWidth()/2, cdoDst.getLocation().y+cdoDst.getSize().getHeight()+PADDING);
                break;
        }

        segmentPath.moveTo(s1Start.x, s1Start.y);
        segmentPath.lineTo(s1End.x, s1End.y);
        segmentPath.moveTo(s2Start.x, s2Start.y);
        segmentPath.lineTo(s2End.x, s2End.y);
        segmentPath.moveTo(s3Start.x, s3Start.y);
        segmentPath.lineTo(s3End.x, s3End.y);
        mPaint.setStrokeWidth(old);
        return new SegmentTuple(segmentPath, s3End);
    }



    /**
     *
     * @param tuple
     * @param gesture
     * @param orientation
     */
    public static Path addDecorator(SegmentTuple tuple, Gesture gesture,
                             GestureOrientation orientation){
        Path fullPath = null;
        switch(gesture){
            case NAVIGABLE:
                fullPath = DecoratorUtil.drawArrow(tuple, orientation);
                // FOR TESTING OVERWRITE THIS ONE SINCE ITS EASY TO PERFORM
//                fullPath = DecoratorUtil.drawDiamond(tuple, orientation);
                break;
            case AGGREGATION:
                // Looks like a diamond
                fullPath = DecoratorUtil.drawDiamond(tuple, orientation);
                break;
            case GENERALIZATION:
                // Draw a  triangle
                fullPath = DecoratorUtil.drawTriangle(tuple, orientation);
                break;
            case REALIZATION:
                // Looks like a lollipop
                fullPath = DecoratorUtil.drawCircle(tuple, orientation);
                break;
            case COMPOSITION:
                fullPath = DecoratorUtil.drawDiamond(tuple, orientation);
                break;
            case REQUIRED:
                fullPath = DecoratorUtil.drawSemiCircle(tuple, orientation);
                break;
            case UNSPECIFIED:
                fullPath = DecoratorUtil.drawLine(tuple, orientation);
                break;
        }
        return fullPath;
    }
}