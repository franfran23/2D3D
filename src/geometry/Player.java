package geometry;

import java.util.ArrayList;

public class Player extends Point {
    public static int size = 20;
    public static double direction = 0; // degrees
    public static double durectionStep = 0.8; // degrees
    public static int visionDistance = 300;
    public static double visionStep = .5; // degrees
    public static int fov = 90; // degrees

    public Player(int x, int y) {
        super(x, y);
    }

    /** Make the player direction turn left */
    public void turnLeft() {
        this.direction -= this.durectionStep;
    }
    /** Make the player direction turn right */
    public void turnRight() {
        this.direction += this.durectionStep;
    }

    /** Generate a list of eye sights
     * @return the list of lines corresponding to the player vision
     */
    public ArrayList<Line> genRays() {
        ArrayList<Line> eyesight = new ArrayList<>();
        for (double i = -fov/2; i<fov/2; i+=visionStep) {
            // la moitié des rayons à gauche l'autre moitié à droite
            eyesight.add(
                new Line(
                    this.x, // origin = player pos
                    this.y,
                    this.x + (int)(this.visionDistance*Math.cos(Conversion.toRad(this.direction+i))),
                    this.y + (int)(this.visionDistance*Math.sin(Conversion.toRad(this.direction+i)))
                )
            );
        }
        return eyesight;
    }

    /** Calculates coordinates of the direction ray
     * (the ray pointing in the movements direction of the player)
     * @return the direction of the player
     */
    public Line genDirectionLine() {
        return new Line(this.x, this.y, 
                    this.x + (int)(this.visionDistance*Math.cos(Conversion.toRad(this.direction))),
                    this.y + (int)(this.visionDistance*Math.sin(Conversion.toRad(this.direction))));
    }

    /** Takes a line, a list of rectangles and calculates the closest intersection point
     * @param line the main vision ray
     * @param ArrayList<MyRectangle> a list of rectangles on the map
     * @return the closest intersection point to the start of the line
     */
    public Point closestIntersection(Line ray, ArrayList<MyRectangle> rects) {
        Point closest = null;
        double smallestDistance = 0;
        for (MyRectangle r: rects) {
            for (Line side: r.getLines()) {
                Point intersection = ray.intersection(side);
                if (intersection != null && (closest == null || this.euclDist(intersection) < smallestDistance)) {
                    closest = intersection;
                    smallestDistance = this.euclDist(intersection);
                }
            }
        }
        return closest;
    }
}