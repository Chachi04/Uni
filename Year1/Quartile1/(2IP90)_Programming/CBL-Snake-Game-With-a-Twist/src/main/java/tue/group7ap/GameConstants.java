package tue.group7ap;

/**
 * Constants for the game.
 */
public class GameConstants {
    static final int SCREEN_WIDTH = 1300;
    static final int SCREEN_HEIGHT = 750;
    static final int UNIT_SIZE = 25;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static final int GAME_WIDTH = SCREEN_WIDTH / UNIT_SIZE;
    static final int GAME_HEIGHT = SCREEN_HEIGHT / UNIT_SIZE;
    static final int DELAY = 60;
    static final int HP = 3;
}
