public class Point {

    public double x, y;
    public int strokeID;

    public Point(double x, double y, int strokeID) {
        this.x = x;
        this.y = y;
        this.strokeID = strokeID;
    }

    @Override
    public String toString() {
    	return String.format("{x=%.3f,y=%.3f,id=%d}", x, y, strokeID);
    }

}
