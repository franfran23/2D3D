import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

/** Tests brutes pour comprendre le fonctionnement des graphics java */
public class trashTestMain extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); // clears background
        Graphics2D g2d = (Graphics2D) g;

        // draw red rect
        g2d.setColor(Color.RED);
        g2d.fillRect(10, 10, 50, 70);
    }




    public static void main(String[] args) {
        // JFrame frame = new JFrame("My frame !");
        // trashTestMain panel = new trashTestMain();

        // frame.add(panel);
        // frame.setSize(500, 500);
        // frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setLocationRelativeTo(null);
        // frame.setVisible(true);
        System.out.println(String.valueOf(Math.cos(10)));
    }

}