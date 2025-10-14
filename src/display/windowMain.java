package display;

import javax.swing.*;

import java.awt.Graphics; import java.awt.Graphics2D; import java.awt.Color;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import geometry.*;


public class windowMain extends JPanel implements Runnable, KeyListener {
    
    private ArrayList<MyRectangle> rects = new ArrayList<>();
    private Player player = new Player(100, 100);
    private boolean up, down, left, right;
    private final AtomicBoolean running = new AtomicBoolean(true);

    public windowMain() {
        rects.add(new MyRectangle(50, 50, 100, 200));
        rects.add(new MyRectangle(200, 50, 50, 50));

        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double nsPerFrame = 1_000_000_000.0 / 165; // 165 FPS

        while (running.get()) {
            long now = System.nanoTime();
            if (now - lastTime >= nsPerFrame) {
                update();
                repaint();
                lastTime = now;
            }

            try { Thread.sleep(1); } catch (InterruptedException ignored) {}
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        ArrayList<Line> rays = player.genRays();

        // 2D display
        g2d.setColor(Color.RED);
        int playerMidSize = (int)this.player.size/2;
        g2d.fillOval(this.player.x-playerMidSize, this.player.y-playerMidSize, this.player.size, this.player.size);
        Line directionLine = this.player.genDirectionLine();
        g2d.drawLine(directionLine.sX, directionLine.sY, directionLine.eX, directionLine.eY);

        g2d.setColor(Color.BLUE);
        for (Line l: rays) {
            Point p = player.closestIntersection(l, rects);
            if (p != null) g2d.drawOval(p.x-2, p.y-2, 4, 4);
        }


        // 3D display
        g2d.setColor(Color.WHITE);
        for (int i = 0; i<rays.size();i++) {
            Line l = rays.get(i);
            Point p = player.closestIntersection(l, rects);
            if (p != null) {
                double dist = player.euclDist(p);
                int x = i*(500/rays.size());
                int height = (int)(((player.visionDistance - dist)/player.visionDistance)*500);
                System.out.println(String.valueOf((int)dist) + " " + String.valueOf(height));
                int y = (int) 250 - height/2;
                //g2d.setColor(Color.getHSBColor((float)240.0, (float)1.0, (float)(player.visionDistance/Math.sqrt(dist))));
                g2d.fillRect(x, y, 500/rays.size(), height);
                
            }
        }

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = true;
                break;
            case KeyEvent.VK_DOWN:
                down = true;
                break;
            case KeyEvent.VK_LEFT:
                left = true;
                break;
            case KeyEvent.VK_RIGHT:
                right = true;
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
                up = false;
                break;
            case KeyEvent.VK_DOWN:
                down = false;
                break;
            case KeyEvent.VK_LEFT:
                left = false;
                break;
            case KeyEvent.VK_RIGHT:
                right = false;
                break;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // optional (used for typed characters, not movement)
    }

    private void update() {
        int speed = 2;
        if (up) {
            this.player.y += (int)speed*Math.sin(Conversion.toRad(this.player.direction));
            this.player.x += (int)speed*Math.cos(Conversion.toRad(this.player.direction));
        }
        if (down) {
            this.player.y -= (int)speed*Math.sin(Conversion.toRad(this.player.direction));
            this.player.x -= (int)speed*Math.cos(Conversion.toRad(this.player.direction));
        }
        if (left) {
            this.player.direction--;
        }
        if (right) {
            this.player.direction++;
        }
    }

    public void stop() {
        running.set(false);
    }


    /** Calculates if the given segment intersects with any side of the given MyRectangle
     * @param Line 
     * @param MyRectangle
     * @return the intersection point if there is one, null else
     */
    public Point doesIntersect(Line line, MyRectangle r) {
        ArrayList<Line> lines = r.getLines();
        for (Line l: lines) {
            Point intersect = line.intersection(l);
            if (intersect != null) return intersect;
        }
        return null;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("My frame !");
        windowMain panel = new windowMain();

        frame.add(panel);
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Thread loopThread = new Thread(panel);
        loopThread.start();
    }
}
