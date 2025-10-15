package display;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
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
    static private int MOUSE_SPEED = 100; // over 1000

    static private int MIN_MOUSE_STEP = 10;
    static private int MOUSE_RANGE_BOUND = 20; // how many pixels can the mouse move before being reset to center position
                                               // (this has no direct impact on direction, it just prevent flickering at small steps)

    private int mouseX = 0;
    private int mouseY = 0;
    private int yOffset = 0;
    private JFrame frame;
    private Robot robot;
    private java.awt.Point centerPoint;

    public void setup() {
        Window window = SwingUtilities.getWindowAncestor(this);
        if (window instanceof JFrame) {
            frame = (JFrame) window;
        } else {
            System.err.println("Could not resolve window inheritence, frame unaccessible. Exiting");
            System.exit(1);
        }
        java.awt.Point windowPos = window.getLocationOnScreen();
        centerPoint = new java.awt.Point(frame.getWidth()/2, frame.getHeight()/2);

        // invisible + locked mouse cursor
        BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor invisibleCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new java.awt.Point(0, 0), "invisibleCursor");
        frame.getContentPane().setCursor(invisibleCursor);

        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }

        // mouse motion listener
        addMouseMotionListener (new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int deltaX = e.getX() - centerPoint.x;
                int deltaY = e.getY() - centerPoint.y;

                // X
                if (Math.abs(deltaX) > MIN_MOUSE_STEP) {
                    player.direction += (deltaX - mouseX)*MOUSE_SPEED/1000;
                    mouseX += deltaX;
                    repaint();
                }

                // Y
                if (Math.abs(deltaY) > MIN_MOUSE_STEP) {
                    yOffset += (deltaY - mouseY);
                    System.out.println("yOffset: " + yOffset);
                    mouseY += deltaY;
                    repaint();
                }


                // mouse position reset
                if (Math.abs(mouseX) > MOUSE_RANGE_BOUND || Math.abs(mouseX) > MOUSE_RANGE_BOUND || Math.abs(yOffset) > 50) {
                    robot.mouseMove(windowPos.x + centerPoint.x, windowPos.y + centerPoint.y);
                    mouseX = 0;
                    mouseY = 0;
                }
                
            }
        });

        // exit listener
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ESCAPE"), "closeWindow");
        getActionMap().put("closeWindow", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

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
        // if (turnLeft) {
        //     this.player.turnLeft();
        // }
        // if (turnRight) {
        //     this.player.turnRight();
        // }
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
            geometry.Point p = player.closestIntersection(l, rects);
            if (p != null) g2d.drawOval(p.x-2, p.y-2, 4, 4);
        }


        // 3D rendering
        for (int i = 0; i<rays.size();i++) {
            Line l = rays.get(i);
            geometry.Point p = player.closestIntersection(l, rects);
            if (p != null) {
                double dist = player.euclDist(p) * Math.cos(Conversion.toRad((-player.fov/2)+i*player.visionStep)); // distance perpendiculaire (corrigée)
                int x = i*(WIDTH/rays.size());
                int height = (int)(((player.visionDistance - dist)/player.visionDistance)*WIDTH);
                int y = (int) HEIGHT/2 - height/2 + yOffset;
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

        panel.setup();

        Thread loopThread = new Thread(panel);
        loopThread.start();
    }
}
