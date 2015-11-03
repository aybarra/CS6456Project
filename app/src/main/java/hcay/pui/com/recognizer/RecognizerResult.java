package hcay.pui.com.recognizer;

import java.awt.Dimension;

public class RecognizerResult implements Comparable<RecognizerResult> {

    public Gesture gesture;
    public double score;
    public Dimension size;

    public RecognizerResult(Gesture gesture, double score, Dimension size) {
        this.gesture = gesture;
        this.score = score;
        this.size = size;
    }

    @Override
    public String toString() {
        return gesture.toString() + String.format(": %.3f", score);
    }

    @Override
    public int compareTo(RecognizerResult another) {
        return equals(another) ? 0 : (score < another.score ? -1 : (score > another.score ? 1 : 0));
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RecognizerResult && ((RecognizerResult) o).gesture == gesture;
    }
}
