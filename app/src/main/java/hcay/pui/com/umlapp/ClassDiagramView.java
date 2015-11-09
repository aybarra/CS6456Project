package hcay.pui.com.umlapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by andrasta on 11/8/15.
 * Uses Canvas to draw
 */
public class ClassDiagramView extends View {

    private Bitmap mResultBitmap; //!< Bitmap to draw markers on
    private Canvas mResultCanvas; //!< Canvas to draw markers to (to draw to bitmap)

    private int mDrawBitmapWidth = 300; //!< Default width of view
    private int mDrawBitmapHeight = 300; //!< Default height of view

    private Matrix mDrawingMatrix; //!< Matrix for rotating markers
    private Paint mDrawingPaint; //!< Paint for drawing markers
    private Rect mDrawRect; //!< Draw rect for drawing completed wheel to view canvas (needs to be set to size of view)

    public ClassDiagramView(Context context, AttributeSet attrs){
        super(context, attrs);

        init(context);
    }

    public ClassDiagramView(Context context, int width, int height){
        super(context);

        mDrawBitmapWidth = width;
        mDrawBitmapHeight = height;

        Toast.makeText(context, "Width is: " + width + " and height is: "+ height, Toast.LENGTH_SHORT).show();
        init(context);
    }


    private void init(Context context){
        mResultBitmap = Bitmap.createBitmap(mDrawBitmapWidth, mDrawBitmapHeight, Bitmap.Config.ARGB_8888);
        mResultCanvas = new Canvas(mResultBitmap);

        mDrawingPaint = new Paint();
        mDrawingPaint.setAntiAlias(true);
        mDrawingPaint.setDither(true);
        mDrawingPaint.setFilterBitmap(true);

        mDrawingMatrix = new Matrix();

        mDrawRect = new Rect(0, 0, mDrawBitmapWidth, mDrawBitmapHeight);
    }

    /*
	 * Drawing of the view to it's canvas
	 *
	 * (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Clear canvas before redrawing
        mResultCanvas.drawColor(Color.TRANSPARENT);
        canvas.drawColor(Color.TRANSPARENT);

//        //Draw each marker
//        for(int i = 0; i < mMaxUVMarkers; i++){
//            Bitmap marker = getMarkerBitmap(i);
//
//            int centerX = (mDrawBitmapWidth/2) - (marker.getWidth()/2);
//            int centerY = (mDrawBitmapHeight/2) - (marker.getHeight()/2);
//            mDrawingMatrix.reset();
//            mDrawingMatrix.postTranslate(centerX, centerY);
//            mDrawingMatrix.postTranslate(0, -centerY);
//            mDrawingMatrix.postRotate(mRotationDegrees*(float)i, mDrawBitmapWidth/2, mDrawBitmapHeight/2);
//            mResultCanvas.drawBitmap(marker, mDrawingMatrix, mDrawingPaint);
//        }

//        mResultBitmap.
//        mResultCanvas.drawText("TEST STRING", 0, 0, mDrawingPaint);

        //After drawing wheel to result bitmap, draw the result bitmap to the view's canvas.
        //This allows the view to be resized to whatever size the view needs to be.
//        canvas.drawBitmap(mResultBitmap, null, mDrawRect, mDrawingPaint);
        canvas.drawText("TEST STRING", 0,100, mDrawingPaint);
    }


    /*
	 * Determining view size based on constraints from parent views
	 *
	 * (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Get size requested and size mode
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width, height = 0;

        //Determine Width
        switch(widthMode){
            case MeasureSpec.EXACTLY:
                width = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                width = Math.min(mDrawBitmapWidth, widthSize);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                width = mDrawBitmapWidth;
                break;
        }

        //Determine Height
        switch(heightMode){
            case MeasureSpec.EXACTLY:
                height = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                height = Math.min(mDrawBitmapHeight, heightSize);
                break;
            case MeasureSpec.UNSPECIFIED:
            default:
                height = mDrawBitmapHeight;
                break;
        }

        setMeasuredDimension(width, height);
    }

    /*
     * Called after onMeasure, returning the actual size of the view before drawing.
     *
     * (non-Javadoc)
     * @see android.view.View#onSizeChanged(int, int, int, int)
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        //Change height and width of draw rect to size of view
        //mDrawRect is using to draw the wheel to the view's canvas,
        //setting mDrawRect to the width and height of the view ensures
        //the wheel is drawn correctly to the view
        mDrawRect.set(0, 0, w, h);
    }

//    /*
//     * Setter for UVIndex
//     */
//    public void setUVIndex(int index){
//        if(index < 0) index = 0;
//        else if(index > mMaxUVMarkers) index = mMaxUVMarkers;
//
//        mUVIndex = index;
//
//        invalidate();
//    }
//
//    /*
//     * Returns correct "on" or "off" bitmap for marker index
//     */
//    private Bitmap getMarkerBitmap(int index){
//        if (index < mUVIndex){
//            return mLightMarkerBitmap;
//        }else{
//            return mDarkMarkerBitmap;
//        }
//    }
}
