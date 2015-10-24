package hcay.pui.com.umlapp;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by andrasta on 10/24/15.
 */
public class ClassDiagram extends View {

    public ClassDiagram(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
