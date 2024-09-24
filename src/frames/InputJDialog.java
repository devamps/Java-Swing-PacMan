package frames;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputJDialog extends JDialog {
    JTextField nameField = new JTextField(20);
    JButton saveButton = new JButton("SAVE SCORE");
    JButton returnButton = new JButton("RETURN TO MENU");
    private int score;

    public InputJDialog(JFrame frame, int score) {
        super(frame, "END OF GAME", true);
        this.score = score;
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        add(new JLabel("Enter your name to save score:"));
        add(nameField);
        add(saveButton);
        add(returnButton);

        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = nameField.getText();
                if (user.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(InputJDialog.this, "Cant save without username...");
                } else {
                    HighScoreFrame highScoreFrame = HighScoreFrame.getInstance();
                    highScoreFrame.addScore(user, score);
                    dispose();
                    new StartingFrame();
                }
            }
        });

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new StartingFrame();
            }
        });

        pack();
        setLocationRelativeTo(null);

    }

}
