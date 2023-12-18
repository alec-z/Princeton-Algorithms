import java.util.ArrayList;
import java.util.List;


import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
public class KdTree {
    private  Node root = null;
    private int size = 0;

    public KdTree() {
        size = 0;
        root = null;
    }

    public boolean isEmpty() {
        return size == 0;
    } // is the set empty?

    public int size() {
        return size;

    } // number of points in the set

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (root == null) { 
            root = new Node(p, 0);
            size ++;
            return;
        }
        
        for (Node cur = root; cur != null; ) {
            if (p.equals(cur.point2d)) {
                return;
                //throw new IllegalArgumentException();
            }
            double pValue = p.y();
            double curValue = cur.point2d.y();
            if (cur.layer % 2 == 0) { 
                pValue = p.x();
                curValue = cur.point2d.x();
            }
            if (pValue < curValue) {
                if (cur.left == null) {
                    cur.left = new Node(p, cur.layer + 1);
                    break;
                }
                cur = cur.left;
            } else {
                if (cur.right == null) {
                    cur.right = new Node(p, cur.layer + 1);
                    break;
                } 
                cur = cur.right;
            }
        }
        size ++;
    } // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return false;
        for (Node cur = root; cur != null; ) {
            if (p.equals(cur.point2d)) return true;
            double pValue = p.y();
            double curValue = cur.point2d.y();
            if (cur.layer % 2 == 0) { 
                pValue = p.x();
                curValue = cur.point2d.x();
            }
            if (pValue < curValue) cur = cur.left;
            else cur = cur.right;
        }
        return false;
    } // does the set contain point p?

    public void draw() {
        draw(root);
    } // draw all points to standard draw

    private void draw(Node root) {
        if (root == null) return;
        root.point2d.draw();
        draw(root.left);
        draw(root.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (isEmpty()) return new ArrayList<Point2D>();
        return tryRange(rect, root);
    } // all points that are inside the rectangle (or on the boundary)

    private List<Point2D> tryRange(RectHV rect, Node root) {
        List<Point2D> res = new ArrayList<Point2D>();
        if (root == null)  return res;
        if (rect.contains(root.point2d)) res.add(root.point2d);
        double curValue = root.point2d.y();
        double rectSmall = rect.ymin();
        double rectBig = rect.ymax();
        if (root.layer % 2 == 0) {
            curValue = root.point2d.x();
            rectSmall = rect.xmin();
            rectBig = rect.xmax();
        }
        if (rectSmall < curValue) res.addAll(tryRange(rect, root.left));
        if (rectBig >= curValue) res.addAll(tryRange(rect, root.right));
        return res;
    }

    private double minDistance; 
    private Point2D nearestPoint2d;

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        minDistance = Double.POSITIVE_INFINITY;
        nearestPoint2d = null;
        tryNearest(p, root, new RectHV(0, 0, 1, 1));
        return nearestPoint2d;

    } // a nearest neighbor in the set to point p; null if the set is empty

    private void tryNearest(Point2D point, Node root, RectHV rect) {
        if (root == null) return;
        double rootDistance = point.distanceTo(root.point2d);
        if (rootDistance < minDistance) {
            minDistance = rootDistance;
            nearestPoint2d = root.point2d;
        }
        RectHV rectLeft = null;
        RectHV rectRight = null;
        if (root.layer % 2 == 0) {
            rectLeft = new RectHV(rect.xmin(), rect.ymin(), root.point2d.x(), rect.ymax());
            rectRight = new RectHV(root.point2d.x(), rect.ymin(), rect.xmax(), rect.ymax());
        } else {
            rectLeft = new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), root.point2d.y());
            rectRight = new RectHV(rect.xmin(), root.point2d.y(), rect.xmax(), rect.ymax());
        }
        double distanceToLeft = minPossibleDistance(point, rectLeft);
        double distanceToRight = minPossibleDistance(point, rectRight);
        if (distanceToLeft < distanceToRight) {
            if (distanceToLeft < minDistance) {
                tryNearest(point, root.left, rectLeft);
            }
            if (distanceToRight < minDistance) {
                tryNearest(point, root.right, rectRight);
            }
        } else {
            if (distanceToRight < minDistance) {
                tryNearest(point, root.right, rectRight);
            }
            if (distanceToLeft < minDistance) {
                tryNearest(point, root.left, rectLeft);
            }
        }
    }

    private double minPossibleDistance(Point2D point, RectHV rect) {
        double x = 0;
        double y = 0;
        if (point.x() < rect.xmin() || point.x() > rect.xmax()) {
            x = Math.min(Math.abs(point.x() - rect.xmin()), Math.abs(point.x() - rect.xmax()));
        }
        if (point.y() < rect.ymin() || point.y() > rect.ymax()) {
            y = Math.min(Math.abs(point.y() - rect.ymin()), Math.abs(point.y() - rect.ymax()));
        }
        return Math.sqrt(x*x + y*y);

    }

    private class Node {
        private Point2D point2d;
        private int layer;
        private Node left = null;
        private Node right = null;

        public Node(Point2D point2d, int layer) {
            this.point2d = point2d;
            this.layer = layer;
        }

    }

    public static void main(String[] args) {

    }

}
