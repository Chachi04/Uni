package tue.group7ap;

import javax.swing.SwingUtilities;

/**
 * This is where the program is run.
 */
public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameFrame gameFrame = new GameFrame();
                gameFrame.show();
            }
        });
    }
}