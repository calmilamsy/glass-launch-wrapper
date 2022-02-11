package gg.codie.mineonline.gui.textures;

import org.json.JSONObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;

public class TextureHelper {
    // Used in reflection.
    public static InputStream convertModernSkin(JSONObject skinPayload){
        try {
            InputStream inputStream = new URL(skinPayload.getString("url")).openStream();
            boolean alex = skinPayload.has("metadata") && "slim".equals(skinPayload.getJSONObject("metadata").optString("model", null));
            BufferedImage skin = ImageIO.read(inputStream);
            boolean tall = skin.getHeight() > 32;
            BufferedImage movePart = null;
            Graphics2D graphics = skin.createGraphics();
            AlphaComposite alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER);
            graphics.setComposite(alpha);


            if (alex) {
                // Convert alex to steve.

                // Stretch right arm.
                movePart = skin.getSubimage(45, 16, 9, 16);
                graphics.drawImage(movePart, 46, 16, null);
                movePart = skin.getSubimage(49, 16, 2, 4);
                graphics.drawImage(movePart, 50, 16, null);
                movePart = skin.getSubimage(53, 20, 2, 12);
                graphics.drawImage(movePart, 54, 20, null);

                if (tall) {
                    // Stretch right sleeve.
                    movePart = skin.getSubimage(45, 32, 9, 16);
                    graphics.drawImage(movePart, 46, 32, null);
                    movePart = skin.getSubimage(49, 32, 2, 4);
                    graphics.drawImage(movePart, 50, 32, null);
                    movePart = skin.getSubimage(53, 36, 2, 12);
                    graphics.drawImage(movePart, 54, 36, null);

                    // Stretch left arm.
                    movePart = skin.getSubimage(37, 48, 9, 16);
                    graphics.drawImage(movePart, 38, 48, null);
                    movePart = skin.getSubimage(41, 48, 2, 4);
                    graphics.drawImage(movePart, 42, 32, null);
                    movePart = skin.getSubimage(45, 52, 2, 12);
                    graphics.drawImage(movePart, 46, 36, null);

                    // Stretch left sleeve.
                    movePart = skin.getSubimage(53, 48, 9, 16);
                    graphics.drawImage(movePart, 54, 48, null);
                    movePart = skin.getSubimage(57, 48, 2, 4);
                    graphics.drawImage(movePart, 58, 32, null);
                    movePart = skin.getSubimage(61, 52, 2, 12);
                    graphics.drawImage(movePart, 62, 36, null);
                }
            }

            if (tall) {
                // Flatten second layers.
                movePart = skin.getSubimage(0, 32, 56, 16);
                graphics.drawImage(movePart, 0, 16, null);
            }

            graphics.dispose();

            // Crop
            BufferedImage croppedSkin = skin.getSubimage(0, 0, 64, 32);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(croppedSkin, "png", os);
            byte[] bytes = os.toByteArray();
            return new ByteArrayInputStream(bytes);
        } catch (Exception ex) {
            return null;
        }
    }
}
