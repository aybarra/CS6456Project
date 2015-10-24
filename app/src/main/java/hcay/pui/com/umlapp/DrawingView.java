package hcay.pui.com.umlapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Handler;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.TypedValue;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import hcay.pui.com.recognizer.Point;
import hcay.pui.com.recognizer.RecognizerResult;
import hcay.pui.com.recognizer.Recognizer;

/**
 * @author Andy Ybarra
 */
public class DrawingView extends View {

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
    private boolean erase=false;

    Recognizer recognizer;
    ArrayList<Point> points;

    // Distinguishes between different strokes
    static int strokeCounter = 0;

    // Timer related variables
    Timer timer;
    TimerTask timerTask;

    final Handler handler = new Handler();


    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
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
        //draw view
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
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

                // set the timer for the recognizer to be called
                startTimer();
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

    public void setErase(boolean isErase){
        //set erase true or false
        erase=isErase;

        if(erase) {
            drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//            drawPaint.setPathEffect(new DashPathEffect(new float[] {10,10}, 5));
        }
        else drawPaint.setXfermode(null);
    }

    public void startNew(){
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

    public void initializeTimerTask(){
        timerTask = new TimerTask(){
            public void run(){
                handler.post(new Runnable() {
                    public void run(){
                        ArrayList<RecognizerResult> results = recognizer.recognize(points);
                        points.clear();
                        drawPath.reset();
                        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
                        invalidate();
                        Toast.makeText(DrawingView.this.getContext(),
                                "Recognizer called",
                                Toast.LENGTH_SHORT).show();
                        for(RecognizerResult rr: results){
                            Toast.makeText(DrawingView.this.getContext(),
                                    "Recognizer found" + rr.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }

                });
            }
        };
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

}
