import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

class Dodecagon {
    private int radius;

    public Dodecagon(int radius) {
        this.radius = radius;
    }

    public void draw(Graphics2D g2d, int x, int y) {
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
}

class Arm {
    private int length;
    private double angle;
    private Dodecagon dodecagon;

    public Arm(int length, double angle) {
        this.length = length;
        this.angle = angle;
        this.dodecagon = new Dodecagon(30);
    }

    public void draw(Graphics2D g2d, int x, int y) {
        AffineTransform old = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(angle));
        g2d.setColor(Color.RED);
        g2d.setStroke(new BasicStroke(10));
        g2d.drawLine(-length, 0, length, 0);

        g2d.setColor(Color.BLACK);
        dodecagon.draw(g2d, length, 0);
        dodecagon.draw(g2d, -length, 0);

        g2d.setTransform(old);
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}

class Pivot {
    private int x, y, height;
    private Color color;

    public Pivot(int x, int y, int height, Color color) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.color = color;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(color);
        int[] xPoints = {x, x - height / 2, x + height / 2};
        int[] yPoints = {y, y + height, y + height};
        g2d.fillPolygon(xPoints, yPoints, 3);
    }
}

class SystemElement {
    private Pivot pivot;
    private Arm arm;

    public SystemElement(int x, int y, int armLength, int pivotHeight, Color pivotColor, double initialAngle) {
        pivot = new Pivot(x, y, pivotHeight, pivotColor);
        arm = new Arm(armLength, initialAngle);
    }

    public void draw(Graphics2D g2d) {
        pivot.draw(g2d);
        arm.draw(g2d, pivot.x, pivot.y);
    }

    public void setAngle(double angle) {
        arm.setAngle(angle);
    }
}

public class HierarchicznaScenaObiektowa extends JPanel implements Runnable {

    private double angle = 0;
    private SystemElement[] systems;

    public HierarchicznaScenaObiektowa() {
        setPreferredSize(new Dimension(800, 600));
        systems = new SystemElement[]{
                new SystemElement(200, 300, 100, 100, Color.MAGENTA, angle),
                new SystemElement(600, 300, 100, 100, Color.BLUE, angle),
                new SystemElement(400, 100, 100, 100, Color.GREEN, angle)
        };
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (SystemElement system : systems) {
            system.draw(g2d);
        }
    }

    @Override
    public void run() {
        while (true) {
            angle += 1;
            for (SystemElement system : systems) {
                system.setAngle(angle);
            }
            repaint();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Scena Hierarchiczna - Metoda Obiektowa");
        HierarchicznaScenaObiektowa panel = new HierarchicznaScenaObiektowa();
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Thread animationThread = new Thread(panel);
        animationThread.start();
    }
}
    