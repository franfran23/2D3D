package geometry;

/** Represents a 2d line
 * caracterized by the coordinates of it's start and end (x, y)
 */
public class Line {

    public int sX, sY, eX, eY;
    public Point start, end;
    


    public Line(int startX, int startY, int endX, int endY) {
        this.sX = startX;
        this.sY = startY;
        this.eX = endX;
        this.eY = endY;
        this.start = new Point(startX, startY);
        this.end = new Point(endX, endY);
    }


    /** Checks if the current line intersects with the given one and calculates the intersection point
     * @param Line an other line
     * @return the intersection point if there is one, else null
     */
    public Point intersection(Line l) {
        int x1 = this.sX;
        int x2 = this.eX;
        int x3 = l.sX;
        int x4 = l.eX;

        int y1 = this.sY;
        int y2 = this.eY;
        int y3 = l.sY;
        int y4 = l.eY;

        double denom = (x1 - x2)*(y3-y4) - (y1-y2)*(x3-x4);
        if (denom == 0) /* parallel lines */ return null;

        double t = ( (x1-x3)*(y3-y4) - (y1-y3)*(x3-x4) )/denom;
        double u = ( (x1-x3)*(y1-y2) - (y1-y3)*(x1-x2) )/denom;

        if (
            t < 0 ||
            t > 1 ||
            u < 0 ||
            u > 1
        ) /* intersection is not on the segments */ return null;

        return new Point((int)(x1 + t*(x2 - x1)), (int)(y1 + t*(y2 - y1)));
    }

    public boolean equals(Line l) {
        return this.sX == l.sX &&
                this.sY == l.sY &&
                this.eX == l.eX &&
                this.eY == l.eY;
    }
}
