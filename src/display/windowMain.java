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
    private boolean up, down, left, right, turnLeft, turnRight;
    private final AtomicBoolean running = new AtomicBoolean(true);

    static private int WIDTH = 1000;
    static private int HEIGHT = 1000;

    public windowMain() {
        rects.add(new MyRectangle(50, 50, 100, 200));
        rects.add(new MyRectangle(200, 50, 50, 50));

        setBackground(Color.BLACK);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    /** Main game loop */
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

    /** Update player position */
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
        if (right) {
            this.player.y += (int)speed*Math.sin(Conversion.toRad(this.player.direction + 90));
            this.player.x += (int)speed*Math.cos(Conversion.toRad(this.player.direction + 90));
        }
        if (left) {
            this.player.y -= (int)speed*Math.sin(Conversion.toRad(this.player.direction + 90));
            this.player.x -= (int)speed*Math.cos(Conversion.toRad(this.player.direction + 90));
        }
        if (turnLeft) {
            this.player.direction--;
        }
        if (turnRight) {
            this.player.direction++;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z:
                up = true;
                break;
            case KeyEvent.VK_S:
                down = true;
                break;
            case KeyEvent.VK_Q:
                left = true;
                break;
            case KeyEvent.VK_D:
                right = true;
                break;
            case KeyEvent.VK_LEFT:
                turnLeft = true;
                break;
            case KeyEvent.VK_RIGHT:
                turnRight = true;
                break;
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_Z:
                up = false;
                break;
            case KeyEvent.VK_S:
                down = false;
                break;
            case KeyEvent.VK_Q:
                left = false;
                break;
            case KeyEvent.VK_D:
                right = false;
                break;
            case KeyEvent.VK_LEFT:
                turnLeft = false;
                break;
            case KeyEvent.VK_RIGHT:
                turnRight = false;
                break;
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {
        // optional (used for typed characters, not movement)
    }

    public void stop() {
        running.set(false);
    }



    /** Actual "frame" processing
     * drawing every object to it's new location on the scene
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        ArrayList<Line> rays = player.genRays();

        // 2D rendering
        g2d.setColor(Color.RED);
        int playerMidSize = (int)this.player.size/2;
        g2d.fillOval(this.player.x-playerMidSize, this.player.y-playerMidSize, this.player.size, this.player.size);
        Line directionLine = this.player.genDirectionLine();
        g2d.drawLine(directionLine.sX, directionLine.sY, directionLine.eX, directionLine.eY);

        g2d.setColor(Color.ORANGE);
        for (Line l: rays) {
            Point p = player.closestIntersection(l, rects);
            if (p != null) g2d.drawOval(p.x-2, p.y-2, 4, 4);
        }


        // 3D rendering
        for (int i = 0; i<rays.size();i++) {
            Line l = rays.get(i);
            Point p = player.closestIntersection(l, rects);
            if (p != null) {
                double dist = player.euclDist(p);
                int x = i*(WIDTH/rays.size());
                int height = (int)(((player.visionDistance - dist)/player.visionDistance)*WIDTH);
                int y = (int) HEIGHT/2 - height/2;
                //                              H (valeur quelconque)  S (saturation 0 = blanc)  B (luminosité dependante de la distance)
                g2d.setColor(Color.getHSBColor( (float)1.0,            (float)0.0,               (float)((player.visionDistance - dist)/player.visionDistance)));
                g2d.fillRect(x, y, WIDTH/rays.size(), height);
                
            }
        }

    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("My frame !");
        windowMain panel = new windowMain();

        frame.add(panel);
        frame.setSize(WIDTH, HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Thread loopThread = new Thread(panel);
        loopThread.start();
    }
}
