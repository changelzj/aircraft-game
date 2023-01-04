package org.example;

import javax.swing.*;
import java.net.URL;

/**
 * 子弹
 */
public class Bullet {
    private int width = 0;
    private int height = 0;

    private int x = 0;
    private int y = 0;

    URL bulletPath = GamePanel.class.getClassLoader().getResource("images/bullet1.png");
    ImageIcon bulletIcon = new ImageIcon(bulletPath);

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

    public Bullet(int x, int y) {
        this.width = bulletIcon.getIconWidth();
        this.height = bulletIcon.getIconHeight();

        this.x = x;
        this.y = y;
    }

    public void move() {
        this.y -= 30;
    }
}
