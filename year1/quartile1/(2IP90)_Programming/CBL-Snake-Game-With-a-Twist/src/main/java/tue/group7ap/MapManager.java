package tue.group7ap;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Class for managing the map.
 */
public class MapManager {
    /**
     * Draw the map (obstacles).
     *
     * @param game        the game panel where the obstacles are drawn
     * @param g           the graphics object used to draw
     * @param coordinates coordinates of the obstacles
     */
    public static void drawMap(JPanel game, Graphics g, ArrayList<Point> coordinates) {
        ImageIcon wallTextureIcon = new ImageIcon("images/wall_texture.jpg");
        Image wallTexture = wallTextureIcon.getImage();
        wallTexture = wallTexture.getScaledInstance(GameConstants.UNIT_SIZE,
                GameConstants.UNIT_SIZE,
                Image.SCALE_DEFAULT);
        ImageIcon scaledWallTextureIcon = new ImageIcon(wallTexture);
        for (Point coordinate : coordinates) {

            int x = coordinate.x;
            int y = coordinate.y;

            scaledWallTextureIcon.paintIcon(game, g, x, y);
        }
    }
}
