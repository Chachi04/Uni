package tue.group7ap;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class EndFrame extends JFrame{
    private JFrame frame;
    JLabel label;
    boolean win;
    String message;

    EndFrame(boolean win) {
        
        //this.frame = new JFrame("Snake Game");
        //this.frame.setResizable(false);
        //this.frame.setSize(300, 300);
        //this.frame.setLocationRelativeTo(null);
        //this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.frame.setVisible(true);

        setResizable(false);
        setSize(300, 300);
        //setPreferredSize(new Dimension(300, 300));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        JPanel mainPanel = new JPanel();
        this.win = win;
        setMessage();
        mainPanel.setPreferredSize(new Dimension(300, 300));

        mainPanel.setBackground(Color.BLACK);
        mainPanel.setFocusable(true);
        this.add(mainPanel);
        label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 30));
        label.setForeground(Color.GREEN);
        
        mainPanel.add(label);
        mainPanel.setVisible(true);
    
    }

    public void setMessage() {
        if (win) {
            message = "YOU WON!";
        }
        else {
            message = "GAME OVER :(";
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponents(g);
        gameOver(g);
    }

    public void win(Graphics g) {
        g.drawString("congratulations", 150, 50);
    }

    public void gameOver(Graphics g) {
        repaint();
        System.out.println("you lost");
        g.setColor(Color.GREEN);
        g.drawRect(0, 0, 200, 200);
        g.drawString("GAME OVER!", GameConstants.SCREEN_WIDTH / 2, GameConstants.SCREEN_HEIGHT / 2);

        //repaint();
    }
    
}
