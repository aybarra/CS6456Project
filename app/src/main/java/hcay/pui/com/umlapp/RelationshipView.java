package hcay.pui.com.umlapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import hcay.pui.com.recognizer.Point;
import hcay.pui.com.recognizer.Size;

/**
 * Created by andrasta on 11/7/15.
 */
public class RelationshipView extends View {

    private View entity1;
    private View entity2;
    private final String TAG = "LineObject_DIAGRAM";

    private Paint mPaint;
    public RelationshipView(Context context, View entity1, View entity2){
        super(context);

        this.entity1 = entity1;
        this.entity2 = entity2;

        init(context);
    }

    public void init(Context context){
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);

        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(20); // TODO make static brushsize?
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        android.graphics.Point p1 = new android.graphics.Point((int)entity1.getX(), (int)entity1.getY());
        android.graphics.Point p2 = new android.graphics.Point((int)entity2.getX(), (int)entity2.getY());

        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mPaint);
    }
}
