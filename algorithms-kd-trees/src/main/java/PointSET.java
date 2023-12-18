import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> rbSet;

    public PointSET() {
        rbSet = new SET<Point2D>();
    }

    public boolean isEmpty() {
        return rbSet.isEmpty();
    } // is the set empty?

    public int size() {
        return rbSet.size();

    } // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        rbSet.add(p);
    } // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return rbSet.contains(p);
    } // does the set contain point p?

    public void draw() {
        for (Point2D p: rbSet) {
            p.draw();
        }
    } // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        SET<Point2D> res = new SET<Point2D>();
        for (Point2D p: rbSet) {
            if (rect.contains(p)) {
                res.add(p);
            }
        }
        return res;
        
    } // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        double nearestDistance = Double.POSITIVE_INFINITY;
        Point2D res = null;
        for (Point2D sP: rbSet) {
            double distance = sP.distanceTo(p);
            if (distance < nearestDistance) {
                res = sP;
                nearestDistance = distance;
            }
        }
        return res;

    } // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {

    }

}
