package hr.fer.zemris.mekrac.ann.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class Canvas extends JPanel {

    private static final Logger logger = Logger.getLogger("Canvas");

    private Gesture currentGesture;
    private Consumer<Gesture> gestureConsumer;
    private int currentLabel;

    public Canvas(Consumer<Gesture> gestureConsumer) {
        logger.warning("Created canvas");
        this.gestureConsumer = gestureConsumer;
        setFocusable(true);
        MouseAdapter ma = new MouseAdapter() {

            @Override
            public void mousePressed(MouseEvent e) {
                currentGesture = new Gesture(currentLabel);
                currentGesture.add(e.getX(), e.getY());
                logger.warning("Added new gesture starting at" + e.getX() + " " + e.getY());
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                currentGesture.add(e.getX(), e.getY());
                logger.warning("Added " + e.getX() + " " + e.getY() + " and finished drawing.");
                if (gestureConsumer != null) {
                    gestureConsumer.accept(currentGesture);
                }
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                currentGesture.add(e.getX(), e.getY());
                logger.info("Added " + e.getX() + " " + e.getY());
                repaint();
            }
        };

        this.addMouseListener(ma);
        this.addMouseMotionListener(ma);
    }

    @Override
    protected void paintComponent(Graphics g) {
        logger.info("Painting canvas.");
        Graphics2D g2d = (Graphics2D) g;
        Color savedColor = g2d.getColor();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(savedColor);

        if (currentGesture != null) {
            currentGesture.paintComponent(g2d);
        }
    }

    public void setCurrentLabel(int label) {
        this.currentLabel = label;
    }
}
