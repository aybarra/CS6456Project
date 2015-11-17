package hcay.pui.com.umlapp;

import android.graphics.Path;

/**
 * Tuple to hold information about a path and its last point.
 *
 * @author Hyun Seo Chung
 * @author Andy Ybarra
 */
public class SegmentTuple {
    Path segPath;
    android.graphics.Point lastPoint;

    public SegmentTuple(Path path, android.graphics.Point point){
        segPath = path;
        lastPoint = point;
    }
}