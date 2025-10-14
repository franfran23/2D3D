package test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import geometry.*;

public class MyRectangleTest {
    @Test
    public void getLinesTest() {
        MyRectangle rect = new MyRectangle(0, 0, 10, 10);
        ArrayList<Line> sides = rect.getLines();
        assertTrue(sides.get(0).equals(new Line(0, 0, 0, 10)));
        assertTrue(sides.get(1).equals(new Line(0, 0, 10, 0)));
        assertTrue(sides.get(2).equals(new Line(10, 0, 10, 10)));
        assertTrue(sides.get(3).equals(new Line(0, 10, 10, 10)));
    }
}
