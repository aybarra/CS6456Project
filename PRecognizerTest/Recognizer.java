import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import java.awt.Dimension;

public class Recognizer {

    /** Number of points to use for the re-sampled path. */
    private static final int N = 32;

    public static ArrayList<RecognizerResult> recognize(ArrayList<Point> points) {
        if (points.isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<Point> normalizedPoints = normalize(points);
        double score = Double.POSITIVE_INFINITY;
        HashMap<Gesture, Double> result = new HashMap<>();

        for (Template template : TemplateManager.templates) {
            double d = greedyCloudMatch(normalizedPoints, template.points);
            result.put(template.gesture, d);
            if (d < score) score = d;
        }

        if (score == Double.POSITIVE_INFINITY) {
            return new ArrayList<>();
        }

        Dimension drawingSize = getSize(points);
        ArrayList<RecognizerResult> convertedResult = new ArrayList<>(result.size());
        for (Map.Entry<Gesture, Double> r : result.entrySet()) {
            if (r.getValue() - score < 0.2) {
                convertedResult.add(new RecognizerResult(r.getKey(), r.getValue(), drawingSize));
            }
        }
        Collections.sort(convertedResult);

        return convertedResult;
    }

    private static Dimension getSize(ArrayList<Point> points) {
        double minX, minY, maxX, maxY;
        minX = minY = Double.POSITIVE_INFINITY;
        maxX = maxY = Double.NEGATIVE_INFINITY;

        for (Point p : points) {
            minX = Math.min(minX, p.x);
            maxX = Math.max(maxX, p.x);
            minY = Math.min(minY, p.y);
            maxY = Math.max(maxY, p.y);
        }

        return new Dimension((int) (maxX - minX + 1), (int) (maxY - minY + 1));
    }

    private static double greedyCloudMatch(ArrayList<Point> points, ArrayList<Point> templatePoints) {
        double e = 0.50;
        double step = Math.floor(Math.pow(N, 1 - e));
        double min = Double.POSITIVE_INFINITY;

        for (int i = 0; i < N; i += step) {
            double d1 = cloudDistance(points, templatePoints, i);
            double d2 = cloudDistance(templatePoints, points, i);
            min = Math.min(min, Math.min(d1, d2));
        }

        return min;
    }

    private static double cloudDistance(ArrayList<Point> points, ArrayList<Point> templatePoints, int start) {
        if (points.size() != templatePoints.size()) {
            return Double.POSITIVE_INFINITY;
        }

        boolean[] matched = new boolean[N];
        double sum = 0;
        int i = start;
        do {
            int index = -1;
            double min = Double.POSITIVE_INFINITY;
            for (int j = 0; j < matched.length; j++) {
                if (!matched[j]) {
                    double d = getPointDistance(points.get(i), templatePoints.get(j));
                    if (d < min) {
                        min = d;
                        index = j;
                    }
                }
            }
            matched[index] = true;
            double weight = 1 - ((i - start + N) % N) / (double) N;
            sum += weight * min;
            i = (i + 1) % N;
        } while (i != start);

        return sum;
    }

    private static ArrayList<Point> resample(ArrayList<Point> points) {
        double incrementLength = getTotalPathDistance(points) / (N - 1);

        ArrayList<Point> newPoints = new ArrayList<>(N);
        newPoints.add(points.get(0));

        double currentDistance = 0;

        for (int i = 1; i < points.size(); i++) {
            Point prev = points.get(i - 1);
            Point curr = points.get(i);
            if (prev.strokeID == curr.strokeID) {
                double d = getPointDistance(prev, curr);
                if ((currentDistance + d) >= incrementLength) {
                    double qx = prev.x + ((incrementLength - currentDistance) / d) * (curr.x - prev.x);
                    double qy = prev.y + ((incrementLength - currentDistance) / d) * (curr.y - prev.y);
                    Point q = new Point(qx, qy, curr.strokeID);
                    newPoints.add(q);
                    points.add(i, q);
                    currentDistance = 0;
                } else {
                    currentDistance += d;
                }
            }
        }

        if (newPoints.size() < N) {
            newPoints.add(points.get(points.size() - 1));
        }

        return newPoints;
    }

    private static ArrayList<Point> scale(ArrayList<Point> points) {
        double minX, minY, maxX, maxY;
        minX = minY = Double.POSITIVE_INFINITY;
        maxX = maxY = 0;

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            minX = Math.min(minX, point.x);
            minY = Math.min(minY, point.y);
            maxX = Math.max(maxX, point.x);
            maxY = Math.max(maxY, point.y);
        }

        double scale = Math.max(maxX - minX, maxY - minY);

        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            point.x = (point.x - minX) / scale;
            point.y = (point.y - minY) / scale;
        }

        return points;
    }

    private static ArrayList<Point> translate(ArrayList<Point> points) {
        Point c = getCentroid(points);

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            p.x -= c.x;
            p.y -= c.y;
        }

        return points;
    }

    private static Point getCentroid(ArrayList<Point> points) {
        Point c = new Point(0, 0, -1);

        for (int i = 0; i < points.size(); i++) {
            Point p = points.get(i);
            c.x += p.x;
            c.y += p.y;
        }

        c.x /= N;
        c.y /= N;

        return c;
    }

    private static ArrayList<Point> rotateToZero(ArrayList<Point> points) {
        Point c = getCentroid(points);
        double t = Math.atan2(c.y - points.get(0).y, c.x - points.get(0).x);
        return rotateBy(points, -t);
    }

    private static ArrayList<Point> rotateBy(ArrayList<Point> points, double t) {
        ArrayList<Point> newPoints = new ArrayList<>(points.size());
        Point c = getCentroid(points);
        for (Point point : points) {
            double qx = (point.x - c.x) * Math.cos(t) - (point.y - c.y) * Math.sin(t) + c.x;
            double qy = (point.x - c.x) * Math.sin(t) + (point.y - c.y) * Math.cos(t) + c.y;
            newPoints.add(new Point(qx, qy, point.strokeID));
        }
        return newPoints;
    }

    protected static ArrayList<Point> normalize(ArrayList<Point> points) {
        points = resample(points);
        points = rotateToZero(points);
        return translate(scale(points));
    }

    private static double getPointDistance(Point p1, Point p2) {
        double dx = p2.x - p1.x;
        double dy = p2.y - p1.y;
        return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    }

    private static double getTotalPathDistance(ArrayList<Point> points) {
        double distance = 0;
        for (int i = 1; i < points.size(); i++) {
            Point prev = points.get(i - 1);
            Point curr = points.get(i);
            if (prev.strokeID == curr.strokeID) {
                // add the distances between points for the same stroke
                distance += getPointDistance(prev, curr);
            }
        }
        return distance;
    }

}
