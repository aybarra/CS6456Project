package hcay.pui.com.umlapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import at.markushi.ui.CircleButton;

/**
 * @author Andy Ybarra
 * @author Hyun Seo Chung
 */
public class ClassDiagram extends LinearLayout {

    private final String TAG = "CLASS_DIAGRAM";
    private float prevX, prevY;

    public ClassDiagram(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClassDiagram(Context context){
        super(context);
//        init(context);
    }

    public void init(Context context){
        ListView memberListView = (ListView)findViewById(R.id.memberListView);
        final FeatureAdapter memberAdapter = new FeatureAdapter(context, new ArrayList<Feature>(), true);
        memberListView.setAdapter(memberAdapter);

        Button addMember = (Button)findViewById(R.id.addMemberBtn);
        addMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                memberAdapter.addItem("");
                memberAdapter.notifyDataSetChanged();
            }
        });

        ListView methodListView = (ListView)findViewById(R.id.methodListView);
        final FeatureAdapter methodAdapter = new FeatureAdapter(context, new ArrayList<Feature>(), false);
        methodListView.setAdapter(methodAdapter);

        Button addMethod = (Button)findViewById(R.id.addMethodBtn);
        addMethod.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                methodAdapter.addItem("");
                methodAdapter.notifyDataSetChanged();
            }
        });

        CircleButton dragButton = (CircleButton) findViewById(R.id.dragButton);
        dragButton.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        prevX = event.getX();
                        prevY = event.getY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getX() - prevX;
                        float dy = event.getY() - prevY;
                        float newX = ClassDiagram.this.getX() + dx;
                        float newY = ClassDiagram.this.getY() + dy;
                        ClassDiagram.this.setX(newX);
                        ClassDiagram.this.setY(newY);
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return ev.getX() <= getMeasuredWidth() && ev.getY() <= getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
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
