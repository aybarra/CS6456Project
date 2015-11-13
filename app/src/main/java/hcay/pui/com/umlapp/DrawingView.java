package hcay.pui.com.umlapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Rect;
import android.graphics.RectF;
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

    private PathEffect dashEffect = new DashPathEffect(new float[] {30,60}, 0);

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
    private List<NoteView> notes;
    private List<Object> selectedObjects = new ArrayList<>();

    android.graphics.Point dispSize;
    int dispWidth;
    int dispHeight;

    private ArrayList<Action> backwardHistory = new ArrayList<>();
    private ArrayList<Action> forwardHistory = new ArrayList<>();

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
        notes = new ArrayList<>();

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
                    if (selectionEnabled) {
                        selectedObjects.clear();
                        drawPath.reset();
                        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        invalidate();
                    }
                    drawPath.moveTo(touchX, touchY);
                    break;
                case MotionEvent.ACTION_MOVE:
                    drawPath.lineTo(touchX, touchY);
                    points.add(new Point(touchX, touchY, strokeCounter));
                    break;
                case MotionEvent.ACTION_UP:
                    if (points.size() == 0) break;
                    if (selectionEnabled) {
                        drawPath.lineTo((float) points.get(0).x, (float) points.get(0).y);
                        points.add(points.get(0));
                    }
                    drawCanvas.drawPath(drawPath, drawPaint);
                    strokeCounter++;

                    // Only set the time if we're in drawing mode
                    if(!selectionEnabled){
                        // set the timer for the recognizer to be called
                        startTimer();
                    } else {
                        performSelection();
                    }

                    break;
                default:
                    return false;
            }

            invalidate();
            return true;
        }
    }

    private void performSelection() {
        Ellipse ellipse = new Ellipse(points);

        for (UMLObject umlObject : umlObjects) {
            if (ellipse.contains(umlObject.view)) {
                selectedObjects.add(umlObject);
            }
        }

        for (NoteView noteView : notes) {
            if (ellipse.contains(noteView)) {
                selectedObjects.add(noteView);
            }
        }

        points.clear();
    }

    private class Ellipse {
        private float x, y, width, height;

        public Ellipse(List<Point> points) {
            float minX, maxX, minY, maxY;
            minX = minY = Float.MAX_VALUE;
            maxX = maxY = Float.MIN_VALUE;

            for (Point p : points) {
                minX = Math.min(minX, (float) p.x);
                maxX = Math.max(maxX, (float) p.x);
                minY = Math.min(minY, (float) p.y);
                maxY = Math.max(maxY, (float) p.y);
            }

            this.x = minX;
            this.y = minY;
            this.width = maxX - minX + 1;
            this.height = maxY - minY + 1;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public float getWidth() {
            return width;
        }

        public float getHeight() {
            return height;
        }

        public boolean contains(Point p) {
            double a = (p.x - x) / width - 0.5;
            double b = (p.y - y) / height - 0.5;
            return a * a + b * b < 0.25;
        }

        public boolean contains(RectF rect) {
            return contains(new Point(rect.left, rect.top))
                    && contains(new Point(rect.right, rect.top))
                    && contains(new Point(rect.left, rect.bottom))
                    && contains(new Point(rect.right, rect.bottom));
        }

        public boolean contains(View view) {
            return contains(new RectF(view.getX(), view.getY(), view.getX() + view.getMeasuredWidth(), view.getY() + view.getMeasuredHeight()));
        }
    }

    private boolean contains(float x, float y, float width, float height, Point p) {
        double a = (p.x - x) / width - 0.5;
        double b = (p.y - y) / height - 0.5;
        return a * a + b * b < 0.25;
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
        selectionEnabled = isSelecting;

        if (selectionEnabled) {
            drawPaint.setPathEffect(dashEffect);
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
        notes.clear();
        selectedObjects.clear();
        backwardHistory.clear();
        forwardHistory.clear();
        updateUndoRedoItems();

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

                        if (results.size() > 1) {
                            // Launch the dialog if there are options that are too close
                            generateRecognizerOptionsDialog(results, tempPoints, bounds);
                        } else if (results.size() == 1) {
                            RecognizerResult result = results.get(0);
                            if (result.gesture == Gesture.DELETE) {
                                performDeleteAction(tempPoints);
                            } else if (result.gesture == Gesture.PIGTAIL) {
                                performNoteAction(tempPoints.get(0));
                            } else {
                                performGestureAction(result, bounds);
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

    private void performDeleteAction(List<Point> points) {
        UMLObject umlObj = getContainingUMLObject(points);
        if (umlObj != null) {
            deleteUMLObject(umlObj);
            backwardHistory.add(new Action(ActionType.REMOVED, umlObj));
        } else {
            NoteView noteView = getContainingNoteView(points);
            if (noteView != null) {
                deleteNote(noteView);
                backwardHistory.add(new Action(ActionType.REMOVED, noteView));
            }
        }
        forwardHistory.clear();
        updateUndoRedoItems();
    }

    private UMLObject deleteUMLObject(UMLObject umlObject) {
        removeView(umlObject.view);
        if (umlObject instanceof ClassDiagramObject) {
            NoteView noteView = ((ClassDiagramObject) umlObject).noteView;
            if (noteView != null) {
                removeView(noteView);
            }
        }
        umlObjects.remove(umlObject);
        invalidate();
        return umlObject;
    }

    private NoteView deleteNote(NoteView noteView) {
        removeView(noteView);
        notes.remove(noteView);
        invalidate();
        return noteView;
    }

    private void performNoteAction(Point firstPoint) {
        UMLObject umlObj = getContainingUMLObject(firstPoint);
        if (umlObj != null && umlObj instanceof ClassDiagramObject) {
            NoteView view = (NoteView) LayoutInflater.from(getContext()).inflate(R.layout.note_layout, DrawingView.this, false);
            view.setX(umlObj.view.getX() + umlObj.view.getMeasuredWidth() + 20);
            view.setY(umlObj.view.getY() - 50);
            notes.add(view);
            ((ClassDiagramObject) umlObj).noteView = view;
            addView(view);
            invalidate();
            backwardHistory.add(new Action(ActionType.ADDED, view));
            forwardHistory.clear();
            updateUndoRedoItems();
        }
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

    private UMLObject getContainingUMLObject(Point point) {
        for (UMLObject umlObject : umlObjects) {
            if (isPointInsideView(point, umlObject.view))
                return umlObject;
        }
        return null;
    }

    private NoteView getContainingNoteView(List<Point> points) {
        for (NoteView note : notes) {
            int count = 0;
            for (Point p : points) {
                if (isPointInsideView(p, note))
                    count++;
            }
            if ((double) count / points.size() >= 0.15) return note;
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
            UMLObject classDiagramObject = new ClassDiagramObject(view);
            umlObjects.add(classDiagramObject);
            addView(view, new LinearLayout.LayoutParams(tempSize.getWidth(), tempSize.getHeight()));
            backwardHistory.add(new Action(ActionType.ADDED, classDiagramObject));
            forwardHistory.clear();
            updateUndoRedoItems();
        } else if(result.gesture == Gesture.NAVIGABLE || result.gesture == Gesture.AGGREGATION
                || result.gesture == Gesture.GENERALIZATION || result.gesture == Gesture.REALIZATION
                || result.gesture == Gesture.COMPOSITION || result.gesture == Gesture.DEPENDENCY
                || result.gesture == Gesture.REALIZATION_DEPENDENCY || result.gesture == Gesture.REQUIRED) {

            GestureOrientation orientation = getGestureOrientation(bounds);
            Log.i(TAG, "Gesture orientation is: " + orientation.toString());

            // TODO: Figure out the two closest classfiers by index
            ClassDiagramObject objectSrc;
            ClassDiagramObject objectDst;
            if(orientation == GestureOrientation.LEFT_TO_RIGHT || orientation == GestureOrientation.RIGHT_TO_LEFT) {
                int left = findClosestTo(leftMost, null);
                if(left == -1){
                    Toast.makeText(getContext(), "No class diagrams made yet", Toast.LENGTH_SHORT).show();
                    return;
                }
                int right = findClosestTo(rightMost, umlObjects.get(left));
                if(right == -1){
                    Toast.makeText(getContext(), "Need two class diagrams to make a relationship", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Perform set based on orientation
                if(orientation == GestureOrientation.LEFT_TO_RIGHT){
                    objectSrc = (ClassDiagramObject)umlObjects.get(left);
                    objectDst = (ClassDiagramObject)umlObjects.get(right);
                } else {
                    objectDst = (ClassDiagramObject)umlObjects.get(left);
                    objectSrc = (ClassDiagramObject)umlObjects.get(right);
                }
            } else {
                int top = findClosestTo(topMost, null);
                if(top == -1){
                    Toast.makeText(getContext(), "No class diagrams made yet", Toast.LENGTH_SHORT).show();
                    return;
                }
                int bottom = findClosestTo(bottomMost, umlObjects.get(top));
                if(bottom == -1){
                    Toast.makeText(getContext(), "Need two class diagrams to make a relationship", Toast.LENGTH_SHORT).show();
                }

                // Perform set based on orientation
                if(orientation == GestureOrientation.TOP_TO_BOTTOM){
                    objectSrc = (ClassDiagramObject)umlObjects.get(top);
                    objectDst = (ClassDiagramObject)umlObjects.get(bottom);
                } else {
                    objectDst = (ClassDiagramObject)umlObjects.get(top);
                    objectSrc = (ClassDiagramObject)umlObjects.get(bottom);
                }
            }

            RelationshipView view = (RelationshipView) LayoutInflater.from(getContext()).inflate(R.layout.relationship_layout, DrawingView.this, false);
            view.init(DrawingView.this.getContext(), objectSrc, objectDst, orientation);

            // TODO: Figure out the placement
            android.graphics.Point location = getPlacementLocation(orientation, objectSrc);
            view.setX((float) location.x);
            view.setY((float) location.y);
            Toast.makeText(getContext(), "Placement of relationship is: " + location.x + ", " + location.y, Toast.LENGTH_SHORT).show();


            // TODO: Need to figure out the size to make the relationship
            Size relationshipSize = new Size((int)Math.abs(leftMost.x-rightMost.x), (int)Math.abs(topMost.y-bottomMost.y));
            Toast.makeText(getContext(), "Size of relationship is: " + relationshipSize.getWidth() + ", " + relationshipSize.getHeight(), Toast.LENGTH_SHORT).show();
            RelationshipObject relationship = new RelationshipObject(view, objectSrc, objectDst, result.gesture);
            DrawingView.this.addView(view, new LinearLayout.LayoutParams(relationshipSize.getWidth(), relationshipSize.getHeight()));
            umlObjects.add(relationship);
        }
        Toast.makeText(DrawingView.this.getContext(),
                "Results were size 1, gesture="+ result.gesture.toString(),
                Toast.LENGTH_SHORT).show();
    }

    public android.graphics.Point getPlacementLocation(GestureOrientation orientation, UMLObject src){
        int height = src.getSize().getHeight();
        int width = src.getSize().getWidth();
        android.graphics.Point location = src.getLocation();
        if(orientation == GestureOrientation.LEFT_TO_RIGHT){
            location.x += width;
            location.y += height/2;
        } else if(orientation == GestureOrientation.RIGHT_TO_LEFT){
            location.y += height/2;
        } else if(orientation == GestureOrientation.TOP_TO_BOTTOM){
            location.x += width/2;
            location.y += height;
        } else if(orientation == GestureOrientation.BOTTOM_TO_TOP){
            location.x += width/2;
        }
        return location;
    }
    /**
     *
     * @param target - Target location we want to be close to
     * @param disregard - In case this is called in succession, you can look at all except disregard
     * @return - index of uml object in our umlobjects arraylist, -1 if there is none
     */
    public int findClosestTo(Point target, UMLObject disregard){

        double distance = Double.MAX_VALUE;
        int minIndex = -1;
        // Only care about classDiagrams so disregard the other relationships
        for(int i = 0; i < umlObjects.size(); i++){
            UMLObject obj = umlObjects.get(i);
            if(obj instanceof ClassDiagramObject){
                if(disregard != null && obj.equals(disregard)){
                    continue;
                }
                double ptDistance = Math.sqrt(Math.pow((obj.getLocation().x - target.x),2) + Math.pow((obj.getLocation().y - target.y),2));
                if(ptDistance < distance){
                    distance = ptDistance;
                    minIndex = i;
                }
            }
        }
        return minIndex;
    }
    /**
     *
     * @param bounds
     * @return
     */
    public GestureOrientation getGestureOrientation(Point [] bounds){
        double midX;
        double midY;

        //{ left, right, top, bottom }
        // Need to figure out which is longer
        if(Math.abs(bounds[1].x-bounds[0].x) > Math.abs(bounds[2].y-bounds[3].y)){
            // right-left is longer
            // Figure out which direction its pointing
            midX = (bounds[0].x + bounds[1].x)/2;
            // This says the point is on the left
            //     top
            // left <-----|---------
            //     bottom
            if(bounds[0].x < midX && bounds[2].x < midX && bounds[3].x < midX){
                return GestureOrientation.RIGHT_TO_LEFT;
            } else {
                return GestureOrientation.LEFT_TO_RIGHT;
            }
        } else{
            // top-bottom is longer
            midY = (bounds[2].y + bounds[3].y)/2;
            // Figure out which direction its pointing
            // Similarly
            //      |
            //      |
            //    -----
            //      |
            //      |
            // left V right
            //    bottom
            if(bounds[0].y > midY && bounds[1].y > midY && bounds[3].y > midY){
                return GestureOrientation.TOP_TO_BOTTOM;
            } else{
                return GestureOrientation.BOTTOM_TO_TOP;
            }
        }
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

    public void undoOrRedo(boolean undo) {
        ArrayList<Action> history = undo ? backwardHistory : forwardHistory;
        Action action = processAction(history.remove(history.size() - 1));
        if (undo) forwardHistory.add(action);
        else backwardHistory.add(action);
        updateUndoRedoItems();
    }

    private void updateUndoRedoItems() {
        MainActivity.undoItem.setEnabled(!backwardHistory.isEmpty());
        MainActivity.redoItem.setEnabled(!forwardHistory.isEmpty());
        MainActivity.undoItem.getIcon().setAlpha(backwardHistory.isEmpty() ? 50 : 255);
        MainActivity.redoItem.getIcon().setAlpha(forwardHistory.isEmpty() ? 50 : 255);
    }

    private Action processAction(Action action) {
        switch (action.type) {
            case ADDED:
                if (action.isUMLObject) deleteUMLObject(action.umlObject);
                if (action.noteView != null) deleteNote(action.noteView);
                action.type = ActionType.REMOVED;
                break;
            case REMOVED:
                if (action.isUMLObject) {
                    umlObjects.add(action.umlObject);
                    addView(action.umlObject.view);
                }
                if (action.noteView != null) {
                    notes.add(action.noteView);
                    addView(action.noteView);
                }
                invalidate();
                action.type = ActionType.ADDED;
                break;
            case NONE:
                break;
        }
        return action;
    }

    private class Action {
        public ActionType type = ActionType.NONE;
        public NoteView noteView;
        public UMLObject umlObject;
        public boolean isUMLObject;

        public Action(ActionType type, NoteView noteView) {
            this.type = type;
            this.noteView = noteView;
            this.isUMLObject = false;
        }

        public Action(ActionType type, UMLObject umlObject) {
            this.type = type;
            this.umlObject = umlObject;
            this.noteView = umlObject instanceof ClassDiagramObject ? ((ClassDiagramObject) umlObject).noteView : null;
            this.isUMLObject = true;
        }
    }

    private enum ActionType {
        NONE, ADDED, REMOVED
    }
}
