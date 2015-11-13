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

    private ClassDiagramObject cdoSrc;
    private ClassDiagramObject cdoDst;
    private final String TAG = "LineObject_DIAGRAM";

    private Paint mPaint;
    public RelationshipView(Context context, ClassDiagramObject cdoSrc, ClassDiagramObject cdoDst){
        super(context);

        this.cdoSrc = cdoSrc;
        this.cdoDst = cdoDst;

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

        android.graphics.Point p1 = new android.graphics.Point(cdoSrc.getLocation().x, cdoSrc.getLocation().y);
        android.graphics.Point p2 = new android.graphics.Point(cdoDst.getLocation().x, cdoDst.getLocation().y);
        canvas.drawLine(p1.x, p1.y, p2.x, p2.y, mPaint);
    }
}
