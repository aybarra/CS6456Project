package hcay.pui.com.umlapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import hcay.pui.com.recognizer.Gesture;
import hcay.pui.com.recognizer.Point;
import hcay.pui.com.recognizer.Size;

/**
 * Created by andrasta on 11/7/15.
 */
public class RelationshipView extends View {

    private int STROKE_WIDTH = 20;
    private final String TAG = "RELATIONSHIPVIEW";

    private ClassDiagramObject cdoSrc;
    private ClassDiagramObject cdoDst;

    private Paint mPaint;
    private Gesture mGesture;
    private GestureOrientation mGestureOrientation;

    private Bitmap mRelBitmap;
    private Canvas drawCanvas;


    public RelationshipView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public RelationshipView(Context context){
        super(context);
    }

    public void init(Context context, ClassDiagramObject cdoSrc, ClassDiagramObject cdoDst, GestureOrientation orientation, Gesture gesture){

        this.cdoSrc = cdoSrc;
        this.cdoDst = cdoDst;
        this.mGesture = gesture;
        this.mGestureOrientation = orientation;

        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);

        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(STROKE_WIDTH);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the line portion
        android.graphics.Point decoratorLocation = drawLineSegments();

        // Draw the arrow head from the position returned
        if(decoratorLocation != null){
            addDecorator(decoratorLocation);
        }

        canvas.drawBitmap(mRelBitmap, 0, 0, mPaint);
    }

    /**
     * Draws the endpoint decorator for the relationship
     * @param start
     */
    public void addDecorator(android.graphics.Point start){
        if(mGesture == Gesture.NAVIGABLE){
            DecoratorUtil.drawArrow(start, drawCanvas, mGestureOrientation, mPaint);
        } else if(mGesture == Gesture.AGGREGATION){
//            DecoratorUtil.drawDiamond(start, drawCanvas);
        } else if(mGesture == Gesture.GENERALIZATION){

        } else if(mGesture == Gesture.REALIZATION){

        } else if(mGesture == Gesture.COMPOSITION){

        } else if(mGesture == Gesture.DEPENDENCY){

        } else if(mGesture == Gesture.REALIZATION_DEPENDENCY){

        } else if(mGesture == Gesture.REQUIRED){

        }
    }

    public android.graphics.Point drawLineSegments(){
        if(mGestureOrientation == GestureOrientation.LEFT_TO_RIGHT){
            drawCanvas.drawLine(0, this.getMeasuredHeight()/2, this.getMeasuredWidth()/2, this.getMeasuredHeight()/2, mPaint);
            drawCanvas.drawLine(this.getMeasuredWidth()/2, this.getMeasuredHeight()/2, this.getMeasuredWidth()/2, (float)(STROKE_WIDTH*2.5), mPaint);
            drawCanvas.drawLine(this.getMeasuredWidth()/2, (float)(STROKE_WIDTH*2.5), this.getMeasuredWidth()- STROKE_WIDTH*4, (float)(STROKE_WIDTH*2.5), mPaint);

            return new android.graphics.Point(this.getMeasuredWidth()- STROKE_WIDTH*4, (int)(STROKE_WIDTH*2.5));
        } else if(mGestureOrientation == GestureOrientation.RIGHT_TO_LEFT){
//            drawCanvas.drawLine((float)(this.getMeasuredWidth()*.95), this.getMeasuredHeight()/2, 0, this.getMeasuredHeight()/2, mPaint);

        } else if(mGestureOrientation == GestureOrientation.BOTTOM_TO_TOP){

        } else if(mGestureOrientation == GestureOrientation.TOP_TO_BOTTOM){

        }
        return null;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        mRelBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(mRelBitmap);
    }
}
