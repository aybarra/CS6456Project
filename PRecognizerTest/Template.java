import java.util.ArrayList;

public class Template {

    public Gesture gesture;
    public ArrayList<Point> points;

    public Template(Gesture gesture, ArrayList<Point> points) {
        this.gesture = gesture;
        this.points = Recognizer.normalize(points);
    }

}