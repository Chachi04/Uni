package tue.group7ap;

import java.awt.*;
import javax.swing.*;

/**
 * Score panel.
 */
public class ScorePanel extends JPanel implements ScoreChangeListener {
    Snake snake;
    Apple apple;
    int score;
    int level;
    int hp;
    LevelManager levelManager;

    /**
     * Constructor method of score panel.
     *
     * @param snake        the snake object of the game
     * @param apple        the apple object of the game
     * @param levelManager the level manager object of the game
     */
    ScorePanel(Snake snake, Apple apple, LevelManager levelManager) {
        setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, 2 * GameConstants.UNIT_SIZE));
        setBackground(Color.BLACK);
        setFocusable(false);
        this.snake = snake;
        this.apple = apple;
        this.levelManager = levelManager;
        level = levelManager.getCurrentLevelIndex() + 1;
        score = snake.applesEaten;
        hp = snake.hp;

    }

    /**
     * Extending the paint method of the super class.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.BLACK);
        printScore(g);
        printLevel(g);
        printHP(g);

    }

    /**
     * Method for priting the score.
     *
     * @param g graphics object used to draw
     */
    public void printScore(Graphics g) {
        g.setColor(Color.GREEN);
        int posX = 2 * GameConstants.UNIT_SIZE;
        int posY = GameConstants.UNIT_SIZE;
        g.drawString("score: " + String.valueOf(score), posX, posY);

    }

    /**
     * Print the level on the board.
     *
     * @param g graphics object used to draw
     */
    public void printLevel(Graphics g) {
        int posX = 6 * GameConstants.UNIT_SIZE;
        int posY = GameConstants.UNIT_SIZE;
        g.drawString("level: " + String.valueOf(level), posX, posY);
    }

    /**
     * Print the healt points.
     *
     * @param g graphics object used to draw
     */
    public void printHP(Graphics g) {
        int posX = 10 * GameConstants.UNIT_SIZE;
        int posY = GameConstants.UNIT_SIZE;
        g.drawString("lives: " + String.valueOf(hp), posX, posY);
    }

    @Override
    public void onVariableChange(int newValue) {
        hp = snake.hp;
        score = snake.applesEaten;
        level = levelManager.getCurrentLevelIndex() + 1;

        repaint();
    }

    @Override
    public void booleanVariableChange(boolean value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'booleanVariableChange'");
    }
}