package hcay.pui.com.recognizer;

import java.util.ArrayList;

public class Template {

    public Gesture gesture;
    public ArrayList<Point> points;
    public boolean normalized = false;

    public Template(Gesture gesture, ArrayList<Point> points) {
        this.gesture = gesture;
        this.points = Recognizer.normalize(points);
        this.normalized = true;
    }

    public void normalizePoints() {
        if (!normalized) {
            points = Recognizer.normalize(points);
            normalized = true;
        }
    }

    @Override
    public String toString() {
    	return "{" + gesture.name + ":" + points.toString() + "}";
    }

}