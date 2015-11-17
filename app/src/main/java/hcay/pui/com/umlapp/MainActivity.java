package hcay.pui.com.umlapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import hcay.pui.com.recognizer.TemplateManager;

/**
 * MainActivity for the application.
 *
 * @author Hyun Seo Chung
 * @author Andy Ybarra
 */
public class MainActivity extends Activity implements View.OnClickListener {

    private DrawingView drawView;
    public static MenuItem undoItem, redoItem, deleteItem, viewItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawView = (DrawingView)findViewById(R.id.drawing);

        float smallBrush = getResources().getInteger(R.integer.small_size);

        drawView.setBrushSize(smallBrush);
        drawView.setColor("#FF000000");

        findViewById(R.id.gestureModeButton).setSelected(true);

        TemplateManager.initialize(getApplicationContext());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        undoItem = menu.findItem(R.id.action_undo);
        redoItem = menu.findItem(R.id.action_redo);
        deleteItem = menu.findItem(R.id.action_delete);
        viewItem = menu.findItem(R.id.action_view);
        undoItem.setEnabled(false);
        redoItem.setEnabled(false);
        deleteItem.setEnabled(false);
        undoItem.getIcon().setAlpha(50);
        redoItem.getIcon().setAlpha(50);
        deleteItem.getIcon().setAlpha(50);
        viewItem.getIcon().setAlpha(100);
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
        } else if (id == R.id.action_reset_templates) {
            boolean result = TemplateManager.resetTemplates(getApplicationContext());
            Toast.makeText(getApplicationContext(), "Reset templates was: " +
                          ((result)? "successful": "unsuccessful"), Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_undo) {
            drawView.undoOrRedo(true);
        } else if (id == R.id.action_redo) {
            drawView.undoOrRedo(false);
        } else if (id == R.id.action_delete) {
            drawView.deleteSelected();
        } else if (id == R.id.action_view) {
            drawView.switchMode();
        }

        return super.onOptionsItemSelected(item);
    }

    public static void updateDeleteItem(boolean enable) {
        updateItem(deleteItem, enable);
    }

    private static void updateItem(MenuItem item, boolean enable) {
        item.setEnabled(enable);
        deleteItem.getIcon().setAlpha(enable ? 255 : 50);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.gestureModeButton) {
            if (!view.isSelected()) {
                view.setSelected(true);
                findViewById(R.id.selectionModeButton).setSelected(false);
                drawView.setSelectionEnabled(false);
            }
        } else if (view.getId() == R.id.selectionModeButton) {
            if (!view.isSelected()) {
                view.setSelected(true);
                findViewById(R.id.gestureModeButton).setSelected(false);
                drawView.setSelectionEnabled(true);
            }
            Toast.makeText(getApplicationContext(), "Selection mode enabled", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.clear_button) {
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
    }

}
