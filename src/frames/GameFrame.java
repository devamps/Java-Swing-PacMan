package frames;

import panels.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {

    private JLabel scoreLabel = new JLabel("POINTS: 0   LIVES: 3");
    private JPanel lifePanel = new JPanel();
    private Image finalLifeImg;
    private int lives = 3;
    private int score;

    public GameFrame(Dimension boardSize) {

        GamePanel gamePanel = new GamePanel(boardSize, this);

        setLayout(new BorderLayout());

        gamePanel.setBackground(Color.BLACK);

        scoreLabel.setOpaque(true);
        scoreLabel.setFont(new Font("Dialog", Font.PLAIN, 24));
        scoreLabel.setBackground(Color.BLACK);
        scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setHorizontalAlignment(SwingConstants.CENTER);

        add(gamePanel, BorderLayout.CENTER);
        add(scoreLabel, BorderLayout.NORTH);
        setSize(boardSize.width * 20, boardSize.height * 20);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new StartingFrame();
                dispose();
            }
        });
    }

    public void updateScore(int score) {
        this.score = score;
        scoreLabel.setText("POINTS: " + score + "   LIVES: " + lives);
    }

    public void setScore(int score) {
        this.score = score;
    }


    public void removeLife() {
        lives--;
        if (lives < 1) {
            endGame(score);
        }
    }

    public void addLife() {
        lives++;
    }

    public void endGame(int score) {
        if (lives < 1) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                InputJDialog dialog = new InputJDialog(this, score);
                dialog.setVisible(true);
            });
        }
    }

}
