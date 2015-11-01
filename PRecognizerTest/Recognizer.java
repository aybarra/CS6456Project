import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Recognizer {

    /** Number of points to use for the re-sampled path. */
    private static final int N = 32;
    private static ArrayList<Template> templates = new ArrayList<>();

    public static void initializeTemplates() {
        Gesture t = Gesture.T;
        templates.add(new Template(t, new ArrayList<>(Arrays.asList(
            new Point(30,7,1),
            new Point(103,7,1),
            new Point(66,7,2),
            new Point(66,87,2)
        ))));

        Gesture n = Gesture.N;
        templates.add(new Template(n, new ArrayList<>(Arrays.asList(
            new Point(177,92,1),new Point(177,2,1),
            new Point(182,1,2), new Point(246,95,2),
            new Point(247,87,3),new Point(247,1,3)
        ))));

        Gesture navigable = Gesture.NAVIGABLE;
        templates.add(new Template(navigable, new ArrayList<>(Arrays.asList(
            new Point(0, 50, 1), new Point(100, 50, 1), new Point(200, 50, 1),
            new Point(150, 0, 2), new Point(200, 50, 2), new Point(150, 100, 2)
        ))));

        Gesture classifier = Gesture.CLASSIFIER;
        templates.add(new Template(classifier, new ArrayList<>(Arrays.asList(
            new Point(0, 0, 1), new Point(0, 400, 1),
            new Point(0, 0, 2), new Point(200, 0, 2), new Point(200, 400, 2),
            new Point(0, 400, 3), new Point(200, 400, 3)
        ))));

        Gesture generalization = Gesture.GENERALIZATION;
        templates.add(new Template(generalization, new ArrayList<>(Arrays.asList(
            new Point(0, 50, 1), new Point(100, 50, 1), new Point(200, 50, 1),
            new Point(200, 0, 2), new Point(200, 100, 2),
            new Point(200, 0, 3), new Point(250, 50, 3), new Point(200, 100, 3)
        ))));

        Gesture unspecified = Gesture.UNSPECIFIED;
        templates.add(new Template(unspecified, new ArrayList<>(Arrays.asList(
            new Point(0, 50, 1), new Point(100, 50, 1), new Point(200, 50, 1)
        ))));

        Gesture binavgiable = Gesture.BI_NAVIGABLE;
        templates.add(new Template(binavgiable, new ArrayList<>(Arrays.asList(
            new Point(160,102,1), new Point(159,102,1), new Point(155,105,1), new Point(152,109,1), new Point(144,115,1), new Point(137,123,1), new Point(131,130,1), new Point(121,139,1), new Point(114,146,1), new Point(109,150,1), new Point(104,155,1), new Point(102,157,1), new Point(101,158,1), new Point(101,160,1), new Point(101,161,1), new Point(103,161,1), new Point(106,164,1), new Point(110,166,1), new Point(116,170,1), new Point(123,173,1), new Point(129,177,1), new Point(133,180,1), new Point(139,184,1), new Point(143,186,1), new Point(147,188,1), new Point(149,190,1), new Point(152,191,1), new Point(154,193,1), new Point(156,194,1), new Point(158,194,1), new Point(159,195,1), new Point(161,196,1), new Point(161,197,1), new Point(162,197,1), new Point(103,152,2), new Point(104,152,2), new Point(108,152,2), new Point(113,152,2), new Point(120,152,2), new Point(123,152,2), new Point(128,152,2), new Point(131,152,2), new Point(135,152,2), new Point(141,152,2), new Point(146,152,2), new Point(153,152,2), new Point(160,152,2), new Point(168,152,2), new Point(177,152,2), new Point(183,152,2), new Point(190,152,2), new Point(198,152,2), new Point(205,152,2), new Point(213,152,2), new Point(220,152,2), new Point(228,152,2), new Point(236,152,2), new Point(243,152,2), new Point(252,152,2), new Point(260,152,2), new Point(267,152,2), new Point(276,152,2), new Point(281,152,2), new Point(287,152,2), new Point(293,152,2), new Point(297,152,2), new Point(302,152,2), new Point(308,152,2), new Point(314,152,2), new Point(320,151,2), new Point(324,150,2), new Point(329,150,2), new Point(333,149,2), new Point(336,148,2), new Point(339,148,2), new Point(340,148,2), new Point(342,148,2), new Point(305,94,3), new Point(307,94,3), new Point(310,94,3), new Point(315,95,3), new Point(321,98,3), new Point(326,103,3), new Point(330,107,3), new Point(335,112,3), new Point(338,115,3), new Point(341,118,3), new Point(346,121,3), new Point(348,124,3), new Point(352,126,3), new Point(354,128,3), new Point(357,131,3), new Point(360,133,3), new Point(361,135,3), new Point(363,137,3), new Point(364,138,3), new Point(365,139,3), new Point(365,140,3), new Point(366,140,3), new Point(366,141,3), new Point(368,143,3), new Point(370,144,3), new Point(373,146,3), new Point(374,147,3), new Point(375,148,3), new Point(375,149,3), new Point(375,150,3), new Point(373,151,3), new Point(370,154,3), new Point(367,156,3), new Point(364,160,3), new Point(361,163,3), new Point(358,166,3), new Point(355,170,3), new Point(354,172,3), new Point(353,172,3), new Point(350,173,3), new Point(348,174,3), new Point(345,174,3), new Point(343,176,3), new Point(340,177,3), new Point(338,178,3), new Point(336,179,3), new Point(334,181,3), new Point(331,182,3), new Point(330,185,3), new Point(328,186,3), new Point(326,187,3), new Point(325,188,3), new Point(324,189,3), new Point(322,190,3), new Point(321,191,3), new Point(321,192,3), new Point(319,193,3), new Point(318,195,3), new Point(317,197,3), new Point(315,198,3), new Point(314,199,3), new Point(314,200,3), new Point(313,200,3)
        ))));
    }

    public static ArrayList<RecognizerResult> recognize(ArrayList<Point> points) {
        if (points.isEmpty()) {
            return new ArrayList<>();
        }

        points = normalize(points);
        double score = Double.POSITIVE_INFINITY;
        HashMap<Gesture, Double> result = new HashMap<>();

        for (Template template : templates) {
            double d = greedyCloudMatch(points, template.points);
            result.put(template.gesture, d);
            if (score > d) score = d;
        }

        if (score == Double.POSITIVE_INFINITY) {
            return new ArrayList<>();
        }

        ArrayList<RecognizerResult> convertedResult = new ArrayList<>(result.size());
        for (Map.Entry<Gesture, Double> r : result.entrySet()) {
            convertedResult.add(new RecognizerResult(r.getKey(), r.getValue()));
        }
        Collections.sort(convertedResult);

        return convertedResult;
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
