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

    /**
     * Renvoie la distance perpendiculaire entre cette ligne et un point
     * @param p un point sur le plan
     * @return la distance perpendiculaire entre cette ligne et le point
     */
    public double perpDist(Point p)
    {
        // coords du point
        int x0 = p.x; int y0 = p.y;
        // coords de la ligne
        int x1 = sX; int y1 = sY;
        int x2 = eX; int y2 = eY;
        
        double num = Math.abs( (y2-y1)*x0 - (x2-x1)*y0 + x2*y1 - y2*x1 );
        double den = Math.sqrt( (y2-y1)*(y2-y1) + (x2-x1)*(x2-x1) );

        return num/den;

    }
}
