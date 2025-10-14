package geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PointTest {
    @Test
    public void testEuclDist() {
        Line l = new Line(0, 0, 0, 5);
        assertTrue(l.start.euclDist(l.end) == 5.0);
    }
    
}
