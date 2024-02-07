/******************************************************************************
 *
 *  @author Alec Zheng 2014-01-30 15:00
 *
 ******************************************************************************/
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    // create a seam carver object based on the given picture

    private Picture pic = null;
    private double[][] energies;
 

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        pic = new Picture(picture);
        energies = new double[width()][height()];
        for (int col = 0; col < pic.width(); col++) {
            for (int row = 0; row < pic.height(); row++) {
                energies[col][row] = energy_p(col, row);
            }
        }
    }

    // current picture
    public Picture picture() {
        return new Picture(pic);
    }

    // width of current picture
    public int width() {
        return pic.width();
    }

    // height of current picture
    public int height() {
        return pic.height();
    }

    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1) throw new IllegalArgumentException();
        if (y < 0 || y > height() - 1) throw new IllegalArgumentException();
        return energies[x][y];
    }

    // energy of pixel at column x and row y
    private double energy_p(int x, int y) {
        Color x_m_1, x_p_1, y_m_1, y_p_1;
        if (x - 1 < 0) {
            return 1000;
        }
        else {
            x_m_1 = pic.get(x - 1, y);
        }
        if (x + 1 > width() - 1) {
            return 1000;
        }
        else {
            x_p_1 = pic.get(x + 1, y);
        }

        if (y - 1 < 0) {
            return 1000;
        } else {
            y_m_1 = pic.get(x, y - 1);
        }

        if (y + 1 > height() - 1) {
            return 1000;
        } else {
            y_p_1 = pic.get(x, y + 1);
        }

        double gradient_x = Math.pow(x_p_1.getRed() - x_m_1.getRed(), 2) + Math.pow(x_p_1.getGreen() - x_m_1.getGreen(), 2) + Math.pow(x_p_1.getBlue() - x_m_1.getBlue(), 2);
        double graident_y = Math.pow(y_p_1.getRed() - y_m_1.getRed(), 2) + Math.pow(y_p_1.getGreen() - y_m_1.getGreen(), 2) + Math.pow(y_p_1.getBlue() - y_m_1.getBlue(), 2);
        return Math.sqrt(gradient_x + graident_y);

    }

    private Iterable<Point> vertical_adj(int col, int row) {
        List<Point> result = new ArrayList<>(3);
        int newRow = row + 1;
        if (newRow  <= height() - 1) {
            if (col - 1 >= 0) result.add(new Point(col - 1, newRow));
            result.add(new Point(col, newRow));
            if (col + 1 <= width() - 1) result.add(new Point(col + 1, newRow));
        }
        return result;
    }

    private Iterable<Point> horizontal_adj(int col, int row) {
        List<Point> result = new ArrayList<>(3);
        int newCol = col + 1;
        if (newCol <= width() - 1) {
            if (row - 1 >= 0) result.add(new Point(newCol, row - 1));
            result.add(new Point(newCol, row));
            if (row + 1 <= height() - 1) result.add(new Point(newCol, row + 1));
        }
        return result;
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] distTo = new double[width()][height()];
        Point[][] vertTo = new Point[width()][height()];
        resetAll(distTo, vertTo);
        int[] result = new int[width()];
        for (int row = 0; row < height(); row ++) {
            distTo[0][row] = energy(0, row);
        }
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row ++) {
                horizontal_relax(col, row, distTo, vertTo);
            }
        }

        double minDist = Double.POSITIVE_INFINITY;
        int rowPos = -1;
        for (int row = 0; row < height(); row ++) {
            if (distTo[width() - 1][row] < minDist) {
                minDist = distTo[width() - 1][row];
                rowPos = row;
            }
        }

        Point cur = new Point(width() - 1, rowPos);
        for (int col = width() - 1; col >= 0; col--) {
            result[col] = cur.row;
            cur = vertTo[cur.col][cur.row];
        }
        return result;
    }

    private void resetAll(double[][] distTo, Point[][] vertTo) {
        for (int i = 0; i < distTo.length; i++) Arrays.fill(distTo[i], Double.POSITIVE_INFINITY);
        for (int i = 0; i < vertTo.length; i++) Arrays.fill(vertTo[i], null);
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        double[][] distTo = new double[width()][height()];
        Point[][] vertTo = new Point[width()][height()];
        resetAll(distTo, vertTo);
        int[] result = new int[height()];
        for (int col = 0; col < width(); col++) {
            distTo[col][0] = energy(col, 0);
        }
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                vertical_relax(col, row, distTo, vertTo);
            }
        }

        double minDist = Double.POSITIVE_INFINITY;
        int colPos = -1;
        for (int col = 0; col < width(); col++) {
            if (distTo[col][height() - 1] < minDist) {
                minDist = distTo[col][height() - 1];
                colPos = col;
            }
        }

        Point cur = new Point(colPos, height() - 1);
        for (int row = height() - 1; row >= 0; row--) {
            result[row] = cur.col;
            cur = vertTo[cur.col][cur.row];
        }
        return result;
    }

    private void vertical_relax(int col, int row, double[][] distTo, Point[][] vertTo) {
        for(Point adj: vertical_adj(col, row)) {
            double newDist = distTo[col][row] + energy(adj.col, adj.row);
            if (newDist < distTo[adj.col][adj.row]) {
                distTo[adj.col][adj.row] = newDist;
                vertTo[adj.col][adj.row] = new Point(col, row);
            }
        }
    }

    private void horizontal_relax(int col, int row,  double[][] distTo, Point[][] vertTo) {
        for(Point adj: horizontal_adj(col, row)) {
            double newDist = distTo[col][row] + energy(adj.col, adj.row);
            if (newDist < distTo[adj.col][adj.row]) {
                distTo[adj.col][adj.row] = newDist;
                vertTo[adj.col][adj.row] = new Point(col, row);
            }
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != width()) throw new IllegalArgumentException();
        if (height() <= 1) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > height() - 1) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        Picture newPic = new Picture(width(), height() - 1);
        double[][] newEnergies = new double[width()][height() - 1];

        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height() - 1; row ++) {
                if (row < seam[col]) {
                    newPic.set(col, row, pic.get(col, row));
                    newEnergies[col][row] = energies[col][row];
                } else {
                    newPic.set(col, row, pic.get(col, row + 1));
                    newEnergies[col][row] = energies[col][row + 1];
                }
            }
        }

        pic = newPic;
        for (int col = 0; col < width(); col++) {
            int newRow = seam[col];
            if (newRow > 0) {
                newEnergies[col][newRow - 1] = energy_p(col, newRow - 1);
            }
            if (newRow < height()) {
                newEnergies[col][newRow] = energy_p(col, newRow);
            }
        }

       
        energies = newEnergies;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException();
        if (seam.length != height()) throw new IllegalArgumentException();
        if (width() <= 1) throw new IllegalArgumentException();
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > width() - 1) {
                throw new IllegalArgumentException();
            }
        }
        for (int i = 0; i < seam.length - 1; i++) {
            if (Math.abs(seam[i] - seam[i + 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }
        
        // new picture and energies;
        Picture newPic = new Picture(width() - 1, height());
        double[][] newEnergies = new double[width() - 1][height()];
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width() - 1; col++) {
                if (col < seam[row]) {
                    newPic.set(col, row, pic.get(col, row));
                    newEnergies[col][row] = energies[col][row];
                } else {
                    newPic.set(col, row, pic.get(col + 1, row));
                    newEnergies[col][row] = energies[col + 1][row];
                }
            }
        }
        pic = newPic;
        
        

        for (int row = 0; row < height(); row++) {
            int newCol = seam[row];
            if (newCol > 0) {
                newEnergies[newCol - 1][row] = energy_p(newCol - 1, row);
            }
            if (newCol < width()) {
                newEnergies[newCol][row] = energy_p(newCol, row);
            }
        }
        

        energies = newEnergies;
    }

    // unit testing (optional)
    public static void main(String[] args) {

    }

    private class Point {
        private int col;
        private int row;
        public Point(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }

}
