package geometry;

public class Point {
    
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Point)) return false;
        Point p = (Point) obj;
        return this.x == p.x && this.y == p.y;
    }

    /** Calculates the euclidian distance between two points
     * @param point a second point
     * @return the euclidian distance between itself and the other point
     */
    public double euclDist(Point p) {
        return Math.sqrt((this.x-p.x)*(this.x-p.x) + (this.y-p.y)*(this.y-p.y));
    }

    public String toString() {
        return String.valueOf(this.x) + " " + String.valueOf(this.y);
    }

}
