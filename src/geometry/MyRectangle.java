package geometry;

import java.awt.Rectangle;
import java.util.ArrayList;

public class MyRectangle extends Rectangle {

    public MyRectangle(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /** Returns an array of all lines composing this rectangle
     * @return an ArrayList of all 4 lines composing this rect
     */
    public ArrayList<Line> getLines() {
        ArrayList<Line> lines = new ArrayList<>();

        int x1 = this.x;
        int x2 = this.x + this.width;
        int y1 = this.y;
        int y2 = this.y + this.height;

        lines.add(new Line(x1, y1, x1, y2));
        lines.add(new Line(x1, y1, x2, y1));
        lines.add(new Line(x2, y1, x2, y2));
        lines.add(new Line(x1, y2, x2, y2));
        return lines;
    }
}
