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
        mPaint.setStrokeWidth(20);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mGestureOrientation == GestureOrientation.LEFT_TO_RIGHT){
            drawCanvas.drawLine(0, this.getMeasuredHeight() / 2, (float) (this.getMeasuredWidth() * .95), this.getMeasuredHeight() / 2, mPaint);
//            Log.i(TAG, "drawLine occurring from : "+ this.getX(), );
            addDecorator((this.getMeasuredWidth() * .95), this.getMeasuredHeight() / 2);
        } else if(mGestureOrientation == GestureOrientation.RIGHT_TO_LEFT){
            drawCanvas.drawLine((float)(this.getMeasuredWidth()*.95), this.getMeasuredHeight()/2, 0, this.getMeasuredHeight()/2, mPaint);
        } else if(mGestureOrientation == GestureOrientation.BOTTOM_TO_TOP){

        } else if(mGestureOrientation == GestureOrientation.TOP_TO_BOTTOM){

        }

        canvas.drawBitmap(mRelBitmap, 0, 0, mPaint);
    }

    public void addDecorator(double x, double y){
        if(mGesture == Gesture.NAVIGABLE){
            drawArrow(x, y);
        }
    }

    public void drawArrow(double x, double y){

        android.graphics.Point a = new android.graphics.Point((int)x, (int)y);
        android.graphics.Point b = null;
        android.graphics.Point c = null;
        android.graphics.Point d = null;
        if(mGestureOrientation == GestureOrientation.LEFT_TO_RIGHT){
            b = new android.graphics.Point((int)(x + this.getMeasuredWidth() * .05), (int)y);
            c = new android.graphics.Point((int)(x - this.getMeasuredWidth() * .10), (int)(y-this.getMeasuredHeight()*.33));
            d = new android.graphics.Point((int)(x - this.getMeasuredWidth() * .10), (int)(y+this.getMeasuredHeight()*.33));

        } else if(mGestureOrientation == GestureOrientation.RIGHT_TO_LEFT){

        } else if(mGestureOrientation == GestureOrientation.TOP_TO_BOTTOM){

        } else if(mGestureOrientation == GestureOrientation.BOTTOM_TO_TOP){

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
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        mRelBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(mRelBitmap);
    }
}
