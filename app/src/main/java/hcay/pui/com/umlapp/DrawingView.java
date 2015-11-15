package hcay.pui.com.umlapp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Matrix;
import android.graphics.PathEffect;

import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
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
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    private Canvas relCanvas;
    private Bitmap relBitmap;

    private float brushSize, lastBrushSize;
    private boolean selectionEnabled = false;

    private PathEffect dashEffect = new DashPathEffect(new float[] {30,60}, 0);

    Recognizer recognizer;
    ArrayList<Point> points;

    // Distinguishes between different strokes
    static int strokeCounter = 0;

    // Timer related variables
    Timer timer;
    TimerTask timerTask;

    final Handler handler = new Handler();

    private List<UMLObject> umlObjects;
    private List<NoteView> notes;
    private List<Relationship> relationships;
    private List<Object> selectedObjects = new ArrayList<>();

    private boolean moving = false;
    private Ellipse selectedRegion;
    private Point originalPoint;

    protected float mPosX;
    protected float mPosY;
    protected float mPosX0 = 0;     // initial displacement values
    protected float mPosY0 = 0;
    protected float mFocusX;
    protected float mFocusY;
    protected float mLastTouchX;
    protected float mLastTouchY;
    protected static final int INVALID_POINTER_ID = -1;
    protected int mActivePointerId = INVALID_POINTER_ID;

    private ArrayList<ArrayList<Action>> backwardHistory = new ArrayList<>();
    private ArrayList<ArrayList<Action>> forwardHistory = new ArrayList<>();

    private boolean isViewMode = false;

    private static final int MAX_CANVAS_WIDTH = 7500;
    private static int MAX_CANVAS_HEIGHT;
    private GestureMode gestureMode = GestureMode.NONE;
    private ScaleGestureDetector scaleDetector;
    private float scaleFactor = 1.f;
    private float prevX = 0;
    private float prevY = 0;
    private boolean dragged = false;
    private int screenWidth, screenHeight;
    private static final float MAX_SCALE_FACTOR = 2.f;
    private static float MIN_SCALE_FACTOR = 0;

    private final int CLASSIFIER_MIN_WIDTH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, getResources().getDisplayMetrics());
    private final int STROKE_WIDTH = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        android.graphics.Point size = new android.graphics.Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        int rid = getResources().getIdentifier("status_bar_height", "dimen", "android");
        int barHeight = rid > 0 ? getResources().getDimensionPixelSize(rid) : 0;
        MAX_CANVAS_HEIGHT = MAX_CANVAS_WIDTH * (screenHeight - barHeight) / screenWidth;
        setupDrawing(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < this.getChildCount(); i++) {
            getChildAt(i).layout(l, t, r, b);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(MAX_CANVAS_WIDTH, MAX_CANVAS_HEIGHT);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void switchMode() {
        isViewMode = !isViewMode;
        for (UMLObject umlObject : umlObjects) {
            if (umlObject instanceof ClassDiagramObject) {
                ((ClassDiagramView) umlObject.view).changeMode(isViewMode);
                if (((ClassDiagramObject) umlObject).noteView != null)
                    ((ClassDiagramObject) umlObject).noteView.setVisibility(isViewMode ? GONE : VISIBLE);
            }
        }
        MainActivity.viewItem.getIcon().setAlpha(isViewMode ? 255 : 200);
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
        relationships = new ArrayList<>();

        scaleDetector = new ScaleGestureDetector(context, new ScaleListener());

        DecoratorUtil.setStrokeWidth(STROKE_WIDTH);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //view given size
        super.onSizeChanged(w, h, oldw, oldh);
        MIN_SCALE_FACTOR = (float) screenWidth / w;
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);

        setX(-(w / 2.0f - screenWidth / 2.0f));
        setY(-(h / 2.0f - screenHeight / 2.0f));

        relBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        relCanvas = new Canvas(relBitmap);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        canvas.save();
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

        // New stuff
        canvas.drawBitmap(relBitmap, 0, 0, canvasPaint);

        canvas.drawPath(drawPath, drawPaint);
        canvas.restore();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float detectorScaleFactor = detector.getScaleFactor();
            if (detectorScaleFactor > 0.9 && detectorScaleFactor < 1.1)
                return false;
            scaleFactor *= detectorScaleFactor;
            scaleFactor = Math.max(Math.min(scaleFactor, MAX_SCALE_FACTOR), MIN_SCALE_FACTOR);
            setScaleX(scaleFactor);
            setScaleY(scaleFactor);
            gestureMode = GestureMode.ZOOM;
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        if (event.getPointerCount() == 2) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    gestureMode = GestureMode.DRAG;
                    prevX = event.getX();
                    prevY = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    clearPath();

                    float dx = (event.getX() - prevX) * scaleFactor;
                    float dy = (event.getY() - prevY) * scaleFactor;
                    if (dx != 0 || dy != 0) dragged = true;

                    if (dragged) {
                        gestureMode = GestureMode.DRAG;
                        setX(getX() + dx);
                        setY(getY() + dy);
                    }

                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    gestureMode = GestureMode.ZOOM;
                    break;
                case MotionEvent.ACTION_UP:
                    gestureMode = GestureMode.NONE;
                    dragged = false;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    gestureMode = GestureMode.DRAG;
                    break;
            }

            scaleDetector.onTouchEvent(event);
            if ((gestureMode == GestureMode.DRAG && scaleFactor != 1f && dragged) || gestureMode == GestureMode.ZOOM) {
                invalidate();
            }
            return true;
        } else {
            if (isViewMode) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    prevX = event.getX();
                    prevY = event.getY();
                }
                return true;
            }

            if (gestureMode != GestureMode.NONE && event.getAction() != MotionEvent.ACTION_UP) {
                return true;
            }

            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    prevX = event.getX();
                    prevY = event.getY();
                    // Need to check if the timer is set, if so dismiss it
                    if (timer != null) {
                        stopTimer();
                    }
                    if (selectionEnabled) {
                        originalPoint = new Point(touchX, touchY);
                        if (selectedRegion != null && selectedRegion.contains(originalPoint)) {
                            moving = true;
                        } else {
                            deselect();
                            points.clear();
                            drawPath.moveTo(touchX, touchY);
                            MainActivity.updateDeleteItem(false);
                        }
                    } else {
                        drawPath.moveTo(touchX, touchY);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (moving) {
                        moveSelection(touchX, touchY);
                    } else {
                        drawPath.lineTo(touchX, touchY);
                        points.add(new Point(touchX, touchY, strokeCounter));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (gestureMode != GestureMode.NONE) {
                        gestureMode = GestureMode.NONE;
                        return true;
                    }

                    if (moving) {
                        moving = false;
                        break;
                    }
                    if (points.size() == 0) break;
                    if (selectionEnabled) {
                        drawPath.lineTo((float) points.get(0).x, (float) points.get(0).y);
                        points.add(points.get(0));
                    }
                    strokeCounter++;

                    // Only set the time if we're in drawing mode
                    if (!selectionEnabled) {
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

    private void moveSelection(float x, float y) {
        float dx = x - (float) originalPoint.x;
        float dy = y - (float) originalPoint.y;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            drawPath.offset(dx, dy);
        } else {
            offsetPath(dx, dy);
        }
        originalPoint = new Point(x, y);
        for (Object selectedObject : selectedObjects) {
            if (selectedObject instanceof ClassDiagramObject) {
                ClassDiagramObject classDiagramObject = (ClassDiagramObject) selectedObject;
                float newX = classDiagramObject.view.getX() + dx;
                float newY = classDiagramObject.view.getY() + dy;
                classDiagramObject.view.setX(newX);
                classDiagramObject.view.setY(newY);
                if (classDiagramObject.noteView != null) {
                    newX = classDiagramObject.noteView.getX() + dx;
                    newY = classDiagramObject.noteView.getY() + dy;
                    classDiagramObject.noteView.setX(newX);
                    classDiagramObject.noteView.setY(newY);
                }
            } else if (selectedObject instanceof NoteView) {
                NoteView noteView = (NoteView) selectedObject;
                // TODO: moving noteViews separately?
            }
        }
    }

    private void offsetPath(float dx, float dy) {
        drawPath.rewind();
        drawPath.moveTo((float) points.get(0).x, (float) points.get(0).y);
        for (int i = 1; i < points.size(); i++) {
            Point p = points.get(i);
            p.x += dx;
            p.y += dy;
            drawPath.lineTo((float) p.x, (float) p.y);
        }
        drawPath.close();
    }

    private void performSelection() {
        selectedRegion = new Ellipse(points);

        for (UMLObject umlObject : umlObjects) {
            if (selectedRegion.contains(umlObject.view)) {
                selectedObjects.add(umlObject);
            }
        }

        for (NoteView noteView : notes) {
            if (selectedRegion.contains(noteView)) {
                selectedObjects.add(noteView);
            }
        }

        if (selectedObjects.isEmpty()) clearPath();
        else MainActivity.updateDeleteItem(true);
    }

    public void deleteSelected() {
        ArrayList<Action> actions = new ArrayList<>();
        for (Object selectedObject : selectedObjects) {
            if (selectedObject instanceof ClassDiagramObject) {
                ClassDiagramObject classDiagramObject = (ClassDiagramObject) selectedObject;
                deleteUMLObject(classDiagramObject);
                actions.add(new Action(Action.ActionType.REMOVED, classDiagramObject));
            } else if (selectedObject instanceof NoteView) {
                NoteView noteView = (NoteView) selectedObject;
                if (noteView.getParent() != null) {
                    deleteNote(noteView);
                    actions.add(new Action(Action.ActionType.REMOVED, noteView));
                }
            }
        }
        backwardHistory.add(actions);
        forwardHistory.clear();
        updateUndoRedoItems();
        deselect();
    }

    private void deselect() {
        MainActivity.updateDeleteItem(false);
        selectedObjects.clear();
        clearPath();
    }

    private void clearPath() {
        selectedRegion = null;
        drawPath.reset();
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
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
            deselect();
            drawPaint.setPathEffect(null);
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
        relationships.clear();
        selectedObjects.clear();
        backwardHistory.clear();
        forwardHistory.clear();
        updateUndoRedoItems();

        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        relCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void initializeTimerTask(){
        timerTask = new TimerTask(){
            public void run(){
                handler.post(new Runnable() {
                    public void run(){
                        if (points == null || points.isEmpty()) return;
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
            backwardHistory.add(Action.create(Action.ActionType.REMOVED, umlObj));
        }

        NoteView noteView = getContainingNoteView(points);
        if (noteView != null && noteView.getParent() != null) {
            deleteNote(noteView);
            backwardHistory.add(Action.create(Action.ActionType.REMOVED, noteView));
        }

        Relationship relationship = getContainingRelationship(points);
        if (relationship != null) {
            deleteRelationship(relationship);
            backwardHistory.add(Action.create(Action.ActionType.REMOVED, relationship));
        }

        forwardHistory.clear();
        updateUndoRedoItems();
    }

    private UMLObject deleteUMLObject(UMLObject umlObject) {
        removeView(umlObject.view);
        if (umlObject instanceof ClassDiagramObject) {
            NoteView noteView = ((ClassDiagramObject) umlObject).noteView;
            if (noteView != null) {
                deleteNote(noteView);
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

    private Relationship deleteRelationship(Relationship relationship) {
        removeDrawing(relationship.path);
        relationships.remove(relationship);
        relationship.src.removeRelationship(relationship);
        relationship.dst.removeRelationship(relationship);
        invalidate();
        return relationship;
    }

    private void removeDrawing(Path path) {
        drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        drawCanvas.drawPath(path, drawPaint);
        drawPaint.setXfermode(null);
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
            backwardHistory.add(Action.create(Action.ActionType.ADDED, view));
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

    private Relationship getContainingRelationship(List<Point> points) {
        for (Relationship relationship : relationships) {
            int count = 0;
            for (Point p : points) {
                if (isPointInsideRect(p, relationship.rect)) {
                    count++;
                }
            }
            if ((double) count / points.size() >= 0.05) return relationship;
        }
        return null;
    }

    private boolean isPointInsideView(Point p, View v) {
        return p.x >= v.getX() && p.x <= (v.getX() + v.getMeasuredWidth())
                && p.y >= v.getY() && p.y <= (v.getY() + v.getMeasuredHeight());
    }

    private boolean isPointInsideRect(Point p, RectF rectF) {
        return p.x >= rectF.left && p.x <= (rectF.left + rectF.width())
                && p.y >= rectF.top && p.y <= (rectF.top + rectF.height());
    }

    private void addRelationship(Relationship relationship) {
        drawCanvas.drawPath(relationship.path, drawPaint);
        relationship.src.addRelationship(relationship);
        relationship.dst.addRelationship(relationship);
        relationships.add(relationship);
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
            addView(view, new LinearLayout.LayoutParams(Math.max(tempSize.getWidth(), CLASSIFIER_MIN_WIDTH), LayoutParams.WRAP_CONTENT));
            backwardHistory.add(Action.create(Action.ActionType.ADDED, classDiagramObject));
            forwardHistory.clear();
            updateUndoRedoItems();
        } else if(result.gesture == Gesture.NAVIGABLE || result.gesture == Gesture.AGGREGATION
                || result.gesture == Gesture.GENERALIZATION || result.gesture == Gesture.REALIZATION
                || result.gesture == Gesture.COMPOSITION || result.gesture == Gesture.DEPENDENCY
                || result.gesture == Gesture.REALIZATION_DEPENDENCY || result.gesture == Gesture.REQUIRED) {

            // Determine the orientation and direction its pointing
            GestureOrientation orientation = OrientLocUtil.getGestureOrientation(bounds);
            Log.i(TAG, "Gesture orientation is: " + orientation.toString());

            // Figure out the two closest classfiers by index
            ClassDiagramObject objectSrc;
            ClassDiagramObject objectDst;
            if(orientation == GestureOrientation.LEFT_TO_RIGHT || orientation == GestureOrientation.RIGHT_TO_LEFT) {
                int left = findClosestTo(leftMost, null);
                if(left == -1){
                    Toast.makeText(getContext(), "Need two class diagrams to make a relationship", Toast.LENGTH_SHORT).show();
                    return;
                }
                int right = findClosestTo(rightMost, umlObjects.get(left));
                if(right == -1){
                    Toast.makeText(getContext(), "Need two class diagrams to make a relationship", Toast.LENGTH_SHORT).show();
                    return;
                }

                Log.d(TAG, "left index is: " + left + " and right index is: " + right);
                // Perform set based on orientation
                if(orientation == GestureOrientation.LEFT_TO_RIGHT){
                    objectSrc = (ClassDiagramObject)umlObjects.get(left);
                    objectDst = (ClassDiagramObject)umlObjects.get(right);
                } else {
                    objectSrc = (ClassDiagramObject)umlObjects.get(right);
                    objectDst = (ClassDiagramObject)umlObjects.get(left);
                }
            } else {
                int top = findClosestTo(topMost, null);
                if(top == -1){
                    Toast.makeText(getContext(), "Need two class diagrams to make a relationship", Toast.LENGTH_SHORT).show();
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

            // Determine the size to make the relationship
            Size relationshipSize = OrientLocUtil.getRelationshipSize(objectSrc, objectDst, orientation);
//            Toast.makeText(getContext(), "Size of relationship is: " + relationshipSize.getWidth() + ", " + relationshipSize.getHeight(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Size of relationship is: " + relationshipSize.getWidth() + ", " + relationshipSize.getHeight());

            SegmentTuple tuple = DecoratorUtil.drawLineSegments(objectSrc, objectDst, orientation, relationshipSize, drawPaint, relCanvas);
            Log.i(TAG, "The size of path is: " + tuple.segPath.toString() + " last point is: " + tuple.lastPoint);
            Path fullPath = DecoratorUtil.addDecorator(tuple, result.gesture, orientation, relCanvas, drawPaint);
            relCanvas.drawPath(fullPath, drawPaint);
            Relationship relationship = new Relationship(relationshipSize, null, fullPath, objectSrc, objectDst);
            objectSrc.addRelationship(relationship);
            objectDst.addRelationship(relationship);
            relationships.add(relationship);
            backwardHistory.add(Action.create(Action.ActionType.ADDED, relationship));
            forwardHistory.clear();
            updateUndoRedoItems();
        }
        Toast.makeText(DrawingView.this.getContext(),
                "Results were size 1, gesture="+ result.gesture.toString(),
                Toast.LENGTH_SHORT).show();
    }

    /**
     *
     * @param target - Target location we want to be close to
     * @param disregard - In case this is called in succession, you can look at all except disregard
     * @return - index of uml object in our umlobjects arraylist, -1 if there are none
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
                double tpLeft = Math.sqrt(Math.pow((obj.getLocation().x - target.x),2)
                                             + Math.pow((obj.getLocation().y - target.y),2));
                double tpRight = Math.sqrt(Math.pow((obj.getLocation().x+obj.getSize().getWidth()-target.x),2)
                                             + Math.pow((obj.getLocation().y-target.y),2));
                double btmLeft = Math.sqrt(Math.pow((obj.getLocation().x - target.x),2)
                                             + Math.pow((obj.getLocation().y+obj.getSize().getHeight()-target.y),2));
                double btmRight = Math.sqrt(Math.pow((obj.getLocation().x+obj.getSize().getWidth() - target.x),2)
                                             + Math.pow((obj.getLocation().y+obj.getSize().getHeight()-target.y),2));
                if(tpLeft < distance || btmLeft < distance || tpRight < distance || btmRight < distance){
                    // Taking the min index
                    minIndex = i;

                    // distance is set to the minimum of the 4 corners
                    List<Double> list = Arrays.asList(tpLeft,tpRight,btmLeft,btmRight);
                    distance = Collections.min(list);
                }
            }
        }
        return minIndex;
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
                    // Get the result that corresponds to this button
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

    public void undoOrRedo(boolean undo) {
        if (selectionEnabled) deselect();
        ArrayList<ArrayList<Action>> history = undo ? backwardHistory : forwardHistory;
        ArrayList<Action> actions = processActions(history.remove(history.size() - 1));
        if (undo) forwardHistory.add(actions);
        else backwardHistory.add(actions);
        updateUndoRedoItems();
    }

    private void updateUndoRedoItems() {
        MainActivity.undoItem.setEnabled(!backwardHistory.isEmpty());
        MainActivity.redoItem.setEnabled(!forwardHistory.isEmpty());
        MainActivity.undoItem.getIcon().setAlpha(backwardHistory.isEmpty() ? 50 : 255);
        MainActivity.redoItem.getIcon().setAlpha(forwardHistory.isEmpty() ? 50 : 255);
    }

    private ArrayList<Action> processActions(ArrayList<Action> actions) {
        for (Action action : actions) {
            processAction(action);
        }
        return actions;
    }

    private Action processAction(Action action) {
        switch (action.type) {
            case ADDED:
                if (action.relationship != null) deleteRelationship(action.relationship);
                if (action.isUMLObject) deleteUMLObject(action.umlObject);
                if (action.noteView != null) deleteNote(action.noteView);
                action.type = Action.ActionType.REMOVED;
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
                if (action.relationship != null) {
                    addRelationship(action.relationship);
                }
                invalidate();
                action.type = Action.ActionType.ADDED;
                break;
            case NONE:
                break;
        }
        return action;
    }

    private enum GestureMode {
        NONE, DRAG, ZOOM
    }

}
