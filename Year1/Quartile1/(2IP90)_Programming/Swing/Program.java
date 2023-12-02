import javax.swing.*;

public class Program {
    JFrame mainFrame;
    JButton button;

    class MyButton extends JButton {
    }

    Program() {
        // creating instance of JFrame
        mainFrame = new JFrame();

        // creating instance of JButton
        button = new JButton("click");

        // x axis, y axis, width, height
        button.setBounds(130, 100, 100, 40);

        // adding button in JFrame
        mainFrame.add(button);

        // 400 width and 500 height
        mainFrame.setSize(400, 500);
        // using no layout managers
        mainFrame.setLayout(null);
        // making the frame visible
        mainFrame.setVisible(true);

    }

    public static void main(String[] args) {
        new Program();
    }
}