package tue.group7ap;

import java.awt.*;
import java.util.Random;
import javax.swing.*;

/**
 * Class for the Apple.
 */
public class Apple {
    int x;
    int y;
    int size;
    Color color;

    /**
     * Constructor method.
     */
    Apple() {
        x = GameConstants.GAME_WIDTH - 1;
        y = GameConstants.GAME_HEIGHT - 1;
        size = GameConstants.UNIT_SIZE;
        color = Color.RED;
    }

    /**
     * Generate new apple position.
     * 
     * @param minX minimal x coordinate
     * @param maxX maximal x coordinate
     * @param minY minimal y coordinate
     * @param maxY maximal y coordinate
     */
    public void generate(int minX, int maxX, int minY, int maxY) {
        Random random = new Random();
        x = random.nextInt(minX, maxX);
        y = random.nextInt(minY, maxY);

    }

    /**
     * Draw the apple.
     * 
     * @param game the game panel where the apple is drawn
     * @param g    the graphics used to draw the apple
     */
    public void draw(JPanel game, Graphics g) {
        ImageIcon wallTextureIcon = new ImageIcon("images/apple.png");
        Image wallTexture = wallTextureIcon.getImage();
        wallTexture = wallTexture.getScaledInstance(GameConstants.UNIT_SIZE,
                GameConstants.UNIT_SIZE,
                Image.SCALE_DEFAULT);
        ImageIcon scaledWallTextureIcon = new ImageIcon(wallTexture);
        int posX = x * GameConstants.UNIT_SIZE;
        int posY = y * GameConstants.UNIT_SIZE;
        scaledWallTextureIcon.paintIcon(game, g, posX, posY);
    }

}
