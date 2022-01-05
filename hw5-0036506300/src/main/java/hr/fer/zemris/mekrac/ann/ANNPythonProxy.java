package hr.fer.zemris.mekrac.ann;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ANNPythonProxy implements ANN {

    private static final Logger logger = Logger.getLogger("ANNPythonProxy");

    private Process pythonProcess;
    private Scanner stdIn;
    private BufferedWriter stdOut;
    private Scanner stdErr;
    private int d;
    private int c;
    private Consumer<String> trainingLogConsumer;
    private AtomicBoolean finishedTraining = new AtomicBoolean();

    public ANNPythonProxy(int d, int c, Consumer<String> trainingLogConsumer) {
        this.d = d;
        this.c = c;
        this.trainingLogConsumer = trainingLogConsumer;
    }

    @Override
    public void train(String pathToDataset, int[] hiddenLayers, int batchSize, int epochs, double lr) throws IOException {
        String architecture = d + "," +
                Arrays.stream(hiddenLayers)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining(","))
                + "," + c;
        String command = "/usr/bin/python3 src/main/python/ann.py";
        command += " " + pathToDataset;
        command += " " + architecture;
        command += " " + batchSize + " " + epochs + " " + lr;
        this.pythonProcess = Runtime.getRuntime().exec(command);
        // pythons stdOut, meaning our stdIn from him
        this.stdIn = new Scanner(pythonProcess.getInputStream());
        this.stdErr = new Scanner(pythonProcess.getErrorStream());
        // pythons stdIn, meaning our stdOut to him
        this.stdOut = new BufferedWriter(new OutputStreamWriter(pythonProcess.getOutputStream()));
        // dedicate separate thread to read trainer's stdout
        new Thread(() -> {
            String line;
            while (stdIn.hasNextLine()) {
                line = stdIn.nextLine();
                trainingLogConsumer.accept("Trainer: " + line);
                if ("DONE".equals(line)) {
                    finishedTraining.set(true);
                    break;
                }
            }
        }).start();
        // dedicate a thread to read from trainer's stderr
        new Thread(() -> {
            String line;
            while (stdErr.hasNextLine()) {
                line = stdErr.nextLine();
                logger.severe("Trainer: " + line);
            }
            // stdOut = null;
        }).start();
    }

    @Override
    public double[] predict(double[] sample) throws IOException {
        if (!finishedTraining.get()) {
            return null;
        } else if (stdOut == null) {
            return null;
        }

        String sampleData = Arrays.stream(sample).mapToObj(Double::toString).collect(Collectors.joining(","));
        logger.warning("sending '" + sampleData + "'");
        stdOut.write(sampleData + "\n");
        stdOut.flush();
        String answer = stdIn.nextLine();
        double[] probs = Arrays.stream(answer.split(",")).mapToDouble(Double::parseDouble).toArray();
        return probs;
    }

    @Override
    public void close() throws IOException {
        this.pythonProcess.destroy();
    }
}
