package models;

import panels.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ghost {
    private Image normalGhostImage;
    private Image weakGhostImage;
    private int x, y;
    private int dirx, diry;
    private Dimension boardSize;
    private GamePanel gamePanel;
    private Random random = new Random();
    private boolean isWeak = false;
    private String upgradeName = null;
    private int speed = 175;

    public Ghost(Dimension boardSize, GamePanel gamePanel) {
        this.boardSize = boardSize;
        this.gamePanel = gamePanel;
        x = boardSize.height / 2 + 2;
        y = boardSize.height / 2;

        ImageIcon normalGhostIcon = new ImageIcon("content/ghost1.png");
        normalGhostImage = normalGhostIcon.getImage().getScaledInstance(20, 20, Image.SCALE_DEFAULT);

        ImageIcon weakGhostIcon = new ImageIcon("content/ghost2.png");
        weakGhostImage = weakGhostIcon.getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT);

        List<int[]> directions = new ArrayList<>();
        directions.add(new int[]{-1,0});
        directions.add(new int[]{1,0});
        directions.add(new int[]{0,1});
        directions.add(new int[]{0,-1});
        dirx = 0;
        diry = -1;
    }

    public void update() {
        List<int[]> availableOptions = getAvailableOptions();

        if (availableOptions.size() == 1 && availableOptions.get(0)[0] == dirx && availableOptions.get(0)[1] == -diry) {
            dirx = -dirx;
            diry = -diry;
        } else {
            if (random.nextDouble() < 0.65 && isValidMove(x+dirx, y+diry)) {

            } else {
                int[] newDir = availableOptions.get(random.nextInt(availableOptions.size()));
                dirx = newDir[0];
                diry = newDir[1];
            }
        }

        x += dirx;
        y += diry;

        if (x < 0) {
            x = boardSize.height - 1;
        } else if (x >= boardSize.height) {
            x = 0;
        }

        if (y < 0) {
            y = boardSize.height - 1;
        } else if (y >= boardSize.height) {
            y = 0;
        }

    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private boolean isValidMove(int nextX, int nextY) {
        if(nextY < 0 || nextY >= boardSize.width) {
            return false;
        }

        Color panelColor = gamePanel.getPanel(nextX, nextY).getBackground();
        return !panelColor.equals(Color.BLUE);
    }

    private List<int[]> getAvailableOptions() {
        List<int[]> options = new ArrayList<>();
        int[][] directions = {{-1,0}, {1,0}, {0,1}, {0,-1}};

        for (int[] direction : directions) {
            if (isValidMove(x + direction[0], y + direction[1])) {
                options.add(direction);
            }
        }

        return options;
    }

    public void makeWeak(String upgradeName) {
        isWeak = true;
        this.upgradeName = upgradeName;

        switch (upgradeName) {
            case "freeze":
                setSpeed(2000);
                break;
            case "slowness":
                setSpeed(800);
                break;
        }

        new Thread(() -> {
            try {
                Thread.sleep(8000);
                makeNormal();
            } catch (InterruptedException e) {
                setSpeed(175);
            }
        }).start();
    }

    public void makeNormal() {
        isWeak = false;
        this.upgradeName = null;
        setSpeed(175);
    }

    public Image getCurrentGhostImg() {
        if (isWeak) {
            return weakGhostImage;
        } else {
            return normalGhostImage;
        }
    }

    public boolean isWeak() {
        return isWeak;
    }

    public void respawn() {
        x = boardSize.height / 2 + 2;
        y = boardSize.height / 2;
        isWeak = false;
    }

    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }

    public int getSpeed() {
        return speed;
    }

}
