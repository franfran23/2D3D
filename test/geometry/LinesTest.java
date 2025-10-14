package geometry;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LinesTest {
    
    @Test
    public void linesIntersectionsTest() {
        Line l1 = new Line(0, 0, 2, 0);
        Line l2 = new Line(1, -1, 1, 1);
        Line l3 = new Line(3, 0, 4, 1);

        assertTrue(l1.intersection(l2).equals(new Point(1, 0)));
        assertNull(l1.intersection(l3));
    }
}
