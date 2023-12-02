package tue.group7ap;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Represents the snake in the game.
 */
public class Snake {
    int length = 10;
    int[] posX;
    int[] posY;
    int applesEaten = 0;
    int movesMade = 0;
    Direction direction = Direction.RIGHT;
    ScoreChangeListener scoreChangeListener;
    int hp;

    /**
     * Snake constructor - initialize position arrays.
     */
    Snake() {
        posX = new int[GameConstants.GAME_UNITS];
        posY = new int[GameConstants.GAME_UNITS];
        hp = GameConstants.HP;
    }

    public void setScoreListener(ScoreChangeListener scoreChangeListener) {
        this.scoreChangeListener = scoreChangeListener;
    }

    /**
     * Move snake 1 unit in the direction of movement.
     */
    public void move() {
        for (int i = length; i > 0; i--) {
            posX[i] = posX[i - 1];
            posY[i] = posY[i - 1];
        }
        switch (direction) {
            case UP:
                posY[0] = posY[0] - GameConstants.UNIT_SIZE;
                break;
            case DOWN:
                posY[0] = posY[0] + GameConstants.UNIT_SIZE;
                break;
            case LEFT:
                posX[0] = posX[0] - GameConstants.UNIT_SIZE;
                break;
            case RIGHT:
                posX[0] = posX[0] + GameConstants.UNIT_SIZE;
                break;
            default:
                throw new RuntimeException("Should never happen");
        }
    }

    /**
     * Change the movement diraction of the snake.
     *
     * @param direction the direction in which the snake will move
     */
    public void changeDirection(Direction direction) {
        switch (direction) {
            case UP:
                if (this.direction != Direction.DOWN) {
                    if (this.direction != Direction.UP) {
                        this.movesMade++;
                    }
                    this.direction = Direction.UP;
                }
                break;
            case DOWN:
                if (this.direction != Direction.UP) {
                    if (this.direction != Direction.DOWN) {
                        this.movesMade++;
                    }
                    this.direction = Direction.DOWN;
                }
                break;
            case LEFT:
                if (this.direction != Direction.RIGHT) {
                    if (this.direction != Direction.LEFT) {
                        this.movesMade++;
                    }
                    this.direction = Direction.LEFT;
                }
                break;
            case RIGHT:
                if (this.direction != Direction.LEFT) {
                    if (this.direction != Direction.RIGHT) {
                        this.movesMade++;
                    }
                    this.direction = Direction.RIGHT;
                }
                break;
            default:
                break;
        }
    }

    /**
     * Draws the snake on the game board.
     *
     * @param g used to draw the snake
     */
    public void draw(JPanel game, Graphics g) {
        ImageIcon snakeBodyIcon = new ImageIcon("images/snake_body.jpg");
        Image snakeBody = snakeBodyIcon.getImage();
        snakeBody = snakeBody.getScaledInstance(GameConstants.UNIT_SIZE,
                GameConstants.UNIT_SIZE,
                Image.SCALE_DEFAULT);
        ImageIcon scaledsnakeBodyIcon = new ImageIcon(snakeBody);

        ImageIcon snakeHeadIcon;
        switch (direction) {
            case LEFT:
                snakeHeadIcon = new ImageIcon("images/snake_head_left.png");
                break;
            case RIGHT:
                snakeHeadIcon = new ImageIcon("images/snake_head_right.png");
                break;
            case UP:
                snakeHeadIcon = new ImageIcon("images/snake_head_up.png");
                break;
            case DOWN:
                snakeHeadIcon = new ImageIcon("images/snake_head_down.png");
                break;
            default:
                snakeHeadIcon = new ImageIcon("images/snake_head_left.png");
                break;
        }
        Image snakeHead = snakeHeadIcon.getImage();
        snakeHead = snakeHead.getScaledInstance(GameConstants.UNIT_SIZE,
                GameConstants.UNIT_SIZE,
                Image.SCALE_DEFAULT);
        ImageIcon scaledSnakeHeadIcon = new ImageIcon(snakeHead);
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                // g.setColor(Color.green);
                // g.fillRect(posX[i], posY[i], GameConstants.UNIT_SIZE,
                // GameConstants.UNIT_SIZE);
                scaledSnakeHeadIcon.paintIcon(game, g, posX[i], posY[i]);
            } else {
                // g.setColor(new Color(45, 180, 0));
                // g.fillRect(posX[i], posY[i], GameConstants.UNIT_SIZE,
                // GameConstants.UNIT_SIZE);
                scaledsnakeBodyIcon.paintIcon(game, g, posX[i], posY[i]);
            }
        }
    }

    /**
     * Check wheter snake has collided either with itself or one of the edge of the
     * board.
     *
     * @return true if there is a colision, otherwise false
     */
    public boolean checkColision(ArrayList<Point> map) {
        // Snake hit itself
        for (int i = length; i > 0; i--) {
            if ((posX[0] == posX[i]) && (posY[0] == posY[i])) {
                return true;
            }
        }
        // check if head touches left border
        if (posX[0] < 0) {
            return true;
        }
        // check if head touches right border
        if (posX[0] > GameConstants.SCREEN_WIDTH - (GameConstants.UNIT_SIZE)) {
            return true;
        }
        // check if head touches top border
        if (posY[0] < 0) {
            return true;
        }
        // check if head touches bottom border
        if (posY[0] > GameConstants.SCREEN_HEIGHT - 2 * (GameConstants.UNIT_SIZE)) {
            return true;
        }

        // check if snake hits an obstacle
        for (Point point : map) {
            if (posX[0] == (int) point.getX() && posY[0] == (int) point.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if apple is eaten.
     *
     * @param appleX x coordinate of the apple
     * @param appleY y coordinate of the apple
     * @return true if snake has eaten the apple, otherwise false
     */
    public boolean appleEaten(int appleX, int appleY) {
        for (int i = length; i > 0; i--) {
            if ((posX[i] == appleX) && posY[i] == appleY) {
                return true;
            }
        }
        return false;
    }

    /**
     * When the snake eats apples the applesEaten counter and the snake's length
     * increment.
     */
    public void eatApple() {
        applesEaten++;
        scoreChangeListener.onVariableChange(applesEaten);
        length++;
    }

    /**
     * Reset the properties of the snake.
     */
    public void reset() {
        length = 10;
        applesEaten = 0;
        scoreChangeListener.onVariableChange(applesEaten);
        movesMade = 0;
        direction = Direction.RIGHT;
        posX = new int[GameConstants.GAME_UNITS];
        posY = new int[GameConstants.GAME_UNITS];
    }
}

enum Direction {
    RIGHT,
    LEFT,
    UP,
    DOWN
}
