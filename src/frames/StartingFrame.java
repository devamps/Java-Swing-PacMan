package frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

public class StartingFrame extends JFrame {

    public StartingFrame() {

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        String[] boardOptions = {"Standard", "Small", "Tower", "Slim", "Large"};

        JLabel titleText = new JLabel("P A C - M A N", JLabel.CENTER);
        ImageIcon initialPacmanLogo = new ImageIcon("content/pacmanlogo.png");
        JLabel boardSelectionLabel = new JLabel("Select Game Board:");
        JComboBox<String> boardSelection = new JComboBox<>(boardOptions);
        JButton startButton = new JButton("START GAME");
        JButton highScoresButton = new JButton("High-scores");
        JButton exitButton = new JButton("EXIT GAME");

        titleText.setFont(new Font("Dialog", Font.BOLD, 32));
        titleText.setForeground(Color.ORANGE);
        titleText.setAlignmentX(Component.CENTER_ALIGNMENT);

        Image pacmanLogoImg = initialPacmanLogo.getImage();
        Image scalePacmanLogoImg = pacmanLogoImg.getScaledInstance(150,150, Image.SCALE_SMOOTH);
        ImageIcon finalPacmanLogo = new ImageIcon(scalePacmanLogoImg);
        JLabel pacmanLogoLabel = new JLabel(finalPacmanLogo);
        pacmanLogoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        boardSelectionLabel.setFont(new Font("Dialog", Font.BOLD, 20));
        boardSelectionLabel.setForeground(Color.WHITE);
        boardSelectionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        boardSelection.setFont(new Font("Dialog", Font.BOLD,16));
        boardSelection.setForeground(Color.BLACK);
        boardSelection.setBackground(Color.WHITE);
        boardSelection.setAlignmentX(Component.CENTER_ALIGNMENT);
        boardSelection.setMaximumSize(new Dimension(250, boardSelection.getPreferredSize().height));

        startButton.setFont(new Font("Dialog", Font.BOLD, 24));
        startButton.setForeground(Color.WHITE);
        startButton.setBackground(Color.GREEN);
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        highScoresButton.setFont(new Font("Dialog", Font.BOLD, 14));
        highScoresButton.setForeground(Color.BLACK);
        highScoresButton.setBackground(Color.WHITE);
        highScoresButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        exitButton.setFont(new Font("SansSerif", Font.BOLD, 12));
        exitButton.setForeground(Color.WHITE);
        exitButton.setBackground(Color.RED);
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        HashMap<String, Dimension> boardSizes = new HashMap<>();
        boardSizes.put("Standard", new Dimension(28,31));
        boardSizes.put("Small", new Dimension(21,22));
        boardSizes.put("Tower", new Dimension(19,37));
        boardSizes.put("Slim", new Dimension(19,37));
        boardSizes.put("Large", new Dimension(30,40));

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String chosenBoard = (String) boardSelection.getSelectedItem();
                Dimension boardSize = boardSizes.get(chosenBoard);
                dispose();
                GameFrame gameFrame = new GameFrame(boardSize);
            }
        });

        highScoresButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                HighScoreFrame highScoreFrame = HighScoreFrame.getInstance();
                highScoreFrame.setVisible(true);
            }
        });

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        panel.add(Box.createVerticalStrut(20));
        panel.add(titleText);
        panel.add(Box.createVerticalStrut(5));
        panel.add(pacmanLogoLabel);
        panel.add(boardSelectionLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(boardSelection);
        panel.add(Box.createVerticalStrut(20));
        panel.add(startButton);
        panel.add(Box.createVerticalStrut(15));
        panel.add(highScoresButton);
        panel.add(Box.createVerticalStrut(30));
        panel.add(exitButton);

        panel.setBackground(Color.BLACK);
        add(panel);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

}
