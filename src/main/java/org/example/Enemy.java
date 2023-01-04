package org.example;


import javax.swing.*;
import java.net.URL;
import java.util.Random;

public class Enemy {

    private int width = 500;

    private int height = 850;

    private int x = 0;
    private int y = 0;


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }



    URL enemyPath = GamePanel.class.getClassLoader().getResource("images/enemy1.png");

    ImageIcon enemyIcon = new ImageIcon(enemyPath);

    public ImageIcon getEnemyIcon() {
        return enemyIcon;
    }

    public void setEnemyIcon(ImageIcon enemyIcon) {
        this.enemyIcon = enemyIcon;
    }

    public Enemy() {
        this.width = enemyIcon.getIconWidth();
        this.height = enemyIcon.getIconHeight();

        Random random = new Random();
        this.x = random.nextInt(GameMain.width - this.width  );
        this.y = -random.nextInt(GameMain.height - this.height );
    }


    public void move() {
        this.y += 2;
    }
}
