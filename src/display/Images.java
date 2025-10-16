package display;

import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;;

public class Images {

    static String imageSource = "resources/images/";
    static Map<String, String> imagesPath = new HashMap<String, String>() {{
        put("degrade", imageSource + "degrade.png");
        put("macron", imageSource + "macron.jpeg");
    }};
        

    static Map<String, BufferedImage> loadImages() {
        Map<String, BufferedImage> images = new HashMap<>();
        for (String key: imagesPath.keySet()) {
            try {
                images.put(key, ImageIO.read(new File(imagesPath.get(key))));
            } catch (IOException e) {
                System.err.println("IO Exception " + e);
            }
        }
        return images;
    }
    

}
