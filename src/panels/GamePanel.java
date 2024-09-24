package panels;

import frames.GameFrame;
import models.Ghost;
import models.Pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePanel extends JPanel {

    private JPanel[][] panels;
    private Dimension boardSize;
    private boolean[][] walls;
    private Image finalPointImg;
    private Image finalUpgradeImg;
    private Pacman pacman;
    private Ghost[] ghosts;
    private boolean[][] points;
    private boolean[][] upgrades;
    private int score = 0;
    private GameFrame gameFrame;
    private int remainingPoints;

    public GamePanel(Dimension boardSize, GameFrame gameFrame) {

        this.boardSize = boardSize;
        this.walls = new boolean[boardSize.height][boardSize.width];
        this.points = new boolean[boardSize.height][boardSize.width];
        this.upgrades = new boolean[boardSize.height][boardSize.width];
        this.gameFrame = gameFrame;
        this.remainingPoints = 0;

        setLayout(new GridLayout(boardSize.height, boardSize.width));
        panels = new JPanel[boardSize.height][boardSize.width];
        ImageIcon initialPointIcon = new ImageIcon("content/point.png");
        Image pointImg = initialPointIcon.getImage();
        finalPointImg = pointImg.getScaledInstance(20,20,Image.SCALE_DEFAULT);
        ImageIcon initialUpgradeIcon = new ImageIcon("content/upgrade.png");
        Image upgradeImg = initialUpgradeIcon.getImage();
        finalUpgradeImg = upgradeImg.getScaledInstance(20,20,Image.SCALE_DEFAULT);
        pacman = new Pacman(boardSize, this, gameFrame);
        ghosts = new Ghost[4];
        for (int i = 0; i < ghosts.length; i++) {
            ghosts[i] = new Ghost(boardSize, this);
        }

        for (int i = 0; i < boardSize.height; i++) {
            for (int j = 0; j < boardSize.width; j++) {
                JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
                panels[i][j] = panel;
                panels[i][j].setBackground(Color.BLACK);
                add(panel);

            }
        }

        for (int i = 3; i < boardSize.height; i++) {
            if(i < boardSize.height / 2 + 1 || i > boardSize.height / 2 + 1) { //create side walls with gap
                setPanel(i,0, Color.BLUE);
                setPanel(i, boardSize.width - 1, Color.BLUE);
            }
            if (i == boardSize.height / 2 || i == boardSize.height / 2 + 2) { //add walls for entrance
                setPanel(i, 1, Color.BLUE);
                setPanel(i, boardSize.width -2, Color.BLUE);
            }
        }

        for (int j = 0; j < boardSize.width; j++) { //create top and bottom walls
            setPanel(3,j,Color.BLUE);
            setPanel(boardSize.height - 1, j, Color.BLUE);
        }

        for (int i = boardSize.height / 2 - 1; i <= boardSize.height / 2 + 3; i++) { //create spawn area for ghosts
            for (int j = boardSize.width / 2 - 2; j <= boardSize.width / 2 + 2; j++) {
                setPanel(i, j, Color.BLUE);
            }
        }

        for (int i = boardSize.height / 2; i <= boardSize.height / 2 + 2; i++) { //make room inside spawn area
            for (int j = boardSize.width / 2 - 1; j <= boardSize.width / 2 + 1; j++) {
                setPanel(i, j, Color.DARK_GRAY);
            }
        }

        for (int j = boardSize.width / 2 - 1; j <= boardSize.width/ 2 + 1; j++) { //create exit
            setPanel(boardSize.height / 2 - 1, j, Color.CYAN);
        }

        generateWalls();
        fillBoardPoints();
        revalidate();
        repaint();

        setFocusable(true);
        requestFocusInWindow();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        pacman.setDir(-1, 0);
                        break;
                    case KeyEvent.VK_DOWN:
                        pacman.setDir(1,0);
                        break;
                    case KeyEvent.VK_RIGHT:
                        pacman.setDir(0,1);
                        break;
                    case KeyEvent.VK_LEFT:
                        pacman.setDir(0,-1);
                        break;
                }
            }
        });

        new Thread(() -> {
            while (true) {
                spawnAndUpdatePacman();
                try {
                    Thread.sleep(pacman.getSpeed());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        for (Ghost ghost : ghosts) {
            new Thread(() -> {
                while (true) {
                    spawnAndUpdateGhost(ghost);
                    try {
                        Thread.sleep(ghost.getSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    public JPanel getPanel(int row, int col) {
        return panels[row][col];
    }

    public void setPanel(int row, int col, Color color) {
        panels[row][col].setBackground(color);
        if(color.equals(Color.BLUE)) {
            walls[row][col] = true;
        }
    }

    public void placeUpgrade() {
        String[] labels = {"speedboost", "freeze", "flash", "slowness", "life"}; //our 5 upgrades
        int[] rows = {7, 7, boardSize.height / 2 + 4, boardSize.height - 4, boardSize.height / 2 + 4};
        int[] cols = {1, boardSize.width - 2, 1, boardSize.width - 2, boardSize.width / 2};

        for (int i = 0; i < rows.length; i++) {
            JLabel upgradeImgLabel = new JLabel();
            upgradeImgLabel.setIcon(new ImageIcon(finalUpgradeImg));
            upgradeImgLabel.setName(labels[i]);
            panels[rows[i]][cols[i]].add(upgradeImgLabel);
            upgrades[rows[i]][cols[i]] = true;
        }
    }

    public boolean isWall(int row, int col) {
        return walls[row][col];
    }


    public void placeShape(int[][] shape, int firstX, int firstY, Color color) {
        for (int i = 0; i < shape.length; i++) {
            for (int j = 0; j < shape[0].length; j++) {
                if (shape[i][j] == 1) {
                    setPanel(i + firstX, j + firstY, color);
                }
            }
        }
    }

    public void generateWalls() {
        if (boardSize.equals(new Dimension(28, 31))) {
            placeShape(pieceOne, 4,1, Color.BLUE);
            placeShape(pieceTwo, 19, 1, Color.BLUE);
            placeShape(pieceThree, 13, 2, Color.BLUE);
            placeShape(pieceFour,13, 17, Color.BLUE);
            placeUpgrade();

        } else if (boardSize.equals(new Dimension(21, 22))) {
            placeShape(cornerOne, 5, 2, Color.BLUE);
            placeShape(cornerTwo, 5, 14, Color.BLUE);
            placeShape(middleOne, 4, 8, Color.BLUE);
            placeShape(sideOne, 11, 3, Color.BLUE);
            placeShape(sideTwo, 11, 14, Color.BLUE);
            placeShape(cornerThree, 15, 2, Color.BLUE);
            placeShape(cornerFour, 15,14, Color.BLUE);
            placeShape(middleTwo, 16,8, Color.BLUE);
            placeUpgrade();

        } else if (boardSize.equals(new Dimension(19,37))) {
            placeShape(pieceThree,4,1,Color.BLUE);
            placeShape(pieceThree,4,10,Color.BLUE);
            placeShape(pieceThree, 10, 1, Color.BLUE);
            placeShape(pieceThree, 10, 10, Color.BLUE);
            placeShape(pieceThree, 22, 1, Color.BLUE);
            placeShape(pieceThree, 22, 10, Color.BLUE);
            placeShape(pieceFive, 28, 1, Color.BLUE);
            placeShape(pieceFive, 28, 10, Color.BLUE);
            placeShape(pieceSix, 16,2,Color.BLUE);
            placeShape(pieceSix, 16,12,Color.BLUE);
            placeUpgrade();
        } else if (boardSize.equals(new Dimension(30,40))) {
            placeShape(pieceFive,4,1,Color.BLUE);
            placeShape(pieceFive,4,10,Color.BLUE);
            placeShape(pieceFive,4,19,Color.BLUE);
            placeShape(pieceFive,11,1,Color.BLUE);
            placeShape(pieceFive,11,10,Color.BLUE);
            placeShape(pieceFive,11,19,Color.BLUE);
            placeShape(pieceFive,24,1,Color.BLUE);
            placeShape(pieceFive,24,10,Color.BLUE);
            placeShape(pieceFive,24,19,Color.BLUE);
            placeShape(pieceFive,31,1,Color.BLUE);
            placeShape(pieceFive,31,10,Color.BLUE);
            placeShape(pieceFive,31,19,Color.BLUE);
            placeShape(pieceSeven, 18, 2, Color.BLUE);
            placeShape(pieceEight, 18, 18, Color.BLUE);
            placeUpgrade();
        }
    }

    public void fillBoardPoints() {
        for (int i = 4; i < boardSize.height; i++) {
            for (int j = 0; j < boardSize.width; j++) {
                JPanel panel = panels[i][j];
                if (panel.getBackground().equals(Color.BLACK) && !upgrades[i][j]) {
                    JLabel pointImgLabel = new JLabel();
                    pointImgLabel.setIcon(new ImageIcon(finalPointImg));
                    panel.add(pointImgLabel);
                    points[i][j] = true;
                    remainingPoints++;

                }
            }
        }
    }

    public void spawnAndUpdatePacman() {
        JLabel pacmanLabel = new JLabel(new ImageIcon(pacman.getCurrentPacmanImg()));

        int oldX = pacman.getX();
        int oldY = pacman.getY();

        JPanel oldPanel = panels[oldX][oldY];
        oldPanel.removeAll();
        oldPanel.revalidate();
        oldPanel.repaint();

        pacman.update();
        checkCollision();

        int newX = pacman.getX();
        int newY = pacman.getY();

        if (points[newX][newY]) {
            points[newX][newY] = false;
            score += 1;
            remainingPoints--;
            gameFrame.updateScore(score);
            gameFrame.setScore(score);

            if (remainingPoints == 0) {
                gameFrame.endGame(score);
            }
        }

        try {
            if (upgrades[newX][newY]) {
                upgrades[newX][newY] = false;
                score += 10;
                gameFrame.updateScore(score);
                gameFrame.setScore(score);

                JLabel label = null;
                Component component = panels[newX][newY].getComponent(0);
                if (component instanceof JLabel) {
                    label = (JLabel) component;

                }

                pacman.makeStrong(label.getName());
                for (Ghost ghost : ghosts) {
                    ghost.makeWeak(label.getName());
                }

            }
        } catch (NullPointerException e) {
            System.out.println("Caught NullPointerException in upgrades: " + e.getMessage());
        }

        JPanel currentPanel = panels[newX][newY];
        currentPanel.removeAll();
        currentPanel.add(pacmanLabel);
        currentPanel.revalidate();
        currentPanel.repaint();
    }

    public void spawnAndUpdateGhost(Ghost ghost) {
        JLabel ghostLabel = new JLabel(new ImageIcon(ghost.getCurrentGhostImg()));

        int oldX = ghost.getX();
        int oldY = ghost.getY();

        JPanel oldPanel = panels[oldX][oldY];
        oldPanel.removeAll();

        if (points[oldX][oldY]) {
            oldPanel.add(new JLabel(new ImageIcon(finalPointImg)));
        }
        if (upgrades[oldX][oldY]) {
            oldPanel.add(new JLabel(new ImageIcon(finalUpgradeImg)));
        }

        oldPanel.revalidate();
        oldPanel.repaint();

        ghost.update();
        checkCollision();

        int newX = ghost.getX();
        int newY = ghost.getY();

        JPanel currentPanel = panels[newX][newY];
        currentPanel.removeAll();
        currentPanel.add(ghostLabel);
        currentPanel.revalidate();
        currentPanel.repaint();
    }

    public void checkCollision() {
        for (Ghost ghost : ghosts) {
            if (pacman.getX() == ghost.getX() && pacman.getY() == ghost.getY()) {
                if (ghost.isWeak()) {
                    ghost.respawn();
                    score += 100;
                    gameFrame.updateScore(score);
                    gameFrame.setScore(score);
                } else {
                    pacman.respawn();
                    gameFrame.removeLife();
                }
            }
        }
    }

    int[][] cornerOne = {
            {1, 1, 0, 1, 1},
            {0, 0, 0, 1, 1},
            {1, 1, 0, 0, 0},
            {1, 1, 0, 1, 1},
            {1, 1, 0, 1, 1}
    };
    int[][] cornerTwo = {
            {1, 1, 0, 1, 1},
            {1, 1, 0, 0, 0},
            {0, 0, 0, 1, 1},
            {1, 1, 0, 1, 1},
            {1, 1, 0, 1, 1},
    };
    int[][] cornerThree = {
            {1, 0, 1, 1, 1},
            {1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1},
            {1, 0, 0, 0, 0},
            {1, 0, 1, 1, 1},
    };
    int[][] cornerFour = {
            {1, 1, 1, 0, 1},
            {1, 0, 0, 0, 1},
            {1, 0, 1, 0, 1},
            {0, 0, 0, 0, 1},
            {1, 1, 1, 0, 1},
    };
    int[][] middleOne = {
            {0, 0, 1, 0, 0},
            {1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1},
            {1, 0, 0, 0, 1},
            {1, 1, 0, 1, 1},
    };
    int[][] middleTwo = {
            {1, 0, 0, 0, 1},
            {0, 0, 1, 0, 0},
            {1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1},
            {1, 0, 0, 0, 1},
    };
    int[][] sideOne = {
            {1, 1, 0, 1},
            {1, 1, 0, 0},
            {1, 1, 0, 1},
    };
    int[][] sideTwo = {
            {1, 0, 1, 1},
            {0, 0, 1, 1},
            {1, 0, 1, 1},
    };

    int[][] pieceOne = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0},
            {0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    int[][] pieceTwo = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 1, 0, 1, 1, 1, 0, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 1, 1, 0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0},
            {0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0},
            {0, 1, 0, 1, 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    int[][] pieceThree = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    int[][] pieceFour = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 0, 1, 1, 0},
            {0, 1, 0, 1, 1, 0, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 1, 1, 0},
            {0, 1, 1, 1, 1, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    int[][] pieceFive = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 0, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    int[][] pieceSix = {
            {0, 0, 0, 0, 0},
            {0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 1, 0, 0, 0},
            {0, 1, 0, 1, 0},
            {0, 0, 0, 0, 0},
    };

    int[][] pieceSeven = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 1, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 0, 0, 1, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 0, 1, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };

    int[][] pieceEight = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 1, 1, 0, 1, 0, 1, 0},
            {0, 1, 0, 0, 0, 0, 1, 0, 0, 0},
            {0, 0, 0, 1, 1, 0, 0, 0, 1, 0},
            {0, 1, 1, 1, 1, 1, 1, 0, 1, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    };


}
