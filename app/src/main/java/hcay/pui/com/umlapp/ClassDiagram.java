package hcay.pui.com.umlapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Andy Ybarra
 * Uses layout to draw view
 */
public class ClassDiagram extends View {

    private final String TAG = "CLASS_DIAGRAM";

    public ClassDiagram(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClassDiagram(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(TAG, "Measurespec is: " + widthMeasureSpec + "," + heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
