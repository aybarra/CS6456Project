package hcay.pui.com.umlapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.ScaleGestureDetector;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import android.graphics.PorterDuff;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import hcay.pui.com.recognizer.Gesture;
import hcay.pui.com.recognizer.Point;
import hcay.pui.com.recognizer.RecognizerResult;
import hcay.pui.com.recognizer.Recognizer;
import hcay.pui.com.recognizer.Size;
import hcay.pui.com.recognizer.TemplateManager;

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

    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private boolean mScaled = false;
    private Rect clipBounds_canvas;

    private List<UMLObject> umlObjects;

    android.graphics.Point dispSize;
    int dispWidth;
    int dispHeight;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i = 0; i < this.getChildCount(); i++){
            getChildAt(i).layout(l, t, r, b);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void setupDrawing(Context context){
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

        umlObjects = new ArrayList<UMLObject>();

        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());

//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        display.getSize(dispSize);
//        dispWidth = dispSize.x;
//        dispHeight = dispSize.y;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
//        if(drawCanvas != null){
//            drawCanvas
//        }
    }

    private static void makeEffect(){
        pe = new DashPathEffect(new float[] {10, 5, 5, 5}, phase);
    }

    @Override
    protected void onDraw(Canvas canvas) {

//        clipBounds_canvas = canvas.getClipBounds();
        // Notify children
//        if(mScaled){
//            for(int i = 0; i < getChildCount(); i++){
//                ((ClassDiagram)getChildAt(i)).updateScale(mScaleFactor);
//                ((ClassDiagram)getChildAt(i)).updateClip(clipBounds_canvas);
//            }
//            mScaled = false;
//        }
//        canvas.scale(mScaleFactor, mScaleFactor);

//        drawPaint.setStrokeWidth(brushSize * (1/mScaleFactor));

        // Draw view
        // Original
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

        // STACKOVERFLOW ATTEMPT
//        drawEndlessBackground(canvas, this.getLeft(), this.getTop());
//        Rect rectangle = canvas.getClipBounds();
//        float val = 1/mScaleFactor;
//        RectF dst = new RectF(rectangle.left*val, rectangle.top*val, rectangle.right*val, rectangle.bottom*val);
//        canvas.drawBitmap(canvasBitmap, null, dst, drawPaint);

        canvas.drawPath(drawPath, drawPaint);

        if(selectionEnabled){
            makeEffect();
            phase += 1;
            invalidate();
        }
    }

    private void drawEndlessBackground(Canvas canvas, float left, float top) {

        float modLeft = left % dispWidth;

        canvas.drawBitmap(canvasBitmap, modLeft, top, null);

        if (left < 0) {
            canvas.drawBitmap(canvasBitmap, modLeft + dispWidth, top, null);
        } else {
            canvas.drawBitmap(canvasBitmap, modLeft - dispWidth, top, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        // New values
//        float touchX = event.getX() / mScaleFactor + clipBounds_canvas.left;
//        float touchY = event.getY() / mScaleFactor + clipBounds_canvas.top;

        // Original
        float touchX = event.getX();
        float touchY = event.getY();

        if(event.getPointerCount() > 1) {
            mScaleDetector.onTouchEvent(event);

            return true;
        } else {
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
        for(int i = count; i >= 0; i--){
            removeView(getChildAt(i));
        }
        // Clear the references we're managing
        umlObjects.clear();

        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void initializeTimerTask(){
        timerTask = new TimerTask(){
            public void run(){
                handler.post(new Runnable() {
                    public void run(){
                        if (points == null || points.isEmpty()) return;
//                        Point first = points.get(0);
                        ArrayList<RecognizerResult> results = Recognizer.recognize(points);
                        Point leftMost = getLeftMost();
                        Point rightMost = getRightMost();
                        Point topMost = getTopMost();
                        Point bottomMost = getBottomMost();
                        Point[] bounds = {leftMost, rightMost, topMost, bottomMost};

                        Log.i(TAG, "left: "+ leftMost.toString() + ", right: " + rightMost.toString()
                                 + ", top: " + topMost.toString() + ", bottom: "+ bottomMost.toString());

                        // Using this for giving the recognizer updates
                        ArrayList<Point>tempPoints = new ArrayList<>();
                        tempPoints.addAll(points);
                        points.clear();
                        drawPath.reset();
                        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        invalidate();

                        if(results.size() > 1){
                            // Launch the dialog if there are options that are too close
                            generateRecognizerOptionsDialog(results, tempPoints, bounds);
                        } else if(results.size() ==1){
                            if (results.get(0).gesture == Gesture.DELETE) {
                                UMLObject umlObj = getContainingUMLObject(tempPoints);
                                if (umlObj != null) {
                                    removeView(umlObj.view);
                                    umlObjects.remove(umlObj);
                                    invalidate();
                                }
                            } else {
                                performGestureAction(results.get(0), bounds);
                            }
                        } else {
                            Toast.makeText(DrawingView.this.getContext(),
                                    "No result",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
    }

    private UMLObject getContainingUMLObject(List<Point> points) {
        for (UMLObject umlObject : umlObjects) {
            int count = 0;
            for (Point p : points) {
                if (isPointInsideView(p, umlObject.view))
                    count++;
            }
            if ((double) count / points.size() >= 0.3) return umlObject;
        }
        return null;
    }

    private boolean isPointInsideView(Point p, View v) {
        return p.x >= v.getX() && p.x <= (v.getX() + v.getMeasuredWidth())
                && p.y >= v.getY() && p.y <= (v.getY() + v.getMeasuredHeight());
    }

    private void performGestureAction(RecognizerResult result, final Point[] bounds){
        Point leftMost = bounds[0];
        Point rightMost = bounds[1];
        Point topMost = bounds[2];
        Point bottomMost = bounds[3];
        Size tempSize = result.size;

        if(result.gesture == Gesture.CLASSIFIER) {
            ClassDiagramView view = (ClassDiagramView) LayoutInflater.from(getContext()).inflate(R.layout.class_diagram_layout, DrawingView.this, false);
            view.init(DrawingView.this.getContext());
            view.setX((float) leftMost.x);
            view.setY((float) topMost.y);
            umlObjects.add(new ClassDiagramObject(view));
            addView(view, new LinearLayout.LayoutParams(tempSize.getWidth(), tempSize.getHeight()));
        } else if(result.gesture == Gesture.UNSPECIFIED) {
//            // TODO: Figure out the two closest classfiers by index
//            ClassDiagramObject objectSrc = (ClassDiagramObject)umlObjects.get(0);
//            ClassDiagramObject objectDst = (ClassDiagramObject)umlObjects.get(1);
//            RelationshipView view = (RelationshipView) LayoutInflater.from(getContext()).inflate(R.layout.relationship_layout, DrawingView.this, false);
//
//            // TODO: Figure out the placement
//            view.setX((float) leftMost.x);
//            view.setY((float) topMost.y);
//
//            // TODO: Need to figure out the size to make the relationship
//            Size relationshipSize = new Size(objectDst.getLocation().x - objectSrc.getLocation().x, );
//
//            // For testing
//            RelationshipObject relationship = new RelationshipObject(view, objectSrc, objectDst);
//            // Need to fetch the right and left most points, top and bottom
        }
        Toast.makeText(DrawingView.this.getContext(),
                "Results were size 1, gesture="+ result.gesture.toString(),
                Toast.LENGTH_SHORT).show();
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

    public void generateRecognizerOptionsDialog(final ArrayList<RecognizerResult> results,
                                                final ArrayList<Point>points,
                                                final Point[] bounds){

        final Dialog gestureDialog = new Dialog(this.getContext());
        gestureDialog.setTitle("Clarify your gesture:");

        // Gives the layout inflater
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE);

        // Gives me the layout
        View view = inflater.inflate(R.layout.gesture_chooser, null);
        LinearLayout ll = (LinearLayout) view.findViewById(R.id.gesture_picker_layout);

        int pos = 0;
        for(RecognizerResult rr: results){
            final ImageButton rrBtn = new ImageButton(this.getContext());
            final Gesture tempGesture = rr.gesture;
            rrBtn.setImageResource(getResources().getIdentifier(rr.gesture.imageName, "drawable",
                    this.getContext().getPackageName()));
            rrBtn.setId(pos);
            rrBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Notify the templateManager to update based on the user's selection
                    TemplateManager.addNewTemplate(tempGesture, points);
                    TemplateManager.save(getContext());
                    Log.i(TAG + "DIALOG", "ID is: " + v.getId());
                    // TODO Get the result that corresponds to this button
                    performGestureAction(results.get(v.getId()), bounds);
                    gestureDialog.dismiss();
                }
            });
            rrBtn.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            ll.addView(rrBtn);
            pos++;
        }

        // Do this at the end
        gestureDialog.setContentView(view);

        gestureDialog.show();
    }


    private Point getLeftMost() {
        Point leftMost = new Point(Double.MAX_VALUE, Double.MAX_VALUE, -1);
        if(points != null){
            for(Point p: points){
                if(p.x < leftMost.x){
                    leftMost = p;
                }
            }
            return leftMost;
        } else {
            Log.e(TAG, "POINTS WAS NULL");
            return null;
        }
    }

    private Point getRightMost() {
        Point rightMost = new Point(Double.MIN_VALUE, Double.MIN_VALUE, -1);
        if(points != null){
            for(Point p: points){
                if(p.x > rightMost.x){
                    rightMost = p;
                }
            }
            return rightMost;
        } else {
            Log.e(TAG, "POINTS WAS NULL");
            return null;
        }
    }

    private Point getTopMost() {
        Point topMost = new Point(Double.MAX_VALUE, Double.MAX_VALUE, -1);
        if(points != null){
            for(Point p: points){
                if(p.y < topMost.y){
                    topMost = p;
                }
            }
            return topMost;
        } else {
            Log.e(TAG, "POINTS WAS NULL");
            return null;
        }
    }

    private Point getBottomMost() {
        Point bottomMost = new Point(Double.MIN_VALUE, Double.MIN_VALUE, -1);
        if(points != null){
            for(Point p: points){
                if(p.y > bottomMost.y){
                    bottomMost = p;
                }
            }
            return bottomMost;
        } else {
            Log.e(TAG, "POINTS WAS NULL");
            return null;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

            DrawingView.this.setScaleX(mScaleFactor);
            DrawingView.this.setScaleY(mScaleFactor);

//            int height = (int)(DrawingView.this.getHeight() * (1/mScaleFactor));
//            int width = (int)(DrawingView.this.getWidth() * (1/mScaleFactor));
//            DrawingView.this.setLayoutParams(new LinearLayout.LayoutParams(width, height));
            Log.i(TAG, "HEIGHT IS: " + DrawingView.this.getHeight() + " WIDTH IS: " + DrawingView.this.getWidth());
            // Resize when views new size would be smaller than the view size
            mScaled = true;
            invalidate();
            return true;
        }
    }
}
