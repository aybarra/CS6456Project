public class RecognizerResult implements Comparable<RecognizerResult> {

    public Gesture gesture;
    public double score;

    public RecognizerResult(Gesture gesture, double score) {
        this.gesture = gesture;
        this.score = score;
    }

    @Override
    public String toString() {
        return gesture.toString() + String.format(": %.3f", score);
    }

    @Override
    public int compareTo(RecognizerResult another) {
        return equals(another) ? 0 : (int) (score - another.score);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RecognizerResult && ((RecognizerResult) o).gesture == gesture;
    }
}
