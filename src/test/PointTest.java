package test;

import org.junit.Test;
import static org.junit.Assert.*;

import geometry.*;

public class PointTest {
    @Test
    public void testEuclDist() {
        Line l = new Line(0, 0, 0, 5);
        assertTrue(l.start.euclDist(l.end) == 5.0);
    }
    
}
