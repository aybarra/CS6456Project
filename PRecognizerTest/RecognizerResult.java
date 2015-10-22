package hcay.pui.com.recognizer;

public class RecognizerResult {

    public Gesture gesture;
    public double score;

    public RecognizerResult(Gesture gesture, double score) {
        this.gesture = gesture;
        this.score = score;
    }

}
