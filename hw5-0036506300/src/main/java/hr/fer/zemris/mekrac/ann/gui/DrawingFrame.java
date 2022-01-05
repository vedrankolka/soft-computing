package hr.fer.zemris.mekrac.ann.gui;

import hr.fer.zemris.mekrac.ann.ANN;
import hr.fer.zemris.mekrac.ann.ANNPythonProxy;
import hr.fer.zemris.mekrac.ann.utils.DataHoarder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class DrawingFrame extends JFrame {

    static {
        try {
            LogManager.getLogManager().readConfiguration(Files.newInputStream(Paths.get("./src/main/resources/logger.properties")));
        } catch (SecurityException | IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    private static final Logger logger = Logger.getLogger(DrawingFrame.class.getName());

    public static final String TITLE = "ANN training application";
    public static String DATASET_PATH = "src/main/resources";
    public static int M = 10;
    public static int K = 5;
    // other stuff
    private DataHoarder dataHoarder = new DataHoarder(M, K);
    private ANN ann;
    // GUI draw
    private JLabel label = new JLabel("Good evening.");
    private JTextField filenameTextField = new JTextField("dataset.csv");
    private JTextField labelTextField = new JTextField("0");
    private List samplesList = new List();
    private Canvas canvas = new Canvas(g -> {
        samplesList.add(g.toString());
        dataHoarder.accept(g);
    });
    // GUI train
    private JLabel architectureLabel = new JLabel("hidden layers:");
    private JTextField architectureField = new JTextField();
    private JLabel batchSizeLabel = new JLabel("batch size:");
    private JTextField batchSizeField = new JTextField("16");
    private List trainingLogList = new List();
    private JLabel epochsLabel = new JLabel("epochs:");
    private JTextField epochsField = new JTextField("10000");
    private JLabel lrLabel = new JLabel("learning rate:");
    private JTextField lrField = new JTextField("0.01");
    private JLabel trainlTabLabel = new JLabel("les gooo");
    // GUI test
    private JLabel answerLabel = new JLabel("the answer goes here");
    private JLabel decisionLabel = new JLabel("decision");
    private JLabel testTabLabel = new JLabel("draw some samples");
    private Canvas testCanvas = new Canvas(g -> {
        double[] sample = dataHoarder.compressOnlyGesture(g);
        classify(sample);
    });

    // actions
    private Action save = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            save();
        }
    };
    private Action load = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            load();
        }
    };
    private Action train = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            train();
        }
    };

    public DrawingFrame() throws HeadlessException {
        setSize(new Dimension(600, 400));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setFocusable(true);
        setTitle(TITLE);

        this.ann = new ANNPythonProxy(2*M, K, trainingLogList::add);

        initGUI();
    }

    private void initGUI() {
        JTabbedPane tabbedPane = new JTabbedPane();

        JPanel tab1 = new JPanel();
        JPanel tab2 = new JPanel();
        JPanel tab3 = new JPanel();

        initDrawTab(tab1);
        initTrainTab(tab2);
        initTestTab(tab3);

        tabbedPane.addTab("draw", tab1);
        tabbedPane.addTab("train", tab2);
        tabbedPane.addTab("test", tab3);

        getContentPane().add(tabbedPane);
        pack();
    }

    private void initDrawTab(JPanel tab) {
        tab.setLayout(new BorderLayout());
        tab.add(createToolBar(), BorderLayout.NORTH);
        tab.add(canvas, BorderLayout.CENTER);
        tab.add(createFooter(), BorderLayout.SOUTH);
        tab.add(createList(), BorderLayout.EAST);
        canvas.setPreferredSize(new Dimension(350, 350));
        setupActions();
    }

    private void initTrainTab(JPanel tab) {
        tab.setLayout(new BorderLayout());
        // initialize top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        tab.add(topPanel, BorderLayout.NORTH);
        // add configuration stuff to top panel
        architectureField.setPreferredSize(new Dimension(50, 20));
        // radio buttons for choosing the algorithm
        JRadioButton batchRadioButton = new JRadioButton("batch");
        JRadioButton onlineRadioButton = new JRadioButton("online");
        JRadioButton minibatchRadioButton = new JRadioButton("minibatch");
        minibatchRadioButton.setSelected(true);
        ButtonGroup algorithmButtons = new ButtonGroup();
        algorithmButtons.add(batchRadioButton);
        algorithmButtons.add(onlineRadioButton);
        algorithmButtons.add(minibatchRadioButton);
        // action listener to enable/disable batch size
        ActionListener enableBatchSizeListener = e -> {
            if (minibatchRadioButton.isSelected()) {
                batchSizeField.setEnabled(true);
            } else if (batchRadioButton.isSelected()) {
                batchSizeField.setEnabled(false);
                batchSizeField.setText("-1");
            } else if (onlineRadioButton.isSelected()) {
                batchSizeField.setEnabled(false);
                batchSizeField.setText("1");
            } else {
                throw new RuntimeException("This should not have happened!");
            }
        };
        batchRadioButton.addActionListener(enableBatchSizeListener);
        onlineRadioButton.addActionListener(enableBatchSizeListener);
        minibatchRadioButton.addActionListener(enableBatchSizeListener);
        // training button
        JButton trainButton = new JButton(train);
        trainButton.setPreferredSize(new Dimension(70, 20));
        // add them all to top
        topPanel.add(architectureLabel);
        topPanel.add(architectureField);
        topPanel.add(batchRadioButton);
        topPanel.add(onlineRadioButton);
        topPanel.add(minibatchRadioButton);
        topPanel.add(batchSizeLabel);
        topPanel.add(batchSizeField);
        topPanel.add(epochsLabel);
        topPanel.add(epochsField);
        topPanel.add(lrLabel);
        topPanel.add(lrField);
        topPanel.add(trainButton);
        // add log list to center
        tab.add(trainingLogList, BorderLayout.CENTER);
        tab.add(trainlTabLabel, BorderLayout.SOUTH);
    }

    private void initTestTab(JPanel tab) {
        tab.setLayout(new BorderLayout());
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        answerLabel.setPreferredSize(new Dimension(300, 30));
        decisionLabel.setPreferredSize(new Dimension(80, 30));

        topPanel.add(answerLabel);
        topPanel.add(decisionLabel);

        tab.add(topPanel, BorderLayout.NORTH);
        tab.add(testCanvas, BorderLayout.CENTER);
        tab.add(testTabLabel, BorderLayout.SOUTH);
    }

    private List createList() {
        samplesList.setPreferredSize(new Dimension(200, 600));
        return samplesList;
    }

    private Container createFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footer.add(label);
        return footer;
    }

    private void setupActions() {
        save.putValue(Action.NAME, "save");
        save.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
        save.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_S);
        save.putValue(Action.SHORT_DESCRIPTION, "Save samples to disk.");
        save.setEnabled(true);

        load.putValue(Action.NAME, "load");
        load.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control L"));
        load.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_L);
        load.putValue(Action.SHORT_DESCRIPTION, "Load samples from disk.");
        load.setEnabled(true);

        train.putValue(Action.NAME, "train");
        train.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke("control T"));
        train.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_T);
        train.putValue(Action.SHORT_DESCRIPTION, "Start the training process.");
        train.setEnabled(true);
    }

    private JToolBar createToolBar() {
        // create and configure buttons
        JButton saveButton = new JButton(save);
        JButton loadButton = new JButton(load);
        saveButton.setPreferredSize(new Dimension(80, 20));
        loadButton.setPreferredSize(new Dimension(80, 20));
        // configure other stuff
        filenameTextField.setPreferredSize(new Dimension(100, 20));
        labelTextField.setPreferredSize(new Dimension(40, 20));
        labelTextField.addActionListener(event -> {
            logger.warning("Edited label.");
            try {
                int gestureLabel = Integer.parseInt(labelTextField.getText());
                canvas.setCurrentLabel(gestureLabel);
            } catch (NumberFormatException e) {
                logger.warning("Could not parse '" + labelTextField.getText() + "' as integer");
            }
        });
        // create the toolbar
        JToolBar toolbar = new JToolBar();
        toolbar.setLayout(new FlowLayout(FlowLayout.LEFT));
        // add everything to the toolbar
        toolbar.add(saveButton);
        toolbar.add(loadButton);
        toolbar.add(labelTextField);
        toolbar.add(filenameTextField);
        return toolbar;
    }

    private void save() {
        try {
            dataHoarder.save(getPath());
            label.setText("Saved successfuly at " + getPath());
        } catch (IOException e) {
            logger.severe(e.getMessage());
            label.setText("Could not save :(");
        }
    }

    private void load() {
        try {
            dataHoarder.load(getPath());
            samplesList.removeAll();
            samplesList.add("Loaded " + dataHoarder.getDataset().size() + " samples.");
            label.setText("Loaded succesfully from " + getPath());
        } catch (IOException e) {
            logger.severe(e.getMessage());
            label.setText("Could not load from " + getPath());
        }
    }

    private void train() {
        try {
            trainingLogList.removeAll();

            String pathToDataset = getPath();
            int[] hiddenLayers = Arrays.stream(architectureField.getText().split(","))
                    .mapToInt(Integer::parseInt).toArray();
            int batchSize = Integer.parseInt(batchSizeField.getText());
            int epochs = Integer.parseInt(epochsField.getText());
            double lr = Double.parseDouble(lrField.getText());
            ann.train(pathToDataset, hiddenLayers, batchSize, epochs, lr);

        } catch (IOException e) {
            label.setText(e.getMessage());
            logger.throwing("DrawingFrame", "train", e);
        }
    }

    private void classify(double[] sample) {
        new Thread(() -> {
            try {
                double[] probs = ann.predict(sample);
                int label = 0;
                for (int i = 1; i < probs.length; i++) {
                    if (probs[i] > probs[label]) {
                        label = i;
                    }
                }

                answerLabel.setText(format(probs));
                decisionLabel.setText("decision: " + label);

            } catch (IOException e) {
                logger.throwing("DrawingFrame", "classify", e);
                testTabLabel.setText(e.getMessage());
            }
        }).start();

/*        new Thread(() -> {
            String line;
            while (stdErr.hasNextLine()) {
                line = stdErr.nextLine();
                logger.severe("Test: " + line);
            }
            testTabLabel.setText("Tester emmited errors, check the log.");
        }).start();
*/
    }

    private String format(double[] array) {
        return Arrays.stream(array).mapToObj(d -> String.format("%.3f", d)).collect(Collectors.joining(", "));
    }

    private String getPath() {
        return DATASET_PATH + "/" + filenameTextField.getText();
    }

    public static void main(String[] args) {
        if (args.length > 0) {
            M = Integer.parseInt(args[0]);
        }
        SwingUtilities.invokeLater(() -> new DrawingFrame().setVisible(true));
    }
}
