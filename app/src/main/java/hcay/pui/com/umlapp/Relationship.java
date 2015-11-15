package hcay.pui.com.umlapp;

import android.graphics.Path;
import android.graphics.RectF;

import java.util.ArrayList;

import hcay.pui.com.recognizer.Point;
import hcay.pui.com.recognizer.Size;

/**
 * Created by Dennis on 11/15/15.
 */
public class Relationship {
    public Size size;
    public Point point;
    public ArrayList<Path> paths;
    public ClassDiagramObject src, dst;
    public RectF rect;

    public Relationship(Size size, Point point, ArrayList<Path> paths, ClassDiagramObject src, ClassDiagramObject dst) {
        this.size = size;
        this.point = point;
        this.paths = paths;
        this.src = src;
        this.dst = dst;
        this.rect = new RectF((float) point.x, (float) point.y, (float) point.x + size.getWidth(), (float) point.y + size.getHeight());
    }
}
