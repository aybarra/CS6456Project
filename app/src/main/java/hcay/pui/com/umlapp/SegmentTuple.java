package hcay.pui.com.umlapp;

import android.graphics.Path;

/**
 * Created by andrasta on 11/15/15.
 */
public class SegmentTuple {
    Path segPath;
    android.graphics.Point lastPoint;
    public SegmentTuple(Path path, android.graphics.Point point){
        segPath = path;
        lastPoint = point;
    }
}