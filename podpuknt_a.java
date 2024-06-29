import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class HierarchicznaScenaSubrutynowa extends JPanel implements Runnable {

    private double angle = 0;

    public HierarchicznaScenaSubrutynowa() {
        setPreferredSize(new Dimension(800, 600));
    }

    private void drawDodecagon(Graphics2D g2d, int x, int y, int radius) {
        int sides = 12;
        int[] xPoints = new int[sides];
        int[] yPoints = new int[sides];
        for (int i = 0; i < sides; i++) {
            double theta = Math.toRadians(i * 360.0 / sides);
            xPoints[i] = (int) (x + radius * Math.cos(theta));
            yPoints[i] = (int) (y + radius * Math.sin(theta));
        }
        g2d.drawPolygon(xPoints, yPoints, sides);
    }

    private void drawArmWithDodecagons(Graphics2D g2d, int x, int y, int length, double angle) {
        AffineTransform old = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(angle));
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(10));
        g2d.drawLine(-length, 0, length, 0);

        g2d.setColor(Color.BLACK);
        drawDodecagon(g2d, length, 0, 30);
        drawDodecagon(g2d, -length, 0, 30);

        g2d.setTransform(old);
    }

    private void drawPivot(Graphics2D g2d, int x, int y, int height, Color color) {
        g2d.setColor(color);
        int[] xPoints = {x, x - height / 2, x + height / 2};
        int[] yPoints = {y, y + height, y + height};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }

    private void drawSystem(Graphics2D g2d, int x, int y, int armLength, int pivotHeight, Color pivotColor, double rotationAngle) {
        drawPivot(g2d, x, y, pivotHeight, pivotColor);
        drawArmWithDodecagons(g2d, x, y, armLength, rotationAngle);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        drawSystem(g2d, 200, 300, 100, 100, Color.MAGENTA, angle);
        drawSystem(g2d, 600, 300, 100, 100, Color.BLUE, angle);
        drawSystem(g2d, 400, 100, 100, 100, Color.GREEN, angle);
    }

    @Override
    public void run() {
        while (true) {
            angle += 1;
            repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Scena Hierarchiczna - Metoda Subrutynowa");
        HierarchicznaScenaSubrutynowa panel = new HierarchicznaScenaSubrutynowa();
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Thread animationThread = new Thread(panel);
        animationThread.start();
    }
}
