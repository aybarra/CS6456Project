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
 * Uses layout to draw view
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
        Log.i(TAG, "MemberListView is null is: " + (memberListView == null));
        final MemberAdapter adapter = new MemberAdapter(context, new ArrayList<Member>());
        memberListView.setAdapter(adapter);

        Button addMember = (Button)findViewById(R.id.addMemberBtn);
        addMember.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG + "_addMember", "Add Member clicked");
                adapter.addItem("");
                adapter.notifyDataSetChanged();
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
