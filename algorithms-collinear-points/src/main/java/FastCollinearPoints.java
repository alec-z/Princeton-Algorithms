import java.util.Arrays;
import java.util.Comparator;


public class FastCollinearPoints {
    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument to BruteCollinearPoints contructor is null");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Argument to BruteCollinearPoints contructor is null");
            }
        }
        Point[] pointsCloned = points.clone();
        this.points = sort(pointsCloned, null, (a, b) -> a.compareTo(b), 0, pointsCloned.length - 1);
        for (int i = 1; i < points.length; i++) {
            if (this.points[i].compareTo(this.points[i - 1]) == 0) {
                throw new IllegalArgumentException("Argument to BruteCollinearPoints contructor is duplicated");
            }
        }
        caculateSegments();
    }

    private final Point[] points;

    private LineSegment[] segments = new LineSegment[8];
    private int numberOfSegments = 0;
    private int capacityOfSegments = 8;

    
    

    public int numberOfSegments() {
        return numberOfSegments;
    }

    private void appendSegment(LineSegment segment) {
        if (numberOfSegments >= capacityOfSegments) {
            capacityOfSegments += capacityOfSegments;
            segments = Arrays.copyOf(segments, capacityOfSegments);
        }
        segments[numberOfSegments++] = segment;
    }
    public LineSegment[] segments() {
        return Arrays.copyOf(segments, numberOfSegments);
    }

    private void caculateSegments() {

        final int N = this.points.length;
        Point[] pointsForSlope = Arrays.copyOf(points, points.length);
        Point[] auxPointsForSlope = Arrays.copyOf(points, points.length);
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                pointsForSlope[j] = points[j];
                auxPointsForSlope[j] = pointsForSlope[j];
            }

            Comparator<Point> comparator = points[i].slopeOrder();
            Point[] pointsOrdered = sort(pointsForSlope, auxPointsForSlope, comparator, 0, N - 1);
            int j = 0;
            while (j < N) {
                double currentSlope = pointsOrdered[j].slopeTo(points[i]);
                int len = 1;
                j++;
                while (j < N && pointsOrdered[j].slopeTo(points[i]) == currentSlope) { 
                    len++;
                    j++;
                }
                
                if (len >= 3) {
                    if (len == 3) {
                        if (points[i].compareTo(pointsOrdered[j - 3]) < 0) {
                            appendSegment(new LineSegment(points[i], pointsOrdered[j - 1]));
                        }
                    } else {
                        if (points[i].compareTo(pointsOrdered[j - 3]) < 0 && points[i].compareTo(pointsOrdered[j - 4]) > 0) {
                            appendSegment(new LineSegment(pointsOrdered[j - len], pointsOrdered[j - 1]));
                        }
                    }
                }
            }
        }   
    }

    private static Point[] sort(Point[] a, Point[] aux, Comparator<Point> comparator, int l, int h) {
        if (aux == null) {
            aux = Arrays.copyOf(a, a.length);
        }
        final int N = h - l + 1;
        if (N >= 2) {
            int lo = l;
            for (; lo <= h - 1; lo += 2) {
                if (comparator.compare(a[lo], a[lo + 1]) <= 0) {
                    aux[lo] = a[lo];
                    aux[lo + 1] = a[lo + 1];
                } else {
                    aux[lo] = a[lo + 1]; 
                    aux[lo + 1] = a[lo];
                }
            }
            if (lo == h) {
                aux[lo] = a[lo];
            }
            Point[] tmp = a;
            a = aux;
            aux = tmp;
        }
    
        for (int size = 2; size <= N; size += size) {
            // assert a is partial ordered
            Point[] tmp = a;
            a = aux;
            aux = tmp;
            for (int lo = l; lo <= h;) {
                int mid = Math.min(lo + size - 1, h), hi = Math.min(mid + size, h);
                merge(a, aux, lo, mid, hi, comparator);
                lo = hi + 1;    
            }
        }
        return a;
    }


    private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi, Comparator<Point> comparator) {
        int i = lo, j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (comparator.compare(aux[i], aux[j]) > 0) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }
    
}
