package tue.group7ap;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * First frame after app launch.
 */
public class GameFrame {
    private JFrame frame;
    JLabel titleLabel;
    boolean inGame;
    Game game;
    // JFrame f;

    /**
     * Game contructor.
     */
    GameFrame() {
        game = new Game();
        initialize();
    }

    private void initialize() {
        this.frame = new JFrame("Snake Game");
        this.frame.setResizable(false);
        this.frame.setSize(300, 300);
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        titlePanel.setBounds(0, 0, 300, 150);
        titleLabel = new JLabel("Snake game", SwingConstants.CENTER);
        titlePanel.add(titleLabel);
        this.frame.add(titlePanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 20));
        JButton startButton = new JButton("Start Game");
        JButton manual = new JButton("?");

        manual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JDialog info = new JDialog(frame, "instructions");
                // info.setLayout(new FlowLayout(FlowLayout.CENTER, 1, 1));
                info.setLocationRelativeTo(null);
                info.setSize(300, 300);
                JTextArea instructions = new JTextArea(
                        "This is snake game. Created by Jiaqi Wang (1986619) and Sonya Hyblova (2005050) as our CBL project. \r\n"
                                + //
                                "Every one know the classic game snake. In this game of ours, though, we have built upon it and added different levels. \r\n"
                                + //
                                "There are levels that require you to eat certain number of apples, or eat an apple within less the a certain number of moves. \r\n"
                                + //
                                "Or eat an apple with a certain amount of time. The last level is a maze an to get to the apple you need to solve the maze. \r\n"
                                + //
                                "Have fun testing our game!");
                instructions.setLineWrap(true);
                instructions.setEditable(false);
                JScrollPane scroll = new JScrollPane(instructions);
                scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
                scroll.setViewportView(instructions);
                info.add(scroll);
                info.setVisible(true);
            }
        });
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // game.inGame = true;
                game.start();
            }
        });
        mainPanel.add(startButton);
        JPanel manuPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        manuPanel.add(manual);
        this.frame.add(mainPanel, BorderLayout.CENTER);
        this.frame.add(manuPanel, BorderLayout.SOUTH);
    }

    void show() {
        this.frame.setVisible(true);
    }

}
