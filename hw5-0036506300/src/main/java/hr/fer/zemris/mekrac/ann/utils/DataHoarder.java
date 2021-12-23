package hr.fer.zemris.mekrac.ann.utils;

import hr.fer.zemris.mekrac.ann.gui.Gesture;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DataHoarder implements Consumer<Gesture> {

    public static final double DELTA = 0.01;
    private static final Logger logger = Logger.getLogger("DataHoarder");

    private int m;
    private int k;
    private List<double[]> dataset = new ArrayList<>();

    public DataHoarder(int m, int k) {
        this.m = m;
        this.k = k;
    }

    @Override
    public void accept(Gesture gesture) {
        logger.warning("Adding gesture for label " + gesture.getLabel());
        dataset.add(compress(gesture));
    }

    public void save(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (double[] sample : dataset) {
            int i;
            for (i = 0; i < sample.length - 1; i++) {
                sb.append(sample[i]).append(',');
            }
            sb.append(sample[i]).append('\n');
        }
        Files.writeString(Paths.get(path), sb.toString());
    }

    public void load(String path) throws IOException {
        dataset = Files.readAllLines(Paths.get(path))
                .stream()
                .map(DataHoarder::parseArray)
                .collect(Collectors.toList());
    }

    public List<double[]> getDataset() {
        return dataset;
    }

    public double[] compress(Gesture gesture) {
        List<Point> points = gesture.getPoints();
        // for translating
        double x0 = points.get(0).x;
        double y0 = points.get(0).y;
        // for scaling
        double maxX = points.stream().mapToDouble(p -> Math.abs(p.x)).max().orElseThrow();
        double maxY = points.stream().mapToDouble(p -> Math.abs(p.y)).max().orElseThrow();
        double max = Math.max(maxX, maxY);
        // transform the points
        List<DoublePoint> transformed = points.stream()
                .map(p -> {
                    double x = (((double) p.x) - x0) / max;
                    double y = (((double) p.y) - y0) / max;
                    return new DoublePoint(x, y);
                }).collect(Collectors.toList());
        double gestureLength = 0.0;
        // distances from p0
        List<Double> distances = new ArrayList<>();
        DoublePoint p0 = transformed.get(0), p1;
        for (int i = 0; i < transformed.size(); i++) {
            p1 = transformed.get(i);
            gestureLength += Point2D.distance(p0.x, p0.y, p1.x, p1.y);
            distances.add(gestureLength);
            p0 = p1;
        }

        double[] sample = new double[2*m + k];
        p0 = transformed.get(0);
        sample[0] = p0.x;
        sample[1] = p0.y;
        double distance = 0.0;
        // desired distance between two representative points
        double desiredDelta = gestureLength / (m - 1);
        for (int k = 1; k < m; k++) {
            double idealDistance = k * desiredDelta;
            DoublePoint pk = findPointAtDistance(idealDistance, transformed, distances);
            sample[2*k] = pk.x;
            sample[2*k+1] = pk.y;
        }
        // OH encode the label
        sample[2*m + gesture.getLabel()] = 1.0;
        return sample;
    }

    public double[] compressOnlyGesture(Gesture g) {
        double[] sample = compress(g);
        return Arrays.copyOf(sample, 2*m);
    }

    private DoublePoint findPointAtDistance(double distance, List<DoublePoint> points, List<Double> distances) {
        int firstLowerIndex = 0, firstGreaterIndex = 1;
        for (int i = 0; i < distances.size(); i++) {
            double d = distances.get(i);
            if (Math.abs(d - distance) < DELTA) {
                return points.get(i);
            } else if (d < distance) {
                firstLowerIndex = i;
            } else if (d > distance) {
                firstGreaterIndex = i;
                break;
            } else {
                // should never we thrown, only for debugging
                throw new RuntimeException("This should not have happened!");
            }
        }
        // linear interpolation between the first lower and first greater point
        double d0 = distances.get(firstLowerIndex);
        double d1 = distances.get(firstGreaterIndex);
        double alpha = (distance - d0) / (distance - d1);
        DoublePoint p0 = points.get(firstLowerIndex);
        DoublePoint p1 = points.get(firstGreaterIndex);
        double x = (1 - alpha) * p0.x + alpha * p1.x;
        double y = (1 - alpha) * p0.y + alpha * p1.y;

        return new DoublePoint(x, y);
    }

    public static double[] parseArray(String line) {
        String[] parts = line.split(",");
        double[] sample = new double[parts.length];
        for (int i = 0; i < sample.length; i++) {
            sample[i] = Double.parseDouble(parts[i]);
        }
        return sample;
    }

    public static class DoublePoint {
        double x;
        double y;

        public DoublePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public DoublePoint() {
        }
    }
}
