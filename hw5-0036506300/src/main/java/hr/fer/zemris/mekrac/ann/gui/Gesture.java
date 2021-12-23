package hr.fer.zemris.mekrac.ann.gui;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Gesture {

    private List<Point> points = new ArrayList<>(128);
    private int label;

    public Gesture(int label) {
        this.label = label;
    }

    public void add(Point point) {
        points.add(point);
    }

    public void add(int x, int y) {
        this.add(new Point(x, y));
    }

    public List<Point> getPoints() {
        return points;
    }

    public int getLabel() {
        return label;
    }

    public void paintComponent(Graphics2D g2d) {
        Point p0 = points.get(0);
        Point p1;
        for (int i = 1; i < points.size(); i++) {
            p1 = points.get(i);
            g2d.drawLine(p0.x, p0.y, p1.x, p1.y);
            p0 = p1;
        }
    }

    @Override
    public String toString() {
        return "gesture " + label + ": " + points.size() + " points";
    }
}
