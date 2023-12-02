package tue.group7ap;

import java.awt.Point;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Class representing a level.
 */
public class Level {
    private ArrayList<Point> obstacles;
    private int speed;
    private LevelType type;
    private int scoreTarget;
    private int maxMoves;
    private int maxTime;

    private int remainingTime; // in seconds
    private Timer timer;

    /**
     * Constructor method for level class.
     *
     * @param speed     the speed of the snake
     * @param obstacles array of obstacle locations
     * @param type      type of level
     * @param goal      goal of level
     */
    public Level(int speed, ArrayList<Point> obstacles, LevelType type, int goal) {
        this.speed = speed;
        this.obstacles = obstacles;
        this.type = type;
        switch (type) {
            case ScoreTarget:
                this.scoreTarget = goal;
                break;
            case LimitedMoves:
                this.maxMoves = goal;
                break;
            case LimitedTime:
                this.maxTime = goal;
                break;
            default:
                break;
        }
    }

    /**
     * Start the timer for level type limitedTime.
     */
    public void startTimer() {
        this.remainingTime = maxTime;

        // Create a Timer that fires an event every 1000 milliseconds (1 second)
        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTimer(); // Method to handle timer logic
            }
        });

        // Start the timer
        timer.start();

    }

    private void updateTimer() {
        if (remainingTime > 0) {
            remainingTime--;
            // Update the UI to display the remaining time to the player
        } else {
            // Trigger game over logic when time runs out
            timer.stop(); // Stop the timer
            // Implement game over actions
        }
    }

    public LevelType getType() {
        return this.type;
    }

    /**
     * Get the goal of the level.
     *
     * @return goal (an integer). It could be number of apples, seconds or moves
     *         depending on the level type.
     */
    public int getGoal() {
        switch (type) {
            case ScoreTarget:
                return this.scoreTarget;
            case LimitedMoves:
                return this.maxMoves;
            case LimitedTime:
                return this.maxTime;
            case Maze:
                return 1;
            default:
                return 0;
        }
    }

    public int getSpeed() {
        return speed;
    }

    public ArrayList<Point> getObstacles() {
        return obstacles;
    }

    public int getRemainingTime() {
        return remainingTime;
    }
}

enum LevelType {
    ScoreTarget,
    LimitedMoves,
    LimitedTime,
    Maze
}