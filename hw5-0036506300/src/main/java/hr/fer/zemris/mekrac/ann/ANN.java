package hr.fer.zemris.mekrac.ann;

import java.io.Closeable;
import java.io.IOException;

public interface ANN extends Closeable {

    void train(String pathToDataset, int[] architecture, int batchSize, int epochs, double lr) throws IOException;

    double[] predict(double[] sample) throws IOException;
}
