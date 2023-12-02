package tue.group7ap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    Snake snake;
    Apple apple;
    ArrayList<Point> currentMap;
    private boolean running = false;
    private LevelManager levelManager;
    Timer timer;

    GamePanel() {
        setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());

        snake = new Snake();
        apple = new Apple();
        levelManager = new LevelManager();
        start();
    }

    public void start() {
        apple.generate(1, GameConstants.GAME_WIDTH - 1, 1, GameConstants.GAME_HEIGHT - 1);
        running = true;
        timer = new Timer(GameConstants.DELAY, this);
        timer.start();
    }

    public void draw(Graphics g) {
        if (running) {
            // for (int i = 0; i < GameConstants.SCREEN_HEIGHT / GameConstants.UNIT_SIZE;
            // i++) {
            // for (int j = 0; j < GameConstants.SCREEN_WIDTH / GameConstants.UNIT_SIZE;
            // j++) {
            // g.drawLine(j * GameConstants.UNIT_SIZE, 0, j * GameConstants.UNIT_SIZE,
            // GameConstants.SCREEN_HEIGHT);
            // g.drawLine(0, i * GameConstants.UNIT_SIZE, GameConstants.SCREEN_WIDTH, i *
            // GameConstants.UNIT_SIZE);
            // }
            // }
            // remove for dinamic levels
            // MapManager.drawMap(g, levelManager.getCurrentLevel().getObstacles());
            // apple.draw(g);
            snake.draw(g);
            // foreach obstacle draw(g)
            apple.draw(g);
            // draw snake
            // draw obstacles
        } else {
            // game over
        }
    }

    public void checkApple() {
        if (snake.appleEaten(apple.x * GameConstants.UNIT_SIZE, apple.y * GameConstants.UNIT_SIZE)) {
            apple.generate(1, GameConstants.GAME_WIDTH - 1, 1, GameConstants.GAME_HEIGHT - 1);
            snake.eatApple();
        }
    }

    public void checkColision() {
        if (snake.checkColision(currentMap)) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver() {

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            // Get Current Level
            // Level currentLevel = levelManager.getCurrentLevel();
            // int currentSpeed = (currentLevel != null) ? currentLevel.getSpeed() : 1;
            // timer.setDelay(GameConstants.DELAY / currentSpeed);
            // move snake
            snake.move();
            checkApple();
            checkColision();

            if (snakeHasCompletedLevel()) {
                if (levelManager.isFinalLevel()) {
                    // Player has won
                } else {
                    levelManager.goToNextLevel();
                    currentMap = levelManager.getCurrentLevel().getObstacles();
                }
            }
        }
        repaint();
    }

    private boolean snakeHasCompletedLevel() {
        if (snake.applesEaten == 5) {
            return true;
        }
        return false;
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    snake.changeDirection(Direction.LEFT);
                    break;
                case KeyEvent.VK_RIGHT:
                    snake.changeDirection(Direction.RIGHT);
                    break;
                case KeyEvent.VK_UP:
                    snake.changeDirection(Direction.UP);
                    break;
                case KeyEvent.VK_DOWN:
                    snake.changeDirection(Direction.DOWN);
                    break;
            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
}
