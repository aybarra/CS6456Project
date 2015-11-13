package hcay.pui.com.umlapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import java.io.File;
import java.util.UUID;
import android.provider.MediaStore;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.widget.Toast;

import hcay.pui.com.recognizer.TemplateManager;

public class MainActivity extends Activity implements View.OnClickListener {

    private DrawingView drawView;
    private ImageButton currPaint, drawBtn, selectionBtn, newBtn, saveBtn;
    private float smallBrush, mediumBrush, largeBrush;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawingView)findViewById(R.id.drawing);

        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);

        drawBtn = (ImageButton)findViewById(R.id.draw_btn);

        drawBtn.setOnClickListener(this);
        drawView.setBrushSize(smallBrush);
        drawView.setColor("#FF000000");

//        Paint fgPaintSel = new Paint();
//        fgPaintSel.setARGB(255, 0, 0,0);
//        fgPaintSel.setStyle(Style.STROKE);
//        fgPaintSel.setPathEffect(new DashPathEffect(new float[] {10,20}, 0));

        selectionBtn = (ImageButton)findViewById(R.id.selection_btn);
        selectionBtn.setOnClickListener(this);

        newBtn = (ImageButton)findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

//        saveBtn = (ImageButton)findViewById(R.id.save_btn);
//        saveBtn.setOnClickListener(this);

        TemplateManager.initialize(getApplicationContext());
    }

    /**
     *
     * @param view
     */
    public void paintClicked(View view){

        drawView.setSelectionEnabled(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        //use chosen color
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();

            drawView.setColor(color);

            imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
            currPaint.setImageDrawable(getResources().getDrawable(R.drawable.paint));
            currPaint=(ImageButton)view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if(id == R.id.action_reset_templates) {
            boolean result = TemplateManager.resetTemplates(getApplicationContext());
            Toast.makeText(getApplicationContext(), "Reset templates was: " +
                          ((result)? "successful": "unsuccessful"), Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.draw_btn){
            drawView.setSelectionEnabled(false);

        } else if(view.getId()==R.id.selection_btn){

            drawView.setSelectionEnabled(true);
            Toast.makeText(getApplicationContext(), "Selection mode enabled", Toast.LENGTH_SHORT).show();

        } else if(view.getId()==R.id.new_btn){
            //new button
            AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });

            newDialog.show();
        }

//        else if(view.getId()==R.id.save_btn){
//            //save drawing
//            AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
//            saveDialog.setTitle("Save drawing");
//            saveDialog.setMessage("Save drawing to device Gallery?");
//            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    //save drawing
//                    drawView.setDrawingCacheEnabled(true);
//                    String imgSaved = null;
//                    try {
//                        imgSaved = MediaStore.Images.Media.insertImage(
//                                getContentResolver(), drawView.getDrawingCache(),
//                                UUID.randomUUID().toString() + ".png", "drawing");
//                    } catch (Exception e) {
//                        if(e.getMessage().contains("")){
//                            fixMediaDir();
//                        }
//                    }
//
//                    if (imgSaved != null) {
//                        Toast savedToast = Toast.makeText(getApplicationContext(),
//                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
//                        savedToast.show();
//                    } else {
//                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
//                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
//                        unsavedToast.show();
//                    }
//
//                    drawView.destroyDrawingCache();
//                }
//            });
//            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.cancel();
//                }
//            });
//            saveDialog.show();
//        }
    }

    /**
     * This was added as a work around for when we're working with a device
     *  that doesn't have any pictures yet.
     */
    void fixMediaDir() {
        File sdcard = Environment.getExternalStorageDirectory();
        if (sdcard != null) {
            File mediaDir = new File(sdcard, "DCIM/Camera");
            if (!mediaDir.exists()) {
                mediaDir.mkdirs();
            }
        }
    }
}
