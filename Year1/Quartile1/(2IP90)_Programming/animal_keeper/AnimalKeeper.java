import java.util.*;

/**
 * Class AnimalKeeper.
 * Program entry point main().
 * Progarm event loop run().
 *
 * @author Sona Hyblova
 * @ID 2005050
 * @author Jiaqi Wang
 * @ID 1986619
 */
public class AnimalKeeper {
    Scanner scanner = new Scanner(System.in);

    void run() {
        try {
            MyZoo myZoo = new MyZoo();
            Command command;

            while (true) {
                command = Command.values()[scanner.nextInt()];
                myZoo.execute(command, scanner);
            }

        } catch (IndexOutOfBoundsException e) {
            // The program terminates.
        }
    }

    public static void main(String[] args) {
        new AnimalKeeper().run();
    }
}
