package tue.group7ap;

import java.awt.*;
import javax.swing.*;

/**
 * The Frame that houses our game.
 */
public class Game {
    boolean inGame;
    private JFrame frame;
    public GamePanel gamePanel;
    public ScorePanel scorePanel;
    ScoreChangeListener scoreChangeListener;

    /**
     * Game contructor.
     */
    Game() {
        initialize();
    }

    public void setScoreListener(ScoreChangeListener scoreChangeListener) {
        this.scoreChangeListener = scoreChangeListener;
    }

    private void initialize() {
        this.frame = new JFrame();
        int screenWidth = GameConstants.SCREEN_WIDTH + (GameConstants.UNIT_SIZE / 2);
        int screenHeight = GameConstants.SCREEN_HEIGHT
                + 2 * GameConstants.UNIT_SIZE + (GameConstants.UNIT_SIZE / 2);
        this.frame.setSize(screenWidth, screenHeight);
        this.frame.setResizable(false);
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Apple apple = new Apple();
        Snake snake = new Snake();
        LevelManager levelManager = new LevelManager();
        scorePanel = new ScorePanel(snake, apple, levelManager);
        snake.setScoreListener(scorePanel);
        gamePanel = new GamePanel(snake, apple, levelManager, this.frame);
        this.frame.add(gamePanel);
        this.frame.add(scorePanel, BorderLayout.NORTH);
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    void start() {
        this.frame.setVisible(true);
    }
}
