package test;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

import geometry.MyRectangle;
import geometry.*;

public class PlayerTest {
    @Test
    public void closestPointTest() {
        Player player = new Player(0, 0);
        ArrayList<MyRectangle> rects = new ArrayList<>();
        rects.add(new MyRectangle(1, -1, 2, 2));
        assertEquals(1, rects.size());
        Point closest = player.closestIntersection(new Line(0, 0, 5, 0), rects);
        assertTrue(closest.x == 1 && closest.y == 0);
    }
}
