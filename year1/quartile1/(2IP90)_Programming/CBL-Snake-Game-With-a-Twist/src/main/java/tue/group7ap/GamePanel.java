package tue.group7ap;

import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.*;

/**
 * The GamePanel is where the main game happens.
 */
public class GamePanel extends JPanel implements ActionListener {
    public Snake snake;
    Apple apple;
    boolean running = false;
    private LevelManager levelManager;
    private Level currentLevel;
    private Queue<Direction> directionQueue = new LinkedList<>();
    private boolean keyboardInputEnabled = true;
    Timer timer;
    ScoreChangeListener scoreChangeListener;
    JFrame frame;

    /**
     * Constructor method for the GamePanel class.
     *
     * @param snake        the snake object
     * @param apple        the apple object
     * @param levelManager manages the levels
     */
    GamePanel(Snake snake, Apple apple, LevelManager levelManager) {
        setPreferredSize(
                new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        this.apple = apple;
        this.snake = snake;
        this.levelManager = levelManager;
        currentLevel = levelManager.getCurrentLevel();
        start();
    }

    GamePanel(Snake snake, Apple apple, LevelManager levelManager, JFrame frame) {
        setPreferredSize(
                new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new MyKeyAdapter());
        this.frame = frame;
        this.apple = apple;
        this.snake = snake;
        this.levelManager = levelManager;
        currentLevel = levelManager.getCurrentLevel();
        start();
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    /**
     * Generate coordinates for the apple and check whether they are valid.
     */
    public void generateApple() {
        apple.generate(1, GameConstants.GAME_WIDTH - 1, 1, GameConstants.GAME_HEIGHT - 1);
        int applePosX = apple.x * GameConstants.UNIT_SIZE;
        int applePosY = apple.y * GameConstants.UNIT_SIZE;
        while (snake.appleEaten(applePosX, applePosY)) {
            apple.generate(1, GameConstants.GAME_WIDTH - 1, 1, GameConstants.GAME_HEIGHT - 1);
        }
        boolean isPositionValid = false;

        while (!isPositionValid) {
            boolean appleInObstackle = false;
            for (Point obstackle : levelManager.getCurrentLevel().getObstacles()) {
                if (apple.x * GameConstants.UNIT_SIZE == obstackle.getX()
                        && apple.y * GameConstants.UNIT_SIZE == obstackle.getY()) {
                    appleInObstackle = true;
                }
            }
            if (!appleInObstackle) {
                isPositionValid = true;
            } else {
                apple.generate(1, GameConstants.GAME_WIDTH - 1, 1, GameConstants.GAME_HEIGHT - 1);
            }
        }

        if (currentLevel.getType() == LevelType.Maze) {
            apple.x = GameConstants.GAME_WIDTH - 2;
            apple.y = GameConstants.GAME_HEIGHT - 3;
        }
    }

    /**
     * Start the game.
     */
    public void start() {
        generateApple();
        levelManager.reset();
        snake.reset();
        running = true;
        timer = new Timer(GameConstants.DELAY, this);
        timer.start();
        startLevel(currentLevel);
    }

    /**
     * Extend the super paintComponent method.
     * Add background and draw components.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        ImageIcon backgroundIcon = new ImageIcon("background.jpg");
        Image background = backgroundIcon.getImage();
        background = background.getScaledInstance(GameConstants.SCREEN_WIDTH,
                GameConstants.SCREEN_HEIGHT,
                Image.SCALE_DEFAULT);
        ImageIcon scaledbackgroundIcon = new ImageIcon(background);
        scaledbackgroundIcon.paintIcon(this, g, 0, 0);
        draw(g);
    }

    /**
     * Draw the components.
     *
     * @param g Graphics object used to draw on the component.
     */
    public void draw(Graphics g) {
        if (running) {
            MapManager.drawMap(this, g, levelManager.getCurrentLevel().getObstacles());
            snake.draw(this, g);
            apple.draw(this, g);
            drawLevelInfo(g);
            drawGoal(g);
        } else {
            // game over
        }
    }

    /**
     * Draw the goal of the level.
     *
     * @param g is used to draw
     */
    private void drawGoal(Graphics g) {
        Level currentLevel = levelManager.getCurrentLevel();
        LevelType currentLevelType = currentLevel.getType();
        int goal = currentLevel.getGoal();
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        String text;
        switch (currentLevelType) {
            case ScoreTarget:
                text = "Target Score: " + snake.applesEaten + " / " + goal;
                break;
            case LimitedMoves:
                text = Integer.toString(goal - snake.movesMade) + " moves left";
                break;

            case LimitedTime:
                text = Integer.toString(currentLevel.getRemainingTime()) + " seconds left";
                break;
            case Maze:
                text = "Solve the maze.";
                break;
            default:
                text = "";
                break;
        }
        int textPosX = (GameConstants.SCREEN_WIDTH - metrics.stringWidth(text)) / 2;
        int textPosY = g.getFont().getSize();
        g.drawString(text, textPosX, textPosY);
    }

    /**
     * Check whether the snake has eaten the apple.
     */
    public void checkApple() {
        int applePosX = apple.x * GameConstants.UNIT_SIZE;
        int applePosY = apple.y * GameConstants.UNIT_SIZE;
        if (snake.appleEaten(applePosX, applePosY)) {
            generateApple();
            snake.eatApple();
        }

    }

    /**
     * Check whether there is a colision of the snake.
     *
     * @return true is snake has colided, otherwise false
     */
    public boolean checkColision() {
        if (snake.checkColision(levelManager.getCurrentLevel().getObstacles())) {
            return true;
        }

        return false;
    }

    /**
     * Game Over.
     */
    public void gameOver() {

        frame.removeAll();
        frame.setVisible(false);
        EndFrame endFrame = new EndFrame(false);

        // scoreChangeListener.booleanVariableChange(running);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            int currentSpeed = (currentLevel != null) ? currentLevel.getSpeed() : 1;
            timer.setDelay(GameConstants.DELAY - (int) (currentSpeed * 0.1 * GameConstants.DELAY));
            // move snake
            if (!directionQueue.isEmpty()) {
                Direction newDirection = directionQueue.poll();
                snake.changeDirection(newDirection);
            }
            snake.move();
            checkApple();
            if (snakeHasLostLevel()) {

                if (snake.hp > 1) {
                    snake.hp--;
                    snake.scoreChangeListener.onVariableChange(snake.hp);
                    snake.reset();
                } else {
                    gameOver();
                }
            }
            if (snakeHasCompletedLevel()) {
                if (levelManager.isFinalLevel()) {
                    System.out.println("you won");
                    frame.removeAll();
                    frame.setVisible(false);
                    EndFrame endFrame = new EndFrame(true);

                } else {
                    levelManager.goToNextLevel();
                    currentLevel = levelManager.getCurrentLevel();
                    int oldHP = snake.hp;
                    if (oldHP < GameConstants.HP) {
                        oldHP++;
                    }
                    currentLevel.startTimer();
                    snake.reset();
                    snake.hp = oldHP;
                    snake.scoreChangeListener.onVariableChange(oldHP);
                    startLevel(currentLevel);
                }
            }

        }

        if (!running) {
            timer.stop();
        }

        repaint();
    }

    /**
     * Start new level.
     *
     * @param level the level that is to be started.
     */
    private void startLevel(Level level) {
        keyboardInputEnabled = false;
        timer.stop();
        displayLevelInformation(level.getType(), level.getGoal());

        Timer inforTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                hideLevelInformation();

                keyboardInputEnabled = true;

                timer.start();

                ((Timer) e.getSource()).stop();
            }
        });

        inforTimer.start();
    }

    private String levelInformation = "";

    private void drawLevelInfo(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString(levelInformation,
                (GameConstants.SCREEN_WIDTH - metrics.stringWidth(levelInformation)) / 2,
                (GameConstants.SCREEN_HEIGHT - metrics.getHeight()) / 2);
    }

    private void displayLevelInformation(LevelType type, int goal) {
        switch (type) {
            case ScoreTarget:
                levelInformation = "Eat " + goal + " apples.";
                break;
            case LimitedMoves:
                levelInformation = "Eat an apple using no more than " + goal + " moves.";
                break;
            case LimitedTime:
                levelInformation = "Eat an apple in " + goal + " seconds";
                break;
            default:
                levelInformation = "";
                break;
        }
    }

    private void hideLevelInformation() {
        levelInformation = "";
    }

    /**
     * Check if snake has completed the level.
     *
     * @return true is level completed, otherwise false
     */
    private boolean snakeHasCompletedLevel() {
        Level currentLevel = levelManager.getCurrentLevel();
        LevelType currentLevelType = currentLevel.getType();
        int goal = currentLevel.getGoal();
        switch (currentLevelType) {
            case ScoreTarget:
                return snake.applesEaten == goal;
            case LimitedMoves:
                return snake.applesEaten == 1;
            case LimitedTime:
                return snake.applesEaten == 1;
            case Maze:
                return snake.applesEaten == 1;
            default:
                break;
        }
        return false;
    }

    /**
     * Check is the snake has lost the level.
     *
     * @return true if level is lost, false otherwise
     */
    private boolean snakeHasLostLevel() {
        Level currentLevel = levelManager.getCurrentLevel();
        LevelType currentLevelType = currentLevel.getType();
        int goal = currentLevel.getGoal();
        if (currentLevelType == LevelType.LimitedMoves) {
            if (snake.movesMade > goal) {
                return true;
            }
        }
        if (currentLevelType == LevelType.LimitedTime) {
            if (currentLevel.getRemainingTime() == 0) {
                return true;
            }
        }
        if (checkColision()) {
            return true;
        }
        return false;
    }

    /**
     * Custom key adapter for listening to user input.
     */
    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            Direction newDirection = null;
            if (keyboardInputEnabled) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        newDirection = Direction.LEFT;
                        break;
                    case KeyEvent.VK_RIGHT:
                        newDirection = Direction.RIGHT;
                        break;
                    case KeyEvent.VK_UP:
                        newDirection = Direction.UP;
                        break;
                    case KeyEvent.VK_DOWN:
                        newDirection = Direction.DOWN;
                        break;
                    default:
                        break;
                }
            }

            if (newDirection != null) {
                directionQueue.add(newDirection);
            }
        }
    }

}
