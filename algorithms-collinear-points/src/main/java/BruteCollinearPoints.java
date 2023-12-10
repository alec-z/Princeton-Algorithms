import java.util.Arrays;


public class BruteCollinearPoints {
    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException("Argument to BruteCollinearPoints contructor is null");
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException("Argument to BruteCollinearPoints contructor is null");
            }
        }

        this.points = points.clone();
        Arrays.sort(this.points);
        for (int i = 1; i < this.points.length; i++) {
            if (this.points[i].compareTo(this.points[ i - 1]) == 0) {
                throw new IllegalArgumentException("Argument to BruteCollinearPoints contructor is duplicated");
            }
        }
        caculateSegments();
    }

    private final Point[] points;

    private int numberOfSegments = 0;
    private LineSegment segments[] = new LineSegment[8];
    private int capacityOfSegments = 8;

    public int numberOfSegments() {
        return numberOfSegments;
    }

    private void appendSegments(LineSegment segment) {
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
        int l = points.length;
        for (int i = 0; i < l - 3; i++) {
            for (int j = i + 1; j < l - 2; j++) {
                double s0 = points[i].slopeTo(points[j]); 
                for (int k = j + 1; k < l - 1; k++) {
                    double s1 = points[i].slopeTo(points[k]);
                    if (s1 == s0) {
                        for (int kk = k + 1; kk < l; kk++) {
                            double s2 = points[i].slopeTo(points[kk]);
                            if (s2 == s0) {
                                appendSegments(new LineSegment(points[i], points[kk]));
                            }
                        }
                    }
                }
            }
        }
    }
}