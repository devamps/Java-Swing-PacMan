package models;

import frames.GameFrame;
import panels.GamePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;


public class Pacman {

    private Image[] pacmanImages;
    private int currentImage = 0;
    private int x, y;
    private int dirx, diry;
    private Dimension boardSize;
    private GamePanel gamePanel;
    private boolean isStrong = false;
    private String upgradeName = null;
    private int speed = 175;
    private GameFrame gameFrame;

    public Pacman(Dimension boardSize, GamePanel gamePanel, GameFrame gameFrame){
        this.boardSize = boardSize;
        this.gamePanel = gamePanel;
        this.gameFrame = gameFrame;
        x = boardSize.height - 2;
        y = 1;

        pacmanImages = new Image[3];

        ImageIcon firstPacmanIcon = new ImageIcon("content/pacman1.png");
        Image pacmanImg1 = firstPacmanIcon.getImage();
        pacmanImages[0] = pacmanImg1.getScaledInstance(20,20,Image.SCALE_DEFAULT);

        ImageIcon secondPacmanIcon = new ImageIcon("content/pacman2.png");
        Image pacmanImg2 = secondPacmanIcon.getImage();
        pacmanImages[1] = pacmanImg2.getScaledInstance(20,20,Image.SCALE_DEFAULT);

        ImageIcon thirdPacmanIcon = new ImageIcon("content/pacman3.png");
        Image pacmanImg3 = thirdPacmanIcon.getImage();
        pacmanImages[2] = pacmanImg3.getScaledInstance(20,20,Image.SCALE_DEFAULT);

    }

    public void update() {
        int newX = x + dirx;
        int newY = y + diry;

        if (newX < 0) {
            newX = boardSize.height - 1;
        } else if (newX >= boardSize.height) {
            newX = 0;
        }

        if (newY < 0) {
            newY = boardSize.width - 1;
        } else if (newY >= boardSize.width) {
            newY = 0;
        }

        if (isValidMove(newX, newY)) {
            x = newX;
            y = newY;
            currentImage = (currentImage + 1) % 3;
        } else {
            currentImage = 2;
        }
    }

    public void setDir(int dirx, int diry) {
        this.dirx = dirx;
        this.diry = diry;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    private BufferedImage toBufferImg(Image img) {
        MediaTracker tracker = new MediaTracker(new Component() {});
        tracker.addImage(img, 0);
        try {
            tracker.waitForID(0);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        BufferedImage bufferedImg = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics bufferedGr = bufferedImg.createGraphics();
        bufferedGr.drawImage(img, 0, 0, null);
        bufferedGr.dispose();

        return bufferedImg;
    }

    public Image getCurrentPacmanImg() {
        double rotation;
        boolean flipHorz = false;

        if (dirx == 1 && diry == 0) {
            rotation = 90;
        } else if (dirx == 0 && diry == 1) {
            rotation = 0;
        } else if (dirx == 0 && diry == -1) {
            rotation = 180;
            flipHorz = true;
        } else {
            rotation = -90;
        }

        BufferedImage bufferedImage = toBufferImg(pacmanImages[currentImage]);

        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        BufferedImage rotatedImage = new BufferedImage(width, height, bufferedImage.getType());

        AffineTransform tx = new AffineTransform();

        if (flipHorz) {
            tx.scale(-1,1);
            tx.translate(-width,0);
        } else {
            tx.rotate(rotation, width / 2.0, height / 2.0);
        }

        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        op.filter(bufferedImage, rotatedImage);

        return rotatedImage;
    }

    public boolean isValidMove(int nextX, int nextY) {
        return !gamePanel.isWall(nextX, nextY);

    }

    public void makeStrong(String upgradeName) {
        if (upgradeName == null) {
            return;
        }

        isStrong = true;
        this.upgradeName = upgradeName;

        switch (upgradeName) {
            case "speedboost":
                setSpeed(125);
                break;
            case "flash":
                setSpeed(75);
                break;
            case "life":
                gameFrame.addLife();
                break;
        }

        new Thread(() -> {
            try {
                Thread.sleep(8000);
                makeNormal();
            } catch (InterruptedException e) {

            }
        }).start();
    }

    public void makeNormal() {
        isStrong = false;
        this.upgradeName = null;
        setSpeed(175);
    }

    public boolean isStrong() {
        return isStrong;
    }

    public void respawn() {
        x = boardSize.height - 2;
        y = 1;
    }

    public void setSpeed(int newSpeed) {
        this.speed = newSpeed;
    }

    public int getSpeed() {
        return speed;
    }

}
