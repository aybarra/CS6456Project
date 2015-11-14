package hcay.pui.com.umlapp;

import android.content.Context;
import android.graphics.Canvas;
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
public class ClassDiagramView extends LinearLayout {

    public FeatureAdapter memberAdapter, methodAdapter;
    public ClassDiagramObject classDiagramObject;
    public ListView memberListView, methodListView;
    public Button addMember, addMethod;

    private final String TAG = "CLASS_DIAGRAM_VIEW";
    private float prevX, prevY;
    private boolean isClass = true;

    public ClassDiagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClassDiagramView(Context context){
        super(context);
//        init(context);
    }

    public void init(Context context){
        memberListView = (ListView)findViewById(R.id.memberListView);
        memberAdapter = new FeatureAdapter(context, new ArrayList<Feature>(), true);
        memberListView.setAdapter(memberAdapter);

        addMember = (Button)findViewById(R.id.addMemberBtn);
        addMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                memberAdapter.addItem("");
                memberAdapter.notifyDataSetChanged();
            }
        });

        methodListView = (ListView)findViewById(R.id.methodListView);
        methodAdapter = new FeatureAdapter(context, new ArrayList<Feature>(), false);
        methodListView.setAdapter(methodAdapter);

        addMethod = (Button)findViewById(R.id.addMethodBtn);
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
                        float newX = ClassDiagramView.this.getX() + dx;
                        float newY = ClassDiagramView.this.getY() + dy;
                        ClassDiagramView.this.setX(newX);
                        ClassDiagramView.this.setY(newY);
                        if (classDiagramObject.noteView != null) {
                            newX = classDiagramObject.noteView.getX() + dx;
                            newY = classDiagramObject.noteView.getY() + dy;
                            classDiagramObject.noteView.setX(newX);
                            classDiagramObject.noteView.setY(newY);
                        }
                        break;
                }
                return false;
            }
        });

        CircleButton modeButton = (CircleButton) findViewById(R.id.typeButton);
        modeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                memberListView.setVisibility(isClass ? GONE : VISIBLE);
                addMember.setVisibility(isClass ? GONE : VISIBLE);
                isClass = !isClass;
            }
        });
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        return ev.getX() <= getMeasuredWidth() && ev.getY() <= getMeasuredHeight();
//    }

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
