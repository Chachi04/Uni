package tue.group7ap;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test cases for the snake class.
 */
public class SnakeTest {

        Snake snake;
        Apple apple;
        LevelManager levelManager;
        ScorePanel scorePanel;

        @BeforeEach
        void setup() {
                apple = new Apple();
                snake = new Snake();
                levelManager = new LevelManager();
                scorePanel = new ScorePanel(snake, apple, levelManager);
                snake.setScoreListener(scorePanel);
        }

        @Test
        @DisplayName("Direction should not be able to be changed 180 degrees")
        void testDirectionChange180Degrees() {
                snake.direction = Direction.UP;
                snake.changeDirection(Direction.DOWN);
                assertEquals(Direction.UP, snake.direction,
                                "Snake will not go down if its direction was up before.");

                snake.direction = Direction.DOWN;
                snake.changeDirection(Direction.UP);
                assertEquals(Direction.DOWN, snake.direction,
                                "Snake will not go up if its direction was down before.");

                snake.direction = Direction.LEFT;
                snake.changeDirection(Direction.RIGHT);
                assertEquals(Direction.LEFT, snake.direction,
                                "Snake will not go right if its direction was up left.");

                snake.direction = Direction.RIGHT;
                snake.changeDirection(Direction.LEFT);
                assertEquals(Direction.RIGHT, snake.direction,
                                "Snake will not go left if its direction was up right.");
        }

        @Test
        @DisplayName("Direction change")
        void testDirectionChange90Degrees() {
                snake.direction = Direction.UP;
                snake.changeDirection(Direction.LEFT);
                assertEquals(Direction.LEFT, snake.direction,
                                "Snake goes left");
                snake.direction = Direction.UP;
                snake.changeDirection(Direction.RIGHT);
                assertEquals(Direction.RIGHT, snake.direction,
                                "Snake goes right");

                snake.direction = Direction.DOWN;
                snake.changeDirection(Direction.LEFT);
                assertEquals(Direction.LEFT, snake.direction,
                                "Snake goes left");
                snake.direction = Direction.DOWN;
                snake.changeDirection(Direction.RIGHT);
                assertEquals(Direction.RIGHT, snake.direction,
                                "Snake goes right");

                snake.direction = Direction.LEFT;
                snake.changeDirection(Direction.UP);
                assertEquals(Direction.UP, snake.direction,
                                "snake goes up");
                snake.direction = Direction.LEFT;
                snake.changeDirection(Direction.DOWN);
                assertEquals(Direction.DOWN, snake.direction,
                                "snake goes down");

                snake.direction = Direction.RIGHT;
                snake.changeDirection(Direction.UP);
                assertEquals(Direction.UP, snake.direction,
                                "snake goes up");
                snake.direction = Direction.RIGHT;
                snake.changeDirection(Direction.DOWN);
                assertEquals(Direction.DOWN, snake.direction,
                                "Snake goes down");
        }

        @Test
        @DisplayName("Eating an apple increments the applesEaten property")
        void testEatingApple() {
                snake.eatApple();
                assertEquals(1, snake.applesEaten, "Eating one apple");
        }
}
