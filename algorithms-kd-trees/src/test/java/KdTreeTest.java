import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTreeTest {
    private KdTree kdTree = null;
    @Before
    public void init() {
        kdTree = new KdTree();
        kdTree.insert(new Point2D(1, 1));
        kdTree.insert(new Point2D(2.1, 2.1));

    }
    @Test
    public void insertContainTest() {        
        assertEquals(2, kdTree.size());
        assertTrue(kdTree.contains(new Point2D(2.1, 2.1)));
        assertFalse(kdTree.contains(new Point2D(2, 2)));
    }

    @Test
    public void rangeTest() {
        RectHV rect = new RectHV(0, 0, 1.1, 1.1);
        List<Point2D> res  = (List<Point2D>)kdTree.range(rect);
        assertTrue(res.contains(new Point2D(1, 1)));
        assertFalse(res.contains(new Point2D(2.1, 2.1)));
    }

    @Test
    public void nearestTest() {
        Point2D p = new Point2D(1.1, 1.1);
        Point2D res = kdTree.nearest(p);
        assertTrue(res.equals(new Point2D(1, 1)));
    }

    @Test
    public void nearestPerformanceTest() {
        KdTree kt2 = new KdTree();
        kt2.insert(new Point2D(0.4375, 0.75)); //A
        kt2.insert(new Point2D(1.0, 0.375)); //B
        kt2.insert(new Point2D(0.0, 0.0)); //C
        kt2.insert(new Point2D(0.0625, 0.1875)); //D
        kt2.insert(new Point2D(0.8125, 0.6875)); //E
        kt2.insert(new Point2D(0.125, 0.0625)); //F
        kt2.insert(new Point2D(0.75, 0.8125)); //G
        kt2.insert(new Point2D(0.625, 0.5625)); //H
        kt2.insert(new Point2D(0.375, 0.5)); // I 
        kt2.insert(new Point2D(0.25, 0.125));
        Point2D res = kt2.nearest(new Point2D(0.9375, 0.25));
    }

    @Test
    public void PoinstSETTest() {
        PointSET ps = new PointSET();
        assertEquals(0, ps.size());
        ps.insert(new Point2D(1.0, 1.0));
        ps.insert(new Point2D(0, 0));
        assertEquals(new Point2D(0, 0), ps.nearest(new Point2D(0, 1)));
    }
    
}
