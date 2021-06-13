package javalgl.object;

import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Render {
    public static BufferedImage copyBufferedImage(BufferedImage bufferedImage) {
        BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(), bufferedImage.getType());

        for (int x = 0; x < bufferedImage.getWidth(); x++) {
            for (int y = 0; y < bufferedImage.getHeight(); y++) {
                newBufferedImage.setRGB(x, y, bufferedImage.getRGB(x, y));
            }
        }

        return newBufferedImage;
    }

    public static void renderBufferedImage(Graphics2D graphics2d, BufferedImage bufferedImage, 
                                           int x, int y, int width, int height, 
                                           boolean xFlip, boolean yFlip) {
        x = xFlip? x+width: x;
        y = yFlip? y+height: y;
        
        width *= xFlip? -1: 1;
        height *= yFlip? -1: 1;

        graphics2d.drawImage(bufferedImage, x, y, width, height, null);
    }

    public static void renderBufferedImage(Graphics2D graphics2d, BufferedImage bufferedImage, 
                                           int x, int y) {
        renderBufferedImage(graphics2d, bufferedImage, x, y, bufferedImage.getWidth(), bufferedImage.getHeight(), false, false);
    }

    public static BufferedImage load(String path) {
        try {
            return ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    

}
