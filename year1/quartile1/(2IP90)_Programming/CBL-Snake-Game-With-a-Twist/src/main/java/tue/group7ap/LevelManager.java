package tue.group7ap;

import java.awt.*;
import java.util.ArrayList;

/**
 * Class for managing the levels.
 */
public class LevelManager {
    private ArrayList<Level> levels;
    private int currentLevelIndex;

    /**
     * Constructor method.
     */
    public LevelManager() {
        levels = new ArrayList<>();
        // Initialize levels

        // 1 level: no obstackles

        levels.add(new Level(1, new ArrayList<Point>(), LevelType.ScoreTarget, 5));
        ArrayList<Point> obstaclesLevel2 = new ArrayList<>();
        for (int i = 10 * GameConstants.UNIT_SIZE; i < GameConstants.SCREEN_WIDTH
                - 10 * GameConstants.UNIT_SIZE; i += GameConstants.UNIT_SIZE) {
            obstaclesLevel2.add(new Point(i, GameConstants.SCREEN_HEIGHT / 2));
        }
        levels.add(new Level(2, obstaclesLevel2, LevelType.LimitedMoves, 3));

        ArrayList<Point> obstaclesLevel3 = new ArrayList<>();
        for (int i = 10 * GameConstants.UNIT_SIZE; i < GameConstants.SCREEN_WIDTH
                - 10 * GameConstants.UNIT_SIZE; i += GameConstants.UNIT_SIZE) {
            obstaclesLevel3.add(new Point(i, GameConstants.SCREEN_HEIGHT / 2));
        }

        for (int i = 5 * GameConstants.UNIT_SIZE; i < GameConstants.SCREEN_HEIGHT
                - 5 * GameConstants.UNIT_SIZE; i += GameConstants.UNIT_SIZE) {
            obstaclesLevel3.add(new Point(GameConstants.SCREEN_WIDTH / 2, i));
        }
        levels.add(new Level(2, obstaclesLevel3, LevelType.ScoreTarget, 7));

        ArrayList<Point> obstaclesLevel4 = new ArrayList<>();
        for (int i = 5 * GameConstants.UNIT_SIZE; i < GameConstants.SCREEN_WIDTH
                - 5 * GameConstants.UNIT_SIZE; i += GameConstants.UNIT_SIZE) {
            obstaclesLevel4
                    .add(new Point(i, 1 * (int) (GameConstants.SCREEN_HEIGHT / 5)
                            - GameConstants.UNIT_SIZE));
            obstaclesLevel4
                    .add(new Point(i, 2 * (int) (GameConstants.SCREEN_HEIGHT / 5)
                            - GameConstants.UNIT_SIZE));
            obstaclesLevel4
                    .add(new Point(i, 3 * (int) (GameConstants.SCREEN_HEIGHT / 5)
                            - GameConstants.UNIT_SIZE));
            obstaclesLevel4
                    .add(new Point(i, 4 * (int) (GameConstants.SCREEN_HEIGHT / 5)
                            - GameConstants.UNIT_SIZE));
        }
        levels.add(new Level(3, obstaclesLevel4, LevelType.LimitedTime, 10));

        ArrayList<Point> obstaclesLevel5 = new ArrayList<>();
        for (int i = 5 * GameConstants.UNIT_SIZE; i < GameConstants.SCREEN_WIDTH
                - 5 * GameConstants.UNIT_SIZE; i += GameConstants.UNIT_SIZE) {
            if (i < GameConstants.SCREEN_WIDTH / 3 || i > 2.0 / 3
                    * GameConstants.SCREEN_WIDTH) {
                obstaclesLevel5
                        .add(new Point(i, 1 * GameConstants.SCREEN_HEIGHT / 5));
                obstaclesLevel5
                        .add(new Point(i, 4 * GameConstants.SCREEN_HEIGHT / 5
                                - 2 * GameConstants.UNIT_SIZE));
            }
        }
        for (int i = 4 * GameConstants.UNIT_SIZE; i < GameConstants.SCREEN_HEIGHT
                - 5 * GameConstants.UNIT_SIZE; i += GameConstants.UNIT_SIZE) {
            obstaclesLevel5.add(new Point(GameConstants.SCREEN_WIDTH / 4, i));
            obstaclesLevel5.add(new Point(2 * GameConstants.SCREEN_WIDTH / 4, i));
            obstaclesLevel5.add(new Point(3 * GameConstants.SCREEN_WIDTH / 4, i));
        }
        levels.add(new Level(4, obstaclesLevel5, LevelType.ScoreTarget, 3));

        ArrayList<Point> obstaclesMaze = new ArrayList<>();

        // Maze map
        String[] maze = {
                "OOOOOOOOOOOOOOOOOOXOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOX",
                "OOOOOOOOOOOOOOOOOOXOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOX",
                "OOOOOOOXOOOOOOOOOOOOOOOOOOOOOOOOOOOOXOOOOOOOOOOOOOOX",
                "OOOOOOOXOOOOOOOOOOOOOOOOOOOOOOOOOOOOXOOOOOOOOOOOOOOX",
                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXOOOOX",
                "XOOOOOOOOOOOOOOOOOOOOOOOOXXXOOOOOOOOOOOOOOOOOOXOOOOX",
                "XOOOOOOOOOOOOOOOOOOOOOOOOOXOOOOOOOOOOOOOOOOOOOXOOOOX",
                "XOOOOOOOOOOOOOOOOOOOOOOOOOXOOOOOOOOOOOOOOOOOOOXOOOOX",
                "XOOOOOOOOOOOOOOOOOOOOOOOOOXOOOOOOOOOOOOOOOOOOOXXXOOX",
                "XOOOOXXXXXXXXXXXXXXXXXOOOOXOOOOOOXXXXXXXXXOOOOXOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOXOOOOOXXOOOOOOOOOOOOXOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOXOOOOXXOOOOOOOOOOOOOXOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOXOOOOXOOOOOOOOOOOOOOXOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOXOOOOXOOOOXXXXXXXXXXXOOXXX",
                "XOOOOXOOOOXXXXXXXXXXXXXXXXXOOOOXOOOOOOOOOOOOOOOOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOXOOOOXOOOOOOOOOOOOOOOOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOXOOOOXXOOOOOOOOOOOOOOOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOXOOOOOXXOOOOOOOOOOOOOOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOXOOOOOOXXXXXXXXXXXXXXXXXXX",
                "XOOOOXXXXXXXXXXXXXXXXXOOOOXXOOOOOOOOOOOOOOOOOOOOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOOXXOOOOOOOOOOOOOOOOOOOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOOOXXOOOOOOOOOOOOOOOOOOOOOX",
                "XOOOOXOOOOOOOOOOOOOOOOOOOOOOOXXXXXXXXXXXXXXXXXOOOOOX",
                "XOOOOXXXXXXXXXXXOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOX",
                "XOOOOOOOOOOOOOXXXXXXXXXXXXOOOOOOOOOOOOOOOOOOOOOOOOOX",
                "XOOOOOOOOOOOOOOOXOOOOOOOXXXXXXXXXXXXXXXXOOOOOOOOOOOX",
                "XOOOOOOXOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOXXXXXXXXXXXXXX",
                "XOOOOOOXOOOOOOOOOOOOOOOOOOOXOOOOOOOOOOOOOOOOOOOOOOOX",
                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX",
        };

        for (int y = 0; y < maze.length; y++) {
            for (int x = 0; x < maze[y].length(); x++) {
                if (maze[y].charAt(x) == 'X') {
                    int posX = x * GameConstants.UNIT_SIZE;
                    int posY = y * GameConstants.UNIT_SIZE;
                    obstaclesMaze.add(new Point(posX, posY));
                }
            }
        }
        levels.add(new Level(1, obstaclesMaze, LevelType.Maze, 0));

        currentLevelIndex = 0; // Start with the first level
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    /**
     * Get the current level.
     *
     * @return level that is being played
     */
    public Level getCurrentLevel() {
        if (currentLevelIndex >= 0 && currentLevelIndex < levels.size()) {
            return levels.get(currentLevelIndex);
        } else {
            return null;
        }
    }

    /**
     * Go to next the next level.
     */
    public void goToNextLevel() {
        if (currentLevelIndex < levels.size() - 1) {
            currentLevelIndex++;
        }
    }

    public boolean isFinalLevel() {
        return currentLevelIndex == levels.size() - 1;
    }

    public void reset() {
        currentLevelIndex = 0;
    }

}
