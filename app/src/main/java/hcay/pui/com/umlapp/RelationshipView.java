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

    public RelationshipObject relObject;

    private Paint mPaint;

    private Bitmap mRelBitmap;
    private Canvas drawCanvas;

    android.graphics.Point decoratorLocation;
    android.graphics.Point lineEnd;

    public RelationshipView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public RelationshipView(Context context){
        super(context);
    }

    public void init(Context context){

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
        decoratorLocation = drawLineSegments();

        // Draw the arrow head from the position returned
        if (decoratorLocation != null) {
            addDecorator(decoratorLocation);
        }

        canvas.drawBitmap(mRelBitmap, 0, 0, mPaint);
    }

    /**
     * Draws the endpoint decorator for the relationship
     * @param start
     */
    public void addDecorator(android.graphics.Point start){
        switch(relObject.mGesture){
            case NAVIGABLE:
                DecoratorUtil.drawArrow(start, drawCanvas, relObject.mOrientation, mPaint);
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

    public android.graphics.Point drawLineSegments(){
        lineEnd = null;
        switch(relObject.mOrientation){
            case LEFT_TO_RIGHT:
                // Going up
                if(relObject.cdoSrc.getLocation().y > relObject.cdoDst.getLocation().y) {
                    int UP_BOTTOM_PADDING = (STROKE_WIDTH*5/2);
                    int UP_TOP_PADDING = STROKE_WIDTH*2;
                    drawCanvas.drawLine(0, this.getMeasuredHeight()-(UP_BOTTOM_PADDING),
                            this.getMeasuredWidth() / 2, this.getMeasuredHeight()-(UP_BOTTOM_PADDING),
                            mPaint);
                    drawCanvas.drawLine(this.getMeasuredWidth() / 2, this.getMeasuredHeight()-(UP_BOTTOM_PADDING),
                            this.getMeasuredWidth() / 2, UP_TOP_PADDING,
                            mPaint);
                    drawCanvas.drawLine(this.getMeasuredWidth() / 2, UP_TOP_PADDING,
                            this.getMeasuredWidth() - STROKE_WIDTH * 4, UP_TOP_PADDING,
                            mPaint);
                    lineEnd = new android.graphics.Point(this.getMeasuredWidth()- STROKE_WIDTH*4, UP_TOP_PADDING);
                    // Going down
                }else if(relObject.cdoSrc.getLocation().y < relObject.cdoDst.getLocation().y){
                    int DOWN_TOP_PADDING = (STROKE_WIDTH/2);
                    int DOWN_BOTTOM_PADDING = STROKE_WIDTH*5/2;
                    drawCanvas.drawLine(0, DOWN_TOP_PADDING, this.getMeasuredWidth()/2, DOWN_TOP_PADDING, mPaint);
                    drawCanvas.drawLine(this.getMeasuredWidth()/2, DOWN_TOP_PADDING, this.getMeasuredWidth()/2, this.getMeasuredHeight()-(DOWN_BOTTOM_PADDING), mPaint);
                    drawCanvas.drawLine(this.getMeasuredWidth()/2, this.getMeasuredHeight()- (DOWN_BOTTOM_PADDING), this.getMeasuredWidth() - STROKE_WIDTH * 3, this.getMeasuredHeight()- (DOWN_BOTTOM_PADDING), mPaint);
                    lineEnd = new android.graphics.Point(this.getMeasuredWidth()- STROKE_WIDTH*4,  (this.getMeasuredHeight() - (DOWN_BOTTOM_PADDING)));
                    // Equal
                } else {
                    // Draw a line across?
                    drawCanvas.drawLine(0, this.getMeasuredHeight()/2, this.getMeasuredWidth()- STROKE_WIDTH*4, this.getMeasuredHeight()/2, mPaint);
                }
                break;
            case RIGHT_TO_LEFT:
                drawCanvas.drawLine((float)(this.getMeasuredWidth()), 0, this.getMeasuredWidth()/2, 0, mPaint);
                drawCanvas.drawLine((float)(this.getMeasuredWidth()/2), 0, this.getMeasuredWidth()/2, 0, mPaint);
                break;
            case TOP_TO_BOTTOM:
                break;
            case BOTTOM_TO_TOP:
                break;
            default:
                break;
        }
        return lineEnd;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        mRelBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(mRelBitmap);
    }
}
