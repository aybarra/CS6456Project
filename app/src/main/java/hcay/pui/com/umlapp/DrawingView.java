package hcay.pui.com.umlapp;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Handler;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hcay.pui.com.recognizer.Gesture;
import hcay.pui.com.recognizer.Point;
import hcay.pui.com.recognizer.RecognizerResult;
import hcay.pui.com.recognizer.Recognizer;
import hcay.pui.com.recognizer.Size;
import hcay.pui.com.recognizer.Template;
import hcay.pui.com.recognizer.TemplateManager;

import android.widget.LinearLayout.LayoutParams;
/**
 * @author Andy Ybarra
 */
public class DrawingView extends ViewGroup {

    private static final String TAG = "DRAWING_VIEW";
    // drawing path
    private Path drawPath;
    // drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    // initial color
    private int paintColor = 0xFF660000;
    // canvas
    private Canvas drawCanvas;
    // canvas bitmap
    private Bitmap canvasBitmap;
    private float brushSize, lastBrushSize;
    private boolean selectionEnabled =false;

    static float phase;
    private static PathEffect pe;

    Recognizer recognizer;
    ArrayList<Point> points;

    // Distinguishes between different strokes
    static int strokeCounter = 0;

    // Timer related variables
    Timer timer;
    TimerTask timerTask;

    final Handler handler = new Handler();

    private List<View> umlObjects;

    /** The amount of space used by children in the left gutter. */
    private int mLeftWidth;

    /** The amount of space used by children in the right gutter. */
    private int mRightWidth;

    /** These are used for computing child frames based on their gravity. */
    private final Rect mTmpContainerRect = new Rect();
    private final Rect mTmpChildRect = new Rect();

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = this.getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        Log.i(TAG, "left, top, right, bottom: " + left + "," + top + "," + right + "," + bottom);
        //get the available size of child view
        int childLeft = this.getPaddingLeft();
        int childTop = this.getPaddingTop();
        int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        int childWidth = childRight - childLeft;
        int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == View.GONE) {
                continue;
            }
//            Log.i(TAG, "ChildWidth is: " + childWidth);
            child.measure(MeasureSpec.makeMeasureSpec(child.getLayoutParams().width, MeasureSpec.AT_MOST),
                    MeasureSpec.makeMeasureSpec(child.getLayoutParams().height, MeasureSpec.AT_MOST));
            curWidth = child.getMeasuredWidth();
            curHeight = child.getMeasuredHeight();
            //wrap is reach to the end
//            if (curLeft + curWidth >= childRight) {
//                curLeft = childLeft;
//                curTop += maxHeight;
//                maxHeight = 0;
//            }
            //do the layout
//            child.layout(curLeft, curTop, curLeft + curWidth, curTop + curHeight);
            int childsLeft = (int)child.getX();
            int childsTop = (int)child.getY();
            int childsWidth = childsLeft + child.getLayoutParams().width;
            int childsHeight = childsTop + child.getLayoutParams().height;

            Log.i(TAG, "left, top, width, height: "+ childsLeft+","+childsTop+","+childsWidth+","+childsHeight);
            child.layout(childsLeft, childsTop, childsWidth, childsHeight);
//            //store the max height
//            if (maxHeight < curHeight)
//                maxHeight = curHeight;
//            curLeft += curWidth;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){

        int desiredWidth = 100;
        int desiredHeight = 100;

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        Log.i(TAG, "Width size is: " + widthSize);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(desiredWidth, widthSize);
        } else {
            //Be whatever you want
            width = desiredWidth;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(desiredHeight, heightSize);
        } else {
            //Be whatever you want
            height = desiredHeight;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    private void setupDrawing(){
        //get drawing area setup for interaction
        drawPath = new Path();
        drawPaint = new Paint();

        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(brushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);

        brushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = brushSize;

        recognizer = new Recognizer();
        points = new ArrayList<Point>();

        umlObjects = new ArrayList<View>();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
    }

    private static void makeEffect(){
        pe = new DashPathEffect(new float[] {10, 5, 5, 5}, phase);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);

        if(selectionEnabled){
            makeEffect();
            phase += 1;
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Need to check if the timer is set, if so dismiss it
                if(timer!=null){
                    stopTimer();
                }
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                drawPath.lineTo(touchX, touchY);
                points.add(new Point(touchX, touchY, strokeCounter));
                break;
            case MotionEvent.ACTION_UP:
                drawCanvas.drawPath(drawPath, drawPaint);
                strokeCounter++;

                // Only set the time if we're in drawing mode
                if(!selectionEnabled){
                    // set the timer for the recognizer to be called
                    startTimer();
                } else {
                    invalidate();
                }

                break;
            default:
                return false;
        }

        invalidate();
        return true;
    }

    public void setColor(String newColor){
        // set color
        invalidate();

        paintColor = Color.parseColor(newColor);
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(float newSize){
        // update size
        float pixelAmount = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                newSize, getResources().getDisplayMetrics());
        brushSize=pixelAmount;
        drawPaint.setStrokeWidth(brushSize);
    }

    public void setLastBrushSize(float lastSize){
        lastBrushSize=lastSize;
    }

    public float getLastBrushSize(){
        return lastBrushSize;
    }

    public void setSelectionEnabled(boolean isSelecting){
        //set selectionEnabled true or false
        selectionEnabled =isSelecting;

        if(selectionEnabled){
            drawPaint.setPathEffect(pe);
        } else {
            // Otherwise we wanna clear the drawpath
            drawPath.reset();
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        }
//        if(selectionEnabled) {
//            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
////            drawPaint.setPathEffect(new DashPathEffect(new float[] {10,10}, 5));
//        }
//        else drawPaint.setXfermode(null);
    }

    public void startNew(){

        int count = this.getChildCount();
        for(int i = 0; i < count; i++){
            removeView(getChildAt(i));
        }
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void initializeTimerTask(){
        timerTask = new TimerTask(){
            public void run(){
                handler.post(new Runnable() {
                    public void run(){
                        Point first;
                        if(points == null || points.isEmpty()) {
                            return;
                        }
                        first = fetchLeftMost();
                        ArrayList<RecognizerResult> results = recognizer.recognize(points);
                        ArrayList<Point>tempPoints = new ArrayList<Point>();
                        tempPoints.addAll(points);
                        points.clear();
                        drawPath.reset();
                        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        invalidate();
                        if(results.size() > 1){
                            // Launch the dialog if there are options that are too close
                            generateRecognizerOptionsDialog(results, tempPoints);
                        } else if(results.size() ==1){

                            if(results.get(0).gesture.name.equals("[]")) {
                                Size tempSize = results.get(0).size;
                                Log.i(TAG, "Size is: "+ tempSize.getWidth()+","+tempSize.getHeight());
                                LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

//                                LinearLayout parent = (LinearLayout)vi.inflate(R.layout.activity_main, null);
//                                DrawingView parent = (DrawingView)findViewById(R.id.drawing);
                                LinearLayout view = (LinearLayout)vi.inflate(R.layout.class_diagram_layout, null);

                                DrawingView parent = (DrawingView)DrawingView.this.findViewById(R.id.drawing);

                                LayoutParams params = new LayoutParams(
                                        tempSize.getWidth(),
                                        tempSize.getHeight());
                                view.setX((float) first.x);
                                view.setY((float) first.y);
//
//                                v.measure(tempSize.getWidth(), tempSize.getHeight());
//                                v.layout(0,0,tempSize.getWidth(),tempSize.getHeight());
//                                v.draw(DrawingView.this.drawCanvas);

                                // TODO: Add the object to a list we manage so we can save/reload
                                parent.addView(view, params);
                                Toast.makeText(DrawingView.this.getContext(), "Number of children viewgroup has is: "+ getChildCount()
                                         + " size is: " + view.getLayoutParams().width+","+view.getLayoutParams().height, Toast.LENGTH_LONG).show();
                            }
                            Toast.makeText(DrawingView.this.getContext(),
                                    "Results were size 1, gesture="+ results.get(0).gesture.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
    }

    private Point fetchLeftMost() {
        Point min = new Point(Double.MAX_VALUE, Double.MAX_VALUE, -1);
        for(Point p: points){
            if(p.x < min.x){
                min.x = p.x;
            } else if(p.y < min.y){
                min.y = p.y;
            }
        }
        return min;
    }

    public void startTimer() {

        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        //schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer.schedule(timerTask, 2000); //
    }

    public void stopTimer() {

        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void generateRecognizerOptionsDialog(ArrayList<RecognizerResult> results, final ArrayList<Point>points){
        final Dialog gestureDialog = new Dialog(this.getContext());
        gestureDialog.setTitle("Clarify your gesture:");

        // Gives the layout inflater
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE);

        // Gives me the layout
        View view = inflater.inflate(R.layout.gesture_chooser, null);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.gesture_picker_layout);

        for(RecognizerResult rr: results){
            final ImageButton rrBtn = new ImageButton(this.getContext());
            final Gesture tempGesture = rr.gesture;
            // TODO: Add assets for other gestures
            String val = rr.gesture.toString();
            // TODO: Change this to use mipmap
            rrBtn.setImageResource(getResources().getIdentifier(val.toLowerCase(), "drawable",
                    this.getContext().getPackageName()));

            rrBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Notify the templateManager to update based on the user's selection
                    TemplateManager.addNewTemplate(tempGesture, points);
                    TemplateManager.save();
                    gestureDialog.dismiss();
                }
            });
            rrBtn.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            ll.addView(rrBtn);

        }

        // Do this at the end
        gestureDialog.setContentView(view);

        gestureDialog.show();

    }
}
