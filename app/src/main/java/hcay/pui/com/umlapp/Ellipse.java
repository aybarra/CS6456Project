package hcay.pui.com.umlapp;

import android.graphics.RectF;
import android.view.View;

import java.util.List;

import hcay.pui.com.recognizer.Point;

/**
 * Represents an ellipse shape, to be used to determine the bounding region of a user-drawn selection.
 *
 * @author Hyun Seo Chung
 * @author Andy Ybarra
 */
public class Ellipse {
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
