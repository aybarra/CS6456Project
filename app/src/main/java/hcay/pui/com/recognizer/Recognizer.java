package hcay.pui.com.recognizer;

import java.util.ArrayList;

public class Recognizer {

    public ArrayList<RecognizerResult> recognize(ArrayList<Point> points) {
        points = adjust(points);

        return null;
    }

    private ArrayList<Point> resample(ArrayList<Point> points) {
        return null;
    }

    private ArrayList<Point> scale(ArrayList<Point> points) {
        return null;
    }

    private ArrayList<Point> translate(ArrayList<Point> points) {
        return null;
    }

    private ArrayList<Point> adjust(ArrayList<Point> points) {
        return translate(scale(resample(points)));
    }

}
